package br.com.caelum.diabetes.dialog;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import br.com.caelum.diabetes.R;
import br.com.caelum.diabetes.extras.Extras;
import br.com.caelum.diabetes.fragment.calculadora.DashboardCalculadoraFragment;
import br.com.caelum.diabetes.fragment.perfil.ConfigurarInsulinaCorrecaoFragment;
import br.com.caelum.diabetes.fragment.perfil.ConfigurarPerfilFragment;

public class PreencherDadosMedicosDialog extends DialogFragment {

	private Button confirmarButton;
	private Button cancelarButton;
		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		View view = inflater.inflate(R.layout.preencher_dados_medicos, container);
		getDialog().setTitle("ATENÇÃO");
		confirmarButton = (Button)view.findViewById(R.id.confirmar_button);
		cancelarButton = (Button)view.findViewById(R.id.cancelar_button);
		
		confirmarButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				setTransaction(R.id.main_view, new ConfigurarInsulinaCorrecaoFragment());
				getDialog().dismiss();
			}
		});
		
		cancelarButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
                SharedPreferences settings = getActivity().getSharedPreferences(Extras.PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean(Extras.PREFS_NAME_INSULINA_CORRECAO, false);
                editor.commit();

				setTransaction(R.id.main_view, new DashboardCalculadoraFragment());
				getDialog().dismiss();
			}
		});
		
		return view;
	}
	
	
	private void setTransaction(int view, Fragment f){
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(view, f);
		transaction.addToBackStack(null);
		transaction.commit();
	}
}
