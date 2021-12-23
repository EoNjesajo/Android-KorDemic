package com.podata.projectk;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.podata.projectk.ui.home.HomeFragment;

public class LoadingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        startLoading();


    }
    private void startLoading() {
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                if(HomeFragment.loading) {
                    finish();
//                }
            }
        }, 3000);

//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if(HomeFragment.loading) {
//                    finish();
//                }
//            }
//        }, 5000);
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if(HomeFragment.loading) {
//                    finish();
//                }else {
//                    Toast.makeText(getApplicationContext(), "네트워크 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
//                    exit();
//                }
//
//            }
//        }, 7000);


    }
    public void onBackPressed(){
        exit();
    }
    public void exit(){
        ActivityCompat.finishAffinity(this);
        System.exit(0);
    }

}
