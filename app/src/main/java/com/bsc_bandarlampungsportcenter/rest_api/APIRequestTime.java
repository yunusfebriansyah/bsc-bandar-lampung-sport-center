package com.bsc_bandarlampungsportcenter.rest_api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIRequestTime {

  @GET("transactions/available/{field}")
  Call<ResponseModelTime> getTimes(
      @Path("field") String field_id
  );

  @GET("transactions/available")
  Call<ResponseModelTime> getEndTimes(
      @Query("field_id") String field_id,
      @Query("start_at") String start_at
  );


}
