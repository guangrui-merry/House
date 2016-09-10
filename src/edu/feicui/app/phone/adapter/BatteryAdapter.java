package edu.feicui.app.phone.adapter;

import edu.feicui.app.phone.biz.SystemManager;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

/**
 * 电池管理适配器
 *
 */
public class BatteryAdapter extends BaseExpandableListAdapter {
	private Context context;
	private final String groups[] = { "基本信息", "CPU信息", "摄像头信息", "分辨率信息", "WIFI信息" };

	public BatteryAdapter(Context context) {
		this.context = context;
		// 取手机检信息
		SystemManager manager = new SystemManager(context);
		manager.BasicInfo(SystemManager.basicInfos);
		manager.CPUInfo(SystemManager.CPUInfos);
		manager.resolutionInfo(SystemManager.resolutionInfos);
		manager.pixelInfo(SystemManager.pixelInfos);
		manager.WIFIInfo(SystemManager.WIFIInfos);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return groups.length;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		switch (groupPosition) {
		case 0:
			return SystemManager.basicInfos.length;
		case 1:
			return SystemManager.CPUInfos.length;
		case 2:
			return SystemManager.resolutionInfos.length;
		case 3:
			return SystemManager.pixelInfos.length;
		case 4:
		default:
			return SystemManager.WIFIInfos.length;
		}
	}

	@Override
	public String getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return groups[groupPosition];
	}

	@Override
	public String getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub

		switch (groupPosition) {
		case 0:
			return SystemManager.basicInfos[childPosition];
		case 1:
			return SystemManager.CPUInfos[childPosition];
		case 2:
			return SystemManager.resolutionInfos[childPosition];
		case 3:
			return SystemManager.pixelInfos[childPosition];
		case 4:
		default:
			return SystemManager.WIFIInfos[childPosition];
		}
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		TextView textView = new TextView(context);
		textView.setText(getGroup(groupPosition));
		return textView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		TextView textView = new TextView(context);
		textView.setText(getChild(groupPosition, childPosition));
		return textView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}
}
