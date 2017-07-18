package net.gini.switchsdk;


import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.gini.switchsdk.utils.AutoRotateImageView;

import java.util.ArrayList;
import java.util.List;

class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int EMPTY_VIEW_TYPE = 1;
    private final Context mContext;
    private final List<Image> mImageList;
    private final Listener mListener;
    private final int mNegativeColor;
    private final int mPositiveColor;
    private int mSelectedElement;


    ImageAdapter(Context context, Listener listener, final int positiveColor,
            final int negativeColor) {
        mContext = context;
        mListener = listener;
        mPositiveColor = positiveColor;
        mNegativeColor = negativeColor;
        mImageList = new ArrayList<>();
        mSelectedElement = 0;
    }

    @Override
    public int getItemCount() {
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
            viewHolder.mProgressBar.setVisibility(
                    processingState == ImageState.PROCESSING ? View.VISIBLE : View.GONE);
            viewHolder.mStatusIndicator.setVisibility(
                    processingState == ImageState.PROCESSING ? View.GONE : View.VISIBLE);
            viewHolder.mStateImageView.setVisibility(
                    processingState == ImageState.PROCESSING ? View.GONE : View.VISIBLE);

            final int processingColor = getProcessingStateColor(processingState);
            viewHolder.mStatusIndicator.setBackgroundColor(processingColor);

            final Drawable drawable = getImageDrawableFromState(processingState);
            viewHolder.mStateImageView.setImageDrawable(drawable);
            viewHolder.mSelector.setVisibility(
                    mSelectedElement == position ? View.VISIBLE : View.GONE);

            viewHolder.mItemLabel.setText(
                    mContext.getString(R.string.documentbar_item_label, (position + 1)));

            viewHolder.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    mListener.onImageClicked(image, holder.getAdapterPosition());
                }
            });
        } else if (holder instanceof EmptyViewHolder) {
            final EmptyViewHolder viewHolder = (EmptyViewHolder) holder;
            viewHolder.mText.setText(shouldShowPlusSymbol() ? "+" : null);
            viewHolder.mSelector.setVisibility(
                    mSelectedElement == position ? View.VISIBLE : View.GONE);

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


    void deleteImage(final Image selectedImage) {
        final int position = mImageList.indexOf(selectedImage);
        mImageList.remove(position);
        notifyItemRemoved(position);
        notifyItemChanged(getItemCount() - 1);
    }

    Image getItem(final int position) {
        return mImageList.get(position);
    }

    int getLastPosition() {
        return getItemCount() - 1;
    }

    void setImages(List<Image> images) {
        mImageList.clear();
        mImageList.addAll(images);
        notifyDataSetChanged();
    }

    void setSelectedElement(final int position) {
        mSelectedElement = position;
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
        Drawable drawable;
        if (state == ImageState.SUCCESSFULLY_PROCESSED) {
            drawable = ContextCompat.getDrawable(mContext, R.drawable.ic_check);
        } else {
            drawable = ContextCompat.getDrawable(mContext, R.drawable.ic_cross);
        }
        final int processingColor = getProcessingStateColor(state);
        drawable.setAlpha(255);
        drawable.setColorFilter(new PorterDuffColorFilter(processingColor, PorterDuff.Mode.SRC_IN));
        return drawable;
    }

    private int getProcessingStateColor(final ImageState processingState) {
        return (processingState == ImageState.SUCCESSFULLY_PROCESSED)
                ? ContextCompat.getColor(mContext, mPositiveColor)
                : ContextCompat.getColor(mContext, mNegativeColor);
    }

    private boolean shouldShowPlusSymbol() {
        return mImageList.size() > 0 && mSelectedElement != getLastPosition();
    }

    private static class EmptyViewHolder extends RecyclerView.ViewHolder {

        View mSelector;
        TextView mText;

        EmptyViewHolder(final View itemView) {
            super(itemView);
            mSelector = itemView.findViewById(R.id.selector);
            mText = (TextView) itemView.findViewById(R.id.plus_text);
        }
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        AutoRotateImageView mImageView;
        TextView mItemLabel;
        ProgressBar mProgressBar;
        View mSelector;
        ImageView mStateImageView;
        View mStatusIndicator;

        ViewHolder(final View itemView) {
            super(itemView);
            mImageView = (AutoRotateImageView) itemView.findViewById(R.id.image_view);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar);
            mStatusIndicator = itemView.findViewById(R.id.status_indicator_view);
            mStateImageView = (ImageView) itemView.findViewById(R.id.image_state);
            mItemLabel = (TextView) itemView.findViewById(R.id.item_label);
            mSelector = itemView.findViewById(R.id.selector);
        }
    }

    interface Listener {
        void onCameraClicked();

        void onImageClicked(final Image image, final int position);
    }
}
