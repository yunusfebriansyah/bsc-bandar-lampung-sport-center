package com.bsc_bandarlampungsportcenter.rest_api;

import java.util.List;

public class ResponseModelUser {

  private List<UserModel> data;

  private List<ErrorModel> errors;

  private int status;

  private String message;

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public List<UserModel> getData() {
    return data;
  }

  public void setData(List<UserModel> data) {
    this.data = data;
  }

  public List<ErrorModel> getErrors() {
    return errors;
  }

  public void setErrors(List<ErrorModel> errors) {
    this.errors = errors;
  }
}
