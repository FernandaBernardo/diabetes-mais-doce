package br.com.diabetesmaisdoce.model;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;

import br.com.diabetesmaisdoce.extras.TipoRefeicao;

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
	@DatabaseField
	private Double madrugada;
	@DatabaseField (columnName="tipoDado")
	private TipoDadoMedico tipo;
	
	public DadosMedicos() {
	}
	
	public DadosMedicos(TipoDadoMedico tipo) {
		this.tipo = tipo;
	}

	public Double get(TipoRefeicao tipoRefeicao) {
		if(tipoRefeicao.equals(TipoRefeicao.CAFE_DA_MANHA)) {
			return cafeManha;
		} else if(tipoRefeicao.equals(TipoRefeicao.LANCHE_DA_MANHA)) {
			return lancheManha;
		} else if(tipoRefeicao.equals(TipoRefeicao.ALMOCO)) {
			return almoco;
		} else if(tipoRefeicao.equals(TipoRefeicao.LANCHE_DA_TARDE)) {
			return lancheTarde;
		} else if(tipoRefeicao.equals(TipoRefeicao.JANTAR)) {
			return jantar;
		} else if(tipoRefeicao.equals(TipoRefeicao.CEIA)) {
			return ceia;
		} else if(tipoRefeicao.equals(TipoRefeicao.MADRUGADA)) {
			return madrugada;
		}
		return null;
	}

	public void set(Double valor, TipoRefeicao tipoRefeicao) {
		if(tipoRefeicao.equals(TipoRefeicao.CAFE_DA_MANHA)) {
			this.cafeManha = valor;
		} else if(tipoRefeicao.equals(TipoRefeicao.LANCHE_DA_MANHA)) {
			this.lancheManha = valor;
		} else if(tipoRefeicao.equals(TipoRefeicao.ALMOCO)) {
			this.almoco = valor;
		} else if(tipoRefeicao.equals(TipoRefeicao.LANCHE_DA_TARDE)) {
			this.lancheTarde = valor;
		} else if(tipoRefeicao.equals(TipoRefeicao.JANTAR)) {
			this.jantar = valor;
		} else if(tipoRefeicao.equals(TipoRefeicao.CEIA)) {
			this.ceia = valor;
		} else if(tipoRefeicao.equals(TipoRefeicao.MADRUGADA)) {
			this.madrugada = valor;
		}
	}

	public TipoDadoMedico getTipo() {
		return tipo;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
}

