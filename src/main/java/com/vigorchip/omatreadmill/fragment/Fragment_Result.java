package com.vigorchip.omatreadmill.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vigorchip.omatreadmill.R;
import com.vigorchip.omatreadmill.activity.MainActivity;
import com.vigorchip.omatreadmill.application.isApplication;
import com.vigorchip.omatreadmill.base.BaseObserver;
import com.vigorchip.omatreadmill.bean.NetUtil;
import com.vigorchip.omatreadmill.bean.SportData;
import com.vigorchip.omatreadmill.eventbus.ServiceToResult;
import com.vigorchip.omatreadmill.interfaci.Api;
import com.vigorchip.omatreadmill.server.ServerWindows;
import com.vigorchip.omatreadmill.sql.MySQLiteHelper;
import com.vigorchip.omatreadmill.utils.DoubleUtils;
import com.vigorchip.omatreadmill.utils.TimeUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import goole.zxing.client.android.EncodingUtils;
import io.reactivex.disposables.Disposable;

import static com.vigorchip.omatreadmill.R.layout.result;
import static com.vigorchip.omatreadmill.application.isApplication.End_SystemTime;
import static com.vigorchip.omatreadmill.server.ServerWindows.RESULT_AVG_SPEED;
import static com.vigorchip.omatreadmill.server.ServerWindows.RESULT_MAX_SPEED;
import static com.vigorchip.omatreadmill.server.ServerWindows.RESULT_TIME;

/**
 * Created by wr-app1 on 2018/4/19.
 */

public class Fragment_Result extends Fragment implements View.OnClickListener {
    private View view;
    private RelativeLayout result_click;
    private ImageView qrCode;
    private TextView tvResult_time, tvResult_distance, tvResult_calories, tvResult_maxSpeed, tvResult_avgSpeed;
    private ImageButton cancel_result;
    private SharedPreferences sp_Device;
    private SharedPreferences.Editor editor;
    private File file;
    /**
     * 用户管理设备信息的信息
     */
    private long time;//总时间
    private double km;//总距离
    private double cal;//总卡路里
    private int frequency;//总次数
    private double avgSpeed;//平均速度

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(result, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    private void init() {
        file = new File("/data/data/com.vigorchip.omatreadmill/shared_prefs/device_info.xml");
        sp_Device = getContext().getSharedPreferences("device_info", Context.MODE_PRIVATE);
        editor = sp_Device.edit();
        result_click = view.findViewById(R.id.result_click);
        qrCode = view.findViewById(R.id.qrCode);
        tvResult_time = view.findViewById(R.id.tvResult_time);
        tvResult_distance = view.findViewById(R.id.tvResult_distance);
        tvResult_calories = view.findViewById(R.id.tvResult_calories);
        tvResult_maxSpeed = view.findViewById(R.id.tvResult_maxSpeed);
        tvResult_avgSpeed = view.findViewById(R.id.tvResult_avgSpeed);
        cancel_result = view.findViewById(R.id.cancel_result);
        cancel_result.setOnClickListener(this);
        result_click.setOnClickListener(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);//必须在最后初始化eventbus，不然接收不到
        }
    }

    private String QRcodeURL;

    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)//提供给service倒计时结束调到二维码页面
    public void onResult(ServiceToResult toResult) {
        End_SystemTime = System.currentTimeMillis();
        Log.i(">>>>运动结果1", TimeUtils.TimeFomat(RESULT_TIME) + RESULT_MAX_SPEED);
        if (toResult.getTo()) {
            QRcodeURL = "http://39.108.225.155/vc_tp/Home/Share/share?mileage=" + String.valueOf(ServerWindows.RESULT_KM)
                    + "&time=" + TimeUtils.TimeFomat(RESULT_TIME)
                    + "&kaluli=" + String.valueOf(ServerWindows.RESULT_CAL / 10)
                    + "&peisu=" + String.valueOf(RESULT_AVG_SPEED / 1000)
                    + "&speed=" + String.valueOf(RESULT_AVG_SPEED / 1000)
                    + "&xinlv=" + String.valueOf(ServerWindows.RESULT_AVG_HRC);
            qrCode.setImageBitmap(EncodingUtils.Create2DCode(QRcodeURL, 450, 450, null));
            // TODO: 2018/5/12 上传记录
            sendupdataRunrecord();
            if (!file.exists()) {
                editor.putString("time", String.valueOf(RESULT_TIME));
                editor.putString("km", String.valueOf(ServerWindows.RESULT_KM));
                editor.putString("cal", String.valueOf(ServerWindows.RESULT_CAL));
                editor.putString("frequency", String.valueOf(1));
                editor.putString("avgSpeed", String.valueOf(RESULT_AVG_SPEED));
                editor.putString("lastTime", String.valueOf(isApplication.End_SystemTime));
                editor.commit();
                Log.i(">>>>运动结果2", TimeUtils.TimeFomat(RESULT_TIME) + RESULT_MAX_SPEED);
            } else {
                time = Long.parseLong(sp_Device.getString("time", ""));
                time = time + RESULT_TIME;
                editor.putString("time", String.valueOf(time));
                km = Double.parseDouble(sp_Device.getString("km", ""));
                km = DoubleUtils.add(km, ServerWindows.RESULT_KM);
                editor.putString("km", String.valueOf(km));
                cal = Double.parseDouble(sp_Device.getString("cal", ""));
                cal = DoubleUtils.add(cal, ServerWindows.RESULT_CAL);
                editor.putString("cal", String.valueOf(cal));
                frequency = Integer.parseInt(sp_Device.getString("frequency", ""));
                frequency = frequency + 1;
                editor.putString("frequency", String.valueOf(frequency));
                avgSpeed = new BigDecimal(km / (((double) time) / 3600)).setScale(1, BigDecimal.ROUND_DOWN).doubleValue();
                editor.putString("avgSpeed", String.valueOf(avgSpeed));
                editor.putString("lastTime", String.valueOf(isApplication.End_SystemTime));
                editor.commit();
                Log.i(">>>>运动结果3", TimeUtils.TimeFomat(RESULT_TIME) + RESULT_MAX_SPEED);
            }
            tvResult_time.setText(TimeUtils.TimeFomat(RESULT_TIME));
            if (isApplication.ph_selector == 1) {
                tvResult_calories.setText(String.valueOf(ServerWindows.RESULT_CAL) + "cal");
                tvResult_distance.setText(String.valueOf(ServerWindows.RESULT_KM) + "km");
                tvResult_avgSpeed.setText(String.valueOf(RESULT_AVG_SPEED) + "km/h");
                tvResult_maxSpeed.setText(String.valueOf(ServerWindows.RESULT_MAX_SPEED) + "km/h");
            } else if (isApplication.ph_selector == 2) {
                tvResult_calories.setText(String.valueOf(ServerWindows.RESULT_CAL) + "cal");
                tvResult_distance.setText(String.valueOf(new BigDecimal(ServerWindows.RESULT_KM).setScale(1, BigDecimal.ROUND_DOWN).doubleValue()) + "mi");
                tvResult_avgSpeed.setText(String.valueOf(new BigDecimal(ServerWindows.RESULT_AVG_SPEED).setScale(1, BigDecimal.ROUND_DOWN).doubleValue()) + "MPH");
                tvResult_maxSpeed.setText(String.valueOf(new BigDecimal(ServerWindows.RESULT_MAX_SPEED).setScale(1, BigDecimal.ROUND_DOWN).doubleValue()) + "MPH");
            }
//            switch (SportData.getStatus()) {
//                case SportData.RUNNING_MODE_MANUAL:
//                    tvResult_time.setText(TimeUtils.TimeFomat(RESULT_TIME));
//                    if (SportData.RADIX == 1) {
//                        tvResult_calories.setText(String.valueOf(ServerWindows.RESULT_CAL) + "cal");
//                        tvResult_distance.setText(String.valueOf(ServerWindows.RESULT_KM) + "km");
//                        tvResult_avgSpeed.setText(String.valueOf(RESULT_AVG_SPEED) + "m/s");
//                        tvResult_maxSpeed.setText(String.valueOf(ServerWindows.RESULT_MAX_SPEED) + "km/h");
//                    } else {
//                        tvResult_calories.setText(String.valueOf(ServerWindows.RESULT_CAL) + "cal");
//                        tvResult_distance.setText(String.valueOf(new BigDecimal(ServerWindows.RESULT_KM * 0.62).setScale(1, BigDecimal.ROUND_DOWN).doubleValue()) + "mi");
//                        tvResult_avgSpeed.setText(String.valueOf(new BigDecimal(ServerWindows.RESULT_AVG_SPEED * 0.62).setScale(1, BigDecimal.ROUND_DOWN).doubleValue()) + "MPH");
//                        tvResult_maxSpeed.setText(String.valueOf(new BigDecimal(ServerWindows.RESULT_MAX_SPEED * 0.62).setScale(1, BigDecimal.ROUND_DOWN).doubleValue()) + "MPH");
//                    }
//                    break;
//                case SportData.RUNNING_MODE_TIME:
//                    tvResult_time.setText(TimeUtils.TimeFomat(RESULT_TIME));
//                    tvResult_calories.setText(String.valueOf(ServerWindows.RESULT_CAL) + "cal");
//                    tvResult_distance.setText(String.valueOf(ServerWindows.RESULT_KM) + "km");
//                    tvResult_avgSpeed.setText(String.valueOf(RESULT_AVG_SPEED) + "m/s");
//                    tvResult_maxSpeed.setText(String.valueOf(ServerWindows.RESULT_MAX_SPEED) + "km/h");
//                    break;
//                case SportData.RUNNING_MODE_DISTANCE:
//                    tvResult_time.setText(TimeUtils.TimeFomat(RESULT_TIME));
//                    tvResult_calories.setText(String.valueOf(ServerWindows.RESULT_CAL) + "cal");
//                    tvResult_distance.setText(String.valueOf(ServerWindows.RESULT_KM) + "km");
//                    tvResult_avgSpeed.setText(String.valueOf(RESULT_AVG_SPEED) + "m/s");
//                    tvResult_maxSpeed.setText(String.valueOf(ServerWindows.RESULT_MAX_SPEED) + "km/h");
//                    break;
//                case SportData.RUNNING_MODE_CALORIES:
//                    tvResult_time.setText(TimeUtils.TimeFomat(RESULT_TIME));
//                    tvResult_calories.setText(String.valueOf(ServerWindows.RESULT_CAL) + "cal");
//                    tvResult_distance.setText(String.valueOf(ServerWindows.RESULT_KM) + "km");
//                    tvResult_avgSpeed.setText(String.valueOf(RESULT_AVG_SPEED) + "m/s");
//                    tvResult_maxSpeed.setText(String.valueOf(ServerWindows.RESULT_MAX_SPEED) + "km/h");
//                    break;
//                case SportData.RUNNING_MODE_LIVE:
//                    tvResult_time.setText(TimeUtils.TimeFomat(RESULT_TIME));
//                    tvResult_calories.setText(String.valueOf(ServerWindows.RESULT_CAL) + "cal");
//                    tvResult_distance.setText(String.valueOf(ServerWindows.RESULT_KM) + "km");
//                    tvResult_avgSpeed.setText(String.valueOf(RESULT_AVG_SPEED) + "m/s");
//                    tvResult_maxSpeed.setText(String.valueOf(ServerWindows.RESULT_MAX_SPEED) + "km/h");
//                    break;
//                case SportData.RUNNING_MODE_WARMUP:
//                    tvResult_time.setText(TimeUtils.TimeFomat(RESULT_TIME));
//                    tvResult_calories.setText(String.valueOf(ServerWindows.RESULT_CAL) + "cal");
//                    tvResult_distance.setText(String.valueOf(ServerWindows.RESULT_KM) + "km");
//                    tvResult_avgSpeed.setText(String.valueOf(RESULT_AVG_SPEED) + "m/s");
//                    tvResult_maxSpeed.setText(String.valueOf(ServerWindows.RESULT_MAX_SPEED) + "km/h");
//                    break;
//                case SportData.RUNNING_MODE_CLIMB:
//                    tvResult_time.setText(TimeUtils.TimeFomat(RESULT_TIME));
//                    tvResult_calories.setText(String.valueOf(ServerWindows.RESULT_CAL) + "cal");
//                    tvResult_distance.setText(String.valueOf(ServerWindows.RESULT_KM) + "km");
//                    tvResult_avgSpeed.setText(String.valueOf(RESULT_AVG_SPEED) + "m/s");
//                    tvResult_maxSpeed.setText(String.valueOf(ServerWindows.RESULT_MAX_SPEED) + "km/h");
//                    break;
//                case SportData.RUNNING_MODE_AEROBIC:
//                    tvResult_time.setText(TimeUtils.TimeFomat(RESULT_TIME));
//                    tvResult_calories.setText(String.valueOf(ServerWindows.RESULT_CAL) + "cal");
//                    tvResult_distance.setText(String.valueOf(ServerWindows.RESULT_KM) + "km");
//                    tvResult_avgSpeed.setText(String.valueOf(RESULT_AVG_SPEED) + "m/s");
//                    tvResult_maxSpeed.setText(String.valueOf(ServerWindows.RESULT_MAX_SPEED) + "km/h");
//                    break;
//                case SportData.RUNNING_MODE_HEALTH:
//                    tvResult_time.setText(TimeUtils.TimeFomat(RESULT_TIME));
//                    tvResult_calories.setText(String.valueOf(ServerWindows.RESULT_CAL) + "cal");
//                    tvResult_distance.setText(String.valueOf(ServerWindows.RESULT_KM) + "km");
//                    tvResult_avgSpeed.setText(String.valueOf(RESULT_AVG_SPEED) + "m/s");
//                    tvResult_maxSpeed.setText(String.valueOf(ServerWindows.RESULT_MAX_SPEED) + "km/h");
//                    break;
//                case SportData.RUNNING_MODE_LOSEWEIGHT:
//                    tvResult_time.setText(TimeUtils.TimeFomat(RESULT_TIME));
//                    tvResult_calories.setText(String.valueOf(ServerWindows.RESULT_CAL) + "cal");
//                    tvResult_distance.setText(String.valueOf(ServerWindows.RESULT_KM) + "km");
//                    tvResult_avgSpeed.setText(String.valueOf(RESULT_AVG_SPEED) + "m/s");
//                    tvResult_maxSpeed.setText(String.valueOf(ServerWindows.RESULT_MAX_SPEED) + "km/h");
//                    break;
//                case SportData.RUNNING_MODE_MAJOR:
//                    tvResult_time.setText(TimeUtils.TimeFomat(RESULT_TIME));
//                    tvResult_calories.setText(String.valueOf(ServerWindows.RESULT_CAL) + "cal");
//                    tvResult_distance.setText(String.valueOf(ServerWindows.RESULT_KM) + "km");
//                    tvResult_avgSpeed.setText(String.valueOf(RESULT_AVG_SPEED) + "m/s");
//                    tvResult_maxSpeed.setText(String.valueOf(ServerWindows.RESULT_MAX_SPEED) + "km/h");
//                    break;
//                case SportData.RUNNING_MODE_TRAINING_USER:
//                    tvResult_time.setText(TimeUtils.TimeFomat(RESULT_TIME));
//                    tvResult_calories.setText(String.valueOf(ServerWindows.RESULT_CAL) + "cal");
//                    tvResult_distance.setText(String.valueOf(ServerWindows.RESULT_KM) + "km");
//                    tvResult_avgSpeed.setText(String.valueOf(RESULT_AVG_SPEED) + "m/s");
//                    tvResult_maxSpeed.setText(String.valueOf(ServerWindows.RESULT_MAX_SPEED) + "km/h");
//                    break;
//                case SportData.RUNNING_MODE_TRAINING_HEART:
//                    tvResult_time.setText(TimeUtils.TimeFomat(RESULT_TIME));
//                    tvResult_calories.setText(String.valueOf(ServerWindows.RESULT_CAL) + "cal");
//                    tvResult_distance.setText(String.valueOf(ServerWindows.RESULT_KM) + "km");
//                    tvResult_avgSpeed.setText(String.valueOf(RESULT_AVG_SPEED) + "m/s");
//                    tvResult_maxSpeed.setText(String.valueOf(ServerWindows.RESULT_MAX_SPEED) + "km/h");
//                    break;
//            }
        }
        //避免暂停的时候还能加速度
        ServerWindows.service_speed.setText("0.0");
        ServerWindows.service_slope.setText("0");
        SportData.setStatus(SportData.MAIN_ACTIVITY);
    }

    private Handler sql_handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    SQLiteOpenHelper sql = new MySQLiteHelper(getContext());
                    SQLiteDatabase db = sql.getWritableDatabase();
                    insert_Record(db,
                            isApplication.Start_SystemTime,
                            ServerWindows.RESULT_TIME,
                            ServerWindows.RESULT_KM,
                            ServerWindows.RESULT_CAL
                    );
                    Log.e("查看當前狀態", String.valueOf(1));
                    break;
            }
        }
    };

    private Map<String, Object> map;

    private void sendupdataRunrecord() {
        if (!isApplication.user_id.equals("")) {
            if (map == null) {
                map = new HashMap<>();
            }
            Log.e("startime", isApplication.Start_SystemTime + "");
            Log.e("startime", System.currentTimeMillis() + "");
            map.put("user_id", isApplication.user_id);//用户ID
            map.put("starttime", isApplication.Start_SystemTime / 1000);//开始时间
            map.put("totletime", (End_SystemTime - isApplication.Start_SystemTime) / 1000);//总共时间
            map.put("speed", RESULT_AVG_SPEED);//速度
            map.put("distance", ServerWindows.RESULT_KM);//距离
            map.put("kaluli", ServerWindows.RESULT_CAL);//卡路里
            map.put("xinlv", "");//心率
            NetUtil.request(Api.updataRunrecord, new BaseObserver(getContext(), "正在上传跑步记录...") {
                @Override
                protected void success(JSONObject value, String code, String message, Disposable disposable) throws IOException, JSONException {
                    if (code.equals("true")) {
                        Log.e("print", value.toString() + "success");
                    } else {
                        Log.e("print", value.toString() + "bad");
                    }
                }
            }, map);
        } else {
            Message msg = new Message();
            msg.what = 1;
            sql_handler.handleMessage(msg);
        }
    }

    private void insert_Record(SQLiteDatabase db, long starttime, long alltime, double distance, double calories) {
        ContentValues values = new ContentValues();
        Log.e("查看傳入的值", String.valueOf(starttime) + " " + String.valueOf(alltime) + " " + String.valueOf(distance) + " " + String.valueOf(calories) + " ");
        values.put("starttime", starttime);
        values.put("alltime", alltime);
        values.put("distance", distance);
        values.put("calories", calories);
//        db.insert("record",null,values);
        long i = db.insert("record", null, values);
        Log.e("查看返回值", String.valueOf(i));
//        query(db);
        db.close();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_result:
                ((MainActivity) getActivity()).showFragment(14, 0);
                break;
            case R.id.result_click:

                break;
        }
    }
}
