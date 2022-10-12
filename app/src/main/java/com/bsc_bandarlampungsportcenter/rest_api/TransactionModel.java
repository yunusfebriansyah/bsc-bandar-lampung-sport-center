package com.bsc_bandarlampungsportcenter.rest_api;

public class TransactionModel {
  int id, user_id, field_id, start_at, end_at, long_of_booking, price_perhour, price, discon, price_total;
  String transaction_code, status, created_at, user_name, user_email, field_name, field_photo, booking_date;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getUser_id() {
    return user_id;
  }

  public void setUser_id(int user_id) {
    this.user_id = user_id;
  }

  public int getField_id() {
    return field_id;
  }

  public void setField_id(int field_id) {
    this.field_id = field_id;
  }

  public int getStart_at() {
    return start_at;
  }

  public void setStart_at(int start_at) {
    this.start_at = start_at;
  }

  public int getEnd_at() {
    return end_at;
  }

  public void setEnd_at(int end_at) {
    this.end_at = end_at;
  }

  public int getPrice_perhour() {
    return price_perhour;
  }

  public void setPrice_perhour(int price_perhour) {
    this.price_perhour = price_perhour;
  }

  public int getLong_of_booking() {
    return long_of_booking;
  }

  public void setLong_of_booking(int long_of_booking) {
    this.long_of_booking = long_of_booking;
  }

  public int getPrice() {
    return price;
  }

  public void setPrice(int price) {
    this.price = price;
  }

  public int getDiscon() {
    return discon;
  }

  public void setDiscon(int discon) {
    this.discon = discon;
  }

  public int getPrice_total() {
    return price_total;
  }

  public void setPrice_total(int price_total) {
    this.price_total = price_total;
  }

  public String getTransaction_code() {
    return transaction_code;
  }

  public void setTransaction_code(String transaction_code) {
    this.transaction_code = transaction_code;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getCreated_at() {
    return created_at;
  }

  public void setCreated_at(String created_at) {
    this.created_at = created_at;
  }

  public String getUser_name() {
    return user_name;
  }

  public void setUser_name(String user_name) {
    this.user_name = user_name;
  }

  public String getUser_email() {
    return user_email;
  }

  public void setUser_email(String user_email) {
    this.user_email = user_email;
  }

  public String getField_name() {
    return field_name;
  }

  public void setField_name(String field_name) {
    this.field_name = field_name;
  }

  public String getField_photo() {
    return field_photo;
  }

  public void setField_photo(String field_photo) {
    this.field_photo = field_photo;
  }

  public String getBooking_date() {
    return booking_date;
  }

  public void setBooking_date(String booking_date) {
    this.booking_date = booking_date;
  }
}
