package com.bsc_bandarlampungsportcenter.rest_api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface APIRequestPrice {

  @GET("price")
  Call<ResponseModelPrice> tampilData();

  @GET("price/{day}")
  Call<ResponseModelPrice> getPriceToday(
      @Path("day") String day
  );

}
