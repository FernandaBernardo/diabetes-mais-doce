package br.com.caelum.diabetes.fragment.glicemia;

import java.util.List;

import org.joda.time.DateTime;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.caelum.diabetes.R;
import br.com.caelum.diabetes.model.Glicemia;

public class ListaGlicemiaAdapter extends BaseAdapter{
	private List<Glicemia> glicemias;
	private Activity activity;
	private View item;

	public ListaGlicemiaAdapter(List<Glicemia> glicemias, Activity activity) {
		this.glicemias = glicemias;
		this.activity = activity;
	}
	
	@Override
	public int getCount() {
		return glicemias.size();
	}

	@Override
	public Object getItem(int pos) {
		return glicemias.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return glicemias.get(pos).getId();
	}

	@Override
	public View getView(int pos, View view, ViewGroup viewGroup) {
		LayoutInflater inflater = activity.getLayoutInflater();
		item = inflater.inflate(R.layout.item_glicemia, null);
		
		Glicemia glicemia = glicemias.get(pos);
		
		TextView campoTipoRefeicao = (TextView) item.findViewById(R.id.glicemia_tipo_refeicao);
		campoTipoRefeicao.setText(glicemia.getTipoRefeicao().getText());
		
		TextView campoDia = (TextView) item.findViewById(R.id.glicemia_dia);
		DateTime data = glicemia.getData();
		campoDia.setText(data.getDayOfMonth() + "/" + data.getMonthOfYear() + "/" + data.getYear());
		
		TextView campoValor = (TextView) item.findViewById(R.id.glicemia_valor);
		int valorGlicemia = glicemia.getValorGlicemia();
		campoValor.setText(valorGlicemia + " mg/DL");
		
		ImageView campoImagem= (ImageView) item.findViewById(R.id.glicemia_imagem);
		if(valorGlicemia >= 100 && valorGlicemia < 200) {
			campoImagem.setImageResource(R.drawable.glicemia_boa);
		} else if (valorGlicemia < 100) {
			campoImagem.setImageResource(R.drawable.glicemia_media);
		} else {
			campoImagem.setImageResource(R.drawable.glicemia_ruim);
		}
		
		return item;
	}
}
