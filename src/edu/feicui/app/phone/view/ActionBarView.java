package edu.feicui.app.phone.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import edu.feicui.app.phone.activity.R;

/**
 * ����������
 */
public class ActionBarView extends LinearLayout {

	private ImageView iv_actionbar_left;
	private ImageView iv_actionbar_right;
	private TextView tv_actionbar_title;

	public ActionBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		inflate(context, R.layout.layout_actionbar, this);
		tv_actionbar_title = (TextView) findViewById(R.id.tv_title);
		iv_actionbar_left = (ImageView) findViewById(R.id.iv_left);
		iv_actionbar_right = (ImageView) findViewById(R.id.iv_right);
	}

	/**
	 * ��ʼ��ActionBar
	 * 
	 * @param title
	 *            �м��������
	 * @param leftResID
	 *            ���ͼ����ԴID(��ͼ��ʱ��-1)
	 * @param rightResID�ұ�ͼ����ԴID
	 *            (��ͼ��ʱ��-1)
	 * @param listener
	 *            ��������ͼ�����
	 */
	public void initActionBar(String title, int leftResID, int rightResID, OnClickListener listener) {
		tv_actionbar_title.setText(title);
		if (leftResID == -1) {
			iv_actionbar_left.setVisibility(View.INVISIBLE);
		} else {
			iv_actionbar_left.setImageResource(leftResID);
			iv_actionbar_left.setOnClickListener(listener);
		}
		if (rightResID == -1) {
			iv_actionbar_right.setVisibility(View.INVISIBLE);
		} else {
			iv_actionbar_right.setImageResource(rightResID);
			iv_actionbar_right.setOnClickListener(listener);
		}
	}
}
