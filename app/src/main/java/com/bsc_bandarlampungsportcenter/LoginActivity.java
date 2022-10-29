package com.bsc_bandarlampungsportcenter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bsc_bandarlampungsportcenter.rest_api.APIRequestUser;
import com.bsc_bandarlampungsportcenter.rest_api.ResponseModelUser;
import com.bsc_bandarlampungsportcenter.rest_api.RetroServer;
import com.bsc_bandarlampungsportcenter.rest_api.UserModel;
import com.bsc_bandarlampungsportcenter.session.Price;
import com.bsc_bandarlampungsportcenter.session.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

  Intent intent;
  Bundle bundle;

  DBConfig config;
  SQLiteDatabase db;
  Cursor cursor;

  List<UserModel> listData = new ArrayList<>();

  EditText edtEmail, edtPassword;
  TextView linkToRegister;
  Button btnLogin;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    getSupportActionBar().hide();

    config = new DBConfig(this);

    db = config.getReadableDatabase();
    cursor = db.rawQuery("SELECT * FROM tbl_user",null);
    cursor.moveToFirst();

    if(cursor.getCount() == 1) {
      intent = new Intent(LoginActivity.this, MainActivity.class);
      startActivity(intent);
      finish();
    }

    edtEmail = findViewById(R.id.email);
    edtPassword = findViewById(R.id.password);
    btnLogin = findViewById(R.id.btn_login);
    linkToRegister = findViewById(R.id.to_register);

    linkToRegister.setOnClickListener(view -> {
      intent = new Intent(LoginActivity.this, RegisterActivity.class);
      intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
      startActivity(intent);
      finish();
    });

    btnLogin.setOnClickListener(view ->{
      login();
    });

    bundle = getIntent().getExtras();
    String message = String.valueOf(bundle.get("message"));
    if( !message.isEmpty() ) {
      Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

  }


  private void login()
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

    APIRequestUser ardData = RetroServer.konekRetrofit().create(APIRequestUser.class);
    Call<ResponseModelUser> login = ardData.login(
            edtEmail.getText().toString(),
            edtPassword.getText().toString()
    );

    login.enqueue(new Callback<ResponseModelUser>() {
      //  ketika data berhasil diambil
      @Override
      public void onResponse(Call<ResponseModelUser> call, Response<ResponseModelUser> response) {
        //  tampilkan data ke dalam list
        listData = response.body().getData();
        int status = response.body().getStatus();

        if( status == 200 ) {
          int userId = listData.get(0).getId(), isAdmin = listData.get(0).getIs_admin();
          Log.i("is admin", String.valueOf(isAdmin));
          db = config.getReadableDatabase();
          db.execSQL("INSERT INTO tbl_user (user_id, is_admin) VALUES ('" + userId + "', '" + isAdmin + "')");
          pd.dismiss();
          intent = new Intent(LoginActivity.this, MainActivity.class);
          intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
          User.setUserId(String.valueOf(userId));
          User.setIsAdmin(String.valueOf(isAdmin));
          Price.setPrice(LoginActivity.this);
          pd.dismiss();
          startActivity(intent);
          finish();
        }else{
          pd.dismiss();
          Toast.makeText(LoginActivity.this, "Email atau password salah!.", Toast.LENGTH_SHORT).show();
        }
      }
      //  ketika data gagal diambil
      @Override
      public void onFailure(Call<ResponseModelUser> call, Throwable t) {
        //  hilangkan progress dialog
        pd.dismiss();
        //  tampilkan pesan
        Toast.makeText(LoginActivity.this, "Registrasi gagal!" + t.getMessage(), Toast.LENGTH_SHORT).show();

      }
    });
  }


}