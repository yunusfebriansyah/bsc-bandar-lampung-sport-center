package com.bsc_bandarlampungsportcenter;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

  Intent intent;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);

    getSupportActionBar().hide();

    intent = new Intent(SplashActivity.this, MainActivity.class);

    new Handler().postDelayed(new Runnable(){
      @Override
      public void run() {
        startActivity(intent);
        finish();
      }
    }, 1000);

  }
}