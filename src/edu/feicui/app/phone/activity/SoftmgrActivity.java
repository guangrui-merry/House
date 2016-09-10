package edu.feicui.app.phone.activity;

import java.text.DecimalFormat;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import edu.feicui.app.phone.base.BaseActivity;
import edu.feicui.app.phone.base.util.CommonUtil;
import edu.feicui.app.phone.biz.MemoryManager;
import edu.feicui.app.phone.view.PiechartView;

/**
 * �������ҳ��
 * 
 * @author dell
 * 
 */
public class SoftmgrActivity extends BaseActivity {

	private PiechartView piechartView; // ����ͼ��ͼ(�Զ���ؼ�)
	private ProgressBar phoneSpace; // �ֻ��ռ������
	private ProgressBar sdcardSpace;// sdcard�ռ������
	private TextView phoneSpaceMsg;// �ֻ��ռ���Ϣ ����/ȫ��
	private TextView sdcardSpaceMsg;// sdcard�ռ���Ϣ ����/ȫ��

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_softmgr);
		// ��ʼ��ActionBar @see super class ActionBarActivity
		String title = getResources().getString(R.string.softmgr);
		initActionBar(title, R.drawable.btn_homeasup_default, -1, clickListener);
		// ��ʼ������ť(ȫ����ϵͳ���û�)
		initMainButton();
		// ��ʼ���ռ���ؿؼ�
		initSpace();
		// ��ʼ���ռ���ؿؼ�������
		initSpaceData();
	}

	private void initMainButton() {
	}

	private void initSpace() {
		piechartView = (PiechartView) findViewById(R.id.piechart);
		phoneSpace = (ProgressBar) findViewById(R.id.pb_phone_space);
		sdcardSpace = (ProgressBar) findViewById(R.id.pb_sdcard_space);
		phoneSpaceMsg = (TextView) findViewById(R.id.tv_phone_space_msg);
		sdcardSpaceMsg = (TextView) findViewById(R.id.tv_sdcard_space_msg);
	}

	private void initSpaceData() {
		// �ֻ�����ռ�
		long phoneSelfTolal = MemoryManager.getPhoneSelfSize();
		long phoneSelfUnused = MemoryManager.getPhoneSelfFreeSize();
		long phoneSelfUsed = phoneSelfTolal - phoneSelfUnused;
		// �ֻ��Դ�sdcard�ռ�
		long phoneSelfSDCardTolal = MemoryManager.getPhoneSelfSDCardSize();
		long phoneSelfSDCardUnused = MemoryManager.getPhoneSelfSDCardFreeSize();
		long phoneSelfSDCardUsed = phoneSelfSDCardTolal - phoneSelfSDCardUnused;
		// �ֻ����ô洢���ռ�
		long phoneOutSDCardTolat = MemoryManager.getPhoneOutSDCardSize(getApplicationContext());
		long phoneOutSDCardUnused = MemoryManager.getPhoneOutSDCardFreeSize(getApplicationContext());
		long phoneOutSDCradUsed = phoneOutSDCardTolat - phoneOutSDCardUnused;
		// �ֻ��ܿռ�
		float phoneAllSpace = phoneSelfTolal + phoneSelfSDCardTolal + phoneOutSDCardTolat;
		// �������
		float phoneSpaceF = (phoneSelfTolal + phoneSelfSDCardTolal) / phoneAllSpace;
		float sdcardSpaceF = phoneOutSDCardTolat / phoneAllSpace;
		// ������ռ����,С�������λ
		DecimalFormat df = new DecimalFormat("#.00");
		phoneSpaceF = Float.parseFloat(df.format(phoneSpaceF));
		sdcardSpaceF = Float.parseFloat(df.format(sdcardSpaceF));
		// ���ñ���ͼ����
		piechartView.setPiechartProportionWithAnim(phoneSpaceF, sdcardSpaceF);
		// �����ֻ��ʹ洢����ʹ���ڴ��ȫ���ڴ�
		long phoneTolatSpace = phoneSelfTolal + phoneSelfSDCardTolal;
		// long phoneTolatSpace = MemoryManager.getPhoneAllSize();
		long phoneUnusedSpace = phoneSelfUnused + phoneSelfSDCardUnused;
		// long phoneUnusedSpace = MemoryManager.getPhoneAllFreeSize();
		long phoneUsedSpace = phoneTolatSpace - phoneUnusedSpace;
		// ���ÿռ�ʹ������ı�������/ȫ��
		phoneSpaceMsg.setText("����: " + CommonUtil.getFileSize(phoneUnusedSpace) + "/" + CommonUtil.getFileSize(phoneTolatSpace));
		sdcardSpaceMsg.setText("����: " + CommonUtil.getFileSize(phoneOutSDCardUnused) + "/" + CommonUtil.getFileSize(phoneOutSDCardTolat));
		// ���ÿռ�ʹ���������: ����/ȫ��
		phoneSpace.setMax((int) (phoneTolatSpace / 1024));
		phoneSpace.setProgress((int) (phoneUsedSpace / 1024));
		sdcardSpace.setMax((int) (phoneOutSDCardTolat / 1024));
		sdcardSpace.setProgress((int) (phoneOutSDCradUsed / 1024));
	}

	/** @see onclick=hitListitem */
	public void hitListitem(View view) {
		int viewID = view.getId();
		switch (viewID) {
		case R.id.rl_soft_all:
		case R.id.rl_soft_sys:
		case R.id.rl_soft_use:
			Bundle bundle = new Bundle();
			bundle.putInt("id", viewID);
			startActivity(SoftmgrAppshowActivity.class, bundle);
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
