package br.com.caelum.diabetes.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.joda.time.DateTime;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

import br.com.caelum.diabetes.extras.TipoRefeicao;

@SuppressWarnings("serial")
public class Refeicao implements Serializable{
	@DatabaseField(generatedId = true)
	private int id;
	@ForeignCollectionField
	private Collection<AlimentoVirtual> alimentos;
	@DatabaseField
	private TipoRefeicao tipoRefeicao;
	@DatabaseField
	private DateTime data;
	
	public Refeicao() {
		this.alimentos = new ArrayList<AlimentoVirtual>();
		this.data = new DateTime();
	}
	
	public void adicionaAlimento(AlimentoVirtual alimento) {
		alimentos.add(alimento);
	}
	
	public List<AlimentoVirtual> getAlimentos() {
		return (List<AlimentoVirtual>) alimentos;
	}

	public void setAlimentos(List<AlimentoVirtual> alimentos) {
		this.alimentos = alimentos;
	}

	public double getTotalCHO() {
		double totalCHO = 0;
		for (AlimentoVirtual alimento : alimentos) {
			totalCHO += alimento.getTotalCarboidrato();
		}
		return totalCHO;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public TipoRefeicao getTipoRefeicao() {
		return tipoRefeicao;
	}

	public void setTipoRefeicao(TipoRefeicao tipoRefeicao) {
		this.tipoRefeicao = tipoRefeicao;
	}

	public DateTime getData() {
		return data;
	}

	public void setData(DateTime data) {
		this.data = data;
	}
	
	@Override
	public String toString() {
		return data.getDayOfMonth() + "/" + data.getMonthOfYear() + "/" + data.getYear() + 
				" - " + data.getHourOfDay() + ":" + data.getMinuteOfHour() + " - " + 
				tipoRefeicao; 
	}
}