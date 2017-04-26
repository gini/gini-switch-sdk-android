package net.gini.tariffsdk.camera;


import android.hardware.Camera;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Surface;

import java.util.List;

@SuppressWarnings("deprecation")
class GiniCameraUtil {

    private static final double ASPECT_RATIO_TOLERANCE = 0.1;

    private GiniCameraUtil() {
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

    static int getRotationValueForLandscape(final int deviceOrientation) {
        switch (deviceOrientation) {
            case Surface.ROTATION_0:
                return 90;
            case Surface.ROTATION_90:
                return 180;
            case Surface.ROTATION_180:
                return 270;
            case Surface.ROTATION_270:
                return 0;
        }
        return 90;
    }

    static int getRotationValueForPortrait(final int deviceOrientation) {
        switch (deviceOrientation) {
            case Surface.ROTATION_0:
                return 0;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return 270;
        }
        return 0;
    }
}
