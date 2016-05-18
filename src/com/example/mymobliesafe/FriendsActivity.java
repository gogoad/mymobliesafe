package com.example.mymobliesafe;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.PrivateCredentialPermission;

import com.example.domain.ContactBean;
import com.example.engine.ReadContactsEngine;
import com.example.utils.MyConstants;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class FriendsActivity extends ListActivity {

	protected static final int LOADING = 1;
	protected static final int FINISH = 2;
	private List<ContactBean> datas = new ArrayList<ContactBean>();
	private MyAdapter adapter;
	private ListView mDatas;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mDatas = getListView();
		adapter = new MyAdapter();
		mDatas.setAdapter(adapter);
		//初始化数据
		initData();
		//初始化事件
		initEvent();
	}
	
	private void initEvent() {
		mDatas.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				//处理条目点击事件
				//获取当前条目的数据
				ContactBean contantBean = datas.get(position);
				//获取号码
				String phone = contantBean.getPhone();
				Intent datas = new Intent();
				datas.putExtra(MyConstants.SAFENUMBER, phone);//保存安全号码
				//设置数据
				setResult(1, datas);
				//关闭自己
				finish();
			}
			
		});
		
	}

	private Handler handler = new Handler() {
		private ProgressDialog pd;

		public void handleMessage(Message msg) {
		
			switch (msg.what) {
			case LOADING:
				pd = new ProgressDialog(FriendsActivity.this);
				pd.setTitle("注意");
				pd.setMessage("正在玩命加载数据。。。。。");
				pd.show();//显示对话框
				break;
				
			case FINISH://数据加载完成
				if (pd != null) {
					pd.dismiss();//关闭对话框
					pd = null;//垃圾回收释放内存
				}
				
				//数据显示在ListView中,是通过适配器来通知listview
				adapter.notifyDataSetChanged();
				break;
			

			default:
				break;
			}
		};
	};
	
	private void initData() {
		//获取数据是一个耗时的操作  
		new Thread() {
			@Override
			public void run() {
				//正在加载
				Message msg = Message.obtain();
				msg.what = LOADING;
				handler.sendMessage(msg);
<<<<<<< a85ebdb4ce72494761159c4ab03551e2f8835d3c
				SystemClock.sleep(3000);
=======
				SystemClock.sleep(2000);
>>>>>>> 第四次提交 手机防盗功能完成
				
				datas = ReadContactsEngine.readContacts(getApplicationContext());
				//数据获取完成,发送数据加载完成的消息
				msg = Message.obtain();
				msg.what = FINISH;
				handler.sendMessage(msg);
			}
		}.start();
		
		
		
	}

	private class MyAdapter extends BaseAdapter {

		private TextView mTvName;
		private TextView mTvPhone;

		@Override
		public int getCount() {
			
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
			View view = View.inflate(getApplicationContext(), R.layout.item_friend_listview, null);
			mTvName = (TextView) view.findViewById(R.id.tv_friends_item_name);
			mTvPhone = (TextView) view.findViewById(R.id.tv_friends_item_phone);
			
			ContactBean bean = datas.get(position);
			mTvName.setText(bean.getName());
			mTvPhone.setText(bean.getPhone());
			return view;
		}
		
	}
}
