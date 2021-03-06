/*
 * Copyright 2011 Google Inc.
 * Copyright 2014 Andreas Schildbach
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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * A Sha512Hash just wraps a byte[] so that equals and hashcode work correctly, allowing it to be used as keys in a
 * map. It also checks that the length is correct and provides a bit more type safety.
 */

public class Sha512Hash {
  public static final int LENGTH = 32; // bytes
  public static final Sha512Hash ZERO_HASH = wrap(new byte[LENGTH]);

  private final byte[] bytes;

  private Sha512Hash(byte[] rawHashBytes) {
    Utils.checkArgument(rawHashBytes.length == LENGTH);
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
   * Calculates the SHA-256 hash of the given bytes,
   * and then hashes the resulting hash again.
   *
   * @param input the bytes to hash
   * @return the double-hash (in big-endian order)
   */
  public static byte[] hashTwice(byte[] input) {
    return hashTwice(input, 0, input.length);
  }

  /**
   * Calculates the SHA-256 hash of the given byte range,
   * and then hashes the resulting hash again.
   *
   * @param input the array containing the bytes to hash
   * @param offset the offset within the array of the bytes to hash
   * @param length the number of bytes to hash
   * @return the double-hash (in big-endian order)
   */
  public static byte[] hashTwice(byte[] input, int offset, int length) {
    MessageDigest digest = newDigest();
    digest.update(input, offset, length);
    return digest.digest(digest.digest());
  }

  /**
   * Calculates the hash of hash on the given byte ranges. This is equivalent to
   * concatenating the two ranges and then passing the result to {@link #hashTwice(byte[])}.
   */
  public static byte[] hashTwice(byte[] input1, int offset1, int length1,
                                 byte[] input2, int offset2, int length2) {
    MessageDigest digest = newDigest();
    digest.update(input1, offset1, length1);
    digest.update(input2, offset2, length2);
    return digest.digest(digest.digest());
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
