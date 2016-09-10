package edu.feicui.app.phone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import edu.feicui.app.phone.activity.R;
import edu.feicui.app.phone.base.adapter.BaseBaseAdapter;
import edu.feicui.app.phone.entity.TableInfo;

public class TelListAdapter extends BaseBaseAdapter<TableInfo> {
	private LayoutInflater layoutInflater;

	public TelListAdapter(Context context) {
		super(context);
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getItemView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.layout_tellist_listitem, null);
		}
		TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name);
		TextView tv_number = (TextView) convertView.findViewById(R.id.tv_number);
		tv_name.setText(getItem(position).getName());
		tv_number.setText(getItem(position).getNumber() + "");
		return convertView;
	}
}
