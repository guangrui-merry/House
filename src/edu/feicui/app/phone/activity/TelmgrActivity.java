package edu.feicui.app.phone.activity;

import java.io.IOException;
import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import edu.feicui.app.phone.adapter.TelClasslistAdapter;
import edu.feicui.app.phone.base.BaseActivity;
import edu.feicui.app.phone.biz.DBTelManager;
import edu.feicui.app.phone.entity.ClassInfo;

/**
 * -------------------------
 * @author dell
 *
 */
public class TelmgrActivity extends BaseActivity {

	private GridView gv_telclasslist;
	private TelClasslistAdapter classlistAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_telmgr);
		// ≥ı ºªØActionBar @see super class ActionBarActivity
		String title = getResources().getString(R.string.telmgr);
		initActionBar(title, R.drawable.btn_homeasup_default, -1, clickListener);
		//
		gv_telclasslist = (GridView) findViewById(R.id.gv_allteltype);
		classlistAdapter = new TelClasslistAdapter(this);
		gv_telclasslist.setAdapter(classlistAdapter);
		gv_telclasslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Bundle bundle = new Bundle();
				bundle.putString("title", classlistAdapter.getDataList().get(position).getName());
				bundle.putInt("idx", classlistAdapter.getDataList().get(position).getIdx());
				startActivity(TelmgrShowActivity.class, bundle);
			}
		});
		//
		asyncInitAdapterData();
	}

	public void asyncInitAdapterData() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				final ArrayList<ClassInfo> classInfos = new ArrayList<ClassInfo>();
				try {
					DBTelManager.readUpdateDB(getResources().getAssets().open("db/commonnum.db"));
					classInfos.clear();
					classInfos.addAll(DBTelManager.readClassListTable());
				} catch (IOException e) {
				}
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						classlistAdapter.setDataToAdapter(classInfos);
						classlistAdapter.notifyDataSetChanged();
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
				startActivity(HomeActivity.class);
				finish();
				break;
			}
		}
	};
}
