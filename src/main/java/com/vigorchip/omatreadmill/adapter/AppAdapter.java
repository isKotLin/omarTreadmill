package com.vigorchip.omatreadmill.adapter;

import android.content.Context;
import android.content.pm.ResolveInfo;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.vigorchip.omatreadmill.R;

import java.util.List;

public class AppAdapter extends BaseAdapter {
    List<ResolveInfo> mApps;
    Context mContext;
    private OnItemListenerOnClick onItemListenerOnClick;
    private OnItemListenerOnLongTouch onItemListenerOnLongTouch;

    public AppAdapter(Context context, List<ResolveInfo> apps) {
        mContext = context;
        mApps = apps;
    }

    @Override
    public int getCount() {
        return mApps == null ? 0 : mApps.size();
    }

    @Override
    public ResolveInfo getItem(int position) {
        return mApps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        AppViewHandle handle;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.app_items, null);
            handle = new AppViewHandle(convertView);
            convertView.setTag(handle);
        } else
            handle = (AppViewHandle) convertView.getTag();
        ResolveInfo resolveInfo = mApps.get(position);
        handle.apps_iv.setImageDrawable(resolveInfo.activityInfo.loadIcon(mContext.getPackageManager()));
        handle.apps_tv.setText(resolveInfo.loadLabel(mContext.getPackageManager()));
        handle.app_ll_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemListenerOnClick.itemClick(position);
            }
        });
        handle.app_ll_item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                onItemListenerOnLongTouch.itemLongTouch(position);
                return false;
            }
        });
        return convertView;
    }

    public void setOnItemListenerOnClick(OnItemListenerOnClick onItemListenerOnClick) {
        this.onItemListenerOnClick = onItemListenerOnClick;
    }

    public void setOnItemListenerOnLongTouch(OnItemListenerOnLongTouch OnItemListenerOnLongTouch) {
        this.onItemListenerOnLongTouch = OnItemListenerOnLongTouch;
    }

    public interface OnItemListenerOnClick {
        void itemClick(int position);
    }

    public interface OnItemListenerOnLongTouch {
        void itemLongTouch(int position);
    }

}