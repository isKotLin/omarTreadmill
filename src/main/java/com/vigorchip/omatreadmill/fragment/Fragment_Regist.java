package com.vigorchip.omatreadmill.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vigorchip.omatreadmill.R;
import com.vigorchip.omatreadmill.activity.MainActivity;
import com.vigorchip.omatreadmill.application.isApplication;
import com.vigorchip.omatreadmill.base.BaseObserver;
import com.vigorchip.omatreadmill.bean.Data_Info;
import com.vigorchip.omatreadmill.bean.NetUtil;
import com.vigorchip.omatreadmill.interfaci.Api;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import io.reactivex.disposables.Disposable;
import io.vov.vitamio.utils.Log;

/**
 * Created by wr-app1 on 2018/4/16.
 */

public class Fragment_Regist extends Fragment implements View.OnClickListener{
    private View view;
    private EditText et_registName, et_registPassword, et_affirm;
    private Button btn_regist, btn_backTologin;
    private Map<String,Object> map;
    public static final String REGEX_USERNAME = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_regist, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
    }

    private void initView() {
        et_registName = view.findViewById(R.id.et_registName);
        et_registPassword = view.findViewById(R.id.et_registPassword);
        et_affirm = view.findViewById(R.id.et_affirm);
        btn_regist = view.findViewById(R.id.btn_regist);
        btn_backTologin = view.findViewById(R.id.btn_backTologin);
        btn_regist.setOnClickListener(this);
        btn_backTologin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_regist:
                if (map==null) {
                    map = new HashMap<>();
                }
                map.clear();
                String phone = et_registName.getText().toString().trim();
                if (Pattern.matches(REGEX_USERNAME,phone)) {
                    String pasword = et_registPassword.getText().toString().trim();
                    String pasword01 = et_affirm.getText().toString().trim();

                    if (pasword.equals(pasword01)) {
                        map.put("phone", phone);
                        map.put("password", pasword);
                        NetUtil.request(Api.register, new BaseObserver(getContext(), "正在注册中...") {
                            @Override
                            protected void success(JSONObject value, String code, String message, Disposable disposable) throws IOException, JSONException {
                                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                if (code.equals("true")) {
                                    builder.setTitle(getContext().getString(R.string.Hint));
                                    builder.setMessage(message);
                                    builder.setCancelable(false);
                                    builder.setNegativeButton(getContext().getString(R.string.OK1), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Log.e("print", which + "");
                                            ((MainActivity) getActivity()).showFragment(25, 8);
                                        }
                                    });
                                    builder.create().show();
                                    //鐢ㄦ埛淇℃伅
                                    String data = value.getString("data");
                                    Data_Info data_info = new Gson().fromJson(data, Data_Info.class);
                                    isApplication.user_id = data_info.getUser_id();
                                } else {
                                    builder.setTitle(getContext().getString(R.string.Hint));
                                    builder.setMessage(message);
                                    builder.setNegativeButton(getContext().getString(R.string.OK1), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                                    builder.create().show();
                                }
                            }
                        }, map);
                    }else {
                        Toast.makeText(getContext(),"兩次密碼輸入有無請重新輸入",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getContext(),"請輸入正確的郵箱",Toast.LENGTH_SHORT).show();
                }
                Log.e("print","success");

                break;
            case R.id.btn_backTologin:
                ((MainActivity) getActivity()).showFragment(25, 23);
                break;
        }
    }
}
