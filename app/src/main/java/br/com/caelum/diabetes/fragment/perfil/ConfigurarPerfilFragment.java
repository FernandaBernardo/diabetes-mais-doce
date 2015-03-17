package br.com.caelum.diabetes.fragment.perfil;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import br.com.caelum.diabetes.R;

public class ConfigurarPerfilFragment extends Fragment{
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.configurar_perfil, null);
		
		Button botaoDados = (Button) view.findViewById(R.id.perfil_dados);
		Button botaoBasal = (Button) view.findViewById(R.id.perfil_basal);
		Button botaoBolus = (Button) view.findViewById(R.id.perfil_bolus);
		Button botaoGlicemiaAlvo = (Button) view.findViewById(R.id.perfil_glicemia_alvo);
		
		onClickBotao(botaoDados, new ConfigurarDadosPessoaisFragment());
		onClickBotao(botaoBasal, new ConfigurarInsulinaContinuaFragment());
		onClickBotao(botaoBolus, new ConfigurarInsulinaCorrecaoFragment());
		onClickBotao(botaoGlicemiaAlvo, new ConfigurarGlicemiaAlvoFragment());
		
		return view;
	}

	private void onClickBotao(Button botaoDados, final Fragment fragment) {
		botaoDados.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				FragmentTransaction transaction = getFragmentManager().beginTransaction();
				transaction.replace(R.id.main_view, fragment);
				transaction.addToBackStack(null);
				transaction.commit();
			}
		});
	}
}
