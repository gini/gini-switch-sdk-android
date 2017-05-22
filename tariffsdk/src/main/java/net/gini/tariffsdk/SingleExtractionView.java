package net.gini.tariffsdk;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
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

    public SingleExtractionView(final Context context, final Extraction extraction) {
        super(context);
        mExtraction = extraction;
        final View view = inflate(context, R.layout.view_extraction, this);
        setOrientation(VERTICAL);

        mInput = (EditText) view.findViewById(R.id.text_input);
        mInput.setText(extraction.getValue());
        mInput.setHint(extraction.getName());
        mInput.setHintTextColor(ContextCompat.getColor(context, R.color.secondaryText));

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
        Drawable wrappedDrawable = DrawableCompat.wrap(mInput.getBackground());
        DrawableCompat.setTint(wrappedDrawable.mutate(),
                ContextCompat.getColor(getContext(), color));
        mInput.setBackground(wrappedDrawable);
    }

    public void setTextColor(@ColorRes final int color) {
        mInput.setTextColor(ContextCompat.getColor(getContext(), color));
    }
}
