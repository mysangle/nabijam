/*
 * Copyright by the original author or authors.
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

package com.twentyhours.nabijam.crypto;

import android.support.annotation.Nullable;

import com.twentyhours.njbm.core.Utils;

import org.spongycastle.math.ec.ECCurve;
import org.spongycastle.math.ec.ECPoint;

import java.util.Arrays;

/**
 * A wrapper around ECPoint that delays decoding of the point for as long as possible. This is useful because point
 * encode/decode in Bouncy Castle is quite slow especially on Dalvik, as it often involves decompression/recompression.
 */
public class LazyECPoint {
  // If curve is set, bits is also set. If curve is unset, point is set and bits is unset. Point can be set along
  // with curve and bits when the cached form has been accessed and thus must have been converted.

  private final ECCurve curve;
  private final byte[] bits;

  // This field is effectively final - once set it won't change again. However it can be set after
  // construction.
  @Nullable
  private ECPoint point;

  public LazyECPoint(ECCurve curve, byte[] bits) {
    this.curve = curve;
    this.bits = bits;
  }

  public LazyECPoint(ECPoint point) {
    this.point = Utils.checkNotNull(point);
    this.curve = null;
    this.bits = null;
  }

  public byte[] getEncoded() {
    if (bits != null)
      return Arrays.copyOf(bits, bits.length);
    else
      return get().getEncoded();
  }

  public ECPoint get() {
    if (point == null)
      point = curve.decodePoint(bits);
    return point;
  }
}
