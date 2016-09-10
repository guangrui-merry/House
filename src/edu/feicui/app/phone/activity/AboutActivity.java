package edu.feicui.app.phone.activity;

import android.os.Bundle;
import android.view.View;
import edu.feicui.app.phone.base.BaseActivity;

/**
 * �������ǽ���
 * 
 * @author dell
 * 
 */
public class AboutActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		// ��ʼ��ActionBar @see super class ActionBarActivity
		String title = getResources().getString(R.string.about);
		initActionBar(title, R.drawable.btn_homeasup_default, -1, clickListener);
	}

	private View.OnClickListener clickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			int viewID = v.getId();
			switch (viewID) {
			case R.id.iv_left:
				// ��ȡ��������
				String fromClassName = getIntent().getStringExtra("className");
				// ��ֵĬ���Ǵ���ҳ������
				if (fromClassName == null || fromClassName.equals("")) {
					startActivity(HomeActivity.class);
					finish();
					return;
				}
				// ������ҳ������
				if (fromClassName.equals(SettingActivity.class.getSimpleName())) {
					startActivity(SettingActivity.class);
				} else {
					startActivity(HomeActivity.class);
				}
				finish();
				break;
			default:
				break;
			}
		}
	};
}
