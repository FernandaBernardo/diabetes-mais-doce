package br.com.diabetesmaisdoce.calculos;

import br.com.diabetesmaisdoce.extras.TipoRefeicao;
import br.com.diabetesmaisdoce.model.DadosMedicos;
import br.com.diabetesmaisdoce.model.Glicemia;
import br.com.diabetesmaisdoce.model.Paciente;
import br.com.diabetesmaisdoce.model.Refeicao;

public class CalculaInsulina {
	Paciente paciente;
	private Refeicao refeicao;
	
	public CalculaInsulina(Refeicao refeicao, Paciente paciente) {
		this.refeicao = refeicao;
		this.paciente = paciente;
	}
	
	public double getTotalInsulina() {
		double totalCHO = refeicao.getTotalCHO();
		DadosMedicos correcao = paciente.getInsulinaCorrecao();
		TipoRefeicao tipoRefeicao = refeicao.getTipoRefeicao();
		double valorCorrecao = correcao.get(tipoRefeicao);

		double totalInsulina = totalCHO / valorCorrecao;
		
		return totalInsulina;
	}

	public static double getTotalInsulinaFatorCorrecao(Paciente paciente, Glicemia glicemia) {
		DadosMedicos fatorCorrecao = paciente.getFatorCorrecao();
		DadosMedicos glicemiaAlvoDadosMedicos = paciente.getGlicemiaAlvo();
		TipoRefeicao tipoRefeicao = glicemia.getTipoRefeicao();
		double valorFatorCorrecao = fatorCorrecao.get(tipoRefeicao);
		double glicemiaAlvo = glicemiaAlvoDadosMedicos.get(tipoRefeicao);

		double totalInsulina = (glicemia.getValorGlicemia() - glicemiaAlvo) / valorFatorCorrecao;

		return totalInsulina < 0 ? 0 : totalInsulina;
	}
}
