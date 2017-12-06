package com.qiu.neteasemusic.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

import java.util.Calendar;

/**
 * Created by qiu on 2017-12-6.
 * http://blog.csdn.net/donmoses/article/details/48728573
 */

public class WatchView extends View {
    private static final int DEFAULT_WIDTH = 300;
    private static final int DEFAULT_HEIGHT = 300;
    private int viewW;
    private Paint mPaint;
    private Path path;
    private PointF cenP;
    //半径
    private int radius;
    //时针角度
    private float angelH;
    //分针角度
    private float angelM;
    //秒针角度
    private float angelS;
    //初始位置
    private float angelStartS;
    private float angelStartM;
    private float angelStartH;
    //日期、星期
    private int dateOfMonth;
    private int dateOfWeek;
    //背景图
   // private Bitmap src;

    public WatchView(Context context) {
        super(context);
        init();
    }

    public WatchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int mWidth = width;
        width -= 200;
        viewW = width + 200;
        cenP.x = width / 2 + 100;
        cenP.y = width / 2;
        radius = width / 2;
        setMeasuredDimension(mWidth,width);
    }

    private int getDimension(int defaultSize, int measureSpec) {
        int result;
        int measureMode = MeasureSpec.getMode(measureSpec);
        int measureSize = MeasureSpec.getSize(measureSpec);
        if (measureMode == MeasureSpec.EXACTLY) {
            result = measureSize;
        } else if (measureMode == MeasureSpec.AT_MOST) {
            result = Math.min(defaultSize, measureSize);
        } else {
            result = defaultSize;
        }
        return result;
    }

    private void init() {
      //  src = BitmapFactory.decodeResource(getResources(), R.mipmap.nav_top);
        //获取系统时间
        getTime();
        //初始化绘画相关
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        cenP = new PointF();
        path = new Path();
        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                super.applyTransformation(interpolatedTime, t);
                float mAngelS = 24 * 60 * 360 * interpolatedTime;
                angelS = angelStartS + mAngelS;
                angelM = angelStartM + mAngelS / 60f;
                angelH = angelStartH + mAngelS / 3600f;
                invalidate();
            }
        };
        animation.setDuration(24 * 60 * 60 * 1000);//一天24个小时为一个周期
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);
        animation.setInterpolator(new LinearInterpolator());
        startAnimation(animation);
    }

    private void getTime() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        int sec = calendar.get(Calendar.SECOND);
        if (hour > 12) {
            hour = hour - 12;
        }
        angelS = 360 * sec / 60f;
        angelM = 360 * min / 60f + sec / 60f;
        angelH = 360 * hour / 12f + 360 * min / (60 * 12f) + 360 * sec / (60 * 60 * 12f);
        angelStartS = angelS;
        angelStartM = angelM;
        angelStartH = angelH;
        dateOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        dateOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setAntiAlias(true);
        //表盘背景
        path.reset();
        path.addCircle(cenP.x, cenP.y, radius, Path.Direction.CW);
        canvas.clipPath(path);
        //canvas.drawBitmap(src, 0, 0, mPaint);背景图
        path.close();

        //表盘边框
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(6.18f * 2);
        mPaint.setColor(Color.RED);
        canvas.drawCircle(cenP.x, cenP.y, radius, mPaint);

        //日期
      //  mPaint.setColor(Color.RED);
       // mPaint.setStrokeWidth(0);
      //  mPaint.setTextSize(44);
       // canvas.drawText(String.valueOf(dateOfMonth), cenP.x + radius / 4, cenP.y + 22, mPaint);
        //星期
        mPaint.setTextSize(44);
        String weekDay = null;
        switch (dateOfWeek) {
            case 1:
                weekDay = "SUN";
                break;
            case 2:
                weekDay = "MON";
                break;
            case 3:
                weekDay = "TUE";
                break;
            case 4:
                weekDay = "WED";
                break;
            case 5:
                weekDay = "THU";
                break;
            case 6:
                weekDay = "FRI";
                break;
            case 7:
                weekDay = "SAT";
                break;
            default:
                break;
        }
       // assert weekDay != null;
    ////    canvas.drawText(weekDay, cenP.x + radius / 2, cenP.y + 22, mPaint);

        //绘制时针
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(20f);
        float hLen = radius / 2f;
        canvas.drawLine(cenP.x, cenP.y, (float) (viewW / 2f + hLen * Math.sin(angelH * Math.PI / 180f)),
                (float) (radius - hLen * Math.cos(angelH * Math.PI / 180f)), mPaint);

        //绘制分针
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(12.5f);
        float mLen = 2 * radius / 3f;
        canvas.drawLine(cenP.x, cenP.y, (float) (viewW / 2f + mLen * Math.sin(angelM * Math.PI / 180f)),
                (float) (radius - mLen * Math.cos(angelM * Math.PI / 180f)), mPaint);

        //绘制秒针
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(5f);
        float sLen = 5 * radius / 6f;
        canvas.drawLine(cenP.x, cenP.y, (float) (viewW / 2f + sLen * Math.sin(angelS * Math.PI / 180f)),
                (float) (radius - sLen * Math.cos(angelS * Math.PI / 180f)), mPaint);

        //中心点
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(cenP.x, cenP.y, radius / 30, mPaint);
        //12个点
        float txtSize = 35f;
        mPaint.setTextSize(txtSize);
        mPaint.setStrokeWidth(3.09f);
        float rR = radius - 35f;
        for (int i = 0; i < 12; i++) {
            String txt;
            if (i == 0) {
                txt = "12";
            } else {
                txt = String.valueOf(i);
            }
            canvas.drawText(
                    txt
                    , (float) (viewW / 2f - txtSize / 2f + rR * Math.sin(i * 30 * Math.PI / 180f))
                    , (float) (radius + txtSize / 2f - rR * Math.cos(i * 30 * Math.PI / 180f))
                    , mPaint);

        }

    }
}

