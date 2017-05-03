package net.gini.tariffsdk;


import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import net.gini.tariffsdk.utils.AutoRotateImageView;

import java.util.ArrayList;
import java.util.List;

class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private final List<Image> mImageList;
    private final Listener mListener;

    ImageAdapter(Listener listener) {
        mListener = listener;
        mImageList = new ArrayList<>();
    }

    @Override
    public int getItemCount() {
        return mImageList.size();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Image image = mImageList.get(position);
        final Uri uri = image.getUri();
        final ImageState processingState = image.getProcessingState();
        holder.mImageView.setImageURI(uri);
        //TODO
        holder.mProgressBar.setVisibility(
                processingState == ImageState.PROCESSING ? View.VISIBLE : View.GONE);

        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                mListener.onImageClicked(image);
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_list_item,
                parent, false);
        return new ViewHolder(view);
    }

    void setImages(List<Image> images) {
        mImageList.clear();
        mImageList.addAll(images);
        notifyDataSetChanged();
    }

    void updateImageState(final Image image) {
        final int position = mImageList.indexOf(image);
        if (position >= 0) {
            mImageList.remove(position);
            mImageList.add(position, image);
            notifyItemChanged(position);
        }
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

    interface Listener {
        void onImageClicked(final Image image);
    }
}
