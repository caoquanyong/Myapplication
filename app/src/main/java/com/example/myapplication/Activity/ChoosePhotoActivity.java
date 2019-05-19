package com.example.myapplication.Activity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.myapplication.Adapter.GridViewAdapter;
import com.example.myapplication.R;
import com.example.myapplication.Utils.Constant;
import com.example.myapplication.Utils.GlideLoader;
import com.example.myapplication.View.BottomLayout;
import com.example.myapplication.View.TitleLayout;
import com.lcw.library.imagepicker.ImagePicker;

import java.io.IOException;
import java.util.ArrayList;

public class ChoosePhotoActivity extends AppCompatActivity {
    private static final int REQUEST_SELECT_IMAGES_CODE = 0x01;

    private  GridView gridView;
    private GridViewAdapter mGridViewAddImgAdapter;
    private Context mContext;
    private ArrayList<String> mPicList = new ArrayList<>();
    private TitleLayout titleLayout;
    private BottomLayout bottomLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_photo);
        mContext=this;
        ActionBar actionBar=getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        final int type=getIntent().getIntExtra("type",2);
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
                switch (type){
                    case 2:
                        Intent intent=new Intent(ChoosePhotoActivity.this,TeleReservationActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        Intent intent2=new Intent(ChoosePhotoActivity.this,OfflineReservationActivity.class);
                        startActivity(intent2);
                        break;
                        default:
                            break;
                }
            }
        });
        bottomLayout=findViewById(R.id.bottom_bar);
        bottomLayout.setMode(type);

        gridView=findViewById(R.id.gridView);
        initGridView();

        final String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/video_"+1+".amr";
        Log.d("dsdsddsdsd", "onCreate: "+path);
        findViewById(R.id.dsd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=0;i<mPicList.size();i++){
                    Log.d("dsdsddsdsd", "onClick: "+mPicList.get(i));
                }
                MediaPlayer player=new MediaPlayer();
                if(player!=null){
                    player.reset();
                }else {
                    player=new MediaPlayer();
                }
                try {
                    player.setDataSource(path);
                    //初始化
                    player.prepare();
                    //开始播放
                    player.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initGridView() {
        mGridViewAddImgAdapter = new GridViewAdapter(mContext, mPicList);
        gridView.setAdapter(mGridViewAddImgAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == parent.getChildCount() - 1) {
                    //如果“增加按钮形状的”图片的位置是最后一张，且添加了的图片的数量不超过9张，才能点击
                    if (mPicList.size() == Constant.MAX_SELECT_PIC_NUM) {
                        //最多添加9张图片
                        viewPluImg(position);
                    } else {
                        //添加凭证图片
                        selectPic(Constant.MAX_SELECT_PIC_NUM - mPicList.size());
                    }
                } else {
                    viewPluImg(position);
                }
            }
        });
    }

    private void viewPluImg(int position) {
        Intent intent = new Intent(mContext, PlusImageActivity.class);
        intent.putStringArrayListExtra(Constant.IMG_LIST, mPicList);
        intent.putExtra(Constant.POSITION, position);
        startActivityForResult(intent, Constant.REQUEST_CODE_MAIN);
    }

    private void selectPic(int num){
        ImagePicker.getInstance()
                .setTitle("选择图片")//设置标题
                .showCamera(true)//设置是否显示拍照按钮
                .showImage(true)//设置是否展示图片
                .showVideo(true)//设置是否展示视频
                .setMaxCount(num)//设置最大选择图片数目(默认为1，单选)
                .setImageLoader(new GlideLoader())//设置自定义图片加载器
                .start(ChoosePhotoActivity.this, REQUEST_SELECT_IMAGES_CODE);//REQEST_SELECT_IMAGES_CODE为Intent调用的requestCode
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK && requestCode==REQUEST_SELECT_IMAGES_CODE){
            ArrayList<String> newPicList = data.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES);
            mPicList.addAll(newPicList);
            mGridViewAddImgAdapter.notifyDataSetChanged();
        }
        if (requestCode == Constant.REQUEST_CODE_MAIN && resultCode == Constant.RESULT_CODE_VIEW_IMG) {
            //查看大图页面删除了图片
            ArrayList<String> toDeletePicList = data.getStringArrayListExtra(Constant.IMG_LIST); //要删除的图片的集合
            mPicList.clear();
            mPicList.addAll(toDeletePicList);
            mGridViewAddImgAdapter.notifyDataSetChanged();
        }
    }
}
