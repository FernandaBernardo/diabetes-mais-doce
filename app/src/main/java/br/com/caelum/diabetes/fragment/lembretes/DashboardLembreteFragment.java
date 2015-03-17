package br.com.caelum.diabetes.fragment.lembretes;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import br.com.caelum.diabetes.R;
import br.com.caelum.diabetes.dao.DbHelper;
import br.com.caelum.diabetes.dao.LembreteDao;
import br.com.caelum.diabetes.model.Lembrete;

public class DashboardLembreteFragment extends Fragment {
	private Button listarTodosLembretes;
	private Button novoLembrete;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.dashboard_lembrete, null);

		listarTodosLembretes = (Button) view.findViewById(R.id.ultimos_lembretes);

		listarTodosLembretes.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				setTransaction(new ListarTodosLembretesFragment());
			}
		});

		novoLembrete = (Button) view.findViewById(R.id.novo_lembrete);

		novoLembrete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				setTransaction(new NovoLembreteFragment());
			}
		});

		listarProximosLembretes(view);

		return view;
	}

	private void listarProximosLembretes(View view) {
		DbHelper helper = new DbHelper(getActivity());
		LembreteDao dao = new LembreteDao(helper);

		List<Lembrete> lembretes = dao.getLembretes(5);

		helper.close();

		ArrayAdapter<Lembrete> adapter = new ArrayAdapter<Lembrete>(getActivity(), android.R.layout.simple_list_item_1, lembretes);

		ListView listaLembretes = (ListView) view.findViewById(R.id.list_lembretes_proximos);
		listaLembretes.setAdapter(adapter);
	}

	private void setTransaction(Fragment fragment) {
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.main_view, fragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}
}
