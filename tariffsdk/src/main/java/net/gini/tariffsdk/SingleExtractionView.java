package net.gini.tariffsdk;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;

@SuppressLint("ViewConstructor")
public class SingleExtractionView extends TextInputLayout {

    private final Extraction mExtraction;
    private final EditText mInput;

    public SingleExtractionView(final Context context, final Extraction extraction) {
        super(context);
        mExtraction = extraction;
        final View view = inflate(context, R.layout.view_extraction, this);
        mInput = (EditText) view.findViewById(R.id.text_input);
        mInput.setText(extraction.getValue());
        setHint(extraction.getName());
    }

    public Extraction getExtraction() {
        return new Extraction(mExtraction.getName(), mInput.getText().toString());
    }
}
