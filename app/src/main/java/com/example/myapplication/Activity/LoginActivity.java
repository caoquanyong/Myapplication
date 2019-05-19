package com.example.myapplication.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Bean.LoginResult;
import com.example.myapplication.R;
import com.example.myapplication.Utils.Constant;
import com.google.gson.Gson;

import android.widget.CompoundButton.OnCheckedChangeListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class LoginActivity extends Activity {

    private EditText userName, password;
    private CheckBox rem_pw, auto_login;
    private ImageButton registerButton;
    private ImageButton loginButton;
    private String userNameValue,passwordValue;
    private TextView forgetPwdText;
    public static SharedPreferences sp;

    // 滑动图片
    private ImageView[] imageViews = null;
    private ImageView imageView = null;
    private ViewPager advPager = null;
    private AtomicInteger what = new AtomicInteger(0);
    private boolean isContinue = true;
    private LoginResult result;
    private Handler handler;
    final OkHttpClient client =new OkHttpClient();

    @SuppressLint("HandlerLeak")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //去除标题
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        //获得实例对象
        sp = this.getSharedPreferences("userInfo", MODE_PRIVATE);
        userName = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.userPwd);
        registerButton = (ImageButton) findViewById(R.id.registerButton);
        loginButton = (ImageButton) findViewById(R.id.loginButton);
        rem_pw= (CheckBox) findViewById(R.id.remPwd);
        auto_login = (CheckBox) findViewById(R.id.login_autoLogin);
        forgetPwdText = (TextView) findViewById(R.id.forgetPwdText);


        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 123:
                        try {
                            LoginResult mResult = (LoginResult) msg.obj;
                            int code = mResult.getCode();
                            String message = mResult.getMsg();
                            String token=mResult.getAccess_token();
                            if (code == 200) {
                                SharedPreferences.Editor editor = sp.edit();
                                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                                if (rem_pw.isChecked()) {
                                    //记住用户名、密码
                                    editor.putBoolean("ISCHECK",true);
                                    editor.putString("USER_NAME", userNameValue);
                                    editor.putString("PASSWORD", passwordValue);
                                }else {
                                    editor.clear();
                                }
                                editor.putString("token",token);
                                editor.apply();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("token",token);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                }
            }
        };

        //判断记住密码多选框的状态
        if(sp.getBoolean("ISCHECK", false))
        {
            //设置默认是记录密码状态
            rem_pw.setChecked(true);
            userName.setText(sp.getString("USER_NAME", ""));
            password.setText(sp.getString("PASSWORD", ""));
            //判断自动登陆多选框状态
            if(sp.getBoolean("AUTO_ISCHECK", false))
            {
                //设置默认是自动登录状态
                auto_login.setChecked(true);
                if (getIntent().getIntExtra("flag",0)!=1) {
                    sendRequest(sp.getString("USER_NAME", ""), sp.getString("PASSWORD", ""));
                }
            }
        }

        // 登录监听事件  现在默认为用户名为：liu 密码：123
        loginButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                userNameValue = userName.getText().toString();
                passwordValue = password.getText().toString();
                sendRequest(userNameValue,passwordValue);

            }
        });

        registerButton.setOnClickListener(  new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);

            }
        });

        //监听记住密码多选框按钮事件
        rem_pw.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (rem_pw.isChecked()) {

                    System.out.println("记住密码已选中");
                    sp.edit().putBoolean("ISCHECK", true).commit();

                }else {

                    System.out.println("记住密码没有选中");
                    sp.edit().putBoolean("ISCHECK", false).commit();

                }

            }
        });

        //监听自动登录多选框事件
        auto_login.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (auto_login.isChecked()) {
                    System.out.println("自动登录已选中");
                    sp.edit().putBoolean("AUTO_ISCHECK", true).commit();
                    rem_pw.setChecked(true);

                } else {
                    System.out.println("自动登录没有选中");
                    sp.edit().putBoolean("AUTO_ISCHECK", false).commit();
                }
            }
        });

    }

    private void sendRequest(String username,String password){
        RequestBody requestBody=new FormBody.Builder()
                .add("username",username)
                .add("password",password)
                .build();
        final Request request=new Request.Builder()
                .url(Constant.LOGIN_URL)
                .post(requestBody)
                .build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response=null;
                try{
                    response=client.newCall(request).execute();
                    if (response.isSuccessful()){
                        String responseBody=response.body().string();
                        Log.d("LoginActivitydddddd", "run: "+responseBody);
                        result=parseJSONWithGson(responseBody);
                        Message message=handler.obtainMessage();
                        message.what=123;
                        message.obj=result;
                        handler.sendMessage(message);
                    }else{
                        Log.d("LoginActivitydddddd", "Fail ");
                    }
                }catch (IOException e){
                    Log.d("LoginActivitydddddd", "Faiddl ");
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private LoginResult parseJSONWithGson(String jsonData){
        Gson gson=new Gson();
        return gson.fromJson(jsonData,LoginResult.class);
    }


}