package edu.feicui.app.phone.activity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import edu.feicui.app.phone.adapter.RubbishFileAdapter;
import edu.feicui.app.phone.base.BaseActivity;
import edu.feicui.app.phone.base.util.CommonUtil;
import edu.feicui.app.phone.biz.DbClearPathManager;
import edu.feicui.app.phone.biz.FileManager;
import edu.feicui.app.phone.entity.RubbishFileInfo;

public class ClearActivity extends BaseActivity {

	private ProgressBar pb_loading;
	private ListView lv_rubbishListview;
	private RubbishFileAdapter rubbishFileAdapter;
	private TextView tv_totalsize;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clear);

		// 初始化ActionBar @see super class ActionBarActivity
		String title = getResources().getString(R.string.sdclean);
		initActionBar(title, R.drawable.btn_homeasup_default, -1, clickListener);
		//
		tv_totalsize = (TextView) findViewById(R.id.tv_filesize);
		pb_loading = (ProgressBar) findViewById(R.id.progressBar);
		lv_rubbishListview = (ListView) findViewById(R.id.listivewRubbish);
		rubbishFileAdapter = new RubbishFileAdapter(this);
		lv_rubbishListview.setAdapter(rubbishFileAdapter);
		//
		try {
			asyncLoaddata();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private long totalSize = 0; // 用来保存总大小的变量

	@Override
	protected void myHandleMessage(Message msg) {
		// TODO Auto-generated method stub
		if (msg.what == 1) {
			long size = (Long) msg.obj;
			totalSize += size;
			tv_totalsize.setText(CommonUtil.getFileSize(totalSize));
		}
		if (msg.what == 2) {
			@SuppressWarnings("unchecked")
			ArrayList<RubbishFileInfo> rubbishFileInfos = (ArrayList<RubbishFileInfo>) msg.obj;
			pb_loading.setVisibility(View.INVISIBLE);
			lv_rubbishListview.setVisibility(View.VISIBLE);
			rubbishFileAdapter = new RubbishFileAdapter(ClearActivity.this);
			lv_rubbishListview.setAdapter(rubbishFileAdapter);
			rubbishFileAdapter.setDataToAdapter(rubbishFileInfos);
			rubbishFileAdapter.notifyDataSetChanged();
		}
	}

	private void asyncLoaddata() throws IOException {
		InputStream path = getResources().getAssets().open("db/clearpath.db");
		DbClearPathManager.readUpdateDB(path);
		final ArrayList<RubbishFileInfo> rubbishFileInfos = DbClearPathManager.getPhoneRubbishfile(getApplicationContext());
		totalSize = 0;
		new Thread(new Runnable() {
			@Override
			public void run() {
				for (RubbishFileInfo rubbishFileInfo : rubbishFileInfos) {
					File file = new File(rubbishFileInfo.getFilepath());
					long size = FileManager.getFileSize(file);
					rubbishFileInfo.setSize(size);
					// 更新全部大小
					Message msg = mainHandler.obtainMessage();
					msg.what = 1;
					msg.obj = size;
					mainHandler.sendMessage(msg);
				}
				// 全部加载完毕 更新UI
				Message msg = mainHandler.obtainMessage();
				msg.what = 2;
				msg.obj = rubbishFileInfos;
				mainHandler.sendMessage(msg);
			}
		}).start();
	}

	private View.OnClickListener clickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			int viewID = v.getId();
			switch (viewID) {
			case R.id.iv_left:
				finish();
				break;
			}
		}
	};
}