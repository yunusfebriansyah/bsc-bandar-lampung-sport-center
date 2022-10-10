package com.bsc_bandarlampungsportcenter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bsc_bandarlampungsportcenter.rest_api.APIRequestField;
import com.bsc_bandarlampungsportcenter.rest_api.FieldAdapter;
import com.bsc_bandarlampungsportcenter.rest_api.FieldModel;
import com.bsc_bandarlampungsportcenter.rest_api.ResponseModelField;
import com.bsc_bandarlampungsportcenter.rest_api.RetroServer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FieldDetailActivity extends AppCompatActivity {

  Intent intent;
  Bundle bundle;
  ImageView photo;
  TextView txtId, txtName, txtDescription, txtPhoto360;

  List<FieldModel> list_field = new ArrayList<>();

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

    bundle = getIntent().getExtras();
    String id = String.valueOf(bundle.get("id"));
    if( !id.isEmpty() ) {
      tampilData(id);
    }



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
        list_field = response.body().getData();
        FieldModel data = list_field.get(0);
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

}