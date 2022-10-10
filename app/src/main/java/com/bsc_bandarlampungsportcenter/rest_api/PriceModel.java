package com.bsc_bandarlampungsportcenter.rest_api;

public class PriceModel {
  int id, price;
  String day, englishDay;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getPrice() {
    return price;
  }

  public void setPrice(int price) {
    this.price = price;
  }

  public String getDay() {
    return day;
  }

  public void setDay(String day) {
    this.day = day;
  }

  public String getEnglishDay() {
    return englishDay;
  }

  public void setEnglishDay(String englishDay) {
    this.englishDay = englishDay;
  }
}
