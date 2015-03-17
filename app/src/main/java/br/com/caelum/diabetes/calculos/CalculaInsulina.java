package br.com.caelum.diabetes.calculos;

import br.com.caelum.diabetes.extras.TipoRefeicao;
import br.com.caelum.diabetes.model.DadosMedicos;
import br.com.caelum.diabetes.model.Paciente;
import br.com.caelum.diabetes.model.Refeicao;

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
		double valorCorrecao = 1;
		
		if(tipoRefeicao == TipoRefeicao.CAFE_DA_MANHA) {
			valorCorrecao = correcao.getCafeManha();
		} else if (tipoRefeicao == TipoRefeicao.LANCHE_DA_MANHA) {
			valorCorrecao = correcao.getLancheManha();
		} else if (tipoRefeicao == TipoRefeicao.ALMOCO) {
			valorCorrecao = correcao.getAlmoco();
		} else if (tipoRefeicao == TipoRefeicao.LANCHE_DA_TARDE) {
			valorCorrecao = correcao.getLancheTarde();
		} else if (tipoRefeicao == TipoRefeicao.JANTAR) {
			valorCorrecao = correcao.getJantar();
		} else if (tipoRefeicao == TipoRefeicao.CEIA) {
			valorCorrecao = correcao.getCeia();
		}
		
		double totalInsulina = totalCHO / valorCorrecao;
		
		return totalInsulina;
	}
}
