package com.podata.projectk;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.podata.projectk.ui.dashboard.DashboardFragment;
import com.podata.projectk.ui.home.HomeFragment;
import com.podata.projectk.ui.notifications.NotificationsFragment;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

    private Fragment home, dash, noti;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, LoadingActivity.class); //로딩시작
        startActivity(intent);



        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);




        BottomNavigationView navView = findViewById(R.id.nav_view);


        //바텀 네비게이션에 따른 변환
        FragmentManager fragmentManager = getSupportFragmentManager();
        home = new HomeFragment();
        dash = new DashboardFragment();
        noti = new NotificationsFragment();
        fragmentManager.beginTransaction().replace(R.id.nav_host_fragment_activity_main,home).commit();
        fragmentManager.beginTransaction().add(R.id.nav_host_fragment_activity_main,dash).commit();
        fragmentManager.beginTransaction().hide(dash).commit();
        fragmentManager.beginTransaction().add(R.id.nav_host_fragment_activity_main,noti).commit();
        fragmentManager.beginTransaction().hide(noti).commit();

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) { //프래그먼트가 변경될 때마다 파싱하는 것을 막기위한 코드(프래그먼트를 새로 생성하는 것이 아닌 숨기고 보이는 식)
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        if(home == null) {
                            fragmentManager.beginTransaction().add(R.id.nav_host_fragment_activity_main,home).commit();
                        }
                        else
                            fragmentManager.beginTransaction().show(home).commit();
                        if (dash!= null) fragmentManager.beginTransaction().hide(dash).commit();
                        if (noti!= null) fragmentManager.beginTransaction().hide(noti).commit();
                        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.main));
                        return true;

                    case R.id.navigation_dashboard:
                        if(dash == null) {
                            fragmentManager.beginTransaction().add(R.id.nav_host_fragment_activity_main,dash).commit();
                        }
                        else
                            fragmentManager.beginTransaction().show(dash).commit();
                        if (home!= null) fragmentManager.beginTransaction().hide(home).commit();
                        if (noti!= null) fragmentManager.beginTransaction().hide(noti).commit();
                        window.setStatusBarColor(Color.parseColor("#EC7F61"));
                        return true;

                    case R.id.navigation_notifications:
                        if(noti == null) {
                            fragmentManager.beginTransaction().add(R.id.nav_host_fragment_activity_main,noti).commit();
                        }
                        else
                            fragmentManager.beginTransaction().show(noti).commit();
                        if (home!= null) fragmentManager.beginTransaction().hide(home).commit();
                        if (dash!= null) fragmentManager.beginTransaction().hide(dash).commit();
                        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.main));
                        return true;

                    default:
                        return false;
                }
            }
        });
    }
}