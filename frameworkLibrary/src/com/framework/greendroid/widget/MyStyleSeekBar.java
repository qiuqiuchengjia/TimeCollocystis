package com.framework.greendroid.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.framework.R;

public class MyStyleSeekBar extends View {

	private String texts;
	private String[] textValue;
	private int text_sizes = 14;
	private int text_focus_color = 0xffff5d5d;
	private int text_unfocus_color = 0xffc9c6c9;
	private int text_hasfocus_color = 0xff666666;
	private int line_focus_color = 0xffff5d5d;
	private int line_unfocus_color = 0xffc9c6c9;
	private int line_height = 2;
	private int circle_focus_color = 0xffff5d5d;
	private int circle_unfocus_color = 0xffc9c6c9;
	private int circle_radius = 4;
	private int text_top_padding = 8;
	private int circle_padding = 20;
	private Bitmap focus_bg;

	// Paints
	private TextPaint textFocusPaint = new TextPaint();
	private TextPaint textUnFocusPaint = new TextPaint();
	private TextPaint texthasFocusPaint = new TextPaint();
	private Paint lineFocusPaint = new Paint();
	private Paint lineUnFocusPaint = new Paint();
	private Paint circleFocusPaint = new Paint();
	private Paint circleUnFocusPaint = new Paint();
	private Paint bitmap_bgPaint = new Paint();

	private int steps = 0;
	private int totalSteps = 0;
	private int focus_bgId = 0;
	private DisplayMetrics metrics = null;

	public MyStyleSeekBar(Context context, AttributeSet attrs) {
		super(context, attrs);

		parseAttributes(context.obtainStyledAttributes(attrs,
				R.styleable.myStyleSeekBar));
	}

	public void setStep(int steps) {
		this.steps = steps;
		invalidate();
	}

	public void setStepValue(String texts) {
		this.texts = texts;
		invalidate();
	}

	public MyStyleSeekBar(Context context) {
		super(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		setupPaints();
		invalidate();
	}

	private void setupPaints() {
		textFocusPaint.setColor(text_focus_color);
		textFocusPaint.setAntiAlias(true);
		textFocusPaint.setTextSize(text_sizes);
		textFocusPaint.setStyle(Style.FILL);

		textUnFocusPaint.setColor(text_unfocus_color);
		textUnFocusPaint.setAntiAlias(true);
		textUnFocusPaint.setTextSize(text_sizes);
		textUnFocusPaint.setStyle(Style.FILL);

		texthasFocusPaint.setColor(text_hasfocus_color);
		texthasFocusPaint.setAntiAlias(true);
		texthasFocusPaint.setTextSize(text_sizes);
		texthasFocusPaint.setStyle(Style.FILL);

		lineFocusPaint.setColor(line_focus_color);
		lineFocusPaint.setAntiAlias(true);
		lineFocusPaint.setStrokeWidth(line_height);
		lineFocusPaint.setStyle(Style.FILL);

		lineUnFocusPaint.setColor(line_unfocus_color);
		lineUnFocusPaint.setAntiAlias(true);
		lineUnFocusPaint.setStrokeWidth(line_height);
		lineUnFocusPaint.setStyle(Style.FILL);

		circleFocusPaint.setColor(circle_focus_color);
		circleFocusPaint.setAntiAlias(true);
		circleFocusPaint.setStyle(Style.FILL);

		circleUnFocusPaint.setColor(circle_unfocus_color);
		circleUnFocusPaint.setAntiAlias(true);
		circleUnFocusPaint.setStyle(Style.FILL);

		bitmap_bgPaint.setStyle(Paint.Style.FILL);
		bitmap_bgPaint.setAntiAlias(true);
	}

	private void parseAttributes(TypedArray a) {
		metrics = getContext().getResources().getDisplayMetrics();
		circle_radius = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, circle_radius, metrics);
		text_sizes = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, text_sizes, metrics);
		line_height = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, line_height, metrics);
		text_top_padding = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, text_top_padding, metrics);
		circle_padding = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, circle_padding, metrics);

		circle_radius = (int) a.getDimension(
				R.styleable.myStyleSeekBar_circle_radius, circle_radius);
		text_sizes = (int) a.getDimension(
				R.styleable.myStyleSeekBar_text_sizes, text_sizes);
		text_top_padding = (int) a.getDimension(
				R.styleable.myStyleSeekBar_text_top_padding, text_top_padding);
		line_height = (int) a.getDimension(
				R.styleable.myStyleSeekBar_line_height, line_height);
		circle_padding = (int) a.getDimension(
				R.styleable.myStyleSeekBar_circle_padding, circle_padding);

		text_focus_color = a.getColor(
				R.styleable.myStyleSeekBar_text_focus_color, text_focus_color);
		text_unfocus_color = a.getColor(
				R.styleable.myStyleSeekBar_text_unfocus_color,
				text_unfocus_color);
		line_focus_color = a.getColor(
				R.styleable.myStyleSeekBar_line_focus_color, line_focus_color);
		line_unfocus_color = a.getColor(
				R.styleable.myStyleSeekBar_line_unfocus_color,
				line_unfocus_color);
		circle_focus_color = a.getColor(
				R.styleable.myStyleSeekBar_circle_focus_color,
				circle_focus_color);
		circle_unfocus_color = a.getColor(
				R.styleable.myStyleSeekBar_circle_unfocus_color,
				circle_unfocus_color);
		focus_bgId = a.getResourceId(R.styleable.myStyleSeekBar_focus_bg, 0);
		if (focus_bgId != 0) {
			Resources r = this.getContext().getResources();
			BitmapDrawable bmpDraw = (BitmapDrawable) r.getDrawable(focus_bgId);
			focus_bg = bmpDraw.getBitmap();
		}
		a.recycle();
	}

	protected void drawUnfocus(Canvas canvas) {
		float viewWidth = getRight() - getLeft();
		float viewHeight = getBottom() - getTop();
		int bt_w = 0;
		int bt_h = 0;
		if (focus_bg != null) {
			bt_w = focus_bg.getWidth();
			bt_h = focus_bg.getHeight();
		}
		String basetxt = textValue[0];

		if (textValue[0].length() < textValue[totalSteps - 1].length()) {
			basetxt = textValue[totalSteps - 1];
		}
		int basetxtWidth = getTextWidth(basetxt, textUnFocusPaint);
		Log.d("test", "basetxtWidth=" + basetxtWidth);
		int startX = basetxtWidth / 2;
		int startY = circle_radius;
		int stopX = (int) viewWidth - basetxtWidth / 2;
		int stopY = circle_radius;
		if (bt_h != 0) {
			startY = bt_h / 2;
			stopY = bt_h / 2;
		}
		canvas.drawLine(startX, startY, stopX, stopY, lineUnFocusPaint);

		int onLineWidth = (int) (viewWidth - circle_padding * 2 - basetxtWidth)
				/ (totalSteps - 1);
		int cx = circle_padding + basetxtWidth / 2;
		int cy = circle_radius;
		if (bt_h != 0) {
			cy = bt_h / 2;
		}
		int valueX = 0;
		int valueY = 0;
		for (int i = 0; i < totalSteps; i++) {
			cx += onLineWidth;
			if (i == 0) {
				cx = circle_padding + basetxtWidth / 2;
			}
			if (i == (totalSteps - 1)) {
				cx = (int) (viewWidth - circle_padding - basetxtWidth / 2);
			}
			canvas.drawCircle(cx, cy, circle_radius, circleUnFocusPaint);

			valueX = cx - getTextWidth(textValue[i], textUnFocusPaint) / 2;
			valueY = (int) (cy * 2 + text_top_padding + getTextHeight(
					textValue[i], textUnFocusPaint) / 2);
			canvas.drawText(textValue[i], (int) valueX, valueY,
					textUnFocusPaint);

		}

	}

	protected void drawfocus(Canvas canvas) {
		float viewWidth = getRight() - getLeft();
		float viewHeight = getBottom() - getTop();
		int bt_w = 0;
		int bt_h = 0;
		if (focus_bg != null) {
			bt_w = focus_bg.getWidth();
			bt_h = focus_bg.getHeight();
		}

		String basetxt = textValue[0];

		if (textValue[0].length() < textValue[totalSteps - 1].length()) {
			basetxt = textValue[totalSteps - 1];
		}
		int basetxtWidth = getTextWidth(basetxt, textFocusPaint);
		Log.d("test", "basetxtWidth=" + basetxtWidth);
		int startX = basetxtWidth / 2;
		int startY = circle_radius;
		int stopX = basetxtWidth / 2 + circle_padding
				+ (int) (viewWidth - circle_padding * 2 - basetxtWidth)
				/ (totalSteps - 1) * (steps - 1)
				+ (int) (viewWidth - circle_padding * 2 - basetxtWidth)
				/ (totalSteps - 1) / 2;
		int stopY = circle_radius;
		if (steps == totalSteps) {
			stopX = (int) viewWidth - basetxtWidth / 2;
		}
		if (bt_h != 0) {
			startY = bt_h / 2;
			stopY = bt_h / 2;
		}
		canvas.drawLine(startX, startY, stopX, stopY, lineFocusPaint);

		int onLineWidth = (int) (viewWidth - circle_padding * 2 - basetxtWidth)
				/ (totalSteps - 1);
		int cx = circle_padding + basetxtWidth / 2;
		int cy = circle_radius;

		if (bt_h != 0) {
			cy = bt_h / 2;
		}
		int valueX = 0;
		int valueY = 0;
		for (int i = 0; i < steps; i++) {
			cx += onLineWidth;
			if (i == 0) {
				cx = circle_padding + basetxtWidth / 2;
			}
			if (i == (totalSteps - 1)) {
				cx = (int) (viewWidth - circle_padding - basetxtWidth / 2);
			}
			if (i == (steps - 1) && focus_bg != null) {
				float left = cx - bt_w / 2;
				float top = cy - bt_h / 2;
				float right = cx + bt_w / 2;
				float bottom = cy + bt_h / 2;

				RectF center = new RectF(left, top, right, bottom);
				canvas.drawBitmap(focus_bg, null, center, bitmap_bgPaint);
			} else {
				canvas.drawCircle(cx, cy, circle_radius, circleFocusPaint);
			}

			valueX = cx - getTextWidth(textValue[i], textUnFocusPaint) / 2;
			valueY = (int) (cy * 2 + text_top_padding + getTextHeight(
					textValue[i], textUnFocusPaint) / 2);
			if (i == (steps - 1)) {
				canvas.drawText(textValue[i], (int) valueX, valueY,
						textFocusPaint);
			} else {
				canvas.drawText(textValue[i], (int) valueX, valueY,
						texthasFocusPaint);
			}

		}
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (texts != null && texts.length() > 0) {
			textValue = texts.split(":");
			totalSteps = textValue.length;
		}
		if (totalSteps > 1) {
			drawUnfocus(canvas);
		}
		if (steps > 0 && steps <= totalSteps) {
			drawfocus(canvas);
		}
		int viewWidth = this.getWidth();
		int viewHeight = this.getHeight();
		Log.d("test", "viewWidth=" + viewWidth + " viewHeight=" + viewHeight);
		viewWidth = getRight() - getLeft();
		viewHeight = getBottom() - getTop();
		Log.d("test", "viewWidth=" + viewWidth + " viewHeight=" + viewHeight);
		// drawCenter(canvas);
		// canvas.drawArc(circleBounds, 360, 360, false, rimPaint);

	}

	public int getTextWidth(String text, Paint paint) {
		Rect bounds = new Rect();
		paint.getTextBounds(text, 0, text.length(), bounds);
		int width = bounds.left + bounds.width();
		return width;
	}

	public int getTextHeight(String text, Paint paint) {
		Rect bounds = new Rect();
		paint.getTextBounds(text, 0, text.length(), bounds);
		int height = bounds.bottom + bounds.height();
		return height;
	}
}
