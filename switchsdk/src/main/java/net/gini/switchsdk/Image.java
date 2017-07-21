package net.gini.switchsdk;


import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

class Image implements Parcelable {

    public static final Parcelable.Creator<Image> CREATOR = new Parcelable.Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel source) {
            return new Image(source);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };
    private final Uri mUri;
    private ImageState mProcessingState;

    Image(final Uri uri, final ImageState processingState) {
        mUri = uri;
        mProcessingState = processingState;
    }

    protected Image(Parcel in) {
        this.mUri = in.readParcelable(Uri.class.getClassLoader());
        int tmpMProcessingState = in.readInt();
        this.mProcessingState =
                tmpMProcessingState == -1 ? null : ImageState.values()[tmpMProcessingState];
    }

    @Override
    public int describeContents() {
        return 0;
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

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mUri, flags);
        dest.writeInt(this.mProcessingState == null ? -1 : this.mProcessingState.ordinal());
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
