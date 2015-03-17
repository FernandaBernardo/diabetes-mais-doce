package br.com.caelum.diabetes.fragment.calculadora;

import java.util.List;

import org.joda.time.DateTime;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import br.com.caelum.diabetes.R;
import br.com.caelum.diabetes.model.Refeicao;

public class ListaRefeicaoAdapter extends BaseAdapter{
	private List<Refeicao> refeicoes;
	private Activity activity;

	public ListaRefeicaoAdapter(List<Refeicao> refeicoes, Activity activity) {
		this.refeicoes = refeicoes;
		this.activity = activity;
	}
	
	@Override
	public int getCount() {
		return refeicoes.size();
	}
	
	@Override
	public Object getItem(int pos) {
		return refeicoes.get(pos);
	}
	
	@Override
	public long getItemId(int pos) {
		return refeicoes.get(pos).getId();
	}
	
	@Override
	public View getView(int pos, View view, ViewGroup viewGroup) {
		LayoutInflater inflater = activity.getLayoutInflater();
		View item = inflater.inflate(R.layout.item_refeicao, null);
		
		Refeicao refeicao = refeicoes.get(pos);
		
		TextView campoTipoRefeicao = (TextView) item.findViewById(R.id.refeicao_tipo_refeicao);
		campoTipoRefeicao.setText(refeicao.getTipoRefeicao().getText());
		TextView campoTotalCho= (TextView) item.findViewById(R.id.refeicao_total_cho);
		campoTotalCho.setText(refeicao.getTotalCHO()+"");
		
		TextView campoDia = (TextView) item.findViewById(R.id.refeicao_dia);
		DateTime data = refeicao.getData();
		campoDia.setText(data.getDayOfMonth() + "/" + data.getMonthOfYear() + "/" + data.getYear());
		
		return item;
	}
}
