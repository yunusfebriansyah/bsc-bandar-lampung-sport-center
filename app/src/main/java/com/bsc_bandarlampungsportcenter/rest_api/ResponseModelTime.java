package com.bsc_bandarlampungsportcenter.rest_api;

import java.util.List;

public class ResponseModelTime {

  private int status;
  private String message;
  private List<ErrorModel> errors;
  private String start_at[], end_at[];
  private boolean is_available;

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public List<ErrorModel> getErrors() {
    return errors;
  }

  public void setErrors(List<ErrorModel> errors) {
    this.errors = errors;
  }

  public String[] getStart_at() {
    return start_at;
  }

  public void setStart_at(String[] start_at) {
    this.start_at = start_at;
  }

  public String[] getEnd_at() {
    return end_at;
  }

  public void setEnd_at(String[] end_at) {
    this.end_at = end_at;
  }

  public boolean isIs_available() {
    return is_available;
  }

  public void setIs_available(boolean is_available) {
    this.is_available = is_available;
  }
}
