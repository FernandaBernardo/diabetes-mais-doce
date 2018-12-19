package br.com.diabetesmaisdoce.model;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;
import java.util.Calendar;

import br.com.diabetesmaisdoce.extras.ParserTools;
import br.com.diabetesmaisdoce.extras.CalendarTypePersister;
import br.com.diabetesmaisdoce.extras.TipoRefeicao;

@SuppressWarnings("serial")
public class Glicemia implements Serializable, Comparable<Glicemia> {
	@DatabaseField(generatedId=true)
	private int id;
    @DatabaseField(persisterClass = CalendarTypePersister.class)
	private Calendar data;
	@DatabaseField
	private TipoRefeicao tipoRefeicao;
	@DatabaseField
	private int valorGlicemia;
	@DatabaseField
	private String observacao;

	public Glicemia() {
		this.data = Calendar.getInstance();
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
	public String getObservacao() {	return observacao; }
	public void setObservacao(String observacao) { this.observacao = observacao; }

	@Override
	public String toString() {
		return ParserTools.getParseDate(data.get(Calendar.DAY_OF_MONTH), data.get(Calendar.MONTH), data.get(Calendar.YEAR)) +
                " - " + ParserTools.getParseHour(data.get(Calendar.HOUR_OF_DAY), data.get(Calendar.MINUTE))
				+ " - " + tipoRefeicao + " - " + valorGlicemia + " - " + observacao;
	}

	@Override
	public int compareTo(Glicemia another) {
		if(this.getData().getTimeInMillis() > another.getData().getTimeInMillis()) {
			return -1;
		}
		if(this.getData().getTimeInMillis() < another.getData().getTimeInMillis()) {
			return 1;
		}

		return 0;
	}
}
