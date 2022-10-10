package com.bsc_bandarlampungsportcenter.rest_api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIRequestField {

  @GET("fields")
  Call<ResponseModelField> tampilData();

  @GET("fields/{id}")
  Call<ResponseModelField> detailData(
      @Path("id") String id
  );

  @FormUrlEncoded
  @POST("login")
  Call<ResponseModelField> login(
      @Field("nis") String nis,
      @Field("password") String password
  );

  @FormUrlEncoded
  @HTTP(method = "PUT", path = "ChangePassword", hasBody = true)
  Call<ResponseModelField> changePassword(
      @Field("idsiswa") String idsiswa,
      @Field("password") String password,
      @Field("new_password") String new_password
  );

  @FormUrlEncoded
  @HTTP(method = "PUT", path = "siswa", hasBody = true)
  Call<ResponseModelField> changeIdentity(
      @Field("idsiswa") String idsiswa,
      @Field("nama") String nama,
      @Field("tmp_lhr") String tmp_lhr,
      @Field("tgl_lhr") String tgl_lhr,
      @Field("jk") String jk,
      @Field("hobi") String hobi,
      @Field("citacita") String citacita,
      @Field("anak_ke") String anak_ke,
      @Field("jml_sdr") String jml_sdr,
      @Field("alamat") String alamat,
      @Field("nik_ayah") String nik_ayah,
      @Field("nama_ayah") String nama_ayah,
      @Field("pend_ayah") String pend_ayah,
      @Field("pekr_ayah") String pekr_ayah,
      @Field("nik_ibu") String nik_ibu,
      @Field("nama_ibu") String nama_ibu,
      @Field("pend_ibu") String pend_ibu,
      @Field("pekr_ibu") String pekr_ibu,
      @Field("alamat_ortu") String alamat_ortu
  );

}
