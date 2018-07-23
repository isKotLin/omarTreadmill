package com.vigorchip.omatreadmill.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.vigorchip.omatreadmill.R;
import com.vigorchip.omatreadmill.activity.MainActivity;
import com.vigorchip.omatreadmill.bean.SportData;
import com.vigorchip.omatreadmill.utils.AnimateUtils;
import com.vigorchip.omatreadmill.utils.CreatDialog;

/**
 * Created by wr-app1 on 2018/3/28.
 */

public class Fragment_Mode_Train extends Fragment implements View.OnClickListener {
    private RelativeLayout btn_train_default, btn_train_user, btn_train_heart, btn_train_BMI;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mode_train, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    private void init() {
        btn_train_default = view.findViewById(R.id.btn_train_default);
        btn_train_user = view.findViewById(R.id.btn_train_user);
        btn_train_heart = view.findViewById(R.id.btn_train_heart);
        btn_train_BMI = view.findViewById(R.id.btn_train_BMI);
        AnimateUtils.setClickAnimation(getContext(), btn_train_default);
        AnimateUtils.setClickAnimation(getContext(), btn_train_user);
        AnimateUtils.setClickAnimation(getContext(), btn_train_heart);
        AnimateUtils.setClickAnimation(getContext(), btn_train_BMI);
        btn_train_default.setOnClickListener(this);
        btn_train_user.setOnClickListener(this);
        btn_train_heart.setOnClickListener(this);
        btn_train_BMI.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_train_default:
                if (SportData.isDefultTrainRunning()) {
                    ((MainActivity) getActivity()).showFragment(4, 17);
                } else if (SportData.isRunning() && !SportData.isDefultTrainRunning()) {
                    CreatDialog.showRunningDialog(getContext(), SportData.IndexSportMode(SportData.getStatus()));
                } else if (!SportData.isRunning()) {
                    ((MainActivity) getActivity()).showFragment(4, 15);
                }
                break;
            case R.id.btn_train_user:
                if (SportData.isRunning() &&
                        SportData.getStatus() == SportData.RUNNING_MODE_TRAINING_USER) {
                    ((MainActivity) getActivity()).showFragment(4, 17);
                } else if (SportData.isRunning() &&
                        SportData.getStatus() != SportData.RUNNING_MODE_TRAINING_USER) {
                    CreatDialog.showRunningDialog(getContext(), SportData.IndexSportMode(SportData.getStatus()));
                } else if (!SportData.isRunning()) {
                    ((MainActivity) getActivity()).showFragment(4, 18);
                }
                break;
            case R.id.btn_train_BMI:
                ((MainActivity) getActivity()).showFragment(4, 19);
                break;
            case R.id.btn_train_heart:
                if (SportData.isRunning() &&
                        SportData.getStatus() == SportData.RUNNING_MODE_TRAINING_HEART) {
                    ((MainActivity) getActivity()).showFragment(4, 22);
                } else if (SportData.isRunning() &&
                        SportData.getStatus() != SportData.RUNNING_MODE_TRAINING_HEART) {
                    CreatDialog.showRunningDialog(getContext(), SportData.IndexSportMode(SportData.getStatus()));
                } else if (!SportData.isRunning()) {
                    ((MainActivity) getActivity()).showFragment(4, 20);
                }
                break;
        }
    }
}
