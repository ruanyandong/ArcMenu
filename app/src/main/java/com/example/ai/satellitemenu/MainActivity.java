package com.example.ai.satellitemenu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;

    private List<String> mDatas;


    private ArcMenu arcMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();

        mListView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,mDatas));

        initEvent();
    }

    private void initEvent() {
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (arcMenu.isOpen()){
                    arcMenu.toggleMenu(300);
                }

            }
        });

        arcMenu.setItemListener(new ArcMenu.OnMenuItemListener() {
            @Override
            public void onClick(View view, int pos) {
                Toast.makeText(MainActivity.this,pos+":"+view.getTag(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        mListView = findViewById(R.id.list_view);
        arcMenu = findViewById(R.id.arc_menu);

    }

    private void initData() {
        mDatas = new ArrayList<String>();

        for (int i = 'A'; i < 'Z'; i++) {
            mDatas.add((char)i+"");
        }

    }
}
