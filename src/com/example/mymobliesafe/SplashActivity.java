package com.example.mymobliesafe;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.domain.UrlBean;
import com.example.utils.MyConstants;
import com.example.utils.SpTools;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SplashActivity extends Activity {

	private static final int ENTERHOME = 1;
	private static final int SHOWUPDATEDIALOG = 2;
	protected static final int ERROR = 3;
	private RelativeLayout rl;
	private TextView tv;
	private int versionCode;
	private String versionName;
	private UrlBean parseJson;
	private long startTimeMillis;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 初始化界面
		initView();
		// 初始化数据
		initData();
		// 初始化动画
		initAnimation();
	
	}

	/**
	 * 耗时的功能封装，只要耗时的处理，都放到此方法
	 */
	private void timeInitialization(){
		//一开始动画，就应该干耗时的业务（网络，本地数据初始化，数据的拷贝等）
		if (SpTools.getBoolean(getApplicationContext(), MyConstants.AUTOUPDATE, false)) {
			//true 自动更新
			// 检测服务器的版本
			checkVersion();
		}
		//增加自己的耗时功能处理
		
	}
	
	private void initAnimation() {
		// 渐变动画
		AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
		aa.setDuration(3000);
		//旋转动画
		RotateAnimation ra = new RotateAnimation(0, 360,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		ra.setDuration(3000);
		//停留在结束状态
		ra.setFillAfter(true);
		
		ScaleAnimation sa = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
				// 设置锚点
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		// 显示时间
		sa.setDuration(3000);
		// 界面停留在结束状�?
		sa.setFillAfter(true);
		
		//创建动画
		AnimationSet as = new AnimationSet(true);
		as.addAnimation(aa);
		as.addAnimation(ra);
		as.addAnimation(sa);
		//rl.startAnimation(aa);
		
		
		as.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				timeInitialization();
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				//动画播完进入主界面
				
				//判断是否进行服务器版本的检测
				if (!SpTools.getBoolean(getApplicationContext(), MyConstants.AUTOUPDATE, false)) {
					//不做版本检测，直接进入主界面
					enterHome();
				} else {
					//界面的衔接是有自动更新来完成，在此不做处理
				}
				
			}
		});
		rl.startAnimation(as);
	}

	private void initView() {
		setContentView(R.layout.activity_main);
		rl = (RelativeLayout) findViewById(R.id.rl_splash_root);
		tv = (TextView) findViewById(R.id.tv_splash_version_name);
		mProgressBar = (ProgressBar) findViewById(R.id.pb_splash_download_progress);
	}

	private void initData() {
		// 获取自己版本的信息
		try {
			PackageInfo packageInfo = getPackageManager().getPackageInfo(
					getPackageName(), 0);
			versionCode = packageInfo.versionCode;
			versionName = packageInfo.versionName;
			// 设置textView
			tv.setText("逗逼版" + versionName);
		} catch (NameNotFoundException e) {
			
			e.printStackTrace();
		}

	}

	private void checkVersion() {
		new Thread() {

			private BufferedReader buf;
			private HttpURLConnection conn;

			public void run() {
				int errorCode = -1;//正常，没有错误
				try {
					startTimeMillis = System.currentTimeMillis();
					URL url = new URL("http://10.0.2.2:8080/update.json");
					conn = (HttpURLConnection) url
							.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(2000);
					conn.setReadTimeout(2000);
					conn.connect();
					int code = conn.getResponseCode();
					if (code == 200) {
						// 链接服务器成功
						InputStream in = conn.getInputStream();
						buf = new BufferedReader(
								new InputStreamReader(in));
						// 读取信息
						String line = buf.readLine();
						// json字符串数据的封装
						StringBuilder json = new StringBuilder();
						while (line != null) {
							json.append(line);
							line = buf.readLine();
						}
						parseJson = parseJson(json);
						isNewVersion(parseJson);// 判断版本更新
						System.out.println(parseJson);
						buf.close();
						// conn.disconnect();
						parseJson(json);
					}else {
						errorCode = 404;
					}
						
				} catch (MalformedURLException e) {
					errorCode = 4002;
					e.printStackTrace();
				} catch (IOException e) {
					errorCode = 4001;
					e.printStackTrace();
				} catch (JSONException e) {
					errorCode = 4003;
					e.printStackTrace();
				}finally {
					Message msg = Message.obtain();
					if (errorCode == -1) {
						msg.what = isNewVersion(parseJson);
					}else {
						msg.what = ERROR;
						msg.arg1 = errorCode;
					}
					long endTime = System.currentTimeMillis();
					if (endTime - startTimeMillis < 3000){
						SystemClock.sleep(3000 - (endTime - startTimeMillis));//时间不超过3秒，补足3秒
					}
					handler.sendMessage(msg);//发送消息
					try {
						//关闭连接资源
						if (buf == null || conn == null){
							return;
						}
						buf.close();
						conn.disconnect();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
		}.start();
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ENTERHOME:// 进入主界面
				enterHome();
				break;
			case SHOWUPDATEDIALOG:// 弹出升级对话框
				showUpdateDialog();
				break;
			case ERROR:
				switch (msg.arg1) {
				case 404://找不到资源
					Toast.makeText(getApplicationContext(), "找不到资源", Toast.LENGTH_LONG).show();
					break;
				case 4001://找不到网络
					Toast.makeText(getApplicationContext(), "找不到网络", Toast.LENGTH_LONG).show();
					break;
				case 4002://json格式错误
					Toast.makeText(getApplicationContext(), "json格式错误", Toast.LENGTH_LONG).show();
					break;
				default:
					break;
				}
				enterHome();
				break;
			default:
				break;
			}
		};
	};
	private ProgressBar mProgressBar;

	protected int isNewVersion(UrlBean parseJson) {
		long endTimeMillis = System.currentTimeMillis();// 执行结束的时间
		if (endTimeMillis - startTimeMillis < 3000) {
			// 设置休眠的时间，保证至少休眠3秒
			SystemClock.sleep(3000 - (endTimeMillis - startTimeMillis));
		}
		// 获取服务器版本信息
		int serverCode = parseJson.getVersionCode();
		System.out.println(serverCode);
		if (serverCode == versionCode) {
			// 版本一致 不需要更新
			return ENTERHOME;
		} else {
			// 版本不一致 需要更新
			return SHOWUPDATEDIALOG;
		}
		

	}

	protected void showUpdateDialog() {
		// 更新对话框 对话框的上下文 是Activity的class,AlertDialog是Activity的一部分
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("提醒");
		builder.setMessage("是否更新新版本？" + parseJson.getDescription());
		builder.setPositiveButton("立即更新", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 下载apk
				downLoadApk();
			}
		});

		builder.setNegativeButton("暂不更新", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
			
				enterHome();
			}
		});
		
		builder.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				enterHome();
			}
		});
		builder.show();
	}

	protected void downLoadApk() {
		
		HttpUtils utils = new HttpUtils();
		utils.download(parseJson.getDownloadUrl(), "/mnt/sdcard/doubi.apk",
				new RequestCallBack<File>() {

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						
						mProgressBar.setVisibility(View.VISIBLE);//设置进度条显示
						mProgressBar.setMax((int) total);//设置最大值
						mProgressBar.setProgress((int) current);//设置当前进度

						super.onLoading(total, current, isUploading);
					}
			
					@Override
					public void onSuccess(ResponseInfo<File> arg0) {
						// 下载成功
						// 在主线程中执行
						Toast.makeText(getApplicationContext(), "下载新版本成功", 1)
								.show();
						// 安装apk
						installApk();
						mProgressBar.setVisibility(View.GONE);//隐藏进度条
						
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						
						// 下载失败
						Toast.makeText(getApplicationContext(), "下载新版本失败", 1)
								.show();
						enterHome();
						mProgressBar.setVisibility(View.GONE);//隐藏进度条
					}
				});
	}

	protected void installApk() {
		/*<intent-filter>
        <action android:name="android.intent.action.VIEW" />
        <category android:name="android.intent.category.DEFAULT" />
        <data android:scheme="content" />
        <data android:scheme="file" />
        <data android:mimeType="application/vnd.android.package-archive" />
    </intent-filter>  源码中得来*/
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		String type = "application/vnd.android.package-archive";
		Uri data = Uri.fromFile(new File("/mnt/sdcard/doubi.apk"));
		intent.setDataAndType(data, type);
		startActivityForResult(intent, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 用户取消安装apk，直接进入主界面
		enterHome();
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	protected void enterHome() {
		
		Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
		startActivity(intent);
		finish();
	}

	protected UrlBean parseJson(StringBuilder json) throws JSONException {
		UrlBean bean = new UrlBean();
		// {"versionName": "2.0", "versionCode": 2, "description":
		// "新增NB功能,赶紧体验!!!", "downloadUrl":
		// "http://192.168.1.109:8080/myMoblieSafe.apk"}
	
			JSONObject jsonObject = new JSONObject(json+ "");
			String versionName = jsonObject.getString("versionName");
			int versionCode = jsonObject.getInt("versionCode");
			String description = jsonObject.getString("description");
			String downloadUrl = jsonObject.getString("downloadUrl");

			bean.setVersionName(versionName);
			bean.setVersionCode(versionCode);
			bean.setDescription(description);
			bean.setDownloadUrl(downloadUrl);
		return bean;
	}

}
