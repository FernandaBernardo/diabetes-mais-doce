package br.com.caelum.diabetes.fragment;

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
import android.widget.TextView;

import br.com.caelum.diabetes.R;
import br.com.caelum.diabetes.dao.DbHelper;
import br.com.caelum.diabetes.dao.PacienteDao;
import br.com.caelum.diabetes.dialog.PreencherDadosMedicosDialog;
import br.com.caelum.diabetes.fragment.calculadora.DashboardCalculadoraFragment;
import br.com.caelum.diabetes.fragment.glicemia.DashboardGlicemiaFragment;
import br.com.caelum.diabetes.fragment.lembretes.DashboardLembreteFragment;
import br.com.caelum.diabetes.fragment.perfil.ConfigurarPerfilFragment;
import br.com.caelum.diabetes.model.Paciente;

public class DashboardFragment extends Fragment {

	private PacienteDao dao;
	private Paciente paciente;

	private void getPaciente() {
		DbHelper helper = new DbHelper(getActivity());
		dao = new PacienteDao(helper);
		paciente = dao.getPaciente();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dashboard, null);

        TextView titulo = (TextView) getActivity().findViewById(R.id.titulo);
        titulo.setText("Diabetes Mais Doce");

		Button calculadora = (Button) view.findViewById(R.id.main_calculadora);
		calculadora.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getPaciente();

				if (!paciente.temValorCorrecao()) {
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

//		Button perfil = (Button) view.findViewById(R.id.main_perfil);
//		perfil.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				FragmentTransaction transaction = getFragmentManager().beginTransaction();
//				transaction.replace(R.id.main_view, new ConfigurarPerfilFragment());
//				transaction.addToBackStack(null);
//				transaction.commit();
//			}
//		});

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

        Button exames = (Button) view.findViewById(R.id.main_exames);
        Button estatisticas = (Button) view.findViewById(R.id.main_estatisticas);

        calculadora.setText(Html.fromHtml("<b><big>" + "Calculadora" + "</big></b>" + "<br />" +
                "<small>" + "Calcule os carboidratos consumidos" + "</small>"));
        lembrete.setText(Html.fromHtml("<b><big>" + "Lembretes" + "</big></b>" + "<br />" +
                "<small>" + "Não esqueça de se alimentar de 3 em 3 horas" + "</small>"));
        medicao.setText(Html.fromHtml("<b><big>" + "Glicemia" + "</big></b>" + "<br />" +
                "<small>" + "Agora ficou fácil monitorar sua glicemia no decorrer do mês" + "</small>"));
        exames.setText(Html.fromHtml("<b><big>" + "Exames (logo)" + "</big></b>" + "<br />" +
                "<small>" + "Lembre-se de todos os seus exames" + "</small>"));
        estatisticas.setText(Html.fromHtml("<b><big>" + "Estatísticas (logo)" + "</big></b>" + "<br />" +
                "<small>" + "Acompanhe a evolução dos seus níveis glicêmicos" + "</small>"));
        return view;
	}
}