package com.bsc_bandarlampungsportcenter.rest_api;

public class ErrorModel {

  String name, username, email, password, new_password, confirm_password, is_admin, photo;

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

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getNew_password() {
    return new_password;
  }

  public void setNew_password(String new_password) {
    this.new_password = new_password;
  }

  public String getConfirm_password() {
    return confirm_password;
  }

  public void setConfirm_password(String confirm_password) {
    this.confirm_password = confirm_password;
  }

  public String getIs_admin() {
    return is_admin;
  }

  public void setIs_admin(String is_admin) {
    this.is_admin = is_admin;
  }

  public String getPhoto() {
    return photo;
  }

  public void setPhoto(String photo) {
    this.photo = photo;
  }
}
