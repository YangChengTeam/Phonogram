package com.yc.phonogram.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class SimpleProgressbar extends ProgressBar {

    private static final String TAG = "SimpleProgressbar";

    public static final int DEFAULT_UNREACHED_COLOR = 0xFFC84900;
    public static final int DEFAULT_REACHED_COLOR = 0xFFFF9D02;

    /**
     * 画笔
     */
    private Paint paint;
    /**
     * 未到达进度条颜色
     */
    private int unreachedColor;
    /**
     * 已到达进度条颜色
     */
    private int reachedColor;

    public SimpleProgressbar(Context context) {
        //        super(context);
        this(context, null);
    }

    public SimpleProgressbar(Context context, AttributeSet attrs) {
        //        super(context, attrs);
        this(context, attrs, 0);
    }

    public SimpleProgressbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        paint = new Paint();
        unreachedColor = DEFAULT_UNREACHED_COLOR;
        reachedColor = DEFAULT_REACHED_COLOR;
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        //        super.onDraw(canvas);
        // 获取画布的宽高
        int width = getWidth();
        int height = getHeight();
        // 获取进度条的实际宽高
        int lineWidth = width - getPaddingLeft() - getPaddingRight();
        int lineHeight = height - getPaddingTop() - getPaddingBottom();
        // 获取当前进度
        float ratio = getProgress() * 1.0f / getMax();
        // 获取未完成进度大小
        int unreachedWidth = (int) (lineWidth * (1 - ratio));
        // 获取已完成进度大小
        int reachedWidth = lineWidth - unreachedWidth;
        // 绘制已完成进度条，设置画笔颜色和大小
        paint.setColor(reachedColor);
        paint.setStrokeWidth(lineHeight);
        // 计算已完成进度条起点和终点的坐标
        int startX = getPaddingLeft();
        int startY = getHeight() / 2;
        int stopX = startX + reachedWidth;
        int stopY = startY;
        // 画线
        canvas.drawLine(startX, startY, stopX, stopY, paint);
        // 设置画笔颜色
        paint.setColor(unreachedColor);

        startX = getPaddingLeft() + reachedWidth;
        stopX = width - getPaddingRight();
        canvas.drawLine(startX, startY, stopX, stopY, paint);
    }

}