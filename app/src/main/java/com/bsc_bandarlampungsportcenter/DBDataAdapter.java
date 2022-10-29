package com.bsc_bandarlampungsportcenter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DBDataAdapter extends RecyclerView.Adapter<DBDataAdapter.ViewHolder> {
    ArrayList id_list;


    public DBDataAdapter(ArrayList id_list){
        this.id_list = id_list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View vw = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
        return new ViewHolder(vw);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        final String id = (String) id_list.get(i);
        viewHolder.id.setText(id);


    }

    @Override
    public int getItemCount() {
        return id_list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView id;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.txt_id);
        }

    }
}
