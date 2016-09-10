package edu.feicui.app.phone.base;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;

public class BaseFragment extends Fragment {

	protected View rootView;

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (rootView != null) {
			ViewGroup viewGroup = (ViewGroup) rootView.getParent();
			viewGroup.removeView(rootView);
		}
	}
}
