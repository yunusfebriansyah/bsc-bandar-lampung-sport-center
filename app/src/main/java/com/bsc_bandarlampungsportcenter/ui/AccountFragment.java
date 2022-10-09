package com.bsc_bandarlampungsportcenter.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bsc_bandarlampungsportcenter.R;
import com.bsc_bandarlampungsportcenter.databinding.FragmentAccountBinding;

public class AccountFragment extends Fragment {

  private FragmentAccountBinding binding;

  View vw;

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
    vw = inflater.inflate(R.layout.fragment_account, container, false);

    return vw;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}