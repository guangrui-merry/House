package edu.feicui.app.phone.entity;

import android.content.pm.PackageInfo;

public class AppInfo {

	private PackageInfo packageInfo;//������Ϣ
	private boolean isDel; // �Ƿ����� (�Ƿ�ѡ��)

	public AppInfo(PackageInfo packageInfo) {
		this(packageInfo, false);
	}

	public AppInfo(PackageInfo packageInfo, boolean isDel) {
		this.packageInfo = packageInfo;
		this.isDel = isDel;
	}

	public PackageInfo getPackageInfo() {
		return packageInfo;
	}

	public void setPackageInfo(PackageInfo packageInfo) {
		this.packageInfo = packageInfo;
	}

	public boolean isDel() {
		return isDel;
	}

	public void setDel(boolean isDel) {
		this.isDel = isDel;
	}

}
