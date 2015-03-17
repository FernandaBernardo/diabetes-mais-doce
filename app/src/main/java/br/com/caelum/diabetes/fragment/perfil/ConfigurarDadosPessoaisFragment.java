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
import android.widget.RadioButton;
import br.com.caelum.diabetes.R;
import br.com.caelum.diabetes.dao.DbHelper;
import br.com.caelum.diabetes.dao.PacienteDao;
import br.com.caelum.diabetes.model.Paciente;
import br.com.caelum.diabetes.util.ValidatorUtils;

public class ConfigurarDadosPessoaisFragment extends Fragment {
	private Paciente paciente;
	private EditText idade;
	private EditText peso;
	private EditText altura;
	private RadioButton feminino;
	private RadioButton masculino;
	private RadioButton tipo1;
	private RadioButton tipo2;
	private PacienteDao dao;
	private Button salvar;
	private View view;

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.configurar_dados, null);
		initializeComponents(view);
		DbHelper helper = new DbHelper(getActivity());
		dao = new PacienteDao(helper);
		paciente = dao.getPaciente();

		validateEditText(idade);
		validateEditText(peso);
		validateEditText(altura);

		setValues();

		salvar = (Button) view.findViewById(R.id.salvar_dados);
		salvar.setEnabled(ValidatorUtils.checkIfIsValidWithHint(idade, peso, altura));

		salvar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (idade.getText().length() == 0) {
					paciente.setIdade(Integer.parseInt(idade.getHint().toString()));
				} else {
					paciente.setIdade(Integer.parseInt(idade.getText().toString()));
				}

				if (peso.getText().length() == 0) {
					paciente.setPeso(Double.parseDouble(peso.getHint().toString()));
				} else {
					paciente.setPeso(Double.parseDouble(peso.getText().toString()));
				}

				if (altura.getText().length() == 0) {
					paciente.setAltura(Double.parseDouble(altura.getHint().toString()));
				} else {
					paciente.setAltura(Double.parseDouble(altura.getText().toString()));
				}

				if (masculino.isChecked()) {
					paciente.setSexo(masculino.getText().toString());
				} else {
					paciente.setSexo(feminino.getText().toString());
				}

				if (tipo2.isChecked()) {
					paciente.setTipoDiabetes(tipo2.getText().toString());
				} else {
					paciente.setTipoDiabetes(tipo1.getText().toString());
				}

				dao.atualiza(paciente);

				getFragmentManager().popBackStack();
			}
		});

		return view;
	}

	private void validateEditText(final EditText editText) {
		editText.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				salvar.setEnabled(ValidatorUtils.checkIfIsValidWithHint(idade, peso, altura));
				ValidatorUtils.checkIfOnError(editText);
			}
		});
	}

	private void initializeComponents(View view) {
		idade = (EditText) view.findViewById(R.id.idade);
		peso = (EditText) view.findViewById(R.id.peso);
		altura = (EditText) view.findViewById(R.id.altura);
		feminino = (RadioButton) view.findViewById(R.id.feminino);
		masculino = (RadioButton) view.findViewById(R.id.masculino);
		tipo1 = (RadioButton) view.findViewById(R.id.diabetes1);
		tipo2 = (RadioButton) view.findViewById(R.id.diabetes2);
	}

	private void setValues() {
		idade.setHint(String.valueOf(paciente.getIdade()));
		peso.setHint(String.valueOf(paciente.getPeso()));
		altura.setHint(String.valueOf(paciente.getAltura()));
		if (masculino.getText().toString().equals(paciente.getSexo())) {
			masculino.setChecked(true);
		} else {
			feminino.setChecked(true);
		}
		if (tipo2.getText().toString().equals(paciente.getTipoDiabetes())) {
			tipo2.setChecked(true);
		} else {
			tipo1.setChecked(true);
		}
	}
}
