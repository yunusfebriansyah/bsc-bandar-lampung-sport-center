package com.bsc_bandarlampungsportcenter;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bsc_bandarlampungsportcenter.rest_api.APIRequestUser;
import com.bsc_bandarlampungsportcenter.rest_api.ErrorModel;
import com.bsc_bandarlampungsportcenter.rest_api.ResponseModelUser;
import com.bsc_bandarlampungsportcenter.rest_api.RetroServer;
import com.bsc_bandarlampungsportcenter.rest_api.UserModel;
import com.bsc_bandarlampungsportcenter.session.User;
import com.squareup.picasso.Picasso;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeProfilePhotoActivity extends AppCompatActivity {
  Intent intent;
  String path;

  private final int REQUEST_CODE = 10;

  ImageView photo;
  TextView selectPhoto, errorPhoto;
  Button btnChangePhoto;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_change_profile_photo);

    getSupportActionBar().hide();

    photo = findViewById(R.id.photo);
    selectPhoto = findViewById(R.id.select_photo);
    errorPhoto = findViewById(R.id.error_photo);
    btnChangePhoto = findViewById(R.id.change_photo);

    showPhoto();

    selectPhoto.setOnClickListener( view -> {
      if(ContextCompat.checkSelfPermission(getApplicationContext(),
              Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
        intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "select picture"), 10);
        startActivityForResult(intent, REQUEST_CODE);
      }else{
        ActivityCompat.requestPermissions(ChangeProfilePhotoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
      }
    });

    btnChangePhoto.setOnClickListener( view -> {
      changePhoto();
    });

  }

  void showPhoto()
  {
    //  deklarasi variabel komponen "Progress Dialog"
    ProgressDialog pd;
    //  setup progress dialog
    pd = new ProgressDialog(ChangeProfilePhotoActivity.this);
    //  progress dialog tidak dapat di cancel
    pd.setCancelable(false);
    //  isi teks progress dialog
    pd.setMessage("Mohon Tunggu ...");
    //  tampilkan progress dialog
    pd.show();

    APIRequestUser ardData = RetroServer.konekRetrofit().create(APIRequestUser.class);
    Call<ResponseModelUser> detailData = ardData.getUserDetail(User.getUserId());

    //  deskripsi isi variabel "cl"
    detailData.enqueue(new Callback<ResponseModelUser>() {
      //  ketika data berhasil diambil
      @Override
      public void onResponse(Call<ResponseModelUser> call, Response<ResponseModelUser> response) {
        //  tampilkan data ke dalam list
        UserModel data = response.body().getData().get(0);
        Picasso.get().load(RetroServer.getBASE_URL_FILE() + String.valueOf(data.getPhoto())).into(photo);

        //  tutup progress dialog
        pd.dismiss();
      }
      //  ketika data gagal diambil
      @Override
      public void onFailure(Call<ResponseModelUser> call, Throwable t) {
        //  hilangkan progress dialog
        pd.dismiss();
        //  tampilkan pesan
        Toast.makeText(ChangeProfilePhotoActivity.this, "Data gagal ditampilkan!" + t.getMessage(), Toast.LENGTH_SHORT).show();

      }
    });
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if( requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
      Uri uri = data.getData();
      Context context = ChangeProfilePhotoActivity.this;
      path = RealPathUtil.getRealPath(context, uri);
      Bitmap bitmap = BitmapFactory.decodeFile(path);
      photo.setImageBitmap(bitmap);
    }
  }

  void changePhoto()
  {
    //  deklarasi variabel komponen "Progress Dialog"
    ProgressDialog pd;
    //  setup progress dialog
    pd = new ProgressDialog(ChangeProfilePhotoActivity.this);
    //  progress dialog tidak dapat di cancel
    pd.setCancelable(false);
    //  isi teks progress dialog
    pd.setMessage("Mohon Tunggu ...");
    //  tampilkan progress dialog
    pd.show();

    APIRequestUser ardData = RetroServer.konekRetrofit().create(APIRequestUser.class);

    File file = new File(path);
    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
    MultipartBody.Part body = MultipartBody.Part.createFormData("photo", file.getName(), requestFile);

    Call<ResponseModelUser> editPhoto = ardData.editPhoto(User.getUserId(),body);

    //  deskripsi isi variabel "cl"
    editPhoto.enqueue(new Callback<ResponseModelUser>() {
      //  ketika data berhasil diambil
      @Override
      public void onResponse(Call<ResponseModelUser> call, Response<ResponseModelUser> response) {
        //  tampilkan data ke dalam list
        int status = response.body().getStatus();
        if( status == 200 ) {
          errorPhoto.setVisibility(View.GONE);
          pd.dismiss();
          Toast.makeText(ChangeProfilePhotoActivity.this, "Foto profil berhasil diubah.", Toast.LENGTH_SHORT).show();
          Handler handler = new Handler();
          handler.postDelayed(new Runnable() {
            public void run() {
              finish();
            }
          }, 1000);
        }else{
          ErrorModel errors = response.body().getErrors().get(0);
          errorPhoto.setText(errors.getPhoto());
          errorPhoto.setVisibility(View.VISIBLE);
          pd.dismiss();
        }

      }
      //  ketika data gagal diambil
      @Override
      public void onFailure(Call<ResponseModelUser> call, Throwable t) {
        //  hilangkan progress dialog
        pd.dismiss();
        //  tampilkan pesan
        Toast.makeText(ChangeProfilePhotoActivity.this, "Data gagal diproses! " + t.getMessage(), Toast.LENGTH_SHORT).show();

      }
    });
  }

}