package com.vigorchip.omatreadmill.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vigorchip.omatreadmill.R;
import com.vigorchip.omatreadmill.activity.MainActivity;
import com.vigorchip.omatreadmill.base.BaseObserver;
import com.vigorchip.omatreadmill.bean.NetUtil;
import com.vigorchip.omatreadmill.interfaci.Api;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import io.reactivex.disposables.Disposable;

/**
 * Created by wr-app1 on 2018/4/16.
 */

public class Fragment_Forget extends Fragment implements View.OnClickListener{
    private View view;
    private EditText et_forgetName;
    private Button btn_forgetPassword, btn_backTologin;
    private Map<String,Object> map;
    public static final String REGEX_USERNAME = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_forget, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    private void init() {
        et_forgetName = view.findViewById(R.id.et_forgetName);
        btn_forgetPassword = view.findViewById(R.id.btn_forgetPassword);
        btn_backTologin = view.findViewById(R.id.btn_backTologin);
        btn_forgetPassword.setOnClickListener(this);
        btn_backTologin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_forgetPassword:
                if (map==null){
                    map = new HashMap<>();
                }
                map.clear();
                String email = et_forgetName.getText().toString().trim();
                boolean is = Pattern.matches(REGEX_USERNAME,email);
                if (is) {
                    map.put("email", email);
                    NetUtil.request(Api.findbackpswd, new BaseObserver(getContext(), "正在重置密碼...") {
                        @Override
                        protected void success(JSONObject value, String code, String message, Disposable disposable) throws IOException, JSONException {
                            Log.e("mima", value.toString());
                            if (code.equals("true")) {
                                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, map);
                } else {
                    Toast.makeText(getContext(),"請輸入正確的郵箱",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_backTologin:
                ((MainActivity) getActivity()).showFragment(26, 23);
                break;
        }
    }
}
