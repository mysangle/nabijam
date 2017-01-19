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
      System.out.println("ripe=" + Utils.bytesToHex(ripe));
    } while (ripe[0] != 0x00);

    byte[] privSigningKeyBytes = signingKey.getPrivKeyBytes();
    byte[] privEncryptionKeyBytes = encryptionKey.getPrivKeyBytes();

    byte[] strip = new byte[ripe.length - 1]; // strip '0x00' from ripe
    System.arraycopy(ripe, 1, strip, 0, ripe.length - 1);
    return new Address(4, 1, strip);
  }

  public static String walletImportFormat(byte[] bytes) {
    byte[] bytesWithPrefix = new byte[bytes.length + 1 + 4];
    bytesWithPrefix[0] = (byte) 0x80;
    System.arraycopy(bytes, 0, bytesWithPrefix, 1, bytes.length);
    byte[] doubleHash = Sha256Hash.hashTwice(bytesWithPrefix, 0, bytesWithPrefix.length - 4);
    byte[] checksum = Utils.checksum(doubleHash);
    System.arraycopy(checksum, 0, bytesWithPrefix, bytesWithPrefix.length - 4, 4);
    return Base58.encode(bytesWithPrefix);
  }
}
