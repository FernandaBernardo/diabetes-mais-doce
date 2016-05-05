package br.com.caelum.diabetes.model;

import java.io.Serializable;
import java.util.Calendar;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

import br.com.caelum.diabetes.extras.ParserTools;

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
		return ParserTools.getParseDate(data)
				+ " - " + ParserTools.getParseHour(data)
				+ " - " + atividade + " - " + anotacoes;
	}
}
