package com.twentyhours.nabijam.core;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.twentyhours.nabijam.core.Utils.checkArgument;

/**
 * Created by soonhyung-imac on 9/2/16.
 */

public class Sha512Hash {
  public static final int LENGTH = 32; // bytes
  public static final Sha512Hash ZERO_HASH = wrap(new byte[LENGTH]);

  private final byte[] bytes;

  private Sha512Hash(byte[] rawHashBytes) {
    checkArgument(rawHashBytes.length == LENGTH);
    this.bytes = rawHashBytes;
  }

  /**
   * Creates a new instance that wraps the given hash value.
   *
   * @param rawHashBytes the raw hash bytes to wrap
   * @return a new instance
   * @throws IllegalArgumentException if the given array length is not exactly 64
   */
  public static Sha512Hash wrap(byte[] rawHashBytes) {
    return new Sha512Hash(rawHashBytes);
  }

  /**
   * Calculates the SHA-512 hash of the given bytes.
   *
   * @param input the bytes to hash
   * @return the hash (in big-endian order)
   */
  public static byte[] hash(byte[] input) {
    return hash(input, 0, input.length);
  }

  /**
   * Calculates the SHA-512 hash of the given byte range.
   *
   * @param input the array containing the bytes to hash
   * @param offset the offset within the array of the bytes to hash
   * @param length the number of bytes to hash
   * @return the hash (in big-endian order)
   */
  public static byte[] hash(byte[] input, int offset, int length) {
    MessageDigest digest = newDigest();
    digest.update(input, offset, length);
    return digest.digest();
  }

  /**
   * Returns a new SHA-256 MessageDigest instance.
   *
   * This is a convenience method which wraps the checked
   * exception that can never occur with a RuntimeException.
   *
   * @return a new SHA-256 MessageDigest instance
   */
  public static MessageDigest newDigest() {
    try {
      return MessageDigest.getInstance("SHA-512");
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);  // Can't happen.
    }
  }
}
