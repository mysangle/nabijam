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

import org.spongycastle.crypto.digests.RIPEMD160Digest;

import java.math.BigInteger;
import java.util.Date;

/**
 * A collection of various utility methods that are helpful for working with the Bitcoin protocol.
 * To enable debug logging from the library, run with -Dbitcoinj.logging=true on your command line.
 */
public class Utils {
  private static int isAndroid = -1;
  public static boolean isAndroidRuntime() {
    if (isAndroid == -1) {
      final String runtime = System.getProperty("java.runtime.name");
      isAndroid = (runtime != null && runtime.equals("Android Runtime")) ? 1 : 0;
    }
    return isAndroid == 1;
  }

  /**
   * If non-null, overrides the return value of now().
   */
  public static volatile Date mockTime;

  // TODO: Replace usages of this where the result is / 1000 with currentTimeSeconds.
  /** Returns the current time in milliseconds since the epoch, or a mocked out equivalent. */
  public static long currentTimeMillis() {
    return mockTime != null ? mockTime.getTime() : System.currentTimeMillis();
  }

  public static long currentTimeSeconds() {
    return currentTimeMillis() / 1000;
  }

  final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
  public static String bytesToHex(byte[] bytes) {
    char[] hexChars = new char[bytes.length * 2];
    for ( int j = 0; j < bytes.length; j++ ) {
      int v = bytes[j] & 0xFF;
      hexChars[j * 2] = hexArray[v >>> 4];
      hexChars[j * 2 + 1] = hexArray[v & 0x0F];
    }
    return new String(hexChars);
  }

  /**
   * Calculates RIPEMD160(SHA512(input)). This is used in Address calculations.
   */
  public static byte[] sha512hash160(byte[] input) {
    byte[] sha512 = Sha512Hash.hash(input);
    RIPEMD160Digest digest = new RIPEMD160Digest();
    digest.update(sha512, 0, sha512.length);
    byte[] out = new byte[20];
    digest.doFinal(out, 0);
    return out;
  }

  /**
   * Ensures the truth of an expression involving one or more parameters to the calling method.
   */
  public static void checkArgument(boolean expression) {
    if (!expression) {
      throw new IllegalArgumentException();
    }
  }

  public static <T> T checkNotNull(T object) {
    if (object == null) {
      throw new NullPointerException();
    }
    return object;
  }

  /** Parse 4 bytes from the byte array (starting at the offset) as unsigned 32-bit integer in little endian format. */
  public static long readUint32(byte[] bytes, int offset) {
    return (bytes[offset] & 0xffl) |
        ((bytes[offset + 1] & 0xffl) << 8) |
        ((bytes[offset + 2] & 0xffl) << 16) |
        ((bytes[offset + 3] & 0xffl) << 24);
  }

  /** Parse 8 bytes from the byte array (starting at the offset) as signed 64-bit integer in little endian format. */
  public static long readInt64(byte[] bytes, int offset) {
    return (bytes[offset] & 0xffl) |
        ((bytes[offset + 1] & 0xffl) << 8) |
        ((bytes[offset + 2] & 0xffl) << 16) |
        ((bytes[offset + 3] & 0xffl) << 24) |
        ((bytes[offset + 4] & 0xffl) << 32) |
        ((bytes[offset + 5] & 0xffl) << 40) |
        ((bytes[offset + 6] & 0xffl) << 48) |
        ((bytes[offset + 7] & 0xffl) << 56);
  }

  public static void uint32ToByteArrayLE(long val, byte[] out, int offset) {
    out[offset] = (byte) (0xFF & val);
    out[offset + 1] = (byte) (0xFF & (val >> 8));
    out[offset + 2] = (byte) (0xFF & (val >> 16));
    out[offset + 3] = (byte) (0xFF & (val >> 24));
  }

  public static void uint64ToByteArrayLE(long val, byte[] out, int offset) {
    out[offset] = (byte) (0xFF & val);
    out[offset + 1] = (byte) (0xFF & (val >> 8));
    out[offset + 2] = (byte) (0xFF & (val >> 16));
    out[offset + 3] = (byte) (0xFF & (val >> 24));
    out[offset + 4] = (byte) (0xFF & (val >> 32));
    out[offset + 5] = (byte) (0xFF & (val >> 40));
    out[offset + 6] = (byte) (0xFF & (val >> 48));
    out[offset + 7] = (byte) (0xFF & (val >> 56));
  }

  /**
   * The regular {@link java.math.BigInteger#toByteArray()} method isn't quite what we often need: it appends a
   * leading zero to indicate that the number is positive and may need padding.
   *
   * @param b the integer to format into a byte array
   * @param numBytes the desired size of the resulting byte array
   * @return numBytes byte long array.
   */
  public static byte[] bigIntegerToBytes(BigInteger b, int numBytes) {
    if (b == null) {
      return null;
    }
    byte[] bytes = new byte[numBytes];
    byte[] biBytes = b.toByteArray();
    int start = (biBytes.length == numBytes + 1) ? 1 : 0;
    int length = Math.min(biBytes.length, numBytes);
    System.arraycopy(biBytes, start, bytes, numBytes - length, length);
    return bytes;
  }

  public static byte[] checksum(byte[] bytes) {
    byte[] checksum = new byte[4];
    checksum[0] = bytes[0];
    checksum[1] = bytes[1];
    checksum[2] = bytes[2];
    checksum[3] = bytes[3];
    return checksum;
  }
}
