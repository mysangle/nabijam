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
 * A Sha256Hash just wraps a byte[] so that equals and hashcode work correctly, allowing it to be used as keys in a
 * map. It also checks that the length is correct and provides a bit more type safety.
 */

public class Sha256Hash {
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
   * Returns a new SHA-256 MessageDigest instance.
   *
   * This is a convenience method which wraps the checked
   * exception that can never occur with a RuntimeException.
   *
   * @return a new SHA-256 MessageDigest instance
   */
  public static MessageDigest newDigest() {
    try {
      return MessageDigest.getInstance("SHA-256");
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);  // Can't happen.
    }
  }
}
