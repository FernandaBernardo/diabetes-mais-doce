package br.com.caelum.diabetes.fragment.glicemia;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import br.com.caelum.diabetes.R;
import br.com.caelum.diabetes.dao.DbHelper;
import br.com.caelum.diabetes.dao.GlicemiaDao;
import br.com.caelum.diabetes.model.Glicemia;

public class ListaGlicemiaFragment extends Fragment{
	private ListView listaGlicemias;
	protected Glicemia glicemiaSelecionada;
	private ListaGlicemiaAdapter adapter;
	private List<Glicemia> glicemias;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.lista_glicemia, null);
		
		listaGlicemias = (ListView) view.findViewById(R.id.glicemias);
		
		listaGlicemias.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int pos, long arg3) {
				glicemiaSelecionada = (Glicemia) listaGlicemias.getItemAtPosition(pos);
				final ImageView campoOpcoes = (ImageView) v.findViewById(R.id.glicemia_opcoes);
				campoOpcoes.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						registerForContextMenu(campoOpcoes);						
						campoOpcoes.showContextMenu();
					}
				});
			}
		});
		
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		carregaLista();
	}
	
	private void carregaLista() {
		DbHelper helper = new DbHelper(getActivity());
		GlicemiaDao dao = new GlicemiaDao(helper);
		
		glicemias = dao.getGlicemias();
		
		helper.close();
		
		adapter = new ListaGlicemiaAdapter(glicemias, getActivity());
		
		listaGlicemias.setAdapter(adapter);		
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		MenuItem delete = menu.add("Deletar");
		delete.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				DbHelper helper = new DbHelper(getActivity());
				GlicemiaDao dao = new GlicemiaDao(helper);
				dao.deletar(glicemiaSelecionada);
				helper.close();
				carregaLista();
				
				return false;
			}
		});
	}
}
