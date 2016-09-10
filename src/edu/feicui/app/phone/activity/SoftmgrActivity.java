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
 * 软件管理页面
 * 
 * @author dell
 * 
 */
public class SoftmgrActivity extends BaseActivity {

	private PiechartView piechartView; // 饼形图视图(自定义控件)
	private ProgressBar phoneSpace; // 手机空间进度条
	private ProgressBar sdcardSpace;// sdcard空间进度条
	private TextView phoneSpaceMsg;// 手机空间信息 空闲/全部
	private TextView sdcardSpaceMsg;// sdcard空间信息 空闲/全部

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_softmgr);
		// 初始化ActionBar @see super class ActionBarActivity
		String title = getResources().getString(R.string.softmgr);
		initActionBar(title, R.drawable.btn_homeasup_default, -1, clickListener);
		// 初始化主按钮(全部，系统，用户)
		initMainButton();
		// 初始化空间相关控件
		initSpace();
		// 初始化空间相关控件的数据
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
		// 手机自身空间
		long phoneSelfTolal = MemoryManager.getPhoneSelfSize();
		long phoneSelfUnused = MemoryManager.getPhoneSelfFreeSize();
		long phoneSelfUsed = phoneSelfTolal - phoneSelfUnused;
		// 手机自带sdcard空间
		long phoneSelfSDCardTolal = MemoryManager.getPhoneSelfSDCardSize();
		long phoneSelfSDCardUnused = MemoryManager.getPhoneSelfSDCardFreeSize();
		long phoneSelfSDCardUsed = phoneSelfSDCardTolal - phoneSelfSDCardUnused;
		// 手机外置存储卡空间
		long phoneOutSDCardTolat = MemoryManager.getPhoneOutSDCardSize(getApplicationContext());
		long phoneOutSDCardUnused = MemoryManager.getPhoneOutSDCardFreeSize(getApplicationContext());
		long phoneOutSDCradUsed = phoneOutSDCardTolat - phoneOutSDCardUnused;
		// 手机总空间
		float phoneAllSpace = phoneSelfTolal + phoneSelfSDCardTolal + phoneOutSDCardTolat;
		// 计算比例
		float phoneSpaceF = (phoneSelfTolal + phoneSelfSDCardTolal) / phoneAllSpace;
		float sdcardSpaceF = phoneOutSDCardTolat / phoneAllSpace;
		// 保留所占比例,小数点后两位
		DecimalFormat df = new DecimalFormat("#.00");
		phoneSpaceF = Float.parseFloat(df.format(phoneSpaceF));
		sdcardSpaceF = Float.parseFloat(df.format(sdcardSpaceF));
		// 设置饼形图比例
		piechartView.setPiechartProportionWithAnim(phoneSpaceF, sdcardSpaceF);
		// 设置手机和存储卡已使用内存和全部内存
		long phoneTolatSpace = phoneSelfTolal + phoneSelfSDCardTolal;
		// long phoneTolatSpace = MemoryManager.getPhoneAllSize();
		long phoneUnusedSpace = phoneSelfUnused + phoneSelfSDCardUnused;
		// long phoneUnusedSpace = MemoryManager.getPhoneAllFreeSize();
		long phoneUsedSpace = phoneTolatSpace - phoneUnusedSpace;
		// 设置空间使用情况文本：可用/全部
		phoneSpaceMsg.setText("可用: " + CommonUtil.getFileSize(phoneUnusedSpace) + "/" + CommonUtil.getFileSize(phoneTolatSpace));
		sdcardSpaceMsg.setText("可用: " + CommonUtil.getFileSize(phoneOutSDCardUnused) + "/" + CommonUtil.getFileSize(phoneOutSDCardTolat));
		// 设置空间使用情况进度: 已用/全部
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
