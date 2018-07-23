package com.vigorchip.omatreadmill.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.vigorchip.omatreadmill.R;
import com.vigorchip.omatreadmill.bean.SportData;
import com.vigorchip.omatreadmill.utils.AnimateUtils;
import com.vigorchip.omatreadmill.utils.CreatDialog;
import com.vigorchip.omatreadmill.utils.LanguageUtils;

import java.util.Locale;

/**
 * Created by wr-app1 on 2018/3/27.
 */

public class Fragment_Language extends Fragment implements View.OnClickListener{
    private Button language_Chinese,language_English,language_France;
    private View view;
    private String able;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_language,container,false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        able = getContext().getResources().getConfiguration().locale.getLanguage();
        init();
        if (able.equals("zh")) {
            language_Chinese.setBackgroundResource(R.color.colorOrange);
        } else if (able.equals("en")){
            language_English.setBackgroundResource(R.color.colorOrange);
        } else if (able.equals("fr")){
            language_France.setBackgroundResource(R.color.colorOrange);
        }
    }

    private void init() {
        language_Chinese = view.findViewById(R.id.language_Chinese);
        language_English = view.findViewById(R.id.language_English);
        language_France = view.findViewById(R.id.language_France);
        language_Chinese.setOnClickListener(this);
        language_English.setOnClickListener(this);
        language_France.setOnClickListener(this);
        AnimateUtils.setClickAnimation(getContext(),language_Chinese);
        AnimateUtils.setClickAnimation(getContext(),language_English);
        AnimateUtils.setClickAnimation(getContext(),language_France);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.language_Chinese:
                if (SportData.isRunning()) {
                    CreatDialog.showDialog(getContext(), R.layout.dialog_language, true);
                    CreatDialog.dialog.findViewById(R.id.language_cancel)
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    CreatDialog.dialog.dismiss();
                                }
                            });
                } else {
                    CreatDialog.showDialog(getContext(), R.layout.dialog_language_toast, true);
                    CreatDialog.dialog.findViewById(R.id.lan_cancel)
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    CreatDialog.dialog.dismiss();
                                }
                            });
                    CreatDialog.dialog.findViewById(R.id.lan_next)
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    LanguageUtils.updateLanguage(Locale.SIMPLIFIED_CHINESE);
                                    CreatDialog.dialog.dismiss();
                                }
                            });
                }
                break;
            case R.id.language_English:
                if (SportData.isRunning()) {
                    CreatDialog.showDialog(getContext(), R.layout.dialog_language, true);
                    CreatDialog.dialog.findViewById(R.id.language_cancel)
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    CreatDialog.dialog.dismiss();
                                }
                            });
                } else {
                    CreatDialog.showDialog(getContext(), R.layout.dialog_language_toast, true);
                    CreatDialog.dialog.findViewById(R.id.lan_cancel)
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    CreatDialog.dialog.dismiss();
                                }
                            });
                    CreatDialog.dialog.findViewById(R.id.lan_next)
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    LanguageUtils.updateLanguage(Locale.ENGLISH);
                                    CreatDialog.dialog.dismiss();
                                }
                            });
                }
                break;
            case R.id.language_France:
                if (SportData.isRunning()) {
                    CreatDialog.showDialog(getContext(), R.layout.dialog_language, true);
                    CreatDialog.dialog.findViewById(R.id.language_cancel)
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    CreatDialog.dialog.dismiss();
                                }
                            });
                } else {
                    CreatDialog.showDialog(getContext(), R.layout.dialog_language_toast, true);
                    CreatDialog.dialog.findViewById(R.id.lan_cancel)
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    CreatDialog.dialog.dismiss();
                                }
                            });
                    CreatDialog.dialog.findViewById(R.id.lan_next)
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    LanguageUtils.updateLanguage(Locale.FRANCE);
                                    CreatDialog.dialog.dismiss();
                                }
                            });
                }
                break;
        }
    }
}
