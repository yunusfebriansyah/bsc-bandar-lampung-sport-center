package com.bsc_bandarlampungsportcenter;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.bsc_bandarlampungsportcenter.rest_api.RetroServer;

public class VRPanoramaViewActivity extends AppCompatActivity {

  Bundle bundle;
  String id;

  WebView view360;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_vrpanorama_view);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    getSupportActionBar().hide();

    view360 = findViewById(R.id.view_360);

    bundle = getIntent().getExtras();
    id = String.valueOf(bundle.get("id"));
    if( !id.isEmpty() ) {
      showView360();
    }

  }

  void showView360()
  {
    //  deklarasi variabel komponen "Progress Dialog"
    ProgressDialog pd;
    //  setup progress dialog
    pd = new ProgressDialog(VRPanoramaViewActivity.this);
    //  progress dialog tidak dapat di cancel
    pd.setCancelable(false);
    //  isi teks progress dialog
    pd.setMessage("Mohon Tunggu ...");
    //  tampilkan progress dialog
    pd.show();

    view360.getSettings().setLoadsImagesAutomatically(true);
    view360.getSettings().setJavaScriptEnabled(true);
    view360.getSettings().setDomStorageEnabled(true);

    // Tiga baris di bawah ini agar laman yang dimuat dapat
    // melakukan zoom.
    view360.getSettings().setSupportZoom(true);
    view360.getSettings().setBuiltInZoomControls(true);
    view360.getSettings().setDisplayZoomControls(false);
    view360.setWebViewClient(new WebViewClient());
    view360.loadUrl(RetroServer.getBASE_URL() + "view-360/" + id);
    view360.setVisibility(View.VISIBLE);

    pd.dismiss();
    
  }
}