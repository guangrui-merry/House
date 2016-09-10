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
 * Activity������
 */
public abstract class BaseActivity extends Activity {

	// --------------------------------------------------------------------------------------
	/** �����������ڴ��ڵ�Activity */
	private static ArrayList<BaseActivity> onlineActivityList = new ArrayList<BaseActivity>();

	/** �����˳���ǰ���ڵ�����Activity */
	public static void finishAll() {
		Iterator<BaseActivity> iterator = onlineActivityList.iterator();
		while (iterator.hasNext()) {
			iterator.next().finish();
		}
	}

	// --------------------------------��ʼ��ActionBar-------------------------------------------------
	protected void initActionBar(String title, int leftResID, int rightResID, OnClickListener listener) {
		try {
			ActionBarView actionBar = (ActionBarView) findViewById(R.id.actionBar);
			actionBar.initActionBar(title, leftResID, rightResID, listener);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// -----------------Activity��ת������-------------------------------------------------------
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
	 * �л�Activity����Ч��
	 * overridePendingTransition(R.anim.fade, R.anim.hold);//���뵭��
	 * overridePendingTransition(R.anim.my_scale_action,R.anim.my_alpha_action);//�Ŵ󵭳�
	 * overridePendingTransition(R.anim.scale_rotate,R.anim.my_alpha_action);//ת������1
	 * overridePendingTransition(R.anim.scale_translate_rotate,R.anim.my_alpha_action);//ת������2
	 * overridePendingTransition(R.anim.scale_translate,R.anim.my_alpha_action);//���Ͻ�չ������Ч��
	 * overridePendingTransition(R.anim.hyperspace_in,R.anim.hyperspace_out);//ѹ����С����Ч��
	 * overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);//�������Ƴ�Ч��
	 * overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);//�������Ƴ�Ч��
	 * overridePendingTransition(R.anim.slide_left,R.anim.slide_right);//���ҽ���Ч��
	 * overridePendingTransition(R.anim.zoom_enter,R.anim.zoom_exit);//��СЧ��
	 * overridePendingTransition(R.anim.slide_up_in,R.anim.slide_down_out);//�����Ľ���
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

	// -----------------�������ڵĹ������-------------------------------------------------
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