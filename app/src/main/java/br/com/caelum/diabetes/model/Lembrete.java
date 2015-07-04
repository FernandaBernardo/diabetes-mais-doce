package br.com.caelum.diabetes.model;

import java.io.Serializable;
import java.util.Calendar;

import org.joda.time.DateTime;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

@SuppressWarnings("serial")
public class Lembrete implements Serializable {
	@DatabaseField(generatedId=true)
	int id;
    @DatabaseField(dataType = DataType.SERIALIZABLE)
	Calendar data;
	@DatabaseField
	String atividade;
	@DatabaseField
	String anotacoes;
	
	public Lembrete() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Calendar getData() {
		return data;
	}

	public void setData(Calendar data) {
		this.data = data;
	}

	public String getAtividade() {
		return atividade;
	}

	public void setAtividade(String atividade) {
		this.atividade = atividade;
	}

	public String getAnotacoes() {
		return anotacoes;
	}

	public void setAnotacoes(String anotacoes) {
		this.anotacoes = anotacoes;
	}

	@Override
	public String toString() {
		return data.get(Calendar.DAY_OF_MONTH) + "/" + data.get(Calendar.MONTH) + "/" + data.get(Calendar.YEAR) +
                " - " + data.get(Calendar.HOUR_OF_DAY) + ":" + data.get(Calendar.MINUTE) + " - " + atividade + " - "
				+ anotacoes;
	}
}
