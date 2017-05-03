package net.gini.tariffsdk;


import android.net.Uri;

class Image {

    private final Uri mUri;
    private ImageState mProcessingState;

    Image(final Uri uri, final ImageState processingState) {
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

    ImageState getProcessingState() {
        return mProcessingState;
    }

    void setProcessingState(final ImageState processingState) {
        mProcessingState = processingState;
    }

    Uri getUri() {
        return mUri;
    }
}
