package br.com.diabetesmaisdoce.fragment.calculadora;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import br.com.diabetesmaisdoce.R;
import br.com.diabetesmaisdoce.model.AlimentoFisico;

/**
 * Created by Fê on 18/03/2015.
 * Updated by Jeancsanchez on 31/08/2017
 */
@SuppressWarnings("SpellCheckingInspection")
public class BuscaAdapter extends RecyclerView.Adapter<BuscaAdapter.BuscaViewHolder> implements Filterable {

    private List<AlimentoFisico> alimentos;
    private Activity activity;
    private List<AlimentoFisico> alimentosTemporario;

    public BuscaAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public BuscaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_busca_alimento, parent, false);
        return new BuscaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BuscaViewHolder holder, int position) {
        final int currentPosition = position;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox checkbox = (CheckBox) view.findViewById(R.id.check_alimento);
                checkbox.setChecked(!checkbox.isChecked());
                onClickCheckbox(currentPosition);
            }
        });

        AlimentoFisico alimento = alimentos.get(position);
        holder.campoNome.setText(alimento.getNome());
        holder.campoCho.setText(alimento.getUnidadeDeMedida() + ": " + alimento.getCarboidrato() + "g");

        if (alimento.isCheck()) holder.checkbox.setChecked(true);
        else holder.checkbox.setChecked(false);

        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickCheckbox(currentPosition);
            }
        });
    }

    public void setAlimentos(List<AlimentoFisico> alimentos){
        this.alimentosTemporario = alimentos;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return alimentos == null ? 0 : alimentos.size();
    }

    @Override
    public long getItemId(int pos) {
        return alimentos.get(pos).getId();
    }


    public void onClickCheckbox(int position) {
        alimentos.get(position).setCheck(!alimentos.get(position).isCheck());
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                alimentos = (List<AlimentoFisico>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                ArrayList<AlimentoFisico> filteredArrayNames = new ArrayList<AlimentoFisico>();

                if (alimentosTemporario == null) {
                    alimentosTemporario = new ArrayList<AlimentoFisico>(alimentos);
                }

                if (constraint == null || constraint.length() == 0) {
                    results.count = alimentosTemporario.size();
                    results.values = alimentosTemporario;
                } else {
                    constraint = removeDiacriticalMarks(constraint.toString().toLowerCase().trim());
                    for (int i = 0; i < alimentosTemporario.size(); i++) {
                        AlimentoFisico data = alimentosTemporario.get(i);
                        if (removeDiacriticalMarks(data.getNome().toLowerCase()).contains(constraint.toString())) {
                            filteredArrayNames.add(data);
                        }
                    }
                    results.count = filteredArrayNames.size();
                    results.values = filteredArrayNames;
                }
                return results;
            }
        };
        return filter;
    }

    private static String removeDiacriticalMarks(String string) {
        return Normalizer.normalize(string, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }


    class BuscaViewHolder extends RecyclerView.ViewHolder {
        TextView campoNome;
        TextView campoCho;
        CheckBox checkbox;

        public BuscaViewHolder(View itemView) {
            super(itemView);
            campoNome = (TextView) itemView.findViewById(R.id.alimento_nome);
            campoCho = (TextView) itemView.findViewById(R.id.alimento_cho);
            checkbox = (CheckBox) itemView.findViewById(R.id.check_alimento);
        }
    }
}
