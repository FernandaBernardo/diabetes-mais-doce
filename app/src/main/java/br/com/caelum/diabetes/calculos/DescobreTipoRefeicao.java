package br.com.caelum.diabetes.calculos;

import org.joda.time.DateTime;

import br.com.caelum.diabetes.extras.TipoRefeicao;

public class DescobreTipoRefeicao {
	TipoRefeicao tipoRefeicao;
	public DescobreTipoRefeicao() {
		DateTime dataAtual = new DateTime();
		if (dataAtual.getHourOfDay() >= 4 && dataAtual.getHourOfDay() < 10) tipoRefeicao = TipoRefeicao.CAFE_DA_MANHA;
		if (dataAtual.getHourOfDay() >= 10 && dataAtual.getHourOfDay() < 12) tipoRefeicao = TipoRefeicao.LANCHE_DA_MANHA;
		if (dataAtual.getHourOfDay() >= 12 && dataAtual.getHourOfDay() < 15) tipoRefeicao = TipoRefeicao.ALMOCO;
		if (dataAtual.getHourOfDay() >= 15 && dataAtual.getHourOfDay() < 18) tipoRefeicao = TipoRefeicao.LANCHE_DA_TARDE;
		if (dataAtual.getHourOfDay() >= 18 && dataAtual.getHourOfDay() < 24) tipoRefeicao = TipoRefeicao.JANTAR;
		if (dataAtual.getHourOfDay() >= 24 || dataAtual.getHourOfDay() < 4) tipoRefeicao = TipoRefeicao.CEIA;
	}
	
	public TipoRefeicao getTipoRefeicao() {
		return tipoRefeicao;
	}
}
