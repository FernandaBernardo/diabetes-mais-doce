package br.com.caelum.diabetes.calculos;

import java.util.Calendar;
import java.util.List;

import org.joda.time.DateTime;

import android.content.Context;
import br.com.caelum.diabetes.dao.DbHelper;
import br.com.caelum.diabetes.dao.GlicemiaDao;
import br.com.caelum.diabetes.model.Glicemia;

public class CalculaMediaGlicemia {
	List<Glicemia> glicemias;
	
	public CalculaMediaGlicemia(Context context) {
		DbHelper helper = new DbHelper(context);
		GlicemiaDao dao = new GlicemiaDao(helper);
		this.glicemias = dao.getGlicemias();
		helper.close();
	}
	
	public int getMediaDoDia() {
		Calendar data = Calendar.getInstance();
		int dia = data.get(Calendar.DAY_OF_YEAR);
		int ano = data.get(Calendar.YEAR);
		int contador = 0;
		int media = 0;
		for (Glicemia glicemia : glicemias) {
			if(glicemia.getData().get(Calendar.DAY_OF_YEAR) == dia && glicemia.getData().get(Calendar.YEAR) == ano) {
				media += glicemia.getValorGlicemia();
				contador++;
			}
		}
		if (contador==0) contador=1;
		media /= contador;
		return media;
	}
	
	public int getMediaDaSemana() {
		Calendar data = Calendar.getInstance();
		int semana = data.get(Calendar.WEEK_OF_YEAR);
		int ano = data.get(Calendar.YEAR);
		int contador = 0;
		int media = 0;
		for (Glicemia glicemia : glicemias) {
			if(glicemia.getData().get(Calendar.WEEK_OF_YEAR) == semana && glicemia.getData().get(Calendar.YEAR) == ano) {
				media += glicemia.getValorGlicemia();
				contador++;
			}
		}
		if (contador==0) contador=1;
		media /= contador;
		return media;
	}
	
	public int getMediaDoMes() {
		Calendar data = Calendar.getInstance();
		int mes = data.get(Calendar.MONTH);
		int ano = data.get(Calendar.YEAR);
		int contador = 0;
		int media = 0;
		for (Glicemia glicemia : glicemias) {
			if(glicemia.getData().get(Calendar.MONTH) == mes && glicemia.getData().get(Calendar.YEAR) == ano) {
				media += glicemia.getValorGlicemia();
				contador++;
			}
		}
		if (contador==0) contador=1;
		media /= contador;
		return media;
	}
}
