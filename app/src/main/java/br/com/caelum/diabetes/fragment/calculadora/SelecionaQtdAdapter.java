package br.com.caelum.diabetes.fragment.calculadora;

import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import br.com.caelum.diabetes.R;
import br.com.caelum.diabetes.model.AlimentoFisico;
import br.com.caelum.diabetes.model.AlimentoVirtual;

/**
 * Created by Fê on 19/03/2015.
 */
public class SelecionaQtdAdapter extends BaseAdapter {
    private final List<AlimentoFisico> alimentos;
    private final FragmentActivity activity;

    public SelecionaQtdAdapter(List<AlimentoFisico> alimentos, FragmentActivity activity) {
        this.alimentos = alimentos;
        this.activity = activity;
    }
    @Override
    public int getCount() {
        return alimentos.size();
    }

    @Override
    public Object getItem(int i) {
        return alimentos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return alimentos.get(i).getId();
    }

    @Override
    public View getView(int pos, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View item = inflater.inflate(R.layout.item_alimento_qtd, null);

        AlimentoFisico alimento = alimentos.get(pos);

        TextView nome = (TextView) item.findViewById(R.id.alimento_qtd);
        nome.setText(alimento.getNome());

        EditText porcao = (EditText) item.findViewById(R.id.porcao_qtd);
        porcao.setText(alimento.getUnidadeDeMedida());

        return item;
    }
}
