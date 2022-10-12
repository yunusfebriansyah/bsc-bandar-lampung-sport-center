package com.bsc_bandarlampungsportcenter.rest_api;

import java.util.List;

public class ResponseModelTransaction {

  private List<TransactionModel> data;

  private List<ErrorModel> errors;

  private int status, count;

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

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public List<TransactionModel> getData() {
    return data;
  }

  public void setData(List<TransactionModel> data) {
    this.data = data;
  }

  public List<ErrorModel> getErrors() {
    return errors;
  }

  public void setErrors(List<ErrorModel> errors) {
    this.errors = errors;
  }
}
