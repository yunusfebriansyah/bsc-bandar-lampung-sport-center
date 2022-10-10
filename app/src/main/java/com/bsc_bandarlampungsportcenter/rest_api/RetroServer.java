package com.bsc_bandarlampungsportcenter.rest_api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroServer {

  private static final String BASE_URL_API = "http://10.0.2.2:8000/api/";
  private static final String BASE_URL = "http://10.0.2.2:8000/";
  private static final String BASE_URL_FILE = "http://10.0.2.2:8000/storage/";
  private static String userId, isAdmin;

  public static String getIsAdmin() {
    return isAdmin;
  }

  public static void setIsAdmin(String isAdmin) {
    RetroServer.isAdmin = isAdmin;
  }

  public static String getUserId() {
    return userId;
  }

  public static void setUserId(String userId) {
    RetroServer.userId = userId;
  }

  public static String getBASE_URL() { return BASE_URL; }
  public static String getBASE_URL_FILE() { return BASE_URL_FILE; }

  private static Retrofit retro;

  public static Retrofit konekRetrofit () {

    if( retro == null ){
      retro = new Retrofit.Builder()
              .baseUrl(BASE_URL_API)
              .addConverterFactory(GsonConverterFactory.create())
              .build();
    }
    return retro;
  }

}
