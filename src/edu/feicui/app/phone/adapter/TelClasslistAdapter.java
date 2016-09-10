package edu.feicui.app.phone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.feicui.app.phone.activity.R;
import edu.feicui.app.phone.base.adapter.BaseBaseAdapter;
import edu.feicui.app.phone.entity.ClassInfo;

public class TelClasslistAdapter extends BaseBaseAdapter<ClassInfo> {

	private LayoutInflater layoutInflater;

	public TelClasslistAdapter(Context context) {
		super(context);
		layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getItemView(int position, View convertView, ViewGroup parent) {
		convertView = layoutInflater.inflate(
				R.layout.layout_telclasslist_griditem, null);
		switch (position % 3) {
		case 0:
			convertView
					.setBackgroundResource(R.drawable.selector_classlist_bg1);
			break;
		case 1:
			convertView
					.setBackgroundResource(R.drawable.selector_classlist_bg2);
			break;
		case 2:
		default:
			convertView
					.setBackgroundResource(R.drawable.selector_classlist_bg3);
			break;
		}
		TextView tv_title = (TextView) convertView.findViewById(R.id.tv_title);
		tv_title.setText(getItem(position).getName());
		return convertView;
	}
}
