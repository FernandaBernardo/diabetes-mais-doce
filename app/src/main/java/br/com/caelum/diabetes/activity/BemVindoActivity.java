package br.com.caelum.diabetes.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import br.com.caelum.diabetes.R;
import br.com.caelum.diabetes.dao.AlimentoFisicoDao;
import br.com.caelum.diabetes.dao.DbHelper;
import br.com.caelum.diabetes.dao.PacienteDao;
import br.com.caelum.diabetes.model.Paciente;

public class BemVindoActivity extends Activity {
	
	private Paciente pacienteBanco;
	private PacienteDao dao;
    private DbHelper helper;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
		setContentView(R.layout.bem_vindo);
		
		helper = new DbHelper(BemVindoActivity.this);
		dao = new PacienteDao(helper);
		pacienteBanco = dao.getPaciente();
		
		if(pacienteBanco != null) {
			Intent intent = new Intent(BemVindoActivity.this, MainActivity.class);
			intent.putExtra("paciente", pacienteBanco);
			startActivity(intent);
		}
		
		Button botao = (Button) findViewById(R.id.botao_proximo);
		botao.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				EditText nomePessoa = (EditText) findViewById(R.id.nome_pessoa);
				Paciente paciente = new Paciente();
				paciente.setNome(nomePessoa.getText().toString());
				dao.salva(paciente);
				Intent intent = new Intent(BemVindoActivity.this, MainActivity.class);
				intent.putExtra("paciente", paciente);
				startActivity(intent);
			}
		});

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                populaBanco(R.raw.insert);
            }
        });

        thread.run();
    }
	
	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}

    public void populaBanco(int arquivo){
        try {
            InputStream insert = getResources().openRawResource(arquivo);
            DataInputStream inputStream = new DataInputStream(insert);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            AlimentoFisicoDao alimentoFisicoDao = new AlimentoFisicoDao(helper);
            while ((line = bufferedReader.readLine()) != null) {
                alimentoFisicoDao.executaInsert(line);
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
