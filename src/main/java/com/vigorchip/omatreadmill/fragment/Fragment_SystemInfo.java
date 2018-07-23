package com.vigorchip.omatreadmill.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vigorchip.omatreadmill.R;
import com.vigorchip.omatreadmill.bean.SportData;
import com.vigorchip.omatreadmill.bean.System_Info;
import com.vigorchip.omatreadmill.serialport.newSerial;

/**
 * Created by wr-app1 on 2018/3/27.
 */

public class Fragment_SystemInfo extends Fragment {
    private TextView tv_app_versionName, tv_app_versionCode, tv_deliveryTime, tv_driver_versionName, tv_driver_versionCode,
            tv_hardWare_batch, tv_maxSpeed, tv_maxSlopes, tv_steps, tv_ports, tv_buzzer, tv_resolution, tv_dpi, tv_systemVersionName;
    private System_Info system_info;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_systeminfo, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
        setSystemText();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            tv_maxSpeed.setText(String.valueOf(SportData.MAXSPEED));
            tv_maxSlopes.setText(String.valueOf(SportData.MAXSLOPES));
        }
    }

    private void init() {
        system_info = new System_Info();
        tv_app_versionName = view.findViewById(R.id.tv_app_versionName);
        tv_app_versionCode = view.findViewById(R.id.tv_app_versionCode);
        tv_deliveryTime = view.findViewById(R.id.tv_deliveryTime);
        tv_driver_versionName = view.findViewById(R.id.tv_driver_versionName);
        tv_driver_versionCode = view.findViewById(R.id.tv_driver_versionCode);
        tv_hardWare_batch = view.findViewById(R.id.tv_hardWare_batch);
        tv_maxSpeed = view.findViewById(R.id.tv_maxSpeed);
        tv_maxSlopes = view.findViewById(R.id.tv_maxSlopes);
        tv_steps = view.findViewById(R.id.tv_steps);
        tv_ports = view.findViewById(R.id.tv_ports);
        tv_buzzer = view.findViewById(R.id.tv_buzzer);
        tv_resolution = view.findViewById(R.id.tv_resolution);
        tv_dpi = view.findViewById(R.id.tv_dpi);
        tv_systemVersionName = view.findViewById(R.id.tv_systemVersionName);
    }

    private void setSystemText() {
        tv_app_versionName.setText(system_info.getAppVersionName(getContext()));
        tv_app_versionCode.setText(String.valueOf(system_info.getAppVersionCode(getContext())));
//        tv_resolution.setText(String.valueOf(system_info.getResolutionWidth(getContext())+"x"+String.valueOf(system_info.getResolutionHeight(getContext()))));
        tv_resolution.setText(String.valueOf("1920*1080"));
        tv_systemVersionName.setText(system_info.getSystemVersion());
        tv_driver_versionName.setText(system_info.getDriverName());
        tv_dpi.setText(String.valueOf(system_info.getDensity(Fragment_SystemInfo.this)));
        tv_steps.setText(String.valueOf(newSerial.MACHINE_STEP));
        tv_hardWare_batch.setText(String.valueOf(newSerial.MACHINE_BATCH));
        tv_driver_versionCode.setText(String.valueOf(android.os.Build.ID));
        tv_ports.setText(String.valueOf("9600"));
        tv_buzzer.setText(String.valueOf(newSerial.MACHINE_BUZZER));
        tv_maxSpeed.setText(String.valueOf(SportData.MAXSPEED));
        tv_maxSlopes.setText(String.valueOf(SportData.MAXSLOPES));
        tv_deliveryTime.setText(String.valueOf("2018-07-17 20:35:24"));
    }
}
