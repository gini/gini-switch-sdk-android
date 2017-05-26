package net.gini.tariffsdk.camera;


import static android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE;

import static net.gini.tariffsdk.camera.GiniCameraUtil.getRotationValueForLandscape;
import static net.gini.tariffsdk.camera.GiniCameraUtil.getRotationValueForPortrait;

import android.hardware.Camera;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.List;

/**
 * An implementation of the GiniCamera interface, with camera 1 which has been deprecated in
 * Android 21 and have been replaced with camera2
 */
@SuppressWarnings("deprecation")
public class Camera1 implements GiniCamera, SurfaceHolder.Callback {

    private static final String TAG = Camera1.class.getSimpleName();
    private final SurfaceHolder mHolder;
    @Nullable
    private Camera mCamera;
    @NonNull
    private Camera.CameraInfo mCameraInfo = new Camera.CameraInfo();
    private int mCurrentOrientation;
    @NonNull
    private Orientation mOrientation = Orientation.DEFAULT;

    public Camera1(@NonNull final SurfaceView cameraPreview) {

        mHolder = cameraPreview.getHolder();
    }

    @Override
    public boolean activateFlash() {
        if (mCamera != null) {
            Camera.Parameters parameters = mCamera.getParameters();
            final List<String> supportedFlashModes = parameters.getSupportedFlashModes();
            if (supportedFlashModes != null && supportedFlashModes
                    .contains(Camera.Parameters.FLASH_MODE_ON)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
                mCamera.setParameters(parameters);
                return true;
            }
        }
        return false;
    }

    @Override
    public void deactivateFlash() {
        if (mCamera != null) {
            Camera.Parameters parameters = mCamera.getParameters();
            final List<String> supportedFlashModes = parameters.getSupportedFlashModes();
            if (supportedFlashModes != null && supportedFlashModes
                    .contains(Camera.Parameters.FLASH_MODE_OFF)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                mCamera.setParameters(parameters);
            }
        }
    }

    @Override
    public void setPreviewOrientation(final Orientation orientation,
            final int currentOrientation) {
        mOrientation = orientation;
        mCurrentOrientation = currentOrientation;
    }

    @Override
    public void start() {

        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
        mCamera = getCameraInstance();
        mCameraInfo = getCameraInfo();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void stop() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
            mHolder.removeCallback(this);
    }

    @Override
    public void surfaceChanged(final SurfaceHolder surfaceHolder, final int i, final int width,
            final int height) {
        try {
            if (mCamera != null) {
                mCamera.setPreviewDisplay(surfaceHolder);
                mCamera.stopPreview();
                final Camera.Parameters parameters = mCamera.getParameters();

                final List<Camera.Size> supportedPreviewSizes = parameters
                        .getSupportedPreviewSizes();
                if (supportedPreviewSizes != null) {
                    final Camera.Size optimalPreviewSize = GiniCameraUtil.getOptimalPreviewSize(
                            supportedPreviewSizes, width, height);

                    if (parameters.getSupportedPreviewSizes().contains(optimalPreviewSize)) {
                        parameters.setPreviewSize(optimalPreviewSize.width,
                                optimalPreviewSize.height);
                    }
                }

                final List<String> supportedFocusModes = parameters.getSupportedFocusModes();
                if (supportedFocusModes != null && supportedFocusModes
                        .contains(FOCUS_MODE_CONTINUOUS_PICTURE)) {
                    parameters.setFocusMode(FOCUS_MODE_CONTINUOUS_PICTURE);
                }

                mCamera.setParameters(parameters);

                if (mOrientation != Orientation.DEFAULT) {
                    mCamera.setDisplayOrientation(
                            getOrientation(mOrientation, mCurrentOrientation));
                }

                mCamera.startPreview();
            }

        } catch (Exception e) {
            Log.d(TAG, "Error setting camera preview: " + e.getLocalizedMessage());
        }
    }

    @Override
    public void surfaceCreated(final SurfaceHolder surfaceHolder) {
        //Initialisation of the preview in onSurfaceChanged, since older devices tend to have
        // problems here.
    }

    @Override
    public void surfaceDestroyed(final SurfaceHolder surfaceHolder) {

    }

    @Override
    public void takePicture(@NonNull final JpegCallback jpegCallback) {
        if (mCamera != null) {
            mCamera.takePicture(null, null, new Camera.PictureCallback() {
                @Override
                public void onPictureTaken(final byte[] bytes, final Camera camera) {
                    if (bytes != null) {
                        jpegCallback.onPictureTaken(bytes);
                    } else {
                        throw new GiniCameraException("Picture byte array was null");
                    }
                }
            });
        }
    }

    private Camera.CameraInfo getCameraInfo() {

        final Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        //Right now just camera 0
        Camera.getCameraInfo(0, cameraInfo);

        return cameraInfo;
    }

    private Camera getCameraInstance() {
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (Exception e) {
            Log.e(TAG, "Camera not available: " + e.getLocalizedMessage());
        }
        return camera;
    }

    private int getOrientation(final Orientation orientation, final int deviceOrientation) {
        final int rotation = orientation == Orientation.PORTRAIT ? getRotationValueForPortrait(
                deviceOrientation) : getRotationValueForLandscape(deviceOrientation);
        if (mCameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            return (360 - (mCameraInfo.orientation + rotation) % 360) % 360;
        } else {  // back-facing
            return (mCameraInfo.orientation - rotation + 360) % 360;
        }
    }
}
