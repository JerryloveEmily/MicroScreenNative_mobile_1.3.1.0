package com.boding.microscreen.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.boding.microscreen.R;
import com.boding.microscreen.app.AppConstants;
import com.boding.microscreen.model.AppInfo;
import com.boding.microscreen.model.BaseDatas;
import com.boding.microscreen.net.Request4BaseDatas;
import com.boding.microscreen.net.RequestManager;
import com.boding.microscreen.service.AudioPlayService;
import com.boding.microscreen.util.JLog;
import com.boding.microscreen.util.MyLog;
import com.boding.microscreen.util.SPUtils;
import com.boding.microscreen.util.Util;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;


public class SplashActivity extends BaseActivity {
    private static final String TAG = "SlashActivity";

    private String validateCode = "";
    private static ProgressBar mLoadProgress;
    private ProgressBar mProgressBar;
    private Dialog mDownloadDialog;
    private TextView mCurVersion;

    /** 是否取消更新 */
    private boolean cancelUpdate = false;
    /** 记录进度条数量 */
    private int progress;

    private String url = "screentv/weixintvWall/";
    /** 下载中 */
    private static final int DOWNLOADING = 1003;
    /** 下载完成 */
    private static final int DOWNLOAD_FINISH = 1004;
    /** 下载失败 */
    private static final int DOWNLOAD_FAILED = 1005;
    /** SD卡不存在 */
    private static final int SD_NO_EXIST = 1007;

    private static final int INSTALL_APP_REQUEST_CODE = 0x110;

//    private ImageView background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_slash);
        initView();
        initDatas();
    }

    /**
     * 初始化视图
     */
    private void initView() {

        Intent i = new Intent(this, AudioPlayService.class);
        i.setAction(AudioPlayService.AUDIO_ACTION);
        startService(i);

        mCurVersion = (TextView) findViewById(R.id.tv_currentVersion);
        PackageInfo packageInfo = Util.getAppPackageInfo(this);
        mCurVersion.setText(packageInfo.versionName.trim());
        mLoadProgress = (ProgressBar) findViewById(R.id.pb_progress);
    }

    /**
     * 初始化数据
     */
    private void initDatas() {
        new MyThread(this).start();
    }

    private static class MyThread extends Thread {

        private WeakReference<SplashActivity> mReference;

        public MyThread(SplashActivity activity){
            mReference = new WeakReference<>(activity);
        }

        @Override
        public void run() {
            super.run();
            SplashActivity outClass = mReference.get();
            if (outClass != null) {
                outClass.updateProgress();
            }
        }
    }

    private void updateProgress(){
        for (int i = 0; i < 110; i++) {
            int progress = mLoadProgress.getProgress();
            if (progress < mLoadProgress.getMax()) {
                progress += 1;
            }
            mLoadProgress.setProgress(progress);
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        checkAppUpdate();
    }

    private void checkAppUpdate() {
        PackageInfo packageInfo = Util.getAppPackageInfo(this);
        loadAppUpdate(packageInfo.versionName.trim());
    }

    /**
     * 界面跳转
     */
    private void turnToInterface() {

        if (Util.isNetworkConnected(this)) {
            // 把首页加载进度条设置为最大进度
            mLoadProgress.setProgress(mLoadProgress.getMax());
            if (SPUtils.contains(this, AppConstants.VALIDATE_CODE)) {
                validateCode = (String) SPUtils.get(this,
                        AppConstants.VALIDATE_CODE, "0");
                MyLog.e(TAG, "validateCode=" + validateCode);
                if (!"".equals(validateCode)) {
                    String url = String.format(AppConstants.BASE_DATA_URL,validateCode);
                    loadBaseData(url);
                } else {
                    Intent intent = new Intent(SplashActivity.this,
                            LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            } else {
                Intent intent = new Intent(SplashActivity.this,
                        LoginActivity.class);
                startActivity(intent);
                finish();
            }
        } else {
            Crouton.showText(SplashActivity.this, "无法连接网络，请检查后重试！", Style.ALERT);
        }
    }

    /**
     * 请求加载数据
     * @param url
     */
    private void loadBaseData(String url){
        JLog.e(url);
        RequestManager.addRequest(new Request4BaseDatas(url, new Response.Listener<BaseDatas>() {
            @Override
            public void onResponse(BaseDatas response) {
                Intent intent = new Intent(SplashActivity.this,HomeActivity.class);
                intent.putExtra("baseDatas", response);
                intent.putExtra("validateCode",validateCode);
                SplashActivity.this.startActivity(intent);
                SplashActivity.this.finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyLog.e(TAG, "错误：" + error.getMessage());
                String errorMsg;
                if (AppConstants.VALIDATE_CODE_OVER_INFO.equals(error.getMessage())){
                    errorMsg = AppConstants.VALIDATE_CODE_OVER_INFO;
                }else {
                    errorMsg = "电视终端码错误，请检查！";
                }
                Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                intent.putExtra("error", errorMsg);
                SplashActivity.this.startActivity(intent);
                SplashActivity.this.finish();
            }
        }), SplashActivity.this);
    }

    /**
     * 加载app更新信息
     * @param curVersion
     */
    private void loadAppUpdate(String curVersion) {
        if (Util.isNetworkConnected(this)) {
            /*IAppUpdateCallBack callBack = new MicroScreenClientInterface.IAppUpdateCallBack() {

                public void cancle() {
                }

                public void error(String info) {
                    turnToInterface();
                }

                public void successed(AppInfo appInfo) {
                    if (appInfo != null) {
                        if (appInfo.isIsUpdate()) {
                            // 检查app有更新后，把首页加载进度条设置为最大进度
                            mLoadProgress.setProgress(mLoadProgress.getMax());
                            noCoerceUpdateAppDialog(appInfo);
                        } else {
                            turnToInterface();
                        }
                    }
                }
            };
            MicroScreenClientHelper.requestAppUpdate(callBack, curVersion);*/
            String url = String.format(AppConstants.UPDATE_APP_URL, curVersion);
            StringRequest appUpdateRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    JLog.d("loadAppUpdate: " + response);
                    JSONTokener jsonParser = new JSONTokener(response);
                    AppInfo appInfo = new AppInfo();
                    try {
                        JSONObject jsonObject = (JSONObject) jsonParser.nextValue();
                        if (jsonObject.has("url")
                                && !"".equals(jsonObject.getString("url"))) {
                            // 应用有更新
                            appInfo.setLatestVersion(jsonObject.getString("latest_version"));
                            appInfo.setDescripte(jsonObject.getString("description"));
                            appInfo.setUrl(jsonObject.getString("url"));
                            appInfo.setIsForce(false);
                            appInfo.setIsUpdate(true);
                            // 检查app有更新后，把首页加载进度条设置为最大进度
                            mLoadProgress.setProgress(mLoadProgress.getMax());
                            noCoerceUpdateAppDialog(appInfo);
                        } else { // 应用没有更新
                            turnToInterface();
                        }
                    } catch (JSONException e) {
                        turnToInterface();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    turnToInterface();
                }
            });
            RequestManager.addRequest(appUpdateRequest, this);

        } else {
            turnToInterface();
        }
    }

    public void noCoerceUpdateAppDialog(final AppInfo appInfo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("更新应用");
        String desc = appInfo.getDescripte();
        if (!"".equals(desc)) {
            // String[] descStrs = desc.split(",");
            StringBuilder builder2 = new StringBuilder();
            builder2.append("更新的内容：\n" + desc);
            // for (int i = 0; i < descStrs.length; i++) {
            // builder2.append((i + 1) + "：" + descStrs[0]);
            // }
            builder.setMessage("新的版本是：" + appInfo.getLatestVersion() + "\n"
                    + builder2.toString());
        }
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                turnToInterface();
            }
        });

        builder.setPositiveButton("立即更新",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // 下载更新应用
                        showDownloadDialog(0, appInfo);
                    }
                });
        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {

            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void showDownloadDialog(final int isCoerce, final AppInfo appInfo) {
        // 构造软件下载对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("正在更新下载应用...");
        // 给下载对话框增加进度条
        final LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.layout_dialog_softupdate_progress, null);
        mProgressBar = (ProgressBar) v.findViewById(R.id.update_progress);
        builder.setView(v);
        // 取消更新
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // 设置取消状态
                cancelUpdate = true;
                Crouton.showText(SplashActivity.this, "下载中断，更新失败!", Style.ALERT);
            }
        });
        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {

            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        mDownloadDialog = builder.create();
        mDownloadDialog.setCanceledOnTouchOutside(false);
        mDownloadDialog.show();
        if (appInfo != null) {
            String appUrl = appInfo.getUrl();
//            MyLog.d(TAG, "appurl: " + appUrl);
            new DownloadApkThread(appUrl).start();
        }
    }

    /**
     * 下载文件线程
     */
    private class DownloadApkThread extends Thread {

        private String appUrl;

        public DownloadApkThread(String appUrl) {
            this.appUrl = appUrl;
        }

        public void run() {
            try {
                Runtime runtime = Runtime.getRuntime();
                String command1 = "chmod -R 777 "
                        + SplashActivity.this.getFilesDir();
                runtime.exec(command1);
                String fileName = SplashActivity.this.getFilesDir()
                        .getPath() + "/" + AppConstants.APK_NAME;
                URL url = new URL(appUrl);
                // 创建连接
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                if (conn != null) {
                    conn.setDoInput(true);
                    conn.setUseCaches(false);
                    conn.setConnectTimeout(300 * 1000); // 设置连接超时的时间
                    conn.setReadTimeout(300 * 1000); // 设置Socket超时的时间
                    conn.connect();
                    // 获取文件大小
                    int length = conn.getContentLength();
                    // 创建输入流
                    InputStream is = conn.getInputStream();
                    File backupFile = new File(SplashActivity.this
                            .getFilesDir().getPath());
                    if (!backupFile.exists()) {
                        backupFile.mkdirs();
                    }
                    File apkFile = new File(fileName);
                    // 判断文件目录是否存在
                    if (!apkFile.exists()) {
                        apkFile.createNewFile();
                    }
                    FileOutputStream fos = new FileOutputStream(apkFile);
                    int count = 0;
                    // 缓存
                    byte buf[] = new byte[1024 * 6];
                    // 写入到文件中
                    do {
                        int numread = is.read(buf);
                        count += numread;
                        // 计算进度条位置
                        progress = (int) (((float) count / length) * 100);
                        // 更新进度
                        mHandler.sendEmptyMessage(DOWNLOADING);
                        if (numread <= 0) {
                            // 下载完成
                            mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
                            break;
                        }
                        // 写入文件
                        fos.write(buf, 0, numread);
                    } while (!cancelUpdate);// 点击取消就停止下载.
                    fos.close();
                    is.close();
                } else {
                    mHandler.sendEmptyMessage(DOWNLOAD_FAILED);
                }
            }  catch (Exception e) {
                e.printStackTrace();
                mHandler.sendEmptyMessage(DOWNLOAD_FAILED);
            }
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                // SD卡不存在
                case SD_NO_EXIST:
                    Crouton.showText(SplashActivity.this, "SD卡不存在，下载失败!", Style.ALERT);
                    break;
                // 正在下载
                case DOWNLOADING:
                    // 设置进度条位置
                    mProgressBar.setProgress(progress);
                    break;

                case DOWNLOAD_FINISH:
                    Crouton.showText(SplashActivity.this, "下载成功!", Style.CONFIRM);
                    // 取消下载对话框显示
                    mDownloadDialog.dismiss();
                    // 安装文件
                    installApk();
                    break;

                case DOWNLOAD_FAILED:
                    Crouton.showText(SplashActivity.this, "下载失败!", Style.ALERT);
                    turnToInterface();
                    break;
            }
        };

        /**
         * 安装APK文件
         */
        private void installApk() {
            Runtime runtime = Runtime.getRuntime();
            String command1 = "chmod -R 777 "
                    + SplashActivity.this.getFilesDir();
            try {
                runtime.exec(command1);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            File apkfile = new File(SplashActivity.this.getFilesDir()
                    .getPath() + "/" + AppConstants.APK_NAME);
            if (!apkfile.exists()) {
                Crouton.showText(SplashActivity.this, "新的应用安装包文件不存在!", Style.ALERT);
                return;
            }
            String apkFilePath = apkfile.getPath();
            // 通过Intent安装APK文件
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setDataAndType(Uri.parse("file://" + apkFilePath),
                    "application/vnd.android.package-archive");
            startActivityForResult(i, INSTALL_APP_REQUEST_CODE);
        }
    };

    protected void onDestroy() {
        if (null != mHandler) {
            mHandler = null;
        }
        validateCode = null;
        mLoadProgress = null;
        mProgressBar = null;
        mDownloadDialog = null;
        mCurVersion = null;
        Glide.get(this).clearMemory();
        Crouton.cancelAllCroutons();
        RequestManager.cancelAll(this);
        System.gc();
        super.onDestroy();
    }
}
