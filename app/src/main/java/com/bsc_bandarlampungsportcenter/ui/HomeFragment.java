package com.bsc_bandarlampungsportcenter.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.bsc_bandarlampungsportcenter.R;
import com.bsc_bandarlampungsportcenter.SliderAdapter;
import com.bsc_bandarlampungsportcenter.SliderItem;
import com.bsc_bandarlampungsportcenter.databinding.FragmentHomeBinding;
import com.bsc_bandarlampungsportcenter.rest_api.APIRequestField;
import com.bsc_bandarlampungsportcenter.rest_api.FieldAdapter;
import com.bsc_bandarlampungsportcenter.rest_api.FieldModel;
import com.bsc_bandarlampungsportcenter.rest_api.ResponseModelField;
import com.bsc_bandarlampungsportcenter.rest_api.RetroServer;
import com.bsc_bandarlampungsportcenter.session.Price;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

  private FragmentHomeBinding binding;

  List<SliderItem> sliderItems = new ArrayList<>();

  View vw;
  Intent intent;
  TextView txtLinkGoogleMap, txtPrice, txtBlank;
  ViewPager2 viewPager2;
  RecyclerView rcv_data;
  RecyclerView.Adapter ad_data;
  RecyclerView.LayoutManager lm_data;
  List<FieldModel> list_field = new ArrayList<>();

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
    vw = inflater.inflate(R.layout.fragment_home, container, false);

    txtLinkGoogleMap = vw.findViewById(R.id.txt_link_google_map);
    txtPrice = vw.findViewById(R.id.txt_price);
    viewPager2 = vw.findViewById(R.id.viewPager);
    txtBlank = vw.findViewById(R.id.txt_blank);
    rcv_data = vw.findViewById(R.id.rcv_data);

    txtLinkGoogleMap.setOnClickListener(view -> {
      intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(txtLinkGoogleMap.getText().toString()));
      startActivity(intent);
    });

    Price.setPrice(getActivity());

    sliderItems.add(new SliderItem(R.drawable.slider1));
    sliderItems.add(new SliderItem(R.drawable.slider2));
    sliderItems.add(new SliderItem(R.drawable.slider3));
    sliderItems.add(new SliderItem(R.drawable.slider4));
    sliderItems.add(new SliderItem(R.drawable.slider5));
    sliderItems.add(new SliderItem(R.drawable.slider6));
    sliderItems.add(new SliderItem(R.drawable.slider7));

    viewPager2.setAdapter(new SliderAdapter(sliderItems, viewPager2));
    Handler sliderHandler = new Handler();
    viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
      @Override
      public void onPageSelected(int position) {
        super.onPageSelected(position);
        sliderHandler.removeCallbacks(sliderRunnable);
        sliderHandler.postDelayed(sliderRunnable, 3000);
      }
    });

    Handler handler = new Handler();
    handler.postDelayed(new Runnable() {
      public void run() {
        txtPrice.setText(String.valueOf(Price.getPriceMoney()));
      }
    }, 1000);

    lm_data = new GridLayoutManager(getActivity(), 2);

    rcv_data.setLayoutManager(lm_data);

    tampilData();

    return vw;
  }

  private Runnable sliderRunnable = new Runnable() {
    int position = 1;
    @Override
    public void run() {
      if( position == sliderItems.size() ) {
        viewPager2.setCurrentItem(1, true);
        position = 1;
      }else{
        viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1, true);
        position++;
      }
    }
  };

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }

  public void tampilData ()
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
          txtBlank.setVisibility(View.VISIBLE);
          rcv_data.setVisibility(View.GONE);
        }

        //  mengisi data adapter dari list
        ad_data = new FieldAdapter(getContext(), list_field, HomeFragment.this);
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

}