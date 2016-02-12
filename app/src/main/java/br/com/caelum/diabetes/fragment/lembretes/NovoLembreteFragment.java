package br.com.caelum.diabetes.fragment.lembretes;

import org.joda.time.DateTime;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.TimePicker;
import br.com.caelum.diabetes.R;
import br.com.caelum.diabetes.dao.DbHelper;
import br.com.caelum.diabetes.dao.LembreteDao;
import br.com.caelum.diabetes.extras.PickerDialog;
import br.com.caelum.diabetes.extras.ValidaCampos;
import br.com.caelum.diabetes.model.Lembrete;
import br.com.caelum.diabetes.util.ValidatorUtils;

public class NovoLembreteFragment extends Fragment {
	private Lembrete lembrete;
	private EditText atividade;
	private EditText anotacoes;
	private Button salvar;
    private PickerDialog pickerDialog;

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.novo_lembrete, null);


		Toolbar header = (Toolbar) getActivity().findViewById(R.id.header);
		header.setTitle("Novo Lembrete");

		TextView horario = (TextView) view.findViewById(R.id.hora_lembrete);
		TextView data = (TextView) view.findViewById(R.id.data_lembrete);

        pickerDialog = new PickerDialog(getFragmentManager(), data, horario);
        pickerDialog.setListener();
        pickerDialog.setText();

		lembrete = new Lembrete();

		atividade = (EditText) view.findViewById(R.id.atividade_lembrete);
		anotacoes = (EditText) view.findViewById(R.id.anotacoes_lembrete);

        salvar = (Button) view.findViewById(R.id.salvar_lembrete);
        salvar.setEnabled(ValidatorUtils.checkEmptyEditText(atividade));

        ValidaCampos.validateEditText(atividade, salvar);

        salvar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				lembrete.setData(pickerDialog.getDataSelecionada());
				lembrete.setAtividade(atividade.getText().toString());
				lembrete.setAnotacoes(anotacoes.getText().toString());

				DbHelper helper = new DbHelper(getActivity());
				LembreteDao dao = new LembreteDao(helper);
				dao.salva(lembrete);
				helper.close();
				getFragmentManager().popBackStack();
			}
		});

		return view;
	}
}
