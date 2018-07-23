package com.vigorchip.omatreadmill.utils;

import android.text.TextUtils;

import com.vigorchip.omatreadmill.application.isApplication;
import com.zj.puliblib.okhttp.OkHttpUtils;
import com.zj.puliblib.okhttp.builder.GetBuilder;
import com.zj.puliblib.okhttp.builder.OkHttpRequestBuilder;
import com.zj.puliblib.okhttp.builder.OtherRequestBuilder;
import com.zj.puliblib.okhttp.builder.PostStringBuilder;
import com.zj.puliblib.okhttp.callback.FileCallBack;
import com.zj.puliblib.okhttp.callback.StringCallback;
import com.zj.puliblib.utils.Logutil;
import com.zj.puliblib.utils.NetUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.vigorchip.omatreadmill.utils.Constant.NO_NET;
import static com.vigorchip.omatreadmill.utils.Constant.SERVER_ERROR;


/*
 *  @项目名：  Customer2 
 *  @包名：    com.zhongjia.kwzo.util
 *  @创建者:   huang
 *  @创建时间:  2016/11/12 17:42
 *  @描述：    这里针对okhttpsUtils 进行简单的封装
 */
public class Okhttp {

    private static final String TENANTID = "TenantId";
    private static final String TENANTID_VALUE = "a3a4a4ec-5569-4b9b-f1fb-39dc32a6e5e1";
    private static final String AUTHORIZATION = "Authorization ";
    /**
     * 目标文件存储的文件夹路径
     */
    private static String destFileDir = FileUtils.getSDCardPath();

    /**
     * 设置头
     *
     * @param builder
     */
    public static void setHeader(OkHttpRequestBuilder builder) {
        String appversion = com.vigorchip.omatreadmill.utils.PublicUtls.getVerName(isApplication.getInstance());
        String appid = "wx9du375bfff511259";
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        String imei = "1508925209";
//        String ssid = MyApplication.getSSID();
        builder.addHeader("appversion", appversion);
        builder.addHeader("appid", appid);
        builder.addHeader("time", time);
        builder.addHeader("imei", imei);
//        builder.addHeader("ssid", ssid);
//        builder.addHeader("sign", sign);
    }

    /**
     * 这个get的请求
     *
     * @param url       //url
     * @param param     //参数：有参数传参数 ，没有传null
     * @param isTokenId // 是否 需要Tokenid
     * @param callBac   //回调方法
     */
    public static void get(boolean isTokenId, final String url, Map param, final CallBac callBac) {
        boolean netState = NetUtils.isNetworkAvailable(isApplication.getInstance());

        if (!netState) {
            callBac.onNoNetwork(NO_NET);
            return;
        }

        String mUrl = null;
        //判断是否有参数
        if (param == null) {
            mUrl = url;
        } else {
            mUrl = url + "?" + getUrlParamsByMap(param);
            Logutil.e("Param=" + param);
        }

        GetBuilder builder = OkHttpUtils
                .get()
                .url(mUrl)
                .tag(mUrl)
                .addHeader(TENANTID, TENANTID_VALUE);
        setHeader(builder);
        //需要tokenID
        if (isTokenId) {
            builder.addHeader(AUTHORIZATION, "Bearer " + getTokenId());
        }
        Logutil.e("URL=" + mUrl);
        builder.build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Logutil.e("服务器异常=" + e.getMessage());
                        callBac.onError(call, e, SERVER_ERROR, id);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Logutil.e("返回信息=" + response);
                        callBac.onResponse(response, id);
                    }
                });
    }

    public static void canecel(Object obj) {
        OkHttpUtils.getInstance().cancelTag(obj);

    }

    /**
     * put请求
     *
     * @param url
     * @param param
     * @param callBac
     */
    public static void put(final String url, final Map param, final CallBac callBac) {
        boolean netState = NetUtils.isNetworkAvailable(isApplication.getInstance());
        if (!netState) {
            callBac.onNoNetwork(NO_NET);
            return;
        }
        RequestBody body = null;
        if (param != null) {
            body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), getParam(param));
        }


        if (body == null) return;

        OkHttpUtils.put()
                .url(url)
                .requestBody(body)
                .addHeader(TENANTID, TENANTID_VALUE)
                .addHeader(AUTHORIZATION, "Bearer " + getTokenId())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Logutil.e("URL=" + url);
                        Logutil.e("参数=" + getParam(param));
                        Logutil.e("服务器异常=" + e.getMessage());
                        callBac.onError(call, e, SERVER_ERROR, id);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Logutil.e("URL=" + url);
                        Logutil.e("参数=" + getParam(param));
                        Logutil.e("返回信息=" + response);
                        callBac.onResponse(response, id);
                    }
                });
    }

    public static void putJSON(final String url, final JSONObject param, final CallBac callBac) {
        boolean netState = NetUtils.isNetworkAvailable(isApplication.getInstance());
        if (!netState) {
            callBac.onNoNetwork(NO_NET);
            return;
        }
        RequestBody body = null;
        if (param != null) {
            body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), param.toString());
        }
        if (body == null) {
            return;
        }
        OkHttpUtils.put()
                .url(url)
                .requestBody(body)
                .addHeader(TENANTID, TENANTID_VALUE)
                .addHeader(AUTHORIZATION, "Bearer " + getTokenId())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Logutil.e("URL=" + url);
                        Logutil.e("参数=" + param.toString());
                        Logutil.e("服务器异常=" + e.getMessage());
                        callBac.onError(call, e, SERVER_ERROR, id);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Logutil.e("URL=" + url);
                        Logutil.e("参数=" + param.toString());
                        Logutil.e("返回信息=" + response);
                        callBac.onResponse(response, id);
                    }
                });
    }


    /**
     * postString(postJson 数据）
     *
     * @param url       //url
     * @param param     //参数
     * @param callBac   // 回调
     * @param istokenId //是否需要Tokenid
     */

    public static void postString(boolean istokenId, final String url, final Map param, final CallBac callBac) {

        boolean netState = NetUtils.isNetworkAvailable(isApplication.getInstance());
        if (!netState) {
            callBac.onNoNetwork(NO_NET);
            return;
        }

        PostStringBuilder builder = OkHttpUtils.postString()
                .url(url)
                .addHeader(TENANTID, TENANTID_VALUE);

        setHeader(builder);
        //需要tokenid
        if (istokenId) {
            builder.addHeader(AUTHORIZATION, "Bearer " + getTokenId());
        }
        builder.content(getParam(param)).postContent(param)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Logutil.e("URL=" + url);
                        Logutil.e("参数=" + getParam(param));
                        Logutil.e("服务器异常=" + e.getMessage());
                        callBac.onError(call, e, SERVER_ERROR, id);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Logutil.e("URL=" + url);
                        Logutil.e("参数=" + getParam(param));
                        Logutil.e("返回信息=" + response);
                        callBac.onResponse(response, id);
                    }
                });
    }

    /**
     * 传json
     *
     * @param istokenId
     * @param url
     * @param json
     * @param callBac
     */
    public static void postJSON(boolean istokenId, final String url, final JSONObject json, final CallBac callBac) {

        boolean netState = NetUtils.isNetworkAvailable(isApplication.getInstance());
        if (!netState) {
            callBac.onNoNetwork(NO_NET);
            return;
        }

        PostStringBuilder builder = OkHttpUtils.postString()
                .url(url)
                .addHeader(TENANTID, TENANTID_VALUE);

        //需要tokenid
        if (istokenId) {
            Logutil.e("我的tokenid=" + getTokenId());
            builder.addHeader(AUTHORIZATION, "Bearer " + getTokenId());
        }


        builder.content(json.toString())
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Logutil.e("URL=" + url);
                        Logutil.e("参数=" + json.toString());
                        Logutil.e("服务器异常=" + e.getMessage());
                        callBac.onError(call, e, SERVER_ERROR, id);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Logutil.e("URL=" + url);
                        Logutil.e("参数=" + json.toString());
                        Logutil.e("返回信息=" + response);
                        callBac.onResponse(response, id);
                    }
                });
    }

    /**
     * post上传文件
     *
     * @param url
     * @param file
     * @param callBac
     */
    public static void postFile(final String url, File file, final CallBac callBac) {
        //判断网络状态
        boolean netState = NetUtils.isNetworkAvailable(isApplication.getInstance());
        if (!netState) {
            callBac.onNoNetwork(NO_NET);
            return;
        }
        OkHttpUtils.post()
                .addFile("file", file.getName(), file)
                .url(url)
                .addHeader(TENANTID, TENANTID_VALUE)
                .addHeader(AUTHORIZATION, "Bearer " + getTokenId())
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Logutil.e("URL=" + url);
                        Logutil.e("服务器异常=" + e.getMessage());
                        callBac.onError(call, e, SERVER_ERROR, id);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Logutil.e("URL=" + url);
                        Logutil.e("返回信息=" + response);
                        callBac.onResponse(response, id);
                    }
                });
    }

    /**
     * postString(postJson 数据）
     *
     * @param url       //url
     * @param param     //参数
     * @param callBac   // 回调
     * @param istokenId //是否需要Tokenid
     */

    public static void delete(boolean istokenId, final String url, final Map param, final CallBac callBac) {

        boolean netState = NetUtils.isNetworkAvailable(isApplication.getInstance());
        if (!netState) {
            callBac.onNoNetwork(NO_NET);
            return;
        }

        OtherRequestBuilder builder = OkHttpUtils.delete().url(url)
                .addHeader(TENANTID, TENANTID_VALUE);

        //需要tokenid
        if (istokenId) {
            Logutil.e("我的tokenid=" + getTokenId());
            builder.addHeader(AUTHORIZATION, "Bearer " + getTokenId());
        }


        builder.requestBody(getParam(param))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Logutil.e("URL=" + url);
                        Logutil.e("参数=" + getParam(param));
                        Logutil.e("服务器异常=" + e.getMessage());
                        callBac.onError(call, e, SERVER_ERROR, id);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Logutil.e("URL=" + url);
                        Logutil.e("参数=" + getParam(param));
                        Logutil.e("返回信息=" + response);
                        callBac.onResponse(response, id);
                    }
                });
    }

    /**
     * 文件下载
     *
     * @param url
     * @param fileName
     * @param callBac
     */
    public static void downloadFile(String url, String fileName, final FileCallBac callBac) {
        //判断网络状态
        boolean netState = NetUtils.isNetworkAvailable(isApplication.getInstance());
        if (!netState) {
            callBac.onNoNetwork(NO_NET);
            return;
        }
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new FileCallBack(destFileDir, fileName) {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        callBac.onError(call, e, id);
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        callBac.onResponse(response, id);
                    }

                    @Override
                    public void inProgress(float progress, long total, int id) {
                        super.inProgress(progress, total, id);
                        callBac.inProgress(progress, total, id);
                    }
                });
    }

    /**
     * 获取tokenid
     */
    public static String getTokenId() {
        if (TextUtils.isEmpty(tokenId)) {
            tokenId = isApplication.sp.getString("tokenid", "");
        }
        return tokenId;
    }

    public static String tokenId;

    public static void setTokenId(String tokentid) {
        isApplication.sp.edit().putString("tokenid", tokentid).commit();
        tokenId = tokentid;
    }

    /**
     * 获取参数
     *
     * @return
     */
    private static String getParam(Map map) {
        JSONObject parms_json = new JSONObject();
        if (map == null || map.size() == 0) {
            return parms_json.toString();
        }
        Set<String> keys = map.keySet();

        for (Iterator<String> it = keys.iterator(); it.hasNext(); ) {
            try {
                String key = (String) it.next();
                String value = (String) map.get(key) + "";  //如果这里报错了,请传入字符串
                parms_json.put(key, value);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return parms_json.toString();
    }

    //文件回调方法
    public interface FileCallBac {
        void onNoNetwork(String state);

        void onError(Call call, Exception e, int id);

        void onResponse(File response, int id);

        void inProgress(float progress, long total, int id);
    }


    //字符串回调接口
    public interface CallBac {

        void onError(Call call, Exception e, String state, int id);

        void onResponse(String response, int id);

        void onNoNetwork(String state);
    }

    //Object回调接口
    public interface ObjectCallBac<T> {

        void onError(Call call, Exception e, int id);

        void onResponse(T response, int id);

        void onNoNetwork(String state);
    }

    public static String getUrlParamsByMap(Map<String, Object> map) {
        if (map == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue());
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }
}
