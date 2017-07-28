package net.gini.switchsdk.camera;


import android.hardware.Camera;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
class GiniCameraUtil {

    private static final double ASPECT_RATIO_TOLERANCE = 0.1;

    private GiniCameraUtil() {
    }

    @Nullable
    static Camera.Size getLargestSize(List<Camera.Size> sizes) {
        Camera.Size bestFit = null;
        for (Camera.Size size : sizes) {
            if (hasFourThreeRatio(size) && (bestFit == null
                    || getArea(bestFit) < getArea(size))) {
                bestFit = size;
            }
        }
        return bestFit;
    }

    @Nullable
    static Camera.Size getLargestSizeWithSimilarAspectRatio(
            @NonNull final List<Camera.Size> sizes, @NonNull final Camera.Size referenceSize) {
        List<Camera.Size> sameAspectSizes = getSameAspectRatioSizes(sizes, referenceSize);
        return getLargestSize(sameAspectSizes);
    }

    @Nullable
    static Camera.Size getOptimalPreviewSize(@NonNull List<Camera.Size> sizes, int width,
            int height) {

        double targetRatio = (double) width / height;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) <= ASPECT_RATIO_TOLERANCE
                    && Math.abs(size.height - height) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - height);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - height) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - height);
                }
            }
        }
        return optimalSize;
    }

    private static long getArea(final Camera.Size size) {
        return size.width * size.height;
    }

    @NonNull
    private static List<Camera.Size> getSameAspectRatioSizes(final @NonNull List<Camera.Size> sizes,
            final @NonNull Camera.Size referenceSize) {
        final float referenceAspectRatio =
                (float) referenceSize.width / (float) referenceSize.height;
        List<Camera.Size> sameAspectSizes = new ArrayList<>();
        for (final Camera.Size size : sizes) {
            final float aspectRatio = (float) size.width / (float) size.height;
            if (isSimilarAspectRatio(aspectRatio, referenceAspectRatio)) {
                sameAspectSizes.add(size);
            }
        }
        return sameAspectSizes;
    }

    private static boolean hasFourThreeRatio(@NonNull final Camera.Size size) {
        return Math.abs((float) size.width / (float) size.height - 4.f / 3.f) < 0.001;
    }

    private static boolean isSimilarAspectRatio(final float aspectRatio,
            final float referenceAspectRatio) {
        return Math.abs(aspectRatio - referenceAspectRatio) < ASPECT_RATIO_TOLERANCE;
    }
}
