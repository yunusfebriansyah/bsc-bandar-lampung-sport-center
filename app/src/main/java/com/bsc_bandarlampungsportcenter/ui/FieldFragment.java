package com.bsc_bandarlampungsportcenter.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.widget.TextView;
import android.widget.Toast;

import com.bsc_bandarlampungsportcenter.R;
import com.bsc_bandarlampungsportcenter.databinding.FragmentFieldBinding;
import com.bsc_bandarlampungsportcenter.rest_api.APIRequestField;
import com.bsc_bandarlampungsportcenter.rest_api.FieldAdapter;
import com.bsc_bandarlampungsportcenter.rest_api.FieldModel;
import com.bsc_bandarlampungsportcenter.rest_api.ResponseModelField;
import com.bsc_bandarlampungsportcenter.rest_api.RetroServer;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FieldFragment extends Fragment {

  private FragmentFieldBinding binding;

  View vw;
  RecyclerView rcv_data;
  RecyclerView.Adapter ad_data;
  RecyclerView.LayoutManager lm_data;
  List<FieldModel> list_field = new ArrayList<>();

  TextView txtBlank;

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
    vw = inflater.inflate(R.layout.fragment_field, container, false);

    txtBlank = vw.findViewById(R.id.txt_blank);

    rcv_data = vw.findViewById(R.id.rcv_data);
    lm_data = new GridLayoutManager(getActivity(), 2);

    rcv_data.setLayoutManager(lm_data);

    tampilData();

    return vw;
  }

  void tampilData ()
  {
    //  deklarasi variabel komponen "Progress Dialog"
    ProgressDialog pd;
    //  setup progress dialog
    pd = new ProgressDialog(getActivity());
    //  progress dialog tidak dapat di cancel
    pd.setCancelable(false);
    //  isi teks progress dialog
    pd.setMessage("Mohon Tunggu ...");
    //  tampilkan progress dialog
    pd.show();

    APIRequestField ardData = RetroServer.konekRetrofit().create(APIRequestField.class);
    Call<ResponseModelField> tampilData = ardData.tampilData();

    //  deskripsi isi variabel "cl"
    tampilData.enqueue(new Callback<ResponseModelField>() {
      //  ketika data berhasil diambil
      @Override
      public void onResponse(Call<ResponseModelField> call, Response<ResponseModelField> response) {
        //  tampilkan data ke dalam list
        list_field = response.body().getData();
        int count = response.body().getCount();
        if( count > 0 ) {
          txtBlank.setVisibility(View.GONE);
          rcv_data.setVisibility(View.VISIBLE);
        }else{
          rcv_data.setVisibility(View.GONE);
          txtBlank.setVisibility(View.VISIBLE);
        }

        //  mengisi data adapter dari list
        ad_data = new FieldAdapter(getContext(), list_field);
        rcv_data.setAdapter(ad_data);
        ad_data.notifyDataSetChanged();

        //  tutup progress dialog
        pd.dismiss();
      }
      //  ketika data gagal diambil
      @Override
      public void onFailure(Call<ResponseModelField> call, Throwable t) {
        //  hilangkan progress dialog
        pd.dismiss();
        //  tampilkan pesan
        Toast.makeText(getActivity(), "Data gagal ditampilkan!" + t.getMessage(), Toast.LENGTH_SHORT).show();

      }
    });
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}