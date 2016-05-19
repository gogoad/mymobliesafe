package com.example.mymobliesafe;

import java.util.ArrayList;
import java.util.List;

import com.example.dao.BlackNumberDao;
import com.example.domain.BlackBean;
import com.example.domain.BlackTable;

import android.app.Activity;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
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


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		initView();
		initData();
		
	}
	
	private void initData() {
		//从db中取黑名单数据，是一个耗时的操作，必须在子线程中取数据
		new Thread() {
			@Override
			public void run() {
				//取数据之前，发个消息显示正在加载数据的进度条
				handler.obtainMessage(LOADING).sendToTarget();
				SystemClock.sleep(2000);
				datas = blackNumberDao.getAllDatas();
				//取数据完成，发消息通知取数据完成
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
					//更新数据
					//通知listview重新去adapter中的数据
					adapter.notifyDataSetChanged();
				}else {
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
		public View getView(int position, View convertView, ViewGroup parent) {
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
			BlackBean bean = datas.get(position);
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
			return convertView;
		}
		
	}
}
