package br.com.caelum.diabetes.fragment.calculadora;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import br.com.caelum.diabetes.R;
import br.com.caelum.diabetes.model.AlimentoFisico;

/**
 * Created by Fê on 19/03/2015.
 */
public class SelecionaQtdAlimentosFragment extends Fragment {
    private List<AlimentoFisico> alimentos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.seleciona_qtd_alimentos, null);

        Bundle bundle = this.getArguments();

        alimentos = (List<AlimentoFisico>) bundle.getSerializable("alimentosSelecionados");

        SelecionaQtdAdapter adapter = new SelecionaQtdAdapter(alimentos, getActivity());

        ListView list = (ListView) view.findViewById(R.id.lista_qtd_alimentos);
        list.setAdapter(adapter);

        Button button = (Button) view.findViewById(R.id.conclui_qtd);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlimentoVirtual alimentoVirtual = new AlimentoVirtual(
/						alimentoAtual, Double.parseDouble(valor.getText()
								.toString()), refeicao);
				refeicao.adicionaAlimento(alimentoVirtual);

				Bundle args = new Bundle();
				args.putSerializable("refeicao", refeicao);

				getFragmentManager().popBackStack();
            }
        });


        return view;
    }
}
