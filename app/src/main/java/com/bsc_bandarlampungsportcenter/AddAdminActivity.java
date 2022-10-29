package com.bsc_bandarlampungsportcenter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bsc_bandarlampungsportcenter.rest_api.APIRequestUser;
import com.bsc_bandarlampungsportcenter.rest_api.ErrorModel;
import com.bsc_bandarlampungsportcenter.rest_api.ResponseModelUser;
import com.bsc_bandarlampungsportcenter.rest_api.RetroServer;
import com.bsc_bandarlampungsportcenter.rest_api.UserModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAdminActivity extends AppCompatActivity {

  EditText edtName, edtUsername, edtEmail, edtPassword, edtPasswordRepeat;
  TextView errorName, errorUsername, errorEmail, errorPassword, errorPasswordRepeat;
  Button btnAddAdmin;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_admin);
    getSupportActionBar().hide();

    edtName = findViewById(R.id.name);
    edtUsername = findViewById(R.id.username);
    edtEmail = findViewById(R.id.email);
    edtPassword = findViewById(R.id.password);
    edtPasswordRepeat = findViewById(R.id.password_repeat);
    btnAddAdmin = findViewById(R.id.btn_add_admin);
    errorName = findViewById(R.id.error_name);
    errorUsername = findViewById(R.id.error_username);
    errorEmail = findViewById(R.id.error_email);
    errorPassword = findViewById(R.id.error_password);
    errorPasswordRepeat = findViewById(R.id.error_password_repeat);

    btnAddAdmin.setOnClickListener( view -> {
      addAdmin();
    });

  }

  private void goneError()
  {
    errorName.setVisibility(View.GONE);
    errorUsername.setVisibility(View.GONE);
    errorEmail.setVisibility(View.GONE);
    errorPassword.setVisibility(View.GONE);
    errorPasswordRepeat.setVisibility(View.GONE);
  }

  private void addAdmin()
  {
    //  deklarasi variabel komponen "Progress Dialog"
    ProgressDialog pd;
    //  setup progress dialog
    pd = new ProgressDialog(this);
    //  progress dialog tidak dapat di cancel
    pd.setCancelable(false);
    //  isi teks progress dialog
    pd.setMessage("Mohon Tunggu ...");
    //  tampilkan progress dialog
    pd.show();

    goneError();

    if( !edtPasswordRepeat.getText().toString().equals(edtPassword.getText().toString()) ) {
      errorPasswordRepeat.setText("Not match with password");
      errorPasswordRepeat.setVisibility(View.VISIBLE);
    }

    APIRequestUser ardData = RetroServer.konekRetrofit().create(APIRequestUser.class);
    Call<ResponseModelUser> register = ardData.registerAdmin(
            edtName.getText().toString(),
            edtUsername.getText().toString(),
            edtEmail.getText().toString(),
            edtPassword.getText().toString()
    );

    register.enqueue(new Callback<ResponseModelUser>() {
      //  ketika data berhasil diambil
      @Override
      public void onResponse(Call<ResponseModelUser> call, Response<ResponseModelUser> response) {

        ErrorModel dataError = response.body().getErrors().get(0);
        int status = response.body().getStatus();

        if( status == 200 ) {
          pd.dismiss();
          Toast.makeText(AddAdminActivity.this, "Admin berhasil ditambah.", Toast.LENGTH_SHORT).show();
          Handler handler = new Handler();
          handler.postDelayed(new Runnable() {
            public void run() {
              finish();
            }
          }, 1000);
        }else{
          String errorValueName = dataError.getName(), errorValueUsername = dataError.getUsername(), errorValueEmail = dataError.getEmail(), errorValuePassword = dataError.getPassword();

          if( errorValueName != null ) {
            errorName.setText(errorValueName);
            errorName.setVisibility(View.VISIBLE);
          }

          if( errorValueUsername != null ) {
            errorUsername.setText(errorValueUsername);
            errorUsername.setVisibility(View.VISIBLE);
          }

          if( errorValueEmail != null ) {
            errorEmail.setText(errorValueEmail);
            errorEmail.setVisibility(View.VISIBLE);
          }

          if( errorValuePassword != null ) {
            errorPassword.setText(errorValuePassword);
            errorPassword.setVisibility(View.VISIBLE);
          }

          pd.dismiss();

        }
      }
      //  ketika data gagal diambil
      @Override
      public void onFailure(Call<ResponseModelUser> call, Throwable t) {
        //  hilangkan progress dialog
        pd.dismiss();
        //  tampilkan pesan
        Toast.makeText(AddAdminActivity.this, "Gagal diproses!" + t.getMessage(), Toast.LENGTH_SHORT).show();

      }
    });
  }

}