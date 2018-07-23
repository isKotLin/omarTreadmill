package com.vigorchip.omatreadmill.fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.vigorchip.omatreadmill.R;
import com.vigorchip.omatreadmill.adapter.AppAdapter;

import java.util.Iterator;
import java.util.List;

/**
 * Created by wr-app1 on 2018/6/9.
 */

public class Fragment_MediaApps extends Fragment implements AppAdapter.OnItemListenerOnClick {
    private View view;
    private GridView MediaApps;
    List<ResolveInfo> mApps;
    PackageManager packageManager;
    AppAdapter appAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_media_app, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    private void init() {
        MediaApps = view.findViewById(R.id.MediaApps);
        loadApps();
        Iterator it = mApps.iterator();
        while (it.hasNext()) {
            ResolveInfo obj = (ResolveInfo) it.next();
            //TODO 添加包名
            if (Language_appSelector(obj)) {
                    it.remove();
            }
            continue;
        }
        appAdapter = new AppAdapter(getContext(), mApps);
        MediaApps.setAdapter(appAdapter);
        appAdapter.setOnItemListenerOnClick(this);
    }

    private void loadApps() {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        packageManager = getActivity().getPackageManager();
        mApps = packageManager.queryIntentActivities(intent, 0);
    }

    @Override
    public void itemClick(int position) {
        try {
            Intent intent = packageManager.getLaunchIntentForPackage(mApps.get(position).activityInfo.packageName);
            startActivity(intent);
        } catch (Exception e) {
            Log.e("跳转失败","");
        }
    }

    private boolean Language_appSelector(ResolveInfo obj) {//不同语言出现不同的音视频软件
        String able = getContext().getResources().getConfiguration().locale.getLanguage();
        if (able.equals("zh")) {
            return Chinese_apps(obj);
        } else if (able.equals("en")) {
            English_apps(obj);
            return English_apps(obj);
        } else if (able.equals("fr")) {
            France_apps(obj);
            return France_apps(obj);
        }
        return false;
    }

    private boolean Chinese_apps(ResolveInfo obj) {//中文时获取中文app
        if (!obj.activityInfo.packageName.equals("com.baidu.music.pad")) {
            if (!obj.activityInfo.packageName.equals("com.qiyi.video.pad")) {
                return true;
            }
        }
        return false;
    }

    private boolean France_apps(ResolveInfo obj) {//法文时获取法文app
        if (!obj.activityInfo.packageName.equals("com.google.android.youtube")) {
            if (!obj.activityInfo.packageName.equals("deezer.android.app")) {
                return true;
            }
        }
        return false;
    }

    private boolean English_apps(ResolveInfo obj) {//英文时获取英文app
        if (!obj.activityInfo.packageName.equals("com.google.android.youtube")) {
            if (!obj.activityInfo.packageName.equals("deezer.android.app")) {
                return true;
            }
        }
        return false;
    }
}
