package edu.feicui.app.phone.entity;

import android.graphics.drawable.Drawable;

public class RuningAppInfo {

	private String packageName; //��װ������
	private Drawable icon;//ͼ��
	private String lableName;//ϵͳ������
	private long size;//Ӧ�ô�С
	private boolean isClear;//�Ƿ���Ҫ���
	private boolean isSystem;//�Ƿ���ϵͳӦ��

	public RuningAppInfo(String packageName, Drawable icon, String lableName, long size) {
		super();
		this.packageName = packageName;
		this.icon = icon;
		this.lableName = lableName;
		this.size = size;
		isClear = false;
		isSystem = false;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public boolean isSystem() {
		return isSystem;
	}

	public void setSystem(boolean isSystem) {
		this.isSystem = isSystem;
	}

	public boolean isClear() {
		return isClear;
	}

	public void setClear(boolean isClear) {
		this.isClear = isClear;
	}

	@Override
	public String toString() {
		return "RuningAppInfo [lableName=" + lableName + ", size=" + size + "]";
	}

	public RuningAppInfo() {
		super();
	}

	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
	}

	public String getLableName() {
		return lableName;
	}

	public void setLableName(String lableName) {
		this.lableName = lableName;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

}
