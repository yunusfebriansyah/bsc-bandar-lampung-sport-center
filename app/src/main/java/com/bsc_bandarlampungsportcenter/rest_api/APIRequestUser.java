package com.bsc_bandarlampungsportcenter.rest_api;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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

  @FormUrlEncoded
  @POST("user/change-password/{id}?_method=PUT")
  Call<ResponseModelUser> changePassword(
      @Path("id") String id,
      @Field("password") String oldPassword,
      @Field("new_password") String newPassword,
      @Field("confirm_password") String confirmPassword
  );

  @Multipart
  @POST("user/edit-photo/{id}?_method=PUT")
  Call<ResponseModelUser> editPhoto(
      @Path("id") String id,
      @Part MultipartBody.Part photo
  );

}
