package com.bsc_bandarlampungsportcenter.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.ViewModelProvider;

import com.bsc_bandarlampungsportcenter.R;
import com.bsc_bandarlampungsportcenter.databinding.FragmentHomeBinding;
import com.bsc_bandarlampungsportcenter.session.Price;

public class HomeFragment extends Fragment {

  private FragmentHomeBinding binding;

  View vw;
  Intent intent;
  TextView txtLinkGoogleMap, txtPrice;

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
    vw = inflater.inflate(R.layout.fragment_home, container, false);

    txtLinkGoogleMap = vw.findViewById(R.id.txt_link_google_map);
    txtPrice = vw.findViewById(R.id.txt_price);

    txtLinkGoogleMap.setOnClickListener(view -> {
      intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(txtLinkGoogleMap.getText().toString()));
      startActivity(intent);
    });

    txtPrice.setText(String.valueOf(Price.getPriceMoney()));

    return vw;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}