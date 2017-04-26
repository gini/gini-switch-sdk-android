package net.gini.tariffsdk.camera;


/**
 * Gini Camera exception, is thrown whenever something goes wrong.
 */
public class GiniCameraException extends RuntimeException {

    public GiniCameraException(final String message) {
        super(message);
    }

    public GiniCameraException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public GiniCameraException(final Throwable cause) {
        super(cause);
    }
}
