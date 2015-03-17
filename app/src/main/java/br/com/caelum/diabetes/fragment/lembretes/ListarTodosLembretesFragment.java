package br.com.caelum.diabetes.fragment.lembretes;

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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import br.com.caelum.diabetes.R;
import br.com.caelum.diabetes.dao.DbHelper;
import br.com.caelum.diabetes.dao.LembreteDao;
import br.com.caelum.diabetes.model.Lembrete;

public class ListarTodosLembretesFragment extends Fragment {

	private ListView listaLembretes;
	private List<Lembrete> lembretes;
	private ArrayAdapter<Lembrete> adapter;
	protected Lembrete lembreteSelecionado;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.listar_lembretes, null);

		listaLembretes = (ListView) view.findViewById(R.id.list_lembretes_all);
		listaLembretes.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View view, int position, long id) {
				lembreteSelecionado = (Lembrete) listaLembretes.getItemAtPosition(position);
				return false;
			}
		});
		
		registerForContextMenu(listaLembretes);

		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		carregaLista();
	}

	private void carregaLista() {
		DbHelper helper = new DbHelper(getActivity());
		LembreteDao dao = new LembreteDao(helper);
		
		lembretes = dao.getLembretes();
		
		helper.close();
		
		adapter = new ArrayAdapter<Lembrete>(getActivity(), android.R.layout.simple_list_item_1, lembretes);
		listaLembretes.setAdapter(adapter);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		MenuItem delete = menu.add("Deletar");
		delete.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				DbHelper helper = new DbHelper(getActivity());
				LembreteDao dao = new LembreteDao(helper);
				dao.deletar(lembreteSelecionado);
				helper.close();
				carregaLista();
				
				return false;
			}
		});
	}
}
