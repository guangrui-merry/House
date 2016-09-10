package edu.feicui.app.phone.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import edu.feicui.app.phone.activity.R;
import edu.feicui.app.phone.base.adapter.BaseBaseAdapter;
import edu.feicui.app.phone.base.util.BitmapCache;
import edu.feicui.app.phone.base.util.BitmapUtil;
import edu.feicui.app.phone.base.util.BitmapUtil.SizeMessage;
import edu.feicui.app.phone.entity.AppInfo;

/**
 * AppInfo  ������
 *
 */
public class AppAdapter extends BaseBaseAdapter<AppInfo> {

	private LayoutInflater layoutInflater;
        private BitmapCache bitmapCache; //������
        
	private Bitmap defIconBitmap; // Ĭ��ͼ���ڿ��ٻ���ʱ����ʾĬ��ͼ��
	private boolean isFling; // �Ƿ��ڿ��ٻ���(��ʱ�͸���ʾĬ��ͼ�񣬶���ȥ��ͼ����صȲ���)

	public boolean isFling() {
		return isFling;
	}

	public void setFling(boolean isFling) {
		this.isFling = isFling;
	}

	public  Bitmap mBitmap(){
		if (isFling) {
			return defIconBitmap;
		}
		return defIconBitmap;
	}
                                       
	@SuppressLint("NewApi")
	public AppAdapter(Context context) {
		super(context);
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//����ͼƬ��С
		SizeMessage sizeMessage = new SizeMessage(context, false, 60, 60);
		defIconBitmap = BitmapUtil.loadBitmap(R.drawable.ic_launcher, sizeMessage);
		bitmapCache = BitmapCache.getInstance();
		mBitmap();
	}


	@Override
	public View getItemView(int position, View convertView, ViewGroup parent) {
		// ��dataList����������ݶ�Ӧ���䵽layout_software_listitem.xml�����ļ���
		ImageView iv_icon;
		TextView tv_title;
		TextView tv_text;
		TextView tv_version;
		CheckBox cb_del;
		convertView = layoutInflater.inflate(R.layout.layout_showapp_listitem, null);

		iv_icon = (ImageView) convertView.findViewById(R.id.iv_app_icon);
		tv_title = (TextView)convertView.findViewById(R.id.tv_app_lable);
		tv_text = (TextView) convertView.findViewById(R.id.tv_app_packagename);
		tv_version = (TextView) convertView.findViewById(R.id.tv_app_version);
		cb_del = (CheckBox) convertView.findViewById(R.id.cb_del);
		cb_del.setOnCheckedChangeListener(checkedChangeListener);

		cb_del.setTag(position);
		AppInfo appInfo = getItem(position);
		
		//
		String title = appInfo.getPackageInfo().applicationInfo.loadLabel(context.getPackageManager()).toString();
		//����
		String text = appInfo.getPackageInfo().packageName;
		//�汾
		String version = appInfo.getPackageInfo().versionName;
		boolean isDel = appInfo.isDel();
		
		Bitmap bitmap = ((BitmapDrawable )appInfo.getPackageInfo().applicationInfo.loadIcon(context.getPackageManager())).getBitmap();
		bitmapCache.addCacheBitmap(bitmap, position);
		iv_icon.setImageBitmap(bitmapCache.getBitmap(position, context));
		
		tv_title.setText(title);
		tv_text.setText(text);
		tv_version.setText(version);
		cb_del.setChecked(isDel);
		return convertView;
	}


	private CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			int position = (Integer) buttonView.getTag();
			getDataList().get(position).setDel(isChecked);
		}
	};
	
}
