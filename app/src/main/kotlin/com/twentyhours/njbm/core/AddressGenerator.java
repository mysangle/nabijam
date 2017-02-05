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
}
