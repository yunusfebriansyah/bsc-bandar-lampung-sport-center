package com.bsc_bandarlampungsportcenter;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bsc_bandarlampungsportcenter.databinding.ActivityMainBinding;
import com.bsc_bandarlampungsportcenter.session.Price;
import com.bsc_bandarlampungsportcenter.session.User;
import com.bsc_bandarlampungsportcenter.ui.TransactionFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

  private ActivityMainBinding binding;

  DBConfig config;
  SQLiteDatabase db;
  Cursor cursor;

  Bundle bundle;

  FrameLayout fg;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    Price.setPrice(MainActivity.this);
    super.onCreate(savedInstanceState);
    config = new DBConfig(this);

    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    fg = findViewById(R.id.nav_host_fragment_activity_main);

    BottomNavigationView navView = findViewById(R.id.nav_view);
    // Passing each menu ID as a set of Ids because each
    // menu should be considered as top level destinations.
    AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
            R.id.navigation_home, R.id.navigation_field, R.id.navigation_transaction, R.id.navigation_account)
            .build();
    NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
    NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    NavigationUI.setupWithNavController(binding.navView, navController);

    // hide action bar
    getSupportActionBar().hide();
    db = config.getReadableDatabase();
    cursor = db.rawQuery("SELECT * FROM tbl_user",null);
    cursor.moveToFirst();
    if(cursor.getCount() == 1) {
      User.setUserId(cursor.getString(0));
      User.setIsAdmin(cursor.getString(1));
    }else{
      User.setIsAdmin("0");
      navView.getMenu().removeItem(R.id.navigation_transaction);
    }

  }

  public void changeFragment()
  {
    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_main, new TransactionFragment()).commit();
  }



}