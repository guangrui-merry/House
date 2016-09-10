package edu.feicui.app.phone.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import edu.feicui.app.phone.adapter.TelListAdapter;
import edu.feicui.app.phone.base.BaseActivity;
import edu.feicui.app.phone.biz.DBTelManager;
import edu.feicui.app.phone.entity.TableInfo;

/**
 * -------------------------
 * @author dell
 *
 */
public class TelmgrShowActivity extends BaseActivity {
	private ListView listView;
	private TelListAdapter telListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_telmgrshow);
		// 取得IDX值 (table1?table2?table3?......)
		// 取得title值(ActionBar显示公共电话？订餐电话？...)
		Intent intent = getIntent();
		int idx = intent.getIntExtra("idx", 0);
		String title = intent.getStringExtra("title");
		if (title == null || title.equals("")) {
			title = "电话列表";
		}
		// 初始化ActionBar @see super class ActionBarActivity
		initActionBar(title, R.drawable.btn_homeasup_default, -1, clickListener);
		// 用来显示电话列表的listview
		listView = (ListView) findViewById(R.id.listView);
		telListAdapter = new TelListAdapter(this);
		listView.setAdapter(telListAdapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				TableInfo tableInfo = telListAdapter.getDataList().get(position);
				long number = tableInfo.getNumber();
				// 通过当前单击的位置，取出“电话信息”实体，取出电话，进行呼叫
				Intent intent = new Intent(Intent.ACTION_CALL);
				intent.setData(Uri.parse("tel:" + number));
				startActivity(intent);
			}
		});
		//
		asyncInitAdapterData(idx);
	}

	private void asyncInitAdapterData(final int idx) {
		final ArrayList<TableInfo> tableInfos = new ArrayList<TableInfo>();
		tableInfos.clear();
		new Thread(new Runnable() {
			@Override
			public void run() {
				tableInfos.addAll(DBTelManager.readTable("table" + idx));
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						telListAdapter.setDataToAdapter(tableInfos);
						telListAdapter.notifyDataSetChanged();
					}
				});
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
