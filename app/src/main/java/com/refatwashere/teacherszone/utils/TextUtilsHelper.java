package com.refatwashere.teacherszone.utils;

import android.widget.EditText;

public class TextUtilsHelper {

    public static String getSafeText(EditText editText) {
        return editText != null && editText.getText() != null
                ? editText.getText().toString().trim()
                : "";
    }
}
