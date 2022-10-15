package com.bsc_bandarlampungsportcenter;

import android.app.ProgressDialog;
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
import com.bsc_bandarlampungsportcenter.session.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

  TextView txtErrorOldPassword, txtErrorNewPassword, txtErrorNewPasswordRepeat;
  EditText edtOldPassword, edtNewPassword, edtNewPasswordRepeat;
  Button btnChangePassword;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_change_password);

    txtErrorOldPassword = findViewById(R.id.error_old_password);
    txtErrorNewPassword = findViewById(R.id.error_new_password);
    txtErrorNewPasswordRepeat = findViewById(R.id.error_new_password_repeat);

    edtOldPassword = findViewById(R.id.old_password);
    edtNewPassword = findViewById(R.id.new_password);
    edtNewPasswordRepeat = findViewById(R.id.new_password_repeat);

    btnChangePassword = findViewById(R.id.btn_change_password);

    btnChangePassword.setOnClickListener(view -> {
      changePassword(User.getUserId());
    });

    getSupportActionBar().hide();

  }

  private void goneError()
  {
    txtErrorOldPassword.setVisibility(View.GONE);
    txtErrorNewPassword.setVisibility(View.GONE);
    txtErrorNewPasswordRepeat.setVisibility(View.GONE);
  }

  void changePassword(String id)
  {
    //  deklarasi variabel komponen "Progress Dialog"
    ProgressDialog pd;
    //  setup progress dialog
    pd = new ProgressDialog(ChangePasswordActivity.this);
    //  progress dialog tidak dapat di cancel
    pd.setCancelable(false);
    //  isi teks progress dialog
    pd.setMessage("Mohon Tunggu ...");
    //  tampilkan progress dialog
    pd.show();

    goneError();

    APIRequestUser ardData = RetroServer.konekRetrofit().create(APIRequestUser.class);
    Call<ResponseModelUser> changePassword = ardData.changePassword(id,
            edtOldPassword.getText().toString(),
            edtNewPassword.getText().toString(),
            edtNewPasswordRepeat.getText().toString());

    //  deskripsi isi variabel "cl"
    changePassword.enqueue(new Callback<ResponseModelUser>() {
      //  ketika data berhasil diambil
      @Override
      public void onResponse(Call<ResponseModelUser> call, Response<ResponseModelUser> response) {
        ErrorModel dataError = response.body().getErrors().get(0);
        int status = response.body().getStatus();
        if( status == 200 ) {
          pd.dismiss();
          Toast.makeText(ChangePasswordActivity.this, "Password berhasil diubah.", Toast.LENGTH_SHORT).show();
          Handler handler = new Handler();
          handler.postDelayed(new Runnable() {
            public void run() {
              finish();
            }
          }, 1000);
        }else{
          String errorValueOldPassword = dataError.getPassword(), errorValueNewPassword = dataError.getNew_password(), errorValueNewPasswordRepeat = dataError.getConfirm_password();

          if( errorValueOldPassword != null ) {
            txtErrorOldPassword.setText(errorValueOldPassword);
            txtErrorOldPassword.setVisibility(View.VISIBLE);
          }

          if( errorValueNewPassword != null ) {
            txtErrorNewPassword.setText(errorValueNewPassword);
            txtErrorNewPassword.setVisibility(View.VISIBLE);
          }

          if( errorValueNewPasswordRepeat != null ) {
            txtErrorNewPasswordRepeat.setText(errorValueNewPasswordRepeat);
            txtErrorNewPasswordRepeat.setVisibility(View.VISIBLE);
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
        Toast.makeText(ChangePasswordActivity.this, "Data gagal diproses!" + t.getMessage(), Toast.LENGTH_SHORT).show();

      }
    });
  }




}