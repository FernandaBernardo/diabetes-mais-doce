package br.com.caelum.diabetes.fragment.glicemia;

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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

import br.com.caelum.diabetes.R;
import br.com.caelum.diabetes.activity.MainActivity;
import br.com.caelum.diabetes.calculos.CalculaInsulina;
import br.com.caelum.diabetes.calculos.DescobreTipoRefeicao;
import br.com.caelum.diabetes.dao.DbHelper;
import br.com.caelum.diabetes.dao.GlicemiaDao;
import br.com.caelum.diabetes.dao.PacienteDao;
import br.com.caelum.diabetes.extras.Extras;
import br.com.caelum.diabetes.extras.PickerDialog;
import br.com.caelum.diabetes.extras.TipoRefeicao;
import br.com.caelum.diabetes.extras.ValidaCampos;
import br.com.caelum.diabetes.model.Glicemia;
import br.com.caelum.diabetes.model.Paciente;
import br.com.caelum.diabetes.util.ValidatorUtils;

public class NovaGlicemiaFragment extends Fragment {
	private Glicemia glicemia;
	private EditText valorGlicemiaCampo;
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

				atualizaGlicemia(valorGlicemia, tipoRefeicao, dataSelecionada);

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

				atualizaGlicemia(valorGlicemia, tipoRefeicao, dataSelecionada);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
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

				atualizaGlicemia(valorGlicemia, tipoRefeicao, dataSelecionada);

				DbHelper helper = new DbHelper(getActivity());
				GlicemiaDao dao = new GlicemiaDao(helper);
				dao.salva(glicemia);
				helper.close();

				getFragmentManager().popBackStack();
			}
		});
		return view;
	}

	private void atualizaGlicemia(int valorGlicemia, TipoRefeicao tipoRefeicao, Calendar data) {
		glicemia.setValorGlicemia(valorGlicemia);
		glicemia.setTipoRefeicao(tipoRefeicao);
		glicemia.setData(data);

		SharedPreferences settings = getActivity().getSharedPreferences(Extras.PREFS_NAME, 0);
		boolean configFatorCorrecao = settings.getBoolean(Extras.PREFS_NAME_FATOR_CORRECAO, false);
		boolean configGlicemiaAlvo = settings.getBoolean(Extras.PREFS_NAME_GLICEMIA_ALVO, false);

		if (configFatorCorrecao && configGlicemiaAlvo && !tipoRefeicao.equals(TipoRefeicao.LANCHE_DA_MANHA) &&
				!tipoRefeicao.equals(TipoRefeicao.LANCHE_DA_TARDE) && !tipoRefeicao.equals(TipoRefeicao.CEIA)) {
			totalInsulinaText.setVisibility(View.VISIBLE);
			totalInsulina.setVisibility(View.VISIBLE);

			double valorInsulina = CalculaInsulina.getTotalInsulinaFatorCorrecao(paciente, glicemia);
			totalInsulina.setText(String.valueOf(valorInsulina) + " U");
		} else {
			totalInsulinaText.setVisibility(View.INVISIBLE);
			totalInsulina.setVisibility(View.INVISIBLE);
		}
	}
}
