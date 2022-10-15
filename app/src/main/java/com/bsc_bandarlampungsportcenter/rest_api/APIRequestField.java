package com.bsc_bandarlampungsportcenter.rest_api;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIRequestField {

  @GET("fields")
  Call<ResponseModelField> tampilData();

  @GET("fields/{id}")
  Call<ResponseModelField> detailData(
      @Path("id") String id
  );

  @Multipart
  @POST("fields")
  Call<ResponseModelField> addField(
      @Part("name") RequestBody name,
      @Part("description") RequestBody description,
      @Part MultipartBody.Part photo,
      @Part MultipartBody.Part photo_360
  );



}
