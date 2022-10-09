package com.bsc_bandarlampungsportcenter.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.bsc_bandarlampungsportcenter.R;
import com.bsc_bandarlampungsportcenter.databinding.FragmentFieldBinding;

public class FieldFragment extends Fragment {

  private FragmentFieldBinding binding;

  View vw;

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
    vw = inflater.inflate(R.layout.fragment_field, container, false);

    return vw;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}