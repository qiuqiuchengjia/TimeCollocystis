package com.framework.greendroid.wheel.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.framework.R;
import com.framework.greendroid.wheel.NumericWheelAdapter;
import com.framework.greendroid.wheel.OnWheelChangedListener;
import com.framework.greendroid.wheel.WheelView;
import com.framework.log.DebugLog;
import com.framework.log.LogUtil;
//SelectWheeelDate wheel = new SelectWheeelDate(this,
//		new OnWheelListener() {
//
//			@Override
//			public void onWheel(Boolean isSubmit, String year,
//					String month, String day) {
//				date = year + "-" + month + "-" + day;
//				// showBuyDate(date);
//				holder.getTxtWedDate().setText(date);
//				getWedInfoBean().setWedDate(date);
//			}
//		}, true, new SimpleDateFormat("yyyy").format(new Date()),
//		new SimpleDateFormat("M").format(new Date()),
//		new SimpleDateFormat("dd").format(new Date()));
//wheel.show(findViewById(R.id.activity_find_partner_parent));

/**
 * 选择日期控件
 *
 * @author shaxiaoning
 */
public class SelectWheeelDate extends PopupWindow implements OnClickListener {
    private static final String LOGTAG = LogUtil
            .makeLogTag(SelectWheeelDate.class);
    private Context mContext;
    private View mMenuView;
    private ViewFlipper viewfipper;
    private Button btnSubmit, btnCancel;
    private String age;
    private DateNumericAdapter monthAdapter, dayAdapter, yearAdapter;
    private WheelView year, month, day;
    private int mCurYear = 80, mCurMonth = 5, mCurDay = 14;
    private String[] dateType;

    private int lastYear, lastMonth, lastDay;
    private boolean isHaveDayWheel;

    private OnWheelListener onWheelListener;

    /**
     * 初始化选择日期对话框
     *
     * @param context
     * @param onWheelListener
     * @param isHaveDayWheel1 是否显示 日
     * @param yearInt
     * @param monthInt
     * @param dayInt
     */
    public SelectWheeelDate(Context mContext, OnWheelListener onWheelListener,
                            boolean isHaveDayWheel1, String yearInt, String monthInt,
                            String dayInt) {
        super(mContext);
        isHaveDayWheel = isHaveDayWheel1;
        this.mContext = mContext;
        this.onWheelListener = onWheelListener;
        this.age = yearInt + "-" + monthInt + "-" + dayInt;

        initView();

    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.select_wheel_date, null);
        viewfipper = new ViewFlipper(mContext);
        viewfipper.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));

        year = (WheelView) mMenuView.findViewById(R.id.select_wheel_year);
        month = (WheelView) mMenuView.findViewById(R.id.select_wheel_month);
        day = (WheelView) mMenuView.findViewById(R.id.select_wheel_day);

        if (isHaveDayWheel)
            day.setVisibility(View.VISIBLE);
        else
            day.setVisibility(View.GONE);

        btnSubmit = (Button) mMenuView.findViewById(R.id.select_wheel_submit);
        btnCancel = (Button) mMenuView.findViewById(R.id.select_wheel_cancel);
        btnSubmit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        Calendar calendar = Calendar.getInstance();
        OnWheelChangedListener listener = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                updateDays(year, month, day);
            }
        };
        int curYear = calendar.get(Calendar.YEAR);
        if (age != null && age.contains("-")) {
            String str[] = age.split("-");
            mCurYear = 100 - (curYear - Integer.parseInt(str[0]));
            mCurMonth = Integer.parseInt(str[1]) - 1;
            mCurDay = Integer.parseInt(str[2]) - 1;

        }
        dateType = mContext.getResources().getStringArray(R.array.date);
        monthAdapter = new DateNumericAdapter(mContext, 1, 12, 5);
        monthAdapter.setTextType(dateType[1]);
        month.setViewAdapter(monthAdapter);
        month.setCurrentItem(mCurMonth);
        month.addChangingListener(listener);
        // year

        // yearAdapter = new DateNumericAdapter(mContext, curYear - 20,
        // curYear + 100, 100 - 20);
        yearAdapter = new DateNumericAdapter(mContext, curYear - 100,
                curYear + 100, 100 - 20);
        yearAdapter.setTextType(dateType[0]);
        year.setViewAdapter(yearAdapter);
        year.setCurrentItem(mCurYear);
        year.addChangingListener(listener);
        // day

        updateDays(year, month, day);
        day.setCurrentItem(mCurDay);
        updateDays(year, month, day);
        day.addChangingListener(listener);

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

    private void updateDays(WheelView year, WheelView month, WheelView day) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,
                calendar.get(Calendar.YEAR) + year.getCurrentItem());
        calendar.set(Calendar.MONTH, month.getCurrentItem());

        int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        dayAdapter = new DateNumericAdapter(mContext, 1, maxDays,
                calendar.get(Calendar.DAY_OF_MONTH) - 1);
        dayAdapter.setTextType(dateType[2]);
        day.setViewAdapter(dayAdapter);
        int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
        day.setCurrentItem(curDay - 1, true);
        // int years = calendar.get(Calendar.YEAR) - 20;
        int years = calendar.get(Calendar.YEAR);

        lastYear = years - 100;
        lastMonth = month.getCurrentItem() + 1;
        lastDay = day.getCurrentItem() + 1;

        age = years + "-" + (month.getCurrentItem() + 1) + "-"
                + (day.getCurrentItem() + 1);
    }

    /**
     * Adapter for numeric wheels. Highlights the current value.
     */
    private class DateNumericAdapter extends NumericWheelAdapter {
        // Index of current item
        int currentItem;
        // Index of item to be highlighted
        int currentValue;

        /**
         * Constructor
         */
        public DateNumericAdapter(Context context, int minValue, int maxValue,
                                  int current) {
            super(context, minValue, maxValue);
            this.currentValue = current;
            setTextSize(20);
            setTextColor(context.getResources().getColor(
                    R.color.wheel_text_color));

        }

        protected void configureTextView(TextView view) {
            super.configureTextView(view);
            view.setTypeface(Typeface.SANS_SERIF);
            // view.setTextColor(color);
        }

        public CharSequence getItemText(int index) {
            currentItem = index;
            return super.getItemText(index);
        }

    }

    public void onClick(View v) {
        String month = "";
        String day = "";
        try {
            Date dates = new SimpleDateFormat("MM").parse(String
                    .valueOf(lastMonth));
            month = new SimpleDateFormat("MM").format(dates);
            Date days = new SimpleDateFormat("dd").parse(String
                    .valueOf(lastDay));
            day = new SimpleDateFormat("dd").format(days);
        } catch (ParseException e) {
            // e.printStackTrace();
            month = new SimpleDateFormat("MM").format(new Date());
            day = new SimpleDateFormat("dd").format(new Date());
        }
        int id = v.getId();
        if (id == R.id.select_wheel_submit) {
            onWheelListener.onWheel(true, "" + lastYear, month, "" + day);
            this.dismiss();
        } else if (id == R.id.select_wheel_cancel) {
            // onWheelListener.onWheel(false, "" + lastYear, month, "" + day);
            this.dismiss();
        } else {
        }

    }

    public interface OnWheelListener {
        /**
         * 获取日期接口
         *
         * @param selectedId
         */
        public void onWheel(Boolean isSubmit, String year, String month,
                            String day);

        // public void getSlectedDate(String date);
    }

}
