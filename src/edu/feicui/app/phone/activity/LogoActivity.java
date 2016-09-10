package edu.feicui.app.phone.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import edu.feicui.app.phone.base.BaseActivity;

/**
 * Logo����
 * 
 * @author dell
 * 
 */
@SuppressLint("HandlerLeak") public class LogoActivity extends BaseActivity {
	private final int SECOND_TIMEOUT = 1;
	Timer timer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logo);
		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				Message message = new Message();
				message.what = SECOND_TIMEOUT;
				handler.sendMessage(message);
			}
		};
		timer = new Timer(true);
		timer.schedule(timerTask, 3000,1000);
	}
	
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SECOND_TIMEOUT:
				/**
				 * �л�Activity����Ч��
				 * overridePendingTransition(R.anim.fade, R.anim.hold);//���뵭��
				 * overridePendingTransition(R.anim.my_scale_action,R.anim.my_alpha_action);//�Ŵ󵭳�
				 * overridePendingTransition(R.anim.scale_rotate,R.anim.my_alpha_action);//ת������1
				 * overridePendingTransition(R.anim.scale_translate_rotate,R.anim.my_alpha_action);//ת������2
				 * overridePendingTransition(R.anim.scale_translate,R.anim.my_alpha_action);//���Ͻ�չ������Ч��
				 * overridePendingTransition(R.anim.hyperspace_in,R.anim.hyperspace_out);//ѹ����С����Ч��
				 * overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);//�������Ƴ�Ч��
				 * overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);//�������Ƴ�Ч��
				 * overridePendingTransition(R.anim.slide_left,R.anim.slide_right);//���ҽ���Ч��
				 * overridePendingTransition(R.anim.zoom_enter,R.anim.zoom_exit);//��СЧ��
				 * overridePendingTransition(R.anim.slide_up_in,R.anim.slide_down_out);//�����Ľ���
				 */
				startActivity(HomeActivity.class,R.anim.fade,R.anim.hold);
				timer.cancel();
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};
}
