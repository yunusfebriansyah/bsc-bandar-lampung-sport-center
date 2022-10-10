package com.bsc_bandarlampungsportcenter.rest_api;

import java.util.List;

public class ResponseModelField {

  private List<FieldModel> data;

  private int count, status;

  private String message;

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

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

  public List<FieldModel> getData() {
    return data;
  }

  public void setData(List<FieldModel> data) {
    this.data = data;
  }
}
