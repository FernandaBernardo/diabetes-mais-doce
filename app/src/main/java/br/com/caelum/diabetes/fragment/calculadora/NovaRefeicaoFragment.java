package br.com.caelum.diabetes.fragment.calculadora;

import java.util.List;

import org.joda.time.DateTime;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextClock;
import android.widget.TimePicker;
import br.com.caelum.diabetes.R;
import br.com.caelum.diabetes.calculos.CalculaInsulina;
import br.com.caelum.diabetes.calculos.DescobreTipoRefeicao;
import br.com.caelum.diabetes.dao.DbHelper;
import br.com.caelum.diabetes.dao.PacienteDao;
import br.com.caelum.diabetes.dao.RefeicaoDao;
import br.com.caelum.diabetes.extras.TipoRefeicao;
import br.com.caelum.diabetes.model.AlimentoVirtual;
import br.com.caelum.diabetes.model.Paciente;
import br.com.caelum.diabetes.model.Refeicao;

@SuppressLint("NewApi")
public class NovaRefeicaoFragment extends Fragment{
	private Refeicao refeicao;
	private Paciente paciente;
	private View view;
	private int dia;
	private int mes;
	private int ano;
	private int hora;
	private int minuto;
	protected AlimentoVirtual alimentoSelecionado;
	private ListView campoLista;
	private EditText totalCHO;
	private EditText totalInsulina;
	
	void carregaBundle(){
		if(null == refeicao){
			refeicao = new Refeicao();
		} else {
			carregaLista();
			atualizaDadosTotais();
		}
	}
	
	private void atualizaDadosTotais() {
		totalCHO.setText(String.valueOf(refeicao.getTotalCHO()) + " g");
		
		double valorInsulina = new CalculaInsulina(refeicao, paciente).getTotalInsulina();
		
		totalInsulina.setText(String.valueOf(valorInsulina) + " U");
	}

	@Override
	public void onResume() {
		super.onResume();
		carregaBundle();
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
		
		DateTime dataAgora = new DateTime();
		
		final TextClock horario = (TextClock) view.findViewById(R.id.hora_refeicao);
		horario.setText(dataAgora.getHourOfDay() + ":" + dataAgora.getMinuteOfHour());
		final TextClock data = (TextClock) view.findViewById(R.id.data_refeicao);
		data.setText(dataAgora.getDayOfMonth() + "/" + dataAgora.getMonthOfYear() + "/" + dataAgora.getYear());
		
		String dataAtual = (String)data.getText();
		String[] numerosData = dataAtual.split("/");
		
		String horarioAtual = (String) horario.getText();
		String[] numerosHorario= horarioAtual.split(":");
		
		dia = Integer.parseInt(numerosData[0]);
		mes = Integer.parseInt(numerosData[1]) - 1;
		ano = Integer.parseInt(numerosData[2]);
		hora = Integer.parseInt(numerosHorario[0]);
		minuto = Integer.parseInt(numerosHorario[1]);
		
		horario.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
	            TimePickerDialog timePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
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
		        DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker arg0, int year, int month, int day) {
						dia = day;
						mes = month;
						ano = year;
						data.setText(dia+ "/" + (mes+1) + "/" + ano);
					}
		        }, ano, mes, dia);
		        datePicker.show();
			}
		});
		
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
		
		String tipo;
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
				transaction.addToBackStack(null);
				transaction.commit();
			}
		});
		
		Button salvar = (Button) view.findViewById(R.id.salvar_refeicao);
		salvar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				DateTime dateTime = new DateTime(ano, mes+1, dia, hora, minuto);
				refeicao.setData(dateTime);
				
				DbHelper helper = new DbHelper(getActivity());
				
				RefeicaoDao refeicaoDao = new RefeicaoDao(helper);
				refeicaoDao.salva(refeicao);
				
				helper.close();
				
				getFragmentManager().popBackStack();
			}
		});
		
		return view;
	}

	private void carregaLista() {
		List<AlimentoVirtual> alimentos = refeicao.getAlimentos();
		ListaAlimentoAdapter adapter = new ListaAlimentoAdapter(alimentos, getActivity());
		campoLista.setAdapter(adapter);
	}
}