package com.konel.kryptapps.onboard.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.View;

import com.konel.kryptapps.onboard.R;

/**
 * Created by tushargupta on 21/06/17.
 */

public class ReadMoreTextView extends AppCompatTextView {

    private static final int DEFAULT_TRIM_LENGTH = 200;
    private static final String ELLIPSIS = " ... ";
    private static final String READ_MORE = "read more";

    private CharSequence originalText;
    private CharSequence trimmedText;
    private BufferType bufferType;
    private boolean trim = true;
    private int trimLength;
    private Context context;

    public ReadMoreTextView(Context context) {
        this(context, null);
    }

    public ReadMoreTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ReadMoreTextView);
        this.trimLength = typedArray.getInt(R.styleable.ReadMoreTextView_trimLength, DEFAULT_TRIM_LENGTH);
        typedArray.recycle();

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                trim = !trim;
                setText();
                requestFocusFromTouch();
            }
        });
    }

    private void setText() {
        super.setText(getDisplayableText(), bufferType);
    }

    private CharSequence getDisplayableText() {
        return trim ? trimmedText : originalText;
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        originalText = text;
        trimmedText = getTrimmedText(text);
        bufferType = type;
        setText();
    }

    private CharSequence getTrimmedText(CharSequence text) {
        if (originalText != null && originalText.length() > trimLength) {
            SpannableStringBuilder string = new SpannableStringBuilder(originalText, 0, trimLength + 1).append(ELLIPSIS).append(READ_MORE);
            final ForegroundColorSpan fcs = new ForegroundColorSpan(ContextCompat.getColor(context, R.color.colorAccent));
            // Set the text color for last 4 characters
            string.setSpan(fcs, string.length() - 9, string.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

            return string;
        } else {
            return originalText;
        }
    }

    public CharSequence getOriginalText() {
        return originalText;
    }

    public int getTrimLength() {
        return trimLength;
    }

    public void setTrimLength(int trimLength) {
        this.trimLength = trimLength;
        trimmedText = getTrimmedText(originalText);
        setText();
    }
}
