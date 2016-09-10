package edu.feicui.app.phone.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import edu.feicui.app.phone.base.BaseActivity;
import edu.feicui.app.phone.biz.AppInfoManager;
import edu.feicui.app.phone.biz.MemoryManager;
import edu.feicui.app.phone.view.ClearArcView;

/**0
 * ��ҳ��
 * 
 * @author dell
 * 
 */
public class HomeActivity extends BaseActivity {

	private View view_homeclear; // һ������Բ
	private View view_homeclear_text; // һ�������ı�
	private ClearArcView view_homeclear_arc;// һ������Բ��(�Զ���)
	private TextView view_homeclear_score;// һ������

	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		// ��ʼ��ActionBar @see super class ActionBarActivity
		String title = getResources().getString(R.string.app_name);
		initActionBar(title, R.drawable.ic_launcher, R.drawable.ic_child_configs, clickListener);
		// ��ʼ������ؼ�
		initHomeClear();
		// ��ʼ��Clear����
		initHomeClearData();
	}

	private void initHomeClear() {
		view_homeclear = findViewById(R.id.iv_homeclear);
		view_homeclear_text = findViewById(R.id.tv_homeclear_text);
		view_homeclear_score = (TextView) findViewById(R.id.tv_score);
		view_homeclear_arc = (ClearArcView) findViewById(R.id.homeclear_arc);
		view_homeclear.setOnClickListener(clickListener);
		view_homeclear_text.setOnClickListener(clickListener);
	}

	private void initHomeClearData() {
		// ��ȡ��ȫ�������ڴ�
		float totalRam = MemoryManager.getPhoneTotalRamMemory();
		// ��ȡ�����������ڴ�
		float freeRam = MemoryManager.getPhoneFreeRamMemory(getApplicationContext());
		// ��ȡ����ʹ�������ڴ�
		float usedRam = totalRam - freeRam;
		// �������ʹ�������ڴ����
		float usedP = usedRam / totalRam;
		int used100 = (int) (usedP * 100); // �������ʹ�������ڴ�ٷֱ�
		view_homeclear_score.setText(used100 + "");
		int angle = (int) (usedP * 360); // �������ʹ�������ڴ�Ƕ�
		view_homeclear_arc.setAngleWithAnim(angle);
	}

	/** @see android:onClick=hitHomeitem */
	public void hitHomeitem(View view) {
		int viewID = view.getId();
		switch (viewID) {
		case R.id.ll_rocket:
			startActivity(SpeedupActivity.class, R.anim.in_down, R.anim.out_up);
			break;
		case R.id.ll_softmgr:
			startActivity(SoftmgrActivity.class, R.anim.in_down, R.anim.out_up);
			break;
		case R.id.ll_phonemgr:
			startActivity(PhonemgrActivity.class, R.anim.in_down, R.anim.out_up);
			break;
		case R.id.ll_telmgr:
			startActivity(TelmgrActivity.class, R.anim.in_down, R.anim.out_up);
			break;
		case R.id.ll_filemgr:
			startActivity(FilemgrActivity.class, R.anim.in_down, R.anim.out_up);
			break;
		case R.id.ll_sdclean:
			startActivity(ClearActivity.class, R.anim.in_down, R.anim.out_up);
			break;
		}
	}

	private View.OnClickListener clickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			int viewID = v.getId();
			switch (viewID) {
			case R.id.iv_homeclear:
			case R.id.tv_homeclear_text:
				// ���������������г���
				AppInfoManager.getAppInfoManager(HomeActivity.this).killALLProcesses();
				// ���³�ʼ�ؼ�����
				initHomeClearData();
				break;
			case R.id.iv_left:
				startActivity(AboutActivity.class, R.anim.in_left, R.anim.out_right);
				break;
			case R.id.iv_right:
				startActivity(SettingActivity.class, R.anim.in_right, R.anim.out_left);
				break;
			}
		}
	};
	
}
