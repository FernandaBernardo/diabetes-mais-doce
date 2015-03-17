package br.com.caelum.diabetes.model;

import java.io.Serializable;

import org.joda.time.DateTime;

import com.j256.ormlite.field.DatabaseField;

@SuppressWarnings("serial")
public class Lembrete implements Serializable {
	@DatabaseField(generatedId=true)
	int id;
	@DatabaseField
	DateTime data;
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

	public DateTime getData() {
		return data;
	}

	public void setData(DateTime data) {
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
		return data.getDayOfMonth() + "/" + data.getMonthOfYear() + "/"
				+ data.getYear() + " - " + data.getHourOfDay() + ":"
				+ data.getMinuteOfHour() + " - " + atividade + " - "
				+ anotacoes;
	}
}
