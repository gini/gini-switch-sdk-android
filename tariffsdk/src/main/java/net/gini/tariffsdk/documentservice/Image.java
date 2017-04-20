package net.gini.tariffsdk.documentservice;


import android.net.Uri;

public class Image {

    private final Uri mUri;
    private State mProcessingState;

    Image(final Uri uri, final State processingState) {
        mUri = uri;
        mProcessingState = processingState;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Image image = (Image) o;

        return mUri != null ? mUri.equals(image.mUri) : image.mUri == null;

    }

    @Override
    public int hashCode() {
        return mUri != null ? mUri.hashCode() : 0;
    }

    public State getProcessingState() {
        return mProcessingState;
    }

    void setProcessingState(final State processingState) {
        mProcessingState = processingState;
    }

    public Uri getUri() {
        return mUri;
    }
}
