package com.example.myapplication.Activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.myapplication.Adapter.ReportAdapter;
import com.example.myapplication.Bean.Report;
import com.example.myapplication.R;
import com.example.myapplication.View.TitleLayout;

import java.util.ArrayList;
import java.util.List;

public class ViewReportActivity extends AppCompatActivity {
    private List<Report> reportList = new ArrayList<>();
    private TitleLayout titleLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }

        titleLayout = findViewById(R.id.title);
        titleLayout.setTitle("查看报告");
        titleLayout.setNextGone();
        titleLayout.setOnBackClickListener(new TitleLayout.OnBackClickListener() {
            @Override
            public void onMenuBackClick() {
                finish();
            }
        });

        init();
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recycle_view);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(ViewReportActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        ReportAdapter reportAdapter=new ReportAdapter(reportList);
        recyclerView.setAdapter(reportAdapter);
    }

    private void init(){
        Report report1=new Report("血压","108");
        Report report2=new Report("心率","68");
        Report report3=new Report("体温","36.8");
        Report report4=new Report("体重","62");
        Report report5=new Report("体重","62");
        Report report6=new Report("体重","62");
        Report report7=new Report("体重","62");
        Report report8=new Report("体重","62");
        Report report9=new Report("体重","62");
        Report report10=new Report("体重","62");
        Report report11=new Report("体重","62");
        Report report12=new Report("体重","62");
        Report report13=new Report("体重","62");
        Report report14=new Report("体重","62");


        reportList.add(report1);
        reportList.add(report2);
        reportList.add(report3);
        reportList.add(report4);
        reportList.add(report5);
        reportList.add(report6);
        reportList.add(report7);
        reportList.add(report8);
        reportList.add(report9);
        reportList.add(report10);
        reportList.add(report11);
        reportList.add(report12);
        reportList.add(report13);
        reportList.add(report14);



    }


}
