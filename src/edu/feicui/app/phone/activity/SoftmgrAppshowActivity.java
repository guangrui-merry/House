package edu.feicui.app.phone.activity;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import edu.feicui.app.phone.adapter.AppAdapter;
import edu.feicui.app.phone.base.BaseActivity;
import edu.feicui.app.phone.biz.AppInfoManager;
import edu.feicui.app.phone.entity.AppInfo;

/**
 * 显示（所有，系统，用户）软件列表界面 --------------------
 *  del app 的系统广播 action
 */
public class SoftmgrAppshowActivity extends BaseActivity {
	private int id;
	private ListView appListView;
	private AppAdapter appAdapter;
	private ProgressBar progressBarArc; // 加载中ui
	private CheckBox cb_delall;
	private Button btn_delall;

	private AppDelRecevice appDelRecevice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phonemgrshowapp);
		int id = getIntent().getIntExtra("id", R.id.rl_soft_all);
		// 初始化ActionBar @see super class ActionBarActivity
		String title = getResources().getString(R.string.softmgr);
		switch (id) {
		case R.id.rl_soft_all:
			title = getResources().getString(R.string.allsoft);
			break;
		case R.id.rl_soft_sys:
			title = getResources().getString(R.string.syssoft);
			break;
		case R.id.rl_soft_use:
			title = getResources().getString(R.string.usesoft);
			break;
		}
		this.id = id;
		initActionBar(title, R.drawable.btn_homeasup_default, -1, clickListener);
		//
		appAdapter = new AppAdapter(this);
		appListView = (ListView) findViewById(R.id.listviewLoad);
		appListView.setAdapter(appAdapter);
		appListView.setOnScrollListener(onScrollListener); // 监听滑动状态(快速滑动不加载显示图像)
		
		progressBarArc = (ProgressBar) findViewById(R.id.progressBar);
		btn_delall = (Button) findViewById(R.id.btn_delall);
		cb_delall = (CheckBox) findViewById(R.id.cb_all);
		cb_delall.setOnCheckedChangeListener(changeListener);
		btn_delall.setOnClickListener(clickListener);
		asynLoadApp();

		// 注册广播接收器
		appDelRecevice = new AppDelRecevice();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		filter.addDataScheme("package");
		filter.addAction(AppDelRecevice.ACTION_APPDEL);
		registerReceiver(appDelRecevice, filter);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(appDelRecevice);
	}

	private List<AppInfo> appInfos = null;

	private void asynLoadApp() {
		progressBarArc.setVisibility(View.VISIBLE);
		appListView.setVisibility(View.INVISIBLE);
		new Thread(new Runnable() {
			@Override
			public void run() {
				switch (id) {
				case R.id.rl_soft_all:
					appInfos = AppInfoManager.getAppInfoManager(getApplicationContext()).getAllPackageInfo(true);
					break;
				case R.id.rl_soft_sys:
					appInfos = AppInfoManager.getAppInfoManager(getApplicationContext()).getSystemPackageInfo(true);
					break;
				case R.id.rl_soft_use:
					appInfos = AppInfoManager.getAppInfoManager(getApplicationContext()).getUserPackageInfo(true);
					break;
				}
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						progressBarArc.setVisibility(View.INVISIBLE);
						appListView.setVisibility(View.VISIBLE);
						appAdapter.setDataToAdapter(appInfos);
						appAdapter.notifyDataSetChanged();
					}
				});
			}
		}).start();

	}

	private OnScrollListener onScrollListener = new OnScrollListener() {
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			AppAdapter appListAdapter = (AppAdapter) appListView.getAdapter();
			switch (scrollState) {
			case OnScrollListener.SCROLL_STATE_FLING: // 快速滑动时
				appListAdapter.setFling(true);
				break;
			case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:// 滑动时
				appListAdapter.setFling(false);
				break;
			case OnScrollListener.SCROLL_STATE_IDLE: // 停止滑动时
				appListAdapter.setFling(false);
				appListAdapter.notifyDataSetChanged();
				break;
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			// TODO Auto-generated method stub
		}
	};

	private OnCheckedChangeListener changeListener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			// 全选
			AppAdapter appListAdapter = (AppAdapter) appListView.getAdapter();
			List<AppInfo> appInfos = appListAdapter.getDataList();
			for (AppInfo appInfo : appInfos) {
				appInfo.setDel(arg1);
			}
			appAdapter.notifyDataSetChanged();
		}
	};
	
	private View.OnClickListener clickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			int viewID = v.getId();
			switch (viewID) {
			case R.id.iv_left:
				finish();
				break;
			case R.id.btn_delall:
				AppAdapter appListAdapter = (AppAdapter) appListView.getAdapter();
				List<AppInfo> appInfos = appListAdapter.getDataList();
				for (AppInfo appInfo : appInfos) {
					if (appInfo.isDel()) {
						String packageName = appInfo.getPackageInfo().packageName;
						Intent intent = new Intent(Intent.ACTION_DELETE);
						intent.setData(Uri.parse("package:" + packageName));
						startActivity(intent);
					}
				}
				break;
			}
		}
	};

	/** 应用删除广播接收器 */
	public class AppDelRecevice extends BroadcastReceiver {
		public static final String ACTION_APPDEL = "com.androidy.app.phone.del";

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			if (action.equals(Intent.ACTION_PACKAGE_REMOVED) || action.equals(ACTION_APPDEL)) {
				asynLoadApp();
			}
		}
	}
	
	
}
