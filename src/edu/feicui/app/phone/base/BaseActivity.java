package edu.feicui.app.phone.base;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View.OnClickListener;

import edu.feicui.app.phone.activity.R;
import edu.feicui.app.phone.base.util.LogUtil;
import edu.feicui.app.phone.view.ActionBarView;

/**
 * Activity基础类
 */
public abstract class BaseActivity extends Activity {

	// --------------------------------------------------------------------------------------
	/** 用来保存所在存在的Activity */
	private static ArrayList<BaseActivity> onlineActivityList = new ArrayList<BaseActivity>();

	/** 依次退出当前存在的所有Activity */
	public static void finishAll() {
		Iterator<BaseActivity> iterator = onlineActivityList.iterator();
		while (iterator.hasNext()) {
			iterator.next().finish();
		}
	}

	// --------------------------------初始化ActionBar-------------------------------------------------
	protected void initActionBar(String title, int leftResID, int rightResID, OnClickListener listener) {
		try {
			ActionBarView actionBar = (ActionBarView) findViewById(R.id.actionBar);
			actionBar.initActionBar(title, leftResID, rightResID, listener);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// -----------------Activity跳转及结束-------------------------------------------------------
	protected void startActivity(Class<?> targetClass) {
		Intent intent = new Intent(this, targetClass);
		startActivity(intent);
	}

	protected void startActivity(Class<?> targetClass, Bundle bundle) {
		Intent intent = new Intent(this, targetClass);
		intent.putExtras(bundle);
		startActivity(intent);
	}
	/**
	 * 切换Activity动画效果
	 * overridePendingTransition(R.anim.fade, R.anim.hold);//淡入淡出
	 * overridePendingTransition(R.anim.my_scale_action,R.anim.my_alpha_action);//放大淡出
	 * overridePendingTransition(R.anim.scale_rotate,R.anim.my_alpha_action);//转动淡出1
	 * overridePendingTransition(R.anim.scale_translate_rotate,R.anim.my_alpha_action);//转动淡出2
	 * overridePendingTransition(R.anim.scale_translate,R.anim.my_alpha_action);//左上角展开淡出效果
	 * overridePendingTransition(R.anim.hyperspace_in,R.anim.hyperspace_out);//压缩变小淡出效果
	 * overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);//右往左推出效果
	 * overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);//下往上推出效果
	 * overridePendingTransition(R.anim.slide_left,R.anim.slide_right);//左右交叉效果
	 * overridePendingTransition(R.anim.zoom_enter,R.anim.zoom_exit);//缩小效果
	 * overridePendingTransition(R.anim.slide_up_in,R.anim.slide_down_out);//上下文交错
	 */
	protected void startActivity(Class<?> targetClass, int inAnimID, int outAnimID) {
		Intent intent = new Intent(this, targetClass);
		startActivity(intent);
		overridePendingTransition(inAnimID, outAnimID);
	}

	protected void startActivity(Class<?> targetClass, int inAnimID, int outAnimID, Bundle bundle) {
		Intent intent = new Intent(this, targetClass);
		intent.putExtras(bundle);
		startActivity(intent);
		overridePendingTransition(inAnimID, outAnimID);
	}

	@Override
	public void finish() {
		super.finish();
	}

	// ------------------------Handler---------------------------------------------------------
	protected Handler mainHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			myHandleMessage(msg);
		};
	};

	protected void myHandleMessage(Message msg) {
	};

	// -----------------生命周期的管理，监控-------------------------------------------------
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		LogUtil.d(this, "onCreate");
		onlineActivityList.add(this);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		LogUtil.d(this, "onDestroy");
		onlineActivityList.remove(this);
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		LogUtil.d(this, "onRestart");
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		LogUtil.d(this, "onStart");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		LogUtil.d(this, "onResume");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		LogUtil.d(this, "onPause");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		LogUtil.d(this, "onStop");
	}
}