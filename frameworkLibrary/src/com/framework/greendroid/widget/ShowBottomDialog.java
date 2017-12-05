package com.framework.greendroid.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.ViewFlipper;

import com.framework.R;
import com.framework.log.LogUtil;

/**
 * 底部menu
 * 
 * @author shaxiaoning
 *
 */
public class ShowBottomDialog extends PopupWindow implements OnClickListener {
	private static final String LOGTAG = LogUtil
			.makeLogTag(ShowBottomDialog.class);
	private Context mContext;
	private View mMenuView;
	private ViewFlipper viewfipper;
	private Button btnSubmit, btnCancel;

	/**
	 * 
	 * @param mContext
	 * @param onWheelListener
	 */
	public ShowBottomDialog(Context mContext, OnWheelListener onWheelListener) {
		super(mContext);
		this.mContext = mContext;

		initView();

	}

	private void initView() {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.show_bottom_dialog, null);
		viewfipper = new ViewFlipper(mContext);
		viewfipper.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));

		btnSubmit = (Button) mMenuView.findViewById(R.id.select_wheel_submit);
		btnCancel = (Button) mMenuView.findViewById(R.id.select_wheel_cancel);
		btnSubmit.setOnClickListener(this);
		btnCancel.setOnClickListener(this);

		viewfipper.addView(mMenuView);
		viewfipper.setFlipInterval(6000000);
		this.setContentView(viewfipper);
		this.setWidth(LayoutParams.MATCH_PARENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setFocusable(true);
		ColorDrawable dw = new ColorDrawable(0x00000000);
		this.setBackgroundDrawable(dw);
		this.update();
	}

	/**
	 * 
	 * @param parentView
	 */
	public void show(View parentView) {
		showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
	}

	@Override
	public void showAtLocation(View parent, int gravity, int x, int y) {
		super.showAtLocation(parent, gravity, x, y);
		viewfipper.startFlipping();
	}

	public void onClick(View v) {

		int id = v.getId();
		if (id == R.id.select_wheel_submit) {
			this.dismiss();
		} else if (id == R.id.select_wheel_cancel) {

			this.dismiss();
		} else {
		}

	}

	public interface OnWheelListener {

		public void onWheel(Boolean isSubmit, String year, String month,
				String day);

	}

}
