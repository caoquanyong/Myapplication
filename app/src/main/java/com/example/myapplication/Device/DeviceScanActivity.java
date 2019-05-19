package com.example.myapplication.Device;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bluetoothlibrary.BluetoothLeClass;
import com.example.bluetoothlibrary.Config;
import com.example.bluetoothlibrary.Impl.ResolveM70c;
import com.example.bluetoothlibrary.Impl.ResolveWbp;
import com.example.bluetoothlibrary.Utils;
import com.example.bluetoothlibrary.entity.ConnectBleServiceInfo;
import com.example.bluetoothlibrary.entity.Constant;
import com.example.bluetoothlibrary.entity.Peripheral;
import com.example.bluetoothlibrary.entity.SampleGattAttributes;
import com.example.bluetoothlibrary.entity.SycnBp;
import com.example.myapplication.Activity.HealthTestActivity;
import com.example.myapplication.Activity.LoginActivity;
import com.example.myapplication.R;
import com.example.myapplication.View.TitleLayout;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.bluetoothlibrary.BluetoothLeClass.GetCharacteristicID;

/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


/**
 * Activity for scanning and displaying available Bluetooth LE devices.
 */
public class DeviceScanActivity extends AppCompatActivity{
    private final static String TAG = DeviceScanActivity.class.getSimpleName();
    private final static String UUID_KEY_DATA = "0000ffe1-0000-1000-8000-00805f9b34fb";
    private final static String UUID_KEY_DATA_WF = "0000fff4-0000-1000-8000-00805f9b34fb";

    public static int WBPMODE = -1;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean isActivityFront = true;
    public static final int MACRO_CODE_1 = 1;
    public static final int MACRO_CODE_2 = 2;
    public static final int MACRO_CODE_8 = 8;
    public static final int MACRO_CODE_9 = 9;
    public static final int MACRO_CODE_10 = 10;
    public static final int MACRO_CODE_11 = 11;
    public static final int MACRO_CODE_12 = 12;
    public static final int MACRO_CODE_13 = 13;
    public static final int MACRO_CODE_15 = 15;
    public static final int MACRO_CODE_18= 18;
    private final int REQUEST_ENABLE_BT = 0xa01;
    public static boolean isHasPermissions = false;
    private OkHttpClient client=new OkHttpClient();
    int currentapiVersion=android.os.Build.VERSION.SDK_INT;
    private BluetoothLeClass mBLE;
    private Config config=new Config();
    public static ArrayList<Peripheral> preipheralCopy = new ArrayList<Peripheral>();

    protected Handler handler;
    public ListView listView,datalist;
    private Button start_wbp,fin_wbp,fin,start,submit;
    private TextView spo2, heart_rate, pi, resvalue,testtime,error,text1,help;
    private TextView sys,dia,hr,bp_mesure;
    public double temp;
    public int spo2_s, heart_rate_s,resvalue_s,wbp;
    public float pi_s;
    private ImageView icon;
    private TextView name;
    private LinearLayout layout1,layout2,layout3,layout4;
    private LinearLayout layout5,layout6,layout7,layout8,layout9;
    private LinearLayout oxi_linearlayout, temp_layout;
    private FrameLayout frameLayout;
    public static LeDeviceListAdapter mLeDeviceListAdapter;
    //this is the data to draw spo2wave
    private Vector<Integer> SPO2WaveValues;
    private Vector<Integer> PIValues;

    private ConnectBleServiceInfo connectServiceInfo;
    private SPO2WaveView mSPO2WaveView;
    private int flag=0;
    private int type=0;
    public int i = 0;
    public int j=2;
    public boolean openble=true;
    ResolveM70c resolveM70c=new ResolveM70c();
    ResolveWbp resolveWbp=new ResolveWbp();
    private TitleLayout titleLayout;
    //
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("dsafdsfdsgfdgdsf", "onCreate: dsadsadasdas");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        SPO2WaveValues = new Vector<Integer>();
        PIValues=new Vector<Integer>();
        mSPO2WaveView = (SPO2WaveView) findViewById(R.id.SPO2Wave);
        mSPO2WaveView.setValues(SPO2WaveValues);
        mSPO2WaveView.setPIValues(PIValues);
        mSPO2WaveView.setZOrderOnTop(true);    // necessary
        SurfaceHolder sfhTrackHolder = mSPO2WaveView.getHolder();
        sfhTrackHolder.setFormat(PixelFormat.TRANSPARENT);
        text1=findViewById(R.id.text1);
        testtime= $(R.id.time);
        error= $(R.id.error);
        layout1=findViewById(R.id.spo);
        layout2=findViewById(R.id.her);
        layout3=findViewById(R.id.linpi);
        layout4=findViewById(R.id.linres);
        layout5=findViewById(R.id.shousuo);
        layout6=findViewById(R.id.shuzhang);
        layout7=findViewById(R.id.xinlv);
        layout8=findViewById(R.id.celiangshijian);
        layout9=findViewById(R.id.cuowu);
        frameLayout=findViewById(R.id.oxi_chart);
        help=findViewById(R.id.help);
        icon=findViewById(R.id.icon);
        name=findViewById(R.id.name);
        submit=findViewById(R.id.submit);
        sys= $(R.id.sys_id);
        dia= $(R.id.dia_id);
        hr= $(R.id.hr_id);
        fin=$(R.id.fin);
        start=$(R.id.start);
        bp_mesure=$(R.id.bp_mesure);
        start_wbp=$(R.id.wbp_start);
        fin_wbp=$(R.id.wbp_fin);
        listView =$(R.id.devive_list);//this for device
        oxi_linearlayout = $(R.id.oxi);
        spo2 = $(R.id.spo2);
        heart_rate = $(R.id.heart_rate);
        pi = $(R.id.pi);
        resvalue = $(R.id.resvalue);
        temp_layout = $(R.id.temp_layout);

        ActionBar actionBar=getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        titleLayout = findViewById(R.id.title);
        titleLayout.setTitle("扫描检测");
        titleLayout.setNextGone();
        titleLayout.setOnBackClickListener(new TitleLayout.OnBackClickListener() {
            @Override
            public void onMenuBackClick() {
                Intent intent=new Intent(DeviceScanActivity.this,HealthTestActivity.class);
                startActivity(intent);
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (parent == listView) {
                    final Peripheral device = mLeDeviceListAdapter.getDevice(position);
                    if (device == null) {
                        return;
                    }
                    mBLE.setBLEService(device.getPreipheralMAC());
                    listView.setVisibility(View.GONE);
                    icon.setVisibility(View.VISIBLE);
                    name.setVisibility(View.VISIBLE);
                    layout8.setVisibility(View.VISIBLE);
                    text1.setVisibility(View.GONE);
                    config.setConnectPreipheralOpsition(device);//set to be current device
                    if(device.getModel().equals("M70C")){
                        layout1.setVisibility(View.VISIBLE);
                        layout2.setVisibility(View.VISIBLE);
                        layout3.setVisibility(View.VISIBLE);
                        layout4.setVisibility(View.VISIBLE);
                        start.setVisibility(View.VISIBLE);
                        fin.setVisibility(View.VISIBLE);
                        frameLayout.setVisibility(View.VISIBLE);
                        name.setText("血氧仪");
                        type=1;
                    }else if(device.getModel().equals("WBP202")){
                        layout5.setVisibility(View.VISIBLE);
                        layout6.setVisibility(View.VISIBLE);
                        layout7.setVisibility(View.VISIBLE);
                        temp_layout.setVisibility(View.VISIBLE);
                        help.setVisibility(View.VISIBLE);
                        name.setText("血压仪");
                        type=0;
                    }
                    Log.e(" the current device", "" + config.getConnectPreipheralOpsition().getPreipheralMAC() + "" + config.getConnectPreipheralOpsition().getBluetooth());
                    Log.e("version of the device", "" + device.getModel());
//            sendMsg(MACRO_CODE_3,handler,null);
                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type==1){//M70C
                    String tt=testtime.getText().toString();
                    String Oxy=spo2.getText().toString();
                    String xinlu=heart_rate.getText().toString();
                    String p=pi.getText().toString();
                    String res=resvalue.getText().toString();
                    Log.d("dsadsadasda", "tt :"+tt);
                    Log.d("dsadsadasda", "Oxy :"+Oxy);
                    Log.d("dsadsadasda", "xinlu :"+xinlu);
                    Log.d("dsadsadasda", "p :"+p);
                    Log.d("dsadsadasda", "res :"+res);
                    sendRequestM70C(tt,Oxy,xinlu,p,res);

                }else{
                    String ttt=testtime.getText().toString();
                    String bp=bp_mesure.getText().toString();
                    String sy=sys.getText().toString();
                    String di=dia.getText().toString();
                    String hh=hr.getText().toString();
                    sendRequestWBP(ttt,bp,sy,di,hh);
                    Log.d("dsadsadasda", "ttt :"+ttt);
                    Log.d("dsadsadasda", "bp :"+bp);
                    Log.d("dsadsadasda", "sy :"+sy);
                    Log.d("dsadsadasda", "di :"+di);
                    Log.d("dsadsadasda", "hh :"+hh);
                }
            }
        });

        fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=0;
            }
        });
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=1;
                Date dt=new Date();
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time=simpleDateFormat.format(dt);
                testtime.setText(time);
                submit.setVisibility(View.VISIBLE);

            }
        });


        start_wbp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  connectingDevice=config.getConnectPreipheralOpsition().getBluetooth();
                if (connectingDevice.equals(Constant.BLT_WBP)) {
                    resolveWbp.onSingleCommand(mBLE);
                }
            }
        });
        fin_wbp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  connectingDevice=config.getConnectPreipheralOpsition().getBluetooth();
                if (connectingDevice.equals(Constant.BLT_WBP)) {
                    resolveWbp.onStopBleCommand(mBLE);
                }
            }
        });

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

//         Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        } else  {
            if (!mBluetoothAdapter.isEnabled()) {
                openble=false;
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                enableBtIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);

            }

        }
//        mBluetoothAdapter.enable();
        mBLE = new BluetoothLeClass(this);
        mBLE.setBluetoothGattCallback();
        if (!mBLE.initialize()) {
            Log.e(TAG, "Unable to initialize Bluetooth");
            finish();
        }

        if (mBluetoothAdapter.isEnabled())
        {
            mBLE.scanLeDevice(true);//start to scan

        }
        mBLE.setOnServiceDiscoverListener(mOnServiceDiscover);
        mBLE.setOnsetDevicePreipheral(mOnSetDevicePreipheral);
        mBLE.setOnDataAvailableListener(mOnDataAvailable);
        mBLE.setOnConnectListener(mOnConnectlistener);
        mBLE.setOnDisconnectListener(mOndisconnectListener);

        resolveM70c.setOnM70cDataListener(onM70cDataListener);
        resolveWbp.setOnWBPDataListener(onWBPDataListener);
        init();
        initHandler();
    }

    private void sendRequestM70C(String time,String xueyang,String xinlv,String p,String res){
        RequestBody requestBody=new FormBody.Builder()
                .add("recordTime",time)
                .add("blood_oxygen",xueyang)
                .add("heart_rate",xinlv)
                .add("pi",p)
                .add("res",res)
                .add("access_token", LoginActivity.sp.getString("token",""))
                .build();
        Request request=new Request.Builder()
                .post(requestBody)
                .url(com.example.myapplication.Utils.Constant.M70C_URL)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        });
    }
    private void sendRequestWBP(String time,String xiudaiya,String shousuoya,String shuzhangya,String xinlv){
        RequestBody requestBody=new FormBody.Builder()
                .add("time",time)
                .add("cuff_pressure",xiudaiya)
                .add("sys_pressure",shousuoya)
                .add("dia_pressure",shuzhangya)
                .add("heart_rate",xinlv)
                .add("access_token", LoginActivity.sp.getString("token",""))
                .build();
        Request request=new Request.Builder()
                .post(requestBody)
                .url(com.example.myapplication.Utils.Constant.WBP_URL)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });

            }
        });

    }
    private final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 0;
    private void init() {
        config = (Config) getApplicationContext();
        Log.e("show currentapiVersion",""+currentapiVersion);
        if (currentapiVersion>=23){
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
                if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_CONTACTS)) {
                    Toast.makeText(this, "shouldShowRequestPermissionRationale", Toast.LENGTH_SHORT).show();
                }
            }
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults[0] == -1) {
            isHasPermissions = false;
            Toast.makeText(this, "we need to get your location", Toast.LENGTH_LONG).show();
        }else {
            isHasPermissions = true;
        }
        mBLE.scanLeDevice(true);
    }


    @SuppressLint("HandlerLeak")
    private void initHandler() {
        handler = new Handler() {

            @SuppressLint("SetTextI18n")
            @Override
            public void dispatchMessage(Message msg) {
                switch (msg.what) {
                    case MACRO_CODE_1://show the device list
                        listView.setAdapter(mLeDeviceListAdapter);
                        if (mLeDeviceListAdapter != null) {
                            mLeDeviceListAdapter.notifyDataSetChanged();
                        }
                        text1.setText("请选择设备进行连接，连接后测量！");
                        break;
                    case MACRO_CODE_2:
                        listView.setAdapter(mLeDeviceListAdapter);
                        if (mLeDeviceListAdapter != null) {
                            mLeDeviceListAdapter.notifyDataSetChanged();
                        }
                        text1.setText("请选择设备进行连接，连接后测量！");
                        break;

                    case MACRO_CODE_8://oxygen value
                        listView.setVisibility(View.GONE);
                        temp_layout.setVisibility(View.GONE);

                        if (flag==1){
                            spo2_s = (int) msg.obj;
                            spo2.setText("" + spo2_s);
                        }
                        break;
                    case MACRO_CODE_9://W70
                        listView.setVisibility(View.GONE);
                        temp_layout.setVisibility(View.GONE);
                        if(flag==1){
                            heart_rate_s = (int) msg.obj;
                            heart_rate.setText("" + heart_rate_s);
                        }
                        break;
                    case MACRO_CODE_10://w70pi
                        listView.setVisibility(View.GONE);
                        temp_layout.setVisibility(View.GONE);
                        if(flag==1){
                            pi_s = (Float) msg.obj;
                            pi.setText("" + pi_s);
                        }
                        break;
                    case MACRO_CODE_11://w70res
                        listView.setVisibility(View.GONE);
                        temp_layout.setVisibility(View.GONE);
                        if(flag==1) {
                            resvalue_s = (int) msg.obj;
                            resvalue.setText("" + resvalue_s);
                        }
                        break;
                    case MACRO_CODE_12://Cuff pressure
                        listView.setVisibility(View.GONE);
                        bp_mesure.setText(""+msg.arg1);
                        break;
                    case MACRO_CODE_13://web the final result
                        listView.setVisibility(View.GONE);
                        sys.setText(""+ msg.arg1);
                        dia.setText(""+msg.arg2);
                        hr.setText(""+msg.obj);
                        help.setVisibility(View.GONE);
                        fin_wbp.setVisibility(View.VISIBLE);
                        start_wbp.setVisibility(View.VISIBLE);
                        submit.setVisibility(View.VISIBLE);
                        Toast.makeText(DeviceScanActivity.this, "连接成功！", Toast.LENGTH_SHORT).show();
                        break;
                    case MACRO_CODE_15://error
                        listView.setVisibility(View.GONE);
                        error.setText(""+msg.obj);
                    case MACRO_CODE_18://BTtime
                        listView.setVisibility(View.GONE);
                        Log.e("time",""+msg.obj);
                        testtime.setText(""+msg.obj);
                        break;
                    default:
                        break;
                }
            }
        };
        config.setMyFragmentHandler(handler);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!openble)
        {
            mBLE.scanLeDevice(true);
        }
        Log.e("see?",""+isActivityFront);
        isActivityFront = true;

        Log.d("dsafdsfdsgfdgdsf", "onResume: dsadsadasdas");
    }

    @Override
    protected void onPause() {

        super.onPause();
        isActivityFront = false;
        if (mSPO2WaveView.getDrawing()) {
            mSPO2WaveView.stopDraw();
        }
        Log.d("dsafdsfdsgfdgdsf", "onPause: dsadsadasdas");
    }

    @Override
    protected void onStop() {
        //mBLE.Unregister();
        super.onStop();
        mBLE.disconnect();
        mBLE.close();
        if (mLeDeviceListAdapter!=null)
        {
            mLeDeviceListAdapter.clear();
        }
        Log.d("dsafdsfdsgfdgdsf", "onStop: dsadsadasdas");
    }

    BluetoothLeClass.OnsetDevicePreipheral mOnSetDevicePreipheral=new BluetoothLeClass.OnsetDevicePreipheral() {
        @Override
        public void setDevicePreipheral(BluetoothDevice device, int model, String SN, float protocolVer) {
            Peripheral preipheral = new Peripheral();
            preipheral.setBluetooth(device.getName());
            preipheral.setPreipheralMAC(device.getAddress());
            switch (model) {
                case 1:
                    preipheral.setModel("WT1");
                    break;
                case 2:
                    preipheral.setModel("WT2");
                    break;
                case 3:
                    preipheral.setModel("WT3");
                    break;
                case 48:
                    preipheral.setModel("M70C");
                    break;
                case 51:
                    preipheral.setModel("WBP202");
                    WBPMODE = 1;
                    break;
                case 57:
                    preipheral.setModel("WBP204");
                    WBPMODE = 1;
                    break;
                case 70:
                    preipheral.setModel("WF100");
                    break;
                case 71:
                    preipheral.setModel("WF200");
                    break;
                default:
                    break;
            }
            //it is just for wbp
            if (WBPMODE!=-1)
            {
                preipheral.setWebMode(WBPMODE);
            }
            preipheral.setPreipheralSN(SN);
            preipheral.setName("Smart thermometer");
            preipheral.setBrand("Wearcare");
            preipheral.setManufacturer("blt");
            preipheral.setIsActivation(0);
            preipheral.setProtocolVer(protocolVer);
            preipheral.setRemark("");
            synchronized (preipheralCopy) {
                if (preipheralCopy.size() == 0) {
                    preipheralCopy.add(preipheral);
                    mLeDeviceListAdapter=new LeDeviceListAdapter(DeviceScanActivity.this,preipheralCopy);
                    sendMsg(DeviceScanActivity.MACRO_CODE_1,handler,null);
                } else {
                    boolean isTrue = false;//
                    for (int i = 0; i < preipheralCopy.size(); i++) {
                        Peripheral preipheral3 = preipheralCopy.get(i);
                        if (preipheral3.getPreipheralSN().equals(SN)) {
                            isTrue = true;//����
                        }
                    }
                    //
                    if (!isTrue) {
                        preipheralCopy.add(preipheral);
                        mLeDeviceListAdapter=new LeDeviceListAdapter(DeviceScanActivity.this,preipheralCopy);

                        sendMsg(DeviceScanActivity.MACRO_CODE_2,handler,null);
                    }
                }
                Log.e("the connecting devie", preipheral.toString());
            }
        }
    };



    BluetoothLeClass.OnConnectListener mOnConnectlistener=new BluetoothLeClass.OnConnectListener() {
        @Override
        public void onConnect(BluetoothGatt gatt) {


            if (config.getConnectPreipheralOpsition().getModel().equals(Constant.M70C))
            {
                if (!mSPO2WaveView.getDrawing() && isActivityFront) {
                    mSPO2WaveView.startDraw();
                }

                PIValues = new Vector<>();
                mSPO2WaveView.setPIValues(PIValues);
            }
        }


    };

    /**
     * the bluetoth is disconnected
     *
     */
    BluetoothLeClass.OnDisconnectListener mOndisconnectListener=new BluetoothLeClass.OnDisconnectListener() {
        @Override
        public void onDisconnect(BluetoothGatt gatt) {
            mSPO2WaveView.stopDraw();
        }
    };

    public ResolveM70c.OnM70cDataListener onM70cDataListener=new ResolveM70c.OnM70cDataListener() {
        @Override
        public void setSPO2Value(int spo2Value) {

            Message msg = new Message();
            msg.what = MACRO_CODE_8;
            msg.obj=spo2Value;
            handler.sendMessage(msg);

        }

        @Override
        public void setHeartRateValue(int heartRateValue) {

            Message msg = new Message();
            msg.what = MACRO_CODE_9;
            msg.obj=heartRateValue;
            handler.sendMessage(msg);

        }

        @Override
        public void setPI(Float pi) {
            Message msg = new Message();
            msg.what = MACRO_CODE_10;
            msg.obj=pi;
            handler.sendMessage(msg);
        }

        @Override
        public void setRespValue(int respValue) {

            Message msg = new Message();
            msg.what = MACRO_CODE_11;
            msg.obj=respValue;
            handler.sendMessage(msg);

        }

        @Override
        public void setbattery(String battery) {
        }

        @Override
        public void setMain(String main) {

        }

        @Override
        public void setSub(String sub) {

            Log.e("SUbversion::",""+sub);
        }

        @Override
        public void setSPo2ValuesPIValues(Vector<Integer> sp02WaveValues, Vector<Integer> piValues) {
            mSPO2WaveView.setValues(sp02WaveValues);
            mSPO2WaveView.setPIValues(piValues);
        }

    };


    public ResolveWbp.OnWBPDataListener onWBPDataListener=new ResolveWbp.OnWBPDataListener() {
        @Override
        public void onMeasurementBp(int temp) {
            Log.e("temp......",""+temp);
            Message msg = new Message();
            msg.what = MACRO_CODE_12;
            msg.arg1=temp;
            handler.sendMessage(msg);
        }
        @Override
        public void onMeasurementfin(final int SYS, final int DIA, final int PR, final Boolean isguestmode) {
            Message msg = new Message();
            msg.what = MACRO_CODE_13;
            msg.arg1=SYS;
            msg.arg2=DIA;
            msg.obj=PR;
            handler.sendMessage(msg);
        }

        /**
         * when measure failed
         * @param obj
         */
        @Override
        public void onErroer(Object obj) {
            Log.e("error",""+obj);
            Message message=new Message();
            message.what = MACRO_CODE_15;
            message.obj=obj;
            handler.sendMessage(message);
        }

        /**
         *
         * @param btbattey  betttey
         * @param bleState   o: spare 1:working
         * @param version
         * @param devState  workmode
         */
        @Override
        public void onState(int btbattey, String bleState, String version, String devState) {

        }

        /**
         * the data that is not send to the app,when bluetooth is reconnect ,it will coming here.
         * @param sycnBps
         */
        @Override
        public void onSycnBp(ArrayList<SycnBp> sycnBps) {

        }

        @Override
        public void onTime(String wbp_time) {
            Message message=new Message();
            message.obj=wbp_time;
            message.what=MACRO_CODE_18;
            handler.sendMessage(message);
        }
        @Override
        public void onUser(int user) {

        }
    };

    /**
     * when observe service then to diaplay the service.
     * no matter what device you want to connect ,just matching the device you want to de connect.do not to change anything.
     */
    private BluetoothLeClass.OnServiceDiscoverListener mOnServiceDiscover = new BluetoothLeClass.OnServiceDiscoverListener() {
        @Override
        public void onServiceDiscover(BluetoothGatt gatt) {
            Log.e("kkkkkkkkkkkkkkkkk", "found");
            connectServiceInfo = new ConnectBleServiceInfo();
            String  connectingDevice=config.getConnectPreipheralOpsition().getBluetooth();
            if (connectingDevice.equals(Constant.BLT_WBP)) {
                connectServiceInfo.setDeviceName(connectingDevice);
                connectServiceInfo.setServiceUUID(SampleGattAttributes.SeviceIDfbb0);
                connectServiceInfo.setCharateUUID(SampleGattAttributes.GetCharacteristicIDfbb2);
                connectServiceInfo.setCharateReadUUID(SampleGattAttributes.GetCharacteristicIDfbb1);
                connectServiceInfo.setConectModel(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            }else if (connectingDevice.equals(Constant.AL_WBP)) {
                connectServiceInfo.setDeviceName(connectingDevice);
                connectServiceInfo.setServiceUUID(SampleGattAttributes.SeviceIDfbb0_ALi);
                connectServiceInfo.setCharateUUID(SampleGattAttributes.GetCharacteristicIDfbb2_ALi);
                connectServiceInfo.setCharateReadUUID(SampleGattAttributes.GetCharacteristicIDfbb1);
                connectServiceInfo.setCharateALiRealTimeUUID(SampleGattAttributes.GetCharacteristicIDRealTime_ALi);
                connectServiceInfo.setCharateALiBatteryUUID(SampleGattAttributes.GetCharacteristicIDBattery_ALi);
                connectServiceInfo.setCharateALiHistoryDataUUID(SampleGattAttributes.GetCharacteristicIDHistoryData_ALi);
                connectServiceInfo.setConectModel(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
            }
            if (connectingDevice.equals(Constant.BLT_WBP)) {
                //���ӱ����ص�ֻ�赥ͨ��ͨ��
                displayGattServices(mBLE.getSupportedGattServices(), connectServiceInfo);
            }else if (connectingDevice.equals(Constant.AL_WBP)) {
                //���Ӱ������Ҫ��ͨ��
                displayGattServices_ali(mBLE.getSupportedGattServices());
            }else if ((connectingDevice.equals(Constant.BLT_WF1))){
                displayGattServices_WF(mBLE.getSupportedGattServices());
            }else
            {
                displayGattServices(mBLE.getSupportedGattServices());
            }
        }
    };
    /**
     *
     */
    private BluetoothLeClass.OnDataAvailableListener mOnDataAvailable = new BluetoothLeClass.OnDataAvailableListener() {
        /**
         * all data comes from here
         */
        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic) {

            Log.e(TAG, "onCharRead " + gatt.getDevice().getName()
                    + " read "
                    + characteristic.getUuid().toString()
                    + " -> "
                    + Utils.bytesToHexString(characteristic.getValue()));
            if (GetCharacteristicID.equals(characteristic.getUuid())) {
                final byte[] datas = characteristic.getValue();
//                    Log.e("datas", "�������ֵ��ʲô" + "" + datas);
                if (config.getConnectPreipheralOpsition().getModel().equals(Constant.WT2)) {
                    //resolveWt2.calculateData_WT2(datas,mBLE,TestActivity.this,config);//to  resolve the data from wt2
                } else if (config.getConnectPreipheralOpsition().getModel().equals(Constant.M70C)) {
                    final byte[] data = characteristic.getValue();
                    if (data != null && data.length > 0) {
                        final StringBuilder stringBuilder = new StringBuilder(
                                data.length);
                        for (byte byteChar : data)
                            stringBuilder.append(String.format("%02X ", byteChar));
                        String s = stringBuilder.toString();
                        resolveM70c.calculateData_M70c(s);//resolve data from m70c
                    }
                } else
                {
                    //resolvewt1.calculateData_WT1(datas,mBLE,TestActivity.this,config);//resolve data from wt1
                }
            } else {
                final byte[] data= characteristic.getValue();
                if (config.getConnectPreipheralOpsition().getBluetooth().equals(Constant.BLT_WBP)){

                    resolveWbp.resolveBPData2(data,mBLE,DeviceScanActivity.this);//resolve data from wep
                }else if (config.getConnectPreipheralOpsition().getBluetooth().equals(Constant.AL_WBP))
                {
                    resolveWbp.resolveALiBPData(data,getApplicationContext());//this is to resolve data from alibaba'device
                }else{

                    if (data != null && data.length > 0) {
                        final StringBuilder stringBuilder = new StringBuilder(
                                data.length);
                        for (byte byteChar : data)
                            stringBuilder.append(String.format("%02X ", byteChar));
                        String s =  stringBuilder.toString();
                        s=s.replace(" ","");
                        Log.e("s", "this is what" + "" + s);
                        //resolveWf100.resolveBPData_wf(s);// this is to resolve data from wf100
                    }
                }
            }
        }
        /**
         * get the callback from write
         */
        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt,
                                          BluetoothGattCharacteristic characteristic) {

            Log.e(TAG, "onCharWrite " + gatt.getDevice().getName()
                    + " write "
                    + characteristic.getUuid().toString()
                    + " -> "
                    + characteristic.getValue().toString());
        }
    };

    /**
     * to display the services of wf100,and to set notification
     * @param gattServices
     */
    @SuppressLint("LongLogTag")
    private void displayGattServices_WF(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;
        for (BluetoothGattService gattService : gattServices) {
            //-----Service���ֶ���Ϣ-----//
            int type = gattService.getType();
            Log.e(TAG, "-->service type:" + Utils.getServiceType(type));
            Log.e(TAG, "-->includedServices size:" + gattService.getIncludedServices().size());
            Log.e(TAG, "-->service uuid:" + gattService.getUuid());

            //-----Characteristics���ֶ���Ϣ-----//
            List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
            for (final BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                Log.e(TAG, "---->char uuid:" + gattCharacteristic.getUuid());
                int permission = gattCharacteristic.getPermissions();
                Log.e(TAG, "---->char permission:" + Utils.getCharPermission(permission));
                int property = gattCharacteristic.getProperties();
                Log.e(TAG, "---->char property:" + Utils.getCharPropertie(property));
//                byte[] data = gattCharacteristic.getValue();
//                if (data != null && data.length > 0) {
//                    Log.e(TAG, "---->char value:" + new String(data));
//                }
                Log.e("0000fff4-0000-1000-8000-00805f9b34fb", "=" + gattCharacteristic.getUuid().toString());
                if (gattCharacteristic.getUuid().toString().equals(UUID_KEY_DATA_WF)) {
                    //������Ϳ���ȥ��֪ͨ�Ͷ�д����
                    //���Զ�ȡ��ǰCharacteristic���ݣ��ᴥ��mOnDataAvailable.onCharacteristicRead()
//                        mHandler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                mBLE.readCharacteristic(gattCharacteristic);
//                           }
//                        }, 500);
                    mBLE.setCharacteristicNotification(gattCharacteristic, true);
                    gattCharacteristic.setValue("send data->");
                    mBLE.writeCharacteristic(gattCharacteristic);
                }
                List<BluetoothGattDescriptor> gattDescriptors = gattCharacteristic.getDescriptors();
                for (BluetoothGattDescriptor gattDescriptor : gattDescriptors) {
                    Log.e(TAG, "-------->desc uuid:" + gattDescriptor.getUuid());
                    int descPermission = gattDescriptor.getPermissions();
                    Log.e(TAG, "-------->desc permission:" + Utils.getDescPermission(descPermission));
                    byte[] desData = gattDescriptor.getValue();
                    if (desData != null && desData.length > 0) {
                        Log.e(TAG, "-------->desc value:" + new String(desData));
                    }
                }
            }
        }//

    }

    //    this is to display the services of wt1
    @SuppressLint("LongLogTag")
    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;
        for (BluetoothGattService gattService : gattServices) {
            //-----Service-----//
            int type = gattService.getType();
            Log.e(TAG, "-->service type:" + Utils.getServiceType(type));
            Log.e(TAG, "-->includedServices size:" + gattService.getIncludedServices().size());
            Log.e(TAG, "-->service uuid:" + gattService.getUuid());

            //-----Characteristics-----//
            List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
            for (final BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                Log.e(TAG, "---->char uuid:" + gattCharacteristic.getUuid());
                int permission = gattCharacteristic.getPermissions();
                Log.e(TAG, "---->char permission:" + Utils.getCharPermission(permission));
                int property = gattCharacteristic.getProperties();
                Log.e(TAG, "---->char property:" + Utils.getCharPropertie(property));
                byte[] data = gattCharacteristic.getValue();
                if (data != null && data.length > 0) {
                    Log.e(TAG, "---->char value:" + new String(data));
                }
                //UUID_KEY_DATA�ǿ��Ը�����ģ�鴮��ͨ�ŵ�Characteristic
                Log.e("0000ffe4-0000-1000-8000-00805f9b34fb", "=" + gattCharacteristic.getUuid().toString());
                if (gattCharacteristic.getUuid().toString().equals(UUID_KEY_DATA)) {
                    //������Ϳ���ȥ��֪ͨ�Ͷ�д����
                    //���Զ�ȡ��ǰCharacteristic���ݣ��ᴥ��mOnDataAvailable.onCharacteristicRead()
//                        mHandler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                mBLE.readCharacteristic(gattCharacteristic);
//                           }
//                        }, 500);
                    //����Characteristic��д��֪ͨ,�յ�����ģ������ݺ�ᴥ��mOnDataAvailable.onCharacteristicWrite()
                    mBLE.setCharacteristicNotification(gattCharacteristic, true);
                    //������������
                    gattCharacteristic.setValue("send data->");
                    //������ģ��д������
                    mBLE.writeCharacteristic(gattCharacteristic);
                }
                //-----Descriptors���ֶ���Ϣ-----//
                List<BluetoothGattDescriptor> gattDescriptors = gattCharacteristic.getDescriptors();
                for (BluetoothGattDescriptor gattDescriptor : gattDescriptors) {
                    Log.e(TAG, "-------->desc uuid:" + gattDescriptor.getUuid());
                    int descPermission = gattDescriptor.getPermissions();
                    Log.e(TAG, "-------->desc permission:" + Utils.getDescPermission(descPermission));
                    byte[] desData = gattDescriptor.getValue();
                    if (desData != null && desData.length > 0) {
                        Log.e(TAG, "-------->desc value:" + new String(desData));
                    }
                }
            }
        }//

    }

    //  you can copy this to your project..if it is works.you will get some datas
    @SuppressLint("LongLogTag")
    private void displayGattServices(List<BluetoothGattService> gattServices, ConnectBleServiceInfo serviceInfo) {
        if (gattServices == null)
            return;
        String uuid = null;
        Log.e("displayGattServices", serviceInfo.toString());
        for (BluetoothGattService gattService : gattServices) {
            uuid = gattService.getUuid().toString();
            Log.e("uuid",""+gattService.getUuid().toString());
            if (serviceInfo.getServiceUUID().equals(uuid)) {
                List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
                for (final BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                    uuid = gattCharacteristic.getUuid().toString();
                    Log.e(" gattCharacteristic.getUuid().toString()",""+ gattCharacteristic.getUuid().toString());
                    if (uuid.equals(serviceInfo.getCharateReadUUID())) {
                        Log.e("����GattCharacteUuid", uuid + ", CharacteSize: " + gattCharacteristics.size());
                        mBLE.setCharacteristicNotification(gattCharacteristic, true, serviceInfo);
                        mBLE.readCharacteristic(gattCharacteristic);
                        Log.e("--------1------", "11");
                        return;
                    }
                    if (uuid.equals(serviceInfo.getCharateUUID())) {

                        Log.e("����GattCharacteUuid", uuid + ", CharacteSize: " + gattCharacteristics.size());
                        mBLE.setCharacteristicNotification(gattCharacteristic, true, serviceInfo);
                        return;
                    }
                }
            }

        }

    }

    //this is for alibaba'device
    private void displayGattServices_ali(List<BluetoothGattService> gattServices) {
        if (gattServices == null)
            return;
        String uuid = null;
        for (BluetoothGattService gattService : gattServices) {
            uuid = gattService.getUuid().toString();
            if (uuid.contains("1810") || uuid.contains("180f")) {//1810:������������, ʵʱ���� 180f:������
                Log.e(TAG, "displayGattServices: " + uuid);
                List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
                for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                    uuid = gattCharacteristic.getUuid().toString();
                    Log.e("console", "2gatt Characteristic: " + uuid);
                    mBLE.setCharacteristicNotification(gattCharacteristic, true);
                    mBLE.readCharacteristic(gattCharacteristic);
                }
            }
        }
    }


    public <T> T $(int id) {
        return (T) findViewById(id);
    }
    //send data
    public static void sendMsg(int flag, Handler handler, Object object) {
        Message msg = new Message();
        msg.what = flag;
        msg.obj = object;
        if (handler!=null) {
            handler.sendMessage(msg);
        }
    }

    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;

            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }

    public static String formatDouble4(double d) {
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(d);
    }

}

