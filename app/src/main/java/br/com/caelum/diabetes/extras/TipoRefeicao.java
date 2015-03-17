package br.com.caelum.diabetes.extras;


public enum TipoRefeicao{
	CAFE_DA_MANHA(Extras.CAFE_DA_MANHA), 
	LANCHE_DA_MANHA(Extras.LANCHE_DA_MANHA),
	ALMOCO(Extras.ALMOCO),
	LANCHE_DA_TARDE(Extras.LANCHE_DA_TARDE),
	JANTAR(Extras.JANTAR),
	CEIA(Extras.CEIA);
	
	private String text;

	private TipoRefeicao(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}

	public static TipoRefeicao fromString(String text) {
		if (text != null) {
			for (TipoRefeicao b : TipoRefeicao.values()) {
				if (text.equalsIgnoreCase(b.text)) return b;
			}
		}
		return null;
	}
}

