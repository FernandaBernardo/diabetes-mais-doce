package br.com.diabetesmaisdoce.fragment.lembretes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import br.com.diabetesmaisdoce.R;
import br.com.diabetesmaisdoce.activity.MainActivity;
import br.com.diabetesmaisdoce.dao.DbHelper;
import br.com.diabetesmaisdoce.dao.LembreteDao;
import br.com.diabetesmaisdoce.extras.PickerDialog;
import br.com.diabetesmaisdoce.extras.ValidaCampos;
import br.com.diabetesmaisdoce.model.Lembrete;
import br.com.diabetesmaisdoce.util.ValidatorUtils;

public class NovoLembreteFragment extends Fragment {
	private Lembrete lembrete;
	private EditText atividade;
	private EditText anotacoes;
	private Button salvar;
    private PickerDialog pickerDialog;

	@Override
	public void onResume() {
		super.onResume();
        ((MainActivity) getActivity()).setTitleHeader("Novo Lembrete");
        ((MainActivity) getActivity()).setBackArrowIcon();
	}

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.novo_lembrete, null);

		TextView horario = (TextView) view.findViewById(R.id.hora_lembrete);
		TextView data = (TextView) view.findViewById(R.id.data_lembrete);

        pickerDialog = new PickerDialog(getFragmentManager(), data, horario);

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
