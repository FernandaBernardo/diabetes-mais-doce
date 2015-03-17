package br.com.caelum.diabetes.util;

import android.widget.EditText;

public class ValidatorUtils {

	public static void checkIfOnError(final EditText editText) {
		if (editText.getText().toString().length() == 0) {
			editText.setError("Campo não preenchido!");
		}

		if (editText.getText().toString().equals("0.0")
				|| editText.getText().toString().equals("0")) {
			editText.setError("Valor Inválido");
		}
	}

	public static boolean checkEmptyEditText(EditText... editTexts) {
		boolean result = true;
		for (EditText editText : editTexts) {
			if (editText.getText().toString().equals("")
					|| editText.getText() == null) {
				result = false;
			}
		}

		return result;
	}

	public static boolean checkIfIsValidWithHint(EditText... editTexts) {
		for (EditText editText : editTexts) {
			if (checkHint(editText)) {
				if (checkValueAndLength(editText)) {
					return false;
				}
			} else {
				if (checkValue(editText)) {
					return false;
				}
			}
		}
		return true;
	}

	public static boolean checkIfIsValid(EditText... editTexts) {
		for (EditText editText : editTexts) {
			if (checkValueAndLength(editText)) {
				return false;
			}
		}
		return true;
	}

	private static boolean checkHint(EditText editText) {
		return (editText.getHint().toString().equals("0.0") || editText
				.getHint().equals("0"));
	}

	private static boolean checkValueAndLength(EditText editText) {
		return (editText.getText().length() == 0 || editText.getText() == null
				|| editText.getText().toString().equals("0.0") || editText
				.getText().toString().equals("0"));
	}

	private static boolean checkValue(EditText editText) {
		return (editText.getText().toString().equals("0.0") || editText
				.getText().toString().equals("0"));
	}
}
