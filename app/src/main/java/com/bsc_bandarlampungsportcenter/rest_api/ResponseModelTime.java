package com.bsc_bandarlampungsportcenter.rest_api;

import java.util.List;

public class ResponseModelTime {

  private int status;
  private String message;
  private List<ErrorModel> errors;
  private String text_start_at[], text_end_at[];
  private int start_at[], end_at[];
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

  public int[] getStart_at() {
    return start_at;
  }

  public void setStart_at(int[] start_at) {
    this.start_at = start_at;
  }

  public int[] getEnd_at() {
    return end_at;
  }

  public void setEnd_at(int[] end_at) {
    this.end_at = end_at;
  }

  public String[] getText_start_at() {
    return text_start_at;
  }

  public void setText_start_at(String[] text_start_at) {
    this.text_start_at = text_start_at;
  }

  public String[] getText_end_at() {
    return text_end_at;
  }

  public void setText_end_at(String[] text_end_at) {
    this.text_end_at = text_end_at;
  }

  public boolean isIs_available() {
    return is_available;
  }

  public void setIs_available(boolean is_available) {
    this.is_available = is_available;
  }
}
