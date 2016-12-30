package br.com.caelum.diabetes.fragment.perfil;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import br.com.caelum.diabetes.R;
import br.com.caelum.diabetes.activity.MainActivity;
import br.com.caelum.diabetes.dao.DadosMedicosDao;
import br.com.caelum.diabetes.dao.DbHelper;
import br.com.caelum.diabetes.extras.Extras;
import br.com.caelum.diabetes.extras.TipoRefeicao;
import br.com.caelum.diabetes.extras.ValidaCampos;
import br.com.caelum.diabetes.model.DadosMedicos;
import br.com.caelum.diabetes.model.TipoDadoMedico;
import br.com.caelum.diabetes.util.ValidatorUtils;

public class ConfigurarInsulinaCorrecaoFragment extends Fragment {
	private EditText cafe;
	private EditText almoco;
	private EditText jantar;
	private EditText lancheManha;
	private EditText lancheTarde;
	private EditText ceia;
	private Button salvar;
	private View view;

	@Override
	public void onResume() {
		super.onResume();
        ((MainActivity) getActivity()).setTitleHeader("Insulina Correção");
        ((MainActivity) getActivity()).setBackArrowIcon();
        ((MainActivity) getActivity()).showInfo(getResources().getString(R.string.info_correcao), getResources().getString(R.string.diabetes_org));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.configurar_insulina_correcao, null);

		getValoresGlobais();
		settarTextos();
		ValidaCampos.validateEditText(cafe, salvar);
        ValidaCampos.validateEditText(almoco, salvar);
        ValidaCampos.validateEditText(jantar, salvar);
        ValidaCampos.validateEditText(lancheManha, salvar);
        ValidaCampos.validateEditText(lancheTarde, salvar);
        ValidaCampos.validateEditText(ceia, salvar);

		salvar.setEnabled(ValidatorUtils.checkIfIsValid(cafe, almoco, jantar, lancheManha, lancheTarde, ceia));
		salvar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DadosMedicos dadosMedicos = new DadosMedicos(TipoDadoMedico.CORRECAO);
				dadosMedicos.set(Double.parseDouble(cafe.getText().toString()), TipoRefeicao.CAFE_DA_MANHA);
				dadosMedicos.set(Double.parseDouble(lancheManha.getText().toString()), TipoRefeicao.LANCHE_DA_MANHA);
				dadosMedicos.set(Double.parseDouble(almoco.getText().toString()), TipoRefeicao.ALMOCO);
				dadosMedicos.set(Double.parseDouble(lancheTarde.getText().toString()), TipoRefeicao.LANCHE_DA_TARDE);
				dadosMedicos.set(Double.parseDouble(jantar.getText().toString()), TipoRefeicao.JANTAR);
				dadosMedicos.set(Double.parseDouble(ceia.getText().toString()), TipoRefeicao.CEIA);

				DbHelper helper = new DbHelper(getActivity());

				DadosMedicosDao dadosDao = new DadosMedicosDao(helper);
				dadosDao.salva(dadosMedicos);

				helper.close();

                SharedPreferences settings = getActivity().getSharedPreferences(Extras.PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean(Extras.PREFS_NAME_INSULINA_CORRECAO, true);
                editor.commit();

				getFragmentManager().popBackStack();
			}
		});

		return view;
	}

	private void settarTextos() {
		DbHelper helper = new DbHelper(getActivity());
		DadosMedicosDao dao = new DadosMedicosDao(helper);

		DadosMedicos dadosMedicosAntigo = dao.getDadosMedicosCom(TipoDadoMedico.CORRECAO);
		if (dadosMedicosAntigo == null) return;

		cafe.setText(String.valueOf(dadosMedicosAntigo.get(TipoRefeicao.CAFE_DA_MANHA)));
		lancheManha.setText(String.valueOf(dadosMedicosAntigo.get(TipoRefeicao.LANCHE_DA_MANHA)));
		almoco.setText(String.valueOf(dadosMedicosAntigo.get(TipoRefeicao.ALMOCO)));
		lancheTarde.setText(String.valueOf(dadosMedicosAntigo.get(TipoRefeicao.LANCHE_DA_TARDE)));
		jantar.setText(String.valueOf(dadosMedicosAntigo.get(TipoRefeicao.JANTAR)));
		ceia.setText(String.valueOf(dadosMedicosAntigo.get(TipoRefeicao.CEIA)));

		helper.close();
	}

	private void getValoresGlobais() {
		cafe = (EditText) view.findViewById(R.id.valor_cafe_correcao);
		lancheManha = (EditText) view.findViewById(R.id.valor_lanche_manha_correcao);
		almoco = (EditText) view.findViewById(R.id.valor_almoco_correcao);
		lancheTarde = (EditText) view.findViewById(R.id.valor_lanche_tarde_correcao);
		jantar = (EditText) view.findViewById(R.id.valor_jantar_correcao);
		ceia = (EditText) view.findViewById(R.id.valor_ceia_correcao);
		salvar = (Button) view.findViewById(R.id.salvar_insulina_correcao);
	}
}
