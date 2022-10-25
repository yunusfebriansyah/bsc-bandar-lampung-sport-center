package com.bsc_bandarlampungsportcenter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.bsc_bandarlampungsportcenter.session.User;
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
  TextView txtId, txtName, txtDescription, txtNotAvailable;
  Button btnSee360, btnBookingNow, btnDelete, btnChange;
  LinearLayout actionAdmin;

  String id, photo360;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_field_detail);
    getSupportActionBar().hide();

    photo = findViewById(R.id.photo);
    txtId = findViewById(R.id.id);
    txtName = findViewById(R.id.name);
    txtDescription = findViewById(R.id.description);
    txtNotAvailable = findViewById(R.id.not_available);
    btnSee360 = findViewById(R.id.btn_see_360);
    btnBookingNow = findViewById(R.id.btn_booking_now);
    actionAdmin = findViewById(R.id.action_admin);
    btnDelete = findViewById(R.id.btn_delete);
    btnChange = findViewById(R.id.btn_change);


    bundle = getIntent().getExtras();
    id = String.valueOf(bundle.get("id"));
    if( !id.isEmpty() ) {
      tampilData(id);
      getTime(id);
    }

    if(User.isAdmin() ) {
      actionAdmin.setVisibility(View.VISIBLE);
    }


    btnBookingNow.setOnClickListener(view -> {
      intent = new Intent(FieldDetailActivity.this, FieldBookingActivity.class);
      intent.putExtra("id", id);
      startActivity(intent);
    });

    btnSee360.setOnClickListener(view -> {
      intent = new Intent(Intent.ACTION_VIEW, Uri.parse(photo360));
      startActivity(intent);
    });

    btnDelete.setOnClickListener( view -> {
      AlertDialog.Builder confirm = new AlertDialog.Builder(view.getContext());
      final CharSequence[] confirmItem = {"Ya", "Batal"};
      confirm.setTitle("Yakin ingin menghapus lapangan?");
      confirm.setItems(confirmItem,(dialogConfirm, whichConfirm)->{
        switch (whichConfirm) {
          case 0:
            deleteField(id);
            break;
        }
      });
      confirm.create().show();
    });

    btnChange.setOnClickListener(view -> {
      intent = new Intent(FieldDetailActivity.this, ChangeFieldActivity.class);
      intent.putExtra("id", id);
      startActivity(intent);
    });

  }

  @Override
  protected void onResume() {
    tampilData(id);
    getTime(id);
    super.onResume();
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
        photo360 = data.getPhoto_360();
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

  void deleteField (String id)
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
    Call<ResponseModelField> deleteField = ardData.deleteField(id);

    //  deskripsi isi variabel "cl"
    deleteField.enqueue(new Callback<ResponseModelField>() {
      //  ketika data berhasil diambil
      @Override
      public void onResponse(Call<ResponseModelField> call, Response<ResponseModelField> response) {
        //  tampilkan data ke dalam list
        Toast.makeText(FieldDetailActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
          public void run() {
            finish();
          }
        }, 1000);
        //  tutup progress dialog
        pd.dismiss();
      }
      //  ketika data gagal diambil
      @Override
      public void onFailure(Call<ResponseModelField> call, Throwable t) {
        //  hilangkan progress dialog
        pd.dismiss();
        //  tampilkan pesan
        Toast.makeText(FieldDetailActivity.this, "Data gagal diproses! " + t.getMessage(), Toast.LENGTH_SHORT).show();

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
        if( !User.isAdmin() ) {
          if (isAvailable) {
            btnBookingNow.setVisibility(View.VISIBLE);
          } else {
            txtNotAvailable.setVisibility(View.VISIBLE);
          }
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