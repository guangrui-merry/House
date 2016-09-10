package edu.feicui.app.phone.activity;

import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import edu.feicui.app.phone.adapter.RuningAppAdapter;
import edu.feicui.app.phone.base.BaseActivity;
import edu.feicui.app.phone.base.util.CommonUtil;
import edu.feicui.app.phone.biz.AppInfoManager;
import edu.feicui.app.phone.biz.MemoryManager;
import edu.feicui.app.phone.biz.SystemManager;
import edu.feicui.app.phone.entity.RuningAppInfo;

/**
 * 手机加速页面
 */
public class SpeedupActivity extends BaseActivity {

	private ListView runingApplistView;
	private RuningAppAdapter runingAppAdapter;

	private Button btn_clear; // 一键清理
	private CheckBox cb_checkClearAll; // 全选
	private Button btn_showall; // 显示app(系统?用户)
	private ProgressBar progressBarArc; // 加载中ui
	private TextView tv_ramMessage; // 运行内存文本
	private ProgressBar pb_ram; // 运行内存
	private TextView tv_phoneName;
	private TextView tv_phoneModle;

	private Map<Integer, List<RuningAppInfo>> runingAppinfos = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_speedup);
		// 初始化ActionBar @see super class ActionBarActivity
		String title = getResources().getString(R.string.setting);
		initActionBar(title, R.drawable.btn_homeasup_default, -1, clickListener);
		// 运行中应用程序列表控件
		runingAppAdapter = new RuningAppAdapter(this);
		runingApplistView = (ListView) findViewById(R.id.listviewLoad);
		runingApplistView.setAdapter(runingAppAdapter);
		// 初始化主操作控件(一键清理,全选,加载中ui)
		initMainButton();
		loadData();
	}

	private void initMainButton() {
		tv_phoneName = (TextView) findViewById(R.id.tv_phoneName);
		tv_phoneModle = (TextView) findViewById(R.id.tv_phoneModel);
		pb_ram = (ProgressBar) findViewById(R.id.pb_ram);
		tv_ramMessage = (TextView) findViewById(R.id.tv_ramMessage);
		progressBarArc = (ProgressBar) findViewById(R.id.progressBar);
		cb_checkClearAll = (CheckBox) findViewById(R.id.cb_all);
		btn_clear = (Button) findViewById(R.id.btn_onekeyClear);
		btn_showall = (Button) findViewById(R.id.btn_showapp);
		btn_clear.setOnClickListener(clickListener);
		btn_showall.setOnClickListener(clickListener);
		cb_checkClearAll.setOnCheckedChangeListener(changeListener);
		initPhoneData();
		initRamData();
	}

	private void initPhoneData() {
		tv_phoneName.setText(SystemManager.getPhoneName().toUpperCase());
		tv_phoneModle.setText(SystemManager.getPhoneModelName());
	}

	private void initRamData() {
		// 获取到全部运行内存
		float totalRam = MemoryManager.getPhoneTotalRamMemory();
		// 获取到空闲运行内存
		float freeRam = MemoryManager.getPhoneFreeRamMemory(getApplicationContext());
		// 获取到已使用运行内存
		float usedRam = totalRam - freeRam;
		// 计算出已使用运行内存比例
		float usedP = usedRam / totalRam;
		int used100 = (int) (usedP * 100); // 计算出已使用运行内存百分比
		pb_ram.setMax(100);
		pb_ram.setProgress(used100);
		tv_ramMessage.setText("已用内存：" + CommonUtil.getFileSize((long) usedRam) + "/" + CommonUtil.getFileSize((long) totalRam));
	}

	private void loadData() {
		progressBarArc.setVisibility(View.VISIBLE);
		runingApplistView.setVisibility(View.INVISIBLE);
		// 新开线程执行加载操作
		new Thread(new Runnable() {
			@Override
			public void run() {
				// 获取所有正在运行进程
				runingAppinfos = AppInfoManager.getAppInfoManager(getApplicationContext()).getRuningAppInfos();
				// 到UI线程执行更新、修改UI的操作
				// runOnUiThread()?Handler?AsyncTask<Params, Progress, Result>
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						initRamData();
						progressBarArc.setVisibility(View.INVISIBLE);
						runingApplistView.setVisibility(View.VISIBLE);
						// 默认只需显示出非系统的进程
						runingAppAdapter.setDataToAdapter(runingAppinfos.get(AppInfoManager.RUNING_APP_TYPE_USER));
						runingAppAdapter.setState(RuningAppAdapter.STATE_SHOW_USER);
						runingAppAdapter.notifyDataSetChanged();
					}
				});
			}
		}).start();
	}

	private OnCheckedChangeListener changeListener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			// 将运行中应用程序列表全选
			List<RuningAppInfo> appInfos = runingAppAdapter.getDataList();
			for (RuningAppInfo appInfo : appInfos) {
				appInfo.setClear(isChecked);
			}
			// 更新适配器
			runingAppAdapter.notifyDataSetChanged();
		}
	};

	private View.OnClickListener clickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			int viewID = v.getId();
			switch (viewID) {
			case R.id.iv_left:
				startActivity(HomeActivity.class);
				finish();
				break;
			case R.id.btn_onekeyClear:
				// kill选中的
				List<RuningAppInfo> appInfos = runingAppAdapter.getDataList();
				for (RuningAppInfo appInfo : appInfos) {
					if (appInfo.isClear()) {
						String packageName = appInfo.getPackageName();
						AppInfoManager.getAppInfoManager(getApplicationContext()).killProcesses(packageName);
					}
				}
				// 重新加载刷新数据
				loadData();
				cb_checkClearAll.setChecked(false);
				break;
			case R.id.btn_showapp:
				if (runingAppinfos != null) {
					switch (runingAppAdapter.getState()) {
					// 在显示用户应用状态下
					case RuningAppAdapter.STATE_SHOW_USER:
						runingAppAdapter.setDataToAdapter(runingAppinfos.get(AppInfoManager.RUNING_APP_TYPE_SYS));
						runingAppAdapter.setState(RuningAppAdapter.STATE_SHOW_ALL);
						btn_showall.setText(getResources().getString(R.string.speedup_show_userapp));
						break;
					// 在显示所用应用状态下
					case RuningAppAdapter.STATE_SHOW_ALL:
						runingAppAdapter.setDataToAdapter(runingAppinfos.get(AppInfoManager.RUNING_APP_TYPE_USER));
						runingAppAdapter.setState(RuningAppAdapter.STATE_SHOW_USER);
						btn_showall.setText(getResources().getString(R.string.speedup_show_sysapp));
						break;
					}
					runingAppAdapter.notifyDataSetChanged();
				}
				break;
			default:
				break;
			}
		}
	};
}
