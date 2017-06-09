package net.gini.tariffsdk.camera;


import android.hardware.Camera;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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
}
