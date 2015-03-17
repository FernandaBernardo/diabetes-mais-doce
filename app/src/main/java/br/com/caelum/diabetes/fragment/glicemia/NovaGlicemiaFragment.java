package br.com.caelum.diabetes.fragment.glicemia;

import org.joda.time.DateTime;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextClock;
import android.widget.TimePicker;
import br.com.caelum.diabetes.R;
import br.com.caelum.diabetes.calculos.DescobreTipoRefeicao;
import br.com.caelum.diabetes.dao.DbHelper;
import br.com.caelum.diabetes.dao.GlicemiaDao;
import br.com.caelum.diabetes.extras.TipoRefeicao;
import br.com.caelum.diabetes.model.Glicemia;
import br.com.caelum.diabetes.util.ValidatorUtils;

@SuppressLint("NewApi")
public class NovaGlicemiaFragment extends Fragment {
	Glicemia glicemia;
	private int dia;
	private int mes;
	private int ano;
	private int hora;
	private int minuto;
	private EditText valorGlicemia;
	private Button salvarGlicemia;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.nova_glicemia, null);

		DateTime dataAgora = new DateTime();

		final TextClock horario = (TextClock) view.findViewById(R.id.hora_glicemia);
		horario.setText(dataAgora.getHourOfDay() + ":" + dataAgora.getMinuteOfHour());
		final TextClock data = (TextClock) view.findViewById(R.id.data_glicemia);
		data.setText(dataAgora.getDayOfMonth() + "/" + dataAgora.getMonthOfYear() + "/" + dataAgora.getYear());

		String dataAtual = (String) data.getText();
		String[] numerosData = dataAtual.split("/");

		String horarioAtual = (String) horario.getText();
		String[] numerosHorario = horarioAtual.split(":");

		dia = Integer.parseInt(numerosData[0]);
		mes = Integer.parseInt(numerosData[1]) - 1;
		ano = Integer.parseInt(numerosData[2]);
		hora = Integer.parseInt(numerosHorario[0]);
		minuto = Integer.parseInt(numerosHorario[1]);

		horario.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				TimePickerDialog timePicker = new TimePickerDialog(getActivity(),
						new TimePickerDialog.OnTimeSetListener() {
							@Override
							public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
								hora = selectedHour;
								minuto = selectedMinute;
								horario.setText(hora + ":" + minuto);
							}
						}, hora, minuto, true);
				timePicker.show();
			}
		});

		data.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				DatePickerDialog datePicker = new DatePickerDialog(getActivity(),
						new DatePickerDialog.OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker arg0, int year, int month, int day) {
								dia = day;
								mes = month;
								ano = year;
								data.setText(dia + "/" + (mes + 1) + "/" + ano);
							}
						}, ano, mes, dia);
				datePicker.show();
			}
		});

		final Spinner tipoRefeicao = (Spinner) view.findViewById(R.id.tipo_refeicao);
		final ArrayAdapter<String> spinnerAdapter = (ArrayAdapter<String>) tipoRefeicao.getAdapter();

		glicemia = new Glicemia();
		TipoRefeicao tipo = new DescobreTipoRefeicao().getTipoRefeicao();

		int position = spinnerAdapter.getPosition(tipo.getText());
		if (position == -1) position = 0;
		tipoRefeicao.setSelection(position);

		valorGlicemia = (EditText) view.findViewById(R.id.valor_glicemia);
		validateEditText(valorGlicemia);
		salvarGlicemia = (Button) view.findViewById(R.id.salvar_glicemia);
		salvarGlicemia.setEnabled(ValidatorUtils.checkEmptyEditText(valorGlicemia));

		salvarGlicemia.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				glicemia.setValorGlicemia(Integer.parseInt(valorGlicemia.getText().toString()));
				int pos = tipoRefeicao.getSelectedItemPosition();
				glicemia.setTipoRefeicao(TipoRefeicao.fromString(spinnerAdapter.getItem(pos)));

				DateTime dateTime = new DateTime(ano, mes + 1, dia, hora, minuto);
				glicemia.setData(dateTime);

				DbHelper helper = new DbHelper(getActivity());
				GlicemiaDao dao = new GlicemiaDao(helper);
				dao.salva(glicemia);
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
				salvarGlicemia.setEnabled(ValidatorUtils.checkEmptyEditText(valorGlicemia));
				ValidatorUtils.checkIfOnError(editText);
			}
		});
	}
}
