package com.bsc_bandarlampungsportcenter.rest_api;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bsc_bandarlampungsportcenter.R;
import com.bsc_bandarlampungsportcenter.TransactionDetailActivity;

import java.text.NumberFormat;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.HolderData> {

  private Context ctx;
  private List<TransactionModel> listTransactions;
  Intent intent;

  public TransactionAdapter(Context ctx, List<TransactionModel> listTransactions) {
    this.ctx = ctx;
    this.listTransactions = listTransactions;
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

    holder.transactionItem.setOnClickListener(view -> {

      intent = new Intent(ctx, TransactionDetailActivity.class);
      intent.putExtra("id", model.getId());
      ctx.startActivity(intent);

    });

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




}
