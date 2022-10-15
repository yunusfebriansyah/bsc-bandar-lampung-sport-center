package com.bsc_bandarlampungsportcenter.rest_api;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bsc_bandarlampungsportcenter.MainActivity;
import com.bsc_bandarlampungsportcenter.R;
import com.bsc_bandarlampungsportcenter.TransactionDetailActivity;
import com.bsc_bandarlampungsportcenter.session.User;
import com.bsc_bandarlampungsportcenter.ui.TransactionFragment;

import java.text.NumberFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.HolderData> {

  private Context ctx;
  Fragment fg;

  AlertDialog.Builder builder;

  private List<TransactionModel> listTransactions;
  Intent intent;

  public TransactionAdapter(Context ctx, List<TransactionModel> listTransactions, Fragment fg) {
    this.ctx = ctx;
    this.listTransactions = listTransactions;
    this.fg = fg;
  }

  @NonNull
  @Override
  public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_item, parent,false);
    HolderData holder = new HolderData(layout);
    return holder;
  }

  @Override
  public void onBindViewHolder(@NonNull HolderData holder, int position) {
    TransactionModel model = listTransactions.get(position);
    holder.id.setText(String.valueOf(model.getId()));
    holder.transactionCode.setText(String.valueOf(model.getTransaction_code()));
    holder.createdAt.setText(String.valueOf(model.getBooking_date()));
    holder.fieldName.setText(String.valueOf(model.getField_name()));
    holder.bookingTime.setText("Jam : " + model.getStart_at() + ":00 - " + model.getEnd_at() + ":00");
    holder.totalPrice.setText("Rp. " + NumberFormat.getInstance().format(model.getPrice_total()));

    if( model.getStatus().equalsIgnoreCase("lunas") ) {
      holder.successStatus.setText(model.getStatus());
      holder.successStatus.setVisibility(View.VISIBLE);
    }else if(model.getStatus().equalsIgnoreCase("menunggu pembayaran")){
      holder.pendingStatus.setText(model.getStatus());
      holder.pendingStatus.setVisibility(View.VISIBLE);
    }else{
      holder.failedStatus.setText(model.getStatus());
      holder.failedStatus.setVisibility(View.VISIBLE);
    }

    if(User.isAdmin() && model.getStatus().equalsIgnoreCase("menunggu pembayaran") ) {

      holder.transactionItem.setOnClickListener(view -> {

          builder = new AlertDialog.Builder(view.getContext());
          final CharSequence[] dialogItem = {"Lihat detail", "Setujui", "Tolak"};
          builder.setTitle("Aksi transaksi...");
          builder.setItems(dialogItem, (dialog, which) -> {
            switch (which) {
              case 0:
                intent = new Intent(ctx, TransactionDetailActivity.class);
                intent.putExtra("id", model.getId());
                ctx.startActivity(intent);
                break;

              case 1:
                AlertDialog.Builder confirmAccept = new AlertDialog.Builder(view.getContext());
                final CharSequence[] confirmAcceptItem = {"Ya", "Batal"};
                confirmAccept.setTitle("Yakin ingin menyetujui transaksi?");
                confirmAccept.setItems(confirmAcceptItem,(dialogConfirm, whichConfirm)->{
                  switch (whichConfirm) {
                    case 0:
                      changeTransaction(holder.id.getText().toString(),"lunas");
                      ((TransactionFragment)fg).tampilData(User.getUserId());
                      break;
                  }
                });
                confirmAccept.create().show();
                break;

              case 2:
                AlertDialog.Builder confirmDenied = new AlertDialog.Builder(view.getContext());
                final CharSequence[] confirmDeniedItem = {"Ya", "Batal"};
                confirmDenied.setTitle("Yakin ingin menolak transaksi?");
                confirmDenied.setItems(confirmDeniedItem,(dialogConfirm, whichConfirm)->{
                  switch (whichConfirm) {
                    case 0:
                      changeTransaction(holder.id.getText().toString(),"ditolak");
                      ((TransactionFragment)fg).tampilData(User.getUserId());
                      break;
                  }
                });
                confirmDenied.create().show();
                break;

            }
          });

          builder.create().show();

      });

    }else {
      holder.transactionItem.setOnClickListener(view -> {

        intent = new Intent(ctx, TransactionDetailActivity.class);
        intent.putExtra("id", model.getId());
        ctx.startActivity(intent);

      });
    }

  }

  @Override
  public int getItemCount() {
    return listTransactions.size();
  }


  public class HolderData extends RecyclerView.ViewHolder{
    TextView id, transactionCode, successStatus, pendingStatus, failedStatus, createdAt, fieldName, bookingTime, totalPrice;
    CardView transactionItem;

    public HolderData(@NonNull View itemView) {
      super(itemView);

      transactionItem = itemView.findViewById(R.id.transaction_item);
      id = itemView.findViewById(R.id.id);
      transactionCode = itemView.findViewById(R.id.transaction_code);
      successStatus = itemView.findViewById(R.id.success_status);
      pendingStatus = itemView.findViewById(R.id.pending_status);
      failedStatus = itemView.findViewById(R.id.failed_status);
      createdAt = itemView.findViewById(R.id.created_at);
      fieldName = itemView.findViewById(R.id.field_name);
      bookingTime = itemView.findViewById(R.id.booking_time);
      totalPrice = itemView.findViewById(R.id.total_price);

    }
  }

  void changeTransaction (String id, String status)
  {
    //  deklarasi variabel komponen "Progress Dialog"
    ProgressDialog pd;
    //  setup progress dialog
    pd = new ProgressDialog(ctx);
    //  progress dialog tidak dapat di cancel
    pd.setCancelable(false);
    //  isi teks progress dialog
    pd.setMessage("Mohon Tunggu ...");
    //  tampilkan progress dialog
    pd.show();

    APIRequestTransaction ardData = RetroServer.konekRetrofit().create(APIRequestTransaction.class);
    Call<ResponseModelTransaction> updateData = ardData.update(id, status);

    //  deskripsi isi variabel "cl"
    updateData.enqueue(new Callback<ResponseModelTransaction>() {
      //  ketika data berhasil diambil
      @Override
      public void onResponse(Call<ResponseModelTransaction> call, Response<ResponseModelTransaction> response) {
        //  tutup progress dialog
        pd.dismiss();
        Toast.makeText(ctx, response.body().getMessage(), Toast.LENGTH_SHORT).show();

      }
      //  ketika data gagal diambil
      @Override
      public void onFailure(Call<ResponseModelTransaction> call, Throwable t) {
        //  hilangkan progress dialog
        pd.dismiss();
        //  tampilkan pesan
        Toast.makeText(ctx, "Gagal diproses!" + t.getMessage(), Toast.LENGTH_SHORT).show();

      }
    });
  }




}
