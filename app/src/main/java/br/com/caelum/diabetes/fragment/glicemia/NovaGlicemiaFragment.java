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
import android.widget.TextView;
import android.widget.TimePicker;
import br.com.caelum.diabetes.R;
import br.com.caelum.diabetes.calculos.DescobreTipoRefeicao;
import br.com.caelum.diabetes.dao.DbHelper;
import br.com.caelum.diabetes.dao.GlicemiaDao;
import br.com.caelum.diabetes.extras.PickerDialog;
import br.com.caelum.diabetes.extras.TipoRefeicao;
import br.com.caelum.diabetes.extras.ValidaCampos;
import br.com.caelum.diabetes.model.Glicemia;
import br.com.caelum.diabetes.util.ValidatorUtils;

public class NovaGlicemiaFragment extends Fragment {
	private Glicemia glicemia;
	private EditText valorGlicemia;
	private Button salvarGlicemia;
    private PickerDialog pickerDialog;

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.nova_glicemia, null);

        TextView titulo = (TextView) getActivity().findViewById(R.id.titulo);
        titulo.setText("Nova Glicemia");

		final TextView horario = (TextView) view.findViewById(R.id.hora_glicemia);
		final TextView data = (TextView) view.findViewById(R.id.data_glicemia);

        pickerDialog = new PickerDialog(getFragmentManager(), data, horario);
        pickerDialog.setListener();
        pickerDialog.setText();

		final Spinner tipoRefeicao = (Spinner) view.findViewById(R.id.tipo_refeicao);
		final ArrayAdapter<String> spinnerAdapter = (ArrayAdapter<String>) tipoRefeicao.getAdapter();

		glicemia = new Glicemia();
		TipoRefeicao tipo = new DescobreTipoRefeicao().getTipoRefeicao();

		int position = spinnerAdapter.getPosition(tipo.getText());
		if (position == -1) position = 0;
		tipoRefeicao.setSelection(position);

		valorGlicemia = (EditText) view.findViewById(R.id.valor_glicemia);
		salvarGlicemia = (Button) view.findViewById(R.id.salvar_glicemia);
        salvarGlicemia.setEnabled(ValidatorUtils.checkEmptyEditText(valorGlicemia));

        ValidaCampos.validateEditText(valorGlicemia, salvarGlicemia);

		salvarGlicemia.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				glicemia.setValorGlicemia(Integer.parseInt(valorGlicemia.getText().toString()));
				int pos = tipoRefeicao.getSelectedItemPosition();
				glicemia.setTipoRefeicao(TipoRefeicao.fromString(spinnerAdapter.getItem(pos)));

				glicemia.setData(pickerDialog.getDataSelecionada());

				DbHelper helper = new DbHelper(getActivity());
				GlicemiaDao dao = new GlicemiaDao(helper);
				dao.salva(glicemia);
				helper.close();

				getFragmentManager().popBackStack();
			}
		});
		return view;
	}
}
