package com.bsc_bandarlampungsportcenter;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

  DBConfig config;
  SQLiteDatabase db;
  Cursor cursor;

  Intent intent;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);

    getSupportActionBar().hide();

    intent = new Intent(SplashActivity.this, RegisterActivity.class);

    config = new DBConfig(this);

    db = config.getReadableDatabase();
    cursor = db.rawQuery("SELECT * FROM tbl_user",null);
    cursor.moveToFirst();

    if(cursor.getCount() == 1) {
      intent = new Intent(SplashActivity.this, MainActivity.class);
    }else{
      intent = new Intent(SplashActivity.this, RegisterActivity.class);
    }

    new Handler().postDelayed(new Runnable(){
      @Override
      public void run() {
        startActivity(intent);
        finish();
      }
    }, 1000);

  }
}