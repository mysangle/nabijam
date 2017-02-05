package com.twentyhours.njbm.exception;

/**
 * Created by soonhyung on 2/5/17.
 */

public class WalletImportFormatException extends IllegalArgumentException {
  public WalletImportFormatException() {
    super();
  }

  public WalletImportFormatException(String message) {
    super(message);
  }
}
