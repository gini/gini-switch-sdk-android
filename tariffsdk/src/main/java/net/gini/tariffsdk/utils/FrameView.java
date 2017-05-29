package net.gini.tariffsdk.utils;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class FrameView extends View {

    private int mHeight;
    private Paint mPaintLine;
    private Paint mPaintRectangle;

    private float mLineLength = 150;

    private static final int WALL_OFFSET = 50;
    private int mWidth;

    public FrameView(final Context context) {
        this(context, null);
    }

    public FrameView(final Context context,
            @Nullable final AttributeSet attrs) {
        super(context, attrs);
        mPaintLine = new Paint();
        mPaintLine.setColor(Color.WHITE);
        mPaintLine.setStyle(Paint.Style.STROKE);
        mPaintLine.setStrokeWidth(2f);

        mPaintRectangle = new Paint();
        mPaintRectangle.setStyle(Paint.Style.FILL);
        mPaintRectangle.setColor(Color.BLACK);
        mPaintRectangle.setAlpha(100);
    }


    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        drawShadowRectangles(canvas);
        drawUpperLeftLines(canvas);
        drawLowerLeftLines(canvas);
        drawUpperRightLines(canvas);
        drawLowerRightLines(canvas);
    }

    private void drawLowerRightLines(final Canvas canvas) {
        canvas.drawLine(mWidth - WALL_OFFSET, mHeight - mLineLength - WALL_OFFSET,
                mWidth - WALL_OFFSET, mHeight - WALL_OFFSET,
                mPaintLine); // |
        canvas.drawLine(mWidth - mLineLength - WALL_OFFSET, mHeight - WALL_OFFSET,
                mWidth - WALL_OFFSET, mHeight - WALL_OFFSET,
                mPaintLine); // -
    }


    private void drawLowerLeftLines(final Canvas canvas) {
        canvas.drawLine(WALL_OFFSET, mHeight - mLineLength - WALL_OFFSET, WALL_OFFSET,
                mHeight - WALL_OFFSET,
                mPaintLine); // |
        canvas.drawLine(WALL_OFFSET, mHeight - WALL_OFFSET, WALL_OFFSET + mLineLength,
                mHeight - WALL_OFFSET,
                mPaintLine); // -
    }

    private void drawUpperLeftLines(final Canvas canvas) {
        canvas.drawLine(WALL_OFFSET, WALL_OFFSET, WALL_OFFSET, WALL_OFFSET + mLineLength,
                mPaintLine); // |
        canvas.drawLine(WALL_OFFSET, WALL_OFFSET, WALL_OFFSET + mLineLength, WALL_OFFSET,
                mPaintLine); // -
    }

    private void drawUpperLeftLines(final Canvas canvas) {
        canvas.drawLine(WALL_OFFSET, WALL_OFFSET, WALL_OFFSET, WALL_OFFSET + mLineLength,
                mPaintLine); // |
        canvas.drawLine(WALL_OFFSET, WALL_OFFSET, WALL_OFFSET + mLineLength, WALL_OFFSET,
                mPaintLine); // -
    }

    private void drawShadowRectangles(final Canvas canvas) {
        //left
        canvas.drawRect(0, 0, WALL_OFFSET, mHeight, mPaintRectangle);
        //right
        canvas.drawRect(mWidth - WALL_OFFSET, 0, mWidth, mHeight, mPaintRectangle);
        //upper
        canvas.drawRect(WALL_OFFSET, 0, mWidth - WALL_OFFSET, WALL_OFFSET, mPaintRectangle);
        //lower
        canvas.drawRect(WALL_OFFSET, mHeight - WALL_OFFSET, mWidth - WALL_OFFSET, mHeight,
                mPaintRectangle);
    }

    private void drawUpperRightLines(final Canvas canvas) {
        canvas.drawLine(mWidth - WALL_OFFSET, WALL_OFFSET, mWidth - WALL_OFFSET,
                WALL_OFFSET + mLineLength,
                mPaintLine); // |
        canvas.drawLine(mWidth - mLineLength - WALL_OFFSET, WALL_OFFSET, mWidth - WALL_OFFSET,
                WALL_OFFSET,
                mPaintLine); // -
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }
}
