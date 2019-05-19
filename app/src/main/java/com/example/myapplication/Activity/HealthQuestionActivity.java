package com.example.myapplication.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Adapter.HealthQuestionAdapter;
import com.example.myapplication.Bean.Disease;
import com.example.myapplication.Bean.Question;
import com.example.myapplication.R;
import com.example.myapplication.Utils.Constant;
import com.example.myapplication.View.BottomLayout;
import com.example.myapplication.View.TitleLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HealthQuestionActivity extends AppCompatActivity {
    private TitleLayout titleLayout;
    private TextView text;
    private BottomLayout bottomLayout;
    private RecyclerView recyclerView;
    private OkHttpClient client=new OkHttpClient();
    private List<Question> questionList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_question);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        Intent intent=getIntent();
        final int type=intent.getIntExtra("type",2);
        Disease disease=(Disease)intent.getSerializableExtra("disease");

        titleLayout = findViewById(R.id.title);
        if (type==2){
            titleLayout.setTitle("远程医疗");
        }else{
            titleLayout.setTitle("线下预约");
        }
        titleLayout.setOnBackClickListener(new TitleLayout.OnBackClickListener() {
            @Override
            public void onMenuBackClick() {
                finish();
            }
        });
        titleLayout.setOnNextClickListener(new TitleLayout.OnNextClickListener() {
            @Override
            public void onMenuNextClick() {
                Intent intent=new Intent(HealthQuestionActivity.this,ChoosePhotoActivity.class);
                intent.putExtra("type",type);
                startActivity(intent);
            }
        });

        text = findViewById(R.id.text);
        text.setText(disease.getName());
        bottomLayout=findViewById(R.id.bottom_bar);
        bottomLayout.setMode(type);
        recyclerView=findViewById(R.id.recycle_view);


        if(ContextCompat.checkSelfPermission(HealthQuestionActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(HealthQuestionActivity.this,new String[]{Manifest.permission.RECORD_AUDIO},1);
        }
        RequestBody requestBody=new FormBody.Builder()
                .add("departmentId",String.valueOf(disease.getId()))
                .add("access_token",LoginActivity.sp.getString("token",""))
                .build();
        Request request=new Request.Builder()
                .post(requestBody)
                .url(Constant.QUESTION_URL)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("dsadsafdsgfd", "onFailure: ");
            }
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String body=response.body().string();
                            JSONObject jsonObject = new JSONObject(body);
                            if (response.isSuccessful()) {
                                Log.d("dsadsafdsgfd", "onResponse: ");
                                JSONArray array = jsonObject.getJSONArray("QAList");
                                for (int i=0;i<array.length();i++){
                                    JSONObject object=array.getJSONObject(i);
                                    Question question=new Question(object.getInt("id"),object.getInt("questionNo"),object.getString("question"),object.getInt("departmentId"));
                                    questionList.add(question);
                                    LinearLayoutManager linearLayoutManager=new LinearLayoutManager(HealthQuestionActivity.this);
                                    recyclerView.setLayoutManager(linearLayoutManager);
                                    HealthQuestionAdapter adapter=new HealthQuestionAdapter(questionList,HealthQuestionActivity.this);
                                    recyclerView.setAdapter(adapter);
                                    Log.d("dsadsafdsgfd", "id "+object.getInt("id"));
                                    Log.d("dsadsafdsgfd", "questionNo "+object.getInt("questionNo"));
                                    Log.d("dsadsafdsgfd", "question"+object.getString("question"));
                                    Log.d("dsadsafdsgfd", "departmentId"+object.getInt("departmentId"));
                                }
                            }else{
                                Toast.makeText(HealthQuestionActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });

            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
         switch (requestCode){
             case 1:
                 if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                 } else {
                     Toast.makeText(this, "拒绝录音权限将无法问诊", Toast.LENGTH_SHORT).show();
                     Intent intent1=new Intent(HealthQuestionActivity.this,TeleMedicineActivity.class);
                     startActivity(intent1);
                 }
                 break;
             default:
                 break;
         }
    }
}
