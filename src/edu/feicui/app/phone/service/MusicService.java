package edu.feicui.app.phone.service;

import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.IBinder;

/**
 * ±≥æ∞“Ù¿÷∑˛ŒÒ
 */
public class MusicService extends Service{
	//±≥æ∞“Ù¿÷≤•∑≈∆˜
	MediaPlayer mediaPlayer ;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		AssetManager assetManager = getAssets(); 
		try {
			AssetFileDescriptor fileDescriptor = assetManager.openFd("mo.mp3");
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),
					fileDescriptor.getStartOffset(),
					fileDescriptor.getLength());
			mediaPlayer.prepare();
			mediaPlayer.start();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		mediaPlayer.stop();
		super.onDestroy();
	}

}
