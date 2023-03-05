package com.bsc_bandarlampungsportcenter;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

  DBConfig config;
  SQLiteDatabase db;
  Cursor cursor;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);

    getSupportActionBar().hide();

    config = new DBConfig(this);

    db = config.getReadableDatabase();
    cursor = db.rawQuery("SELECT * FROM tbl_user",null);
    cursor.moveToFirst();

//    if(cursor.getCount() == 1)

    new Handler().postDelayed(new Runnable(){
      @Override
      public void run() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
      }
    }, 1000);

  }
}