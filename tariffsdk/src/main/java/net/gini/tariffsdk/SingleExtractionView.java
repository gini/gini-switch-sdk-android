package net.gini.tariffsdk;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("ViewConstructor")
public class SingleExtractionView extends LinearLayout {

    private final View mBackground;
    private final Extraction mExtraction;
    private final EditText mInput;
    private final TextView mTitle;
    private int mLineColor;
    private int mNegativeColor;

    public SingleExtractionView(final Context context, final Extraction extraction) {
        super(context);
        mExtraction = extraction;
        final View view = inflate(context, R.layout.view_extraction, this);
        setOrientation(VERTICAL);

        mInput = (EditText) view.findViewById(R.id.text_input);
        mInput.setText(extraction.getValue());
        mInput.setHint(extraction.getName());
        mInput.setHintTextColor(ContextCompat.getColor(context, R.color.secondaryText));
        mInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(final Editable s) {
                if (TextUtils.isEmpty(mInput.getText().toString())) {
                    mInput.setError(context.getString(R.string.extractions_error_empty_input));
                    paintLineColor(mNegativeColor);

                } else {
                    paintLineColor(mLineColor);
                }
            }

            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count,
                    final int after) {
            }

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before,
                    final int count) {
            }
        });

        mTitle = (TextView) view.findViewById(R.id.text_title);
        mTitle.setText(extraction.getName());

        mBackground = view.findViewById(R.id.background);

    }

    public void setBackgroundColor(@ColorRes final int color) {
        mBackground.setBackgroundColor(ContextCompat.getColor(getContext(), color));
    }

    public Extraction getExtraction() {
        return new Extraction(mExtraction.getName(), mInput.getText().toString());
    }

    public void setHintColor(@ColorRes final int color) {
        int colorHint = ContextCompat.getColor(getContext(), color);
        mTitle.setTextColor(colorHint);
        mInput.setHintTextColor(colorHint);
    }

    public void setLineColor(@ColorRes final int color) {
        mLineColor = color;
        paintLineColor(mLineColor);
    }

    public void setNegativeColor(@ColorRes final int negativeColor) {
        mNegativeColor = negativeColor;
    }

    public void setTextColor(@ColorRes final int color) {
        mInput.setTextColor(ContextCompat.getColor(getContext(), color));
    }

    private void paintLineColor(@ColorRes final int color) {
        Drawable wrappedDrawable = DrawableCompat.wrap(mInput.getBackground());
        DrawableCompat.setTint(wrappedDrawable.mutate(),
                ContextCompat.getColor(getContext(), color));
        mInput.setBackground(wrappedDrawable);
    }

}
