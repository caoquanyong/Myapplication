package com.example.myapplication.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Bean.User;
import com.example.myapplication.R;
import com.example.myapplication.Utils.Constant;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends Activity implements OnClickListener {
    final static String TAG = "Register";

    private EditText userName;
    private EditText userPwd;
    private EditText userPwdAgain;
    private EditText userPersonName;
    private RadioGroup userSex;
    private EditText userBirthday;
    private EditText userTel;
    private EditText address;
    private EditText emailAddress;
    private EditText emergencyMan;
    private EditText emergencyManTel;

    private Button registerButton;
    private CheckBox agreementBox;
    private TextView agreementText;
    private static String sex;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 启动layout
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
        // 找到各个组件
        userName = (EditText) findViewById(R.id.register_userName);
        userPwd = (EditText) findViewById(R.id.register_userPwd);
        userPwdAgain = (EditText) findViewById(R.id.register_userPwdAgain);
        userPersonName = (EditText) findViewById(R.id.register_userPersonName);
        userSex = (RadioGroup) findViewById(R.id.register_userSex);
        userBirthday = (EditText) findViewById(R.id.register_birthday);
        userTel = (EditText) findViewById(R.id.register_userTel);
        address = (EditText) findViewById(R.id.register_userAddress);
        emailAddress = (EditText) findViewById(R.id.register_userEmail_Address);
        emergencyMan = (EditText) findViewById(R.id.register_userEmergency_Man);
        emergencyManTel = (EditText) findViewById(R.id.register_userEmergency_Man_Tel);
        agreementBox = (CheckBox) findViewById(R.id.agreementBox);
        agreementText = (TextView) findViewById(R.id.agreementText);
        registerButton = (Button) findViewById(R.id.register_registerButton);
        // 设置初始值
        RegisterActivity.sex = "男";
        // 给组件设置监听器
        registerButton.setOnClickListener(this);
        userSex.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                // 获取变更后的选中项的ID
                int radioButtonId = arg0.getCheckedRadioButtonId();
                // 根据ID获取RadioButton的实例
                RadioButton rb = (RadioButton) findViewById(radioButtonId);
                // 设置sex值，以符合选中项
                if (rb.getText().toString().trim().equals("男")) {
                    RegisterActivity.sex = "男";
                } else if (rb.getText().toString().trim().equals("女")) {
                    RegisterActivity.sex = "女";
                }
            }
        });
        agreementText.setOnClickListener(this);
        handler =new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 120:
                        try{
                            int code=msg.arg1;
                            String message= (String) msg.obj;
                            if (code==200){
                                Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                                startActivity(intent);
                            }else {
                                Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                }
            }
        };
    }

    /**
     * 注册方法
     */
    public void register() {
        if (checkInfo()) {
            String username = this.userName.getText().toString().trim().toLowerCase();
            String password = this.userPwd.getText().toString().trim();
            String userPersonName = this.userPersonName.getText().toString().trim();
            String gender = RegisterActivity.sex;
            String userAge = this.userBirthday.getText().toString().trim();
            String userTel = this.userTel.getText().toString().trim();
            String userAddress = this.address.getText().toString().trim();
            String email_address = this.emailAddress.getText().toString().trim();
            String emergency_man = this.emergencyMan.getText().toString().trim();
            String emergency_man_tel = this.emergencyManTel.getText().toString().trim();

            // User user=new User(username,password,userPersonName,gender,userAge,userTel,userAddress,email_address,emergency_man,emergency_man_tel);
            // Gson gson=new Gson();
            //final String json=gson.toJson(user);
            //Log.d("dsadsafasda", "register: "+json);
            //RequestBody requestBody=RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json);
            final RequestBody requestBody=new FormBody.Builder()
                    .add("username",username)
                    .add("password",password)
                    .add("trueName",userPersonName)
                    .add("gender",gender)
                    .add("birthday",userAge)
                    .add("tel",userTel)
                    .add("address",userAddress)
                    .add("postalCode",email_address)
                    .add("emergency_people",emergency_man)
                    .add("emergency_tel",emergency_man_tel)
                    .build();
            final Request request=new Request.Builder()
                    .url(Constant.REGISTER_URL)
                    .post(requestBody)
                    .build();
            //HttpUrl httpUrl=HttpUrl.parse("").newBuilder().addQueryParameter(name,value);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        OkHttpClient client =new OkHttpClient();
                        Response response=client.newCall(request).execute();
                        if (response.isSuccessful()){
                            String responseData=response.body().string();
                            JSONObject jsonObject=new JSONObject(responseData);
                            String msg=jsonObject.getString("msg");
                            int code=jsonObject.getInt("code");
                            Message message=handler.obtainMessage();
                            message.what=120;
                            message.arg1=code;
                            message.obj=msg;
                            handler.sendMessage(message);
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();

        }
    }


    /**
     * 验证注册信息
     */
    @SuppressLint("DefaultLocale")
    public boolean checkInfo() {
        String userName = this.userName.getText().toString().trim();
        String userPwd = this.userPwd.getText().toString().trim();
        String userPwdAgain = this.userPwdAgain.getText().toString().trim();
        String userPersonName = this.userPersonName.getText().toString().trim();
        String age=this.userBirthday.getText().toString().trim();
        String userBirthday = this.userBirthday.getText().toString().trim();
        String userTel = this.userTel.getText().toString().trim();
        String userAddress = this.address.getText().toString().trim();
        String email_address = this.emailAddress.getText().toString().trim();
        String emergency_man = this.emergencyMan.getText().toString().trim();
        String emergency_man_tel = this.emergencyManTel.getText().toString().trim();

        if (userName.equals("")) {
            Toast.makeText(this, getString(R.string.username_empty),
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (userPwd.equals("")) {
            Toast.makeText(this, getString(R.string.userpwd_empty),
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (userPwd.length() < 6) {
            Toast.makeText(this, getString(R.string.userpwd_short),
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (userPwdAgain.equals("")) {
            Toast.makeText(this, getString(R.string.userPwdAgain_empty),
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (!userPwdAgain.equals(userPwd)) {
            Toast.makeText(this, getString(R.string.userPwd_error),
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (userPersonName.equals("")) {
            Toast.makeText(this, getString(R.string.userPersonName_empty),
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (age.equals("")) {
            Toast.makeText(this, getString(R.string.userAge_empty),
                    Toast.LENGTH_SHORT).show();
            return false;
        }else if(!JudgeBirthday(userBirthday)){
            Toast.makeText(this, "出生日期格式不匹配,年月日间请用“-”分割", Toast.LENGTH_SHORT).show();
            return false;
        }else if (userTel.equals("")) {
            Toast.makeText(this, getString(R.string.userTel_empty),
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (userTel.length() != 11 && !JudgeTel()) {
            Toast.makeText(this, getString(R.string.userTel_error),
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (!JudgeTel()) {
            Toast.makeText(this, getString(R.string.userTel_error),
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (userAddress.equals("")) {
            Toast.makeText(this, getString(R.string.userAddress_empty),
                    Toast.LENGTH_SHORT).show();
            return false;
        }else if (email_address.equals("")) {
            Toast.makeText(this, getString(R.string.useremail_empty),
                    Toast.LENGTH_SHORT).show();
            return false;
        }else if (emergency_man.equals("")) {
            Toast.makeText(this, getString(R.string.userEmergency_empty),
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (emergency_man_tel.equals("")) {
            Toast.makeText(this, getString(R.string.userEmergencyTel_empty),
                    Toast.LENGTH_SHORT).show();
            return false;
        }else if (!agreementBox.isChecked()) {
            Toast.makeText(this, getString(R.string.agreement_error),
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    public boolean JudgeBirthday(String ss){
        String pattern="^[0-9]{4}-[0-9]{2}-[0-9]{2}";
        Pattern pattern1=Pattern.compile(pattern);
        Matcher matcher=pattern1.matcher(ss);
        boolean match=matcher.matches();
        return match;
    }


    public boolean JudgeTel() {
        String userTel = this.userTel.getText().toString().trim();
        String[] nk = new String[] { "130", "131", "132", "133", "134", "135",
                "136", "137", "138", "139", "150", "151", "152", "153", "154",
                "155", "156", "157", "158", "159", "170", "180", "181", "182",
                "183", "184", "185", "186", "187", "188", "189" };
        if (userTel.length() == 11) {
            String num = userTel.substring(0, 3);
            int i;
            for (i = 0; i < nk.length; i++)
                if (nk[i].equals(num))
                    break;
            if (i < nk.length) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_registerButton:
                register();
                break;
            case R.id.agreementText:
                Intent intent = new Intent();
                intent.setClass(RegisterActivity.this, AgreementActivity.class);
                startActivity(intent);
                break;
        }
    }

}