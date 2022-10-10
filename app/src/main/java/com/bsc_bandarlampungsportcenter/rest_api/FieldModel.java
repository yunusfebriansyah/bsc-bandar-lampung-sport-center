package com.bsc_bandarlampungsportcenter.rest_api;

public class FieldModel {
  int id;
  String name, photo, photo_360, description;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPhoto() {
    return photo;
  }

  public void setPhoto(String photo) {
    this.photo = photo;
  }

  public String getPhoto_360() {
    return photo_360;
  }

  public void setPhoto_360(String photo_360) {
    this.photo_360 = photo_360;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
