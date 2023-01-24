package com.bsc_bandarlampungsportcenter.rest_api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroServer {

//  private static final String BASE_URL = "http://10.0.2.2:8000/";
  private static final String BASE_URL = "http://rentalfutsal.skripsi.tech/";
  private static final String BASE_URL_API = BASE_URL + "api/";
  private static final String BASE_URL_FILE = BASE_URL + "storage/";

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
