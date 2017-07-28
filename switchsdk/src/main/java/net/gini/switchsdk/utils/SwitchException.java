package net.gini.switchsdk.utils;


public class SwitchException extends Exception {
    private ErrorCode mErrorCode;
    private String mMessage = "";

    public SwitchException(final String message) {
        super(message);
        mMessage = message;
    }

    public SwitchException(final ErrorCode errorCode) {
        mErrorCode = errorCode;
    }

    public SwitchException(final ErrorCode errorCode, final String message) {

        mErrorCode = errorCode;
        mMessage = message;
    }

    public String errorMessage() {
        switch (mErrorCode) {
            case CREATE_USER:
                return "Create User failed." + mMessage;
            case REQUEST_CLIENT_TOKEN:
                return "Requesting client token failed." + mMessage;
            case REQUEST_USER_TOKEN:
                return "Request user token failed." + mMessage;
            case REQUEST_CONFIGURATION:
                return "Request configuration failed." + mMessage;
            case GET_ORDER_STATE:
                return "Get order state failed." + mMessage;
            case RETRIEVE_EXTRACTIONS:
                return "Retrieving extractions failed." + mMessage;
            case UPLOAD_IMAGE:
                return "Uploading image failed." + mMessage;
        }
        return mMessage;
    }

    public enum ErrorCode {
        CREATE_USER, REQUEST_CLIENT_TOKEN, REQUEST_USER_TOKEN, CREATE_EXTRACTION_ORDER,
        REQUEST_CONFIGURATION, GET_ORDER_STATE, RETRIEVE_EXTRACTIONS, UPLOAD_IMAGE
    }
}
