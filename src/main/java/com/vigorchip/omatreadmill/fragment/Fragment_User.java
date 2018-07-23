package com.vigorchip.omatreadmill.fragment;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.contrarywind.listener.OnItemSelectedListener;
import com.contrarywind.view.WheelView;
import com.google.gson.Gson;
import com.vigorchip.omatreadmill.R;
import com.vigorchip.omatreadmill.activity.MainActivity;
import com.vigorchip.omatreadmill.adapter.SportDataAdapter;
import com.vigorchip.omatreadmill.application.isApplication;
import com.vigorchip.omatreadmill.base.BaseObserver;
import com.vigorchip.omatreadmill.bean.Data_Info;
import com.vigorchip.omatreadmill.bean.Item_Message;
import com.vigorchip.omatreadmill.bean.NetUtil;
import com.vigorchip.omatreadmill.bean.SportData;
import com.vigorchip.omatreadmill.bean.User_Info;
import com.vigorchip.omatreadmill.interfaci.Api;
import com.vigorchip.omatreadmill.sql.MySQLiteHelper;
import com.vigorchip.omatreadmill.utils.CreatDialog;
import com.vigorchip.omatreadmill.utils.DataUtil;
import com.vigorchip.omatreadmill.utils.TimeUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;

import static android.content.Context.MODE_PRIVATE;
import static com.vigorchip.omatreadmill.application.isApplication.UserData;
import static com.vigorchip.omatreadmill.application.isApplication.currentAge;
import static com.vigorchip.omatreadmill.application.isApplication.currentHeight;
import static com.vigorchip.omatreadmill.application.isApplication.currentWeight;
import static com.vigorchip.omatreadmill.application.isApplication.editor_RadixUser1;
import static com.vigorchip.omatreadmill.application.isApplication.editor_RadixUser2;
import static com.vigorchip.omatreadmill.application.isApplication.editor_user;
import static com.vigorchip.omatreadmill.application.isApplication.indexAge;
import static com.vigorchip.omatreadmill.application.isApplication.indexHeight;
import static com.vigorchip.omatreadmill.application.isApplication.indexWeight;
import static com.vigorchip.omatreadmill.application.isApplication.radix;
import static com.vigorchip.omatreadmill.application.isApplication.sb_currentHeight;
import static com.vigorchip.omatreadmill.application.isApplication.sb_currentWeight;
import static com.vigorchip.omatreadmill.application.isApplication.sb_indexHeight;
import static com.vigorchip.omatreadmill.application.isApplication.sb_indexWeight;
import static com.vigorchip.omatreadmill.application.isApplication.sp_RadixUser1;
import static com.vigorchip.omatreadmill.application.isApplication.sp_RadixUser2;

/**
 * Created by wr-app1 on 2018/3/28.
 */

public class Fragment_User extends Fragment implements View.OnClickListener {
    private ImageView btn_personal, btn_deviceInfo, btn_history, btn_sign;
    private Button clear_userMsg;
    private ImageButton btn_history_left, btn_history_right;
    private TextView user_title, lv_empty;
    private RelativeLayout Relative_userinfo, Relative_deviceinfo, Relative_userhistory;
    private Calendar calendar;
    private int mYear;
    private int mMonth;
    private int final_year;
    private int final_month;
    private Map<String, Object> getmap;
    private List<Item_Message> itemMessage;
    private SportDataAdapter sportDataAdapter;
    private RecyclerView lv;
    private RelativeLayout relativeLayout;

    private RelativeLayout btn_user_name, btn_user_age, btn_user_sex, btn_user_height, btn_user_weight, btn_user_radix;
    private TextView tv_user_name, tv_user_age, tv_user_height, tv_user_weight, tv_user_unit, unit_height, unit_weight, unit_scale;
    private ImageView img_user_sex;
    //設備信息頁
    private TextView tv_device_time, tv_device_distance, tv_device_calories, tv_device_workout, tv_device_avg, tv_device_lastTime, unit_distance, unit_avgSpeed;
    //年龄dialog
    private WheelView picker_age;
    private TextView btn_commit_age, btn_cancel_age;
    //用户名dialog
    private EditText et_inputName;
    private TextView btn_commit_name, btn_cancel_name;
    //性别dialog
    private RadioGroup Sex_rg;
    private RadioButton rb_man, rb_woman;
    //公制身高dialog
    private WheelView picker_height;
    private TextView btn_commit_height, btn_cancel_height;
    //公制体重dialog
    private WheelView picker_weight;
    private TextView btn_commit_weight, btn_cancel_weight;
    //英制身高dialog
    private WheelView picker_height_radix2;
    private TextView btn_commit_height_radix2, btn_cancel_height_radix2;
    //英制体重dialog
    private WheelView picker_weight_radix2;
    private TextView btn_commit_weight_radix2, btn_cancel_weight_radix2;
    //公英制dialog
    private TextView radix_cancel, radix_next;
    private RadioGroup Radix_rg;
    private RadioButton rb_metric, rb_british;
    //用戶退出dialog
    private TextView signOut_cancel, signOut_ok;

    private User_Info user_info;
    private View view;

    /**
     * 用户管理设备信息的信息
     */
    private SharedPreferences sp_Device;
    private SharedPreferences.Editor editor_Device;
    private File file_account;

    /**
     * 個人信息的信息
     */
    private SharedPreferences sp_User;
    private SharedPreferences.Editor editor;

    private String username;
    public static boolean isGone = true;
    private Map<String, Object> map;

    //用户登录状态的本地账号密码
    private File file;

    /**
     * 公英制切換身高體重的信息
     */
    private ArrayList<Integer> arr_height1;//公制身高集合
    private ArrayList<Integer> arr_height2;//英制身高集合

    private ArrayList<Integer> arr_weight1;//公制体重集合
    private ArrayList<Integer> arr_weight2;//英制体重集合

    private File sql_file;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Relative_userinfo = view.findViewById(R.id.Relative_userinfo);
        Relative_deviceinfo = view.findViewById(R.id.Relative_deviceinfo);
        Relative_userhistory = view.findViewById(R.id.Relative_userhistory);
        init();
        if (user_info.getSex() == 1) {
            img_user_sex.setImageResource(R.drawable.male);
        } else {
            img_user_sex.setImageResource(R.drawable.female);
        }
        btn_personal.setBackgroundResource(R.color.colorBlack);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            isGone = true;
        } else {
            isGone = false;
            //鐢ㄦ埛宸茬粡鐧婚檰
            if (!isApplication.user_id.equals("")) {
                btn_sign.setImageResource(R.drawable.signout);
                clear_userMsg.setVisibility(View.GONE);
                editor.putString("user_id", String.valueOf(isApplication.user_id));
                editor.commit();
                getRunMessage(mYear, mMonth);
                if (map == null) {
                    map = new HashMap<>();
                }
                map.put("user_id", isApplication.user_id);
                NetUtil.request(Api.getInfo, new BaseObserver(getContext(), true) {
                    @Override
                    protected void success(JSONObject value, String code, String message, Disposable disposable) throws IOException, JSONException {
                        if (code.equals("true")) {
                            String data = value.getString("data");
                            Data_Info data_info = new Gson().fromJson(data, Data_Info.class);
                            tv_user_name.setText(data_info.getNickname());
                            tv_user_age.setText(data_info.getAge());
                            tv_user_height.setText(data_info.getTall());
                            tv_user_weight.setText(data_info.getWeight());
                            if (data_info.getSex().equals("1")) {
                                img_user_sex.setImageResource(R.drawable.male);
                            } else if (data_info.getSex().equals("2")) {
                                img_user_sex.setImageResource(R.drawable.female);
                            }
                        }
                    }
                }, map);
            }
            if (Relative_userinfo.getVisibility() == View.VISIBLE) {
                setSelector(String.valueOf(getContext().getString(R.string.Profile)), Relative_userinfo, Relative_deviceinfo, Relative_userhistory,
                        btn_personal, btn_deviceInfo, btn_history);
            } else if (Relative_deviceinfo.getVisibility() == View.VISIBLE) {
                setSelector(String.valueOf(getContext().getString(R.string.Device)), Relative_deviceinfo, Relative_userinfo, Relative_userhistory,
                        btn_deviceInfo, btn_personal, btn_history);
                if (file.exists()) {
                    tv_device_time.setText(String.valueOf(((Integer.parseInt(sp_Device.getString("time", "")) / 3600))));
                    tv_device_distance.setText(sp_Device.getString("km", ""));
                    if (Double.parseDouble(sp_Device.getString("cal", "")) != 0.0) {
                        tv_device_calories.setText(String.valueOf(new BigDecimal(Double.parseDouble(sp_Device.getString("cal", "")) / 1000).setScale(1, BigDecimal.ROUND_DOWN).doubleValue()));
                    }
                    tv_device_workout.setText(sp_Device.getString("frequency", ""));
                    tv_device_avg.setText(sp_Device.getString("avgSpeed", ""));
                    tv_device_lastTime.setText(getDateToString(Long.parseLong(sp_Device.getString("lastTime", ""))));
                    if (Relative_deviceinfo.getVisibility() == View.VISIBLE) {
                        Log.e("查看有没有进来", String.valueOf(SportData.RADIX));
                        if (SportData.RADIX == 1 && isApplication.OLD_RADIX != 1) {//公制
                            if (Double.valueOf((String) tv_device_distance.getText()) != 0) {
                                tv_device_distance.setText(String.valueOf(new BigDecimal(Double.valueOf((String) tv_device_distance.getText()) / 0.62).setScale(1, BigDecimal.ROUND_DOWN).doubleValue()));
                            }
                            if (Double.valueOf((String) tv_device_avg.getText()) != 0) {
                                tv_device_avg.setText(String.valueOf(new BigDecimal(Double.valueOf((String) tv_device_avg.getText()) / 0.62).setScale(1, BigDecimal.ROUND_DOWN).doubleValue()));
                            }
                        }
                        if (SportData.RADIX == 2 && isApplication.OLD_RADIX != 2) {//英制
                            tv_device_distance.setText(String.valueOf(new BigDecimal(Double.valueOf((String) tv_device_distance.getText()) * 0.62).setScale(1, BigDecimal.ROUND_DOWN).doubleValue()));
                            tv_device_avg.setText(String.valueOf(new BigDecimal(Double.valueOf((String) tv_device_avg.getText()) * 0.62).setScale(1, BigDecimal.ROUND_DOWN).doubleValue()));
                        }
                    }
                }
            } else if (Relative_userhistory.getVisibility() == View.VISIBLE) {
                setSelector(String.valueOf(getContext().getString(R.string.WorkoutRecords)) + " (" + mYear + "-" + (mMonth >= 10 ? mMonth : "0" + mMonth) + ")", Relative_userhistory, Relative_userinfo, Relative_deviceinfo,
                        btn_history, btn_personal, btn_deviceInfo);
                if (!isApplication.user_id.equals("")) {
                    getRunMessage(mYear, mMonth);
                } else {
                    query(mYear, mMonth);
                }
            }
        }
    }

    private void init() {
        sql_file = new File("data/data/com.vigorchip.omatreadmill/databases/run_record.db");
        file = new File("/data/data/com.vigorchip.omatreadmill/shared_prefs/device_info.xml");
        sp_Device = getContext().getSharedPreferences("device_info", MODE_PRIVATE);
        editor_Device = sp_Device.edit();

        sp_User = getContext().getSharedPreferences("user_data", MODE_PRIVATE);
        editor = sp_User.edit();
        user_info = new User_Info();
        calendar = Calendar.getInstance();
        lv = view.findViewById(R.id.lv_history);
        relativeLayout = view.findViewById(R.id.history);
        ViewTreeObserver observer = relativeLayout.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                h = relativeLayout.getHeight();
                w = relativeLayout.getWidth();
            }
        });
        btn_history_left = view.findViewById(R.id.btn_history_left);
        btn_history_right = view.findViewById(R.id.btn_history_right);
        lv_empty = view.findViewById(R.id.lv_empty);
        btn_history_left.setOnClickListener(this);
        btn_history_right.setOnClickListener(this);

        btn_personal = view.findViewById(R.id.btn_personal);
        btn_deviceInfo = view.findViewById(R.id.btn_deviceInfo);
        btn_history = view.findViewById(R.id.btn_history);
        btn_personal.setOnClickListener(this);
        btn_deviceInfo.setOnClickListener(this);
        btn_history.setOnClickListener(this);
        user_title = view.findViewById(R.id.user_title);
        btn_sign = view.findViewById(R.id.btn_sign);
        clear_userMsg = view.findViewById(R.id.clear_userMsg);
        btn_sign.setOnClickListener(this);
        clear_userMsg.setOnClickListener(this);

        //Relative_userinfo
        btn_user_name = view.findViewById(R.id.btn_user_name);
        btn_user_age = view.findViewById(R.id.btn_user_age);
        btn_user_sex = view.findViewById(R.id.btn_user_sex);
        btn_user_height = view.findViewById(R.id.btn_user_height);
        btn_user_weight = view.findViewById(R.id.btn_user_weight);
        btn_user_radix = view.findViewById(R.id.btn_user_radix);
        unit_height = view.findViewById(R.id.unit_height);
        unit_weight = view.findViewById(R.id.unit_weight);
        unit_scale = view.findViewById(R.id.unit_scale);

        btn_user_name.setOnClickListener(this);
        btn_user_age.setOnClickListener(this);
        btn_user_sex.setOnClickListener(this);
        btn_user_height.setOnClickListener(this);
        btn_user_weight.setOnClickListener(this);
        btn_user_radix.setOnClickListener(this);
        tv_user_age = view.findViewById(R.id.tv_user_age);
        tv_user_name = view.findViewById(R.id.tv_user_name);
        tv_user_unit = view.findViewById(R.id.tv_user_unit);
        img_user_sex = view.findViewById(R.id.img_user_sex);
        tv_user_height = view.findViewById(R.id.tv_user_height);
        tv_user_weight = view.findViewById(R.id.tv_user_weight);
        tv_user_age.setText(String.valueOf(isApplication.currentAge));
        tv_user_height.setText(String.valueOf(currentHeight));
        tv_user_weight.setText(String.valueOf(currentWeight));
        //Relative_deviceInfo
        tv_device_time = view.findViewById(R.id.tv_device_time);
        tv_device_distance = view.findViewById(R.id.tv_device_distance);
        tv_device_calories = view.findViewById(R.id.tv_device_calories);
        tv_device_workout = view.findViewById(R.id.tv_device_workout);
        tv_device_avg = view.findViewById(R.id.tv_device_avg);
        tv_device_lastTime = view.findViewById(R.id.tv_device_lastTime);
        unit_distance = view.findViewById(R.id.unit_distance);
        unit_avgSpeed = view.findViewById(R.id.unit_avgSpeed);
        arr_height1 = user_info.getArr_height1();
        arr_weight1 = user_info.getArr_weight1();
        arr_height2 = user_info.getArr_height2();
        arr_weight2 = user_info.getArr_weight2();

        if (isApplication.user_Sex == 1) {
            img_user_sex.setImageResource(R.drawable.male);
            user_info.setSex(1);
        } else if (isApplication.user_Sex == 2) {
            img_user_sex.setImageResource(R.drawable.female);
            user_info.setSex(2);
        }
//        if (isApplication.first == false) {
        if (radix == 1) {
            tv_user_unit.setText(String.valueOf(getContext().getString(R.string.Metric)));
            unit_scale.setText("(cm,km,kg)");
            unit_weight.setText("(kg)");
            unit_height.setText("(cm)");
            unit_avgSpeed.setText("(km/h)");
            unit_distance.setText("(km)");
//                tv_user_weight.setText(String.valueOf((int) new BigDecimal(Integer.parseInt((String) tv_user_weight.getText()) / 2.2).setScale(0, BigDecimal.ROUND_DOWN).doubleValue()));
//                tv_user_height.setText(String.valueOf((int) new BigDecimal(Integer.parseInt((String) tv_user_height.getText()) / 0.39).setScale(0, BigDecimal.ROUND_DOWN).doubleValue()));
//                if (Double.valueOf((String) tv_device_distance.getText()) != 0) {
//                    tv_device_distance.setText(String.valueOf(new BigDecimal(Double.valueOf((String) tv_device_distance.getText()) / 0.62).setScale(1, BigDecimal.ROUND_DOWN).doubleValue()));
//                }
//                if (Double.valueOf((String) tv_device_avg.getText()) != 0) {
//                    tv_device_avg.setText(String.valueOf(new BigDecimal(Double.valueOf((String) tv_device_avg.getText()) / 0.62).setScale(1, BigDecimal.ROUND_DOWN).doubleValue()));
//                }
            SportData.RADIX = 1;
        } else if (radix == 2) {
            tv_user_unit.setText(String.valueOf(getContext().getString(R.string.Imperial)));
            unit_scale.setText("(in,mile,Ibs)");
            unit_weight.setText("(Ibs)");
            unit_height.setText("(inch)");
            unit_avgSpeed.setText("(MPH)");
            unit_distance.setText("(mile)");
//                tv_user_weight.setText(String.valueOf((int) new BigDecimal(Integer.parseInt((String) tv_user_weight.getText()) * 2.2).setScale(0, BigDecimal.ROUND_DOWN).doubleValue()));
//                tv_user_height.setText(String.valueOf((int) new BigDecimal(Integer.parseInt((String) tv_user_height.getText()) * 0.39).setScale(0, BigDecimal.ROUND_DOWN).doubleValue()));
//                tv_device_distance.setText(String.valueOf(new BigDecimal(Double.valueOf((String) tv_device_distance.getText()) * 0.62).setScale(1, BigDecimal.ROUND_DOWN).doubleValue()));
//                tv_device_avg.setText(String.valueOf(new BigDecimal(Double.valueOf((String) tv_device_avg.getText()) * 0.62).setScale(1, BigDecimal.ROUND_DOWN).doubleValue()));
            SportData.RADIX = 2;
        }
//        }

        //第一次進來查看有無登錄狀態
        if (!isApplication.user_id.equals("")) {
            btn_sign.setImageResource(R.drawable.signout);
            clear_userMsg.setVisibility(View.GONE);
            editor.putString("user_id", String.valueOf(isApplication.user_id));
            editor.commit();
            getRunMessage(mYear, mMonth);
            if (map == null) {
                map = new HashMap<>();
            }
            map.put("user_id", isApplication.user_id);
            NetUtil.request(Api.getInfo, new BaseObserver(getContext(), true) {
                @Override
                protected void success(JSONObject value, String code, String message, Disposable disposable) throws IOException, JSONException {
                    if (code.equals("true")) {
                        String data = value.getString("data");
                        Data_Info data_info = new Gson().fromJson(data, Data_Info.class);
                        tv_user_name.setText(data_info.getNickname());
                        tv_user_age.setText(data_info.getAge());
                        tv_user_height.setText(data_info.getTall());
                        tv_user_weight.setText(data_info.getWeight());
                        isApplication.currentAge = Integer.parseInt(data_info.getAge());
                        isApplication.currentHeight = Integer.parseInt(data_info.getTall());
                        isApplication.currentWeight = Integer.parseInt(data_info.getWeight());
                        if (data_info.getSex().equals("1")) {
                            img_user_sex.setImageResource(R.drawable.male);
                        } else if (data_info.getSex().equals("2")) {
                            img_user_sex.setImageResource(R.drawable.female);
                        }
                    }
                }
            }, map);
        } else {//第一次如果不是登錄狀態
            UserData("user_name");
            UserData("user_sex");
            UserData("user_age");
            UserData("user_height");
            UserData("user_weight");
            UserData("radix");
            tv_user_name.setText(isApplication.user_Name);
            if (isApplication.user_Sex == 1) {
                img_user_sex.setImageResource(R.drawable.male);
                user_info.setSex(1);
            } else {
                img_user_sex.setImageResource(R.drawable.female);
                user_info.setSex(2);
            }
            tv_user_age.setText(String.valueOf(isApplication.currentAge));
            if (radix == 1) {//公制獲取公制單位的身高體重
                SportData.RADIX = 1;
                tv_user_height.setText(String.valueOf(currentHeight));
                tv_user_weight.setText(String.valueOf(currentWeight));
            } else if (radix == 2) {//英制獲取英制單位的身高體重
                SportData.RADIX = 2;
                tv_user_height.setText(String.valueOf(sb_currentHeight));
                tv_user_weight.setText(String.valueOf(sb_currentWeight));
            }
        }
        //防止中英法语言切换时不显示中文默认名字
        String able = getContext().getResources().getConfiguration().locale.getLanguage();
        if (able.equals("zh")) {
            if (tv_user_name.getText().toString().equals("Not Set")) {
                isApplication.user_Name = "未设置";
                tv_user_name.setText(isApplication.user_Name);
            }
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    isApplication.user_id = "";
                    editor.putString("user_age", String.valueOf(isApplication.user_id));
                    editor.commit();
                    itemMessage.clear();
                    if (sportDataAdapter != null) {
                        sportDataAdapter.notifyDataSetChanged();
                    }
                    btn_sign.setImageResource(R.drawable.signin);
                    clear_userMsg.setVisibility(View.VISIBLE);
                    file_account = new File("/data/data/com.vigorchip.omatreadmill/shared_prefs/sp_account.xml");
                    if (file_account.exists()) {
                        file_account.delete();
                    }
                    isApplication.currentAge = 25;//当前年龄的默认值
                    isApplication.indexAge = 24;//当前年龄的默认下标
                    isApplication.currentHeight = 175;//公制当前身高的默认值
                    isApplication.currentWeight = 70;//公制当前体重的默认值
                    isApplication.indexHeight = 75;//公制当前身高的默认下标
                    isApplication.indexWeight = 50;//公制当前体重的默认下标
                    UserData("user_name");
                    UserData("user_sex");
                    UserData("user_age");
                    UserData("user_height");
                    UserData("user_weight");
                    UserData("radix");
                    tv_user_name.setText(isApplication.user_Name);
                    if (isApplication.user_Sex == 1) {
                        img_user_sex.setImageResource(R.drawable.male);
                        user_info.setSex(1);
                    } else {
                        img_user_sex.setImageResource(R.drawable.female);
                        user_info.setSex(2);
                    }
                    tv_user_age.setText(String.valueOf(isApplication.currentAge));
                    tv_user_height.setText(String.valueOf(currentHeight));
                    tv_user_weight.setText(String.valueOf(currentWeight));
                    if (radix == 1) {
                        SportData.RADIX = 1;
                    } else if (radix == 2) {
                        SportData.RADIX = 2;
                    }
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign:
                if (isApplication.user_id.equals("")) {
                    ((MainActivity) getActivity()).showFragment(8, 23);
                } else {
                    CreatDialog.showDialog(getContext(), R.layout.dialog_signout, true);
                    signOut_cancel = CreatDialog.dialog.findViewById(R.id.signOut_cancel);
                    signOut_ok = CreatDialog.dialog.findViewById(R.id.signOut_ok);
                    signOut_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            CreatDialog.dialog.dismiss();
                        }
                    });
                    signOut_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Message message = new Message();
                            message.what = 1;
                            handler.handleMessage(message);
                            CreatDialog.dialog.dismiss();
                            query(mYear,mMonth);
                        }
                    });
                }
                break;
            case R.id.clear_userMsg://删除键,恢复所有初始数据
                CreatDialog.showDialog(getContext(), R.layout.dialog_delete_info, true);
                TextView delete_cancel = CreatDialog.dialog.findViewById(R.id.delete_cancel);
                TextView delete_ok = CreatDialog.dialog.findViewById(R.id.delete_ok);
                delete_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        File deviceFile = new File("/data/data/com.vigorchip.omatreadmill/shared_prefs/device_info.xml");
                        File userFile = new File("/data/data/com.vigorchip.omatreadmill/shared_prefs/user_data.xml");
                        if (userFile.exists()) {
                            tv_user_name.setText(String.valueOf(getContext().getString(R.string.NotSet2)));
                            tv_user_age.setText("25");
                            tv_user_weight.setText("70");
                            tv_user_height.setText("175");
                            img_user_sex.setImageResource(R.drawable.male);
                            user_info.setSex(1);
                            isApplication.user_Sex = 1;
                            isApplication.indexHeight = 75;
                            isApplication.indexWeight = 50;
                            isApplication.indexAge = 24;
                            isApplication.ph_selector = 1;
                            isApplication.sb_indexHeight = 0;
                            sb_currentHeight = 39;
                            isApplication.sb_indexWeight = 0;
                            sb_currentWeight = 44;
                            tv_user_unit.setText(String.valueOf(getContext().getString(R.string.Metric)));
                            unit_scale.setText("(cm,km,kg)");
                            unit_weight.setText("(kg)");
                            unit_height.setText("(cm)");
                            unit_avgSpeed.setText("(km/h)");
                            unit_distance.setText("(km)");
                            isApplication.UserData("user_name");
                            isApplication.UserData("user_sex");
                            isApplication.UserData("user_age");
                        }
                        if (deviceFile.exists()) {
                            tv_device_time.setText("0");
                            tv_device_distance.setText("0.0");
                            tv_device_calories.setText("0.0");
                            tv_device_workout.setText("0");
                            tv_device_avg.setText("0.0");
                            tv_device_lastTime.setText("2017-01-01 00:00:00");
                            editor_Device.putString("time", String.valueOf(0));
                            editor_Device.putString("km", String.valueOf(0.0));
                            editor_Device.putString("cal", String.valueOf(0.0));
                            editor_Device.putString("frequency", String.valueOf(0));
                            editor_Device.putString("avgSpeed", String.valueOf(0.0));
                            editor_Device.putString("lastTime", "1483200000000");
                            editor_Device.commit();
                        }
                        isApplication.editor_RadixUser1.putString("user_height", String.valueOf(currentHeight));
                        isApplication.editor_RadixUser1.putString("user_index_height", String.valueOf(indexHeight));
                        isApplication.editor_RadixUser1.putString("user_weight", String.valueOf(currentWeight));
                        isApplication.editor_RadixUser1.putString("user_index_weight", String.valueOf(indexWeight));
                        isApplication.editor_RadixUser1.commit();
                        isApplication.editor_RadixUser2.putString("user_height", String.valueOf(sb_currentHeight));
                        isApplication.editor_RadixUser2.putString("user_index_height", String.valueOf(sb_indexHeight));
                        isApplication.editor_RadixUser2.putString("user_weight", String.valueOf(sb_currentWeight));
                        isApplication.editor_RadixUser2.putString("user_index_weight", String.valueOf(sb_indexWeight));
                        isApplication.editor_RadixUser2.commit();
                        isApplication.editor_user.putString("user_index_age", String.valueOf(24));
                        isApplication.editor_user.commit();
                        isApplication.indexAge = Integer.parseInt(sp_User.getString("user_index_age", ""));
                        isApplication.UserData("user_index_age");
                        isApplication.radix = 1;
                        SportData.RADIX = 1;
                        editor_user.putString("radix", String.valueOf(SportData.RADIX));
                        editor_user.commit();
                        if (sql_file.exists()) {
                            SQLiteOpenHelper sql = new MySQLiteHelper(getContext());
                            SQLiteDatabase db = sql.getWritableDatabase();
                            db.execSQL("delete from record");
                            if (sportDataAdapter!=null) {
                                sportDataAdapter.notifyDataSetChanged();
                            }
                        }
//                        isApplication.UserData("user_height");
//                        isApplication.UserData("user_index_height");
//                        isApplication.UserData("user_weight");
//                        isApplication.UserData("user_index_weight");
                        CreatDialog.dialog.dismiss();
                        CreatDialog.dialog = null;
                        Log.e("查看身高体重下标222", isApplication.indexHeight + "  " + isApplication.indexWeight);
                    }
                });
                delete_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CreatDialog.dialog.dismiss();
                        CreatDialog.dialog = null;
                    }
                });
                break;
            case R.id.btn_personal:
                setSelector(String.valueOf(getContext().getString(R.string.Profile)), Relative_userinfo, Relative_deviceinfo, Relative_userhistory,
                        btn_personal, btn_deviceInfo, btn_history);
                break;
            case R.id.btn_deviceInfo:
                setSelector(String.valueOf(getContext().getString(R.string.Device)), Relative_deviceinfo, Relative_userinfo, Relative_userhistory,
                        btn_deviceInfo, btn_personal, btn_history);
                if (file.exists()) {
                    tv_device_time.setText(String.valueOf(((Integer.parseInt(sp_Device.getString("time", "")) / 3600))));
                    tv_device_distance.setText(sp_Device.getString("km", ""));
                    if (Double.parseDouble(sp_Device.getString("cal", "")) != 0.0) {
                        tv_device_calories.setText(String.valueOf(new BigDecimal(Double.parseDouble(sp_Device.getString("cal", "")) / 1000).setScale(1, BigDecimal.ROUND_DOWN).doubleValue()));
                    }
                    tv_device_workout.setText(sp_Device.getString("frequency", ""));
                    tv_device_avg.setText(sp_Device.getString("avgSpeed", ""));
                    tv_device_lastTime.setText(getDateToString(Long.parseLong(sp_Device.getString("lastTime", ""))));
                    if (Relative_deviceinfo.getVisibility() == View.VISIBLE) {
                        Log.e("查看有没有进来", String.valueOf(SportData.RADIX));
                        if (SportData.RADIX == 1 && isApplication.OLD_RADIX != 1) {//公制
                            if (Double.valueOf((String) tv_device_distance.getText()) != 0) {
                                tv_device_distance.setText(String.valueOf(new BigDecimal(Double.valueOf((String) tv_device_distance.getText()) / 0.62).setScale(1, BigDecimal.ROUND_DOWN).doubleValue()));
                            }
                            if (Double.valueOf((String) tv_device_avg.getText()) != 0) {
                                tv_device_avg.setText(String.valueOf(new BigDecimal(Double.valueOf((String) tv_device_avg.getText()) / 0.62).setScale(1, BigDecimal.ROUND_DOWN).doubleValue()));
                            }
                        }
                        if (SportData.RADIX == 2 && isApplication.OLD_RADIX != 2) {//英制
                            tv_device_distance.setText(String.valueOf(new BigDecimal(Double.valueOf((String) tv_device_distance.getText()) * 0.62).setScale(1, BigDecimal.ROUND_DOWN).doubleValue()));
                            tv_device_avg.setText(String.valueOf(new BigDecimal(Double.valueOf((String) tv_device_avg.getText()) * 0.62).setScale(1, BigDecimal.ROUND_DOWN).doubleValue()));
                        }
                    }
                }
                break;
            case R.id.btn_history:
                mYear = calendar.get(Calendar.YEAR); // 获取当前年份
                mMonth = calendar.get(Calendar.MONTH) + 1;// 获取当前月份
                final_month = mMonth;
                final_year = mYear;
                setSelector(String.valueOf(getContext().getString(R.string.WorkoutRecords)) + " (" + mYear + "-" + (mMonth >= 10 ? mMonth : "0" + mMonth) + ")", Relative_userhistory, Relative_userinfo, Relative_deviceinfo,
                        btn_history, btn_personal, btn_deviceInfo);
                if (!isApplication.user_id.equals("")) {
                    getRunMessage(mYear, mMonth);
                } else {
                    query(mYear, mMonth);
                }
                break;
            case R.id.btn_user_name:
                CreatDialog.showDialog(getContext(), R.layout.dialog_input_name, true);
                et_inputName = CreatDialog.dialog.findViewById(R.id.et_inputName);
                et_inputName.setSelection(et_inputName.getText().length());
                btn_commit_name = CreatDialog.dialog.findViewById(R.id.btn_commit_name);
                btn_cancel_name = CreatDialog.dialog.findViewById(R.id.btn_cancel_name);
                if (!tv_user_name.getText().equals("")) {
                    et_inputName.setText(tv_user_name.getText());
                }
                btn_commit_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        username = et_inputName.getText().toString().trim();
                        if (username.length() != 0) {
                            tv_user_name.setText(username);
                            if (isApplication.user_id.equals("")) {
                                editor.putString("user_name", String.valueOf(username));
                                editor.commit();
                            }
                            setUserMsg(isApplication.user_id, "nickname", username);
                        } else if (username.length() == 0) {
                            tv_user_name.setText(getContext().getString(R.string.NotSet2));
                        }
                        CreatDialog.dialog.dismiss();
                    }
                });
                btn_cancel_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CreatDialog.dialog.dismiss();
                    }
                });
                break;
            case R.id.btn_user_age:
                CreatDialog.showDialog(getContext(), R.layout.dialog_age, false);
                picker_age = CreatDialog.dialog.findViewById(R.id.picker_age);
                btn_commit_age = CreatDialog.dialog.findViewById(R.id.btn_commit_age);
                btn_cancel_age = CreatDialog.dialog.findViewById(R.id.btn_cancel_age);
                isApplication.editor_user.putString("user_index_age", String.valueOf(
                        user_info.backAgeIndex(Integer.parseInt(String.valueOf(tv_user_age.getText())))));
                isApplication.editor_user.commit();
                isApplication.indexAge = Integer.parseInt(sp_User.getString("user_index_age", ""));
                picker_age.setCurrentItem(indexAge);
                picker_age.setCyclic(true);
                picker_age.setAdapter(new ArrayWheelAdapter(user_info.getArr_age()));
                picker_age.setOnItemSelectedListener(new OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(int index) {
                        indexAge = index;
                        currentAge = user_info.getArr_age().get(indexAge);
                        picker_age.setCurrentItem(indexAge);
                        Log.i("Ageindex", String.valueOf(indexAge));
                    }
                });
                btn_commit_age.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isApplication.user_id.equals("")) {
                            editor.putString("user_index_age", String.valueOf(indexAge));
                            editor.putString("user_age", String.valueOf(isApplication.currentAge));
                            editor.commit();
                        }
                        tv_user_age.setText(String.valueOf(isApplication.currentAge));
                        setUserMsg(isApplication.user_id, "age", String.valueOf(currentAge));
                        CreatDialog.dialog.dismiss();
                    }
                });
                btn_cancel_age.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CreatDialog.dialog.dismiss();
                    }
                });
                break;
            case R.id.btn_user_sex:
                CreatDialog.showDialog(getContext(), R.layout.dialog_sex, true);
                Sex_rg = CreatDialog.dialog.findViewById(R.id.Sex_rg);
                rb_man = CreatDialog.dialog.findViewById(R.id.rb_man);
                rb_woman = CreatDialog.dialog.findViewById(R.id.rb_woman);
                if (user_info.getSex() == 1) {
                    rb_man.setChecked(true);
                    rb_woman.setChecked(false);
                } else {
                    rb_woman.setChecked(true);
                    rb_man.setChecked(false);
                }
                Sex_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int rb) {
                        RadioButton radioButton = Sex_rg.findViewById(rb);
                        String result = radioButton.getText().toString();
                        switch (result) {
                            case "男":
                                img_user_sex.setImageResource(R.drawable.male);
                                user_info.setSex(1);
                                if (isApplication.user_id.equals("")) {
                                    editor.putString("user_sex", String.valueOf(1));
                                    editor.commit();
                                }
                                CreatDialog.dialog.dismiss();
                                setUserMsg(isApplication.user_id, "sex", String.valueOf("1"));
                                break;
                            case "女":
                                img_user_sex.setImageResource(R.drawable.female);
                                user_info.setSex(2);
                                if (isApplication.user_id.equals("")) {
                                    editor.putString("user_sex", String.valueOf(2));
                                    editor.commit();
                                }
                                CreatDialog.dialog.dismiss();
                                setUserMsg(isApplication.user_id, "sex", String.valueOf("2"));
                                break;
                            case "Male":
                                img_user_sex.setImageResource(R.drawable.male);
                                user_info.setSex(1);
                                if (isApplication.user_id.equals("")) {
                                    editor.putString("user_sex", String.valueOf(1));
                                    editor.commit();
                                }
                                CreatDialog.dialog.dismiss();
                                setUserMsg(isApplication.user_id, "sex", String.valueOf("1"));
                                break;
                            case "Female":
                                img_user_sex.setImageResource(R.drawable.female);
                                user_info.setSex(2);
                                if (isApplication.user_id.equals("")) {
                                    editor.putString("user_sex", String.valueOf(2));
                                    editor.commit();
                                }
                                CreatDialog.dialog.dismiss();
                                setUserMsg(isApplication.user_id, "sex", String.valueOf("2"));
                                break;
                        }
                    }
                });
                break;
            case R.id.btn_user_height:
                Log.e("查看公制身高下标", String.valueOf(isApplication.indexHeight));
                if (SportData.RADIX == 1) {
                    CreatDialog.showDialog(getContext(), R.layout.dialog_height, false);
                    picker_height = CreatDialog.dialog.findViewById(R.id.picker_height);
                    btn_commit_height = CreatDialog.dialog.findViewById(R.id.btn_commit_height);
                    btn_cancel_height = CreatDialog.dialog.findViewById(R.id.btn_cancel_height);
                    editor_RadixUser1.putString("user_index_height", String.valueOf(
                            user_info.backHeightIndex1(Integer.parseInt(String.valueOf(tv_user_height.getText())))));
                    editor_RadixUser1.commit();
                    indexHeight = Integer.parseInt(sp_RadixUser1.getString("user_index_height", ""));
                    picker_height.setCurrentItem(indexHeight);
                    picker_height.setCyclic(true);
                    picker_height.setAdapter(new ArrayWheelAdapter(arr_height1));
                    if (arr_height1.size() != 0) {
                        currentHeight = arr_height1.get(isApplication.indexHeight);
                    }
                    picker_height.setOnItemSelectedListener(new OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(int index) {
                            isApplication.indexHeight = index;
                            currentHeight = arr_height1.get(isApplication.indexHeight);
                            picker_height.setCurrentItem(isApplication.indexHeight);
                        }
                    });
                    btn_commit_height.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (isApplication.user_id.equals("")) {
                                editor_RadixUser1.putString("user_index_height", String.valueOf(isApplication.indexHeight));
                                editor_RadixUser1.putString("user_height", String.valueOf(currentHeight));
                                editor_RadixUser1.commit();
                            }
                            tv_user_height.setText(String.valueOf(currentHeight));
                            setUserMsg(isApplication.user_id, "tall", String.valueOf(currentHeight));
                            CreatDialog.dialog.dismiss();
                        }
                    });
                    btn_cancel_height.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            CreatDialog.dialog.dismiss();
                        }
                    });
                } else if (SportData.RADIX == 2) {
                    CreatDialog.showDialog(getContext(), R.layout.dialog_height_radix2, false);
                    picker_height_radix2 = CreatDialog.dialog.findViewById(R.id.picker_height_radix2);
                    btn_commit_height_radix2 = CreatDialog.dialog.findViewById(R.id.btn_commit_height_radix2);
                    btn_cancel_height_radix2 = CreatDialog.dialog.findViewById(R.id.btn_cancel_height_radix2);
                    Log.e("查看英制身高下标", String.valueOf(sb_indexHeight));
                    editor_RadixUser2.putString("user_index_height", String.valueOf(
                            user_info.backHeightIndex2(Integer.parseInt(String.valueOf(tv_user_height.getText())))));
                    editor_RadixUser2.commit();
                    sb_indexHeight = Integer.parseInt(sp_RadixUser2.getString("user_index_height", ""));
                    picker_height_radix2.setCurrentItem(sb_indexHeight);
                    picker_height_radix2.setCyclic(true);
                    picker_height_radix2.setAdapter(new ArrayWheelAdapter(arr_height2));
                    if (arr_height2.size() != 0) {
                        sb_currentHeight = arr_height2.get(sb_indexHeight);
                    }
                    picker_height_radix2.setOnItemSelectedListener(new OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(int index) {
                            Log.e("index", String.valueOf(index));
                            if (index == -1) {//防數組越界
                                sb_indexHeight = 0;
                            } else if (index > 48) {
                                sb_indexHeight = 0;
                            } else {
                                sb_indexHeight = index;
                            }
                            sb_currentHeight = arr_height2.get(sb_indexHeight);
                            picker_height_radix2.setCurrentItem(sb_indexHeight);
                        }
                    });
                    btn_commit_height_radix2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (isApplication.user_id.equals("")) {
                                editor_RadixUser2.putString("user_index_height", String.valueOf(sb_indexHeight));
                                editor_RadixUser2.putString("user_height", String.valueOf(sb_currentHeight));
                                editor_RadixUser2.commit();
                            }
                            tv_user_height.setText(String.valueOf(sb_currentHeight));
                            setUserMsg(isApplication.user_id, "tall", String.valueOf(sb_currentHeight));
                            CreatDialog.dialog.dismiss();
                        }
                    });
                    btn_cancel_height_radix2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            CreatDialog.dialog.dismiss();
                        }
                    });
                }
                break;
            case R.id.btn_user_weight:
                Log.e("查看公英制", String.valueOf(SportData.RADIX) + "   " + String.valueOf(isApplication.radix));
                if (SportData.RADIX == 1) {
                    CreatDialog.showDialog(getContext(), R.layout.dialog_weight, false);
                    picker_weight = CreatDialog.dialog.findViewById(R.id.picker_weight);
                    btn_commit_weight = CreatDialog.dialog.findViewById(R.id.btn_commit_weight);
                    btn_cancel_weight = CreatDialog.dialog.findViewById(R.id.btn_cancel_weight);
                    editor_RadixUser1.putString("user_index_weight", String.valueOf(
                            user_info.backWeightIndex1(Integer.parseInt(String.valueOf(tv_user_weight.getText())))));
                    editor_RadixUser1.commit();
                    indexWeight = Integer.parseInt(sp_RadixUser1.getString("user_index_weight", ""));
                    picker_weight.setCurrentItem(indexWeight);
                    picker_weight.setCyclic(true);
                    picker_weight.setAdapter(new ArrayWheelAdapter(arr_weight1));
                    if (arr_weight1.size() != 0) {
                        currentWeight = arr_weight1.get(indexWeight);
                    }
                    picker_weight.setOnItemSelectedListener(new OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(int index) {
                            if (index == -1) {//防數組越界
                                isApplication.indexWeight = arr_weight1.size() - 1;
                            } else if (index >= arr_weight1.size()) {
                                isApplication.indexWeight = 0;
                            } else {
                                isApplication.indexWeight = index;
                            }
                            currentWeight = arr_weight1.get(indexWeight);
                            Log.e("查看下表77", String.valueOf(currentWeight));
                            picker_weight.setCurrentItem(indexWeight);
                            Log.i("Weightindex", String.valueOf(indexWeight));
                        }
                    });
                    btn_commit_weight.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (isApplication.user_id.equals("")) {
                                editor_RadixUser1.putString("user_index_weight", String.valueOf(indexWeight));
                                editor_RadixUser1.putString("user_weight", String.valueOf(currentWeight));
                                editor_RadixUser1.commit();
                            }
                            tv_user_weight.setText(String.valueOf(currentWeight));
                            setUserMsg(isApplication.user_id, "weight", String.valueOf(currentWeight));
                            Log.e("查看下表78", String.valueOf(currentWeight));
                            CreatDialog.dialog.dismiss();
                        }
                    });
                    btn_cancel_weight.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            CreatDialog.dialog.dismiss();
                        }
                    });
                } else if (SportData.RADIX == 2) {
                    CreatDialog.showDialog(getContext(), R.layout.dialog_weight_radix2, false);
                    picker_weight_radix2 = CreatDialog.dialog.findViewById(R.id.picker_weight_radix2);
                    btn_commit_weight_radix2 = CreatDialog.dialog.findViewById(R.id.btn_commit_weight_radix2);
                    btn_cancel_weight_radix2 = CreatDialog.dialog.findViewById(R.id.btn_cancel_weight_radix2);
                    Log.e("查看英制体重下标", String.valueOf(sb_indexWeight));
                    editor_RadixUser2.putString("user_index_weight", String.valueOf(
                            user_info.backWeightIndex2(Integer.parseInt(String.valueOf(tv_user_weight.getText())))));
                    editor_RadixUser2.commit();
                    sb_indexWeight = Integer.parseInt(sp_RadixUser2.getString("user_index_weight", ""));
                    picker_weight_radix2.setCurrentItem(sb_indexWeight);
                    picker_weight_radix2.setCyclic(true);
                    picker_weight_radix2.setAdapter(new ArrayWheelAdapter(arr_weight2));
                    if (arr_weight2.size() != 0) {
                        sb_currentWeight = arr_weight2.get(sb_indexWeight);
                    }
                    picker_weight_radix2.setOnItemSelectedListener(new OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(int index) {
                            sb_indexWeight = index;
                            sb_currentWeight = arr_weight2.get(sb_indexWeight);
                            Log.e("查看下表77", String.valueOf(sb_currentWeight));
                            picker_weight_radix2.setCurrentItem(sb_indexWeight);
                            Log.i("Weightindex", String.valueOf(sb_indexWeight));
                        }
                    });
                    btn_commit_weight_radix2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (isApplication.user_id.equals("")) {
                                editor_RadixUser2.putString("user_index_weight", String.valueOf(sb_indexWeight));
                                editor_RadixUser2.putString("user_weight", String.valueOf(sb_currentWeight));
                                editor_RadixUser2.commit();
                            }
                            tv_user_weight.setText(String.valueOf(sb_currentWeight));
                            setUserMsg(isApplication.user_id, "weight", String.valueOf(sb_currentWeight));
                            Log.e("查看下表78", String.valueOf(sb_currentWeight));
                            CreatDialog.dialog.dismiss();
                        }
                    });
                    btn_cancel_weight_radix2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            CreatDialog.dialog.dismiss();
                        }
                    });
                }
                break;
            case R.id.btn_user_radix:
                CreatDialog.showDialog(getContext(), R.layout.dialog_radix_toast, true);
                radix_cancel = CreatDialog.dialog.findViewById(R.id.radix_cancel);
                radix_next = CreatDialog.dialog.findViewById(R.id.radix_next);
                radix_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CreatDialog.dialog.dismiss();
                    }
                });
                radix_next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CreatDialog.dialog.dismiss();
                        CreatDialog.showDialog(getContext(), R.layout.dialog_radix, true);
                        Radix_rg = CreatDialog.dialog.findViewById(R.id.Radix_rg);
                        rb_metric = CreatDialog.dialog.findViewById(R.id.rb_metric);
                        rb_british = CreatDialog.dialog.findViewById(R.id.rb_british);
                        if (SportData.RADIX == 1) {
                            rb_metric.setChecked(true);
                            rb_british.setChecked(false);
                        } else if (SportData.RADIX == 2) {
                            rb_british.setChecked(true);
                            rb_metric.setChecked(false);
                        }
                        Radix_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup radioGroup, int rb) {
                                RadioButton radioButton = Radix_rg.findViewById(rb);
                                String result = radioButton.getText().toString();
                                switch (result) {
                                    case "公制":
                                        rb_metric.setChecked(true);
                                        rb_british.setChecked(false);
                                        tv_user_unit.setText(getContext().getString(R.string.Metric));
                                        unit_scale.setText("(cm,km,kg)");
                                        unit_weight.setText("(kg)");
                                        unit_height.setText("(cm)");
                                        unit_avgSpeed.setText("(km/h)");
                                        unit_distance.setText("(km)");
                                        isApplication.radixUserInfo("user_height", 1);
                                        isApplication.radixUserInfo("user_weight", 1);
                                        if (SportData.RADIX != 1) {
                                            tv_user_weight.setText(String.valueOf(currentWeight));
                                            tv_user_height.setText(String.valueOf(currentHeight));
                                        }
                                        SportData.RADIX = 1;
                                        editor.putString("radix", String.valueOf(SportData.RADIX));
                                        editor.commit();
                                        break;
                                    case "英制":
                                        rb_british.setChecked(true);
                                        rb_metric.setChecked(false);
                                        tv_user_unit.setText(getContext().getString(R.string.Imperial));
                                        unit_scale.setText("(in,mile,Ibs)");
                                        unit_weight.setText("(Ibs)");
                                        unit_height.setText("(inch)");
                                        unit_avgSpeed.setText("(MPH)");
                                        unit_distance.setText("(mile)");
                                        isApplication.radixUserInfo("user_height", 2);
                                        isApplication.radixUserInfo("user_weight", 2);
                                        if (SportData.RADIX != 2) {
                                            tv_user_weight.setText(String.valueOf(sb_currentWeight));
                                            tv_user_height.setText(String.valueOf(sb_currentHeight));
                                        }
                                        SportData.RADIX = 2;
                                        editor.putString("radix", String.valueOf(SportData.RADIX));
                                        editor.commit();
                                        break;
                                    case "Metric":
                                        rb_metric.setChecked(true);
                                        rb_british.setChecked(false);
                                        tv_user_unit.setText(getContext().getString(R.string.Metric));
                                        unit_scale.setText("(cm,km,kg)");
                                        unit_weight.setText("(kg)");
                                        unit_height.setText("(cm)");
                                        unit_avgSpeed.setText("(km/h)");
                                        unit_distance.setText("(km)");
                                        isApplication.radixUserInfo("user_height", 1);
                                        isApplication.radixUserInfo("user_weight", 1);
                                        if (SportData.RADIX != 1) {
                                            tv_user_weight.setText(String.valueOf(currentWeight));
                                            tv_user_height.setText(String.valueOf(currentHeight));
                                        }
                                        SportData.RADIX = 1;
                                        editor.putString("radix", String.valueOf(SportData.RADIX));
                                        editor.commit();
                                        break;
                                    case "Imperial":
                                        rb_british.setChecked(true);
                                        rb_metric.setChecked(false);
                                        tv_user_unit.setText(getContext().getString(R.string.Imperial));
                                        unit_scale.setText("(in,mile,Ibs)");
                                        unit_weight.setText("(Ibs)");
                                        unit_height.setText("(inch)");
                                        unit_avgSpeed.setText("(MPH)");
                                        unit_distance.setText("(mile)");
                                        isApplication.radixUserInfo("user_height", 2);
                                        isApplication.radixUserInfo("user_weight", 2);
                                        if (SportData.RADIX != 2) {
                                            tv_user_weight.setText(String.valueOf(sb_currentWeight));
                                            tv_user_height.setText(String.valueOf(sb_currentHeight));
                                        }
                                        SportData.RADIX = 2;
                                        editor.putString("radix", String.valueOf(SportData.RADIX));
                                        editor.commit();
                                        break;
                                    case "Métrique":
                                        rb_metric.setChecked(true);
                                        rb_british.setChecked(false);
                                        tv_user_unit.setText(getContext().getString(R.string.Metric));
                                        unit_scale.setText("(cm,km,kg)");
                                        unit_weight.setText("(kg)");
                                        unit_height.setText("(cm)");
                                        unit_avgSpeed.setText("(km/h)");
                                        unit_distance.setText("(km)");
                                        isApplication.radixUserInfo("user_height", 1);
                                        isApplication.radixUserInfo("user_weight", 1);
                                        if (SportData.RADIX != 1) {
                                            tv_user_weight.setText(String.valueOf(currentWeight));
                                            tv_user_height.setText(String.valueOf(currentHeight));
                                        }
                                        SportData.RADIX = 1;
                                        editor.putString("radix", String.valueOf(SportData.RADIX));
                                        editor.commit();
                                        break;
                                    case "Impérial":
                                        rb_british.setChecked(true);
                                        rb_metric.setChecked(false);
                                        tv_user_unit.setText(getContext().getString(R.string.Imperial));
                                        unit_scale.setText("(in,mile,Ibs)");
                                        unit_weight.setText("(Ibs)");
                                        unit_height.setText("(inch)");
                                        unit_avgSpeed.setText("(MPH)");
                                        unit_distance.setText("(mile)");
                                        isApplication.radixUserInfo("user_height", 2);
                                        isApplication.radixUserInfo("user_weight", 2);
                                        if (SportData.RADIX != 2) {
                                            tv_user_weight.setText(String.valueOf(sb_currentWeight));
                                            tv_user_height.setText(String.valueOf(sb_currentHeight));
                                        }
                                        SportData.RADIX = 2;
                                        editor.putString("radix", String.valueOf(SportData.RADIX));
                                        editor.commit();
                                        break;
                                }
                            }
                        });
                    }
                });
                break;
            case R.id.btn_history_left:
                if (mMonth == 1) {
                    mMonth = 12;
                    mYear--;
                } else {
                    mMonth--;
                }
                if (!isApplication.user_id.equals("")) {
                    getRunMessage(mYear, mMonth);
                } else {
                    query(mYear, mMonth);
                }
                user_title.setText(String.valueOf(getContext().getString(R.string.WorkoutRecords) + " (" + mYear + "-" + (mMonth >= 10 ? mMonth : "0" + mMonth) + ")"));
                break;
            case R.id.btn_history_right:
                if (final_year > mYear) {
                    if (mMonth == 12) {
                        mMonth = 1;
                        mYear++;
                    } else {
                        mMonth++;
                    }
                } else if (final_year == mYear) {
                    if (final_month > mMonth) {
                        mMonth++;
                    }
                }
                if (!isApplication.user_id.equals("")) {
                    getRunMessage(mYear, mMonth);
                } else {
                    query(mYear, mMonth);
                }
                user_title.setText(String.valueOf(getContext().getString(R.string.WorkoutRecords) + " (" + mYear + "-" + (mMonth >= 10 ? mMonth : "0" + mMonth) + ")"));
                break;
        }
    }



    public void query(int Year, int Month) {
        if (sql_file.exists()) {
            if (itemMessage == null) {
                itemMessage = new ArrayList();
            }

            itemMessage.clear();
            String currentData = Year + "-" + (Month >= 10 ? Month : "0" + Month);
            Log.d("查看时间转换", currentData);
            SQLiteDatabase db = getContext().openOrCreateDatabase("run_record.db", MODE_PRIVATE, null);
            //查询全部数据
            Cursor cursor = db.rawQuery("select * from " + "record", null);
            //将游标移到第一行
            cursor.moveToFirst();
            //循环读取数据
            while (!cursor.isAfterLast()) {
                //获得当前行的标签对应的数据
                long starttime = cursor.getLong(cursor.getColumnIndex("starttime"));
                String sql_data = TimeUtils.transForDate(String.valueOf(starttime));//利用开始时间转换成日期判断
                long alltime = cursor.getLong(cursor.getColumnIndex("alltime"));
                double distance = cursor.getDouble(cursor.getColumnIndex("distance"));
                double calories = cursor.getDouble(cursor.getColumnIndex("calories"));

                if (sql_data.equals(currentData)) {
                    Log.d("查看数据库获取的数据",
                            " **  starttime:" + starttime
                                    + " **  alltime: " + alltime
                                    + " **  distance:" + distance
                                    + " **  calories: " + calories);
                    Item_Message item = new Item_Message(String.valueOf(starttime),
                            String.valueOf(alltime),
                            String.valueOf(distance),
                            String.valueOf(calories));
                    itemMessage.add(item);
                }
                //游标移到下一行
                cursor.moveToNext();
            }
            db.close();
            setdata();
        } else {
            Log.e("listview---5", String.valueOf(sportDataAdapter== null));
//            lv.showEmpty();
            lv.setVisibility(View.GONE);
            lv_empty.setVisibility(View.VISIBLE);
        }
    }

    private void setdata() {
        if (itemMessage.size()==0) {
            if (sportDataAdapter!=null) {
                sportDataAdapter.notifyDataSetChanged();
            }
            lv.setVisibility(View.GONE);
            lv_empty.setVisibility(View.VISIBLE);
        } else {
            lv.setVisibility(View.VISIBLE);
            lv_empty.setVisibility(View.GONE);
            if (sportDataAdapter == null) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
                lv.setLayoutManager(linearLayoutManager);
                sportDataAdapter = new SportDataAdapter(getContext(),itemMessage,h);
//                adapter = new CommonAdapter<Item_Message>(getContext(), R.layout.lv_run_item, itemMessage) {
//                    @Override
//                    protected void convert(ViewHolder holder, Item_Message s, int position) {
//                        Log.e("Item_Message", s.toString());
//                        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.getConvertView().getLayoutParams();
//                        params.width = WindowManager.LayoutParams.MATCH_PARENT;
//                        params.height = h / 5;
//                        holder.getConvertView().setLayoutParams(params);
//                        TextView item_starttime = holder.getView(R.id.item_starttime);
//                        item_starttime.setText(getDateToString(Long.parseLong(s.getStarttime())));
//                        TextView item_timeall = holder.getView(R.id.item_timeall);
//                        item_timeall.setText(TimeUtils.TimeFomat(Long.parseLong(s.getTotletime())));
//                        TextView item_distance = holder.getView(R.id.item_distance);
//                        item_distance.setText(s.getDistance() + "km");
//                        TextView itme_calorie = holder.getView(R.id.itme_calorie);
//                        itme_calorie.setText(s.getKaluli() + "cal");
//                        TextView item_datatime = holder.getView(R.id.item_datatime);
//                        String strtime1 = (String) item_starttime.getText();
//                        String strtime2 = strtime1.substring(11, 13);//截取字符串獲取小時
//                        Date date = new Date(Long.parseLong(s.getStarttime()) * 1000);//設置時間戳秒形式獲取星期
//                        Log.e("查看日跑夜跑", strtime2);
//                        if (Integer.parseInt(strtime2) >= 18) {//大于18代表大於六點，表示夜跑
//                            item_datatime.setText(TimeUtils.getWeekOfDate(date) + "  " + getContext().getString(R.string.NightRun));
//                        } else {
//                            item_datatime.setText(TimeUtils.getWeekOfDate(date) + "  " + getContext().getString(R.string.DayRun));
//                        }
//                    }
//                };

               lv.setAdapter(sportDataAdapter);
                Log.e("print11",sportDataAdapter.getItemCount()+"");
//                Log.e("listview---1", String.valueOf(adapter.getItemCount()));
//                            lv.setAdapter(adapter);
            } else {
                Log.e("listview---3", String.valueOf(sportDataAdapter == null));
                sportDataAdapter.notifyDataSetChanged();
//                adapter.notifyDataSetChanged();
            }

        }
    }

    private Map<String, Object> userMap;

    private void setUserMsg(String userId, String arg0, String arg1) {
        if (!isApplication.user_id.equals("")) {
            if (userMap == null) {
                userMap = new HashMap<>();
            }
            userMap.put("user_id", userId);
            userMap.put(arg0, arg1);
            NetUtil.request(Api.compelete, new BaseObserver(getContext(), "正在上传中...") {
                @Override
                protected void success(JSONObject value, String code, String message, Disposable disposable) throws IOException, JSONException {
                    if (code.equals("true")) {
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    }
                }
            }, userMap);
        }
    }

    private int w;
    private int h;

    private void getRunMessage(int Year, int Month) {
        if (getmap == null) {
            getmap = new HashMap<>();
        }
        if (itemMessage == null) {
            itemMessage = new ArrayList();
        }
        getmap.clear();
        itemMessage.clear();
        long starttime = DataUtil.getSupportBeginDayofMonth(Year, Month).getTime() / 1000;
        long endTime = DataUtil.getSupportEndDayofMonth(Year, Month).getTime() / 1000;
        Log.e("time", starttime + "___" + endTime);
        // TODO: 2018/5/14 做好请求数据
        getmap.put("user_id", isApplication.user_id);
        getmap.put("starttime", starttime);
        getmap.put("endtime", endTime);
        getmap.put("size", 65535 + "");
        getmap.put("page", 1 + "");
        Log.e("startTime", starttime + "  " + endTime);
        NetUtil.request(Api.getRunRecord, new BaseObserver(getContext(), "正在获取跑步记录中...") {
            @Override
            protected void success(JSONObject value, String code, String message, Disposable disposable) throws IOException, JSONException {
                Log.e("print", value.toString());
                if (code.equals("true")) {
                    JSONObject jsonObject = new JSONObject(value.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Item_Message item = new Gson().fromJson(jsonArray.getString(i), Item_Message.class);
                        itemMessage.add(item);
                    }
                    if (itemMessage.size()>0) {
                        if (sportDataAdapter == null) {
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
                            lv.setLayoutManager(linearLayoutManager);
                            sportDataAdapter = new SportDataAdapter(getContext(), itemMessage, h);
//                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
//                        lv.setLayoutManager(linearLayoutManager);
//                        adapter = new CommonAdapter<Item_Message>(getContext(), R.layout.lv_run_item, itemMessage) {
//                            @Override
//                            protected void convert(ViewHolder holder, Item_Message s, int position) {
//
//                                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.getConvertView().getLayoutParams();
//                                params.width = w;
//                                params.height = h / 5;
//
//                                holder.getConvertView().setLayoutParams(params);
//                                TextView item_starttime = holder.getView(R.id.item_starttime);
//                                item_starttime.setText(getDateToString(Long.parseLong(s.getStarttime()) * 1000));
//                                TextView item_timeall = holder.getView(R.id.item_timeall);
//                                item_timeall.setText(TimeUtils.TimeFomat(Long.parseLong(s.getTotletime())));
//                                TextView item_distance = holder.getView(R.id.item_distance);
//                                item_distance.setText(s.getDistance() + "km");
//                                TextView itme_calorie = holder.getView(R.id.itme_calorie);
//                                itme_calorie.setText(s.getKaluli() + "cal");
//                                TextView item_datatime = holder.getView(R.id.item_datatime);
//                                String strtime1 = (String) item_starttime.getText();
//                                String strtime2 = strtime1.substring(11, 13);//截取字符串獲取小時
//                                Date date = new Date(Long.parseLong(s.getStarttime()) * 1000);//設置時間戳秒形式獲取星期
//                                if (Integer.parseInt(strtime2) >= 18) {//大于18代表大於六點，表示夜跑
//                                    item_datatime.setText(TimeUtils.getWeekOfDate(date) + "  " + getContext().getString(R.string.NightRun));
//                                } else {
//                                    item_datatime.setText(TimeUtils.getWeekOfDate(date) + "  " + getContext().getString(R.string.DayRun));
//                                }
//                            }
//                        };
                            if (itemMessage.size() == 0) {//数据为空时显示空数据布局
//                            lv.showEmpty();
                                lv.setVisibility(View.GONE);
                                lv_empty.setVisibility(View.VISIBLE);
                            }
                            lv.setAdapter(sportDataAdapter);
                        } else {
                            sportDataAdapter.notifyDataSetChanged();
                            if (itemMessage.size() == 0) {
//                            lv.showEmpty();
                                lv.setVisibility(View.GONE);
                                lv_empty.setVisibility(View.VISIBLE);
                            }
                        }
                    }else {
                        if (sportDataAdapter!=null){
                            sportDataAdapter.notifyDataSetChanged();
                        }
                    }
                } else {
//                    lv.showEmpty();
                    lv.setVisibility(View.GONE);
                    lv_empty.setVisibility(View.VISIBLE);
                }
            }
        }, getmap);
    }

    private void setSelector(String title, View v1, View v2, View v3, View v4, View v5, View v6) {
        user_title.setText(String.valueOf(title));
        v1.setVisibility(View.VISIBLE);
        v2.setVisibility(View.GONE);
        v3.setVisibility(View.GONE);
        v4.setBackgroundResource(R.color.colorBlack);
        v5.setBackgroundResource(R.color.colorBlack3);
        v6.setBackgroundResource(R.color.colorBlack3);
    }

    public static String getDateToString(long milSecond) {
        Date date = new Date(milSecond);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }
}
