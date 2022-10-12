package com.bsc_bandarlampungsportcenter;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bsc_bandarlampungsportcenter.databinding.ActivityMainBinding;
import com.bsc_bandarlampungsportcenter.session.Price;
import com.bsc_bandarlampungsportcenter.session.User;

public class MainActivity extends AppCompatActivity {

  private ActivityMainBinding binding;

  DBConfig config;
  SQLiteDatabase db;
  Cursor cursor;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    Price.setPrice(MainActivity.this);
    super.onCreate(savedInstanceState);
    config = new DBConfig(this);

    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

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
    User.setUserId(cursor.getString(0));
    User.setIsAdmin(cursor.getString(1));

  }

}