package com.example.myapplication.View;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.Activity.LoginActivity;
import com.example.myapplication.R;

public class TitleLayout extends LinearLayout {
    private OnBackClickListener mListener1;
    private OnNextClickListener mListener2;
    private TextView textView;
    private ImageButton back;
    private TextView next;
    public TitleLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.title, this);
        back = findViewById(R.id.main_menuButton);
        next =findViewById(R.id.next);

        textView =findViewById(R.id.menuText);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener1.onMenuBackClick();
            }
        });
        next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener2.onMenuNextClick();
            }
        });

    }

    public interface OnBackClickListener{
        void onMenuBackClick();
    }

    public void setOnBackClickListener(OnBackClickListener listener){
        this.mListener1=listener;
    }

    public interface OnNextClickListener{
        void onMenuNextClick();
    }

    public void setOnNextClickListener(OnNextClickListener listener){
        this.mListener2=listener;
    }


    public void setTitle(String text){
        textView.setText(text);
    }
    public void setNextGone(){
        next.setVisibility(View.GONE);
    }


}
