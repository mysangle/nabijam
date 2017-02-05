package com.twentyhours.njbm.core;

import com.twentyhours.njbm.exception.WalletImportFormatException;

/**
 * Created by soonhyung on 2/5/17.
 */

public class WalletImportFormat {
  /**
   * encode private keyto WIF
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
   * decode WIF to private key
   */
  public static byte[] decodeFromWIF(String wifString) throws WalletImportFormatException {
    byte[] base58Decode = Base58.decode(wifString);
    // checksum checking
    byte[] doubleHash = Sha256Hash.hashTwice(base58Decode, 0, base58Decode.length - 4);
    byte[] checksum = Utils.checksum(doubleHash);
    for (int i = 0; i < 4; i++) {
      if (checksum[i] != base58Decode[base58Decode.length - 4 + i]) {
        throw new WalletImportFormatException();
      }
    }
    byte[] decoded = new byte[base58Decode.length - 5];
    System.arraycopy(base58Decode, 1, decoded, 0, base58Decode.length - 5);
    return decoded;
  }
}
