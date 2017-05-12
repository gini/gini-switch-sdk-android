package net.gini.tariffsdk;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import net.gini.tariffsdk.utils.AutoRotateImageView;

import java.util.ArrayList;
import java.util.List;

class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int EMPTY_VIEW_TYPE = 1;
    private final Context mContext;
    private final List<Image> mImageList;
    private final Listener mListener;

    ImageAdapter(Context context, Listener listener) {
        mContext = context;
        mListener = listener;
        mImageList = new ArrayList<>();
    }

    @Override
    public int getItemCount() {
        if (mImageList.size() == 0) {
            return 1;
        }
        return mImageList.size() + 1;
    }

    @Override
    public int getItemViewType(final int position) {
        if (position == mImageList.size()) {
            return EMPTY_VIEW_TYPE;
        }
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            final ViewHolder viewHolder = (ViewHolder) holder;
            final Image image = mImageList.get(position);
            final Uri uri = image.getUri();
            final ImageState processingState = image.getProcessingState();
            viewHolder.mImageView.setImageURI(uri);
            //TODO
            viewHolder.mProgressBar.setVisibility(
                    processingState == ImageState.PROCESSING ? View.VISIBLE : View.GONE);
            viewHolder.mStatusIndicator.setVisibility(
                    processingState == ImageState.PROCESSING ? View.GONE : View.VISIBLE);
            viewHolder.mStatusIndicator.setBackgroundColor(
                    processingState == ImageState.SUCCESSFULLY_PROCESSED ? Color.GREEN : Color.RED);


            viewHolder.mStateImageView.setVisibility(
                    processingState == ImageState.PROCESSING ? View.GONE : View.VISIBLE);
            Drawable drawable = getImageDrawableFromState(processingState);
            viewHolder.mStateImageView.setImageDrawable(drawable);

            viewHolder.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    mListener.onImageClicked(image);
                }
            });
        } else {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    mListener.onCameraClicked();
                }
            });
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        if (viewType == EMPTY_VIEW_TYPE) {
            final View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.image_list_item_empty, parent, false);
            return new EmptyViewHolder(view);
        }
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

    private Drawable getImageDrawableFromState(final ImageState state) {
        if (state == ImageState.SUCCESSFULLY_PROCESSED) {
            return ContextCompat.getDrawable(mContext, android.R.drawable.ic_input_get);
        }
        return ContextCompat.getDrawable(mContext, android.R.drawable.ic_delete);
    }

    private static class EmptyViewHolder extends RecyclerView.ViewHolder {

        EmptyViewHolder(final View itemView) {
            super(itemView);
        }
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        AutoRotateImageView mImageView;
        ProgressBar mProgressBar;
        ImageView mStateImageView;
        View mStatusIndicator;


        ViewHolder(final View itemView) {
            super(itemView);
            mImageView = (AutoRotateImageView) itemView.findViewById(R.id.image_view);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar);
            mStatusIndicator = itemView.findViewById(R.id.status_indicator_view);
            mStateImageView = (ImageView) itemView.findViewById(R.id.image_state);
        }
    }

    interface Listener {
        void onCameraClicked();

        void onImageClicked(final Image image);
    }
}
