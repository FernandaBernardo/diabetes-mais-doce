package br.com.diabetesmaisdoce.dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Layout;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import br.com.diabetesmaisdoce.R;
import br.com.diabetesmaisdoce.activity.BemVindoActivity;

public class TermsDialog extends DialogFragment {

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		String text = getResources().getString(R.string.terms);
		
		View view = inflater.inflate(R.layout.terms, container);

        TextView termsText = (TextView) view.findViewById(R.id.terms);
        termsText.setText(text);
		termsText.setMovementMethod(new ScrollingMovementMethod());

		final CheckBox termsCheckbox = (CheckBox) getActivity().findViewById(R.id.terms);

		Button acceptTerms = (Button) view.findViewById(R.id.accept_terms);
		acceptTerms.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				termsCheckbox.setChecked(true);
				getDialog().dismiss();
			}
		});

		Button declineTerms = (Button) view.findViewById(R.id.decline_terms);
		declineTerms.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				termsCheckbox.setChecked(false);
				getDialog().dismiss();
			}
		});

		return view;
	}

}
