package com.example.mymobliesafe;

import java.util.ArrayList;
import java.util.List;

import com.example.dao.BlackNumberDao;
import com.example.domain.BlackBean;
import com.example.domain.BlackTable;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
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
 * 分页技术
 * 
 * @author jxa
 */
public class TelSmsSafeActivityPage extends Activity {

	protected static final int LOADING = 1;
	protected static final int FINSIH = 2;
	private Button mAddSafeNumber;
	private ListView mListSafeNumber;
	private TextView mNodata;
	private ProgressBar mPbLoading;
	private BlackNumberDao blackNumberDao;
	private MyAdapter adapter;
	private List<BlackBean> datas = new ArrayList<BlackBean>();
	private int totalPages;// 总页数
	private int currentPage = 1;// 当前页的数据,默认1
	private final int perPage = 20;// 每页显示20条数据

	private TextView mItemNumber;
	private TextView mItemMode;
	private ImageView mItemDelete;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		initView();
		initData();
	}

	private void initData() {
		// 从db中取黑名单数据，是一个耗时的操作，必须在子线程中取数据
		new Thread() {
			@Override
			public void run() {
				// 取数据之前，发个消息显示正在加载数据的进度条
				// handler.obtainMessage(LOADING).sendToTarget();
				Message msg = Message.obtain();
				msg.what = LOADING;
				handler.sendMessage(msg);
				SystemClock.sleep(2000);
				datas = blackNumberDao.getAllDatas();
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
				if (datas.size() != 0) {
					mListSafeNumber.setVisibility(View.VISIBLE);
					mNodata.setVisibility(View.GONE);
					mPbLoading.setVisibility(View.GONE);
					// 更新数据
					// 通知listview重新去adapter中的数据
					adapter.notifyDataSetChanged();

					//初始化总页数和当前页的值
					mTotalpages.setText(currentPage + "/" + totalPages);
				} else {
					mListSafeNumber.setVisibility(View.GONE);
					mNodata.setVisibility(View.VISIBLE);
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
	private EditText mGotopage;
	private TextView mTotalpages;

	private void initView() {
		setContentView(R.layout.activity_telsmssafe);
		// 添加黑名单号码的按钮
		mAddSafeNumber = (Button) findViewById(R.id.bt_telsms_addsafenumber);
		// 显示黑名单数据
		mListSafeNumber = (ListView) findViewById(R.id.lv_telsms_safenumbers);
		mNodata = (TextView) findViewById(R.id.tv_telsms_nodata);
		// 正在加载的对话框
		mPbLoading = (ProgressBar) findViewById(R.id.pb_telsms_loading);
		mGotopage = (EditText) findViewById(R.id.et_telsms_gotopage);
		mTotalpages = (TextView) findViewById(R.id.tv_telsms_totalpages);

		// 黑名单业务对象
		blackNumberDao = new BlackNumberDao(getApplicationContext());
		adapter = new MyAdapter();
		// 给Listview设置适配器,取适配器的数据显示
		// 首先 调用adapter的getCount方法来获取多少条数据，如果为0，不显示任何数据，否则调用getView方法依次取出显示位置的数据
		mListSafeNumber.setAdapter(adapter);
	}

	private class ItemView {
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
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ItemView itemView = null;// 声明组件封装对象 初始为null
			if (convertView == null) {
				convertView = View.inflate(getApplicationContext(),
						R.layout.item_telsmssafe_listview, null);
				itemView = new ItemView();
				itemView.mItemNumber = (TextView) convertView
						.findViewById(R.id.tv_telsmssafe_listview_item_number);
				itemView.mItemMode = (TextView) convertView
						.findViewById(R.id.tv_telsmssafe_listview_item_mode);
				itemView.mItemDelete = (ImageView) convertView
						.findViewById(R.id.iv_telsmssafe_listview_item_delete);
				convertView.setTag(itemView);
			} else {
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
			// 删除黑名单数据
			itemView.mItemDelete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					AlertDialog.Builder builder = new AlertDialog.Builder(
							TelSmsSafeActivityPage.this);
					builder.setTitle("注意")
							.setMessage("是否删除黑名单号码")
							.setPositiveButton("删除",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// 删除数据库中的数据
											blackNumberDao.delete(bean
													.getPhone());
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
	
	/**
	 * 下一页
	 * 
	 * @param v
	 */
	public void nextPage(View v) {
		// 10页,最后一页，再点击下页： 1，给用户提醒最后一页，2，回到第一页
		currentPage++;// 下一页

		// 处理越界
		currentPage = currentPage % totalPages;

		// 取当前页的数据
		initData();

	}

	/**
	 * 上一页
	 * 
	 * @param v
	 */
	public void prevPage(View v) {
		// 10页,第一页，再点击上页： 1，给用户提醒第一页，2，回到尾页
		currentPage--;// 下一页

		if (currentPage == 0) {
			currentPage = totalPages;//显示最后一页
		}
		
		// 取当前页的数据
		initData();
	}

	/**
	 * 尾页
	 * 
	 * @param v
	 */
	public void endPage(View v) {
		//设置当前页为尾页
		currentPage = totalPages;
		//取数据
		initData();
	}

	/**
	 * 跳转
	 * 
	 * @param v
	 */
	public void jumpPage(View v) {
		//获取跳转页的页码
		String jumpPageStr = mGotopage.getText().toString().trim();
		if (TextUtils.isEmpty(jumpPageStr)) {
			Toast.makeText(getApplicationContext(), "跳转页不能为空", 1).show();
			return;
		}
		
		//把字符串的页码转成整数
		int jumpPage = Integer.parseInt(jumpPageStr);
		
		if (jumpPage >= 1 && jumpPage <= totalPages) {
			//把跳转页设置给当前页
			currentPage = jumpPage;
			initData();//初始数据
		} else {
			Toast.makeText(getApplicationContext(), "请按套路出牌", 1).show();
			return;
		}
	}

}
