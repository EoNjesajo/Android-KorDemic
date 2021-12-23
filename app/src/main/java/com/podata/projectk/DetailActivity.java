package com.podata.projectk;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    final Bundle bundle = new Bundle();
    TextView textView, textView_ad, textView_vs;
    private GoogleMap mMap;
    private Geocoder geocoder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        textView = findViewById(R.id.title_location_detail);
        textView_ad = findViewById(R.id.location_detail);
        textView_vs = findViewById(R.id.time);

        String name = getIntent().getStringExtra("name");
        String address = getIntent().getStringExtra("address");
        String visitTime = getIntent().getStringExtra("visitTime");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);


        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        textView.setText(name);
        textView_ad.setText(address);
        textView_vs.setText(visitTime);


        String str = textView_ad.getText().toString();

        List<Address> addressList = null;
        try{
            geocoder = new Geocoder(this, Locale.getDefault());
            addressList = geocoder.getFromLocationName(str, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }


        List<Address> finalAddressList = addressList;


        DetailActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run(){
                try{
                    String []splitStr = finalAddressList.get(0).toString().split(",");
                    String address2 = splitStr[0].substring(splitStr[0].indexOf("\"") + 1, splitStr[0].length() - 2);
                    String latitude = splitStr[10].substring(splitStr[10].indexOf("=") + 1); //에뮬은 14, 16이고 내 폰은 10, 12
                    String longitude = splitStr[12].substring(splitStr[12].indexOf("=") + 1);


                    bundle.putString("latitude", latitude);
                    bundle.putString("longitude", longitude);
                    bundle.putString("address2", address2);

                    Message msg = handler.obtainMessage();
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                }catch (Exception e) {
                    Toast.makeText(DetailActivity.this, "자세히 보기를 지원하지 않는 지역입니다.",Toast.LENGTH_SHORT).show();
                    finish();
                }


            }
        });
    }



    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            Bundle bundle = msg.getData();

            String latitude = bundle.getString("latitude");
            String longitude = bundle.getString("longitude");
            String address2 = bundle.getString("address2");


            LatLng point = null;
            try{
                point = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
            }catch(Exception e){
                Toast.makeText(DetailActivity.this, "자세히 보기를 지원하지 않는 지역입니다.",Toast.LENGTH_SHORT).show();
                finish();
            }
            
            MarkerOptions mOptions2 = new MarkerOptions();
            mOptions2.title("search result");
            mOptions2.snippet(address2);
            mOptions2.position(point);
            // 마커 추가
            mMap.addMarker(mOptions2);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point,17));
            return false;
        }
    });


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        geocoder = new Geocoder(this);

        LatLng target = new LatLng(36.350806, 127.300621);
        mMap.addMarker(new MarkerOptions()
                .position(target)
                .title("Marker"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(target, 16));

    }

    public void backBtn(View view){
        finish();
    }


}