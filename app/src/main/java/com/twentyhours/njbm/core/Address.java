package com.twentyhours.njbm.core;

/**
 * Created by soonhyung-imac on 8/29/16.
 */

public class Address {
  private byte[] addressBytes;

  public Address(int addressVersionNumber, int streamNumber, byte[] ripe) {
    // version number + stream number + ripe + checksum
    byte[] encodedVersionNumber = encodingVarInt(addressVersionNumber);
    byte[] encodedStreamNumber = encodingVarInt(streamNumber);
    addressBytes = new byte[encodedVersionNumber.length + encodedStreamNumber.length + ripe.length + 4];
    int length = 0;
    System.arraycopy(encodedVersionNumber, 0, addressBytes, length, encodedVersionNumber.length);
    length = encodedVersionNumber.length;
    System.arraycopy(encodedStreamNumber, 0, addressBytes, length, encodedStreamNumber.length);
    length += encodedStreamNumber.length;
    System.arraycopy(ripe, 0, addressBytes, length, ripe.length);
    length += ripe.length;
    byte[] sha512Hash = Sha512Hash.hashTwice(addressBytes, 0, addressBytes.length - 4);
    byte[] checksum = getChecksum(sha512Hash);
    System.arraycopy(checksum, 0, addressBytes, length, 4);
  }

  public String toBase58() {
    return Base58.encode(addressBytes);
  }

  private byte[] getChecksum(byte[] sha512Hash) {
    byte[] checksum = new byte[4];
    checksum[0] = sha512Hash[0];
    checksum[1] = sha512Hash[1];
    checksum[2] = sha512Hash[2];
    checksum[3] = sha512Hash[3];
    return checksum;
  }

  private byte[] encodingVarInt(int integer) {
    byte[] encoded;
    if (integer < 0xfd) {
      encoded = new byte[1];
      encoded[0] = (byte)integer;
    } else if (integer < 0xffff) {
      encoded = new byte[3];
      encoded[0] = (byte)0xfd;
      encoded[1] = (byte)(integer >> 8);
      encoded[2] = (byte)(integer >> 0);
    } else { //if (integer < 0xffffffff) {
      encoded = new byte[5];
      encoded[0] = (byte)0xfe;
      encoded[1] = (byte)(integer >> 24);
      encoded[2] = (byte)(integer >> 16);
      encoded[3] = (byte)(integer >> 8);
      encoded[4] = (byte)(integer >> 0);
    }
    return encoded;
  }

  private byte[] encodingVarInt(long integer) {
    byte[] encoded = new byte[9];
    encoded[0] = (byte)0xff;
    encoded[1] = (byte)(integer >> 56);
    encoded[2] = (byte)(integer >> 48);
    encoded[3] = (byte)(integer >> 40);
    encoded[4] = (byte)(integer >> 32);
    encoded[5] = (byte)(integer >> 24);
    encoded[6] = (byte)(integer >> 16);
    encoded[7] = (byte)(integer >> 8);
    encoded[8] = (byte)(integer >> 0);
    return encoded;
  }
}
