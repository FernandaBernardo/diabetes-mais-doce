package br.com.caelum.diabetes.model;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;

@SuppressWarnings("serial")
public class AlimentoFisico implements Serializable{
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField
	private String nome;
	@DatabaseField
	private double carboidrato;
	@DatabaseField
	private String unidadeDeMedida;
	
	public AlimentoFisico() {
	}
	
	public AlimentoFisico(String nome, double carboidrato, String unidadeDeMedida) {
		this.nome = nome;
		this.carboidrato = carboidrato;
		this.unidadeDeMedida = unidadeDeMedida;
	}
	
	public double getCarboidratoPorValor(double valor) {
		return valor*carboidrato;
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public double getCarboidrato() {
		return carboidrato;
	}
	public void setCarboidrato(double carboidrato) {
		this.carboidrato = carboidrato;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return nome;
	}

	public String getUnidadeDeMedida() {
		return unidadeDeMedida;
	}

	public void setUnidadeDeMedida(String unidadeDeMedida) {
		this.unidadeDeMedida = unidadeDeMedida;
	}
}
