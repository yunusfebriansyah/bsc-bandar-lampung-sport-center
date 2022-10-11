package com.bsc_bandarlampungsportcenter.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bsc_bandarlampungsportcenter.DBConfig;
import com.bsc_bandarlampungsportcenter.EditProfileActivity;
import com.bsc_bandarlampungsportcenter.FieldDetailActivity;
import com.bsc_bandarlampungsportcenter.LoginActivity;
import com.bsc_bandarlampungsportcenter.R;
import com.bsc_bandarlampungsportcenter.databinding.FragmentAccountBinding;
import com.bsc_bandarlampungsportcenter.rest_api.APIRequestUser;
import com.bsc_bandarlampungsportcenter.rest_api.FieldModel;
import com.bsc_bandarlampungsportcenter.rest_api.ResponseModelUser;
import com.bsc_bandarlampungsportcenter.rest_api.RetroServer;
import com.bsc_bandarlampungsportcenter.rest_api.UserModel;
import com.bsc_bandarlampungsportcenter.session.User;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountFragment extends Fragment {

  private FragmentAccountBinding binding;

  View vw;
  Intent intent;

  DBConfig config;
  SQLiteDatabase db;
  Cursor cursor;

  TextView txtName, txtUsername, txtEmail, txtCountSuccess, txtCountPending, txtCountFailed;
  ImageView photoProfile;

  Button btnChangePassword, btnLogout, btnChangeAccount;

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
    vw = inflater.inflate(R.layout.fragment_account, container, false);
    config = new DBConfig(getContext());

    photoProfile = vw.findViewById(R.id.photo);
    txtName = vw.findViewById(R.id.name);
    txtUsername = vw.findViewById(R.id.username);
    txtEmail = vw.findViewById(R.id.email);
    txtCountSuccess = vw.findViewById(R.id.count_success);
    txtCountPending = vw.findViewById(R.id.count_pending);
    txtCountFailed = vw.findViewById(R.id.count_failed);

    btnChangePassword = vw.findViewById(R.id.btn_change_password);
    btnChangeAccount = vw.findViewById(R.id.btn_edit_profile);
    btnLogout = vw.findViewById(R.id.btn_logout);

    btnChangeAccount.setOnClickListener(view -> {
      intent = new Intent(getActivity(), EditProfileActivity.class);
      startActivity(intent);
    });

    btnLogout.setOnClickListener( view -> {
      db = config.getReadableDatabase();

      db.execSQL("DELETE FROM tbl_user");

      intent = new Intent(getActivity(), LoginActivity.class);
      intent.putExtra("message", "Logout berhasil.");
      startActivity(intent);
      getActivity().finish();
    });

    tampilData(User.getUserId());
    return vw;
  }

  @Override
  public void onResume() {
    tampilData(User.getUserId());
    super.onResume();
  }

  void tampilData (String id)
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

    APIRequestUser ardData = RetroServer.konekRetrofit().create(APIRequestUser.class);
    Call<ResponseModelUser> detailData = ardData.getUserDetail(id);

    //  deskripsi isi variabel "cl"
    detailData.enqueue(new Callback<ResponseModelUser>() {
      //  ketika data berhasil diambil
      @Override
      public void onResponse(Call<ResponseModelUser> call, Response<ResponseModelUser> response) {
        //  tampilkan data ke dalam list
        UserModel data = response.body().getData().get(0);
        Picasso.get().load(RetroServer.getBASE_URL_FILE() + String.valueOf(data.getPhoto())).into(photoProfile);
        txtName.setText(String.valueOf(data.getName()));
        txtUsername.setText(String.valueOf(data.getUsername()));
        txtEmail.setText(String.valueOf(data.getEmail()));
        txtCountSuccess.setText(String.valueOf(data.getCount_success()));
        txtCountPending.setText(String.valueOf(data.getCount_pending()));
        txtCountFailed.setText(String.valueOf(data.getCount_failed()));

        //  tutup progress dialog
        pd.dismiss();
      }
      //  ketika data gagal diambil
      @Override
      public void onFailure(Call<ResponseModelUser> call, Throwable t) {
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