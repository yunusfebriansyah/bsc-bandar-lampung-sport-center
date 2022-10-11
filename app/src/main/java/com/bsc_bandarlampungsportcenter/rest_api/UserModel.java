package com.bsc_bandarlampungsportcenter.rest_api;

public class UserModel {
  int id, is_admin, count_success, count_pending, count_failed;
  String name, username, email, photo;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getIs_admin() {
    return is_admin;
  }

  public void setIs_admin(int is_admin) {
    this.is_admin = is_admin;
  }

  public int getCount_success() {
    return count_success;
  }

  public void setCount_success(int count_success) {
    this.count_success = count_success;
  }

  public int getCount_pending() {
    return count_pending;
  }

  public void setCount_pending(int count_pending) {
    this.count_pending = count_pending;
  }

  public int getCount_failed() {
    return count_failed;
  }

  public void setCount_failed(int count_failed) {
    this.count_failed = count_failed;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhoto() {
    return photo;
  }

  public void setPhoto(String photo) {
    this.photo = photo;
  }
}
