package net.gini.tariffsdk.takepictures;


import android.net.Uri;
import android.support.v4.util.SimpleArrayMap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import net.gini.tariffsdk.utils.AutoRotateImageView;
import net.gini.tariffsdk.R;

class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private final Listener mListener;

    interface Listener{
        void onImageClicked(Uri uri);
    }

    private final SimpleArrayMap<Uri, Boolean> mImageMap;

    ImageAdapter(Listener listener) {
        mListener = listener;
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

        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                mListener.onImageClicked(uri);
            }
        });
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

    @Override
    public int getItemCount() {
        return mImageMap.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        AutoRotateImageView mImageView;

        ProgressBar mProgressBar;

        ViewHolder(final View itemView) {
            super(itemView);
            mImageView = (AutoRotateImageView) itemView.findViewById(R.id.image_view);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar);
        }
    }
}
