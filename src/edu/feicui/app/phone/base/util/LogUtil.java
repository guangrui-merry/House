package edu.feicui.app.phone.base.util;

import android.util.Log;

/**
 * 日志工具类
 * 
 * @author yuanc
 * 
 */
public class LogUtil {

	public static boolean isOpenDebug = true; // 是否开启debug logcat

	/**
	 * 推荐使用。用调用者类名做为tag值，进行日志输出
	 * 
	 * @param obj
	 *            　一般传入 this
	 * @param msg
	 *            日志信息
	 * 
	 * @see {@link #isOpenDebug}
	 */
	public static void d(Object obj, String msg) {
		if (isOpenDebug) {
			Log.d(obj.getClass().getSimpleName(), msg);
		}
	}

	/**
	 * 推荐使用。用调用者类名做为tag值，进行日志输出
	 * 
	 * @param obj
	 *            　一般传入 this
	 * @param msg
	 *            日志信息
	 * @param throwable
	 *            异常对象,没有可传入null, or {@link #d(Object, String)}
	 * 
	 * @see {@link #isOpenDebug}
	 */
	public static void d(Object obj, String msg, Throwable throwable) {
		if (isOpenDebug) {
			Log.d(obj.getClass().getSimpleName(), msg, throwable);
		}
	}
}
