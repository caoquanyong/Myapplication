package com.example.myapplication.Activity;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.View.TitleLayout;

public class OnlinePayActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ImageView imageView;
    private TextView more_info;
    private TextView textView;
    private Spinner time;
    private String stringTime;
    private TitleLayout titleLayout;
    private String str1="李医生：北京中医药大学东方学院副院长兼养生康复学院院长，世界针联养生保健工作委员会副主任委员，1991年被派往俄亥俄大学医学院进修，从事国内外教学、临床和科研工作。先后赴美国、英国、爱尔兰、日本等多个国家参加学术交流，1998年至2000年，被派往英国密德萨斯大学任教两年，这是国外第一个中医药学学历学位教育项目。国家核心期刊等多种学术刊物上发表学术论文40余篇，先后主编和参编教材如《养生大典》、《黄帝内经养生全集》等20余部；主译和参译英文精品教材和专著10余部，担任英文版《中医养生学》的主编和主译，为《21世纪中医药英文版系列标准化教材》，供168个国家和地区教学使用。";
    private String text = "\n姓名：李凯\n中华医学会骨科分会骨病组委员\n擅长针灸推拿和小针刀治疗\n科室：骨科\n职务-职称：副主任医师\n坐诊时间：周一至周五、周日";
    private String []array=new String[]{"2019/4/1 18:00-19:00","2019/4/2 14:00-16:00","2019/4/3 15:00-17:00","2019/4/5 08:00-10:00"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_pay);
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
        imageView=(ImageView)findViewById(R.id.icon);
        textView=(TextView)findViewById(R.id.text);
        imageView.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.doc11));
        textView.setText(text);
        more_info=(TextView)findViewById(R.id.more_info);
        more_info.setText(str1);
        time=(Spinner)findViewById(R.id.time);
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(this,R.layout.spinner_item,array);
        time.setAdapter(arrayAdapter);
        time.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.time:
                stringTime =parent.getItemAtPosition(position).toString();
                break;
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
