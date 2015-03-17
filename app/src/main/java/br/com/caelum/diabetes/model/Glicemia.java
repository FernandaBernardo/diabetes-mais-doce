package br.com.caelum.diabetes.model;

import java.io.Serializable;

import org.joda.time.DateTime;

import com.j256.ormlite.field.DatabaseField;

import br.com.caelum.diabetes.extras.TipoRefeicao;

@SuppressWarnings("serial")
public class Glicemia implements Serializable{
	@DatabaseField(generatedId=true)
	private int id;
	@DatabaseField
	private DateTime data;
	@DatabaseField
	private TipoRefeicao tipoRefeicao;
	@DatabaseField
	private int valorGlicemia;
	
	public Glicemia() {
		this.data = new DateTime();
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
	public TipoRefeicao getTipoRefeicao() {
		return tipoRefeicao;
	}
	public void setTipoRefeicao(TipoRefeicao tipoRefeicao) {
		this.tipoRefeicao = tipoRefeicao;
	}
	public int getValorGlicemia() {
		return valorGlicemia;
	}
	public void setValorGlicemia(int valorGlicemia) {
		this.valorGlicemia = valorGlicemia;
	}
	
	@Override
	public String toString() {
		return data.getDayOfMonth() + "/" + data.getMonthOfYear() + "/" + data.getYear() + 
				" - " + data.getHourOfDay() + ":" + data.getMinuteOfHour() + " - " + 
				tipoRefeicao + " - " + valorGlicemia; 
	}
}
