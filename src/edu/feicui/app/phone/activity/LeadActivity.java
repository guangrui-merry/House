package edu.feicui.app.phone.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import edu.feicui.app.phone.base.BaseActivity;
import edu.feicui.app.phone.base.adapter.BasePagerAdapter;
import edu.feicui.app.phone.service.MusicService;

/**
 * ������������ҳ��
 */
public class LeadActivity extends BaseActivity implements OnClickListener {

	private TextView tv_skip;
	private ViewPager viewPager;
	private BasePagerAdapter leadPagerAdapter;
	private ImageView[] icons = new ImageView[3];

	private boolean isFromSetting; // �Ƿ���������ҳ��

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// �жϴ����������
		Intent intent = getIntent();
		String fromClassName = intent.getStringExtra("className");
		if (fromClassName != null && fromClassName.equals("SettingActivity")) {
			isFromSetting = true;
		}
		// ���������Ϣ���ж��Ƿ���Ҫ��ʾ���е�ǰ����ҳ��
		SharedPreferences preferences = getSharedPreferences("lead_config", Context.MODE_PRIVATE);
		boolean isFirstRun = preferences.getBoolean("isFirstRun", true);
		if (!isFirstRun) {
			startActivity(LogoActivity.class);
			finish();
		}
		// �ӵ�ǰ����ҳ�濪ʼִ��
		else {
			setContentView(R.layout.activity_lead);
			// ��ʼ������ҳ��Сͼ��+skip (5��Сͼ�� + 1��skip����)
			initLeadIcon();
			// ��ʼ������ҳ��ViewPager��ͼ (��ViewPager����Adapter)
			initViewPager();
			// ��ʼ������ҳ��ViewPager����ʾ����ͼ���� (��ViewPager��Adapter�������ͼ(ͼƬ))
			initPagerData();
			
			Intent serviceIntent = new Intent(this,MusicService.class);
			startService(serviceIntent);
		}
	}

	private void savePreferences() {
		SharedPreferences preferences = getSharedPreferences("lead_config", Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putBoolean("isFirstRun", false);
		editor.commit();
	}

	@Override
	public void onClick(View v) {
		// �༭����������Ϣ(�����ǵ�һ�����д�����ҳ��)
		savePreferences();
		// ������ת
		if (isFromSetting) {
			startActivity(SettingActivity.class);
		} else {
			startActivity(LogoActivity.class);
		}
		Intent musicIntent = new Intent(this,MusicService.class);
		stopService(musicIntent);
		finish();
	}

	private void initLeadIcon() {
		icons[0] = (ImageView) findViewById(R.id.icon1);
		icons[1] = (ImageView) findViewById(R.id.icon2);
		icons[2] = (ImageView) findViewById(R.id.icon3);
		icons[0].setImageResource(R.drawable.adware_style_selected);
		tv_skip = (TextView) findViewById(R.id.tv_skip);
		tv_skip.setVisibility(View.INVISIBLE);
		tv_skip.setOnClickListener(this);
	}

	private void initViewPager() {
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		leadPagerAdapter = new BasePagerAdapter(this);
		viewPager.setAdapter(leadPagerAdapter);
		viewPager.setOnPageChangeListener(pageChangeListener);
	}
	
	private void initPagerData() {
		ImageView imageView = null;
		imageView = (ImageView) getLayoutInflater().inflate(R.layout.layout_lead_item, null);
		imageView.setImageResource(R.drawable.adware_style_applist);
		leadPagerAdapter.addViewToAdapter(imageView);
		imageView = (ImageView) getLayoutInflater().inflate(R.layout.layout_lead_item, null);
		imageView.setImageResource(R.drawable.adware_style_banner);
		leadPagerAdapter.addViewToAdapter(imageView);
		imageView = (ImageView) getLayoutInflater().inflate(R.layout.layout_lead_item, null);
		imageView.setImageResource(R.drawable.adware_style_creditswall);
		leadPagerAdapter.addViewToAdapter(imageView);
		leadPagerAdapter.notifyDataSetChanged();
	}

	private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {

		@Override
		public void onPageSelected(int arg0) {
			// �������һ��pageʱ����ʾ��skip�ı�
			tv_skip.setVisibility(View.INVISIBLE);
			if (arg0 >= 2) {
				tv_skip.setVisibility(View.VISIBLE);
			}
			// �����±�ͼ��
			for (int i = 0; i < icons.length; i++) {
				icons[i].setImageResource(R.drawable.adware_style_default);
			}
			icons[arg0].setImageResource(R.drawable.adware_style_selected);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	};
}