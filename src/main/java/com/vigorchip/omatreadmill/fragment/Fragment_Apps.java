package com.vigorchip.omatreadmill.fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
 * Created by wr-app1 on 2018/3/31.
 */

public class Fragment_Apps extends Fragment implements AppAdapter.OnItemListenerOnClick, AppAdapter.OnItemListenerOnLongTouch {
    private View view;
    private GridView apps_gv;
    List<ResolveInfo> mApps;
    PackageManager packageManager;
    AppAdapter appAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_apps, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        loadApps();
        apps_gv = view.findViewById(R.id.apps_gv);
        Iterator it = mApps.iterator();
        while (it.hasNext()) {
            ResolveInfo obj = (ResolveInfo) it.next();
            String able = getContext().getResources().getConfiguration().locale.getLanguage();
            language_apps(obj,it,able);
            continue;
        }
        appAdapter = new AppAdapter(getContext(), mApps);
        apps_gv.setAdapter(appAdapter);
        appAdapter.setOnItemListenerOnClick(this);
        appAdapter.setOnItemListenerOnLongTouch(this);
    }

    @Override
    public void itemClick(int position) {
        try {
            Intent intent = packageManager.getLaunchIntentForPackage(mApps.get(position).activityInfo.packageName);
            startActivity(intent);
        } catch (Exception e){
            Log.e("跳转失败","");
        }
    }

    private void loadApps() {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        packageManager = getActivity().getPackageManager();
        mApps = packageManager.queryIntentActivities(intent, 0);
    }

    private Handler longHandler;
    private String mPackageName;
    Runnable Run_LongTouch = new Runnable() {
        @Override
        public void run() {
            if (DeleteApp(mPackageName)) {
                Intent intent = new Intent(Intent.ACTION_DELETE,Uri.fromParts("package",mPackageName, null));
                startActivity(intent);
                //TODO 刷新
            }
        }
    };

    @Override
    public void itemLongTouch(int position) {
        mPackageName = mApps.get(position).activityInfo.packageName;
        longHandler = new Handler();
        longHandler.postDelayed(Run_LongTouch, 500);
    }

    private boolean DeleteApp(String PackageName) {
        if (PackageName.equals("com.google.android.youtube") ||
                PackageName.equals("deezer.android.app") ||
                PackageName.equals("com.facebook.katana") ||
                PackageName.equals("com.twitter.android") ||
                PackageName.equals("com.king.candycrushsaga") ||
                PackageName.equals("com.rovio.angrybirds") ||
                PackageName.equals("com.baidu.music.pad") ||
                PackageName.equals("com.qiyi.video.pad")) {
            return false;
        } else {
            return true;
        }
    }

    private void language_apps(ResolveInfo obj, Iterator it, String language) {//语言不同显示不同的app
        switch (language) {
            case "zh":
                if (//!obj.activityInfo.packageName.equals("")||!obj.activityInfo.packageName.equals("")
                        obj.activityInfo.packageName.equals("com.vigorchip.omatreadmill") ||
                                obj.activityInfo.packageName.equals("com.android.settings") ||
                                obj.activityInfo.packageName.equals("com.google.android.inputmethod.pinyin") ||
                                obj.activityInfo.packageName.equals("com.qt.cleanmaster") ||
                                obj.activityInfo.packageName.equals("com.vigorchip.WrMusic.wr2") ||
                                obj.activityInfo.packageName.equals("com.softwinner.update") ||
                                obj.activityInfo.packageName.equals("com.vigorchip.autocopy") ||
                                obj.activityInfo.packageName.equals("com.vigorchip.WrVideo.wr2") ||
                                obj.activityInfo.packageName.equals("com.android.contacts") ||
                                obj.activityInfo.packageName.equals("com.android.providers.calendar") ||
                                obj.activityInfo.packageName.equals("com.android.calculator2") ||
                                obj.activityInfo.packageName.equals("com.android.gallery3d") ||
                                obj.activityInfo.packageName.equals("com.android.chrome") ||
                                obj.activityInfo.packageName.equals("com.android.documentsui") ||
                                obj.activityInfo.packageName.equals("com.android.inputmethod.latin") ||
                                obj.activityInfo.packageName.equals("com.android.quicksearchbox") ||
                                obj.activityInfo.packageName.equals("com.android.soundrecorder") ||
                                obj.activityInfo.packageName.equals("com.estrongs.android.pop") ||
                                obj.activityInfo.packageName.equals("com.android.camera2") ||
                                obj.activityInfo.packageName.equals("com.android.calendar") ||
                                obj.activityInfo.packageName.equals("com.android.browser") ||
                                obj.activityInfo.packageName.equals("com.android.providers.downloads.ui") ||
                                obj.activityInfo.packageName.equals("com.android.deskclock") ||
                                obj.activityInfo.packageName.equals("com.android.email") ||
                                obj.activityInfo.packageName.equals("com.softwinner.fireplayer") ||
                                obj.activityInfo.packageName.equals("com.android.music") ||
                                obj.activityInfo.packageName.equals("com.android.development") ||
                                obj.activityInfo.packageName.equals("com.google.android.youtube") ||
                                obj.activityInfo.packageName.equals("deezer.android.app") ||
                                obj.activityInfo.packageName.equals("com.facebook.katana") ||
                                obj.activityInfo.packageName.equals("com.twitter.android") ||
                                obj.activityInfo.packageName.equals("com.king.candycrushsaga") ||
                                obj.activityInfo.packageName.equals("com.rovio.angrybirds")
                        ) {
                    it.remove();
                }
                break;
            case "en":
                if (obj.activityInfo.packageName.equals("com.vigorchip.omatreadmill") ||
                                obj.activityInfo.packageName.equals("com.android.settings") ||
                                obj.activityInfo.packageName.equals("com.google.android.inputmethod.pinyin") ||
                                obj.activityInfo.packageName.equals("com.qt.cleanmaster") ||
                                obj.activityInfo.packageName.equals("com.vigorchip.WrMusic.wr2") ||
                                obj.activityInfo.packageName.equals("com.softwinner.update") ||
                                obj.activityInfo.packageName.equals("com.vigorchip.autocopy") ||
                                obj.activityInfo.packageName.equals("com.vigorchip.WrVideo.wr2") ||
                                obj.activityInfo.packageName.equals("com.android.contacts") ||
                                obj.activityInfo.packageName.equals("com.android.providers.calendar") ||
                                obj.activityInfo.packageName.equals("com.android.calculator2") ||
                                obj.activityInfo.packageName.equals("com.android.gallery3d") ||
                                obj.activityInfo.packageName.equals("com.android.chrome") ||
                                obj.activityInfo.packageName.equals("com.android.documentsui") ||
                                obj.activityInfo.packageName.equals("com.android.inputmethod.latin") ||
                                obj.activityInfo.packageName.equals("com.android.quicksearchbox") ||
                                obj.activityInfo.packageName.equals("com.android.soundrecorder") ||
                                obj.activityInfo.packageName.equals("com.estrongs.android.pop") ||
                                obj.activityInfo.packageName.equals("com.android.camera2") ||
                                obj.activityInfo.packageName.equals("com.android.calendar") ||
                                obj.activityInfo.packageName.equals("com.android.browser") ||
                                obj.activityInfo.packageName.equals("com.android.providers.downloads.ui") ||
                                obj.activityInfo.packageName.equals("com.android.deskclock") ||
                                obj.activityInfo.packageName.equals("com.android.email") ||
                                obj.activityInfo.packageName.equals("com.softwinner.fireplayer") ||
                                obj.activityInfo.packageName.equals("com.android.music") ||
                                obj.activityInfo.packageName.equals("com.android.development") ||
                                obj.activityInfo.packageName.equals("com.baidu.music.pad") ||
                                obj.activityInfo.packageName.equals("com.qiyi.video.pad")
                        ) {
                    it.remove();
                }
                break;
            case "fr":
                if (obj.activityInfo.packageName.equals("com.vigorchip.omatreadmill") ||
                                obj.activityInfo.packageName.equals("com.android.settings") ||
                                obj.activityInfo.packageName.equals("com.google.android.inputmethod.pinyin") ||
                                obj.activityInfo.packageName.equals("com.qt.cleanmaster") ||
                                obj.activityInfo.packageName.equals("com.vigorchip.WrMusic.wr2") ||
                                obj.activityInfo.packageName.equals("com.softwinner.update") ||
                                obj.activityInfo.packageName.equals("com.vigorchip.autocopy") ||
                                obj.activityInfo.packageName.equals("com.vigorchip.WrVideo.wr2") ||
                                obj.activityInfo.packageName.equals("com.android.contacts") ||
                                obj.activityInfo.packageName.equals("com.android.providers.calendar") ||
                                obj.activityInfo.packageName.equals("com.android.calculator2") ||
                                obj.activityInfo.packageName.equals("com.android.gallery3d") ||
                                obj.activityInfo.packageName.equals("com.android.chrome") ||
                                obj.activityInfo.packageName.equals("com.android.documentsui") ||
                                obj.activityInfo.packageName.equals("com.android.inputmethod.latin") ||
                                obj.activityInfo.packageName.equals("com.android.quicksearchbox") ||
                                obj.activityInfo.packageName.equals("com.android.soundrecorder") ||
                                obj.activityInfo.packageName.equals("com.estrongs.android.pop") ||
                                obj.activityInfo.packageName.equals("com.android.camera2") ||
                                obj.activityInfo.packageName.equals("com.android.calendar") ||
                                obj.activityInfo.packageName.equals("com.android.browser") ||
                                obj.activityInfo.packageName.equals("com.android.providers.downloads.ui") ||
                                obj.activityInfo.packageName.equals("com.android.deskclock") ||
                                obj.activityInfo.packageName.equals("com.android.email") ||
                                obj.activityInfo.packageName.equals("com.softwinner.fireplayer") ||
                                obj.activityInfo.packageName.equals("com.android.music") ||
                                obj.activityInfo.packageName.equals("com.android.development") ||
                                obj.activityInfo.packageName.equals("com.baidu.music.pad") ||
                                obj.activityInfo.packageName.equals("com.qiyi.video.pad")
                        ) {
                    it.remove();
                }
                break;
        }
    }


}
