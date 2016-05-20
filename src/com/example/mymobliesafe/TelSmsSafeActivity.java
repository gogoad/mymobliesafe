package com.example.mymobliesafe;

import java.util.ArrayList;
import java.util.List;

import com.example.dao.BlackNumberDao;
import com.example.domain.BlackBean;
import com.example.domain.BlackTable;
import com.example.utils.MyConstants;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 通讯卫士
 * 黑名单管理的原理，点击添加按钮，弹出自定义对话框，
 * 在对话框中让用户输入要拦截的号码，并且选择要拦截的类型
 * 拦截的类型分为短信拦截、电话拦截、全部拦截。
 * 当用户确定后，那么会将这些要拦截的电话以及拦截类型保存在本地数据
 * 库中。同时会编写一个Service服务，在该服务里面我们监听所有的短信和电话
 * 当有短信来时我们拿到短信发送者的号码，如果改号码在黑马单里面则要
 * 么拦截期短信（终止广播或者删除短信数据库），要么拦截其电话（挂断电话）
 * @author jxa
 */
public class TelSmsSafeActivity extends Activity {

	protected static final int LOADING = 1;
	protected static final int FINSIH = 2;
	private Button mAddSafeNumber;
	private ListView mListSafeNumber;
	private TextView mNodata;
	private ProgressBar mPbLoading;
	private BlackNumberDao blackNumberDao;
	private MyAdapter adapter;
	private List<BlackBean> datas = new ArrayList<BlackBean>();

	private TextView mItemNumber;
	private TextView mItemMode;
	private ImageView mItemDelete;
	private final int MOREDATASCOUNTS = 20;// 分批加载的数据个数



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		initView();
		initData();
		initEvent();
		
		initPopupWindow();//弹出窗体，功能：让用户可以从联系人，电话记录，短信记录中添加黑名单数据
		
	}
	
	
	
	private void initPopupWindow() {
		view = View.inflate(getApplicationContext(), R.layout.popup_blacknumber_item, null);
		//手动导入
		mShoudong = (TextView) view.findViewById(R.id.tv_popup_black_shoudong);
		//联系人导入
		mContacts = (TextView) view.findViewById(R.id.tv_popup_black_contacts);
		//通话记录导入
		mPhoneLog = (TextView) view.findViewById(R.id.tv_popup_black_phonelog);
		//短信记录导入
		mSmsLog = (TextView) view.findViewById(R.id.tv_popup_black_smslog);
		
		View.OnClickListener listener = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.tv_popup_black_contacts:// 从联系人导入
					System.out.println("从联系人导入");
					{
						Intent intent = new Intent(TelSmsSafeActivity.this, FriendsActivity.class);
						startActivityForResult(intent, 1);
					}
					break;
				case R.id.tv_popup_black_phonelog:// 从电话日志导入
					System.out.println("从电话日志导入");
					{
						Intent intent = new Intent(TelSmsSafeActivity.this, CalllogsActivity.class);
						startActivityForResult(intent, 1);
					}
					break;
				case R.id.tv_popup_black_shoudong:// 手动导入
					//System.out.println("手动导入");
					showInputBlackNumberDialog("");
					break;
				case R.id.tv_popup_black_smslog:// 从短信导入
					System.out.println("从短信导入");
					{
						Intent intent = new Intent(TelSmsSafeActivity.this, SmslogsActivity.class);
						startActivityForResult(intent, 1);
					}
					break;

				default:
					break;
				}

				// 关闭popupwindow
				closePopupWindow();
			}
		};
		mShoudong.setOnClickListener(listener);
		mContacts.setOnClickListener(listener);
		mPhoneLog.setOnClickListener(listener);
		mSmsLog.setOnClickListener(listener);
		
		pw = new PopupWindow(view,-2,-2);
		// 显示动画要有背景
		pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

		sa = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0f);
		sa.setDuration(200);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (data != null) {
			String phone = data.getStringExtra(MyConstants.SAFENUMBER);
			showInputBlackNumberDialog(phone);
			adapter.notifyDataSetChanged();//更新数据界面
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private void showPopupWindow() {
		if (pw != null && pw.isShowing()) {
			pw.dismiss();
		}else {
			int[] location = new int[2];
			//添加按钮的坐标
			mAddSafeNumber.getLocationInWindow(location);
			//开启动画
			view.startAnimation(sa);
			
			//设置右上角对齐
			pw.showAtLocation(mAddSafeNumber,  Gravity.RIGHT| Gravity.TOP,
					location[0] - (getWindowManager().getDefaultDisplay().getWidth() - mAddSafeNumber.getWidth()),
					location[1] + mAddSafeNumber.getHeight());
			
		}
		
	}
	
	protected void closePopupWindow() {
		// TODO Auto-generated method stub
		if (pw !=null && pw.isShowing()) {
			pw.dismiss();
		}
	}



	private void initEvent() {
		// TODO Auto-generated method stub
		mListSafeNumber.setOnScrollListener(new OnScrollListener() {
			
			/**
			 * onScrollStateChanged 状态改变调用此方法 SCROLL_STATE_FLING:
			 * 惯性滑动 SCROLL_STATE_IDLE: 滑动停止 SCROLL_STATE_TOUCH_SCROLL: 按住滑动
			 * 三种状态，每种状态改变都会触发此方法
			 */
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				//如果为静止状态
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					int lastVisiblePosition = mListSafeNumber.getLastVisiblePosition();
					if (lastVisiblePosition == datas.size()-1) {
						initData();
					}
				}
			}
			//按住滑动触发事件
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				
			}
		});
	}


	private List<BlackBean> moreDatas;
	private void initData() {
		//从db中取黑名单数据，是一个耗时的操作，必须在子线程中取数据
		new Thread() {
			

			@Override
			public void run() {
				//取数据之前，发个消息显示正在加载数据的进度条
				//handler.obtainMessage(LOADING).sendToTarget();
				handler.obtainMessage(LOADING).sendToTarget();

				// 加载更多数据
				moreDatas = blackNumberDao.getMoreDatas(MOREDATASCOUNTS, datas.size());

				datas.addAll(moreDatas);// 把一个容器的所有数据加进来
				// 取数据完成，发消息通知取数据完成
				handler.obtainMessage(FINSIH).sendToTarget();
				super.run();
			}
		}.start();	
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LOADING:
				mListSafeNumber.setVisibility(View.GONE);
				mNodata.setVisibility(View.GONE);
				mPbLoading.setVisibility(View.VISIBLE);
				break;
			case FINSIH:
				if (moreDatas.size() != 0) {
					// 显示listview
					mListSafeNumber.setVisibility(View.VISIBLE);

					// 隐藏没有数据
					mNodata.setVisibility(View.GONE);

					// 隐藏加载数据的进度
					mPbLoading.setVisibility(View.GONE);

					// 更新数据
					adapter.notifyDataSetChanged();// 通知listview重新去adapter中的数据
				} else {// 没有取到数据

					if (datas.size() != 0) {// 分批加载数据，没有更多数据
						Toast.makeText(getApplicationContext(), "没有更多数据", 1)
								.show();
						return;
					}
					// 没有数据
					// 隐藏listview
					mListSafeNumber.setVisibility(View.GONE);

					// 显示没有数据
					mNodata.setVisibility(View.VISIBLE);

					// 隐藏加载数据的进度
					mPbLoading.setVisibility(View.GONE);
				}
				break;

			default:
				break;
			}
		};
	};
	private TextView mShoudong;
	private TextView mContacts;
	private TextView mPhoneLog;
	private TextView mSmsLog;
	private PopupWindow pw;
	private ScaleAnimation sa;
	private View view;
	private AlertDialog dialog;
	
	private void initView() {
		setContentView(R.layout.activity_telsmssafe);
		//添加黑名单号码的按钮
		mAddSafeNumber = (Button) findViewById(R.id.bt_telsms_addsafenumber);
		//显示黑名单数据
		mListSafeNumber = (ListView) findViewById(R.id.lv_telsms_safenumbers);
		mNodata = (TextView) findViewById(R.id.tv_telsms_nodata);
		//正在加载的对话框
		mPbLoading = (ProgressBar) findViewById(R.id.pb_telsms_loading);
		//黑名单业务对象
		blackNumberDao = new BlackNumberDao(getApplicationContext());
		adapter = new MyAdapter();
		//给Listview设置适配器,取适配器的数据显示
        //首先 调用adapter的getCount方法来获取多少条数据，如果为0，不显示任何数据，否则调用getView方法依次取出显示位置的数据
		mListSafeNumber.setAdapter(adapter);
	}
	
	private class ItemView{
		TextView mItemNumber;
		TextView mItemMode;
		ImageView mItemDelete;
	}
	
	public class MyAdapter extends BaseAdapter {

		private BlackBean bean;

		@Override
		public int getCount() {
			// 返回黑名单数据的条目
			return datas.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ItemView itemView = null;//声明组件封装对象 初始为null
			if (convertView == null) {
				convertView = View.inflate(getApplicationContext(), R.layout.item_telsmssafe_listview, null);
				itemView = new ItemView();
				itemView.mItemNumber = (TextView) convertView.findViewById(R.id.tv_telsmssafe_listview_item_number);
				itemView.mItemMode = (TextView) convertView.findViewById(R.id.tv_telsmssafe_listview_item_mode);
				itemView.mItemDelete = (ImageView)convertView.findViewById(R.id.iv_telsmssafe_listview_item_delete);
				convertView.setTag(itemView);
			}else {
				itemView = (ItemView) convertView.getTag();
			}
			bean = datas.get(position);
			itemView.mItemNumber.setText(bean.getPhone());
			switch (bean.getMode()) {
			case BlackTable.SMS:
				itemView.mItemMode.setText("短信拦截");
				break;
			case BlackTable.TEL:
				itemView.mItemMode.setText("电话拦截");
				break;
			case BlackTable.ALL:
				itemView.mItemMode.setText("全部拦截");
				break;

			default:
				break;
			}
			//删除黑名单数据
			itemView.mItemDelete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					AlertDialog.Builder builder = new AlertDialog.Builder(TelSmsSafeActivity.this);
					builder.setTitle("注意")
						   .setMessage("是否删除黑名单号码")
						   .setPositiveButton("删除", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								//删除数据库中的数据
								blackNumberDao.delete(bean.getPhone());
								// 删除容器中对应的数据
								datas.remove(position);
								// 通知界面更新数据，让用户看到删除数据不存在
								adapter.notifyDataSetChanged();
							}
						});
					builder.setNegativeButton("点错了", null);
					builder.show();
				}
			});
			
			return convertView;
		}
		
	}
	
	public void addBlackNumber(View v) {
		showPopupWindow();
	}

	private void showInputBlackNumberDialog(String phone) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View view = View.inflate(getApplicationContext(), R.layout.dialog_addblacknumber, null);
		final EditText mEtblack = (EditText) view.findViewById(R.id.et_telsmssafe_blacknumber);
		final CheckBox mCbsms = (CheckBox) view.findViewById(R.id.cb_telsmssafe_smsmode);
		final CheckBox mCbphone = (CheckBox) view.findViewById(R.id.cb_telsmssafe_phonemode);
		Button mBtadd = (Button) view.findViewById(R.id.bt_telsmssafe_add);
		Button mBtcancel = (Button) view.findViewById(R.id.bt_telsmssafe_cancel);
	
		mEtblack.setText(phone);
		mBtcancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		
		mBtadd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String blackNumber = mEtblack.getText().toString().trim();
				if (TextUtils.isEmpty(blackNumber)) {
					Toast.makeText(getApplicationContext(), "号码不能为空", Toast.LENGTH_LONG).show();
					return;
				}
				if (!mCbsms.isChecked() && !mCbphone.isChecked()) {
					Toast.makeText(getApplicationContext(), "至少选择一种拦截模式", Toast.LENGTH_LONG).show();
					return;
				}
				
				int mode = 0;
				if (mCbphone.isChecked()) {
					mode |= BlackTable.TEL;
				}else if (mCbsms.isChecked()) {
					mode |= BlackTable.SMS;
				}
				//界面看到用户新增的数据
				BlackBean bean = new BlackBean();
				bean.setMode(mode);
				bean.setPhone(blackNumber);
				
				//添加到黑名单表中
				blackNumberDao.add(bean);
				// 如果新增的数据已经存在
				datas.remove(bean);
				datas.add(0, bean);//让listview显示第一条数据
				adapter = new MyAdapter();
				adapter.notifyDataSetChanged();//更新数据界面
				dialog.dismiss();
			
				mListSafeNumber.setVisibility(View.VISIBLE);
				mPbLoading.setVisibility(View.GONE);
				mNodata.setVisibility(View.GONE);
			}
		});
		builder.setView(view);
		dialog = builder.create();
		dialog.show();
	}

	
}
