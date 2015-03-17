package br.com.caelum.diabetes.extras;

import java.util.ArrayList;


public enum UnidadeMedidaAlimento{
	COLHER_DE_SOPA(Extras.COLHER_DE_SOPA), 
	COLHER_DE_CHA(Extras.COLHER_DE_CHA),
	CONCHA_MEDIA(Extras.CONCHA_MEDIA),
	FATIA_MEDIA(Extras.FATIA_MEDIA),
	UNIDADE_MEDIA(Extras.UNIDADE_MEDIA),
	UNIDADE_PEQUENA(Extras.UNIDADE_PEQUENA),
	ESCUMADEIRA(Extras.ESCUMADEIRA),
	XICARA(Extras.XICARA),
	COPO(Extras.COPO);
	
	private String text;

	private UnidadeMedidaAlimento(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}

	public static UnidadeMedidaAlimento fromString(String text) {
		if (text != null) {
			for (UnidadeMedidaAlimento b : UnidadeMedidaAlimento.values()) {
				if (text.equalsIgnoreCase(b.text)) return b;
			}
		}
		return null;
	}
	
	public static ArrayList<String> getAll() {
		ArrayList<String> lista = new ArrayList<String>();
		lista.add(COLHER_DE_SOPA.getText());
		lista.add(COLHER_DE_CHA.getText());
		lista.add(CONCHA_MEDIA.getText());
		lista.add(FATIA_MEDIA.getText());
		lista.add(UNIDADE_MEDIA.getText());
		lista.add(UNIDADE_PEQUENA.getText());
		lista.add(ESCUMADEIRA.getText());
		lista.add(XICARA.getText());
		lista.add(COPO.getText());
		
		return lista;
	}
}

