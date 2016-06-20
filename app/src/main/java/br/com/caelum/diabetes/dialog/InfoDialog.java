package br.com.caelum.diabetes.dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.caelum.diabetes.R;

public class InfoDialog extends DialogFragment {

    private String text;
    private String referencia;

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		View view = inflater.inflate(R.layout.info, container);
		getDialog().setTitle("AJUDA");

        TextView textoInfo = (TextView) view.findViewById(R.id.texto_info);
        textoInfo.setText(text);

        TextView referenciaInfo = (TextView) view.findViewById(R.id.referencia_info);
        referenciaInfo.setText(referencia);

        return view;
	}

    public void setText(String text) {
        this.text = text;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }
}
