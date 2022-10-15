package com.bsc_bandarlampungsportcenter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bsc_bandarlampungsportcenter.rest_api.APIRequestUser;
import com.bsc_bandarlampungsportcenter.rest_api.ErrorModel;
import com.bsc_bandarlampungsportcenter.rest_api.ResponseModelUser;
import com.bsc_bandarlampungsportcenter.rest_api.RetroServer;
import com.bsc_bandarlampungsportcenter.rest_api.UserModel;
import com.bsc_bandarlampungsportcenter.session.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

  Intent intent;
  TextView txtErrorName, txtErrorUsername, txtErrorEmail;
  EditText edtName, edtUsername, edtEmail;
  Button btnChangeProfile;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_edit_profile);

    edtName = findViewById(R.id.name);
    edtUsername = findViewById(R.id.username);
    edtEmail = findViewById(R.id.email);
    txtErrorName = findViewById(R.id.error_name);
    txtErrorUsername = findViewById(R.id.error_username);
    txtErrorEmail = findViewById(R.id.error_email);
    btnChangeProfile = findViewById(R.id.btn_edit_profile);

    btnChangeProfile.setOnClickListener( view -> {
      editProfile(User.getUserId());
    });

    getSupportActionBar().hide();
    tampilData(User.getUserId());

  }

  private void goneError()
  {
    txtErrorName.setVisibility(View.GONE);
    txtErrorUsername.setVisibility(View.GONE);
    txtErrorEmail.setVisibility(View.GONE);
  }

  void tampilData (String id)
  {
    //  deklarasi variabel komponen "Progress Dialog"
    ProgressDialog pd;
    //  setup progress dialog
    pd = new ProgressDialog(EditProfileActivity.this);
    //  progress dialog tidak dapat di cancel
    pd.setCancelable(false);
    //  isi teks progress dialog
    pd.setMessage("Mohon Tunggu ...");
    //  tampilkan progress dialog
    pd.show();

    APIRequestUser ardData = RetroServer.konekRetrofit().create(APIRequestUser.class);
    Call<ResponseModelUser> detailData = ardData.getUserDetail(id);

    //  deskripsi isi variabel "cl"
    detailData.enqueue(new Callback<ResponseModelUser>() {
      //  ketika data berhasil diambil
      @Override
      public void onResponse(Call<ResponseModelUser> call, Response<ResponseModelUser> response) {
        //  tampilkan data ke dalam list
        UserModel data = response.body().getData().get(0);
        edtName.setText(String.valueOf(data.getName()));
        edtUsername.setText(String.valueOf(data.getUsername()));
        edtEmail.setText(String.valueOf(data.getEmail()));

        //  tutup progress dialog
        pd.dismiss();
      }
      //  ketika data gagal diambil
      @Override
      public void onFailure(Call<ResponseModelUser> call, Throwable t) {
        //  hilangkan progress dialog
        pd.dismiss();
        //  tampilkan pesan
        Toast.makeText(EditProfileActivity.this, "Data gagal diproses!" + t.getMessage(), Toast.LENGTH_SHORT).show();

      }
    });
  }

  void editProfile(String id)
  {
    //  deklarasi variabel komponen "Progress Dialog"
    ProgressDialog pd;
    //  setup progress dialog
    pd = new ProgressDialog(EditProfileActivity.this);
    //  progress dialog tidak dapat di cancel
    pd.setCancelable(false);
    //  isi teks progress dialog
    pd.setMessage("Mohon Tunggu ...");
    //  tampilkan progress dialog
    pd.show();

    goneError();

    APIRequestUser ardData = RetroServer.konekRetrofit().create(APIRequestUser.class);
    Call<ResponseModelUser> detailData = ardData.editProfile(id,
        edtName.getText().toString(),
        edtUsername.getText().toString(),
        edtEmail.getText().toString());

    //  deskripsi isi variabel "cl"
    detailData.enqueue(new Callback<ResponseModelUser>() {
      //  ketika data berhasil diambil
      @Override
      public void onResponse(Call<ResponseModelUser> call, Response<ResponseModelUser> response) {
        ErrorModel dataError = response.body().getErrors().get(0);
        int status = response.body().getStatus();
        if( status == 200 ) {
          Toast.makeText(EditProfileActivity.this, "Data profil berhasil diubah.", Toast.LENGTH_SHORT).show();
          Handler handler = new Handler();
          handler.postDelayed(new Runnable() {
            public void run() {
              finish();
            }
          }, 1000);
        }else{
          String errorValueName = dataError.getName(), errorValueUsername = dataError.getUsername(), errorValueEmail = dataError.getEmail();

          if( errorValueName != null ) {
            txtErrorName.setText(errorValueName);
            txtErrorName.setVisibility(View.VISIBLE);
          }

          if( errorValueUsername != null ) {
            txtErrorUsername.setText(errorValueUsername);
            txtErrorUsername.setVisibility(View.VISIBLE);
          }

          if( errorValueEmail != null ) {
            txtErrorEmail.setText(errorValueEmail);
            txtErrorEmail.setVisibility(View.VISIBLE);
          }

        }

        //  tutup progress dialog
        pd.dismiss();
      }
      //  ketika data gagal diambil
      @Override
      public void onFailure(Call<ResponseModelUser> call, Throwable t) {
        //  hilangkan progress dialog
        pd.dismiss();
        //  tampilkan pesan
        Toast.makeText(EditProfileActivity.this, "Data gagal ditampilkan!" + t.getMessage(), Toast.LENGTH_SHORT).show();

      }
    });
  }

}