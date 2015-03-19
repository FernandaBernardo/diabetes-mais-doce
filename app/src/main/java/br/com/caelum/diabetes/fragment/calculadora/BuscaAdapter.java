package br.com.caelum.diabetes.fragment.calculadora;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.caelum.diabetes.R;
import br.com.caelum.diabetes.model.AlimentoFisico;

/**
 * Created by Fê on 18/03/2015.
 */
public class BuscaAdapter extends BaseAdapter implements Filterable {

    private List<AlimentoFisico> alimentos;
    private Activity activity;
    private List<AlimentoFisico> alimentosTemporario;

    public BuscaAdapter(List<AlimentoFisico> alimentos, Activity activity) {
        this.alimentos = alimentos;
        this.activity = activity;
        this.alimentosTemporario = alimentos;
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
        View vi = view;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (view == null) {
            vi = inflater.inflate(R.layout.item_busca_alimento, null);
        }

        AlimentoFisico alimento = alimentos.get(pos);

        TextView campoNome = (TextView) vi.findViewById(R.id.alimento_nome);
        campoNome.setText(alimento.getNome());

        TextView campoCho = (TextView) vi.findViewById(R.id.alimento_cho);
        campoCho.setText(alimento.getUnidadeDeMedida() + ": " + alimento.getCarboidrato() + "g");

        return vi;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {
                alimentos = (List<AlimentoFisico>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                ArrayList<AlimentoFisico> FilteredArrayNames = new ArrayList<AlimentoFisico>();

                if (alimentosTemporario == null) {
                    alimentosTemporario = new ArrayList<AlimentoFisico>(alimentos);
                }

                if (constraint == null || constraint.length() == 0) {
                    results.count = alimentosTemporario.size();
                    results.values = alimentosTemporario;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < alimentosTemporario.size(); i++) {
                        AlimentoFisico data = alimentosTemporario.get(i);
                        if (data.getNome().toLowerCase().startsWith(constraint.toString())) {
                            FilteredArrayNames.add(data);
                        }
                    }
                    results.count = FilteredArrayNames.size();
                    results.values = FilteredArrayNames;
                }
                return results;
            }
        };
        return filter;
    }
}
