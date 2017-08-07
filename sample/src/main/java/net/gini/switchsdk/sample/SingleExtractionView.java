package net.gini.switchsdk.sample;


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

    private final EditText mInput;

    public SingleExtractionView(final Context context, final String title, final String value) {
        super(context);
        final View view = inflate(context, R.layout.view_extraction, this);
        setOrientation(VERTICAL);

        mInput = (EditText) view.findViewById(R.id.text_input);
        mInput.setText(value);
        mInput.setHint(title);
        mInput.setHintTextColor(ContextCompat.getColor(context, R.color.secondaryText));
        mInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(final Editable s) {
                String inputString = mInput.getText().toString();
                setLineColor(context, inputString);
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

        setLineColor(context, value);

        final TextView titleTextView = (TextView) view.findViewById(R.id.text_title);
        titleTextView.setText(title);

    }

    public String getValue() {
        Editable text = mInput.getText();
        return text != null ? text.toString() : "";
    }

    private void paintLineColor(@ColorRes final int color) {
        Drawable wrappedDrawable = DrawableCompat.wrap(mInput.getBackground());
        DrawableCompat.setTint(wrappedDrawable.mutate(),
                ContextCompat.getColor(getContext(), color));
        mInput.setBackground(wrappedDrawable);
    }

    private void setLineColor(final Context context, final String value) {
        if (TextUtils.isEmpty(value)) {
            mInput.setError(context.getString(R.string.extractions_error_empty_input));
            paintLineColor(R.color.negativeColor);
        } else {
            paintLineColor(R.color.positiveColor);
        }
    }

}
