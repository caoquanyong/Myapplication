package com.example.myapplication.View;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.Activity.HealthMallActivity;
import com.example.myapplication.Activity.HealthTestActivity;
import com.example.myapplication.Activity.OfflineReservationActivity;
import com.example.myapplication.Activity.TeleMedicineActivity;
import com.example.myapplication.R;


public class BottomLayout extends LinearLayout implements View.OnClickListener{

    private TextView jiankangjiance;
    private TextView jiankangshangcheng;
    private TextView yuanchengyiliao;
    private TextView xianxiayuyue;
    private TextView line1;
    private TextView line2;
    private TextView line3;
    private int mode;

    public BottomLayout(Context context){
        this(context,null);
    }
    public BottomLayout(Context context, @Nullable AttributeSet attrs){
        this(context,attrs,0);

    }
    public BottomLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr){
        super(context,attrs,defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BottomLayout);
        mode = typedArray.getInteger(R.styleable.BottomLayout_mode,4);
        typedArray.recycle();

        LayoutInflater.from(context).inflate(R.layout.bottom_bar,this);
        jiankangjiance =(TextView)findViewById(R.id.health_monitor);
        jiankangshangcheng =(TextView)findViewById(R.id.health_mall);
        yuanchengyiliao= (TextView)findViewById(R.id.telemedicine);
        xianxiayuyue =(TextView)findViewById(R.id.offline_reservation);
        line1=(TextView)findViewById(R.id.line1);
        line2=(TextView)findViewById(R.id.line2);
        line3=(TextView)findViewById(R.id.line3);
        setVisiable(mode);

        jiankangjiance.setOnClickListener(this);
        jiankangshangcheng.setOnClickListener(this);
        yuanchengyiliao.setOnClickListener(this);
        xianxiayuyue.setOnClickListener(this);
    }
    private void setVisiable(int mode){
        switch (mode){
            case 0:
                jiankangjiance.setVisibility(View.GONE);
                line1.setVisibility(View.GONE);
                break;
            case 1:
                jiankangshangcheng.setVisibility(View.GONE);
                line2.setVisibility(View.GONE);
                break;
            case 2:
                yuanchengyiliao.setVisibility(View.GONE);
                line3.setVisibility(View.GONE);
                break;
            case 3:
                xianxiayuyue.setVisibility(View.GONE);
                line3.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.health_monitor:
                Intent intent1=new Intent(getContext(),HealthTestActivity.class);
                getContext().startActivity(intent1);
                break;
            case R.id.health_mall:
                Intent intent2=new Intent(getContext(),HealthMallActivity.class);
                getContext().startActivity(intent2);
                break;
            case R.id.telemedicine:
                Intent intent3=new Intent(getContext(), TeleMedicineActivity.class);
                intent3.putExtra("type",2);
                getContext().startActivity(intent3);
                break;
            case R.id.offline_reservation:
                Intent intent4=new Intent(getContext(),TeleMedicineActivity.class);
                intent4.putExtra("type",3);
                getContext().startActivity(intent4);
                break;
        }
    }
    public void setMode(int mode){
        setVisiable(mode);
    }
}
