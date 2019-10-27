package br.com.diabetesmaisdoce.extras;


public enum TipoRefeicao{
	CAFE_DA_MANHA(1, Extras.CAFE_DA_MANHA),
	LANCHE_DA_MANHA(2, Extras.POS_CAFE),
	ALMOCO(3, Extras.ALMOCO),
	LANCHE_DA_TARDE(4, Extras.POS_ALMOCO),
	JANTAR(5, Extras.JANTAR),
	CEIA(6, Extras.POS_JANTAR),
	MADRUGADA(7, Extras.MADRUGADA);

	private String text;
	private int excelColumnIndex;

	private TipoRefeicao(int excelColumnIndex, String text) {
		this.text = text;
		this.excelColumnIndex = excelColumnIndex;
	}

	public String getText() {
		return this.text;
	}

    public int getExcelColumnIndex() {return this.excelColumnIndex; }

	public static TipoRefeicao fromString(String text) {
		if (text != null) {
			for (TipoRefeicao b : TipoRefeicao.values()) {
				if (text.equalsIgnoreCase(b.text)) return b;
			}
		}
		return null;
	}
}

