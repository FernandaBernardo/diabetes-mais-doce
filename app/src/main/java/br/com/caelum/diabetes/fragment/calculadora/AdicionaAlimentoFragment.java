package br.com.caelum.diabetes.fragment.calculadora;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import br.com.caelum.diabetes.R;
import br.com.caelum.diabetes.dao.AlimentoFisicoDao;
import br.com.caelum.diabetes.dao.DbHelper;
import br.com.caelum.diabetes.model.AlimentoFisico;
import br.com.caelum.diabetes.model.AlimentoVirtual;
import br.com.caelum.diabetes.model.Refeicao;
import br.com.caelum.diabetes.util.ValidatorUtils;

public class AdicionaAlimentoFragment extends Fragment {

	private EditText carboidrato;
	private AlimentoFisico alimentoAtual;
	private EditText valor;
	private EditText unidade;
	private Button adicionarAlimento;
	private AlimentoFisicoDao alimentoDao;
	private Refeicao refeicao;
	private DbHelper helper;
	private AutoCompleteTextView buscaAlimento;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.adiciona_alimento, null);

		Bundle bundle = this.getArguments();

		refeicao = (Refeicao) bundle.getSerializable("refeicao");
		
		helper = new DbHelper(getActivity());
		alimentoDao = new AlimentoFisicoDao(helper);

		final List<AlimentoFisico> alimentos = alimentoDao.getAlimentos();
		
		helper.close();
		
		carboidrato = (EditText) view.findViewById(R.id.carboidrato_alimento);
		valor = (EditText) view.findViewById(R.id.valor);
		unidade = (EditText) view.findViewById(R.id.unidade);
		
		adicionarAlimento = (Button) view.findViewById(R.id.adicionar_alimento);

		ArrayAdapter<AlimentoFisico> adapter = new ArrayAdapter<AlimentoFisico>(
				getActivity(), android.R.layout.simple_dropdown_item_1line,
				alimentos);
		buscaAlimento = (AutoCompleteTextView) view.findViewById(R.id.busca);
		buscaAlimento.setAdapter(adapter);
		
		buscaAlimento.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int pos,
					long arg3) {
				alimentoAtual = (AlimentoFisico) adapter.getAdapter().getItem(
						pos);

				carboidrato.setText(String.valueOf(alimentoAtual
						.getCarboidrato()));
				valor.setText("1");
				unidade.setText(alimentoAtual.getUnidadeDeMedida());
			}
		});

		validateEditText(buscaAlimento);
		adicionarAlimento.setEnabled(ValidatorUtils
				.checkIfIsValid(buscaAlimento));
		valor.setEnabled(ValidatorUtils.checkIfIsValid(buscaAlimento));
		valor.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s != null && s.length() > 0) {
					double carboidratoPorValor = alimentoAtual
							.getCarboidratoPorValor(Double.parseDouble(s
									.toString()));
					carboidrato.setText(String.valueOf(carboidratoPorValor));
				} else {
					carboidrato.setText("0.0");
				}
			}

			@Override
			public void afterTextChanged(Editable arg0) {
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}
		});

		adicionarAlimento.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlimentoVirtual alimentoVirtual = new AlimentoVirtual(
						alimentoAtual, Double.parseDouble(valor.getText()
								.toString()), refeicao);
				refeicao.adicionaAlimento(alimentoVirtual);

				Bundle args = new Bundle();
				args.putSerializable("refeicao", refeicao);

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
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				valor.setEnabled(ValidatorUtils.checkIfIsValid(buscaAlimento));
				adicionarAlimento.setEnabled(ValidatorUtils
						.checkIfIsValid(buscaAlimento));
				ValidatorUtils.checkIfOnError(editText);
			}

		});

	}
}
