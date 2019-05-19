package com.example.myapplication.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton jiankangjiance;
    private ImageButton jiankangshangcheng;
    private ImageButton yuanchengyiliao;
    private ImageButton xianxiayuyue;
    private ImageButton menu;
    private ImageButton change_user;

    private DrawerLayout drawerLayout;
    private ImageView[] imageViews = null;
    private ImageView imageView = null;
    private ViewPager advPager = null;
    private AtomicInteger what = new AtomicInteger(0);
    private boolean isContinue = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }

        jiankangjiance = findViewById(R.id.main_jiankangjianceButton);
        jiankangshangcheng = findViewById(R.id.main_jiankangshangchengButton);
        yuanchengyiliao = findViewById(R.id.main_yuanchengyiliaoButton);
        xianxiayuyue = findViewById(R.id.main_xianxiayuyueButton);
        menu= findViewById(R.id.main_menuButton);
        change_user = findViewById(R.id.main_changeUserButton);
        jiankangjiance.setOnClickListener(this);
        jiankangshangcheng.setOnClickListener(this);
        yuanchengyiliao.setOnClickListener(this);
        xianxiayuyue.setOnClickListener(this);
        menu.setOnClickListener(this);
        change_user.setOnClickListener(this);
        initViewPager();
        drawerLayout=findViewById(R.id.drawer_layout);

        NavigationView navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Toast.makeText(MainActivity.this, menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_jiankangjianceButton:
                Intent intent2 = new Intent(MainActivity.this,
                        HealthTestActivity.class);
                startActivity(intent2);
                break;
            case R.id.main_jiankangshangchengButton:
                //Intent intent3 = new Intent(MainActivity.this, AccountInfoActivity.class);
                Intent intent3 = new Intent(MainActivity.this, HealthMallActivity.class);
                startActivity(intent3);
                break;
            case R.id.main_yuanchengyiliaoButton:
                Intent intent4 = new Intent(MainActivity.this,
                        TeleMedicineActivity.class);
                intent4.putExtra("type",2);
                startActivity(intent4);
                break;
            case R.id.main_xianxiayuyueButton:
                Intent intent5 = new Intent(MainActivity.this,
                        TeleMedicineActivity.class);
                intent5.putExtra("type",3);
                startActivity(intent5);
                break;
            case R.id.main_menuButton:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.main_changeUserButton:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("提示");
                builder.setMessage("是否确定切换用户？");
                builder.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent5 = new Intent(MainActivity.this,LoginActivity.class);
                                intent5.putExtra("flag",1);
                                startActivity(intent5);
                                finish();
                            }
                        });
                builder.setNeutralButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.create().show();
                break;
        }

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // && event.getRepeatCount() ==0
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("确定要退出吗?");
            builder.setTitle("提示");
            builder.setPositiveButton("确认",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            android.os.Process.killProcess(android.os.Process.myPid());
                        }
                    });
            builder.setNegativeButton("取消",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.create().show();
        }
        return true;
    }



    // 滑动图片一系列方法
    @SuppressLint("ClickableViewAccessibility")
    private void initViewPager() {
        advPager = (ViewPager) findViewById(R.id.adv_pager);
        List<View> advPics = new ArrayList<View>();
        // 1
        ImageView img1 = new ImageView(this);
        img1.setBackgroundResource(R.drawable.main_datu1);
        advPics.add(img1);
        // 2
        ImageView img2 = new ImageView(this);
        img2.setBackgroundResource(R.drawable.main_datu2);
        advPics.add(img2);
        // 3
        ImageView img3 = new ImageView(this);
        img3.setBackgroundResource(R.drawable.main_datu3);
        advPics.add(img3);
        // 4
        // ImageView img4 = new ImageView(this);
        // img4.setBackgroundResource(R.drawable.main_datu1);
        // advPics.add(img4);

        ViewGroup group = (ViewGroup) findViewById(R.id.viewGroup);
        imageViews = new ImageView[advPics.size()];

        for (int i = 0; i < advPics.size(); i++) {
            imageView = new ImageView(this);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(20, 20));
            imageView.setPadding(20, 0, 20, 0);
            imageViews[i] = imageView;
            if (i == 0) {
                imageViews[i]
                        .setBackgroundResource(R.drawable.banner_dian_focus);
            } else {
                imageViews[i]
                        .setBackgroundResource(R.drawable.banner_dian_blur);
            }
            group.addView(imageViews[i]);
        }

        advPager.setAdapter(new AdvAdapter(advPics));
        advPager.setOnPageChangeListener(new GuidePageChangeListener());
        advPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        isContinue = false;
                        break;
                    case MotionEvent.ACTION_UP:
                        isContinue = true;
                        break;
                    default:
                        isContinue = true;
                        break;
                }
                return false;
            }
        });

        new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    if (isContinue) {
                        viewHandler.sendEmptyMessage(what.get());
                        whatOption();
                    }
                }
            }

        }).start();
    }

    private void whatOption() {
        what.incrementAndGet();
        if (what.get() > imageViews.length - 1) {
            what.getAndAdd(-4);
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {

        }
    }

    @SuppressLint("HandlerLeak")
    private final Handler viewHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            advPager.setCurrentItem(msg.what);
            super.handleMessage(msg);
        }
    };

    private final class GuidePageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int arg0) {
            for (int i = 0; i < imageViews.length; i++) {
                imageViews[arg0]
                        .setBackgroundResource(R.drawable.banner_dian_focus);
                if (arg0 != i) {
                    imageViews[i]
                            .setBackgroundResource(R.drawable.banner_dian_blur);
                }
            }
        }
    }


    private final class AdvAdapter extends PagerAdapter {
        private List<View> views = null;

        public AdvAdapter(List<View> views) {
            this.views = views;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(views.get(arg1));
        }

        @Override
        public void finishUpdate(View arg0) {

        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(views.get(arg1), 0);
            return views.get(arg1);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {

        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {

        }
    }




}
