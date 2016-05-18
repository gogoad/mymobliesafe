package com.example.mymobliesafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public abstract class BaseSetupAcitivity extends Activity {
	private GestureDetector mDetector;

	//定义一个手势识别器
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
		//初始化手势识别器
		initGesture();
		//初始化数据
		initData();
		//初始化事件
		initEvent();
		
	}

	public void initEvent() {
		// TODO Auto-generated method stub
		
	}

	public void initData() {
		// TODO Auto-generated method stub
		
	}

	private void initGesture() {
		mDetector = new GestureDetector(this,  new SimpleOnGestureListener() {
			/**
			 * e1表示起点  e2表示结束点 
			 * velocity表示水平方向的速度
			 * velocity表示垂直方向的速度
			 */
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				// TODO Auto-generated method stub
				//e2.getRawX();//表示屏幕上的坐标点
				//e2.getX();//相对当前被触摸的控件的的坐标点
				if (Math.abs(e2.getRawY() - e1.getRawY()) > 100) {
					//y方向滑动太大
					Toast.makeText(getApplicationContext(), "不能这样滑，哥们", Toast.LENGTH_LONG).show();
					return true;
				}
				if (Math.abs(velocityX) < 150) {
					Toast.makeText(getApplicationContext(), "滑动速度太慢了", Toast.LENGTH_LONG).show();
				}
				if (Math.abs(e2.getRawX() - e1.getRawX()) > 100) {
					//向右滑，上一页
					next(null);
					
				}
				if (Math.abs(e1.getRawX() - e2.getRawY()) > 100)  {
					//向左滑，下一页
					prev(null);
				}
				return super.onFling(e1, e2, velocityX, velocityY);
			}
			
		});
	}

	public abstract void initView();

	public void next(View v) {
		nextActivity();
		nextAnimation();
	}

	public void prev(View v) {
		//画面切换
		prevActivity();
		//动画播放 
		prevtAnimation();
	}
	
	public abstract void nextActivity();

	public abstract void prevActivity();
	
	public void startActivity(Class type) {
		Intent intent = new Intent(this, type);
		startActivity(intent);
		finish();
	}

	private void nextAnimation() {
		overridePendingTransition(R.anim.next_in, R.anim.next_out);
	}
	
	private void prevtAnimation() {
		//第一个参数进来的动画，第二个参数是出去的动画
		overridePendingTransition(R.anim.prev_in, R.anim.prev_out);
	}
	//初始化手势识别器,要想手势识别器生效，绑定onTouch事件
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		mDetector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}
}
