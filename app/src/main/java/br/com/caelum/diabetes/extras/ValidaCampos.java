package br.com.caelum.diabetes.extras;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import br.com.caelum.diabetes.util.ValidatorUtils;

/**
 * Created by Fernanda on 04/07/2015.
 */
public class ValidaCampos {
    public static void validateEditText(final EditText editText, final Button salvar) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                salvar.setEnabled(ValidatorUtils.checkEmptyEditText(editText));
                ValidatorUtils.checkIfOnError(editText);
            }
        });
    }
}
