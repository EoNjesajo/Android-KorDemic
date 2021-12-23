package com.podata.projectk.ui.dashboard;

//예외처리
import android.annotation.SuppressLint;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.podata.projectk.R;
import com.podata.projectk.setting;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;


@SuppressLint("SetTextI18n")
public class DashboardFragment extends Fragment {
    //위젯 변수 선언, 패턴은  TV_이름;
    TextView TV_KoreaTotal, TV_BaseDate, TV_Total, TV_Release, TV_Isolate, TV_RIP, TV_BeforeTotal;

    //파싱 코드 배열(@string), 패턴은 parsing_이름
    String[] parsing_Location, parsing_URL, parsing_BaseDate, parsing_KoreaTotal, parsing_Total,
            parsing_Release, parsing_Isolate, parsing_RIP, parsing_BeforeTotal;

    //그외 변수 선언
    int location; //지역값
    final Bundle bundle = new Bundle(); //쓰레드, 파싱을 위한 번들

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);

        location = Integer.parseInt(setting.getConfigValue(getContext(),"location")); //setting클래스를 활용하여 설정된 지역값 불러오기

        //@string에 있는 파싱 코드를 배열에 저장
        parsing_Location= getResources().getStringArray(R.array.location); //지역
        parsing_URL= getResources().getStringArray(R.array.URl2); //주소
        parsing_BaseDate= getResources().getStringArray(R.array.basetime); //기준시간
        parsing_KoreaTotal = getResources().getStringArray(R.array.korea_total); //한국총계
        parsing_Total= getResources().getStringArray(R.array.total); //지역총계
        parsing_Release= getResources().getStringArray(R.array.release); //격리자 수
        parsing_Isolate= getResources().getStringArray(R.array.isolate); //격리해제자 수
        parsing_RIP= getResources().getStringArray(R.array.rip); //사망자 수
        parsing_BeforeTotal= getResources().getStringArray(R.array.total_before); //전날 총계


        new Thread() {
            @Override
            public void run() {
                Parsing();
            }
        }.start();

        return v;
    }

    //프래그먼트의 위젯을 다루기 위한 부분
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((TextView)getView().findViewById(R.id.location2)).setText("지역현황("+parsing_Location[location]+")");
        ((SwipeRefreshLayout)view.findViewById(R.id.swipe2)).setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() { //스와이프레이아웃을 위로 당기면 다시 파싱하여 데이터 입력
                new Thread() {
                    @Override
                    public void run() {
                        Parsing();
                    }
                }.start();
                ((SwipeRefreshLayout)view.findViewById(R.id.swipe2)).setRefreshing(false); //작업이 완료되었고 스와이프레이아웃의 작업이 완료됬음을 표시(필수)
            }
        });
    }

    public void Parsing(){  //파싱하는 함수
        Document doc;
        try {
            doc = Jsoup.connect(parsing_URL[location]).get();

            String koreaTotal = Jsoup.connect(parsing_URL[location]).get().select(parsing_KoreaTotal[location]).text();
            String baseDate = doc.select(parsing_BaseDate[location]).text();
            String total = doc.select(parsing_Total[location]).text();
            String release = doc.select(parsing_Release[location]).text();
            String isolate = doc.select(parsing_Isolate[location]).text();
            String rip = doc.select(parsing_RIP[location]).text();
            String beforeTotal = doc.select(parsing_BeforeTotal[location]).text();

            bundle.putString("koreaTotal", koreaTotal);
            bundle.putString("base", baseDate);
            bundle.putString("total", total);
            bundle.putString("release", release);
            bundle.putString("isolate", isolate);
            bundle.putString("rip", rip);
            bundle.putString("beforeTotal", beforeTotal);

            Message msg = handler.obtainMessage();
            msg.setData(bundle);
            handler.sendMessage(msg);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    Handler handler = new Handler(new Handler.Callback() { //파싱한 데이터를 텍스트뷰에 정리해서 입력
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            Bundle bundle = msg.getData();

            TV_KoreaTotal = (TextView)getView().findViewById(R.id.korea_total);
            TV_Total = (TextView) getView().findViewById(R.id.total_num);
            TV_Release = (TextView) getView().findViewById(R.id.release_num);
            TV_Isolate = (TextView) getView().findViewById(R.id.isolate_num);
            TV_RIP = (TextView) getView().findViewById(R.id.rip_num);
            TV_BeforeTotal = (TextView) getView().findViewById(R.id.total_before_num);
            TV_BaseDate = (TextView) getView().findViewById(R.id.baseDate);

            TV_KoreaTotal.setText(bundle.getString("koreaTotal")+ "명");
            TV_BaseDate.setText(bundle.getString("base")+"(전국)");
            TV_Total.setText(bundle.getString("total")+ "명");
            TV_Release.setText(bundle.getString("release")+ "명");
            TV_Isolate.setText(bundle.getString("isolate")+ "명");
            TV_RIP.setText(bundle.getString("rip")+ "명");
            TV_BeforeTotal.setText("전일대비+"+bundle.getString("beforeTotal"));

            return false;
        }
    });
}