package br.com.diabetesmaisdoce.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import br.com.diabetesmaisdoce.R;
import br.com.diabetesmaisdoce.activity.MainActivity;
import br.com.diabetesmaisdoce.dao.DbHelper;
import br.com.diabetesmaisdoce.dao.PacienteDao;
import br.com.diabetesmaisdoce.dialog.PreencherDadosMedicosDialog;
import br.com.diabetesmaisdoce.extras.Extras;
import br.com.diabetesmaisdoce.fragment.calculadora.DashboardCalculadoraFragment;
import br.com.diabetesmaisdoce.fragment.estatisticas.DashboardEstatisticasFragment;
import br.com.diabetesmaisdoce.fragment.glicemia.DashboardGlicemiaFragment;
import br.com.diabetesmaisdoce.fragment.lembretes.DashboardLembreteFragment;
import br.com.diabetesmaisdoce.model.Paciente;

public class DashboardFragment extends Fragment {
    private Paciente paciente;

    private void getPaciente() {
		DbHelper helper = new DbHelper(getActivity());
        PacienteDao dao = new PacienteDao(helper);
        paciente = dao.getPaciente();
	}

	@Override
	public void onResume() {
		super.onResume();
        ((MainActivity) getActivity()).setTitleHeader("Diabetes Mais Doce");
        ((MainActivity) getActivity()).setHamburguerIcon();
		((MainActivity) getActivity()).showSettings();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dashboard, null);

        SharedPreferences settings = getActivity().getSharedPreferences(Extras.PREFS_NAME, 0);
        final boolean calculoInsulina = settings.getBoolean(Extras.PREFS_NAME_INSULINA_CORRECAO, true);

        Button calculadora = (Button) view.findViewById(R.id.main_calculadora);
		calculadora.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getPaciente();

				if (!paciente.temValorCorrecao() && calculoInsulina) {
					PreencherDadosMedicosDialog dadosMedicosDialog = new PreencherDadosMedicosDialog();
					FragmentManager fm = getFragmentManager();
					dadosMedicosDialog.show(fm, "dashboard_fragment");
				} else {
					FragmentTransaction transaction = getFragmentManager().beginTransaction();
					transaction.replace(R.id.main_view, new DashboardCalculadoraFragment());
					transaction.addToBackStack(null);
					transaction.commit();
				}
			}
		});

		Button medicao = (Button) view.findViewById(R.id.main_glicemia);
		medicao.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				FragmentTransaction transaction = getFragmentManager().beginTransaction();
				transaction.replace(R.id.main_view, new DashboardGlicemiaFragment());
				transaction.addToBackStack(null);
				transaction.commit();
			}
		});

		Button lembrete = (Button) view.findViewById(R.id.main_lembretes);
		lembrete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				FragmentTransaction transaction = getFragmentManager().beginTransaction();
				transaction.replace(R.id.main_view, new DashboardLembreteFragment());
				transaction.addToBackStack(null);
				transaction.commit();
			}
		});

        Button estatisticas = (Button) view.findViewById(R.id.main_estatisticas);
        estatisticas.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.main_view, new DashboardEstatisticasFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

//        Button exames = (Button) view.findViewById(R.id.main_exames);

        calculadora.setText(Html.fromHtml("<b><big>" + "Calculadora" + "</big></b>" + "<br />" +
                "<small>" + "Calcule os carboidratos consumidos" + "</small>"));
        lembrete.setText(Html.fromHtml("<b><big>" + "Lembretes" + "</big></b>" + "<br />" +
                "<small>" + "Não esqueça de se alimentar de 3 em 3 horas" + "</small>"));
        medicao.setText(Html.fromHtml("<b><big>" + "Glicemia" + "</big></b>" + "<br />" +
                "<small>" + "Agora ficou fácil monitorar sua glicemia no decorrer do mês" + "</small>"));
        estatisticas.setText(Html.fromHtml("<b><big>" + "Estatísticas" + "</big></b>" + "<br />" +
                "<small>" + "Acompanhe a evolução dos seus níveis glicêmicos" + "</small>"));
//        exames.setText(Html.fromHtml("<b><big>" + "Exames (logo)" + "</big></b>" + "<br />" +
//                "<small>" + "Lembre-se de todos os seus exames" + "</small>"));
        return view;
	}
}