package com.codeteddy.frcscout.utils;

import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * @author Alex
 * @version 1.0.0
 * Created by Alex on 24.02.2017.
 */

public class LengthTextWatcher implements TextWatcher {

    private int maxLength;
    private EditText editText;
    private TextInputLayout textInputLayout;

    public LengthTextWatcher(int maxLength, EditText editText, TextInputLayout textInputLayout){
        this.maxLength = maxLength;
        this.textInputLayout = textInputLayout;
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if(editText.getText().toString().length() > maxLength){
            textInputLayout.setError("Text is too lomg!");
        }else {
            textInputLayout.setErrorEnabled(false);
        }

    }
}
