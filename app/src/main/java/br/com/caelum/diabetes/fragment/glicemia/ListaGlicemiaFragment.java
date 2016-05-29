package br.com.caelum.diabetes.fragment.glicemia;

import java.util.Collections;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
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
import android.widget.TextView;

import br.com.caelum.diabetes.R;
import br.com.caelum.diabetes.activity.MainActivity;
import br.com.caelum.diabetes.dao.DbHelper;
import br.com.caelum.diabetes.dao.GlicemiaDao;
import br.com.caelum.diabetes.model.Glicemia;

public class ListaGlicemiaFragment extends Fragment{
	private ListView listaGlicemias;
	protected Glicemia glicemiaSelecionada;
	private ListaGlicemiaAdapter adapter;
	private List<Glicemia> glicemias;

	@Override
	public void onResume() {
		super.onResume();
		carregaLista();
        ((MainActivity) getActivity()).setTitleHeader("Glicemias");
        ((MainActivity) getActivity()).setBackArrowIcon();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.lista_glicemia, null);

		listaGlicemias = (ListView) view.findViewById(R.id.glicemias);
        listaGlicemias.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                glicemiaSelecionada = (Glicemia) listaGlicemias.getItemAtPosition(position);
                return false;
            }
        });

        registerForContextMenu(listaGlicemias);

		return view;
	}
	
	private void carregaLista() {
		DbHelper helper = new DbHelper(getActivity());
		GlicemiaDao dao = new GlicemiaDao(helper);
		
		glicemias = dao.getGlicemias();

        helper.close();

        Collections.sort(glicemias);

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
