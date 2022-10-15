package com.bsc_bandarlampungsportcenter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bsc_bandarlampungsportcenter.rest_api.APIRequestTransaction;
import com.bsc_bandarlampungsportcenter.rest_api.TransactionModel;
import com.bsc_bandarlampungsportcenter.rest_api.ResponseModelTransaction;
import com.bsc_bandarlampungsportcenter.rest_api.RetroServer;
import com.bsc_bandarlampungsportcenter.session.User;
import com.bsc_bandarlampungsportcenter.ui.TransactionFragment;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionDetailActivity extends AppCompatActivity {

  Intent intent;
  Bundle bundle;

  LinearLayout actionAdmin;
  
  ImageView fieldPhoto;
  TextView txtId, txtTransactionCode, txtSuccessStatus, txtPendingStatus, txtFailedStatus, txtCreatedAt, txtFieldName, txtStartAt, txtEndAt, txtLongOfBooking, txtBookingPricePerHour, txtPrice, txtDiscon, txtPriceTotal;
  Button btnSeePeyment, btnCancelTransaction, btnDenied, btnAccept;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_transaction_detail);
    
    fieldPhoto = findViewById(R.id.field_photo);
    txtId = findViewById(R.id.id);
    txtTransactionCode = findViewById(R.id.transaction_code);
    txtSuccessStatus = findViewById(R.id.success_status);
    txtPendingStatus = findViewById(R.id.pending_status);
    txtFailedStatus = findViewById(R.id.failed_status);
    txtCreatedAt = findViewById(R.id.created_at);
    txtFieldName = findViewById(R.id.field_name);
    txtStartAt = findViewById(R.id.start_at);
    txtEndAt = findViewById(R.id.end_at);
    txtLongOfBooking = findViewById(R.id.long_of_booking);
    txtBookingPricePerHour = findViewById(R.id.price_booking_perhour);
    txtPrice = findViewById(R.id.price);
    txtDiscon = findViewById(R.id.discon);
    txtPriceTotal = findViewById(R.id.price_total);
    btnSeePeyment = findViewById(R.id.btn_see_payment);
    btnCancelTransaction = findViewById(R.id.btn_cancel_transaction);
    actionAdmin = findViewById(R.id.action_admin);
    btnDenied = findViewById(R.id.btn_denied);
    btnAccept = findViewById(R.id.btn_accept);

    btnSeePeyment.setOnClickListener( view -> {
      intent = new Intent(TransactionDetailActivity.this, PaymentActivity.class);
      intent.putExtra("message", "");
      startActivity(intent);
    });

    btnCancelTransaction.setOnClickListener( view -> {
      AlertDialog.Builder confirm = new AlertDialog.Builder(view.getContext());
      final CharSequence[] confirmItem = {"Ya", "Batal"};
      confirm.setTitle("Yakin ingin membatalkan transaksi?");
      confirm.setItems(confirmItem,(dialogConfirm, whichConfirm)->{
        switch (whichConfirm) {
          case 0:
            changeTransaction("dibatalkan");
            break;
        }
      });
      confirm.create().show();
    });

    btnDenied.setOnClickListener( view -> {
      AlertDialog.Builder confirm = new AlertDialog.Builder(view.getContext());
      final CharSequence[] confirmItem = {"Ya", "Batal"};
      confirm.setTitle("Yakin ingin menolak transaksi?");
      confirm.setItems(confirmItem,(dialogConfirm, whichConfirm)->{
        switch (whichConfirm) {
          case 0:
            changeTransaction("ditolak");
            break;
        }
      });
      confirm.create().show();
    });

    btnAccept.setOnClickListener( view -> {
      AlertDialog.Builder confirm = new AlertDialog.Builder(view.getContext());
      final CharSequence[] confirmItem = {"Ya", "Batal"};
      confirm.setTitle("Yakin ingin menyetujui transaksi?");
      confirm.setItems(confirmItem,(dialogConfirm, whichConfirm)->{
        switch (whichConfirm) {
          case 0:
            changeTransaction("lunas");
            break;
        }
      });
      confirm.create().show();
    });

    bundle = getIntent().getExtras();
    String id = String.valueOf(bundle.get("id"));
    if( !id.isEmpty() ) {
      tampilData(id);
    }

    getSupportActionBar().hide();
    
  }

  void tampilData (String id)
  {
    //  deklarasi variabel komponen "Progress Dialog"
    ProgressDialog pd;
    //  setup progress dialog
    pd = new ProgressDialog(TransactionDetailActivity.this);
    //  progress dialog tidak dapat di cancel
    pd.setCancelable(false);
    //  isi teks progress dialog
    pd.setMessage("Mohon Tunggu ...");
    //  tampilkan progress dialog
    pd.show();

    APIRequestTransaction ardData = RetroServer.konekRetrofit().create(APIRequestTransaction.class);
    Call<ResponseModelTransaction> detailData = ardData.getDetailData(id);

    //  deskripsi isi variabel "cl"
    detailData.enqueue(new Callback<ResponseModelTransaction>() {
      //  ketika data berhasil diambil
      @Override
      public void onResponse(Call<ResponseModelTransaction> call, Response<ResponseModelTransaction> response) {
        //  tampilkan data ke dalam list
        TransactionModel data = response.body().getData().get(0);
        Picasso.get().load(RetroServer.getBASE_URL_FILE() + data.getField_photo()).into(fieldPhoto);
        txtId.setText(String.valueOf(data.getId()));
        txtTransactionCode.setText(String.valueOf(data.getTransaction_code()));
        txtCreatedAt.setText(String.valueOf(data.getBooking_date()));
        txtFieldName.setText(String.valueOf(data.getField_name()));
        txtStartAt.setText(data.getStart_at() + ":00");
        txtEndAt.setText(data.getEnd_at() + ":00");
        txtLongOfBooking.setText(data.getLong_of_booking() + "jam");
        txtBookingPricePerHour.setText("Rp. " + NumberFormat.getInstance().format(data.getPrice_perhour()));
        txtPrice.setText("Rp. " + NumberFormat.getInstance().format(data.getPrice()));
        txtDiscon.setText("Rp. " + NumberFormat.getInstance().format(data.getDiscon()));
        txtPriceTotal.setText("Rp. " + NumberFormat.getInstance().format(data.getPrice_total()));

        String transactionStatus = data.getStatus();
        if( transactionStatus.equalsIgnoreCase("lunas") ) {
          txtSuccessStatus.setText(transactionStatus);
          txtSuccessStatus.setVisibility(View.VISIBLE);
        }else if( transactionStatus.equalsIgnoreCase("menunggu pembayaran") ) {
          txtPendingStatus.setText(transactionStatus);
          txtPendingStatus.setVisibility(View.VISIBLE);
          if(!User.isAdmin()) {
            btnSeePeyment.setVisibility(View.VISIBLE);
            btnCancelTransaction.setVisibility(View.VISIBLE);
          }else{
            actionAdmin.setVisibility(View.VISIBLE);
          }
        }else{
          txtFailedStatus.setText(transactionStatus);
          txtFailedStatus.setVisibility(View.VISIBLE);
        }

        //  tutup progress dialog
        pd.dismiss();
      }
      //  ketika data gagal diambil
      @Override
      public void onFailure(Call<ResponseModelTransaction> call, Throwable t) {
        //  hilangkan progress dialog
        pd.dismiss();
        //  tampilkan pesan
        Toast.makeText(TransactionDetailActivity.this, "Data gagal ditampilkan!" + t.getMessage(), Toast.LENGTH_SHORT).show();

      }
    });
  }

  void changeTransaction (String status)
  {
    //  deklarasi variabel komponen "Progress Dialog"
    ProgressDialog pd;
    //  setup progress dialog
    pd = new ProgressDialog(TransactionDetailActivity.this);
    //  progress dialog tidak dapat di cancel
    pd.setCancelable(false);
    //  isi teks progress dialog
    pd.setMessage("Mohon Tunggu ...");
    //  tampilkan progress dialog
    pd.show();

    APIRequestTransaction ardData = RetroServer.konekRetrofit().create(APIRequestTransaction.class);
    Call<ResponseModelTransaction> updateData = ardData.update(
        txtId.getText().toString(), status );

    //  deskripsi isi variabel "cl"
    updateData.enqueue(new Callback<ResponseModelTransaction>() {
      //  ketika data berhasil diambil
      @Override
      public void onResponse(Call<ResponseModelTransaction> call, Response<ResponseModelTransaction> response) {
        //  tutup progress dialog
        pd.dismiss();
        Toast.makeText(TransactionDetailActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
        intent = new Intent(TransactionDetailActivity.this, TransactionDetailActivity.class);
        intent.putExtra("id", txtId.getText().toString());
        startActivity(intent);
        finish();

      }
      //  ketika data gagal diambil
      @Override
      public void onFailure(Call<ResponseModelTransaction> call, Throwable t) {
        //  hilangkan progress dialog
        pd.dismiss();
        //  tampilkan pesan
        Toast.makeText(TransactionDetailActivity.this, "Gagal diproses!" + t.getMessage(), Toast.LENGTH_SHORT).show();

      }
    });
  }


}