package br.com.caelum.diabetes.dao;

import android.content.res.Resources;
import android.os.AsyncTask;

import com.j256.ormlite.misc.TransactionManager;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import br.com.caelum.diabetes.R;

/**
 * Created by Fê on 25/03/2015.
 */
public class PopulaAlimento extends AsyncTask<String,Void,Boolean> {

    private final DbHelper helper;
    private final Resources resources;

    public PopulaAlimento(DbHelper helper, Resources resources) {
        this.helper = helper;
        this.resources = resources;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        int arquivo = R.raw.insert;
        boolean success = false;
        try {

            InputStream insert = resources.openRawResource(arquivo);
            DataInputStream inputStream = new DataInputStream(insert);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            AlimentoFisicoDao alimentoFisicoDao = new AlimentoFisicoDao(helper);
            ArrayList<String> inserts = new ArrayList<String>();
            while ((line = bufferedReader.readLine()) != null) {
                inserts.add(line);
            }
            inputStream.close();
            alimentoFisicoDao.importData(inserts);
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
