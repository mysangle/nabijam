package com.twentyhours.njbm.core;

/**
 * Created by soonhyung on 1/10/17.
 */

public class AddressGenerator {
  /**
   * generate random address
   */
  public static Address generate() {
    ECKey signingKey;
    ECKey encryptionKey;
    byte[] ripe;
    do {
      signingKey = new ECKey();
      encryptionKey = new ECKey();
      byte[] signingPubKey = signingKey.getPubKeyEncoded();
      byte[] encryptionPubKey = encryptionKey.getPubKeyEncoded();
      int length = signingPubKey.length + encryptionPubKey.length;
      byte[] combined = new byte[length];
      System.arraycopy(signingPubKey, 0, combined, 0, signingPubKey.length);
      System.arraycopy(encryptionPubKey, 0, combined, signingPubKey.length, encryptionPubKey.length);
      ripe = Utils.sha512hash160(combined);
    } while (ripe[0] != 0x00);

    byte[] strip = new byte[ripe.length - 1]; // strip '0x00' from ripe
    System.arraycopy(ripe, 1, strip, 0, ripe.length - 1);

    Address address = new Address(4, 1, strip);
    address.setSigningKey(signingKey);
    address.setEncryptionKey(encryptionKey);

    return address;
  }

  /**
   * convert private key bytes as WIF
   */
  public static String encodeToWIF(byte[] privateKey) {
    byte[] bytesWithPrefix = new byte[privateKey.length + 1 + 4];
    bytesWithPrefix[0] = (byte) 0x80;
    System.arraycopy(privateKey, 0, bytesWithPrefix, 1, privateKey.length);
    byte[] doubleHash = Sha256Hash.hashTwice(bytesWithPrefix, 0, bytesWithPrefix.length - 4);
    byte[] checksum = Utils.checksum(doubleHash);
    System.arraycopy(checksum, 0, bytesWithPrefix, bytesWithPrefix.length - 4, 4);
    return Base58.encode(bytesWithPrefix);
  }

  /**
   * convert WIF to private key bytes
   */
  public static byte[] decodeFromWIF(String wifString) throws WIFFormatException {
    byte[] base58Decode = Base58.decode(wifString);
    // checksum checking
    byte[] doubleHash = Sha256Hash.hashTwice(base58Decode, 0, base58Decode.length - 4);
    byte[] checksum = Utils.checksum(doubleHash);
    for (int i = 0; i < 4; i++) {
      if (checksum[i] != base58Decode[base58Decode.length - 4 + i]) {
        throw new WIFFormatException();
      }
    }
    byte[] decoded = new byte[base58Decode.length - 5];
    System.arraycopy(base58Decode, 1, decoded, 0, base58Decode.length - 5);
    return decoded;
  }
}
