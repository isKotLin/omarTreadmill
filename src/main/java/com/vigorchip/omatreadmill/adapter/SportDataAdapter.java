package com.vigorchip.omatreadmill.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vigorchip.omatreadmill.R;
import com.vigorchip.omatreadmill.bean.Item_Message;
import com.vigorchip.omatreadmill.utils.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SportDataAdapter extends RecyclerView.Adapter<SportDataAdapter.ViewHolder> {
    private List<Item_Message> list;
    private Context context;
    private int h;


    public SportDataAdapter(Context context, List<Item_Message> list, int h) {

        this.context = context;

        this.list = list;

        this.h = h;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.lv_run_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.item_starttime.setText(getDateToString(Long.parseLong(list.get(position).getStarttime())));
        holder.item_timeall.setText(TimeUtils.TimeFomat(Long.parseLong(list.get(position).getTotletime())));
        holder.item_distance.setText(list.get(position).getDistance() + "km");
        holder.itme_calorie.setText(list.get(position).getKaluli() + "cal");
        String strtime1 = (String) holder.item_starttime.getText().toString();

        final String strtime2 = strtime1.substring(11, 13);//截取字符串獲取小時
        Date date = new Date(Long.parseLong(list.get(position).getStarttime()));//設置時間戳秒形式獲取星期
        if (Integer.parseInt(strtime2) >= 18) {//大于18代表大於六點，表示夜跑
            holder.item_datatime.setText(TimeUtils.getWeekOfDate(date) + "  " + context.getString(R.string.NightRun));
        } else {
            holder.item_datatime.setText(TimeUtils.getWeekOfDate(date) + "  " + context.getString(R.string.DayRun));
        }
        //item点击事件
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(context,strtime2,Toast.LENGTH_SHORT).show();
//            }
//        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView item_starttime, item_timeall, item_distance, itme_calorie, item_datatime;

        public ViewHolder(View itemView) {
            super(itemView);
            item_starttime = itemView.findViewById(R.id.item_starttime);
            item_timeall = itemView.findViewById(R.id.item_timeall);
            item_distance = itemView.findViewById(R.id.item_distance);
            itme_calorie = itemView.findViewById(R.id.itme_calorie);
            item_datatime = itemView.findViewById(R.id.item_datatime);


        }

    }

    public static String getDateToString(long milSecond) {
        Date date = new Date(milSecond);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }
}
