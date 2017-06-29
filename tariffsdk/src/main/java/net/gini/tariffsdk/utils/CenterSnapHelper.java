package net.gini.tariffsdk.utils;


import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class CenterSnapHelper extends LinearSnapHelper {
    private OrientationHelper mHorizontalHelper;
    private int mViewPosition;

    @Override
    public int[] calculateDistanceToFinalSnap(@NonNull RecyclerView.LayoutManager layoutManager,
            @NonNull View targetView) {
        int[] out = new int[2];
        out[0] = distanceToCenter(layoutManager, targetView, getHorizontalHelper(layoutManager));
        out[1] = 0;

        mViewPosition = layoutManager.getPosition(targetView);

        return out;
    }

    @Override
    public View findSnapView(RecyclerView.LayoutManager layoutManager) {

        if (layoutManager instanceof LinearLayoutManager) {
            return getView(layoutManager, getHorizontalHelper(layoutManager));
        }

        return super.findSnapView(layoutManager);
    }

    public int getCenteredPosition() {
        return mViewPosition;
    }

    private int distanceToCenter(@NonNull RecyclerView.LayoutManager layoutManager,
            @NonNull View targetView, OrientationHelper helper) {
        final int childCenter = helper.getDecoratedStart(targetView) +
                (helper.getDecoratedMeasurement(targetView) / 2);
        final int containerCenter = getContainerCenter(layoutManager, helper);

        return childCenter - containerCenter;
    }

    private int getContainerCenter(final @NonNull RecyclerView.LayoutManager layoutManager,
            final OrientationHelper helper) {
        final int containerCenter;
        if (layoutManager.getClipToPadding()) {
            containerCenter = helper.getStartAfterPadding() + helper.getTotalSpace() / 2;
        } else {
            containerCenter = helper.getEnd() / 2;
        }
        return containerCenter;
    }

    private OrientationHelper getHorizontalHelper(RecyclerView.LayoutManager layoutManager) {
        if (mHorizontalHelper == null) {
            mHorizontalHelper = OrientationHelper.createHorizontalHelper(layoutManager);
        }
        return mHorizontalHelper;
    }

    private View getView(RecyclerView.LayoutManager layoutManager,
            OrientationHelper helper) {

        if (layoutManager instanceof LinearLayoutManager) {
            int firstChildPos =
                    ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
            int lastChildPos =
                    ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();

            if (firstChildPos == RecyclerView.NO_POSITION
                    || lastChildPos == RecyclerView.NO_POSITION) {
                return null;
            }

            final int containerCenter = getContainerCenter(layoutManager, helper);

            //search for the view
            for (int i = firstChildPos; i <= lastChildPos; i++) {
                View child = layoutManager.findViewByPosition(i);
                final int childSize = helper.getDecoratedMeasurement(child);
                final int childEnd = helper.getDecoratedEnd(child);
                final int childStart = helper.getDecoratedStart(child);

                final int containerLeftBound = containerCenter - (childSize / 2);
                final int containerRightBound = containerCenter + (childSize / 2);

                //child's right corner is in the right half of the container
                if (childEnd >= containerCenter && childEnd < containerRightBound) {
                    return child;
                }
                //child's left corner is in the left half of the container
                if (childStart >= containerLeftBound && childStart < containerCenter) {
                    return child;
                }
            }
        }

        return super.findSnapView(layoutManager);
    }
}
