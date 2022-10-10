package com.bsc_bandarlampungsportcenter.rest_api;

import java.util.List;

public class ResponseModelPrice {

  private List<PriceModel> data;

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

  public List<PriceModel> getData() {
    return data;
  }

  public void setData(List<PriceModel> data) {
    this.data = data;
  }
}
