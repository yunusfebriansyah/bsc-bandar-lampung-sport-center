package com.bsc_bandarlampungsportcenter.rest_api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIRequestUser {

  @GET("user/{id}")
  Call<ResponseModelUser> getUserDetail(
      @Path("id") String id
  );

  @FormUrlEncoded
  @POST("user/login")
  Call<ResponseModelUser> login(
      @Field("email") String email,
      @Field("password") String password
  );

  @FormUrlEncoded
  @POST("user/register")
  Call<ResponseModelUser> register(
      @Field("name") String name,
      @Field("username") String username,
      @Field("email") String email,
      @Field("password") String password
  );

  @FormUrlEncoded
  @POST("user/edit-profile/{id}?_method=PUT")
  Call<ResponseModelUser> editProfile(
      @Path("id") String id,
      @Field("name") String name,
      @Field("username") String username,
      @Field("email") String email
  );

}
