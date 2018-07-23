package com.vigorchip.omatreadmill.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.vigorchip.omatreadmill.R;
import com.vigorchip.omatreadmill.activity.MainActivity;
import com.vigorchip.omatreadmill.utils.AnimateUtils;

/**
 * Created by wr-app1 on 2018/3/27.
 */

public class Fragment_Media extends Fragment implements View.OnClickListener {
    private RelativeLayout Music, Video, Media_More;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_media, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    private void init() {
        Music = view.findViewById(R.id.Music);
        Video = view.findViewById(R.id.Video);
        Media_More = view.findViewById(R.id.Media_More);
        Music.setOnClickListener(this);
        Video.setOnClickListener(this);
        Media_More.setOnClickListener(this);
        AnimateUtils.setClickAnimation(getContext(), Music);
        AnimateUtils.setClickAnimation(getContext(), Video);
        AnimateUtils.setClickAnimation(getContext(), Media_More);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Music:
                Log.e("查看包名", String.valueOf(getActivity().getPackageManager().getLaunchIntentForPackage("com.vigorchip.WrMusic.wr2") != null));
                if (getActivity().getPackageManager().getLaunchIntentForPackage("com.vigorchip.WrMusic.wr2") != null) {
                    startActivity(getActivity().getPackageManager().getLaunchIntentForPackage("com.vigorchip.WrMusic.wr2"));
                }
                break;
            case R.id.Video:
                Log.e("查看包名", String.valueOf(getActivity().getPackageManager().getLaunchIntentForPackage("com.vigorchip.WrVideo.wr2") != null));
                if (getActivity().getPackageManager().getLaunchIntentForPackage("com.vigorchip.WrVideo.wr2") != null) {
                    startActivity(getActivity().getPackageManager().getLaunchIntentForPackage("com.vigorchip.WrVideo.wr2"));
                }
                break;
            case R.id.Media_More:
                ((MainActivity) getActivity()).showFragment(6,27);
                break;
        }
    }
}
