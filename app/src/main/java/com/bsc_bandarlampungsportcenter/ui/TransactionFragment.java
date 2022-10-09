package com.bsc_bandarlampungsportcenter.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.bsc_bandarlampungsportcenter.R;
import com.bsc_bandarlampungsportcenter.databinding.FragmentTransactionBinding;

public class TransactionFragment extends Fragment {

  private FragmentTransactionBinding binding;

  View vw;

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
    vw = inflater.inflate(R.layout.fragment_transaction, container, false);

    return vw;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}