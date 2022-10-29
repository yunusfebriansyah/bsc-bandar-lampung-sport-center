package com.bsc_bandarlampungsportcenter.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.bsc_bandarlampungsportcenter.R;
import com.bsc_bandarlampungsportcenter.SliderAdapter;
import com.bsc_bandarlampungsportcenter.SliderItem;
import com.bsc_bandarlampungsportcenter.databinding.FragmentHomeBinding;
import com.bsc_bandarlampungsportcenter.session.Price;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

  private FragmentHomeBinding binding;

  List<SliderItem> sliderItems = new ArrayList<>();

  View vw;
  Intent intent;
  TextView txtLinkGoogleMap, txtPrice;
  ViewPager2 viewPager2;

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
    vw = inflater.inflate(R.layout.fragment_home, container, false);

    txtLinkGoogleMap = vw.findViewById(R.id.txt_link_google_map);
    txtPrice = vw.findViewById(R.id.txt_price);
    viewPager2 = vw.findViewById(R.id.viewPager);

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
}