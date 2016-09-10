package edu.feicui.app.phone.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import edu.feicui.app.phone.adapter.PhonemgrAdapter;
import edu.feicui.app.phone.base.BaseActivity;
import edu.feicui.app.phone.base.util.CommonUtil;
import edu.feicui.app.phone.biz.MemoryManager;
import edu.feicui.app.phone.biz.PhoneManager;
import edu.feicui.app.phone.entity.PhoneInfo;

/**
 * 手机检测界面
 */
public class PhonemgrActivity extends BaseActivity {
	
	private View layout_battery; // 电池电量整体布局
	private ListView exListView; // 　手检测信息
	private PhonemgrAdapter phonemgrAdapter;
	private TextView tv_battery; // 电池电量百分比
	private ProgressBar pb_battery;// 电池电量进度
	private BatteryBroadcastReceiver broadcastReceiver;// 电池电量广播接收器
	private ProgressBar pb_loading;

	private int currentBattery; // 当前电量
	private int temperatureBattery;// 电池温度

	@Override    
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phonemgr);
		// 初始化ActionBar @see super class ActionBarActivity
		String title = getResources().getString(R.string.phonemgr);
		initActionBar(title, R.drawable.btn_homeasup_default, -1, clickListener);
		//
		initMainButton();
		//
		exListView = (ListView) findViewById(R.id.listviewLoad);
		phonemgrAdapter = new PhonemgrAdapter(this);
		exListView.setAdapter(phonemgrAdapter);
		// 初始化手机检测信息
		new Thread(new Runnable() {
			@Override
			public void run() {
				initAdapterData();
			}
		}).start();
	}

	private void initMainButton() {
		layout_battery = findViewById(R.id.ll_layout_battery);
		layout_battery.setOnClickListener(clickListener);
		tv_battery = (TextView) findViewById(R.id.tv_battery);
		pb_battery = (ProgressBar) findViewById(R.id.pb_battery);
		pb_loading = (ProgressBar) findViewById(R.id.progressBar);
		// 注册电池电量广播接收器(放在控件findView后面)
		broadcastReceiver = new BatteryBroadcastReceiver();
		IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		registerReceiver(broadcastReceiver, filter);
	}

	/**
	 * 获取手机硬件信息并绑定在adapter中，在ListView中显示
	 */
	public void initAdapterData() {
		pb_loading.setVisibility(View.VISIBLE);
		exListView.setVisibility(View.INVISIBLE);
		PhoneManager manager = PhoneManager.getPhoneManage(this);
		String title;
		String text;
		Drawable icon;
		//
		title = "设备名称:" + manager.getPhoneName1();
		text = "系统版本:" + manager.getPhoneSystemVersion();
		icon = getResources().getDrawable(R.drawable.setting_info_icon_version);
		final PhoneInfo info1 = new PhoneInfo(title, text, icon);
		//
		title = "全部运行内存" + CommonUtil.getFileSize(MemoryManager.getPhoneTotalRamMemory());
		text = "剩余运行内存" + CommonUtil.getFileSize(MemoryManager.getPhoneFreeRamMemory(this));
		icon = getResources().getDrawable(R.drawable.setting_info_icon_space);
		final PhoneInfo info2 = new PhoneInfo(title, text, icon);
		//
		title = "cpu名称:" + manager.getPhoneCpuName();
		text = "cpu数量:" + manager.getPhoneCpuNumber();
		icon = getResources().getDrawable(R.drawable.setting_info_icon_cpu);
		final PhoneInfo info3 = new PhoneInfo(title, text, icon);
		//
		title = "手机分辩率:" + manager.getResolution();
		text = "相机分辩率:" + manager.getMaxPhotoSize(); 
		icon = getResources().getDrawable(R.drawable.setting_info_icon_camera);
		final PhoneInfo info4 = new PhoneInfo(title, text, icon);
		//
		title = "基带版本:" + manager.getPhoneSystemBasebandVersion();
		text = "是否ROOT:" + (manager.isRoot() ? "是" : "否");
		icon = getResources().getDrawable(R.drawable.setting_info_icon_root);
		final PhoneInfo info5 = new PhoneInfo(title, text, icon);
		//
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				phonemgrAdapter.addDataToAdapter(info1);
				phonemgrAdapter.addDataToAdapter(info2);
				phonemgrAdapter.addDataToAdapter(info3);
				phonemgrAdapter.addDataToAdapter(info4);
				phonemgrAdapter.addDataToAdapter(info5);
				phonemgrAdapter.notifyDataSetChanged();
				pb_loading.setVisibility(View.INVISIBLE);
				exListView.setVisibility(View.VISIBLE);
			}
		});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(broadcastReceiver);
	}

	private View.OnClickListener clickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			int viewID = v.getId();
			switch (viewID) {
			case R.id.iv_left:
				startActivity(HomeActivity.class);
				finish();
				break;
			case R.id.ll_layout_battery:
				showBatteryMessage();
				break;
			}
		}
	};

	private void showBatteryMessage() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("电池电量信息");
		builder.setItems(new String[] { "当前电量：" + currentBattery, "电池温度：" + temperatureBattery }, null);
		builder.show();
	}

	public class BatteryBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
				Bundle bundle = intent.getExtras();
				int maxBattery = (Integer) bundle.get(BatteryManager.EXTRA_SCALE); // 总电量
				currentBattery = (Integer) bundle.get(BatteryManager.EXTRA_LEVEL); // 当前电量
				temperatureBattery = (Integer) bundle.get(BatteryManager.EXTRA_TEMPERATURE); // 电池温度
				pb_battery.setMax(maxBattery);
				pb_battery.setProgress(currentBattery);
				int current100 = currentBattery * 100 / maxBattery;
				tv_battery.setText(current100 + "%");
			}
		}
	}
}
