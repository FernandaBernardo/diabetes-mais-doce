package br.com.caelum.diabetes.dao;

import java.sql.SQLException;
import java.util.List;

import br.com.caelum.diabetes.exception.TratadorExcecao;
import br.com.caelum.diabetes.model.DadosMedicos;
import br.com.caelum.diabetes.model.Paciente;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

public class PacienteDao {
	private RuntimeExceptionDao<Paciente, Integer> dao;
	private DbHelper helper;
	private RuntimeExceptionDao<DadosMedicos, Integer> dadosDao;

	public PacienteDao(DbHelper helper) {
		this.helper = helper;
		dao = helper.getSimpleDataDao(Paciente.class);
		dadosDao = helper.getSimpleDataDao(DadosMedicos.class);
	}
	
	public void salva(Paciente paciente) {
		dao.create(paciente);
	}

	public void deletar(Paciente paciente) {
		dao.delete(paciente);
	}

	public void atualiza(Paciente paciente) {
		dao.update(paciente);
	}
	
	public Paciente getPaciente() {
		QueryBuilder<Paciente,Integer> builder = dao.queryBuilder();
		QueryBuilder<DadosMedicos,Integer> builderDados = dadosDao.queryBuilder();

		PreparedQuery<Paciente> prepare = null;
		PreparedQuery<DadosMedicos> prepareDados = null;
		try {
			prepare = builder.prepare();
			prepareDados = builderDados.prepare();
		} catch (SQLException e) {
			new TratadorExcecao(helper.context).trataSqlException(e);
		}
		
		List<Paciente> list = dao.query(prepare);
		Paciente paciente = null;
		if (list.size() == 0)
			return null;
		else {
			paciente = list.get(0);
		}
		List<DadosMedicos> listDados = dadosDao.query(prepareDados);
		for (DadosMedicos dadosMedicos : listDados) {
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
		}
		return paciente;
	}
}