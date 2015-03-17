package br.com.caelum.diabetes.dao;

import java.sql.SQLException;
import java.util.List;

import br.com.caelum.diabetes.exception.TratadorExcecao;
import br.com.caelum.diabetes.model.DadosMedicos;
import br.com.caelum.diabetes.model.Paciente;
import br.com.caelum.diabetes.model.TipoDadoMedico;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

public class DadosMedicosDao {
	private RuntimeExceptionDao<DadosMedicos, Integer> dao;
	private PacienteDao pacienteDao;
	private DbHelper helper;

	public DadosMedicosDao(DbHelper helper) {
		this.helper = helper;
		dao = helper.getSimpleDataDao(DadosMedicos.class);
		pacienteDao = new PacienteDao(helper);
	}
	
	public void salva(DadosMedicos dadosMedicos) {
		DadosMedicos dadosMedicosCom = getDadosMedicosCom(dadosMedicos.getTipo());
		if (dadosMedicosCom == null) {
			dao.create(dadosMedicos);
			adicionaDadosMedicosAoPaciente(dadosMedicos);
		} else {
			dadosMedicos.setId(dadosMedicosCom.getId());
			dao.update(dadosMedicos);
		}
	}

	private void adicionaDadosMedicosAoPaciente(DadosMedicos dadosMedicos) {
		Paciente paciente = pacienteDao.getPaciente();
		switch (dadosMedicos.getTipo()) {
			case CONTINUA:
				paciente.setInsulinaContinua(dadosMedicos);
				break;
			
			case CORRECAO:
				paciente.setInsulinaCorrecao(dadosMedicos);
				break;
				
			case GLICEMIA_ALVO:
				paciente.setGlicemiaAlvo(dadosMedicos);
				break;
		}
		
		pacienteDao.atualiza(paciente);
	}

	public void deletar(DadosMedicos dadosMedicos) {
		dao.delete(dadosMedicos);
	}

	public void atualiza(DadosMedicos dadosMedicos) {
		dao.update(dadosMedicos);
	}
	
	public List<DadosMedicos> getDadosMedicos() {
		QueryBuilder<DadosMedicos,Integer> builder = dao.queryBuilder();
		PreparedQuery<DadosMedicos> prepare = null;
		try {
			prepare = builder.prepare();
		} catch (SQLException e) {
			new TratadorExcecao(helper.context).trataSqlException(e);
		}
		return dao.query(prepare);
	}
	
	public DadosMedicos getDadosMedicosCom(TipoDadoMedico glicemiaAlvo) {
		QueryBuilder<DadosMedicos,Integer> builder = dao.queryBuilder();
		
		PreparedQuery<DadosMedicos> prepare = null;
		try {
			builder.where().eq("tipoDado", glicemiaAlvo);
			prepare = builder.prepare();
		} catch (SQLException e) {
			new TratadorExcecao(helper.context).trataSqlException(e);
		}
		
		List<DadosMedicos> list = dao.query(prepare);
		return list.size() == 0 ? null : list.get(0);
	}
}
