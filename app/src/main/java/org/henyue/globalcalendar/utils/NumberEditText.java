package org.henyue.globalcalendar.utils;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Kong on 2014/8/21.
 */
public class NumberEditText extends EditText {
    private int minNumber = 0;
    private int maxNumber = 99;

    public NumberEditText(Context context) {
        super(context);
        this.init(null);
    }

    public NumberEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(attrs);
    }

    public NumberEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.init(attrs);
    }

    private void init(AttributeSet attrs) {
        this.setInputType(InputType.TYPE_CLASS_NUMBER);
        this.setRawInputType(InputType.TYPE_CLASS_NUMBER);
        if (attrs != null) {
            for (int i = 0; i < attrs.getAttributeCount(); i++) {
                String attrName = attrs.getAttributeName(i);
                String attrValue = attrs.getAttributeValue(i);
                if (attrName.equals("maxNumber")) {
                    this.maxNumber = Integer.parseInt(attrValue);
                } else if (attrName.equals("minNumber")) {
                    this.minNumber = Integer.parseInt(attrValue);
                }
            }
        }
        this.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    int inputNum = getNumber();
                    if (inputNum > maxNumber) {
                        setText(String.valueOf(maxNumber));
                    } else if (inputNum < minNumber) {
                        setText(String.valueOf(minNumber));
                    }
                }
            }
        });
    }

    public int getNumber() {
        String editValue = this.getText().toString();
        Log.d("NumberEditText", "The editValue is " + editValue);
        if (editValue == null || editValue.equals("")) {
            return 0;
        }
        return Integer.parseInt(editValue);
    }

    public void setMaxNumber(int maxNumber) {
        this.maxNumber = maxNumber;
    }

    public void setMinNumber(int minNumber) {
        this.minNumber = minNumber;
    }

}
