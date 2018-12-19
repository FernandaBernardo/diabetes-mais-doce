package br.com.diabetesmaisdoce.fragment.calculadora;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import br.com.diabetesmaisdoce.R;
import br.com.diabetesmaisdoce.activity.MainActivity;
import br.com.diabetesmaisdoce.dao.AlimentoFisicoDao;
import br.com.diabetesmaisdoce.dao.DbHelper;
import br.com.diabetesmaisdoce.model.AlimentoFisico;
import br.com.diabetesmaisdoce.model.Refeicao;

public class AdicionaAlimentoFragment extends Fragment {

	private AlimentoFisicoDao alimentoDao;
	private Refeicao refeicao;
	private DbHelper helper;
    private BuscaAdapter adapter;
    private List<AlimentoFisico> alimentos;
    private ArrayList<AlimentoFisico> alimentosSelecionados;

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setTitleHeader("Adiciona Alimento");
        ((MainActivity) getActivity()).setBackArrowIcon();
    }

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.lista_busca_alimento, null);

		Bundle bundle = this.getArguments();

		refeicao = (Refeicao) bundle.getSerializable("refeicao");
		
		helper = new DbHelper(getActivity());
		alimentoDao = new AlimentoFisicoDao(helper);

        // Make sure that this call is on separated thread (async mode) for no block the main UI thread.
		alimentos = alimentoDao.getAlimentos();
		
		helper.close();

        adapter = new BuscaAdapter(getActivity());
        adapter.setAlimentos(alimentos);

        EditText edit = (EditText) view.findViewById(R.id.busca_alimento);
        edit.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void afterTextChanged(Editable s) {}
        });

        // Maybe is more useful to use RecyclerView instead ListView for better performance :) See more: https://goo.gl/6gfk6B
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.lista_busca_alimentos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        alimentosSelecionados = new ArrayList<>();

        TextView cadastra = (TextView) view.findViewById(R.id.cadastrar_alimento);
        cadastra.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.main_view, new NovoAlimentoDiferenteFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        Button conclui = (Button) view.findViewById(R.id.conclui_alimentos);
        conclui.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                for(AlimentoFisico alimento: alimentos) {
                    if (alimento.isCheck()) alimentosSelecionados.add(alimento);
                }

                Bundle bundle = new Bundle();
                bundle.putSerializable("alimentosSelecionados", alimentosSelecionados);
                bundle.putSerializable("refeicao", refeicao);

                Fragment fragment = new SelecionaQtdAlimentosFragment();
                fragment.setArguments(bundle);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.main_view, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        Button cancela = (Button) view.findViewById(R.id.cancelar_alimentos);
        cancela.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        return view;
	}
}
