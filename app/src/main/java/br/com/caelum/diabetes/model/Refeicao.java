package br.com.caelum.diabetes.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import br.com.caelum.diabetes.extras.ParserTools;
import br.com.caelum.diabetes.extras.TipoRefeicao;

@SuppressWarnings("serial")
public class Refeicao implements Serializable{
	@DatabaseField(generatedId = true)
	private int id;
	@ForeignCollectionField
	private Collection<AlimentoVirtual> alimentos;
	@DatabaseField
	private TipoRefeicao tipoRefeicao;
	@DatabaseField(dataType = DataType.SERIALIZABLE)
	private Calendar data;
	
	public Refeicao() {
		this.alimentos = new ArrayList<AlimentoVirtual>();
		this.data = Calendar.getInstance();
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

	public Calendar getData() {
		return data;
	}

	public void setData(Calendar data) {
        this.data = data;
	}
	
	@Override
	public String toString() {
		return ParserTools.getParseDate(data)
				+ " - " + ParserTools.getParseHour(data) + " - " + tipoRefeicao;
	}
}