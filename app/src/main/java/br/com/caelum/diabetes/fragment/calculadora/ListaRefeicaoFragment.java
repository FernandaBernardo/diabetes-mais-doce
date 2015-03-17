package br.com.caelum.diabetes.fragment.calculadora;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import br.com.caelum.diabetes.R;
import br.com.caelum.diabetes.dao.DbHelper;
import br.com.caelum.diabetes.dao.RefeicaoDao;
import br.com.caelum.diabetes.model.Refeicao;

public class ListaRefeicaoFragment extends Fragment{
	private List<Refeicao> refeicoes;
	private ListaRefeicaoAdapter adapter;
	private ListView listaRefeicoes;
	protected Refeicao refeicaoSelecionada;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.lista_refeicao, null);
		
		listaRefeicoes = (ListView) view.findViewById(R.id.lista_refeicoes);
		listaRefeicoes.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View view, int position, long id) {
				refeicaoSelecionada = (Refeicao) listaRefeicoes.getItemAtPosition(position);
				return false;
			}
		});
		
		registerForContextMenu(listaRefeicoes);
		
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		carregaLista();
	}

	private void carregaLista() {
		DbHelper helper = new DbHelper(getActivity());
		RefeicaoDao dao = new RefeicaoDao(helper);
		
		refeicoes = dao.getRefeicoes();
		
		helper.close();
		
		adapter = new ListaRefeicaoAdapter(refeicoes, getActivity());
		listaRefeicoes.setAdapter(adapter);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		MenuItem delete = menu.add("Deletar");
		delete.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				DbHelper helper = new DbHelper(getActivity());
				RefeicaoDao dao = new RefeicaoDao(helper);
				dao.deletar(refeicaoSelecionada);
				helper.close();
				carregaLista();
				
				return false;
			}
		});
	}
}
