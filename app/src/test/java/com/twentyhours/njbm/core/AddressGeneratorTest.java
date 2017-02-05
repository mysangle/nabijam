package com.twentyhours.njbm.core;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by soonhyung on 2/5/17.
 */

public class AddressGeneratorTest {
  private byte[] privateKey = {0x0C, 0x28, (byte)0xFC, (byte)0xA3, (byte)0x86, (byte)0xC7, (byte)0xA2, 0x27,
      0x60, 0x0B, 0x2F, (byte)0xE5, 0x0B, 0x7C, (byte)0xAE, 0x11,
      (byte)0xEC, (byte)0x86, (byte)0xD3, (byte)0xBF, 0x1F, (byte)0xBE, 0x47, 0x1B,
      (byte)0xE8, (byte)0x98, 0x27, (byte)0xE1, (byte)0x9D, 0x72, (byte)0xAA, 0x1D};
  private String wifString = "5HueCGU8rMjxEXxiPuD5BDku4MkFqeZyd4dZ1jvhTVqvbTLvyTJ";

  @Test
  public void testEncodingToWIF() {
    String encoded = AddressGenerator.encodeToWIF(privateKey);

    assertEquals(wifString, encoded);
  }

  @Test
  public void testDecodingFromWIF() {
    byte[] decoded = AddressGenerator.decodeFromWIF(wifString);

    assertTrue(Arrays.equals(privateKey, decoded));
  }
}
