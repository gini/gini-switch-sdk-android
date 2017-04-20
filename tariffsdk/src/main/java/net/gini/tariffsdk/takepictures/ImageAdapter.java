package net.gini.tariffsdk.takepictures;


import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import net.gini.tariffsdk.R;
import net.gini.tariffsdk.documentservice.Image;
import net.gini.tariffsdk.documentservice.State;
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
        final State processingState = image.getProcessingState();
        holder.mImageView.setImageURI(uri);
        //TODO
        holder.mProgressBar.setVisibility(
                processingState == State.PROCESSING ? View.VISIBLE : View.GONE);

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

    void hideLoadingForImage(final Image image) {

        final int position = mImageList.indexOf(image);
        mImageList.remove(position);
        mImageList.add(position, image);
        notifyItemChanged(position);
    }

    void setImages(List<Image> images) {
        mImageList.clear();
        mImageList.addAll(images);
        notifyDataSetChanged();
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
