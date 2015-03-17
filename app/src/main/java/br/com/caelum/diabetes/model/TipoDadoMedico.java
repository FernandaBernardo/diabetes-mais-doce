package br.com.caelum.diabetes.model;

import java.io.Serializable;

import br.com.caelum.diabetes.extras.Extras;

public enum TipoDadoMedico implements Serializable {
	CONTINUA(Extras.CONTINUO), 
	CORRECAO(Extras.CORRECAO),
	GLICEMIA_ALVO(Extras.GLICEMIA_ALVO);
	
	private String text;

	private TipoDadoMedico(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}

	public static TipoDadoMedico fromString(String text) {
		if (text != null) {
			for (TipoDadoMedico b : TipoDadoMedico.values()) {
				if (text.equalsIgnoreCase(b.text)) return b;
			}
		}
		return null;
	}

}
