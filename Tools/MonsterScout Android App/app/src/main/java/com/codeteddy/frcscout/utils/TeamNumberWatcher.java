package com.codeteddy.frcscout.utils;

import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by Alex on 24.02.2017.
 */

public class TeamNumberWatcher implements TextWatcher {

    private TextInputLayout textInputLayout;
    private EditText editText;

    public TeamNumberWatcher(EditText editText, TextInputLayout textInputLayout){
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
        try {
            int a = Integer.valueOf(editText.getText().toString().trim());

            if((a > 0) && (a < 10000)){
                textInputLayout.setErrorEnabled(false);
            }
            else if(a < 10000){
                textInputLayout.setError("Your number is too small");
            }
            else {
                textInputLayout.setError("Your number is too big");
            }

        }catch (Exception ex){
            textInputLayout.setError("This is not a number");
        }

    }
}
