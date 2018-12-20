package br.com.diabetesmaisdoce.dao;

import android.content.res.Resources;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import br.com.diabetesmaisdoce.R;

/**
 * Created by Fê on 25/03/2015.
 */
public class PopulaAlimento extends AsyncTask<String,Void,Boolean> {

    private final AlimentoFisicoDao alimentoFisicoDao;
    private final ArrayList<String> inserts;
    private final Resources resources;
    private BufferedReader bufferedReader;

    public PopulaAlimento(DbHelper helper, Resources resources) {
        this.resources = resources;
        this.alimentoFisicoDao = new AlimentoFisicoDao(helper);
        this.inserts = new ArrayList<String>();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        InputStream insert = resources.openRawResource(R.raw.insert);
        DataInputStream inputStream = new DataInputStream(insert);
        this.bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        boolean success = false;
        try {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                inserts.add(line);
            }
            bufferedReader.close();
            alimentoFisicoDao.importarAlimentos(inserts);
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
    }
}
