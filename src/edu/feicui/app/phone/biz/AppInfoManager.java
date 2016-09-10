package edu.feicui.app.phone.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Debug;

import edu.feicui.app.phone.entity.AppInfo;
import edu.feicui.app.phone.entity.RuningAppInfo;

/** Ӧ�ó�������� */
public class AppInfoManager {
	private Context context;
	private PackageManager packageManager;
	private ActivityManager activityManager;
	/** ������������Ӧ�ó����(activity��)�б� */
	private List<AppInfo> allPackageInfos = new ArrayList<AppInfo>();
	private List<AppInfo> userPackageInfos = new ArrayList<AppInfo>();
	private List<AppInfo> systemPackageInfos = new ArrayList<AppInfo>();

	/** ʵ��������ʱ(��̬��)����ȥ��ȡ����Ӧ�ó����б�,������ {@link #allPackageInfos} */
	private AppInfoManager(Context context) {
		this.context = context;
		packageManager = context.getPackageManager();
		activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	}

	/** ���������������еĳ���(����Ϊ����������ϵķ�ϵͳ����) */
	public void killALLProcesses() {
		List<RunningAppProcessInfo> appProcessInfos = activityManager.getRunningAppProcesses();
		for (RunningAppProcessInfo appProcessInfo : appProcessInfos) {
			if (appProcessInfo.importance >= RunningAppProcessInfo.IMPORTANCE_SERVICE) {
				String packageName = appProcessInfo.processName;
				try {
					ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA | PackageManager.GET_SHARED_LIBRARY_FILES | PackageManager.GET_SHARED_LIBRARY_FILES | PackageManager.GET_UNINSTALLED_PACKAGES);
					if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
					} else {
						activityManager.killBackgroundProcesses(packageName);
					}
				} catch (NameNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/** ����ָ������ */
	public void killProcesses(String packageName) {
		activityManager.killBackgroundProcesses(packageName);
	}

	public static final int RUNING_APP_TYPE_SYS = 0;
	public static final int RUNING_APP_TYPE_USER = 1;

	/** ��ȡ��������Ӧ�� */
	public Map<Integer, List<RuningAppInfo>> getRuningAppInfos() {
		Map<Integer, List<RuningAppInfo>> runingAppInfos = new HashMap<Integer, List<RuningAppInfo>>();
		List<RuningAppInfo> sysapp = new ArrayList<RuningAppInfo>();
		List<RuningAppInfo> userapp = new ArrayList<RuningAppInfo>();
		// ��ȡ������������Ӧ��
		List<RunningAppProcessInfo> appProcessInfos = activityManager.getRunningAppProcesses();
		for (RunningAppProcessInfo appProcessInfo : appProcessInfos) {
			String packageName = appProcessInfo.processName; // �������г��������
			int pid = appProcessInfo.pid; // �������г������ID
			int importance = appProcessInfo.importance; // �������г�����̼���
			// ������̣��������������½���
			if (importance >= RunningAppProcessInfo.IMPORTANCE_SERVICE) {
				Drawable icon; // ��ȡ���ݣ������г���ͼ��
				String lableName; // ��ȡ���ݣ������г�������
				long size; // ��ȡ���ݣ������г�����ռ�ڴ�
				Debug.MemoryInfo[] memoryInfos = activityManager.getProcessMemoryInfo(new int[] { pid });
				size = (memoryInfos[0].getTotalPrivateDirty()) * 1024;
				try {
					icon = packageManager.getApplicationIcon(packageName);
					ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA | PackageManager.GET_SHARED_LIBRARY_FILES | PackageManager.GET_UNINSTALLED_PACKAGES);
					lableName = packageManager.getApplicationLabel(applicationInfo).toString();
					RuningAppInfo runingAppInfo = new RuningAppInfo(packageName, icon, lableName, size);
					// ϵͳ����
					if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
						runingAppInfo.setSystem(true);
						runingAppInfo.setClear(false);
						sysapp.add(runingAppInfo);
					}
					// �û�����(Ĭ��ѡ��)
					else {
						runingAppInfo.setSystem(false);
						runingAppInfo.setClear(true);
						userapp.add(runingAppInfo);
					}
				} catch (NameNotFoundException ex) {
				}
			}
		}
		runingAppInfos.put(RUNING_APP_TYPE_SYS, sysapp);
		runingAppInfos.put(RUNING_APP_TYPE_USER, userapp);
		return runingAppInfos;
	}

	/** �������ر����Ψһ���� (��̬ģ�顡������ͬ������,���Ż���һ��ͬ������) */
	private static AppInfoManager appManager = null;

	public static AppInfoManager getAppInfoManager(Context context) {
		if (appManager == null) {
			synchronized (context) {
				if (appManager == null) {
					appManager = new AppInfoManager(context);
				}
			}
		}
		return appManager;
	}

	/** ������������Ӧ�ó����б� */
	public List<AppInfo> getAllPackageInfo(boolean isReset) {
		if (isReset) {
			loadAllActivityPackager();
		}
		return allPackageInfos;
	}

	/** ������������ϵͳӦ�ó����б� */
	public List<AppInfo> getSystemPackageInfo(boolean isReset) {
		if (isReset) {
			loadAllActivityPackager();
			systemPackageInfos.clear();
			for (AppInfo appInfo : allPackageInfos) {
				if ((appInfo.getPackageInfo().applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
					systemPackageInfos.add(appInfo);// ϵͳ���
				} else {
				}
			}
		}
		return systemPackageInfos;
	}

	/** �������������û�Ӧ�ó����б� */
	public List<AppInfo> getUserPackageInfo(boolean isReset) {
		if (isReset) {
			loadAllActivityPackager();
			userPackageInfos.clear();
			for (AppInfo appInfo : allPackageInfos) {
				if ((appInfo.getPackageInfo().applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
				} else {
					userPackageInfos.add(appInfo);// �û����
				}
			}
		}
		return userPackageInfos;
	}

	// ��������ActivityӦ�ó����
	private void loadAllActivityPackager() {
		List<PackageInfo> infos = packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES | PackageManager.GET_UNINSTALLED_PACKAGES);
		allPackageInfos.clear();
		for (PackageInfo packageInfo : infos) {
			allPackageInfos.add(new AppInfo(packageInfo));
		}
	}
}
