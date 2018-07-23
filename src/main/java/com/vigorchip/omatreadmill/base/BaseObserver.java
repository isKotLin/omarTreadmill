package com.vigorchip.omatreadmill.base;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

/**
 * Created by wr-app2 on 2018/5/12.
 */

public abstract class BaseObserver<T> implements Observer<T> {

    private String msg;
    private Context mContext;
    private Disposable mDisposable;
    private AlertDialog loadDialog;
    private boolean flag = true;
    private boolean isAccident = true;
    private String snackBarMessage = "断开网络连接";
    private String netErrorMessage = "连接服务器失败,请稍后重试";
    private JSONObject mError;
    private JSONObject mJsonObject;

    public BaseObserver(Context context) {
        mContext = context;
    }

    /**
     * @param context
     * @param msg     对话框提示语
     */
    public BaseObserver(Context context, String msg) {
        mContext = context;
        this.msg = msg;
    }

    /**
     * @param context
     * @param flag    是否显示对话框
     */
    public BaseObserver(Context context, boolean flag) {
        mContext = context;
        this.flag = flag;
    }

    @Override
    public void onSubscribe(final Disposable d) {
        Log.e("print","onSubscribe");
        mDisposable = d;
        if (flag) {
            loadDialog = null == msg ? showDialog() : showDialog(msg);
        }
    }

    /**
     * 解析服务端返回的数据
     *
     * @param value
     */
    @Override
    public void onNext(T value) {
        Log.e("print","onNext");
        try {
            if (value instanceof ResponseBody) {
                String string = ((ResponseBody) value).string();
                mJsonObject = new JSONObject(string);
                String code = mJsonObject.getString("success");//这是返回的状态
                String message = mJsonObject.getString("message");//这是返回的信息
                success(mJsonObject, code, message, mDisposable);
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onError(Throwable e) {
        Log.e("print",e.toString());
        if (loadDialog != null) {
            snackBarMessage = netErrorMessage;
            String message = e.getMessage();
            if (message.equals("timeout")) {
                netErrorMessage = "连接超时";
            }
            isAccident = true;
            loadDialog.dismiss();
        }
        e.printStackTrace();

    }
    @Override
    public void onComplete() {
        Log.e("print","onComplete");
        if (loadDialog != null) {
            isAccident = false;
            loadDialog.dismiss();
        }
    }

    public AlertDialog showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("提示");
        builder.setMessage("正在加载中");
        builder.setCancelable(false);
        loadDialog = builder.create();
        loadDialog.show();
        return loadDialog;
    }

    public AlertDialog showDialog(String content) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("提示");
        builder.setMessage(content);
        builder.setCancelable(false);
        loadDialog = builder.create();
        loadDialog.show();
        return loadDialog;
    }

    protected abstract void success(JSONObject value, String code, String message, Disposable disposable) throws IOException, JSONException;

    //得到上下文
    public Context getContext() {
        return mContext;
    }
}