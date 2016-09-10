package edu.feicui.app.phone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import edu.feicui.app.phone.activity.R;
import edu.feicui.app.phone.base.adapter.BaseBaseAdapter;
import edu.feicui.app.phone.base.util.CommonUtil;
import edu.feicui.app.phone.entity.RubbishFileInfo;

public class RubbishFileAdapter extends BaseBaseAdapter<RubbishFileInfo> {

	private LayoutInflater layoutInflater;

	public RubbishFileAdapter(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getItemView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.layout_rubbishfile_listitem, null);
		}
		TextView tv_lable = (TextView) convertView.findViewById(R.id.tv_app_lable);
		TextView tv_size = (TextView) convertView.findViewById(R.id.tv_size);
		ImageView iv_icon = (ImageView) convertView.findViewById(R.id.iv_app_icon);
		tv_lable.setText(getItem(position).getSoftChinesename());
		tv_size.setText(CommonUtil.getFileSize(getItem(position).getSize()));
		iv_icon.setImageDrawable(getItem(position).getIcon());
		return convertView;
	}
}