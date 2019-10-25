package br.com.diabetesmaisdoce.fragment.glicemia;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

import br.com.diabetesmaisdoce.R;
import br.com.diabetesmaisdoce.activity.MainActivity;
import br.com.diabetesmaisdoce.calculos.CalculaInsulina;
import br.com.diabetesmaisdoce.calculos.DescobreTipoRefeicao;
import br.com.diabetesmaisdoce.dao.DbHelper;
import br.com.diabetesmaisdoce.dao.GlicemiaDao;
import br.com.diabetesmaisdoce.dao.PacienteDao;
import br.com.diabetesmaisdoce.extras.Extras;
import br.com.diabetesmaisdoce.extras.ParserTools;
import br.com.diabetesmaisdoce.extras.PickerDialog;
import br.com.diabetesmaisdoce.extras.TipoRefeicao;
import br.com.diabetesmaisdoce.extras.ValidaCampos;
import br.com.diabetesmaisdoce.model.Glicemia;
import br.com.diabetesmaisdoce.model.Paciente;
import br.com.diabetesmaisdoce.util.ValidatorUtils;

public class NovaGlicemiaFragment extends Fragment {
	private Glicemia glicemia;
	private EditText valorGlicemiaCampo;
	private EditText observacaoText;
	private Button salvarGlicemia;
	private PickerDialog pickerDialog;
	private EditText totalInsulina;
	private Paciente paciente;
	private TextView totalInsulinaText;

	@Override
	public void onResume() {
		super.onResume();
		((MainActivity) getActivity()).setTitleHeader("Nova Glicemia");
		((MainActivity) getActivity()).setBackArrowIcon();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.nova_glicemia, null);

		DbHelper helper = new DbHelper(getActivity());
		final PacienteDao pacienteDao = new PacienteDao(helper);
		this.paciente = pacienteDao.getPaciente();

		final TextView horario = (TextView) view.findViewById(R.id.hora_glicemia);
		final TextView data = (TextView) view.findViewById(R.id.data_glicemia);

		pickerDialog = new PickerDialog(getFragmentManager(), data, horario);

		final Spinner tipoRefeicao = (Spinner) view.findViewById(R.id.tipo_refeicao);
		final ArrayAdapter<String> spinnerAdapter = (ArrayAdapter<String>) tipoRefeicao.getAdapter();

		glicemia = new Glicemia();
		TipoRefeicao tipo = new DescobreTipoRefeicao().getTipoRefeicao();

		int position = spinnerAdapter.getPosition(tipo.getText());
		if (position == -1) position = 0;
		tipoRefeicao.setSelection(position);

		valorGlicemiaCampo = (EditText) view.findViewById(R.id.valor_glicemia);
		totalInsulina = (EditText) view.findViewById(R.id.glicemia_total_insulina);
		totalInsulinaText = (TextView) view.findViewById(R.id.glicemia_total_insulina_text);

		valorGlicemiaCampo.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View view, int i, KeyEvent keyEvent) {
				int pos = tipoRefeicao.getSelectedItemPosition();
				String item = spinnerAdapter.getItem(pos);
				TipoRefeicao tipoRefeicao = TipoRefeicao.fromString(item);
				String text = valorGlicemiaCampo.getText().toString();
				int valorGlicemia = Integer.parseInt(text.equals("") ? "0" : text);
				Calendar dataSelecionada = pickerDialog.getDataSelecionada();
				String observacao = observacaoText.getText().toString();

				atualizaGlicemia(valorGlicemia, tipoRefeicao, dataSelecionada, observacao);

				return false;
			}
		});

		tipoRefeicao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View view, int pos, long id) {
				String item = spinnerAdapter.getItem(pos);
				TipoRefeicao tipoRefeicao = TipoRefeicao.fromString(item);
				String text = valorGlicemiaCampo.getText().toString();
				int valorGlicemia = Integer.parseInt(text.equals("") ? "0" : text);
				Calendar dataSelecionada = pickerDialog.getDataSelecionada();
				String observacao = observacaoText.getText().toString();

				atualizaGlicemia(valorGlicemia, tipoRefeicao, dataSelecionada, observacao);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		CheckBox observacaoCheck = (CheckBox) view.findViewById(R.id.observacao_check);
		observacaoText = (EditText) view.findViewById(R.id.observacao_text);

		observacaoCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
				if (isChecked) {
					observacaoText.setVisibility(View.VISIBLE);
				} else {
					observacaoText.setVisibility(View.GONE);
				}
			}
		});

		salvarGlicemia = (Button) view.findViewById(R.id.salvar_glicemia);
		salvarGlicemia.setEnabled(ValidatorUtils.checkEmptyEditText(valorGlicemiaCampo));

		ValidaCampos.validateEditText(valorGlicemiaCampo, salvarGlicemia);

		salvarGlicemia.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				int pos = tipoRefeicao.getSelectedItemPosition();
				int valorGlicemia = Integer.parseInt(valorGlicemiaCampo.getText().toString());
				TipoRefeicao tipoRefeicao = TipoRefeicao.fromString(spinnerAdapter.getItem(pos));
				Calendar dataSelecionada = pickerDialog.getDataSelecionada();
				String observacao = observacaoText.getText().toString();

				atualizaGlicemia(valorGlicemia, tipoRefeicao, dataSelecionada, observacao);

				DbHelper helper = new DbHelper(getActivity());
				GlicemiaDao dao = new GlicemiaDao(helper);
				dao.salva(glicemia);
				helper.close();

				getFragmentManager().popBackStack();
			}
		});
		return view;
	}

	private void atualizaGlicemia(int valorGlicemia, TipoRefeicao tipoRefeicao, Calendar data, String observacao) {
		glicemia.setValorGlicemia(valorGlicemia);
		glicemia.setTipoRefeicao(tipoRefeicao);
		glicemia.setData(data);
		glicemia.setObservacao(observacao);

		SharedPreferences settings = getActivity().getSharedPreferences(Extras.PREFS_NAME, 0);
		boolean configFatorCorrecao = settings.getBoolean(Extras.PREFS_NAME_FATOR_CORRECAO, false);
		boolean configGlicemiaAlvo = settings.getBoolean(Extras.PREFS_NAME_GLICEMIA_ALVO, false);

		if (configFatorCorrecao && configGlicemiaAlvo && !tipoRefeicao.equals(TipoRefeicao.LANCHE_DA_MANHA) &&
				!tipoRefeicao.equals(TipoRefeicao.LANCHE_DA_TARDE) && !tipoRefeicao.equals(TipoRefeicao.CEIA) &&
				!tipoRefeicao.equals(TipoRefeicao.MADRUGADA)) {
			totalInsulinaText.setVisibility(View.VISIBLE);
			totalInsulina.setVisibility(View.VISIBLE);

			double valorInsulina = CalculaInsulina.getTotalInsulinaFatorCorrecao(paciente, glicemia);
			totalInsulina.setText(ParserTools.getParseDouble(valorInsulina) + " U");
		} else {
			totalInsulinaText.setVisibility(View.GONE);
			totalInsulina.setVisibility(View.GONE);
		}
	}
}
