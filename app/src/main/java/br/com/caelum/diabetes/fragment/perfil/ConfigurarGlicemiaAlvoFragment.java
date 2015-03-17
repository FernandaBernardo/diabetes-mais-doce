package br.com.caelum.diabetes.fragment.perfil;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import br.com.caelum.diabetes.R;
import br.com.caelum.diabetes.dao.DadosMedicosDao;
import br.com.caelum.diabetes.dao.DbHelper;
import br.com.caelum.diabetes.model.DadosMedicos;
import br.com.caelum.diabetes.model.TipoDadoMedico;
import br.com.caelum.diabetes.util.ValidatorUtils;

public class ConfigurarGlicemiaAlvoFragment extends Fragment {
	private EditText cafe;
	private EditText almoco;
	private EditText jantar;
	private Button salvar;
	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.configurar_glicemia, null);

		getValoresGlobais();
		settarTextos();
		validateEditText(cafe);
		validateEditText(almoco);
		validateEditText(jantar);

		salvar.setEnabled(ValidatorUtils.checkIfIsValid(cafe, almoco, jantar));
		salvar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DadosMedicos dadosMedicos = new DadosMedicos(TipoDadoMedico.GLICEMIA_ALVO);
				dadosMedicos.setCafeManha(Double.parseDouble(cafe.getText().toString()));
				dadosMedicos.setAlmoco(Double.parseDouble(almoco.getText().toString()));
				dadosMedicos.setJantar(Double.parseDouble(jantar.getText().toString()));

				DbHelper helper = new DbHelper(getActivity());

				DadosMedicosDao dadosDao = new DadosMedicosDao(helper);
				dadosDao.salva(dadosMedicos);

				helper.close();

				getFragmentManager().popBackStack();
			}
		});

		return view;
	}

	private void validateEditText(final EditText editText) {
		editText.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable arg0) {
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				salvar.setEnabled(ValidatorUtils.checkIfIsValid(cafe, almoco, jantar));
				ValidatorUtils.checkIfOnError(editText);
			}
		});
	}

	private void settarTextos() {
		DbHelper helper = new DbHelper(getActivity());
		DadosMedicosDao dao = new DadosMedicosDao(helper);

		DadosMedicos dadosMedicosAntigo = dao.getDadosMedicosCom(TipoDadoMedico.GLICEMIA_ALVO);
		if (dadosMedicosAntigo == null) return;

		cafe.setText(String.valueOf(dadosMedicosAntigo.getCafeManha()));
		almoco.setText(String.valueOf(dadosMedicosAntigo.getAlmoco()));
		jantar.setText(String.valueOf(dadosMedicosAntigo.getJantar()));

		helper.close();
	}

	private void getValoresGlobais() {
		cafe = (EditText) view.findViewById(R.id.valor_cafe_glicemia);
		almoco = (EditText) view.findViewById(R.id.valor_almoco_glicemia);
		jantar = (EditText) view.findViewById(R.id.valor_jantar_glicemia);
		salvar = (Button) view.findViewById(R.id.salvar_dados_perfil_glicemia);
	}
}
