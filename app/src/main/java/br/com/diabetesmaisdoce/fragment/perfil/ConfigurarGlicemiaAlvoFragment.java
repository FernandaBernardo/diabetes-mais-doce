package br.com.diabetesmaisdoce.fragment.perfil;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import br.com.diabetesmaisdoce.R;
import br.com.diabetesmaisdoce.activity.MainActivity;
import br.com.diabetesmaisdoce.dao.DadosMedicosDao;
import br.com.diabetesmaisdoce.dao.DbHelper;
import br.com.diabetesmaisdoce.extras.Extras;
import br.com.diabetesmaisdoce.extras.TipoRefeicao;
import br.com.diabetesmaisdoce.extras.ValidaCampos;
import br.com.diabetesmaisdoce.model.DadosMedicos;
import br.com.diabetesmaisdoce.model.TipoDadoMedico;
import br.com.diabetesmaisdoce.util.ValidatorUtils;

public class ConfigurarGlicemiaAlvoFragment extends Fragment {
	private EditText cafe;
	private EditText almoco;
	private EditText jantar;
	private Button salvar;
	private View view;

	@Override
	public void onResume() {
		super.onResume();
        ((MainActivity) getActivity()).setTitleHeader("Glicemia Alvo");
        ((MainActivity) getActivity()).setBackArrowIcon();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.configurar_glicemia, null);

		getValoresGlobais();
		settarTextos();

		ValidaCampos.validateEditText(cafe, salvar);
        ValidaCampos.validateEditText(almoco, salvar);
        ValidaCampos.validateEditText(jantar, salvar);

		salvar.setEnabled(ValidatorUtils.checkIfIsValid(cafe, almoco, jantar));
		salvar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DadosMedicos dadosMedicos = new DadosMedicos(TipoDadoMedico.GLICEMIA_ALVO);
				dadosMedicos.set(Double.parseDouble(cafe.getText().toString()), TipoRefeicao.CAFE_DA_MANHA);
				dadosMedicos.set(Double.parseDouble(almoco.getText().toString()), TipoRefeicao.ALMOCO);
				dadosMedicos.set(Double.parseDouble(jantar.getText().toString()), TipoRefeicao.JANTAR);

				DbHelper helper = new DbHelper(getActivity());

				DadosMedicosDao dadosDao = new DadosMedicosDao(helper);
				dadosDao.salva(dadosMedicos);

				helper.close();

				SharedPreferences settings = getActivity().getSharedPreferences(Extras.PREFS_NAME, 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putBoolean(Extras.PREFS_NAME_GLICEMIA_ALVO, true);
				editor.commit();

				getFragmentManager().popBackStack();
			}
		});

		return view;
	}

	private void settarTextos() {
		DbHelper helper = new DbHelper(getActivity());
		DadosMedicosDao dao = new DadosMedicosDao(helper);

		DadosMedicos dadosMedicosAntigo = dao.getDadosMedicosCom(TipoDadoMedico.GLICEMIA_ALVO);
		if (dadosMedicosAntigo == null) return;

		cafe.setText(String.valueOf(dadosMedicosAntigo.get(TipoRefeicao.CAFE_DA_MANHA)));
		almoco.setText(String.valueOf(dadosMedicosAntigo.get(TipoRefeicao.ALMOCO)));
		jantar.setText(String.valueOf(dadosMedicosAntigo.get(TipoRefeicao.JANTAR)));

		helper.close();
	}

	private void getValoresGlobais() {
		cafe = (EditText) view.findViewById(R.id.valor_cafe_glicemia);
		almoco = (EditText) view.findViewById(R.id.valor_almoco_glicemia);
		jantar = (EditText) view.findViewById(R.id.valor_jantar_glicemia);
		salvar = (Button) view.findViewById(R.id.salvar_dados_perfil_glicemia);
	}
}
