package com.vigorchip.omatreadmill.bean;

import com.vigorchip.omatreadmill.R;

/**
 * Created by wr-app1 on 2018/4/3.
 */

public class Skins_Selector {
    public int[] arr_drawable =
            {R.drawable.bg_black_pic,R.drawable.bg_1, R.drawable.bg_2,R.drawable.bg_3,
                    R.drawable.bg_4,R.drawable.bg_5,R.drawable.bg_6,R.drawable.bg_yellow_pic};
    private String Selector = "0";
    private String Mark = "0";

    public String getMark() {
        return Mark;
    }

    public void setMark(String mark) {
        Mark = mark;
    }

    public String getSelector() {
        return Selector;
    }

    public void setSelector(String selector) {
        Selector = selector;
    }
}
