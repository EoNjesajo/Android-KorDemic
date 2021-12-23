package com.podata.projectk.ui.notifications;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.podata.projectk.MainActivity;
import com.podata.projectk.R;
import com.podata.projectk.databinding.FragmentNotificationsBinding;
import com.podata.projectk.setting;

public class NotificationsFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_notifications, container, false);
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);




        ((ConstraintLayout)view.findViewById(R.id.seoul)).setOnClickListener(on_Click);
        ((ConstraintLayout)view.findViewById(R.id.incheon)).setOnClickListener(on_Click);
        ((ConstraintLayout)view.findViewById(R.id.daejeon)).setOnClickListener(on_Click);
        ((ConstraintLayout)view.findViewById(R.id.sejong)).setOnClickListener(on_Click);
        ((ConstraintLayout)view.findViewById(R.id.daegu)).setOnClickListener(on_Click);
        ((ConstraintLayout)view.findViewById(R.id.ulsan)).setOnClickListener(on_Click);
        ((ConstraintLayout)view.findViewById(R.id.busan)).setOnClickListener(on_Click);
        ((ConstraintLayout)view.findViewById(R.id.gwangju)).setOnClickListener(on_Click);
    }

     public final View.OnClickListener on_Click = new View.OnClickListener(){
           @SuppressLint("NonConstantResourceId")
           public void onClick(View v) {
               switch(v.getId()){
                   case R.id.seoul :
                       setting.setConfigValue(getContext(),"location","0");
                       break;
                   case R.id.incheon:
                       setting.setConfigValue(getContext(),"location","1");
                       break;
                   case R.id.daejeon:
                       setting.setConfigValue(getContext(),"location","2");
                       break;
                   case R.id.sejong:
                       setting.setConfigValue(getContext(),"location","3");
                       break;
                   case R.id.daegu:
                       setting.setConfigValue(getContext(),"location","4");
                       break;
                   case R.id.ulsan:
                       setting.setConfigValue(getContext(),"location","5");
                       break;
                   case R.id.busan:
                       setting.setConfigValue(getContext(),"location","6");
                       break;
                   case R.id.gwangju:
                       setting.setConfigValue(getContext(),"location","7");
                       break;
               }
               restart(getContext());
           }
     };
    private void restart(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
        ComponentName componentName = intent.getComponent();
        Intent mainIntent = Intent.makeRestartActivityTask(componentName);
        context.startActivity(mainIntent);
        Runtime.getRuntime().exit(0);
    }
}