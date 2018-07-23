package com.vigorchip.omatreadmill.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.vigorchip.omatreadmill.R;
import com.vigorchip.omatreadmill.activity.MainActivity;
import com.vigorchip.omatreadmill.bean.Skins_Selector;

/**
 * Created by wr-app1 on 2018/4/3.
 */

public class Fragment_Skins extends Fragment implements View.OnClickListener{
    private ImageView bg_yellow,bg_1,bg_2,bg_3,bg_4,bg_5,bg_6,bg_black;
    private ImageView mark_yellow,mark_1,mark_2,mark_3,mark_4,mark_5,mark_6,mark_black;
    private View view;
    private Skins_Selector skins_selector;
    private SharedPreferences sp_saveSkins;
    private SharedPreferences sp_loadSkins;
    private SharedPreferences.Editor editor;
    private String spGetMark;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_skins,container,false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        skins_selector = new Skins_Selector();
        init();
        sp_loadSkins = getContext().getSharedPreferences("sp_skins",Context.MODE_PRIVATE);
        if (sp_loadSkins != null) {
            spGetMark = sp_loadSkins.getString("mark","");
            if (!spGetMark.equals("0")) {
                if (spGetMark.equals("1")){
                    ((MainActivity)getContext()).activity_main.setBackgroundResource(skins_selector.arr_drawable[1]);
                    mark_1.setVisibility(View.VISIBLE);
                    mark_2.setVisibility(View.GONE);
                    mark_3.setVisibility(View.GONE);
                    mark_4.setVisibility(View.GONE);
                    mark_5.setVisibility(View.GONE);
                    mark_6.setVisibility(View.GONE);
                    mark_yellow.setVisibility(View.GONE);
                    mark_black.setVisibility(View.GONE);
                } else if (spGetMark.equals("2")) {
                    ((MainActivity)getContext()).activity_main.setBackgroundResource(skins_selector.arr_drawable[2]);
                    mark_1.setVisibility(View.GONE);
                    mark_2.setVisibility(View.VISIBLE);
                    mark_3.setVisibility(View.GONE);
                    mark_4.setVisibility(View.GONE);
                    mark_5.setVisibility(View.GONE);
                    mark_6.setVisibility(View.GONE);
                    mark_yellow.setVisibility(View.GONE);
                    mark_black.setVisibility(View.GONE);
                } else if (spGetMark.equals("3")) {
                    ((MainActivity)getContext()).activity_main.setBackgroundResource(skins_selector.arr_drawable[3]);
                    mark_1.setVisibility(View.GONE);
                    mark_2.setVisibility(View.GONE);
                    mark_3.setVisibility(View.VISIBLE);
                    mark_4.setVisibility(View.GONE);
                    mark_5.setVisibility(View.GONE);
                    mark_6.setVisibility(View.GONE);
                    mark_yellow.setVisibility(View.GONE);
                    mark_black.setVisibility(View.GONE);
                } else if (spGetMark.equals("4")) {
                    ((MainActivity)getContext()).activity_main.setBackgroundResource(skins_selector.arr_drawable[4]);
                    mark_1.setVisibility(View.GONE);
                    mark_2.setVisibility(View.GONE);
                    mark_3.setVisibility(View.GONE);
                    mark_4.setVisibility(View.VISIBLE);
                    mark_5.setVisibility(View.GONE);
                    mark_6.setVisibility(View.GONE);
                    mark_yellow.setVisibility(View.GONE);
                    mark_black.setVisibility(View.GONE);
                } else if (spGetMark.equals("5")) {
                    ((MainActivity)getContext()).activity_main.setBackgroundResource(skins_selector.arr_drawable[5]);
                    mark_1.setVisibility(View.GONE);
                    mark_2.setVisibility(View.GONE);
                    mark_3.setVisibility(View.GONE);
                    mark_4.setVisibility(View.GONE);
                    mark_5.setVisibility(View.VISIBLE);
                    mark_6.setVisibility(View.GONE);
                    mark_yellow.setVisibility(View.GONE);
                    mark_black.setVisibility(View.GONE);
                } else if (spGetMark.equals("6")) {
                    ((MainActivity)getContext()).activity_main.setBackgroundResource(skins_selector.arr_drawable[6]);
                    mark_1.setVisibility(View.GONE);
                    mark_2.setVisibility(View.GONE);
                    mark_3.setVisibility(View.GONE);
                    mark_4.setVisibility(View.GONE);
                    mark_5.setVisibility(View.GONE);
                    mark_6.setVisibility(View.VISIBLE);
                    mark_yellow.setVisibility(View.GONE);
                    mark_black.setVisibility(View.GONE);
                } else if (spGetMark.equals("7")) {
                    ((MainActivity)getContext()).activity_main.setBackgroundResource(skins_selector.arr_drawable[7]);
                    mark_1.setVisibility(View.GONE);
                    mark_2.setVisibility(View.GONE);
                    mark_3.setVisibility(View.GONE);
                    mark_4.setVisibility(View.GONE);
                    mark_5.setVisibility(View.GONE);
                    mark_6.setVisibility(View.GONE);
                    mark_yellow.setVisibility(View.VISIBLE);
                    mark_black.setVisibility(View.GONE);
                } else if (spGetMark.equals("8")) {
                    ((MainActivity)getContext()).activity_main.setBackgroundResource(skins_selector.arr_drawable[0]);
                    mark_1.setVisibility(View.GONE);
                    mark_2.setVisibility(View.GONE);
                    mark_3.setVisibility(View.GONE);
                    mark_4.setVisibility(View.GONE);
                    mark_5.setVisibility(View.GONE);
                    mark_6.setVisibility(View.GONE);
                    mark_yellow.setVisibility(View.GONE);
                    mark_black.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void init() {
        bg_yellow = view.findViewById(R.id.bg_yellow);
        bg_1 = view.findViewById(R.id.bg_1);
        bg_2 = view.findViewById(R.id.bg_2);
        bg_3 = view.findViewById(R.id.bg_3);
        bg_4 = view.findViewById(R.id.bg_4);
        bg_5 = view.findViewById(R.id.bg_5);
        bg_6 = view.findViewById(R.id.bg_6);
        bg_black = view.findViewById(R.id.bg_black);
        Glide.with(this).load(skins_selector.arr_drawable[0]).into(bg_black);
        Glide.with(this).load(skins_selector.arr_drawable[1]).into(bg_1);
        Glide.with(this).load(skins_selector.arr_drawable[2]).into(bg_2);
        Glide.with(this).load(skins_selector.arr_drawable[3]).into(bg_3);
        Glide.with(this).load(skins_selector.arr_drawable[4]).into(bg_4);
        Glide.with(this).load(skins_selector.arr_drawable[5]).into(bg_5);
        Glide.with(this).load(skins_selector.arr_drawable[6]).into(bg_6);
        Glide.with(this).load(skins_selector.arr_drawable[7]).into(bg_yellow);

        bg_yellow.setOnClickListener(this);
        bg_1.setOnClickListener(this);
        bg_2.setOnClickListener(this);
        bg_3.setOnClickListener(this);
        bg_4.setOnClickListener(this);
        bg_5.setOnClickListener(this);
        bg_6.setOnClickListener(this);
        bg_black.setOnClickListener(this);

        mark_yellow = view.findViewById(R.id.mark_yellow);
        mark_1 = view.findViewById(R.id.mark_1);
        mark_2 = view.findViewById(R.id.mark_2);
        mark_3 = view.findViewById(R.id.mark_3);
        mark_4 = view.findViewById(R.id.mark_4);
        mark_5 = view.findViewById(R.id.mark_5);
        mark_6 = view.findViewById(R.id.mark_6);
        mark_black = view.findViewById(R.id.mark_black);
        Glide.with(this).load(R.drawable.checkmark).into(mark_yellow);
        Glide.with(this).load(R.drawable.checkmark).into(mark_1);
        Glide.with(this).load(R.drawable.checkmark).into(mark_2);
        Glide.with(this).load(R.drawable.checkmark).into(mark_3);
        Glide.with(this).load(R.drawable.checkmark).into(mark_4);
        Glide.with(this).load(R.drawable.checkmark).into(mark_5);
        Glide.with(this).load(R.drawable.checkmark).into(mark_6);
        Glide.with(this).load(R.drawable.checkmark).into(mark_black);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bg_1:
                skins_selector.setSelector("1");
                showAlerDialog(getContext(),skins_selector.getSelector(),
                        ((MainActivity)getContext()).activity_main);
                break;
            case R.id.bg_2:
                skins_selector.setSelector("2");
                showAlerDialog(getContext(),skins_selector.getSelector(),
                        ((MainActivity)getContext()).activity_main);
                break;
            case R.id.bg_3:
                skins_selector.setSelector("3");
                showAlerDialog(getContext(),skins_selector.getSelector(),
                        ((MainActivity)getContext()).activity_main);
                break;
            case R.id.bg_4:
                skins_selector.setSelector("4");
                showAlerDialog(getContext(),skins_selector.getSelector(),
                        ((MainActivity)getContext()).activity_main);
                break;
            case R.id.bg_5:
                skins_selector.setSelector("5");
                showAlerDialog(getContext(),skins_selector.getSelector(),
                        ((MainActivity)getContext()).activity_main);
                break;
            case R.id.bg_6:
                skins_selector.setSelector("6");
                showAlerDialog(getContext(),skins_selector.getSelector(),
                        ((MainActivity)getContext()).activity_main);
                break;
            case R.id.bg_yellow:
                skins_selector.setSelector("7");
                showAlerDialog(getContext(),skins_selector.getSelector(),
                        ((MainActivity)getContext()).activity_main);
                break;
            case R.id.bg_black:
                skins_selector.setSelector("8");
                showAlerDialog(getContext(),skins_selector.getSelector(),
                        ((MainActivity)getContext()).activity_main);
                break;
        }
    }

    public void showAlerDialog(Context context, final String selector, final View v) {
        sp_saveSkins = getContext().getSharedPreferences("sp_skins",Context.MODE_PRIVATE);
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.Hint))
                .setMessage(context.getString(R.string.skin_toast))
                .setPositiveButton(context.getString(R.string.OK2), new DialogInterface.OnClickListener() {
                    private Skins_Selector skins_selector = new Skins_Selector();
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (selector.equals("1")){
                            v.setBackgroundResource(skins_selector.arr_drawable[1]);
                            skins_selector.setMark("1");
                            mark_1.setVisibility(View.VISIBLE);
                            mark_2.setVisibility(View.GONE);
                            mark_3.setVisibility(View.GONE);
                            mark_4.setVisibility(View.GONE);
                            mark_5.setVisibility(View.GONE);
                            mark_6.setVisibility(View.GONE);
                            mark_yellow.setVisibility(View.GONE);
                            mark_black.setVisibility(View.GONE);
                            editor = sp_saveSkins.edit();
                            editor.putString("mark", skins_selector.getMark());
                            editor.commit();
                        } else if (selector.equals("2")) {
                            v.setBackgroundResource(skins_selector.arr_drawable[2]);
                            skins_selector.setMark("2");
                            mark_1.setVisibility(View.GONE);
                            mark_2.setVisibility(View.VISIBLE);
                            mark_3.setVisibility(View.GONE);
                            mark_4.setVisibility(View.GONE);
                            mark_5.setVisibility(View.GONE);
                            mark_6.setVisibility(View.GONE);
                            mark_yellow.setVisibility(View.GONE);
                            mark_black.setVisibility(View.GONE);
                            editor = sp_saveSkins.edit();
                            editor.putString("mark", skins_selector.getMark());
                            editor.commit();
                        } else if (selector.equals("3")) {
                            v.setBackgroundResource(skins_selector.arr_drawable[3]);
                            skins_selector.setMark("3");
                            mark_1.setVisibility(View.GONE);
                            mark_2.setVisibility(View.GONE);
                            mark_3.setVisibility(View.VISIBLE);
                            mark_4.setVisibility(View.GONE);
                            mark_5.setVisibility(View.GONE);
                            mark_6.setVisibility(View.GONE);
                            mark_yellow.setVisibility(View.GONE);
                            mark_black.setVisibility(View.GONE);
                            editor = sp_saveSkins.edit();
                            editor.putString("mark", skins_selector.getMark());
                            editor.commit();
                        } else if (selector.equals("4")) {
                            v.setBackgroundResource(skins_selector.arr_drawable[4]);
                            skins_selector.setMark("4");
                            mark_1.setVisibility(View.GONE);
                            mark_2.setVisibility(View.GONE);
                            mark_3.setVisibility(View.GONE);
                            mark_4.setVisibility(View.VISIBLE);
                            mark_5.setVisibility(View.GONE);
                            mark_6.setVisibility(View.GONE);
                            mark_yellow.setVisibility(View.GONE);
                            mark_black.setVisibility(View.GONE);
                            editor = sp_saveSkins.edit();
                            editor.putString("mark", skins_selector.getMark());
                            editor.commit();
                        } else if (selector.equals("5")) {
                            v.setBackgroundResource(skins_selector.arr_drawable[5]);
                            skins_selector.setMark("5");
                            mark_1.setVisibility(View.GONE);
                            mark_2.setVisibility(View.GONE);
                            mark_3.setVisibility(View.GONE);
                            mark_4.setVisibility(View.GONE);
                            mark_5.setVisibility(View.VISIBLE);
                            mark_6.setVisibility(View.GONE);
                            mark_yellow.setVisibility(View.GONE);
                            mark_black.setVisibility(View.GONE);
                            editor = sp_saveSkins.edit();
                            editor.putString("mark", skins_selector.getMark());
                            editor.commit();
                        } else if (selector.equals("6")) {
                            v.setBackgroundResource(skins_selector.arr_drawable[6]);
                            skins_selector.setMark("6");
                            mark_1.setVisibility(View.GONE);
                            mark_2.setVisibility(View.GONE);
                            mark_3.setVisibility(View.GONE);
                            mark_4.setVisibility(View.GONE);
                            mark_5.setVisibility(View.GONE);
                            mark_6.setVisibility(View.VISIBLE);
                            mark_yellow.setVisibility(View.GONE);
                            mark_black.setVisibility(View.GONE);
                            editor = sp_saveSkins.edit();
                            editor.putString("mark", skins_selector.getMark());
                            editor.commit();
                        } else if (selector.equals("7")) {
                            v.setBackgroundResource(skins_selector.arr_drawable[7]);
                            skins_selector.setMark("7");
                            mark_1.setVisibility(View.GONE);
                            mark_2.setVisibility(View.GONE);
                            mark_3.setVisibility(View.GONE);
                            mark_4.setVisibility(View.GONE);
                            mark_5.setVisibility(View.GONE);
                            mark_6.setVisibility(View.GONE);
                            mark_yellow.setVisibility(View.VISIBLE);
                            mark_black.setVisibility(View.GONE);
                            editor = sp_saveSkins.edit();
                            editor.putString("mark", skins_selector.getMark());
                            editor.commit();
                        } else if (selector.equals("8")) {
                            v.setBackgroundResource(skins_selector.arr_drawable[0]);
                            skins_selector.setMark("8");
                            mark_1.setVisibility(View.GONE);
                            mark_2.setVisibility(View.GONE);
                            mark_3.setVisibility(View.GONE);
                            mark_4.setVisibility(View.GONE);
                            mark_5.setVisibility(View.GONE);
                            mark_6.setVisibility(View.GONE);
                            mark_yellow.setVisibility(View.GONE);
                            mark_black.setVisibility(View.VISIBLE);
                            editor = sp_saveSkins.edit();
                            editor.putString("mark", skins_selector.getMark());
                            editor.commit();
                        }
                    }
                })
                .setNegativeButton(getContext().getString(R.string.Cancel),null)
                .create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.WHITE);
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.WHITE);
    }
}