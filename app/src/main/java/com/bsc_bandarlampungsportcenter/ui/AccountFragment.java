package com.bsc_bandarlampungsportcenter.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bsc_bandarlampungsportcenter.AddAdminActivity;
import com.bsc_bandarlampungsportcenter.ChangePasswordActivity;
import com.bsc_bandarlampungsportcenter.ChangeProfilePhotoActivity;
import com.bsc_bandarlampungsportcenter.DBConfig;
import com.bsc_bandarlampungsportcenter.EditProfileActivity;
import com.bsc_bandarlampungsportcenter.LoginActivity;
import com.bsc_bandarlampungsportcenter.MainActivity;
import com.bsc_bandarlampungsportcenter.R;
import com.bsc_bandarlampungsportcenter.databinding.FragmentAccountBinding;
import com.bsc_bandarlampungsportcenter.rest_api.APIRequestUser;
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

  TextView txtName, txtUsername, txtEmail, txtCountSuccess, txtCountPending, txtCountFailed, labelTransaction;
  ImageView photoProfile;
  ScrollView scrollContainer;

  Button btnChangePassword, btnLogout, btnChangeAccount, btnChangePhoto, btnAddAdmin, btnNotLogin;

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {

    vw = inflater.inflate(R.layout.fragment_account, container, false);
    config = new DBConfig(getContext());
    db = config.getReadableDatabase();

    photoProfile = vw.findViewById(R.id.photo);
    txtName = vw.findViewById(R.id.name);
    txtUsername = vw.findViewById(R.id.username);
    txtEmail = vw.findViewById(R.id.email);
    txtCountSuccess = vw.findViewById(R.id.count_success);
    txtCountPending = vw.findViewById(R.id.count_pending);
    txtCountFailed = vw.findViewById(R.id.count_failed);
    labelTransaction = vw.findViewById(R.id.label_transaction);
    scrollContainer = vw.findViewById(R.id.container);

    btnChangePassword = vw.findViewById(R.id.btn_change_password);
    btnChangeAccount = vw.findViewById(R.id.btn_edit_profile);
    btnLogout = vw.findViewById(R.id.btn_logout);
    btnChangePhoto = vw.findViewById(R.id.btn_change_photo);
    btnAddAdmin = vw.findViewById(R.id.btn_add_admin);
    btnNotLogin = vw.findViewById(R.id.btn_not_login);


    cursor = db.rawQuery("SELECT * FROM tbl_user",null);
    cursor.moveToFirst();

    if( User.isAdmin() ){
      labelTransaction.setText("Rekap Transaksi");
      btnAddAdmin.setVisibility(View.VISIBLE);
    }

    btnChangePhoto.setOnClickListener(view -> {
      intent = new Intent(getActivity(), ChangeProfilePhotoActivity.class);
      startActivity(intent);
    });

    btnChangeAccount.setOnClickListener(view -> {
      intent = new Intent(getActivity(), EditProfileActivity.class);
      startActivity(intent);
    });

    btnChangePassword.setOnClickListener(view -> {
      intent = new Intent(getActivity(), ChangePasswordActivity.class);
      startActivity(intent);
    });

    btnAddAdmin.setOnClickListener(view -> {
      intent = new Intent(getActivity(), AddAdminActivity.class);
      startActivity(intent);
    });

    btnLogout.setOnClickListener( view -> {
      db = config.getReadableDatabase();

      db.execSQL("DELETE FROM tbl_user");

      intent = new Intent(getActivity(), MainActivity.class);
//      intent.putExtra("message", "Logout berhasil.");
      startActivity(intent);
      getActivity().finish();
    });

    btnNotLogin.setOnClickListener(view -> {
      intent = new Intent(getActivity(), LoginActivity.class);
      intent.putExtra("message", "");
      startActivity(intent);
    });

    if(cursor.getCount() == 1) {
      tampilData(User.getUserId());
    }else{
      btnNotLogin.setVisibility(View.VISIBLE);
    }

    return vw;
  }

  @Override
  public void onResume() {
//    if(cursor.getCount() == 1) {
//      tampilData(User.getUserId());
//      container.setVisibility(View.VISIBLE);
//    }else{
//      btnNotLogin.setVisibility(View.VISIBLE);
//    }
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

        scrollContainer.setVisibility(View.VISIBLE);

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