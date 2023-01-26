package com.bsc_bandarlampungsportcenter.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bsc_bandarlampungsportcenter.R;
import com.bsc_bandarlampungsportcenter.SliderAdapter;
import com.bsc_bandarlampungsportcenter.SliderItem;
import com.bsc_bandarlampungsportcenter.databinding.FragmentHomeBinding;
import com.bsc_bandarlampungsportcenter.rest_api.APIRequestField;
import com.bsc_bandarlampungsportcenter.rest_api.APIRequestTransaction;
import com.bsc_bandarlampungsportcenter.rest_api.FieldAdapter;
import com.bsc_bandarlampungsportcenter.rest_api.FieldModel;
import com.bsc_bandarlampungsportcenter.rest_api.ResponseModelField;
import com.bsc_bandarlampungsportcenter.rest_api.ResponseModelTransaction;
import com.bsc_bandarlampungsportcenter.rest_api.RetroServer;
import com.bsc_bandarlampungsportcenter.session.Price;
import com.bsc_bandarlampungsportcenter.session.User;

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
  ConstraintLayout overlayAds;
  ImageView overlayImage1, overlayImage2, overlayImage3, overlayImage4, imageAds1, imageAds2, imageAds3, imageAds4;

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
    vw = inflater.inflate(R.layout.fragment_home, container, false);

    txtLinkGoogleMap = vw.findViewById(R.id.txt_link_google_map);
    txtPrice = vw.findViewById(R.id.txt_price);
    viewPager2 = vw.findViewById(R.id.viewPager);
    txtBlank = vw.findViewById(R.id.txt_blank);
    imageAds1 = vw.findViewById(R.id.img_ads_1);
    imageAds2 = vw.findViewById(R.id.img_ads_2);
    imageAds3 = vw.findViewById(R.id.img_ads_3);
    imageAds4 = vw.findViewById(R.id.img_ads_4);
    overlayAds = vw.findViewById(R.id.overlay_ads);
    overlayImage1 = vw.findViewById(R.id.img_overlay_1);
    overlayImage2 = vw.findViewById(R.id.img_overlay_2);
    overlayImage3 = vw.findViewById(R.id.img_overlay_3);
    overlayImage4 = vw.findViewById(R.id.img_overlay_4);

    txtLinkGoogleMap.setOnClickListener(view -> {
      intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(txtLinkGoogleMap.getText().toString()));
      startActivity(intent);
    });

    overlayAds.setOnClickListener(view -> {
      overlayImage1.setVisibility(View.GONE);
      overlayImage2.setVisibility(View.GONE);
      overlayImage3.setVisibility(View.GONE);
      overlayImage4.setVisibility(View.GONE);
      overlayAds.setVisibility(View.GONE);
    });

    imageAds1.setOnClickListener(view -> {
      overlayImage1.setVisibility(View.VISIBLE);
      overlayAds.setVisibility(View.VISIBLE);
    });

    imageAds2.setOnClickListener(view -> {
      overlayImage2.setVisibility(View.VISIBLE);
      overlayAds.setVisibility(View.VISIBLE);
    });

    imageAds3.setOnClickListener(view -> {
      overlayImage3.setVisibility(View.VISIBLE);
      overlayAds.setVisibility(View.VISIBLE);
    });

    imageAds4.setOnClickListener(view -> {
      overlayImage4.setVisibility(View.VISIBLE);
      overlayAds.setVisibility(View.VISIBLE);
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

    if( User.isAdmin() && !User.getIsDeniedYesterday()){
      deniedYesterday();
    }

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

  public void deniedYesterday ()
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
    Call<ResponseModelTransaction> deniedYesterday = ardData.deniedYesterday();

    //  deskripsi isi variabel "cl"
    deniedYesterday.enqueue(new Callback<ResponseModelTransaction>() {
      //  ketika data berhasil diambil
      @Override
      public void onResponse(Call<ResponseModelTransaction> call, Response<ResponseModelTransaction> response) {
        User.setIsDeniedYesterday(true);
        //  tutup progress dialog
        pd.dismiss();
      }
      //  ketika data gagal diambil
      @Override
      public void onFailure(Call<ResponseModelTransaction> call, Throwable t) {
        //  hilangkan progress dialog
        pd.dismiss();
        //  tampilkan pesan
        Toast.makeText(getActivity(), "Data gagal diproses!" + t.getMessage(), Toast.LENGTH_SHORT).show();

      }
    });
  }

}