package com.bsc_bandarlampungsportcenter.session;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.bsc_bandarlampungsportcenter.FieldDetailActivity;
import com.bsc_bandarlampungsportcenter.rest_api.APIRequestPrice;
import com.bsc_bandarlampungsportcenter.rest_api.PriceModel;
import com.bsc_bandarlampungsportcenter.rest_api.ResponseModelPrice;
import com.bsc_bandarlampungsportcenter.rest_api.RetroServer;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Price {

  private static int priceToday = 0;

  public static int getPrice() {
    return priceToday;
  }

  public static String getPriceMoney()
  {
    String money = "Rp. " + NumberFormat.getInstance().format(priceToday);
    return money;
  }

  public static void setPrice(Context ctx) {
    //  deklarasi variabel komponen "Progress Dialog"
    ProgressDialog pd;
    //  setup progress dialog
    pd = new ProgressDialog(ctx);
    //  progress dialog tidak dapat di cancel
    pd.setCancelable(false);
    //  isi teks progress dialog
    pd.setMessage("Mohon Tunggu ...");
    //  tampilkan progress dialog
    pd.show();
    APIRequestPrice ardData = RetroServer.konekRetrofit().create(APIRequestPrice.class);
    SimpleDateFormat formatter = new SimpleDateFormat("u");
    Date date = new Date();
    String day = formatter.format(date);

    Call<ResponseModelPrice> priceToday = ardData.getPriceToday(day);
//
    //  deskripsi isi variabel "cl"
    priceToday.enqueue(new Callback<ResponseModelPrice>() {
      //  ketika data berhasil diambil
      @Override
      public void onResponse(Call<ResponseModelPrice> call, Response<ResponseModelPrice> response) {
        //  tampilkan data ke dalam list
        PriceModel data = response.body().getData().get(0);
        Price.priceToday = data.getPrice();
        pd.dismiss();
      }
      @Override
      public void onFailure(Call<ResponseModelPrice> call, Throwable t) {
        //  tampilkan pesan
        Log.e("error", t.getMessage());
        pd.dismiss();
      }
    });
  }
}
