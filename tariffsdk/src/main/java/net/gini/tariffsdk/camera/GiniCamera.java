package net.gini.tariffsdk.camera;


import android.support.annotation.NonNull;

public interface GiniCamera {

    /**
     * Activates the flash, returns true if successful, false not. Also false when no flash is
     * available
     */
    boolean activateFlash();

    /**
     * Deactivates the flash,  if flash is not supported nothing gets deactivated.
     */
    void deactivateFlash();

    /**
     * Sets the orientation of the preview screen, use Activity.getWindowManager().getDefaultDisplay().getRotation()
     * to get the current orientation
     */
    void setPreviewOrientation(final int currentOrientation);

    void start();

    void stop();

    /**
     * Use this to take a jpeg picture, make also sure to release the camera after it.
     *
     * @param jpegCallback callback that gets called when the picture has been taken
     */
    void takePicture(@NonNull final JpegCallback jpegCallback);

    enum Orientation {
        DEFAULT, LANDSCAPE, PORTRAIT
    }

    /**
     * Callback interface for taking a jpeg picture.
     */
    interface JpegCallback {

        /**
         * On success with the data as byte array
         *
         * @throws GiniCameraException if the data is null in some weird circumstances
         */
        void onPictureTaken(@NonNull final byte[] data, final int orientation)
                throws GiniCameraException;

    }
}
