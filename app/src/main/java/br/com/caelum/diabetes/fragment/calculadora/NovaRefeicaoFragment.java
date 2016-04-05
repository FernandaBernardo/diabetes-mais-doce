package br.com.caelum.diabetes.fragment.calculadora;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.List;

import br.com.caelum.diabetes.R;
import br.com.caelum.diabetes.activity.MainActivity;
import br.com.caelum.diabetes.calculos.CalculaInsulina;
import br.com.caelum.diabetes.calculos.DescobreTipoRefeicao;
import br.com.caelum.diabetes.dao.DbHelper;
import br.com.caelum.diabetes.dao.PacienteDao;
import br.com.caelum.diabetes.dao.RefeicaoDao;
import br.com.caelum.diabetes.extras.PickerDialog;
import br.com.caelum.diabetes.extras.TipoRefeicao;
import br.com.caelum.diabetes.model.AlimentoVirtual;
import br.com.caelum.diabetes.model.Paciente;
import br.com.caelum.diabetes.model.Refeicao;

public class NovaRefeicaoFragment extends Fragment {
	private Refeicao refeicao;
	private Paciente paciente;
	private View view;
	protected AlimentoVirtual alimentoSelecionado;
	private ListView campoLista;
	private EditText totalCHO;
	private EditText totalInsulina;
    private TextView dataRefeicao;
    private TextView horaRefeicao;
    private PickerDialog pickerDialog;

	@Override
	public void onResume() {
		super.onResume();
		carregaBundle();
        ((MainActivity) getActivity()).setTitleHeader("Nova Refeição");
        ((MainActivity) getActivity()).setBackArrowIcon();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.nova_refeicao, null);

		totalCHO = (EditText) view.findViewById(R.id.totalCHO);
		totalInsulina = (EditText) view.findViewById(R.id.totalInsulina);
		
		carregaBundle();
		
		DbHelper helper = new DbHelper(getActivity());
		
		PacienteDao pacienteDao = new PacienteDao(helper);
		this.paciente = pacienteDao.getPaciente();
		
		helper.close();

        dataRefeicao = (TextView) view.findViewById(R.id.data_refeicao);
        horaRefeicao = (TextView) view.findViewById(R.id.hora_refeicao);

        pickerDialog = new PickerDialog(getFragmentManager(), dataRefeicao, horaRefeicao);
        pickerDialog.setListener();
        pickerDialog.setText();

        campoLista = (ListView) view.findViewById(R.id.lista_alimentos);

		carregaLista();
		
		campoLista.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int pos, long arg3) {
				alimentoSelecionado = (AlimentoVirtual) campoLista.getItemAtPosition(pos);
				final ImageView campoDeletar = (ImageView) v.findViewById(R.id.refeicao_alimento_deletar);
				campoDeletar.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						refeicao.getAlimentos().remove(alimentoSelecionado);
						carregaLista();
						atualizaDadosTotais();
					}
				});				
			}
		});
		
		Spinner tipoRefeicao = (Spinner) view.findViewById(R.id.tipo_refeicao);
		final ArrayAdapter<String> spinnerAdapter = (ArrayAdapter<String>) tipoRefeicao.getAdapter();
		
		String tipo = null;
		if(refeicao.getTipoRefeicao() == null) {
			tipo = new DescobreTipoRefeicao().getTipoRefeicao().getText();
		} else {
			tipo = refeicao.getTipoRefeicao().getText();
		}
		
		int position = spinnerAdapter.getPosition(tipo);
		if (position == -1) position = 0;
		tipoRefeicao.setSelection(position);
		refeicao.setTipoRefeicao(TipoRefeicao.fromString(tipo));
		
		tipoRefeicao.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View view, int pos, long id) {
				String item = spinnerAdapter.getItem(pos);
				refeicao.setTipoRefeicao(TipoRefeicao.fromString(item));
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		
		Button novoAlimento = (Button) view.findViewById(R.id.novo_alimento);
		novoAlimento.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Bundle bundle = new Bundle();
				bundle.putSerializable("refeicao", refeicao);
				
				Fragment fragment = new AdicionaAlimentoFragment();
				fragment.setArguments(bundle);
				
				FragmentTransaction transaction = getFragmentManager().beginTransaction();
				transaction.replace(R.id.main_view, fragment);
				transaction.addToBackStack("novarefeicao");
				transaction.commit();
			}
		});
		
		Button salvar = (Button) view.findViewById(R.id.salvar_refeicao);
		salvar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				refeicao.setData(pickerDialog.getDataSelecionada());
				
				DbHelper helper = new DbHelper(getActivity());
				
				RefeicaoDao refeicaoDao = new RefeicaoDao(helper);
				refeicaoDao.salva(refeicao);
				
				helper.close();
				
				getFragmentManager().popBackStack();
			}
		});
		
		return view;
	}

    private void carregaBundle(){
        if(null == refeicao){
            refeicao = new Refeicao();
        } else {
            carregaLista();
            atualizaDadosTotais();
        }
    }

    private void atualizaDadosTotais() {
        totalCHO.setText(String.valueOf(refeicao.getTotalCHO()) + " g");

        SharedPreferences settings = getActivity().getSharedPreferences("CalculoInsulina", 0);
        final boolean calculoInsulina = settings.getBoolean("calculoInsulina", false);

        if (calculoInsulina) {
            double valorInsulina = new CalculaInsulina(refeicao, paciente).getTotalInsulina();

            totalInsulina.setText(String.valueOf(valorInsulina) + " U");
        } else {
            TextView texto = (TextView) view.findViewById(R.id.textoTotalInsulina);
            texto.setVisibility(View.INVISIBLE);
            totalInsulina.setVisibility(View.INVISIBLE);
        }
    }

	private void carregaLista() {
		List<AlimentoVirtual> alimentos = refeicao.getAlimentos();
		ListaAlimentoAdapter adapter = new ListaAlimentoAdapter(alimentos, getActivity());
		campoLista.setAdapter(adapter);
	}
}