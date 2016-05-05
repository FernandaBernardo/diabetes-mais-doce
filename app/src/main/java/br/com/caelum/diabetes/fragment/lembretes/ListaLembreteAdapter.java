package br.com.caelum.diabetes.fragment.lembretes;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import br.com.caelum.diabetes.R;
import br.com.caelum.diabetes.extras.ParserTools;
import br.com.caelum.diabetes.model.Lembrete;

/**
 * Created by Fê on 22/03/2015.
 */
public class ListaLembreteAdapter extends BaseAdapter {
    private final List<Lembrete> lembretes;
    private final Activity activity;

    public ListaLembreteAdapter(List<Lembrete> lembretes, Activity activity) {
        this.lembretes = lembretes;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return lembretes.size();
    }

    @Override
    public Object getItem(int i) {
        return lembretes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return lembretes.get(i).getId();
    }

    @Override
    public View getView(int pos, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View item = inflater.inflate(R.layout.item_lembrete, null);

        Lembrete lembrete = lembretes.get(pos);
        String dataAux = ParserTools.getParseDate(lembrete.getData())
                + " - " + ParserTools.getParseHour(lembrete.getData());
        TextView data = (TextView) item.findViewById(R.id.lembrete_data);
        data.setText(dataAux);
        TextView atividade = (TextView) item.findViewById(R.id.lembrete_nome);
        atividade.setText(lembrete.getAtividade());
        return item;
    }
}
