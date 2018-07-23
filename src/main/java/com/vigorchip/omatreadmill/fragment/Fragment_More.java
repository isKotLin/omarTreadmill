package com.vigorchip.omatreadmill.fragment;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vigorchip.omatreadmill.R;
import com.vigorchip.omatreadmill.activity.MainActivity;
import com.vigorchip.omatreadmill.application.isApplication;
import com.vigorchip.omatreadmill.utils.AnimateUtils;
import com.vigorchip.omatreadmill.utils.CacheDataManager;
import com.vigorchip.omatreadmill.utils.CreatDialog;
import com.vigorchip.omatreadmill.utils.Okhttp;
import com.vigorchip.omatreadmill.utils.UpdataSystemUtil;
import com.vigorchip.omatreadmill.utils.UrlConstant;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

import static android.widget.Toast.makeText;

/**
 * Created by wr-app1 on 2018/3/28.
 */

public class Fragment_More extends Fragment implements View.OnClickListener {
    private RelativeLayout MoreID_Apps, MoreID_Product, MoreID_Contact, MoreID_Calculator,
            MoreID_SystemInfo, MoreID_Update, MoreID_FileManager, MoreID_Clean, MoreID_Factory;
    private ImageView icon_Factory;
    private View view;
    public String downSystemUrl;
    String version_system_content;//系统更新内容

    class clearCache implements Runnable {//清理緩存的綫程

        @Override
        public void run() {
            try {
                Log.e("adesadadad", "0000");
                CacheDataManager.clearAllCache(getContext());
                Thread.sleep(3000);
                if (CacheDataManager.getTotalCacheSize(getContext()).startsWith("0")) {
                    handler.sendEmptyMessage(0);
                }
            } catch (Exception e) {
                return;
            }
        }
    }

    private Handler handler = new Handler() {//清理緩存的完成的綫程
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    CreatDialog.dialog.dismiss();
                    try {
                        Log.e("查看緩存", CacheDataManager.getTotalCacheSize(getContext()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast toast = Toast.makeText(getContext(), getContext().getString(R.string.CleanedSuccessfully), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 380);
                    toast.show();
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_more, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    private void init() {
        MoreID_Apps = view.findViewById(R.id.MoreID_Apps);
        MoreID_Product = view.findViewById(R.id.MoreID_Product);
        MoreID_Contact = view.findViewById(R.id.MoreID_Contact);
        MoreID_Calculator = view.findViewById(R.id.MoreID_Calculator);
        MoreID_SystemInfo = view.findViewById(R.id.MoreID_SystemInfo);
        MoreID_Update = view.findViewById(R.id.MoreID_Update);
        MoreID_FileManager = view.findViewById(R.id.MoreID_FileManager);
        MoreID_Clean = view.findViewById(R.id.MoreID_Clean);
        MoreID_Factory = view.findViewById(R.id.MoreID_Factory);
        icon_Factory = view.findViewById(R.id.icon_Factory);
        MoreID_Apps.setOnClickListener(this);
        MoreID_Product.setOnClickListener(this);
        MoreID_Contact.setOnClickListener(this);
        MoreID_Calculator.setOnClickListener(this);
        MoreID_SystemInfo.setOnClickListener(this);
        MoreID_Update.setOnClickListener(this);
        MoreID_FileManager.setOnClickListener(this);
        MoreID_Clean.setOnClickListener(this);
        MoreID_Factory.setOnClickListener(this);
        AnimateUtils.setClickAnimation(getContext(), MoreID_Apps);
        AnimateUtils.setClickAnimation(getContext(), MoreID_Product);
        AnimateUtils.setClickAnimation(getContext(), MoreID_Contact);
        AnimateUtils.setClickAnimation(getContext(), MoreID_Calculator);
        AnimateUtils.setClickAnimation(getContext(), MoreID_SystemInfo);
        AnimateUtils.setClickAnimation(getContext(), MoreID_Update);
        AnimateUtils.setClickAnimation(getContext(), MoreID_FileManager);
        AnimateUtils.setClickAnimation(getContext(), MoreID_Clean);
        AnimateUtils.setClickAnimation(getContext(), MoreID_Factory);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.MoreID_Apps:
                ((MainActivity) getActivity()).showFragment(9, 12);
                break;
            case R.id.MoreID_Product:
                if (getActivity().getPackageManager().getLaunchIntentForPackage("com.android.chrome") != null) {
                    Intent intent1 = new Intent();
                    intent1.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse("https://mall.jd.com/index-37085.html");
                    intent1.setData(content_url);
                    intent1.setClassName("com.android.chrome", "com.google.android.apps.chrome.Main");
                    startActivity(intent1);
                }
//                doStartApplicationWithPackageName("com.android.chrome");
                break;
            case R.id.MoreID_Contact:

                break;
            case R.id.MoreID_Calculator:
                if (getActivity().getPackageManager().getLaunchIntentForPackage("com.android.calculator2") != null)
                    startActivity(getActivity().getPackageManager().getLaunchIntentForPackage("com.android.calculator2"));
                break;
            case R.id.MoreID_SystemInfo:
                ((MainActivity) getActivity()).showFragment(9, 11);
                break;
            case R.id.MoreID_Update:
                getSystemString();
                break;
            case R.id.MoreID_FileManager:
                CreatDialog.showDialog(getContext(), R.layout.dialog_file, false);
                final EditText et_filePsw = CreatDialog.dialog.findViewById(R.id.et_filePsw);
                TextView btn_commit_file = CreatDialog.dialog.findViewById(R.id.btn_commit_file);
                TextView btn_cancel_file = CreatDialog.dialog.findViewById(R.id.btn_cancel_file);
                btn_commit_file.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (et_filePsw.getText().toString().trim().equals("!Net4SAV%")) {
                            if (getActivity().getPackageManager().getLaunchIntentForPackage("com.estrongs.android.pop") != null) {
                                startActivity(getActivity().getPackageManager().getLaunchIntentForPackage("com.estrongs.android.pop"));
                            }
                            CreatDialog.dialog.dismiss();
                        }
                    }
                });
                btn_cancel_file.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CreatDialog.dialog.dismiss();
                    }
                });
                break;
            case R.id.MoreID_Clean:
                CreatDialog.showDialog(getContext(), R.layout.dialog_clear, false);
                try {
                    Log.e("查看緩存", CacheDataManager.getTotalCacheSize(getContext()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new Thread(new clearCache()).start();
                break;
            case R.id.MoreID_Factory://TODO 判断工厂密码
                final File file = new File("/data/canInstallApp");
                Log.e("查看當前是否工廠模式", String.valueOf(isApplication.FACTORY_LOCK) + file.exists());
                if (!isApplication.FACTORY_LOCK) {
                    CreatDialog.showDialog(getContext(), R.layout.dialog_factory, false);
                    final EditText et_factoryPsw = CreatDialog.dialog.findViewById(R.id.et_factoryPsw);
                    TextView btn_commit_factory = CreatDialog.dialog.findViewById(R.id.btn_commit_factory);
                    TextView btn_cancel_factory = CreatDialog.dialog.findViewById(R.id.btn_cancel_factory);
                    btn_commit_factory.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (et_factoryPsw.getText().toString().trim().equals("sw20170822")) {
                                isApplication.FACTORY_LOCK = true;
                                icon_Factory.setImageResource(R.drawable.unlock);
                                if (!file.exists()) {
                                    try {
                                        file.createNewFile();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                Toast toast = Toast.makeText(getContext(), "密码输入正确", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 380);
                                toast.show();
                                CreatDialog.dialog.dismiss();
                            }
                        }
                    });
                    btn_cancel_factory.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            CreatDialog.dialog.dismiss();
                        }
                    });
                } else {
                    icon_Factory.setImageResource(R.drawable.lock);
                    isApplication.FACTORY_LOCK = false;
                    if (file.exists()) {
                        file.delete();
                    }
                    Toast toast = Toast.makeText(getContext(), "关闭工厂模式", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 380);
                    toast.show();
                }
                break;
        }
    }

    private void doStartApplicationWithPackageName(String packagename) {//获取app主页面类名
        PackageInfo packageinfo = null;
        try {
            packageinfo = getContext().getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageinfo == null) {
            return;
        }


// 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);


// 通过getPackageManager()的queryIntentActivities方法遍历
        List<ResolveInfo> resolveinfoList = getContext().getPackageManager().queryIntentActivities(resolveIntent, 0);


        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
// packagename = 参数packname
            String packageName = resolveinfo.activityInfo.packageName;
// 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
            String className = resolveinfo.activityInfo.name;
            Log.i("Abel_Test", "类名是：" + className);
// LAUNCHER Intent
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
// 设置ComponentName参数1:packagename参数2:MainActivity路径
            ComponentName cn = new ComponentName(packageName, className);

            intent.setComponent(cn);
            startActivity(intent);
        }
    }

    /**
     * 提示版本更新的对话框
     */
    private void showDialogUpdate() {
        //Toast.makeText(MainActivity.this, "选择确定哦", 0).show();
        if (isNetworkAvailable(getContext())) {
            final ProgressDialog finalPdDialog = new ProgressDialog(getContext());
            finalPdDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            finalPdDialog.setCancelable(false);
            finalPdDialog.setButton(DialogInterface.BUTTON_NEGATIVE, String.valueOf(getContext().getString(R.string.Cancel)),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub

                        }
                    });
            finalPdDialog.show();
            Okhttp.downloadFile(downSystemUrl, "update.zip", new Okhttp.FileCallBac() {
                @Override
                public void onNoNetwork(String state) {

                }

                @Override
                public void onError(Call call, Exception e, int id) {

                }

                @Override
                public void onResponse(final File response, int id) {
                    UpdataSystemUtil.updataSystem(getContext(), response);
                }

                @Override
                public void inProgress(float progress, long total, int id) {
                    finalPdDialog.setProgress((int) (progress * 100));
                    com.zj.puliblib.utils.Logutil.e((int) (progress * 100) + "");
                }
            });
        } else {
            makeText(getContext(), "NO NETWORKS FOUND", Toast.LENGTH_SHORT).show();
        }
    }

    //判断设备有没有开启网络
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }

    public void getSystemString() {
        HashMap<String, String> map = new HashMap<>();
        try {
            map.put("type", "KVT48L-101-001");
            map.put("version", getContext().getPackageManager().
                    getPackageInfo(getContext().getPackageName(), 0).versionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Okhttp.get(false, UrlConstant.IS_NEED_SYSTEM_UPDATA_URL, map, new Okhttp.CallBac() {
            @Override
            public void onError(Call call, Exception e, String state, int id) {
//                isB = true;
            }

            @Override
            public void onResponse(String response, int id) {
//                isB = true;
                parseSystemJSON(response);
            }

            @Override
            public void onNoNetwork(String state) {
                Log.e("state:" + state, " 没有连接网络");
            }
        });
    }

    private void parseSystemJSON(String response) {
        try {
            JSONObject respose_json = new JSONObject(response);
            if (respose_json.optBoolean("success") && !respose_json.optString("data").equals("null")) {
                JSONObject data_json = respose_json.optJSONObject("data");
                downSystemUrl = data_json.optString("version_down_url");
                version_system_content = data_json.optString("version_content");
                Log.e("查看downSystemUrl", downSystemUrl);
                if (!downSystemUrl.equals("")) {
                    showDialogUpdate();
                } else {
                    Toast toast = Toast.makeText(getContext(), getContext().getString(R.string.LatestVersion), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 380);
                    toast.show();
                }
            } else {
//                Logutil.e(respose_json.optString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
