package com.example.myapplication.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.R;


public class TextArea extends LinearLayout {
    private static final int DEFAULT_NUM = 100;
    private String mHintText;
    private int mTotalNum;

    private EditText editText;
    private TextView textView;
    private Context mContext;

    public TextArea(Context context) {
        this(context, null);
    }

    public TextArea(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextArea(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        setOrientation(VERTICAL);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextArea);
        mHintText = typedArray.getString(R.styleable.TextArea_hint_text);
        mTotalNum = typedArray.getInteger(R.styleable.TextArea_total_num, DEFAULT_NUM);
        typedArray.recycle();
        init();
    }

    private void init() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.textarea, null);
        addView(view);
        editText = (EditText)view.findViewById(R.id.edit_text);
        editText.setHint(mHintText);
        editText.setMovementMethod(ScrollingMovementMethod.getInstance());
        editText.setSelection(editText.getText().length(), editText.getText().length());

        if (mTotalNum > 0) {
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mTotalNum)});
        }
        textView = (TextView)view.findViewById(R.id.left_num);
        textView.setText(String.valueOf(mTotalNum));

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Do nothing
            }
            @Override
            public void afterTextChanged(Editable s) {
                textView.setText(String.valueOf(mTotalNum - s.length()));
            }
        });

    }
    public void setHintText(String text) {
        editText.setHint(text);
    }

    public void setTotalNum(int totalNum) {
        textView.setText(String.valueOf(totalNum));
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(totalNum)});
    }
    public String getText() {
        return editText.getText().toString();
    }

}
