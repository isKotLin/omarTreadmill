package com.vigorchip.omatreadmill.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
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
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import io.reactivex.disposables.Disposable;
import io.vov.vitamio.utils.Log;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by wr-app1 on 2018/4/8.
 */

public class Fragment_Login extends Fragment implements View.OnClickListener {
    private View view;
    private EditText et_loginName, et_loginPassword;
    private TextView tv_register, tv_forget;
    private Button btn_login;
    String userName, userPassword;
    private Map<String, Object> map;
    public static final String REGEX_USERNAME = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$";
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private ImageView facebook;
    private CallbackManager callbackManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();

    }

    private void initView() {
        sp = getContext().getSharedPreferences("sp_account", Context.MODE_PRIVATE);
        editor = sp.edit();
        et_loginName = view.findViewById(R.id.et_loginName);
        et_loginPassword = view.findViewById(R.id.et_loginPassword);
        tv_register = view.findViewById(R.id.tv_register);
        tv_forget = view.findViewById(R.id.tv_forget);
        btn_login = view.findViewById(R.id.btn_login);
        facebook = view.findViewById(R.id.facebook);
        tv_register.setOnClickListener(this);
        tv_forget.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        facebook.setOnClickListener(this);
        callbackManager = CallbackManager.Factory.create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_register:
                ((MainActivity) getActivity()).showFragment(23, 25);
                break;
            case R.id.tv_forget:
                ((MainActivity) getActivity()).showFragment(23, 26);
                break;
            case R.id.btn_login:
                if (map == null) {
                    map = new HashMap<>();
                }
                userName = et_loginName.getText().toString().trim();
                userPassword = et_loginPassword.getText().toString().trim();
                if (Pattern.matches(REGEX_USERNAME, userName)) {
                    map.put("phone", userName);
                    map.put("password", userPassword);
                    NetUtil.request(Api.login, new BaseObserver(getContext(), "正在登陆中...") {
                        @Override
                        protected void success(JSONObject value, String code, String message, Disposable disposable) throws IOException, JSONException {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            if (code.equals("true")) {
                                //用户id
                                String data = value.getString("data");
                                Data_Info data_info = new Gson().fromJson(data, Data_Info.class);
                                isApplication.user_id = data_info.getUser_id();
                                builder.setTitle(getContext().getString(R.string.Hint));
                                builder.setMessage(getContext().getString(R.string.signSuccess));
                                builder.setCancelable(false);
                                builder.setNegativeButton(getContext().getString(R.string.OK2), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Log.e("print", which + "");
                                        editor.putString("user_account", String.valueOf(userName));
                                        editor.putString("user_password", String.valueOf(userPassword));
                                        editor.commit();
                                        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
                                        ((MainActivity) getActivity()).showFragment(23, 8);
                                    }
                                });
                                builder.create().show();
                            } else {
                                builder.setTitle(getContext().getString(R.string.Hint));
                                builder.setMessage(getContext().getString(R.string.signFailed));
                                builder.setNegativeButton(getContext().getString(R.string.OK2), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                builder.create().show();
                            }
                        }
                    }, map);
                } else {
                    Toast.makeText(getContext(), getContext().getString(R.string.signFailed), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.facebook:
                if (getActivity().getPackageManager().getLaunchIntentForPackage("com.facebook.katana") != null) {
                    Collection<String> permissions = Arrays.asList("public_profile", "email");
                    LoginManager.getInstance().logInWithReadPermissions(this, permissions);
                    LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            AccessToken accessToken = loginResult.getAccessToken();
                            String userId = accessToken.getUserId();
                            String token = accessToken.getToken();
                            Log.e("facebook查看userid", userId + "  " + token);
                            if (!userId.equals("")) {
                                ((MainActivity) getActivity()).showFragment(23, 8);
                            }
                        }

                        @Override
                        public void onCancel() {
                            Toast.makeText(getApplicationContext(), "facebook Login Cancel", Toast.LENGTH_SHORT).show();
                            Log.e("facebook查看onCancel", "facebook登录取消");
                        }

                        @Override
                        public void onError(FacebookException error) {
                            Toast.makeText(getContext(), "Facebook Login Errors"+error.toString(), Toast.LENGTH_SHORT).show();
                            Log.e("facebook查看onError",error.toString());
                        }
                    });
                } else {
                    Toast toast = Toast.makeText(getContext(), "No Facebook", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 380);
                    toast.show();
                }
                break;
        }
    }
}
