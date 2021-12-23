package com.podata.projectk.ui.home;


import android.os.AsyncTask;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
//import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.podata.projectk.MyAdapter;
import com.podata.projectk.R;
import com.podata.projectk.SSLConnect;
import com.podata.projectk.item;
import com.podata.projectk.setting;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    //파싱 코드 배열(@string), 패턴은 parsing_이름
    String[] parsing_URL, parsing_Size,parsing_Unit, parsing_District, parsing_Facility, parsing_Brand,
            parsing_Address, parsing_VisitTime, parsing_VisitTime2, parsing_Disinfection, parsing_Source, parsing_Location;


    //그외 변수 선언
    int location; //지역값
    private ArrayList<item> list = new ArrayList<>(); //리사이클러뷰의 리스트
    RecyclerView recyclerView;
    public static boolean loading = false;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);

        location = Integer.parseInt(setting.getConfigValue(getContext(),"location")); //setting클래스를 활용하여 설정된 지역값 불러오기

        //@string에 있는 파싱 코드를 배열에 저장
        parsing_Source = getResources().getStringArray(R.array.source); //출처
        parsing_Size =  getResources().getStringArray(R.array.list_size); //리스트크기
        parsing_Unit = getResources().getStringArray(R.array.unit); //리스트크기의 단위
        parsing_Location = getResources().getStringArray(R.array.location); //지역(시)
        parsing_URL = getResources().getStringArray(R.array.URl1); //주소
        parsing_District = getResources().getStringArray(R.array.district); //지역(구)
        parsing_Facility = getResources().getStringArray(R.array.facility); //유형
        parsing_Brand = getResources().getStringArray(R.array.brand); //상호명
        parsing_Address = getResources().getStringArray(R.array.address); //주소
        parsing_VisitTime = getResources().getStringArray(R.array.visitTime); //방문시간
        parsing_VisitTime2 = getResources().getStringArray(R.array.visitTime2); //방문시간2
        parsing_Disinfection = getResources().getStringArray(R.array.disinfection); //소독여부

        new Description().execute();

        return v;
    }

    //프래그먼트의 위젯을 다루기 위한 부분
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        TextView source = (TextView)view.findViewById(R.id.source);
        source.setText(parsing_Source[location]);
        TextView area = (TextView)view.findViewById(R.id.location);
        area.setText(parsing_Location[location]);

//        ((SwipeRefreshLayout)view.findViewById(R.id.swipe1)).setOnRefreshListener(() -> {  //스와이프레이아웃을 위로 당기면 다시 파싱하여 데이터 입력
//            list = new ArrayList<>(); //다시 불러오기 위해 초기화(없을 시 데이터 중복)
//            new Description().execute();
//            ((SwipeRefreshLayout)view.findViewById(R.id.swipe1)).setRefreshing(false); //작업이 완료되었고 스와이프레이아웃의 작업이 완료됬음을 표시(필수)
//        });
    }



    private class Description extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                String brand, address, district, facility , visitTime, visitTime2, disinfection;
                SSLConnect ssl = new SSLConnect();
                if(location==2)
                    ssl.postHttps(parsing_URL[location],1000,1000);
                Document doc = Jsoup.connect(parsing_URL[location]).get();

                Elements mElementDataSize;
                mElementDataSize = doc.select(parsing_Size[location]).select(parsing_Unit[location]);

                if(location == 0){ //서울은 별도의 코드 사용(시청 사이트가 아닌 보건복지부의 데이터의 의거)
                    for(Element elem : mElementDataSize) {
                        if(elem.select("tr th").text().equals("서울")){  //지역(시)가 서울인 부분만 파싱
                            district = elem.select(parsing_District[location]).text();
                            if(district.equals("")) //지역(구)의 표기에 따른 유형 작성(지역/유형으로 표기)
                                facility = elem.select(parsing_Facility[location]).text();
                            else
                                facility = "/"+elem.select(parsing_Facility[location]).text();

                            brand = elem.select(parsing_Brand[location]).text();
                            int splitIndex = brand.lastIndexOf("(서울");
                            address = brand.substring(splitIndex+1, brand.length()-1);
                            brand = brand.substring(0,splitIndex);

                            visitTime = elem.select(parsing_VisitTime[location]).text();
                            visitTime2 = elem.select(parsing_VisitTime[location]).text();

                            disinfection = elem.select(parsing_Disinfection[location]).text();

                            if(!brand.equals("") && !brand.contains("역학조사"))
                                list.add(new item(district, facility, brand, address, visitTime, visitTime2, disinfection));
                        }
                    }
                }else{
                    for(Element elem : mElementDataSize) {
                        district = elem.select(parsing_District[location]).text();
                        if(district.equals(""))
                            facility = elem.select(parsing_Facility[location]).text();
                        else
                            facility = "/"+elem.select(parsing_Facility[location]).text();
                        brand = elem.select(parsing_Brand[location]).text();
                        address = elem.select(parsing_Address[location]).text();
                        visitTime = elem.select(parsing_VisitTime[location]).text();

                        visitTime2 = elem.select(parsing_VisitTime2[location]).text();
                        disinfection = elem.select(parsing_Disinfection[location]).text();
                        System.out.println("대전:"+address);

                        if(!brand.equals("") && !brand.contains("역학조사"))
                            list.add(new item(district, facility, brand, address, visitTime, visitTime2, disinfection));

                    }
                }
                loading = true;
            } catch (IOException e) {
                loading = false;
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            MyAdapter myAdapter = new MyAdapter(getActivity(),list);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(myAdapter);
        }
    }
}