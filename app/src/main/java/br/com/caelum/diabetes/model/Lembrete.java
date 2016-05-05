package br.com.caelum.diabetes.model;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;
import java.util.Calendar;

import br.com.caelum.diabetes.extras.ParserTools;
import br.com.caelum.diabetes.extras.CalendarTypePersister;

@SuppressWarnings("serial")
public class Lembrete implements Serializable {
	@DatabaseField(generatedId=true)
	int id;
    @DatabaseField(persisterClass = CalendarTypePersister.class)
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
		return ParserTools.getParseDate(data)
				+ " - " + ParserTools.getParseHour(data)
				+ " - " + atividade + " - " + anotacoes;
	}
}
