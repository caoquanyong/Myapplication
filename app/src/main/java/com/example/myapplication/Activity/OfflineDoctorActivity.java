package com.example.myapplication.Activity;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.myapplication.Adapter.DoctorsAdapter;
import com.example.myapplication.Bean.DoctorData;
import com.example.myapplication.R;
import com.example.myapplication.View.TitleLayout;

import java.util.ArrayList;
import java.util.List;

public class OfflineDoctorActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<DoctorData> list=new ArrayList<>();
    private TitleLayout titleLayout;
    public String[] text = {
            "\n姓名：李凯\n中华医学会骨科分会骨病组委员\n擅长针灸推拿和小针刀治疗\n科室：骨科\n职务-职称：副主任医师\n坐诊时间：周一至周五、周日",
            "\n姓名：季霞\n协和不孕不育研究所首席专家\n中华医学会生殖医学分会委员\n科室：不孕不育中心\n职务-职称：主任医师\n坐诊时间：周一至周六",
            "\n姓名：叶天琼\n重庆市不孕不育临床中心主任 \n妇女健康知识巡回宣讲团讲师\n科室：不孕不育中心\n职务-职称：副主任医师\n坐诊时间：周一至周六",
            "\n姓名：徐小蓉\n中华医学会妇产科分会委员\n抗癌协会妇科肿瘤专委会全国委员\n科室：妇产科\n职务-职称：主任医师 教授 \n坐诊时间：周二至周六",
            "\n姓名：赵本书\n妇女病康复专业委员会不孕症学组委员\n多次参加全国妇产科专业学术交流\n科室：不孕不育中心\n职务-职称：主治医师\n坐诊时间：全天坐诊" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_doctor);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        titleLayout = findViewById(R.id.title);
        titleLayout.setTitle("线下预约");
        titleLayout.setNextGone();
        titleLayout.setOnBackClickListener(new TitleLayout.OnBackClickListener() {
            @Override
            public void onMenuBackClick() {
                finish();
            }
        });
        init();
        recyclerView=findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        DoctorsAdapter doctorsAdapter=new DoctorsAdapter(list);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(doctorsAdapter);
    }

    private void init(){
        DoctorData doctorData1=new DoctorData(ContextCompat.getDrawable(this,R.drawable.doc11),text[0]);
        DoctorData doctorData2=new DoctorData(ContextCompat.getDrawable(this,R.drawable.doc22),text[1]);
        DoctorData doctorData3=new DoctorData(ContextCompat.getDrawable(this,R.drawable.doc33),text[2]);
        DoctorData doctorData4=new DoctorData(ContextCompat.getDrawable(this,R.drawable.doc44),text[3]);
        DoctorData doctorData5=new DoctorData(ContextCompat.getDrawable(this,R.drawable.doc55),text[4]);
        list.add(doctorData1);
        list.add(doctorData2);
        list.add(doctorData3);
        list.add(doctorData4);
        list.add(doctorData5);
    }
}
