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
 * �ֻ�����ҳ��
 */
public class SpeedupActivity extends BaseActivity {

	private ListView runingApplistView;
	private RuningAppAdapter runingAppAdapter;

	private Button btn_clear; // һ������
	private CheckBox cb_checkClearAll; // ȫѡ
	private Button btn_showall; // ��ʾapp(ϵͳ?�û�)
	private ProgressBar progressBarArc; // ������ui
	private TextView tv_ramMessage; // �����ڴ��ı�
	private ProgressBar pb_ram; // �����ڴ�
	private TextView tv_phoneName;
	private TextView tv_phoneModle;

	private Map<Integer, List<RuningAppInfo>> runingAppinfos = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_speedup);
		// ��ʼ��ActionBar @see super class ActionBarActivity
		String title = getResources().getString(R.string.setting);
		initActionBar(title, R.drawable.btn_homeasup_default, -1, clickListener);
		// ������Ӧ�ó����б�ؼ�
		runingAppAdapter = new RuningAppAdapter(this);
		runingApplistView = (ListView) findViewById(R.id.listviewLoad);
		runingApplistView.setAdapter(runingAppAdapter);
		// ��ʼ���������ؼ�(һ������,ȫѡ,������ui)
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
		// ��ȡ��ȫ�������ڴ�
		float totalRam = MemoryManager.getPhoneTotalRamMemory();
		// ��ȡ�����������ڴ�
		float freeRam = MemoryManager.getPhoneFreeRamMemory(getApplicationContext());
		// ��ȡ����ʹ�������ڴ�
		float usedRam = totalRam - freeRam;
		// �������ʹ�������ڴ����
		float usedP = usedRam / totalRam;
		int used100 = (int) (usedP * 100); // �������ʹ�������ڴ�ٷֱ�
		pb_ram.setMax(100);
		pb_ram.setProgress(used100);
		tv_ramMessage.setText("�����ڴ棺" + CommonUtil.getFileSize((long) usedRam) + "/" + CommonUtil.getFileSize((long) totalRam));
	}

	private void loadData() {
		progressBarArc.setVisibility(View.VISIBLE);
		runingApplistView.setVisibility(View.INVISIBLE);
		// �¿��߳�ִ�м��ز���
		new Thread(new Runnable() {
			@Override
			public void run() {
				// ��ȡ�����������н���
				runingAppinfos = AppInfoManager.getAppInfoManager(getApplicationContext()).getRuningAppInfos();
				// ��UI�߳�ִ�и��¡��޸�UI�Ĳ���
				// runOnUiThread()?Handler?AsyncTask<Params, Progress, Result>
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						initRamData();
						progressBarArc.setVisibility(View.INVISIBLE);
						runingApplistView.setVisibility(View.VISIBLE);
						// Ĭ��ֻ����ʾ����ϵͳ�Ľ���
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
			// ��������Ӧ�ó����б�ȫѡ
			List<RuningAppInfo> appInfos = runingAppAdapter.getDataList();
			for (RuningAppInfo appInfo : appInfos) {
				appInfo.setClear(isChecked);
			}
			// ����������
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
				// killѡ�е�
				List<RuningAppInfo> appInfos = runingAppAdapter.getDataList();
				for (RuningAppInfo appInfo : appInfos) {
					if (appInfo.isClear()) {
						String packageName = appInfo.getPackageName();
						AppInfoManager.getAppInfoManager(getApplicationContext()).killProcesses(packageName);
					}
				}
				// ���¼���ˢ������
				loadData();
				cb_checkClearAll.setChecked(false);
				break;
			case R.id.btn_showapp:
				if (runingAppinfos != null) {
					switch (runingAppAdapter.getState()) {
					// ����ʾ�û�Ӧ��״̬��
					case RuningAppAdapter.STATE_SHOW_USER:
						runingAppAdapter.setDataToAdapter(runingAppinfos.get(AppInfoManager.RUNING_APP_TYPE_SYS));
						runingAppAdapter.setState(RuningAppAdapter.STATE_SHOW_ALL);
						btn_showall.setText(getResources().getString(R.string.speedup_show_userapp));
						break;
					// ����ʾ����Ӧ��״̬��
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
