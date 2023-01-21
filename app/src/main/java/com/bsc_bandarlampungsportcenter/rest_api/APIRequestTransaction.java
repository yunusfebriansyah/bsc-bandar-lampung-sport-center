package com.bsc_bandarlampungsportcenter.rest_api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIRequestTransaction {

  @GET("transactions/all/{id}")
  Call<ResponseModelTransaction> getAllTransactions(
      @Path("id") String id
  );

  @GET("transactions/denied-yesterday")
  Call<ResponseModelTransaction> deniedYesterday();

  @GET("transactions/{id}")
  Call<ResponseModelTransaction> getDetailData(
      @Path("id") String id
  );

  @FormUrlEncoded
  @POST("transactions/{id}?_method=PUT")
  Call<ResponseModelTransaction> update(
      @Path("id") String id,
      @Field("status") String status
  );

  @FormUrlEncoded
  @POST("transactions")
  Call<ResponseModelTransaction> booking(
      @Field("user_id") String user_id,
      @Field("field_id") String field_id,
      @Field("start_at") String start_at,
      @Field("end_at") String end_at,
      @Field("long_of_booking") String long_of_booking,
      @Field("price_perhour") String price_perhour,
      @Field("price") String price,
      @Field("discon") String discon,
      @Field("price_total") String price_total
  );


}
