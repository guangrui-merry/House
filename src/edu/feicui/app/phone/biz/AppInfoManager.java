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

/** 应用程序管理类 */
public class AppInfoManager {
	private Context context;
	private PackageManager packageManager;
	private ActivityManager activityManager;
	/** 用来保存所有应用程序包(activity的)列表 */
	private List<AppInfo> allPackageInfos = new ArrayList<AppInfo>();
	private List<AppInfo> userPackageInfos = new ArrayList<AppInfo>();
	private List<AppInfo> systemPackageInfos = new ArrayList<AppInfo>();

	/** 实例化本类时(单态了)，将去获取所有应用程序列表,保存在 {@link #allPackageInfos} */
	private AppInfoManager(Context context) {
		this.context = context;
		packageManager = context.getPackageManager();
		activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	}

	/** 清理所有正在运行的程序(级别为服务进程以上的非系统进程) */
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

	/** 清理指定程序 */
	public void killProcesses(String packageName) {
		activityManager.killBackgroundProcesses(packageName);
	}

	public static final int RUNING_APP_TYPE_SYS = 0;
	public static final int RUNING_APP_TYPE_USER = 1;

	/** 获取正在运行应用 */
	public Map<Integer, List<RuningAppInfo>> getRuningAppInfos() {
		Map<Integer, List<RuningAppInfo>> runingAppInfos = new HashMap<Integer, List<RuningAppInfo>>();
		List<RuningAppInfo> sysapp = new ArrayList<RuningAppInfo>();
		List<RuningAppInfo> userapp = new ArrayList<RuningAppInfo>();
		// 获取所有正在运行应用
		List<RunningAppProcessInfo> appProcessInfos = activityManager.getRunningAppProcesses();
		for (RunningAppProcessInfo appProcessInfo : appProcessInfos) {
			String packageName = appProcessInfo.processName; // 正在运行程序进程名
			int pid = appProcessInfo.pid; // 正在运行程序进程ID
			int importance = appProcessInfo.importance; // 正在运行程序进程级别
			// 服务进程（包括）级别以下进程
			if (importance >= RunningAppProcessInfo.IMPORTANCE_SERVICE) {
				Drawable icon; // 所取数据：运行中程序图标
				String lableName; // 所取数据：运行中程序名称
				long size; // 所取数据：运行中程序所占内存
				Debug.MemoryInfo[] memoryInfos = activityManager.getProcessMemoryInfo(new int[] { pid });
				size = (memoryInfos[0].getTotalPrivateDirty()) * 1024;
				try {
					icon = packageManager.getApplicationIcon(packageName);
					ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA | PackageManager.GET_SHARED_LIBRARY_FILES | PackageManager.GET_UNINSTALLED_PACKAGES);
					lableName = packageManager.getApplicationLabel(applicationInfo).toString();
					RuningAppInfo runingAppInfo = new RuningAppInfo(packageName, icon, lableName, size);
					// 系统进程
					if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
						runingAppInfo.setSystem(true);
						runingAppInfo.setClear(false);
						sysapp.add(runingAppInfo);
					}
					// 用户进程(默认选中)
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

	/** 用来返回本类的唯一对象 (单态模块　且做了同步处理,还优化了一下同步处理) */
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

	/** 用来返回所有应用程序列表 */
	public List<AppInfo> getAllPackageInfo(boolean isReset) {
		if (isReset) {
			loadAllActivityPackager();
		}
		return allPackageInfos;
	}

	/** 用来返回所有系统应用程序列表 */
	public List<AppInfo> getSystemPackageInfo(boolean isReset) {
		if (isReset) {
			loadAllActivityPackager();
			systemPackageInfos.clear();
			for (AppInfo appInfo : allPackageInfos) {
				if ((appInfo.getPackageInfo().applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
					systemPackageInfos.add(appInfo);// 系统软件
				} else {
				}
			}
		}
		return systemPackageInfos;
	}

	/** 用来返回所有用户应用程序列表 */
	public List<AppInfo> getUserPackageInfo(boolean isReset) {
		if (isReset) {
			loadAllActivityPackager();
			userPackageInfos.clear();
			for (AppInfo appInfo : allPackageInfos) {
				if ((appInfo.getPackageInfo().applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
				} else {
					userPackageInfos.add(appInfo);// 用户软件
				}
			}
		}
		return userPackageInfos;
	}

	// 加载所有Activity应用程序包
	private void loadAllActivityPackager() {
		List<PackageInfo> infos = packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES | PackageManager.GET_UNINSTALLED_PACKAGES);
		allPackageInfos.clear();
		for (PackageInfo packageInfo : infos) {
			allPackageInfos.add(new AppInfo(packageInfo));
		}
	}
}
