package com.example.myapplication.Activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;

import com.example.myapplication.Adapter.DiseaseAdapter;
import com.example.myapplication.Bean.Disease;
import com.example.myapplication.R;
import com.example.myapplication.Utils.Constant;
import com.example.myapplication.View.BottomLayout;
import com.example.myapplication.View.TitleLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class TeleMedicineActivity extends AppCompatActivity {
    private TitleLayout titleLayout;
    private BottomLayout bottomLayout;
    private Handler handler;
    private List<Disease> diseaseList=new ArrayList<>();
    private RecyclerView recyclerView;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tele_medicine);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        Intent intent=getIntent();
        type=intent.getIntExtra("type",2);
        titleLayout = findViewById(R.id.title);
        if (type==2){
            titleLayout.setTitle("远程医疗");
        }else{
            titleLayout.setTitle("线下预约");
        }
        titleLayout.setNextGone();
        titleLayout.setOnBackClickListener(new TitleLayout.OnBackClickListener() {
            @Override
            public void onMenuBackClick() {
                Intent intent=new Intent(TeleMedicineActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        recyclerView=findViewById(R.id.recycle_view);
        bottomLayout=findViewById(R.id.bottom_bar);
        bottomLayout.setMode(type);
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(Constant.QUERYDISEASE_URL)
                .build();
        DiseaseService diseaseService=retrofit.create(DiseaseService.class);
        final Call<ResponseBody> call=diseaseService.getDisease(LoginActivity.sp.getString("token",""));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final retrofit2.Response<ResponseBody> response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            String body=response.body().string();
                            JSONObject jsonObject=new JSONObject(body);
                            JSONArray array=jsonObject.getJSONArray("diseaseList");
                            for(int i=0;i<array.length();i++){
                                JSONObject object=array.getJSONObject(i);
                                Log.d("dsadsadsaasd", "id "+object.getInt("id")+"name "+object.getString("name"));
                                Disease disease=new Disease(object.getInt("id"),object.getString("name"));
                                diseaseList.add(disease);
                                GridLayoutManager layoutManager=new GridLayoutManager(TeleMedicineActivity.this,3);
                                recyclerView.setLayoutManager(layoutManager);
                                DiseaseAdapter adapter=new DiseaseAdapter(diseaseList,type);
                                recyclerView.setAdapter(adapter);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                });

            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent=new Intent(TeleMedicineActivity.this,MainActivity.class);
            startActivity(intent);
        }
        return true;
    }
    public interface DiseaseService{
        @GET("queryDiseases")
        Call<ResponseBody> getDisease(@Query("access_token") String token);
    }
}
