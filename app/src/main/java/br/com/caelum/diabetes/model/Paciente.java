package br.com.caelum.diabetes.model;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;

@SuppressWarnings("serial")
public class Paciente implements Serializable{
	
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField(canBeNull= false)
	private String nome;
	@DatabaseField
	private Integer idade;
	@DatabaseField
	private Double peso;
	@DatabaseField
	private Double altura;
	@DatabaseField
	private String sexo;
	@DatabaseField
	private String tipoDiabetes;
	
	@DatabaseField(foreign=true)
	private DadosMedicos insulinaContinua;
	@DatabaseField(foreign=true)
	private DadosMedicos insulinaCorrecao;
	@DatabaseField(foreign=true)
	private DadosMedicos glicemiaAlvo;
	@DatabaseField(foreign=true)
	private DadosMedicos fatorCorrecao;

	public Paciente() {
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Integer getIdade() {
		if(idade == null) return 0;
		return idade;
	}
	public void setIdade(Integer idade) {
		this.idade = idade;
	}
	public Double getPeso() {
		if(peso == null) return 0.0;
		return peso;
	}
	public void setPeso(Double peso) {
		this.peso = peso;
	}
	public Double getAltura() {
        if(altura== null) return 0.0;
        return altura;
	}
	public void setAltura(Double altura) {
		this.altura = altura;
	}
	public String getTipoDiabetes() {
		return tipoDiabetes;
	}
	public void setTipoDiabetes(String tipoDiabetes) {
		this.tipoDiabetes = tipoDiabetes;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	public DadosMedicos getInsulinaContinua() {
		return insulinaContinua;
	}
	public void setInsulinaContinua(DadosMedicos insulinaContinua) {
		this.insulinaContinua = insulinaContinua;
	}
	public DadosMedicos getInsulinaCorrecao() {
		return insulinaCorrecao;
	}
	public void setInsulinaCorrecao(DadosMedicos insulinaCorrecao) {
		this.insulinaCorrecao = insulinaCorrecao;
	}
	public DadosMedicos getGlicemiaAlvo() {
		return glicemiaAlvo;
	}
	public void setGlicemiaAlvo(DadosMedicos glicemiaAlvo) {
		this.glicemiaAlvo = glicemiaAlvo;
	}
	
	public boolean temValorCorrecao(){
		return (!(this.getInsulinaCorrecao() == null));
	}

	public DadosMedicos getFatorCorrecao() {
		return fatorCorrecao;
	}

	public void setFatorCorrecao(DadosMedicos fatorCorrecao) {
		this.fatorCorrecao = fatorCorrecao;
	}
}