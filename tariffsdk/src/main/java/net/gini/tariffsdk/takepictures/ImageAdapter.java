package net.gini.tariffsdk.takepictures;


import android.net.Uri;
import android.support.media.ExifInterface;
import android.support.v4.util.SimpleArrayMap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import net.gini.tariffsdk.R;

import java.io.IOException;

class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder>{

    private final SimpleArrayMap<Uri, Boolean> mImageMap;

    ImageAdapter() {
        mImageMap = new SimpleArrayMap<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Uri uri = mImageMap.keyAt(position);
        final boolean processing = mImageMap.get(uri);
        holder.mImageView.setImageURI(uri);
        holder.mProgressBar.setVisibility(processing ? View.VISIBLE : View.GONE);

        float degrees = getRequiredRotationDegrees(uri);
        holder.mImageView.setRotation(degrees);
    }

    void hideLoadingForImage(final Uri imageUri) {
        mImageMap.put(imageUri, false);
        final int position = mImageMap.indexOfKey(imageUri);
        notifyItemChanged(position);
    }

    void setImages(SimpleArrayMap<Uri, Boolean> images) {
        mImageMap.clear();
        mImageMap.putAll(images);
        notifyDataSetChanged();
    }

    private float getRequiredRotationDegrees(final Uri imageUri) {

        final ExifInterface exif;
        try {
            exif = new ExifInterface(imageUri.getPath());
            final String orientation = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
            switch (orientation) {
                case "3":
                    return 180;
                case "0":
                case "6":
                    return 90;
                case "8":
                    return 270;
            }

        } catch (IOException ignored) {
        }

        return 0;
    }

    @Override
    public int getItemCount() {
        return mImageMap.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mImageView;

        ProgressBar mProgressBar;

        ViewHolder(final View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.image_view);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar);
        }
    }
}
