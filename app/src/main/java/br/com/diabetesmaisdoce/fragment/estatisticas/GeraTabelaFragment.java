package br.com.diabetesmaisdoce.fragment.estatisticas;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import br.com.diabetesmaisdoce.BuildConfig;
import br.com.diabetesmaisdoce.R;
import br.com.diabetesmaisdoce.activity.MainActivity;
import br.com.diabetesmaisdoce.dao.DbHelper;
import br.com.diabetesmaisdoce.dao.GlicemiaDao;
import br.com.diabetesmaisdoce.extras.PickerDialog;
import br.com.diabetesmaisdoce.extras.PlanilhaExcel;
import br.com.diabetesmaisdoce.model.Glicemia;
import br.com.diabetesmaisdoce.util.RequestPermissionHandler;
import br.com.diabetesmaisdoce.util.RequestPermissionListener;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;

/**
 * Created by Fernanda Bernardo on 08/05/2016.
 */
@SuppressWarnings("SpellCheckingInspection")
public class GeraTabelaFragment extends Fragment {
    private RequestPermissionHandler mRequestPermissionHandler;

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setTitleHeader("Gerar Tabela");
        ((MainActivity) getActivity()).setBackArrowIcon();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRequestPermissionHandler = new RequestPermissionHandler();
        final View view = inflater.inflate(R.layout.gera_tabela, null);

        Button salvar = (Button) view.findViewById(R.id.gerar_tabela);
        TextView dataInicialText = (TextView) view.findViewById(R.id.data_inicial_tabela);
        final TextView dataFinalText= (TextView) view.findViewById(R.id.data_final_tabela);

        Calendar dataInicial = Calendar.getInstance();
        dataInicial.add(Calendar.MONTH, -1);

        final PickerDialog dataInicialPicker = new PickerDialog(getFragmentManager(), dataInicialText, dataInicial);
        final PickerDialog dataFinalPicker = new PickerDialog(getFragmentManager(), dataFinalText);
        final ProgressDialog progress = new ProgressDialog(getActivity());

        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.setTitle("Aguarde...");
                progress.setMessage("Gerando tabela...");
                progress.setCancelable(false);
                progress.show();

                mRequestPermissionHandler.requestPermission(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123, new RequestPermissionListener() {
                    @Override
                    public void onSuccess() {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                DbHelper helper = new DbHelper(getActivity());
                                GlicemiaDao dao = new GlicemiaDao(helper);
                                List<Glicemia> glicemias = dao.getGlicemiasEntre(dataInicialPicker.getDataSelecionada(), dataFinalPicker.getDataSelecionada());
                                helper.close();


                                File file = new PlanilhaExcel().criaArquivo(getContext(), glicemias);
                                Uri uri = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".provider", file);
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setFlags(FLAG_GRANT_READ_URI_PERMISSION | FLAG_GRANT_WRITE_URI_PERMISSION);
                                intent.setDataAndType(uri, "application/vnd.ms-excel");
                                startActivity(intent);
                                progress.dismiss();
                            }
                        }, 1000);
                    }
                    @Override
                    public void onFailed() {
                    }
                });
            }
        });

        return view;
    }
}
