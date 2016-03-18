package com.boding.microscreen.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.boding.microscreen.R;
import com.boding.microscreen.app.AppConstants;
import com.boding.microscreen.model.BaseDatas;
import com.boding.microscreen.net.Request4BaseDatas;
import com.boding.microscreen.net.RequestManager;
import com.boding.microscreen.service.AudioPlayService;
import com.boding.microscreen.util.MyLog;
import com.boding.microscreen.util.SPUtils;
import com.boding.microscreen.util.Util;
import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class LoginActivity extends BaseActivity {
	
	private static final String TAG = "LoginActivity";
	private ImageView mIvLoginBg;
	private TextView tv_currentVersion;
	private EditText et_code;
	private Button btn_enter;
	@Bind(R.id.ibtn_close)
	ImageButton mNavCloseView;
	private String validateCode = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_activity_login);

		initView();
		initData();
		initEvent();
	}
	
	/**
	 * 初始化视图
	 */
	private void initView(){
		ButterKnife.bind(this);
		mIvLoginBg = (ImageView) findViewById(R.id.id_iv_login_bg);
		tv_currentVersion = (TextView) findViewById(R.id.tv_currentVersion);
		et_code = (EditText) findViewById(R.id.et_code);
		btn_enter = (Button) findViewById(R.id.btn_enter);
	}

	/**
	 * 初始化数据
	 */
	private void initData(){

		// 是否加载的基础数据出错，比如绑定码错误
		Intent intent = getIntent();
		if (null != intent){
			if(intent.hasExtra("error")){
				String errorMsg = intent.getStringExtra("error");
				if(!"".equals(errorMsg)){
					Crouton.makeText(this, errorMsg, Style.ALERT, R.id.id_fl_contain).show();
				}
			}
		}

		// 背景图片
        /*Glide.with(this)
                .load(R.drawable.login_bg)
                .placeholder(R.drawable.login_bg)
                .error(R.drawable.login_bg)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mIvLoginBg);*/

		PackageInfo packageInfo = Util.getAppPackageInfo(this);
		tv_currentVersion.setText(packageInfo.versionName.trim());
		if(SPUtils.contains(this, AppConstants.VALIDATE_CODE)){
			validateCode = (String)SPUtils.get(this, AppConstants.VALIDATE_CODE, "0");
		}
		et_code.setText(validateCode.trim());
		String code = et_code.getText().toString().trim();
		if (!"".equals(code)) {
			// 把光标移到字符串的最后面
			Editable edit = et_code.getText();
			if (edit instanceof Spannable) {
			    Spannable spanText = (Spannable)edit;
			    Selection.setSelection(spanText, code.length());
			}
		}
	}
	
	/**
	 * 初始化事件
	 */
	private void initEvent(){
		
		// 点击"进入微屏幕"按钮回调事件处理
		btn_enter.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				if (Util.isNetworkConnected(LoginActivity.this)) {
					validateCode = et_code.getText().toString().trim();
					// 把新的绑定码保存到 sharedPreference中
					SPUtils.put(LoginActivity.this, AppConstants.VALIDATE_CODE, validateCode);
					// 跳转到微屏幕主页
					if (!"".equals(validateCode)) {
						String url = String.format(AppConstants.BASE_DATA_URL,validateCode);
						loadBaseData(url);
					}else{
                        Crouton.showText(LoginActivity.this, "您还没有输入识别码！", Style.ALERT);
					}
				}else{
                    Crouton.showText(LoginActivity.this, "无法连接网络，请检查后重试！", Style.ALERT);
				}
			}
		});

		mNavCloseView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 显示退出应用对话框
				Util.exitDialog(LoginActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
			}
		});
	}

	/**
	 * 请求加载数据
	 * @param url 请求地址
	 */
	private void loadBaseData(String url){
		RequestManager.addRequest(new Request4BaseDatas(url, new Response.Listener<BaseDatas>() {
			@Override
			public void onResponse(BaseDatas response) {

				Intent i = new Intent(LoginActivity.this, AudioPlayService.class);
				i.setAction(AudioPlayService.AUDIO_ACTION);
				startService(i);

				Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
				intent.putExtra("baseDatas", response);
				intent.putExtra("validateCode",validateCode);
				LoginActivity.this.startActivity(intent);
				LoginActivity.this.finish();

			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				MyLog.e(TAG, "错误000：" + error.getMessage());
				String errorMsg;
				if (AppConstants.VALIDATE_CODE_OVER_INFO.equals(error.getMessage())){
					errorMsg = AppConstants.VALIDATE_CODE_OVER_INFO;
				}else {
					errorMsg = "电视终端码错误，请检查！";
				}
                Crouton.showText(LoginActivity.this, errorMsg, Style.ALERT);
			}
		}), LoginActivity.this);
	}

	@Override
	public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 显示退出应用对话框
			Util.exitDialog(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onDestroy() {
		tv_currentVersion = null;
		et_code = null;
		btn_enter = null;
		validateCode = null;
		Glide.get(this).clearMemory();
		Crouton.cancelAllCroutons();
		RequestManager.cancelAll(this);
		System.gc();
		super.onDestroy();
	}
}
