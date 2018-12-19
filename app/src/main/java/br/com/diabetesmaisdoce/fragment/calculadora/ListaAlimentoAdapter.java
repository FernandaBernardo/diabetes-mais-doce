package br.com.diabetesmaisdoce.fragment.calculadora;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import br.com.diabetesmaisdoce.R;
import br.com.diabetesmaisdoce.extras.ParserTools;
import br.com.diabetesmaisdoce.model.AlimentoVirtual;

public class ListaAlimentoAdapter extends BaseAdapter{
	
	private List<AlimentoVirtual> alimentos;
	private Activity activity;

	public ListaAlimentoAdapter(List<AlimentoVirtual> alimentos, Activity activity) {
		this.alimentos = alimentos;
		this.activity = activity;
	}
	
	@Override
	public int getCount() {
		return alimentos.size();
	}

	@Override
	public Object getItem(int pos) {
		return alimentos.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return alimentos.get(pos).getId();
	}

	@Override
	public View getView(int pos, View view, ViewGroup viewGroup) {
		LayoutInflater inflater = activity.getLayoutInflater();
		View item = inflater.inflate(R.layout.item_alimento, null);
		
		AlimentoVirtual alimento = alimentos.get(pos);
		
		TextView campoNome = (TextView) item.findViewById(R.id.refeicao_alimento);
		campoNome.setText(alimento.getAlimento().getNome());
		
		TextView campoCho = (TextView) item.findViewById(R.id.refeicao_alimento_cho);
		campoCho.setText(ParserTools.getParseDouble(alimento.getTotalCarboidrato()) + "g");
		
		return item;
	}
}
