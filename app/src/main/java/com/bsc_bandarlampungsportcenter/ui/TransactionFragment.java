package com.bsc_bandarlampungsportcenter.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bsc_bandarlampungsportcenter.R;
import com.bsc_bandarlampungsportcenter.databinding.FragmentTransactionBinding;
import com.bsc_bandarlampungsportcenter.rest_api.APIRequestTransaction;
import com.bsc_bandarlampungsportcenter.rest_api.TransactionAdapter;
import com.bsc_bandarlampungsportcenter.rest_api.TransactionModel;
import com.bsc_bandarlampungsportcenter.rest_api.ResponseModelTransaction;
import com.bsc_bandarlampungsportcenter.rest_api.RetroServer;
import com.bsc_bandarlampungsportcenter.session.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionFragment extends Fragment {

  private FragmentTransactionBinding binding;

  View vw;

  RecyclerView rcv_data;
  RecyclerView.Adapter ad_data;
  RecyclerView.LayoutManager lm_data;
  List<TransactionModel> list_transaction = new ArrayList<>();

  TextView txtBlank;

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
    vw = inflater.inflate(R.layout.fragment_transaction, container, false);

    txtBlank = vw.findViewById(R.id.txt_blank);

    rcv_data = vw.findViewById(R.id.rcv_data);
    lm_data = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

    rcv_data.setLayoutManager(lm_data);

    tampilData(User.getUserId());

    return vw;
  }

  @Override
  public void onResume() {
    tampilData(User.getUserId());
    super.onResume();
  }

  public void tampilData (String id)
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

    APIRequestTransaction ardData = RetroServer.konekRetrofit().create(APIRequestTransaction.class);
    Call<ResponseModelTransaction> tampilData = ardData.getAllTransactions(id);

    //  deskripsi isi variabel "cl"
    tampilData.enqueue(new Callback<ResponseModelTransaction>() {
      //  ketika data berhasil diambil
      @Override
      public void onResponse(Call<ResponseModelTransaction> call, Response<ResponseModelTransaction> response) {
        //  tampilkan data ke dalam list
        list_transaction = response.body().getData();
        int count = response.body().getCount();
        if( count > 0 ) {
          txtBlank.setVisibility(View.GONE);
          rcv_data.setVisibility(View.VISIBLE);
        }else{
          rcv_data.setVisibility(View.GONE);
          txtBlank.setVisibility(View.VISIBLE);
        }

        //  mengisi data adapter dari list
        ad_data = new TransactionAdapter(getContext(), list_transaction, TransactionFragment.this);
        rcv_data.setAdapter(ad_data);
        ad_data.notifyDataSetChanged();

        //  tutup progress dialog
        pd.dismiss();
      }
      //  ketika data gagal diambil
      @Override
      public void onFailure(Call<ResponseModelTransaction> call, Throwable t) {
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