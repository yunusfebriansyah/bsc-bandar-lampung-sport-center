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
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bsc_bandarlampungsportcenter.rest_api.APIRequestField;
import com.bsc_bandarlampungsportcenter.rest_api.ErrorModel;
import com.bsc_bandarlampungsportcenter.rest_api.FieldModel;
import com.bsc_bandarlampungsportcenter.rest_api.ResponseModelField;
import com.bsc_bandarlampungsportcenter.rest_api.RetroServer;
import com.squareup.picasso.Picasso;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeFieldActivity extends AppCompatActivity {

  Intent intent;
  Bundle bundle;

  TextView errorName, errorDescription, errorPhoto, errorPhoto360;
  EditText edtName, edtDescription;
  Button btnSelectPhoto, btnSelectPhoto360, btnChange;
  ImageView photo, photo360;

  Call<ResponseModelField> updateField;

  private final int REQUEST_PHOTO_CODE = 10, REQUEST_PHOTO_360_CODE = 100;
  String photoPath = null, photo360Path = null, id;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_change_field);
    getSupportActionBar().hide();


    errorName = findViewById(R.id.error_name);
    errorDescription = findViewById(R.id.error_description);
    errorPhoto = findViewById(R.id.error_photo);
    errorPhoto360 = findViewById(R.id.error_photo_360);
    edtName = findViewById(R.id.name);
    edtDescription = findViewById(R.id.description);
    btnSelectPhoto = findViewById(R.id.select_photo);
    btnSelectPhoto360 = findViewById(R.id.select_photo_360);
    btnChange = findViewById(R.id.btn_change);
    photo = findViewById(R.id.photo);
    photo360 = findViewById(R.id.photo_360);

    bundle = getIntent().getExtras();
    id = String.valueOf(bundle.get("id"));
    if( !id.isEmpty() ) {
      tampilData(id);
    }

    btnSelectPhoto.setOnClickListener( view -> {
      if(ContextCompat.checkSelfPermission(getApplicationContext(),
              Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
        intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_PHOTO_CODE);
      }else{
        ActivityCompat.requestPermissions(ChangeFieldActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
      }
    });

    btnSelectPhoto360.setOnClickListener( view -> {
      if(ContextCompat.checkSelfPermission(getApplicationContext(),
              Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
        intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_PHOTO_360_CODE);
      }else{
        ActivityCompat.requestPermissions(ChangeFieldActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
      }
    });

    btnChange.setOnClickListener( view -> {
      changeField();
    });


  }

  void errorGone()
  {
    errorName.setVisibility(View.GONE);
    errorDescription.setVisibility(View.GONE);
    errorPhoto.setVisibility(View.GONE);
    errorPhoto360.setVisibility(View.GONE);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if( requestCode == REQUEST_PHOTO_CODE && resultCode == Activity.RESULT_OK){
      Uri uri = data.getData();
      Context context = ChangeFieldActivity.this;
      photoPath = RealPathUtil.getRealPath(context, uri);
      Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
      photo.setImageBitmap(bitmap);
      photo.setVisibility(View.VISIBLE);
    }
    if( requestCode == REQUEST_PHOTO_360_CODE && resultCode == Activity.RESULT_OK){
      Uri uri = data.getData();
      Context context = ChangeFieldActivity.this;
      photo360Path = RealPathUtil.getRealPath(context, uri);
      Bitmap bitmap = BitmapFactory.decodeFile(photo360Path);
      photo360.setImageBitmap(bitmap);
      photo360.setVisibility(View.VISIBLE);
    }
  }

  void tampilData (String id)
  {
    //  deklarasi variabel komponen "Progress Dialog"
    ProgressDialog pd;
    //  setup progress dialog
    pd = new ProgressDialog(ChangeFieldActivity.this);
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
        Picasso.get().load(RetroServer.getBASE_URL_FILE() + String.valueOf(data.getPhoto_360())).into(photo360);
        edtName.setText(String.valueOf(data.getName()));
        edtDescription.setText(String.valueOf(data.getDescription()));
        photo.setVisibility(View.VISIBLE);
        photo360.setVisibility(View.VISIBLE);
        //  tutup progress dialog
        pd.dismiss();
      }
      //  ketika data gagal diambil
      @Override
      public void onFailure(Call<ResponseModelField> call, Throwable t) {
        //  hilangkan progress dialog
        pd.dismiss();
        //  tampilkan pesan
        Toast.makeText(ChangeFieldActivity.this, "Data gagal ditampilkan!" + t.getMessage(), Toast.LENGTH_SHORT).show();

      }
    });
  }

  void changeField()
  {
    //  deklarasi variabel komponen "Progress Dialog"
    ProgressDialog pd;
    //  setup progress dialog
    pd = new ProgressDialog(ChangeFieldActivity.this);
    //  progress dialog tidak dapat di cancel
    pd.setCancelable(false);
    //  isi teks progress dialog
    pd.setMessage("Mohon Tunggu ...");
    //  tampilkan progress dialog
    pd.show();

    errorGone();

    APIRequestField ardData = RetroServer.konekRetrofit().create(APIRequestField.class);

    RequestBody name = RequestBody.create(MediaType.parse("multipart/form-data"), edtName.getText().toString());
    RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), edtDescription.getText().toString());


    File filePhoto;
    RequestBody requestFilePhoto;
    MultipartBody.Part photo;
    if( photoPath == null )
    {
      photo = null;
    }else {
      filePhoto = new File(photoPath);
      requestFilePhoto = RequestBody.create(MediaType.parse("multipart/form-data"), filePhoto);
      photo = MultipartBody.Part.createFormData("photo", filePhoto.getName(), requestFilePhoto);
    }

    File filePhoto360;
    RequestBody requestFilePhoto360;
    MultipartBody.Part photo360;
    if( photo360Path == null ) {
      photo360 =null;
    }else{
      filePhoto360 = new File(photo360Path);
      requestFilePhoto360 = RequestBody.create(MediaType.parse("multipart/form-data"), filePhoto360);
      photo360 = MultipartBody.Part.createFormData("photo_360", filePhoto360.getName(), requestFilePhoto360);
    }

    updateField = ardData.changeField(id, name, description, photo, photo360);

    //  deskripsi isi variabel "cl"
    updateField.enqueue(new Callback<ResponseModelField>() {
      //  ketika data berhasil diambil
      @Override
      public void onResponse(Call<ResponseModelField> call, Response<ResponseModelField> response) {
        //  tampilkan data ke dalam list
        int status = response.body().getStatus();
        if (status == 200) {
          pd.dismiss();
          Toast.makeText(ChangeFieldActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
          Handler handler = new Handler();
          handler.postDelayed(new Runnable() {
            public void run() {
              finish();
            }
          }, 1000);
        } else {
          ErrorModel errors = response.body().getErrors().get(0);

          String errorValueName = errors.getName(), errorValueDescription = errors.getDescription(), errorValuePhoto = errors.getPhoto(), errorValuePhoto360 = errors.getPhoto_360();

          if (errorValueName != null) {
            errorName.setText(errorValueName);
            errorName.setVisibility(View.VISIBLE);
          }

          if (errorValueDescription != null) {
            errorDescription.setText(errorValueDescription);
            errorDescription.setVisibility(View.VISIBLE);
          }

          if (errorValuePhoto != null) {
            errorPhoto.setText(errorValuePhoto);
            errorPhoto.setVisibility(View.VISIBLE);
          }

          if (errorValuePhoto360 != null) {
            errorPhoto360.setText(errorValuePhoto360);
            errorPhoto360.setVisibility(View.VISIBLE);
          }

          pd.dismiss();
        }

      }

      //  ketika data gagal diambil
      @Override
      public void onFailure(Call<ResponseModelField> call, Throwable t) {
        //  hilangkan progress dialog
        pd.dismiss();
        //  tampilkan pesan
        Toast.makeText(ChangeFieldActivity.this, "Data gagal diproses! " + t.getMessage(), Toast.LENGTH_SHORT).show();

      }
    });

  }


}