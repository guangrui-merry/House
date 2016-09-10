package edu.feicui.app.phone.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ToggleButton;
import edu.feicui.app.phone.base.BaseActivity;
import edu.feicui.app.phone.base.util.NotificationUtil;

/**
 * 设置界面
 * 
 * @author dell
 * 
 */
public class SettingActivity extends BaseActivity {

	private ToggleButton tb_notif;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		// 初始化ActionBar @see super class ActionBarActivity
		String title = getResources().getString(R.string.setting);
		initActionBar(title, R.drawable.btn_homeasup_default, -1, clickListener);
		// 初始化主按钮(开机启动，通知图标，消息推送,关于我们.....)
		initMainButton();
	}

	private void initMainButton() {
		tb_notif = (ToggleButton) findViewById(R.id.tb_toggle_notification);
		tb_notif.setChecked(NotificationUtil.isOpenNotification(getApplicationContext()));
		tb_notif.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					NotificationUtil.showAppIconNotification(getApplicationContext());
				} else {
					NotificationUtil.cancelAppIconNotification(getApplicationContext());
				}
			}
		});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		NotificationUtil.setOpenNotification(getApplicationContext(), tb_notif.isChecked());
	}

	/** @see android:onClick=hitSettingitem */
	public void hitSettingitem(View view) {
		int viewID = view.getId();
		switch (viewID) {
		case R.id.rl_setting_about:
			// 用来判断是从哪里进入的关于我们界面
			Bundle bundle = new Bundle();
			bundle.putString("className", SettingActivity.this.getClass().getSimpleName());
			startActivity(AboutActivity.class, bundle);
			break;
		case R.id.rl_setting_help:
			SharedPreferences preferences = getSharedPreferences("lead_config", Context.MODE_PRIVATE);
			Editor editor = preferences.edit();
			editor.putBoolean("isFirstRun", true);
			editor.commit();
			// 用来判断是从哪里进入的关于我们界面
			Bundle bundle2 = new Bundle();
			bundle2.putString("className", SettingActivity.this.getClass().getSimpleName());
			startActivity(LeadActivity.class, bundle2);
			break;
		default:
			break;
		}
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
			}
		}
	};
}
