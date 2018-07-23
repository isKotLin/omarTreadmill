package com.vigorchip.omatreadmill.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vigorchip.omatreadmill.R;
import com.vigorchip.omatreadmill.activity.MainActivity;
import com.vigorchip.omatreadmill.eventbus.setLiveVideoPath;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

/**
 * Created by wr-app1 on 2018/3/28.
 * 实景模式选择
 */

public class Fragment_Mode_Live extends Fragment implements View.OnClickListener {
    private RelativeLayout btn_live1, btn_live2, btn_live3, btn_live4, btn_live5, btn_live6;
    private ImageView img_live1, img_live2, img_live3, img_live4, img_live5, img_live6;
    private View view;
    private String path_live1, path_live2, path_live3, path_live4, path_live5, path_live6;

    private File file;//视频文件

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mode_live, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    private void init() {
        btn_live1 = view.findViewById(R.id.btn_live1);
        btn_live2 = view.findViewById(R.id.btn_live2);
        btn_live3 = view.findViewById(R.id.btn_live3);
        btn_live4 = view.findViewById(R.id.btn_live4);
        btn_live5 = view.findViewById(R.id.btn_live5);
        btn_live6 = view.findViewById(R.id.btn_live6);
        img_live1 = view.findViewById(R.id.img_live1);
        img_live2 = view.findViewById(R.id.img_live2);
        img_live3 = view.findViewById(R.id.img_live3);
        img_live4 = view.findViewById(R.id.img_live4);
        img_live5 = view.findViewById(R.id.img_live5);
        img_live6 = view.findViewById(R.id.img_live6);
        Glide.with(this).load(R.drawable.video_img1).into(img_live1);
        Glide.with(this).load(R.drawable.video_img2).into(img_live2);
        Glide.with(this).load(R.drawable.video_img3).into(img_live3);
        Glide.with(this).load(R.drawable.video_img4).into(img_live4);
        Glide.with(this).load(R.drawable.video_img5).into(img_live5);
        Glide.with(this).load(R.drawable.video_img6).into(img_live6);
        btn_live1.setOnClickListener(this);
        btn_live2.setOnClickListener(this);
        btn_live3.setOnClickListener(this);
        btn_live4.setOnClickListener(this);
        btn_live5.setOnClickListener(this);
        btn_live6.setOnClickListener(this);
        path_live1 = "/system/treadmill/video_01.mp4";
        path_live2 = "/system/treadmill/video_02.mp4";
        path_live3 = "/system/treadmill/video_03.mp4";
        path_live4 = "/system/treadmill/video_04.mp4";
        path_live5 = "/system/treadmill/video_05.mp4";
        path_live6 = "/system/treadmill/video_06.mp4";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_live1://发送第一个实景视频路径到实景跑步界面
                file = new File("/system/treadmill/video_01.mp4");
                if (file.exists()) {
                    EventBus.getDefault().postSticky(new setLiveVideoPath(path_live1));
                    ((MainActivity) getActivity()).showFragment(5, 21);
                } else {
                    Toast toast = Toast.makeText(getContext(), "No File", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 380);
                    toast.show();
                }
//                Intent intent = new Intent(getContext(), VideoActivity.class);
//                startActivity(intent);
                break;
            case R.id.btn_live2://发送第二个实景视频路径到实景跑步界面
                file = new File("/system/treadmill/video_02.mp4");
                if (file.exists()) {
                    EventBus.getDefault().postSticky(new setLiveVideoPath(path_live2));
                    ((MainActivity) getActivity()).showFragment(5, 21);
                } else {
                    Toast toast = Toast.makeText(getContext(), "No File", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 380);
                    toast.show();
                }
                break;
            case R.id.btn_live3://发送第三个实景视频路径到实景跑步界面
                file = new File("/system/treadmill/video_03.mp4");
                if (file.exists()) {
                    EventBus.getDefault().postSticky(new setLiveVideoPath(path_live3));
                    ((MainActivity) getActivity()).showFragment(5, 21);
                } else {
                    Toast toast = Toast.makeText(getContext(), "No File", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 380);
                    toast.show();
                }
                break;
            case R.id.btn_live4://发送第四个实景视频路径到实景跑步界面
                file = new File("/system/treadmill/video_04.mp4");
                if (file.exists()) {
                    EventBus.getDefault().postSticky(new setLiveVideoPath(path_live4));
                    ((MainActivity) getActivity()).showFragment(5, 21);
                } else {
                    Toast toast = Toast.makeText(getContext(), "No File", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 380);
                    toast.show();
                }
                break;
            case R.id.btn_live5://发送第五个实景视频路径到实景跑步界面
                file = new File("/system/treadmill/video_04.mp4");
                if (file.exists()) {
                    EventBus.getDefault().postSticky(new setLiveVideoPath(path_live5));
                    ((MainActivity) getActivity()).showFragment(5, 21);
                } else {
                    Toast toast = Toast.makeText(getContext(), "No File", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 380);
                    toast.show();
                }
                break;
            case R.id.btn_live6://发送第六个实景视频路径到实景跑步界面
                file = new File("/system/treadmill/video_04.mp4");
                if (file.exists()) {
                    EventBus.getDefault().postSticky(new setLiveVideoPath(path_live6));
                    ((MainActivity) getActivity()).showFragment(5, 21);
                } else {
                    Toast toast = Toast.makeText(getContext(), "No File", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 380);
                    toast.show();
                }
                break;
        }
    }
}
