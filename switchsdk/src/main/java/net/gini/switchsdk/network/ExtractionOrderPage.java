package net.gini.switchsdk.network;


public class ExtractionOrderPage {

    private final String mSelf;
    private final Status mStatus;

    public ExtractionOrderPage(final String self, final Status status) {
        mSelf = self;
        mStatus = status;
    }

    public String getSelf() {
        return mSelf;
    }

    public Status getStatus() {
        return mStatus;
    }

    public enum Status {
        processing, failed, processed
    }
}
