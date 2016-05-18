package com.example.mymobliesafe;

import com.example.utils.Md5Utils;
import com.example.utils.MyConstants;
import com.example.utils.SpTools;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity {

	private GridView mGvMenus;
	private int icons[] = {
			R.drawable.safe,R.drawable.callmsgsafe,R.drawable.item_gv_selector_app
			,R.drawable.taskmanager,R.drawable.netmanager,R.drawable.trojan
			,R.drawable.sysoptimize,R.drawable.atools,R.drawable.settings
	};
	
	private String[] names = {
			"手机防盗","通讯卫士","软件管家","进程管理","流量统计","病毒查杀","缓存清理","高级工具","设置中心"
	};
	private MyAdapter adapter;
	private AlertDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		//初始化界面
		initView();
		//初始化数据
		initData();
		//初始化事件
		initEvent();
	}
	/**
	 *  当第一次点击手机防盗的时候弹出一个对话框，提示我们输入密码并再次
	 *	输入密码进行确定。密码设置好以后当我们再次点击手机防盗，那么弹出一个
	 *  对话框，提示输入密码。我们将用户输入的密码保存到SharedPreference里面，
	 *	这里我们需要做一个自定义对话框
	 */
	private void initEvent() {
		// TODO Auto-generated method stub
		mGvMenus.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				switch (position) {
				case 0://手机防盗
					showPwdDialog();
					break;
				case 8://设置中心
<<<<<<< a85ebdb4ce72494761159c4ab03551e2f8835d3c
					
=======
					Intent intent = new Intent(HomeActivity.this, SettingCenterActivity.class);
					startActivity(intent);
>>>>>>> 第四次提交 手机防盗功能完成
					break;
				default:
					break;
				}
			}
			
		});
	}

	protected void showPwdDialog() {
		String password = SpTools.getString(getApplicationContext(), MyConstants.PASSWORD, "");
		if (TextUtils.isEmpty(password)) {
			//如果为空  进入showSettingPwdDialog(); 设置密码对话框页面
			showSettingPwdDialog(); 
		}else {
			//进入输入密码对话框也面
			showInputPwdDialog();
		}
		
	}
	private void showInputPwdDialog() {
		final String password = SpTools.getString(getApplicationContext(), MyConstants.PASSWORD, "");
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View view = View.inflate(getApplicationContext(), R.layout.dialog_input_password, null);
		
		final EditText mEtPassOne = (EditText) view.findViewById(R.id.et_dialog_input_password_passone);
		Button mBtSetPass = (Button) view.findViewById(R.id.bt_dialog_input_password_setpass);
		Button mBtCancel = (Button) view.findViewById(R.id.bt_dialog_input_password_cancel);
		builder.setView(view);
		
		mBtSetPass.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String passOne = mEtPassOne.getText().toString().trim();
				if (TextUtils.isEmpty(passOne)) {
					Toast.makeText(getApplicationContext(), "密码不能为空", Toast.LENGTH_LONG).show();
					return;
				}else {
					String passone = Md5Utils.md5(passOne);
					String passMd5 = SpTools.getString(getApplicationContext(), MyConstants.PASSWORD, "");
					if (passone.equals(passMd5)) {
						//表示密码正确  进入手机防盗页面
						Intent intent = new Intent(HomeActivity.this, LostFindActivity.class);
						startActivity(intent);
						
					}else {
						Toast.makeText(getApplicationContext(), "密码错误", Toast.LENGTH_LONG).show();
					}
					//dialog.dismiss();
				}
			}
		});
		
		mBtCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				
			}
		});
		dialog = builder.create();
		dialog.show();
		
	}
	protected void showSettingPwdDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View view = View.inflate(getApplicationContext(), R.layout.dialog_setting_password, null);
		
		final EditText mEtPassOne = (EditText) view.findViewById(R.id.et_dialog_setting_password_passone);
		final EditText mEtPassTwo = (EditText) view.findViewById(R.id.et_dialog_setting_password_passtwo);
		Button mBtSetPass = (Button) view.findViewById(R.id.bt_dialog_setting_password_setpass);
		Button mBtCancel = (Button) view.findViewById(R.id.bt_dialog_setting_password_cancel);
		builder.setView(view);
		
		mBtSetPass.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//得到第一次和第二次输入的密码  如果一致
				String passOne = mEtPassOne.getText().toString().trim();
				String passTwo = mEtPassTwo.getText().toString().trim();
				
				if (TextUtils.isEmpty(passOne) || TextUtils.isEmpty(passTwo)) {
					Toast.makeText(getApplicationContext(), "密码不能为空", Toast.LENGTH_LONG).show();
					return;
				}else if (!passOne.equals(passTwo)) {
					Toast.makeText(getApplicationContext(), "密码不一致", Toast.LENGTH_LONG).show();
					return;
				}else {
					//保存密码
					String passone = Md5Utils.md5(passOne);
					System.out.println("加密后的密码" +passone);
					SpTools.putString(getApplicationContext(), MyConstants.PASSWORD, passone);
					dialog.dismiss();
					
				}
				
			}
		});
		
		mBtCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				
			}
		});
		dialog = builder.create();
		dialog.show();
		
	}
	private void initData() {
		adapter = new MyAdapter();
		mGvMenus.setAdapter(adapter);
	}

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			
			return icons.length;
		}

		@Override
		public Object getItem(int position) {
		
			return null;
		}

		@Override
		public long getItemId(int position) {
			
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			//加载布局
			View view = View.inflate(getApplicationContext(), R.layout.home_gridview, null);
			ImageView mIvIcon = (ImageView) view.findViewById(R.id.iv_home_gv_icon);
			TextView mTvTitle = (TextView) view.findViewById(R.id.tv_home_gv_title);
			
			mIvIcon.setImageResource(icons[position]);
			mTvTitle.setText(names[position]);
			return view;
		}
		
	}
	
	private void initView() {
		setContentView(R.layout.activity_home);
		mGvMenus = (GridView) findViewById(R.id.gv_home_menus);
	}
}
