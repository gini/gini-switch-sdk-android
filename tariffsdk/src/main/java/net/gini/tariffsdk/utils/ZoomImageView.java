package net.gini.tariffsdk.utils;


import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

public class ZoomImageView extends android.support.v7.widget.AppCompatImageView {

    private static final int MAX_DIFF_FOR_CLICK = 3;
    private static final float MAX_SCALE = 4f;
    private static final float MIN_SCALE = 1f;
    private float[] mCriticPoints;
    private PointF mLastTouch = new PointF();
    private Matrix mMatrix;
    private Mode mMode = Mode.NONE;
    private float mRight, mBottom, mOriginalBitmapWidth, mOriginalBitmapHeight;
    private float mSaveScale = 1f;
    private ScaleGestureDetector mScaleDetector;
    private PointF mStartTouch = new PointF();

    public ZoomImageView(Context context) {
        this(context, null);
    }
    public ZoomImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public ZoomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int bmHeight = getBitmapHeight();
        int bmWidth = getBitmapWidth();

        float width = getMeasuredWidth();
        float height = getMeasuredHeight();
        float scale = 1;

        // If image is bigger then display fit it to screen.
        if (width < bmWidth || height < bmHeight) {
            scale = width > height ? height / bmHeight : width / bmWidth;
        } else {
            scale = width > height ? width / bmWidth : height / bmHeight;
        }

        mMatrix.setScale(scale, scale);
        mSaveScale = 1f;

        mOriginalBitmapWidth = scale * bmWidth;
        mOriginalBitmapHeight = scale * bmHeight;

        // Center the image
        final float redundantYSpace = (height - mOriginalBitmapHeight);
        final float redundantXSpace = (width - mOriginalBitmapWidth);

        mMatrix.postTranslate(redundantXSpace / 2, redundantYSpace / 2);

        setImageMatrix(mMatrix);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleDetector.onTouchEvent(event);

        mMatrix.getValues(mCriticPoints);
        float translateX = mCriticPoints[Matrix.MTRANS_X];
        float translateY = mCriticPoints[Matrix.MTRANS_Y];
        PointF currentPoint = new PointF(event.getX(), event.getY());

        switch (event.getAction()) {
            //when one finger is touching
            case MotionEvent.ACTION_DOWN:
                mLastTouch.set(event.getX(), event.getY());
                mStartTouch.set(mLastTouch);
                mMode = Mode.DRAG;
                break;
            //when two fingers are touching
            case MotionEvent.ACTION_POINTER_DOWN:
                mLastTouch.set(event.getX(), event.getY());
                mStartTouch.set(mLastTouch);
                mMode = Mode.ZOOM;
                break;
            //when a finger moves
            //If mode is applicable move image
            case MotionEvent.ACTION_MOVE:

                //if the mode is ZOOM or
                //if the mode is DRAG and already zoomed
                if (mMode == Mode.ZOOM || (mMode == Mode.DRAG && mSaveScale > MIN_SCALE)) {

                    float deltaX = currentPoint.x - mLastTouch.x;// x difference
                    float deltaY = currentPoint.y - mLastTouch.y;// y difference
                    float scaleWidth = Math.round(
                            mOriginalBitmapWidth * mSaveScale);// width after applying current scale
                    float scaleHeight = Math.round(mOriginalBitmapHeight
                            * mSaveScale);// height after applying current scale

                    // Move image to lef or right if its width is bigger than display width
                    if (scaleWidth > getWidth()) {
                        if (translateX + deltaX > 0) {
                            deltaX = -translateX;
                        } else if (translateX + deltaX < -mRight) {
                            deltaX = -(translateX + mRight);
                        }
                    } else {
                        deltaX = 0;
                    }
                    // Move image to up or bottom if its height is bigger than display height
                    if (scaleHeight > getHeight()) {
                        if (translateY + deltaY > 0) {
                            deltaY = -translateY;
                        } else if (translateY + deltaY < -mBottom) {
                            deltaY = -(translateY + mBottom);
                        }
                    } else {
                        deltaY = 0;
                    }

                    //move the image with the matrix
                    mMatrix.postTranslate(deltaX, deltaY);
                    //set the last touch location to the current
                    mLastTouch.set(currentPoint.x, currentPoint.y);
                }
                break;
            //first finger is lifted
            case MotionEvent.ACTION_UP:
                mMode = Mode.NONE;
                int xDiff = (int) Math.abs(currentPoint.x - mStartTouch.x);
                int yDiff = (int) Math.abs(currentPoint.y - mStartTouch.y);
                if (xDiff < MAX_DIFF_FOR_CLICK && yDiff < MAX_DIFF_FOR_CLICK)
                    performClick();
                break;
            // second finger is lifted
            case MotionEvent.ACTION_POINTER_UP:
                mMode = Mode.NONE;
                break;
        }
        setImageMatrix(mMatrix);
        invalidate();
        return true;
    }

    private int getBitmapHeight() {
        final Drawable drawable = getDrawable();
        if (drawable != null) {
            return drawable.getIntrinsicHeight();
        }
        return 0;
    }

    private int getBitmapWidth() {
        final Drawable drawable = getDrawable();
        if (drawable != null) {
            return drawable.getIntrinsicWidth();
        }
        return 0;
    }

    private void init(Context context) {
        super.setClickable(true);
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        mCriticPoints = new float[9];
        mMatrix = new Matrix();
        setImageMatrix(mMatrix);
        setScaleType(ScaleType.MATRIX);
    }

    private enum Mode {
        NONE, DRAG, ZOOM
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scaleFactor = detector.getScaleFactor();
            float newScale = mSaveScale * scaleFactor;
            if (newScale < MAX_SCALE && newScale > MIN_SCALE) {
                mSaveScale = newScale;
                float width = getWidth();
                float height = getHeight();
                mRight = (mOriginalBitmapWidth * mSaveScale) - width;
                mBottom = (mOriginalBitmapHeight * mSaveScale) - height;

                float scaledBitmapWidth = mOriginalBitmapWidth * mSaveScale;
                float scaledBitmapHeight = mOriginalBitmapHeight * mSaveScale;

                if (scaledBitmapWidth <= width || scaledBitmapHeight <= height) {
                    mMatrix.postScale(scaleFactor, scaleFactor, width / 2, height / 2);
                } else {
                    mMatrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(),
                            detector.getFocusY());
                }
            }
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            mMode = Mode.ZOOM;
            return true;
        }

    }
}
