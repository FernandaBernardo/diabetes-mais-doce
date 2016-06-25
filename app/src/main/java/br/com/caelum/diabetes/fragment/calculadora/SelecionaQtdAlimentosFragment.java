package br.com.caelum.diabetes.fragment.calculadora;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import br.com.caelum.diabetes.R;
import br.com.caelum.diabetes.activity.MainActivity;
import br.com.caelum.diabetes.model.AlimentoFisico;
import br.com.caelum.diabetes.model.AlimentoVirtual;
import br.com.caelum.diabetes.model.Refeicao;

/**
 * Created by Fê on 19/03/2015.
 */
public class SelecionaQtdAlimentosFragment extends Fragment {
    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setTitleHeader("Quantidade Alimentos");
        ((MainActivity) getActivity()).setBackArrowIcon();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.seleciona_qtd_alimentos, null);
        Bundle bundle = this.getArguments();
        ListView listView = (ListView) view.findViewById(R.id.lista_qtd_alimentos);
        Button concluirButton = (Button) view.findViewById(R.id.conclui_qtd);

        final Refeicao refeicao = (Refeicao) bundle.getSerializable("refeicao");
        final List<AlimentoFisico> alimentosBundle = (List<AlimentoFisico>) bundle.getSerializable("alimentosSelecionados");
        final List<AlimentoVirtual> alimentosSelecionados = new ArrayList<>();
        final List<AlimentoVirtual> alimentosExistentes = new ArrayList<>();

        for(AlimentoFisico alimentoFisico: alimentosBundle) {
            AlimentoVirtual alimentoExistente = refeicao.getAlimentoExistente(alimentoFisico);
            AlimentoVirtual alimentoVirtual;
            if(alimentoExistente == null) {
                alimentoVirtual = new AlimentoVirtual(alimentoFisico, 1.0, refeicao);
            } else {
                alimentoVirtual = alimentoExistente;
                alimentosExistentes.add(alimentoExistente);
            }
            alimentosSelecionados.add(alimentoVirtual);
        }

        SelecionaQtdAdapter adapter = new SelecionaQtdAdapter(alimentosSelecionados, getActivity());

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                EditText quantidade = (EditText) view.findViewById(R.id.qtd_cho);
                alimentosSelecionados.get(i).setQuantidade(Double.parseDouble(quantidade.getText().toString()));
            }
        });

        concluirButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refeicao.removeAlimentos(alimentosExistentes);
                refeicao.adicionaAlimentos(alimentosSelecionados);

                Bundle args = new Bundle();
                args.putSerializable("refeicao", refeicao);

                getFragmentManager().popBackStack("novarefeicao", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });

        return view;
    }
}
