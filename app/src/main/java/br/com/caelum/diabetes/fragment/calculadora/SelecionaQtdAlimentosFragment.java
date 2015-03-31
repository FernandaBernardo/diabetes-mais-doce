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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.caelum.diabetes.R;
import br.com.caelum.diabetes.model.AlimentoFisico;
import br.com.caelum.diabetes.model.AlimentoVirtual;
import br.com.caelum.diabetes.model.Refeicao;

/**
 * Created by Fê on 19/03/2015.
 */
public class SelecionaQtdAlimentosFragment extends Fragment {
    private List<AlimentoFisico> alimentos;
    private Refeicao refeicao;
    private SelecionaQtdAdapter adapter;
    private ListView list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.seleciona_qtd_alimentos, null);

        TextView titulo = (TextView) getActivity().findViewById(R.id.titulo);
        titulo.setText("Quantidade Alimentos");

        Bundle bundle = this.getArguments();

        alimentos = (List<AlimentoFisico>) bundle.getSerializable("alimentosSelecionados");
        refeicao = (Refeicao) bundle.getSerializable("refeicao");

        for(AlimentoFisico aux: alimentos) {
            refeicao.adicionaAlimento(new AlimentoVirtual(aux, 1.0, refeicao));
        }

        adapter = new SelecionaQtdAdapter(refeicao.getAlimentos(), getActivity());

        list = (ListView) view.findViewById(R.id.lista_qtd_alimentos);
        list.setAdapter(adapter);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                EditText qtd = (EditText) view.findViewById(R.id.qtd_cho);
                refeicao.getAlimentos().get(i).setQuantidade(Double.parseDouble(qtd.getText().toString()));
            }
        });

        Button concluir = (Button) view.findViewById(R.id.conclui_qtd);
        concluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<AlimentoVirtual> listAux = new ArrayList<AlimentoVirtual>();
                for (int i = 0; i < list.getAdapter().getCount(); i++) {
                    AlimentoVirtual aux = (AlimentoVirtual) list.getAdapter().getItem(i);
                    listAux.add(aux);
                }
                refeicao.setAlimentos(listAux);

                Bundle args = new Bundle();
                args.putSerializable("refeicao", refeicao);

                getFragmentManager().popBackStack("novarefeicao", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });


        return view;
    }
}
