package com.bsc_bandarlampungsportcenter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bsc_bandarlampungsportcenter.rest_api.APIRequestField;
import com.bsc_bandarlampungsportcenter.rest_api.APIRequestTime;
import com.bsc_bandarlampungsportcenter.rest_api.APIRequestTransaction;
import com.bsc_bandarlampungsportcenter.rest_api.FieldModel;
import com.bsc_bandarlampungsportcenter.rest_api.ResponseModelField;
import com.bsc_bandarlampungsportcenter.rest_api.ResponseModelTime;
import com.bsc_bandarlampungsportcenter.rest_api.ResponseModelTransaction;
import com.bsc_bandarlampungsportcenter.rest_api.RetroServer;
import com.bsc_bandarlampungsportcenter.session.Price;
import com.bsc_bandarlampungsportcenter.session.User;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FieldBookingActivity extends AppCompatActivity {

  Bundle bundle;
  ImageView imgField;
  TextView txtId, txtName, txtPrice, txtPriceMoney, txtBookingPricePerHour, txtLongOfBooking, txtBookingPrice, txtDiscon, txtPriceTotal;
  Button btnCancel, btnBooking;
  Spinner spStartAt, spEndAt;
  int startAtItems[], endAtItems[];
  String textStartAtItems[], textEndAtItems[];
  ArrayAdapter<String> startAtAdapter, endAtAdapter;
  int longOfBooking, discon, price;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_field_booking);

    Price.setPrice(FieldBookingActivity.this);

    imgField = findViewById(R.id.photo);
    txtId = findViewById(R.id.id);
    txtName = findViewById(R.id.name);
    txtPrice = findViewById(R.id.price);
    txtPriceMoney = findViewById(R.id.price_money);
    txtBookingPricePerHour = findViewById(R.id.price_booking_perhour);
    txtLongOfBooking = findViewById(R.id.long_of_booking);
    txtBookingPrice = findViewById(R.id.price_booking);
    txtDiscon = findViewById(R.id.discon);
    txtPriceTotal = findViewById(R.id.price_total);
    spStartAt = findViewById(R.id.start_at);
    spEndAt = findViewById(R.id.end_at);

    btnCancel = findViewById(R.id.btn_cancel);
    btnBooking = findViewById(R.id.btn_booking);

    txtPrice.setText(String.valueOf(Price.getPrice()));
    txtPriceMoney.setText(Price.getPriceMoney() + " / jam");
    txtBookingPricePerHour.setText(Price.getPriceMoney());

    bundle = getIntent().getExtras();
    String id = String.valueOf(bundle.get("id"));
    if( !id.isEmpty() )
    {
      txtId.setText(id);
      tampilData(id);
      getTime(id);
    }

    btnCancel.setOnClickListener( view -> {
      finish();
    });

    spStartAt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        getEndAtTime(id, String.valueOf(startAtItems[(int) spStartAt.getSelectedItemId()]));
      }

      @Override
      public void onNothingSelected(AdapterView<?> adapterView) {

      }
    });

    spEndAt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        changePriceByEndAt();
      }

      @Override
      public void onNothingSelected(AdapterView<?> adapterView) {

      }
    });

    btnBooking.setOnClickListener( view -> {
      booking(id);
    });

    getSupportActionBar().hide();

  }


  void changePriceByEndAt()
  {
    int startAt = startAtItems[(int) spStartAt.getSelectedItemId()];
    int endAt = endAtItems[(int) spEndAt.getSelectedItemId()];

    longOfBooking = endAt - startAt;
    price = longOfBooking * Integer.valueOf(txtPrice.getText().toString());
    discon = 0;

    txtLongOfBooking.setText(longOfBooking + " jam");
    txtBookingPrice.setText("Rp. " + NumberFormat.getInstance().format(price));

    if( longOfBooking >= 3 ){
      discon = 15000 * (longOfBooking - 2);
      txtDiscon.setText("Rp. " + NumberFormat.getInstance().format(discon));
    }else{
      txtDiscon.setText("Rp. 0");
    }

    txtPriceTotal.setText("Rp. " + NumberFormat.getInstance().format(price - discon));
  }

  void getTime (String id)
  {
    //  deklarasi variabel komponen "Progress Dialog"
    ProgressDialog pd;
    //  setup progress dialog
    pd = new ProgressDialog(FieldBookingActivity.this);
    //  progress dialog tidak dapat di cancel
    pd.setCancelable(false);
    //  isi teks progress dialog
    pd.setMessage("Mohon Tunggu ...");
    //  tampilkan progress dialog
    pd.show();

    APIRequestTime ardData = RetroServer.konekRetrofit().create(APIRequestTime.class);
    Call<ResponseModelTime> detailData = ardData.getTimes(id);

    //  deskripsi isi variabel "cl"
    detailData.enqueue(new Callback<ResponseModelTime>() {
      //  ketika data berhasil diambil
      @Override
      public void onResponse(Call<ResponseModelTime> call, Response<ResponseModelTime> response) {
        //  tampilkan data ke dalam list
        startAtItems = response.body().getStart_at();
        endAtItems = response.body().getEnd_at();
        textStartAtItems = response.body().getText_start_at();
        textEndAtItems = response.body().getText_end_at();

        startAtAdapter = new ArrayAdapter<String>(FieldBookingActivity.this,
                R.layout.spinner_list,textStartAtItems);
        startAtAdapter.setDropDownViewResource(R.layout.spinner_list);
        spStartAt.setAdapter(startAtAdapter);

        endAtAdapter = new ArrayAdapter<String>(FieldBookingActivity.this,
                R.layout.spinner_list,textEndAtItems);
        endAtAdapter.setDropDownViewResource(R.layout.spinner_list);
        spEndAt.setAdapter(endAtAdapter);

        longOfBooking = endAtItems[0] - startAtItems[0];
        price = longOfBooking * Integer.valueOf(txtPrice.getText().toString());
        discon = 0;

        txtLongOfBooking.setText(longOfBooking + " jam");
        txtBookingPrice.setText("Rp. " + NumberFormat.getInstance().format(price));

        if( longOfBooking >= 3 ){
          discon = 15000 * (longOfBooking - 2);
          txtDiscon.setText("Rp. " + NumberFormat.getInstance().format(discon));
        }else{
          txtDiscon.setText("Rp. 0");
        }

        txtPriceTotal.setText("Rp. " + NumberFormat.getInstance().format(price - discon));


        //  tutup progress dialog
        pd.dismiss();
      }
      //  ketika data gagal diambil
      @Override
      public void onFailure(Call<ResponseModelTime> call, Throwable t) {
        //  hilangkan progress dialog
        pd.dismiss();
        //  tampilkan pesan
        Toast.makeText(FieldBookingActivity.this, "Data gagal diproses!" + t.getMessage(), Toast.LENGTH_SHORT).show();

      }
    });
  }

  void getEndAtTime (String id, String startAt)
  {
    //  deklarasi variabel komponen "Progress Dialog"
    ProgressDialog pd;
    //  setup progress dialog
    pd = new ProgressDialog(FieldBookingActivity.this);
    //  progress dialog tidak dapat di cancel
    pd.setCancelable(false);
    //  isi teks progress dialog
    pd.setMessage("Mohon Tunggu ...");
    //  tampilkan progress dialog
    pd.show();

    APIRequestTime ardData = RetroServer.konekRetrofit().create(APIRequestTime.class);
    Call<ResponseModelTime> detailData = ardData.getEndTimes(id, startAt);

    //  deskripsi isi variabel "cl"
    detailData.enqueue(new Callback<ResponseModelTime>() {
      //  ketika data berhasil diambil
      @Override
      public void onResponse(Call<ResponseModelTime> call, Response<ResponseModelTime> response) {
        //  tampilkan data ke dalam list
        endAtItems = response.body().getEnd_at();
        textEndAtItems = response.body().getText_end_at();

        endAtAdapter = new ArrayAdapter<String>(FieldBookingActivity.this,
                R.layout.spinner_list,textEndAtItems);
        endAtAdapter.setDropDownViewResource(R.layout.spinner_list);
        spEndAt.setAdapter(endAtAdapter);

        longOfBooking = endAtItems[0] - startAtItems[0];
        price = longOfBooking * Integer.valueOf(txtPrice.getText().toString());
        discon = 0;

        txtLongOfBooking.setText(longOfBooking + " jam");
        txtBookingPrice.setText("Rp. " + NumberFormat.getInstance().format(price));

        if( longOfBooking >= 3 ){
          discon = 15000 * (longOfBooking - 2);
          txtDiscon.setText("Rp. " + NumberFormat.getInstance().format(discon));
        }else{
          txtDiscon.setText("Rp. 0");
        }

        txtPriceTotal.setText("Rp. " + NumberFormat.getInstance().format(price - discon));


        //  tutup progress dialog
        pd.dismiss();
      }
      //  ketika data gagal diambil
      @Override
      public void onFailure(Call<ResponseModelTime> call, Throwable t) {
        //  hilangkan progress dialog
        pd.dismiss();
        //  tampilkan pesan
        Toast.makeText(FieldBookingActivity.this, "Data gagal diproses!" + t.getMessage(), Toast.LENGTH_SHORT).show();

      }
    });
  }

  void tampilData (String id)
  {
    //  deklarasi variabel komponen "Progress Dialog"
    ProgressDialog pd;
    //  setup progress dialog
    pd = new ProgressDialog(FieldBookingActivity.this);
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
        Picasso.get().load(RetroServer.getBASE_URL_FILE() + String.valueOf(data.getPhoto())).into(imgField);
        txtName.setText(String.valueOf(data.getName()));
        //  tutup progress dialog
        pd.dismiss();
      }
      //  ketika data gagal diambil
      @Override
      public void onFailure(Call<ResponseModelField> call, Throwable t) {
        //  hilangkan progress dialog
        pd.dismiss();
        //  tampilkan pesan
        Toast.makeText(FieldBookingActivity.this, "Data gagal diproses!" + t.getMessage(), Toast.LENGTH_SHORT).show();

      }
    });
  }

  void booking (String id)
  {
    //  deklarasi variabel komponen "Progress Dialog"
    ProgressDialog pd;
    //  setup progress dialog
    pd = new ProgressDialog(FieldBookingActivity.this);
    //  progress dialog tidak dapat di cancel
    pd.setCancelable(false);
    //  isi teks progress dialog
    pd.setMessage("Mohon Tunggu ...");
    //  tampilkan progress dialog
    pd.show();

    APIRequestTransaction ardData = RetroServer.konekRetrofit().create(APIRequestTransaction.class);
    Call<ResponseModelTransaction> booking = ardData.booking(
        User.getUserId(),
        id,
        String.valueOf(startAtItems[(int) spStartAt.getSelectedItemId()]),
        String.valueOf(endAtItems[(int) spEndAt.getSelectedItemId()]),
        String.valueOf(longOfBooking),
        txtPrice.getText().toString(),
        String.valueOf(price),
        String.valueOf(discon),
        String.valueOf((price * longOfBooking) - discon)
    );

    //  deskripsi isi variabel "cl"
    booking.enqueue(new Callback<ResponseModelTransaction>() {
      //  ketika data berhasil diambil
      @Override
      public void onResponse(Call<ResponseModelTransaction> call, Response<ResponseModelTransaction> response) {
        //  tutup progress dialog
        pd.dismiss();
        Toast.makeText(FieldBookingActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable(){
          @Override
          public void run() {
            finish();
          }
        }, 1000);
      }
      //  ketika data gagal diambil
      @Override
      public void onFailure(Call<ResponseModelTransaction> call, Throwable t) {
        //  hilangkan progress dialog
        pd.dismiss();
        //  tampilkan pesan
        Toast.makeText(FieldBookingActivity.this, "Data gagal diproses!" + t.getMessage(), Toast.LENGTH_SHORT).show();

      }
    });
  }

}