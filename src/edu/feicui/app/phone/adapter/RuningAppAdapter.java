package edu.feicui.app.phone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import edu.feicui.app.phone.activity.R;
import edu.feicui.app.phone.base.adapter.BaseBaseAdapter;
import edu.feicui.app.phone.base.util.CommonUtil;
import edu.feicui.app.phone.entity.RuningAppInfo;

public class RuningAppAdapter extends BaseBaseAdapter<RuningAppInfo> {

	private LayoutInflater layoutInflater;
	private int state = 0; //显示用户进程
	public static final int STATE_SHOW_USER = 0;//显示用户进程
	public static final int STATE_SHOW_ALL = 1;//显示全部进程
	public static final int STATE_SHOW_SYS = 2;//显示系统进程

	public RuningAppAdapter(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		state = STATE_SHOW_USER;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	@Override
	public View getItemView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.layout_speedup_listitem, null);
		}
		TextView tv_lable = (TextView) convertView.findViewById(R.id.tv_app_lable);
		TextView tv_size = (TextView) convertView.findViewById(R.id.tv_app_packagename);
		ImageView iv_icon = (ImageView) convertView.findViewById(R.id.iv_app_icon);
		CheckBox cb_clear = (CheckBox) convertView.findViewById(R.id.cb_clear);
		TextView tv_message = (TextView) convertView.findViewById(R.id.tv_app_version);

		cb_clear.setTag(position);
		cb_clear.setOnCheckedChangeListener(checkedChangeListener); // 监听CheckBox

		tv_lable.setText(getItem(position).getLableName());
		tv_size.setText("内存：" + CommonUtil.getFileSize(getItem(position).getSize()));
		iv_icon.setImageDrawable(getItem(position).getIcon());
		cb_clear.setChecked(getItem(position).isClear());
		tv_message.setText(getItem(position).isSystem() ? "系统进程" : "");
		return convertView;
	}

	private OnCheckedChangeListener checkedChangeListener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			int position = (Integer) buttonView.getTag();
			getItem(position).setClear(isChecked); // 更新当前CheckBox是否选中的实体数据
		}
	};

}
