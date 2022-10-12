package com.bsc_bandarlampungsportcenter.rest_api;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bsc_bandarlampungsportcenter.FieldDetailActivity;
import com.bsc_bandarlampungsportcenter.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FieldAdapter extends RecyclerView.Adapter<FieldAdapter.HolderData> {

  private Context ctx;
  private List<FieldModel> listFields;
  Intent intent;

  public FieldAdapter(Context ctx, List<FieldModel> listFields) {
    this.ctx = ctx;
    this.listFields = listFields;
  }

  @NonNull
  @Override
  public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.field_item, parent,false);
    HolderData holder = new HolderData(layout);
    return holder;
  }

  @Override
  public void onBindViewHolder(@NonNull HolderData holder, int position) {
    FieldModel model = listFields.get(position);
    holder.id.setText(String.valueOf(model.getId()));
    holder.name.setText(model.getName());
    Picasso.get().load(RetroServer.getBASE_URL_FILE() + model.getPhoto()).into(holder.photo);

    holder.fieldItem.setOnClickListener(view -> {

      intent = new Intent(ctx, FieldDetailActivity.class);
      intent.putExtra("id", model.getId());
      ctx.startActivity(intent);

    });

  }

  @Override
  public int getItemCount() {
    return listFields.size();
  }


  public class HolderData extends RecyclerView.ViewHolder{
    TextView id, name;
    ImageView photo;
    CardView fieldItem;

    public HolderData(@NonNull View itemView) {
      super(itemView);

      fieldItem = itemView.findViewById(R.id.field_item);
      id = itemView.findViewById(R.id.id);
      name = itemView.findViewById(R.id.name);
      photo = itemView.findViewById(R.id.photo);

    }
  }




}
