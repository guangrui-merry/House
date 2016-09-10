package edu.feicui.app.phone.view;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import edu.feicui.app.phone.activity.R;

/**
 * ����ͼ�ؼ�
 * 
 * @author yuanc
 */
public class PiechartView extends View {

	private Paint paint;
	private RectF oval; // Բ�δ�С
	private float proportionPhone = 0; // #�ֻ��ܿռ���ռ���� (0.3F)
	private float proportionSD = 0; // #�洢���ܿռ���ռ����(0.7F)
	private float piecharAnglePhone = 0; // �ֻ��ռ�Ƕȣ���ͼ���� ??->360*proportionPhone��
	private float piecharAngleSD = 0; // �洢���ռ�Ƕȣ���ͼ���� ?? 360*proportionSD��

	private int phoneColor = 0; // �ֻ��ռ������ɫ
	private int sdColor = 0; // SDCard�ռ������ɫ
	private int baseColor = 0;

	public PiechartView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		paint = new Paint();
		phoneColor = context.getResources().getColor(R.color.piechar_phone);
		sdColor = context.getResources().getColor(R.color.piechar_sdcard);
		baseColor = context.getResources().getColor(R.color.piechar_base);
	}

	/**
	 * ���ñ���ͼ(�������ֻ��洢�ռ���)�ռ��С���� 0.0f -- 1.0f
	 * 
	 * @param f1
	 *            �ֻ�
	 * @param f2
	 *            SDCard
	 */
	public void setPiechartProportion(float f1, float f2) {
		proportionPhone = f1;
		proportionSD = f2;
		// Ŀ��Ƕ�
		final float phoneTargetAngle = 360 * proportionPhone;
		final float sdcardTargetAngle = 360 * proportionSD;
		// ֱ�ӽ��ֻ�������洢�������趨��Ŀ��Ƕ�
		piecharAnglePhone = phoneTargetAngle;
		piecharAngleSD = sdcardTargetAngle;
		postInvalidate();
	}

	public void setPiechartProportionWithAnim(float f1, float f2) {
		proportionPhone = f1;
		proportionSD = f2;
		// Ŀ��Ƕ�
		final float phoneTargetAngle = 360 * proportionPhone;
		final float sdcardTargetAngle = 360 * proportionSD;
		// ͨ��ÿ��+4�ȣ�"ͨ������"�趨��Ŀ��Ƕ�
		final Timer timer = new Timer();
		final TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				piecharAnglePhone += 4;
				piecharAngleSD += 4;
				postInvalidate();
				if (piecharAnglePhone >= phoneTargetAngle) {
					piecharAnglePhone = phoneTargetAngle;
				}
				if (piecharAngleSD >= sdcardTargetAngle) {
					piecharAngleSD = sdcardTargetAngle;
				}
				if (piecharAnglePhone == phoneTargetAngle && piecharAngleSD == sdcardTargetAngle) {
					timer.cancel();
				}
			}
		};
		timer.schedule(timerTask, 26, 26);
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int viewWidth = MeasureSpec.getSize(widthMeasureSpec);
		int viewHeight = MeasureSpec.getSize(heightMeasureSpec);
		oval = new RectF(0, 0, viewWidth, viewHeight);
		setMeasuredDimension(viewWidth, viewHeight);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		paint.setAntiAlias(true);
		// ��ɫ
		paint.setColor(baseColor);
		canvas.drawArc(oval, -90, 360, true, paint);
		// �����ֻ������ֻ��ռ���ռ
		paint.setColor(phoneColor);
		canvas.drawArc(oval, -90, piecharAnglePhone, true, paint);
		// �����ֻ������ӣĿռ���ռ
		paint.setColor(sdColor);
		canvas.drawArc(oval, -90 + piecharAnglePhone, piecharAngleSD, true, paint);
	}
}
