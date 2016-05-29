package br.com.caelum.diabetes.fragment.estatisticas;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import br.com.caelum.diabetes.R;
import br.com.caelum.diabetes.activity.MainActivity;
import br.com.caelum.diabetes.dao.DbHelper;
import br.com.caelum.diabetes.dao.GlicemiaDao;
import br.com.caelum.diabetes.extras.PickerDialog;
import br.com.caelum.diabetes.extras.PlanilhaExcel;
import br.com.caelum.diabetes.model.Glicemia;

/**
 * Created by Fernanda Bernardo on 08/05/2016.
 */
public class GeraTabelaFragment extends Fragment {
    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setTitleHeader("Gerar Tabela");
        ((MainActivity) getActivity()).setBackArrowIcon();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.gera_tabela, null);

        Button salvar = (Button) view.findViewById(R.id.gerar_tabela);
        TextView dataInicialText = (TextView) view.findViewById(R.id.data_inicial_tabela);
        final TextView dataFinalText= (TextView) view.findViewById(R.id.data_final_tabela);

        Calendar dataInicial = Calendar.getInstance();
        dataInicial.add(Calendar.MONTH, -1);

        final PickerDialog dataInicialPicker = new PickerDialog(getFragmentManager(), dataInicialText, dataInicial);
        final PickerDialog dataFinalPicker = new PickerDialog(getFragmentManager(), dataFinalText);

        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DbHelper helper = new DbHelper(getActivity());
                GlicemiaDao dao = new GlicemiaDao(helper);
                List<Glicemia> glicemias = dao.getGlicemiasEntre(dataInicialPicker.getDataSelecionada(), dataFinalPicker.getDataSelecionada());
                helper.close();

                File file = new PlanilhaExcel().criaArquivo(glicemias);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(file),"application/vnd.ms-excel");
                startActivity(intent);
            }
        });

        return view;
    }
}
