package com.test.exception.excel;

import com.test.exception.config.ApiException;

public class ExtentionNotSupportedException extends ApiException {

  public ExtentionNotSupportedException(String message, Throwable cause) {
    super(message, cause);
  }

  public ExtentionNotSupportedException(String message) {
    super(message);
  }

  public ExtentionNotSupportedException() {
    super("the extention is not supported");

  }

}
