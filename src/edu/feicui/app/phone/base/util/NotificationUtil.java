package edu.feicui.app.phone.base.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.RemoteViews;
import edu.feicui.app.phone.activity.R;

public class NotificationUtil {

	private static NotificationManager manager;
	private static Notification notification;

	public static final int NOTIFI_APPICON_ID = 1;

	public static boolean isOpenNotification(Context context) {
		SharedPreferences preferences = context.getSharedPreferences("notifi", Context.MODE_PRIVATE);
		return preferences.getBoolean("open", true);
	}

	public static void setOpenNotification(Context context, boolean open) {
		SharedPreferences preferences = context.getSharedPreferences("notifi", Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putBoolean("open", open);
		editor.commit();
	}

	public static void cancelAppIconNotification(Context context) {
		if (manager == null) {
			manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		}
		manager.cancel(NOTIFI_APPICON_ID);
	}

	public static void showAppIconNotification(Context context) {
		if (notification == null) {
			notification = new Notification();
		}
		notification.flags = Notification.FLAG_NO_CLEAR;
		notification.icon = R.drawable.ic_launcher;
		notification.tickerText = "ÐÂÍ¨Öª";
		notification.when = System.currentTimeMillis();
		RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.layout_notification_appincon);
		notification.contentView = contentView;
		Intent intent = new Intent("com.androidy.app.phone");
		PendingIntent contentIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		notification.contentIntent = contentIntent;
		if (manager == null) {
			manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		}
		manager.notify(NOTIFI_APPICON_ID, notification);
	}
}
