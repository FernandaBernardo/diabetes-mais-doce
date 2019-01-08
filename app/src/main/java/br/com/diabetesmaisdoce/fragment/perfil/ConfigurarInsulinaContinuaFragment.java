package br.com.diabetesmaisdoce.fragment.perfil;

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
import br.com.diabetesmaisdoce.extras.TipoRefeicao;
import br.com.diabetesmaisdoce.extras.ValidaCampos;
import br.com.diabetesmaisdoce.model.DadosMedicos;
import br.com.diabetesmaisdoce.model.TipoDadoMedico;
import br.com.diabetesmaisdoce.util.ValidatorUtils;

public class ConfigurarInsulinaContinuaFragment extends Fragment {
	private EditText cafe;
	private EditText almoco;
	private EditText jantar;
	private EditText lancheManha;
	private EditText lancheTarde;
	private EditText ceia;
	private EditText madrugada;
	private Button salvar;
	private View view;

	@Override
	public void onResume() {
		super.onResume();
        ((MainActivity) getActivity()).setTitleHeader("Insulina Basal (Fixa)");
        ((MainActivity) getActivity()).setBackArrowIcon();
		((MainActivity) getActivity()).showInfo(getResources().getString(R.string.info_continua), getResources().getString(R.string.diabetes_org));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.configurar_insulina_continua, null);

		getValoresGlobais();
		settarTextos();

		ValidaCampos.validateEditText(cafe, salvar);
        ValidaCampos.validateEditText(almoco, salvar);
        ValidaCampos.validateEditText(jantar, salvar);
        ValidaCampos.validateEditText(lancheManha, salvar);
        ValidaCampos.validateEditText(lancheTarde, salvar);
        ValidaCampos.validateEditText(ceia, salvar);
        ValidaCampos.validateEditText(madrugada, salvar);

		salvar.setEnabled(ValidatorUtils.checkIfIsValid(cafe, almoco, jantar, lancheManha, lancheTarde, ceia));
		salvar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DadosMedicos dadosMedicos = new DadosMedicos(TipoDadoMedico.CONTINUA);
				dadosMedicos.set(Double.parseDouble(verificaValor(cafe)), TipoRefeicao.CAFE_DA_MANHA);
				dadosMedicos.set(Double.parseDouble(verificaValor(lancheManha)), TipoRefeicao.LANCHE_DA_MANHA);
				dadosMedicos.set(Double.parseDouble(verificaValor(almoco)), TipoRefeicao.ALMOCO);
				dadosMedicos.set(Double.parseDouble(verificaValor(lancheTarde)), TipoRefeicao.LANCHE_DA_TARDE);
				dadosMedicos.set(Double.parseDouble(verificaValor(jantar)), TipoRefeicao.JANTAR);
				dadosMedicos.set(Double.parseDouble(verificaValor(ceia)), TipoRefeicao.CEIA);
				dadosMedicos.set(Double.parseDouble(verificaValor(madrugada)), TipoRefeicao.MADRUGADA);

				DbHelper helper = new DbHelper(getActivity());

				DadosMedicosDao dadosDao = new DadosMedicosDao(helper);
				dadosDao.salva(dadosMedicos);

				helper.close();

				getFragmentManager().popBackStack();
			}
		});

		return view;
	}

	private String verificaValor(EditText text) {
		String valor = text.getText().toString();
		if(valor.equals("")) {
			return "0";
		} else {
			return valor;
		}
	}

	private void settarTextos() {
		DbHelper helper = new DbHelper(getActivity());
		DadosMedicosDao dao = new DadosMedicosDao(helper);

		DadosMedicos dadosMedicosAntigo = dao.getDadosMedicosCom(TipoDadoMedico.CONTINUA);
		if (dadosMedicosAntigo == null) return;

		cafe.setText(getStringOf(dadosMedicosAntigo.get(TipoRefeicao.CAFE_DA_MANHA)));
		lancheManha.setText(getStringOf(dadosMedicosAntigo.get(TipoRefeicao.LANCHE_DA_MANHA)));
		almoco.setText(getStringOf(dadosMedicosAntigo.get(TipoRefeicao.ALMOCO)));
		lancheTarde.setText(getStringOf(dadosMedicosAntigo.get(TipoRefeicao.LANCHE_DA_TARDE)));
		jantar.setText(getStringOf(dadosMedicosAntigo.get(TipoRefeicao.JANTAR)));
		ceia.setText(getStringOf(dadosMedicosAntigo.get(TipoRefeicao.CEIA)));
		madrugada.setText(getStringOf(dadosMedicosAntigo.get(TipoRefeicao.MADRUGADA)));

		helper.close();
	}

	private String getStringOf(Double tipoRefeicao) {
		if(tipoRefeicao == null) {
			return "";
		} else {
			return String.valueOf(tipoRefeicao);
		}
	}

	private void getValoresGlobais() {
		cafe = (EditText) view.findViewById(R.id.valor_cafe_continua);
		lancheManha = (EditText) view.findViewById(R.id.valor_lanche_manha_continua);
		almoco = (EditText) view.findViewById(R.id.valor_almoco_continua);
		lancheTarde = (EditText) view.findViewById(R.id.valor_lanche_tarde_continua);
		jantar = (EditText) view.findViewById(R.id.valor_jantar_continua);
		ceia = (EditText) view.findViewById(R.id.valor_ceia_continua);
		madrugada = (EditText) view.findViewById(R.id.valor_madrugada_continua);
		salvar = (Button) view.findViewById(R.id.salvar_insulina_continua);
	}
}
