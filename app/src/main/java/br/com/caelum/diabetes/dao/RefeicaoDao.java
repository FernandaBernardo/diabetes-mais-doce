package br.com.caelum.diabetes.dao;

import java.sql.SQLException;
import java.util.List;

import br.com.caelum.diabetes.exception.TratadorExcecao;
import br.com.caelum.diabetes.model.AlimentoVirtual;
import br.com.caelum.diabetes.model.Refeicao;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

public class RefeicaoDao{
	private RuntimeExceptionDao<Refeicao, Integer> dao;
	private DbHelper helper;
	private AlimentoVirtualDao alimentoVirtualDao;

	public RefeicaoDao(DbHelper helper) {
		this.helper = helper;
		dao = helper.getSimpleDataDao(Refeicao.class);
		alimentoVirtualDao = new AlimentoVirtualDao(helper);
	}
	
	public void salva(Refeicao refeicao) {
		dao.create(refeicao);
		List<AlimentoVirtual> alimentos = refeicao.getAlimentos();
		for (AlimentoVirtual alimentoVirtual : alimentos) {
			alimentoVirtualDao.salva(alimentoVirtual);
		}
	}

	public void deletar(Refeicao refeicao) {
		dao.delete(refeicao);
		for (AlimentoVirtual alimentoVirtual : refeicao.getAlimentos()) {
			alimentoVirtualDao.deletar(alimentoVirtual);
		}
	}

	public void atualiza(Refeicao refeicao) {
		dao.update(refeicao);
	}
	
	public List<Refeicao> getRefeicoes() {
		QueryBuilder<Refeicao, Integer> builder = dao.queryBuilder();
		
		PreparedQuery<Refeicao> prepare = null;
		try {
			prepare = builder.prepare();
		} catch (SQLException e) {
			new TratadorExcecao(helper.context).trataSqlException(e);
		}
		
		List<Refeicao> refeicoes = dao.query(prepare);
		
		for (Refeicao refeicao : refeicoes) {
			refeicao.setAlimentos(alimentoVirtualDao.getAlimentosDaRefeicao(refeicao));
		}
		
		return refeicoes;
	}
}