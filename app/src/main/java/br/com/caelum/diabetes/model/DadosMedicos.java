package br.com.caelum.diabetes.model;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;

@SuppressWarnings("serial")
public class DadosMedicos implements Serializable {
	@DatabaseField(generatedId = true)
	private Integer id;
	@DatabaseField
	private Double cafeManha;
	@DatabaseField
	private Double lancheManha;
	@DatabaseField
	private Double almoco;
	@DatabaseField
	private Double lancheTarde;
	@DatabaseField
	private Double jantar;
	@DatabaseField
	private Double ceia;
	@DatabaseField (columnName="tipoDado")
	private TipoDadoMedico tipo;
	
	public DadosMedicos() {
	}
	
	public DadosMedicos(TipoDadoMedico tipo) {
		this.tipo = tipo;
	}
		
	public Double getCafeManha() {
		return cafeManha;
	}
	public void setCafeManha(Double cafeManha) {
		this.cafeManha = cafeManha;
	}
	public Double getLancheManha() {
		return lancheManha;
	}
	public void setLancheManha(Double lancheManha) {
		this.lancheManha = lancheManha;
	}
	public Double getAlmoco() {
		return almoco;
	}
	public void setAlmoco(Double almoco) {
		this.almoco = almoco;
	}
	public Double getLancheTarde() {
		return lancheTarde;
	}
	public void setLancheTarde(Double lancheTarde) {
		this.lancheTarde = lancheTarde;
	}
	public Double getJantar() {
		return jantar;
	}
	public void setJantar(Double jantar) {
		this.jantar = jantar;
	}
	public Double getCeia() {
		return ceia;
	}
	public void setCeia(Double ceia) {
		this.ceia = ceia;
	}
	public TipoDadoMedico getTipo() {
		return tipo;
	}
	public void setTipo(TipoDadoMedico tipo) {
		this.tipo = tipo;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public boolean isEmpty(){
		
		if (this.tipo == TipoDadoMedico.GLICEMIA_ALVO){
			return this.cafeManha == null || this.almoco == null || this.jantar == null;
		}
		
		return this.almoco == null || this.cafeManha == null
				|| this.ceia == null || this.jantar == null
				|| this.lancheManha == null || this.lancheTarde == null;
	}
}

