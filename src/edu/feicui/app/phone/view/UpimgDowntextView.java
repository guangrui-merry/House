package edu.feicui.app.phone.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import edu.feicui.app.phone.activity.R;

/**
 * 自定义控件（组合）,未使用
 * 
 * @author yuanc
 * 
 */
public class UpimgDowntextView extends LinearLayout {

	private ImageView iv_up;
	private TextView tv_down;
	
	public UpimgDowntextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub

		inflate(getContext(), R.layout.layout_upimgdowntext, this);
		iv_up = (ImageView) findViewById(R.id.iv_up);
		tv_down = (TextView) findViewById(R.id.tv_down);

		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.UpimgDowntext);
		Drawable drawable = typedArray.getDrawable(R.styleable.UpimgDowntext_imgid);
		String text = typedArray.getString(R.styleable.UpimgDowntext_text);
		typedArray.recycle();
		if (drawable != null) {
			iv_up.setImageDrawable(drawable);
		}
		if (text != null) {
			tv_down.setText(text);
		}
	}

	public void initUpimgDowntextView(int upImageID, String downText) {
		iv_up.setImageResource(upImageID);
		tv_down.setText(downText);
	}

}
