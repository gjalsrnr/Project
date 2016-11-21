package com.example.ooongi.serverprograming;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;

import java.util.ArrayList;

/**
 * Created by ooongi on 2016-11-19.
 */

public class mainscreenActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 빈 데이터 리스트 생성.

        ArrayList<String> items = new ArrayList<String>() ;
        // ArrayAdapter 생성. 아이템 View를 선택(single choice)가능하도록 만듦.
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,items) ;
        ListView listview = (ListView) findViewById(R.id.drawerLV) ;
        listview.setAdapter(adapter);
        items.add("스터디플래너");
        items.add("프로필");
        items.add("about");
        items.add("로그아웃");
        adapter.notifyDataSetChanged();
        // listview 생성 및 adapter 지정.
        TabHost tabHost = (TabHost)findViewById(R.id.tab_host);
        tabHost.setup();

        // Tab1 Setting
        TabHost.TabSpec tabSpec1 = tabHost.newTabSpec("Tab1");
        tabSpec1.setIndicator("방 목록"); // Tab Subject
        tabSpec1.setContent(R.id.tab_view1); // Tab Content
        tabHost.addTab(tabSpec1);

        // Tab2 Setting
        TabHost.TabSpec tabSpec2 = tabHost.newTabSpec("Tab2");
        tabSpec2.setIndicator("현재 스터디룸"); // Tab Subject
        tabSpec2.setContent(R.id.tab_view2); // Tab Content
        tabHost.addTab(tabSpec2);

        // show First Tab Content
        tabHost.setCurrentTab(0);


    }
}
