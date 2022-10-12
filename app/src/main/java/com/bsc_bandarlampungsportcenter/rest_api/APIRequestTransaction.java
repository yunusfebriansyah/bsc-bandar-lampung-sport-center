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

  @GET("transactions/{id}")
  Call<ResponseModelTransaction> getDetailData(
          @Path("id") String id
  );

}
