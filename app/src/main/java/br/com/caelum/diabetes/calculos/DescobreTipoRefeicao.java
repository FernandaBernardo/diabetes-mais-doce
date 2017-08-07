package br.com.caelum.diabetes.calculos;

import org.joda.time.DateTime;

import br.com.caelum.diabetes.extras.TipoRefeicao;

public class DescobreTipoRefeicao {
	TipoRefeicao tipoRefeicao;
	public DescobreTipoRefeicao() {
		DateTime dataAtual = new DateTime();
		int hourOfDay = dataAtual.getHourOfDay();
		if (hourOfDay >= 5 && hourOfDay < 10) tipoRefeicao = TipoRefeicao.CAFE_DA_MANHA;
		if (hourOfDay >= 10 && hourOfDay < 12) tipoRefeicao = TipoRefeicao.LANCHE_DA_MANHA;
		if (hourOfDay >= 12 && hourOfDay < 15) tipoRefeicao = TipoRefeicao.ALMOCO;
		if (hourOfDay >= 15 && hourOfDay < 18) tipoRefeicao = TipoRefeicao.LANCHE_DA_TARDE;
		if (hourOfDay >= 18 && hourOfDay < 24) tipoRefeicao = TipoRefeicao.JANTAR;
		if (hourOfDay >= 24 || hourOfDay < 1) tipoRefeicao = TipoRefeicao.CEIA;
		if (hourOfDay >= 1 || hourOfDay < 5) tipoRefeicao = TipoRefeicao.MADRUGADA;
	}
	
	public TipoRefeicao getTipoRefeicao() {
		return tipoRefeicao;
	}
}
