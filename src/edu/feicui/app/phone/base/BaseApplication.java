package edu.feicui.app.phone.base;

import android.annotation.SuppressLint;
import android.app.Application;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.util.LruCache;
import edu.feicui.app.phone.base.util.LogUtil;

public class BaseApplication extends Application {

	private LruCache<String, Bitmap> lruCache = null;
	
	@SuppressLint("NewApi")
	public BaseApplication() {
		lruCache = new LruCache<String, Bitmap>(5 * 1024 * 1024) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
				// TODO Auto-generated method stub
				if (Build.VERSION.SDK_INT >= 12) {
					return value.getByteCount();
				}
				return value.getRowBytes() * value.getHeight();
			}
		};
	}

	public LruCache<String, Bitmap> getLruCache() {
		return lruCache;
	}

	public void addBitmapToCache(String key, Bitmap bitmap) {
		lruCache.put(key, bitmap);
	}

	public Bitmap getBitmapFromCache(String key) {
		return lruCache.get(key);
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		LogUtil.d(this, "Application Create");
		// SDKInitializer.initialize(getApplicationContext());
	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
		LogUtil.d(this, "Application Terminate");
	}

}
