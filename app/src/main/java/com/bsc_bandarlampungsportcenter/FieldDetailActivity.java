package com.bsc_bandarlampungsportcenter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bsc_bandarlampungsportcenter.rest_api.APIRequestField;
import com.bsc_bandarlampungsportcenter.rest_api.APIRequestTime;
import com.bsc_bandarlampungsportcenter.rest_api.FieldAdapter;
import com.bsc_bandarlampungsportcenter.rest_api.FieldModel;
import com.bsc_bandarlampungsportcenter.rest_api.ResponseModelField;
import com.bsc_bandarlampungsportcenter.rest_api.ResponseModelTime;
import com.bsc_bandarlampungsportcenter.rest_api.RetroServer;
import com.bsc_bandarlampungsportcenter.session.Price;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FieldDetailActivity extends AppCompatActivity {

  Intent intent;
  Bundle bundle;
  ImageView photo;
  TextView txtId, txtName, txtDescription, txtPhoto360, txtNotAvailable;
  Button btnSee360, btnBookingNow;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_field_detail);
    getSupportActionBar().hide();

    photo = findViewById(R.id.photo);
    txtId = findViewById(R.id.id);
    txtPhoto360 = findViewById(R.id.photo_360);
    txtName = findViewById(R.id.name);
    txtDescription = findViewById(R.id.description);
    txtNotAvailable = findViewById(R.id.not_available);
    btnSee360 = findViewById(R.id.btn_see_360);
    btnBookingNow = findViewById(R.id.btn_booking_now);


    bundle = getIntent().getExtras();
    String id = String.valueOf(bundle.get("id"));
    if( !id.isEmpty() ) {
      tampilData(id);
      getTime(id);
    }


    btnBookingNow.setOnClickListener(view -> {
      intent = new Intent(FieldDetailActivity.this, FieldBookingActivity.class);
      intent.putExtra("id", id);
      startActivity(intent);
    });



  }

  void tampilData (String id)
  {
    //  deklarasi variabel komponen "Progress Dialog"
    ProgressDialog pd;
    //  setup progress dialog
    pd = new ProgressDialog(FieldDetailActivity.this);
    //  progress dialog tidak dapat di cancel
    pd.setCancelable(false);
    //  isi teks progress dialog
    pd.setMessage("Mohon Tunggu ...");
    //  tampilkan progress dialog
    pd.show();

    APIRequestField ardData = RetroServer.konekRetrofit().create(APIRequestField.class);
    Call<ResponseModelField> detailData = ardData.detailData(id);

    //  deskripsi isi variabel "cl"
    detailData.enqueue(new Callback<ResponseModelField>() {
      //  ketika data berhasil diambil
      @Override
      public void onResponse(Call<ResponseModelField> call, Response<ResponseModelField> response) {
        //  tampilkan data ke dalam list
        FieldModel data = response.body().getData().get(0);
        Picasso.get().load(RetroServer.getBASE_URL_FILE() + String.valueOf(data.getPhoto())).into(photo);
        txtId.setText(String.valueOf(data.getId()));
        txtPhoto360.setText(RetroServer.getBASE_URL_FILE() + data.getPhoto_360());
        txtName.setText(String.valueOf(data.getName()));
        txtDescription.setText(String.valueOf(data.getDescription()));

        //  tutup progress dialog
        pd.dismiss();
      }
      //  ketika data gagal diambil
      @Override
      public void onFailure(Call<ResponseModelField> call, Throwable t) {
        //  hilangkan progress dialog
        pd.dismiss();
        //  tampilkan pesan
        Toast.makeText(FieldDetailActivity.this, "Data gagal ditampilkan!" + t.getMessage(), Toast.LENGTH_SHORT).show();

      }
    });
  }

  void getTime (String id)
  {
    //  deklarasi variabel komponen "Progress Dialog"
    ProgressDialog pd;
    //  setup progress dialog
    pd = new ProgressDialog(FieldDetailActivity.this);
    //  progress dialog tidak dapat di cancel
    pd.setCancelable(false);
    //  isi teks progress dialog
    pd.setMessage("Mohon Tunggu ...");
    //  tampilkan progress dialog
    pd.show();

    APIRequestTime ardData = RetroServer.konekRetrofit().create(APIRequestTime.class);
    Call<ResponseModelTime> detailData = ardData.getTimes(id);

    //  deskripsi isi variabel "cl"
    detailData.enqueue(new Callback<ResponseModelTime>() {
      //  ketika data berhasil diambil
      @Override
      public void onResponse(Call<ResponseModelTime> call, Response<ResponseModelTime> response) {
        //  tampilkan data ke dalam list
        boolean isAvailable = response.body().isIs_available();
        if( isAvailable ) {
          btnBookingNow.setVisibility(View.VISIBLE);
        }else{
          txtNotAvailable.setVisibility(View.VISIBLE);
        }

        //  tutup progress dialog
        pd.dismiss();
      }
      //  ketika data gagal diambil
      @Override
      public void onFailure(Call<ResponseModelTime> call, Throwable t) {
        //  hilangkan progress dialog
        pd.dismiss();
        //  tampilkan pesan
        Toast.makeText(FieldDetailActivity.this, "Data gagal diproses!" + t.getMessage(), Toast.LENGTH_SHORT).show();

      }
    });
  }

}