/*
 * Copyright 2011 Google Inc.
 * Copyright 2014 Andreas Schildbach
 * Copyright 2014-2016 the libsecp256k1 contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.twentyhours.njbm.core;

import android.support.annotation.Nullable;

import com.twentyhours.nabijam.crypto.LazyECPoint;
import com.twentyhours.nabijam.crypto.LinuxSecureRandom;

import org.spongycastle.asn1.x9.X9ECParameters;
import org.spongycastle.crypto.AsymmetricCipherKeyPair;
import org.spongycastle.crypto.ec.CustomNamedCurves;
import org.spongycastle.crypto.generators.ECKeyPairGenerator;
import org.spongycastle.crypto.params.ECDomainParameters;
import org.spongycastle.crypto.params.ECKeyGenerationParameters;
import org.spongycastle.crypto.params.ECPrivateKeyParameters;
import org.spongycastle.crypto.params.ECPublicKeyParameters;
import org.spongycastle.math.ec.ECPoint;
import org.spongycastle.math.ec.FixedPointCombMultiplier;
import org.spongycastle.math.ec.FixedPointUtil;

import java.math.BigInteger;
import java.security.SecureRandom;

import static com.twentyhours.njbm.core.Utils.checkArgument;

/**
 * <p>Represents an elliptic curve public and (optionally) private key, usable for digital signatures but not encryption.
 * Creating a new ECKey with the empty constructor will generate a new random keypair. Other static methods can be used
 * when you already have the public or private parts. If you create a key with only the public part, you can check
 * signatures but not create them.</p>
 *
 * <p>ECKey also provides access to Bitcoin Core compatible text message signing, as accessible via the UI or JSON-RPC.
 * This is slightly different to signing raw bytes - if you want to sign your own data and it won't be exposed as
 * text to people, you don't want to use this. If in doubt, ask on the mailing list.</p>
 *
 * <p>The ECDSA algorithm supports <i>key recovery</i> in which a signature plus a couple of discriminator bits can
 * be reversed to find the public key used to calculate it. This can be convenient when you have a message and a
 * signature and want to find out who signed it, rather than requiring the user to provide the expected identity.</p>
 *
 * <p>This class supports a variety of serialization forms. The methods that accept/return byte arrays serialize
 * private keys as raw byte arrays and public keys using the SEC standard byte encoding for public keys. Signatures
 * are encoded using ASN.1/DER inside the Bitcoin protocol.</p>
 *
 * <p>A key can be <i>compressed</i> or <i>uncompressed</i>. This refers to whether the public key is represented
 * when encoded into bytes as an (x, y) coordinate on the elliptic curve, or whether it's represented as just an X
 * co-ordinate and an extra byte that carries a sign bit. With the latter form the Y coordinate can be calculated
 * dynamically, however, <b>because the binary serialization is different the address of a key changes if its
 * compression status is changed</b>. If you deviate from the defaults it's important to understand this: money sent
 * to a compressed version of the key will have a different address to the same key in uncompressed form. Whether
 * a public key is compressed or not is recorded in the SEC binary serialisation format, and preserved in a flag in
 * this class so round-tripping preserves state. Unless you're working with old software or doing unusual things, you
 * can usually ignore the compressed/uncompressed distinction.</p>
 */
public class ECKey {
  // The parameters of the secp256k1 curve that Bitcoin uses.
  private static final X9ECParameters CURVE_PARAMS = CustomNamedCurves.getByName("secp256k1");

  /** The parameters of the secp256k1 curve that Bitcoin uses. */
  public static final ECDomainParameters CURVE;

  /**
   * Equal to CURVE.getN().shiftRight(1), used for canonicalising the S value of a signature. If you aren't
   * sure what this is about, you can ignore it.
   */
  public static final BigInteger HALF_CURVE_ORDER;

  private static final SecureRandom secureRandom;

  static {
    // Init proper random number generator, as some old Android installations have bugs that make it unsecure.
    if (Utils.isAndroidRuntime()) {
      new LinuxSecureRandom();
    }

    // Tell Bouncy Castle to precompute data that's needed during secp256k1 calculations. Increasing the width
    // number makes calculations faster, but at a cost of extra memory usage and with decreasing returns. 12 was
    // picked after consulting with the BC team.
    FixedPointUtil.precompute(CURVE_PARAMS.getG(), 12);
    CURVE = new ECDomainParameters(CURVE_PARAMS.getCurve(), CURVE_PARAMS.getG(), CURVE_PARAMS.getN(),
        CURVE_PARAMS.getH());
    HALF_CURVE_ORDER = CURVE_PARAMS.getN().shiftRight(1);
    secureRandom = new SecureRandom();
  }

  // The two parts of the key. If "priv" is set, "pub" can always be calculated. If "pub" is set but not "priv", we
  // can only verify signatures not make them.
  protected final BigInteger priv;  // A field element.
  protected final LazyECPoint pub;

  // Creation time of the key in seconds since the epoch, or zero if the key was deserialized from a version that did
  // not have this field.
  protected long creationTimeSeconds;

  private byte[] pubKeyHash;

  /**
   * Generates an entirely new keypair. Point compression is used so the resulting public key will be 33 bytes
   * (32 for the co-ordinate and 1 byte to represent the y bit).
   */
  public ECKey() {
    this(secureRandom);
  }

  /**
   * Generates an entirely new keypair with the given {@link SecureRandom} object. Point compression is used so the
   * resulting public key will be 33 bytes (32 for the co-ordinate and 1 byte to represent the y bit).
   */
  public ECKey(SecureRandom secureRandom) {
    ECKeyPairGenerator generator = new ECKeyPairGenerator();
    ECKeyGenerationParameters keygenParams = new ECKeyGenerationParameters(CURVE, secureRandom);
    generator.init(keygenParams);
    AsymmetricCipherKeyPair keypair = generator.generateKeyPair();
    ECPrivateKeyParameters privParams = (ECPrivateKeyParameters) keypair.getPrivate();
    ECPublicKeyParameters pubParams = (ECPublicKeyParameters) keypair.getPublic();
    priv = privParams.getD();
    pub = new LazyECPoint(CURVE.getCurve(), pubParams.getQ().getEncoded(true));
    creationTimeSeconds = Utils.currentTimeSeconds();
  }

  protected ECKey(@Nullable BigInteger priv, ECPoint pub) {
    if (priv != null) {
      // Try and catch buggy callers or bad key imports, etc. Zero and one are special because these are often
      // used as sentinel values and because scripting languages have a habit of auto-casting true and false to
      // 1 and 0 or vice-versa. Type confusion bugs could therefore result in private keys with these values.
      checkArgument(!priv.equals(BigInteger.ZERO));
      checkArgument(!priv.equals(BigInteger.ONE));
    }
    this.priv = priv;
    this.pub = new LazyECPoint(Utils.checkNotNull(pub));
  }

  /** Gets the hash160 form of the public key (as seen in addresses). */
  public byte[] getPubKeyHash() {
    if (pubKeyHash == null)
      pubKeyHash = Utils.sha512hash160(this.pub.getEncoded());
    return pubKeyHash;
  }

  public byte[] getPubKeyEncoded() {
    return pub.getEncoded();
  }

  /**
   * Returns a 32 byte array containing the private key.
   * @throws com.twentyhours.njbm.core.ECKey.MissingPrivateKeyException if the private key bytes are missing/encrypted.
   */
  public byte[] getPrivKeyBytes() {
    return Utils.bigIntegerToBytes(getPrivKey(), 32);
  }

  /**
   * Gets the private key in the form of an integer field element. The public key is derived by performing EC
   * point addition this number of times (i.e. point multiplying).
   *
   * @throws java.lang.IllegalStateException if the private key bytes are not available.
   */
  public BigInteger getPrivKey() {
    if (priv == null)
      throw new MissingPrivateKeyException();
    return priv;
  }

  /**
   * Creates an ECKey given the private key only. The public key is calculated from it (this is slow). The resulting
   * public key is compressed.
   */
  public static ECKey fromPrivate(byte[] privKeyBytes) {
    return fromPrivate(new BigInteger(1, privKeyBytes));
  }

  /**
   * Creates an ECKey given the private key only. The public key is calculated from it (this is slow). The resulting
   * public key is compressed.
   */
  public static ECKey fromPrivate(BigInteger privKey) {
    return fromPrivate(privKey, true);
  }

  /**
   * Creates an ECKey given the private key only. The public key is calculated from it (this is slow), either
   * compressed or not.
   */
  public static ECKey fromPrivate(BigInteger privKey, boolean compressed) {
    ECPoint point = publicPointFromPrivate(privKey);
    return new ECKey(privKey, getPointWithCompression(point, compressed));
  }

  /**
   * Returns public key point from the given private key. To convert a byte array into a BigInteger, use <tt>
   * new BigInteger(1, bytes);</tt>
   */
  public static ECPoint publicPointFromPrivate(BigInteger privKey) {
        /*
         * TODO: FixedPointCombMultiplier currently doesn't support scalars longer than the group order,
         * but that could change in future versions.
         */
    if (privKey.bitLength() > CURVE.getN().bitLength()) {
      privKey = privKey.mod(CURVE.getN());
    }
    return new FixedPointCombMultiplier().multiply(CURVE.getG(), privKey);
  }

  private static ECPoint getPointWithCompression(ECPoint point, boolean compressed) {
    if (point.isCompressed() == compressed)
      return point;
    point = point.normalize();
    BigInteger x = point.getAffineXCoord().toBigInteger();
    BigInteger y = point.getAffineYCoord().toBigInteger();
    return CURVE.getCurve().createPoint(x, y, compressed);
  }

  public static class MissingPrivateKeyException extends RuntimeException {
  }
}
