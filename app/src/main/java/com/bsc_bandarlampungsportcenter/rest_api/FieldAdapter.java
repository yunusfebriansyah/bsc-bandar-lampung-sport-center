package com.bsc_bandarlampungsportcenter.rest_api;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bsc_bandarlampungsportcenter.ChangeFieldActivity;
import com.bsc_bandarlampungsportcenter.FieldDetailActivity;
import com.bsc_bandarlampungsportcenter.R;
import com.bsc_bandarlampungsportcenter.session.User;
import com.bsc_bandarlampungsportcenter.ui.FieldFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FieldAdapter extends RecyclerView.Adapter<FieldAdapter.HolderData> {

  private Context ctx;
  private Fragment fg;
  private List<FieldModel> listFields;
  Intent intent;
  AlertDialog.Builder builder;

  public FieldAdapter(Context ctx, List<FieldModel> listFields, Fragment fg) {
    this.ctx = ctx;
    this.listFields = listFields;
    this.fg = fg;
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

    if( User.isAdmin() ) {
      holder.fieldItem.setOnClickListener(view -> {
        builder = new AlertDialog.Builder(view.getContext());
        final CharSequence[] dialogItem = {"Lihat detail", "Ubah", "Hapus"};
        builder.setTitle("Aksi lapangan...");
        builder.setItems(dialogItem, (dialog, which) -> {
          switch (which) {
            case 0:
              intent = new Intent(ctx, FieldDetailActivity.class);
              intent.putExtra("id", model.getId());
              ctx.startActivity(intent);
              break;

            case 1:
              intent = new Intent(ctx, ChangeFieldActivity.class);
              intent.putExtra("id", model.getId());
              ctx.startActivity(intent);
              break;

            case 2:
              AlertDialog.Builder confirmDelete = new AlertDialog.Builder(view.getContext());
              final CharSequence[] confirmDeleteItem = {"Ya", "Batal"};
              confirmDelete.setTitle("Yakin ingin menghapus lapangan?");
              confirmDelete.setItems(confirmDeleteItem,(dialogConfirm, whichConfirm)->{
                switch (whichConfirm) {
                  case 0:
                    deleteField(holder.id.getText().toString());
                    ((FieldFragment)fg).tampilData();
                    break;
                }
              });
              confirmDelete.create().show();
              break;

          }
        });

        builder.create().show();
      });
    }else{
      holder.fieldItem.setOnClickListener(view -> {

        intent = new Intent(ctx, FieldDetailActivity.class);
        intent.putExtra("id", model.getId());
        ctx.startActivity(intent);

      });
    }

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


  void deleteField (String id)
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

    APIRequestField ardData = RetroServer.konekRetrofit().create(APIRequestField.class);
    Call<ResponseModelField> deleteField = ardData.deleteField(id);

    //  deskripsi isi variabel "cl"
    deleteField.enqueue(new Callback<ResponseModelField>() {
      //  ketika data berhasil diambil
      @Override
      public void onResponse(Call<ResponseModelField> call, Response<ResponseModelField> response) {
        //  tampilkan data ke dalam list
        Toast.makeText(ctx, response.body().getMessage(), Toast.LENGTH_SHORT).show();
        ((FieldFragment)fg).tampilData();
        //  tutup progress dialog
        pd.dismiss();
      }
      //  ketika data gagal diambil
      @Override
      public void onFailure(Call<ResponseModelField> call, Throwable t) {
        //  hilangkan progress dialog
        pd.dismiss();
        //  tampilkan pesan
        Toast.makeText(ctx, "Data gagal diproses! " + t.getMessage(), Toast.LENGTH_SHORT).show();

      }
    });
  }




}
