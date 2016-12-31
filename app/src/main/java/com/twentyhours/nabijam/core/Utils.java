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

package com.twentyhours.nabijam.core;

import org.spongycastle.crypto.digests.RIPEMD160Digest;

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
}
