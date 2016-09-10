package edu.feicui.app.phone.base.util;

import android.content.Context;
import android.view.WindowManager;

/** 设备 工具类,封装了一些对设备上的常用操作 */
public class DeviceUtil {
	/** 单位转换 */
	public static int dp2px(Context context, int dp) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}

	/** 单位转换 */
	public static int px2dp(Context context, int px) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (px / scale + 0.5f);
	}

	/** 屏幕宽 */
	public static int getScreenWidthPX(Context context) {
		WindowManager mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		return mWindowManager.getDefaultDisplay().getWidth();
	}

	/** 屏幕高 */
	public static int getScreenHeightPX(Context context) {
		WindowManager mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		return mWindowManager.getDefaultDisplay().getHeight();
	}

}
