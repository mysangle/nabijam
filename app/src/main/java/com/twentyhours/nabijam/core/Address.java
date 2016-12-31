package com.twentyhours.nabijam.core;

/**
 * Created by soonhyung-imac on 8/29/16.
 */

public class Address {
  private ECKey ecKey;

  private final String nickname;

  public static Address create(String nickname) {
    return new Address(nickname);
  }

  private Address(String nickname) {
    this.ecKey = new ECKey();
    this.nickname = nickname;
  }

  public void regenerate() {
    ecKey = new ECKey();
  }

  public String getNickname() {
    return nickname;
  }

  public String getAddress() {
    return Utils.bytesToHex(ecKey.getPubKeyHash());
  }
}
