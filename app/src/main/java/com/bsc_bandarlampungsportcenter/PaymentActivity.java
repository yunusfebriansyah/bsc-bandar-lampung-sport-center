package com.bsc_bandarlampungsportcenter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class PaymentActivity extends AppCompatActivity {

  Intent intent;
  Bundle bundle;
  LinearLayout icon;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_payment);

    icon = findViewById(R.id.icon);

    bundle = getIntent().getExtras();
    String message = String.valueOf(bundle.get("message"));
    if( !message.isEmpty() ) {
      icon.setVisibility(View.VISIBLE);
    }

    getSupportActionBar().hide();

  }
}