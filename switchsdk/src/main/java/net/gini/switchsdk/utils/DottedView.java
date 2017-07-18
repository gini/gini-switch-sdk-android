package net.gini.switchsdk.utils;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class DottedView extends View {
    private final Path mLeftPath;
    private final Path mLowerPath;
    private final Paint mPaintLine;
    private final Path mRightPath;
    private final Path mUpperPath;

    public DottedView(final Context context) {
        this(context, null);
    }

    public DottedView(final Context context,
            @Nullable final AttributeSet attrs) {
        super(context, attrs);

        mUpperPath = new Path();
        mLowerPath = new Path();
        mLeftPath = new Path();
        mRightPath = new Path();

        final float lineWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2,
                getContext().getResources().getDisplayMetrics());
        mPaintLine = new Paint();
        mPaintLine.setFlags(Paint.ANTI_ALIAS_FLAG);
        mPaintLine.setAntiAlias(true);
        mPaintLine.setColor(Color.WHITE);
        mPaintLine.setStyle(Paint.Style.STROKE);
        mPaintLine.setStrokeWidth(lineWidth);
        DashPathEffect effect = new DashPathEffect(new float[]{4, 4}, 0);
        mPaintLine.setPathEffect(effect);
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mUpperPath, mPaintLine);
        canvas.drawPath(mLowerPath, mPaintLine);
        canvas.drawPath(mLeftPath, mPaintLine);
        canvas.drawPath(mRightPath, mPaintLine);
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        final int width = MeasureSpec.getSize(widthMeasureSpec);
        final int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        mUpperPath.moveTo(0, 0);
        mUpperPath.quadTo(width, 0, width, 0);
        mLowerPath.moveTo(0, height);
        mLowerPath.quadTo(0, height, width, height);

        mLeftPath.moveTo(0, 0);
        mLeftPath.quadTo(0, 0, 0, height);
        mRightPath.moveTo(width, 0);
        mRightPath.quadTo(width, 0, width, height);
    }
}
