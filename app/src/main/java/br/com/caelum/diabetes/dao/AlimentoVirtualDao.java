package br.com.caelum.diabetes.dao;

import java.sql.SQLException;
import java.util.List;

import br.com.caelum.diabetes.exception.TratadorExcecao;
import br.com.caelum.diabetes.model.AlimentoVirtual;
import br.com.caelum.diabetes.model.Refeicao;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

public class AlimentoVirtualDao{

	private RuntimeExceptionDao<AlimentoVirtual, Integer> dao;
	private DbHelper helper;

	public AlimentoVirtualDao(DbHelper helper) {
		this.helper = helper;
		dao = helper.getSimpleDataDao(AlimentoVirtual.class);
	}
	
	public void salva(AlimentoVirtual alimento) {
		dao.create(alimento);
	}

	public void deletar(AlimentoVirtual alimento) {
		dao.delete(alimento);
	}

	public void atualiza(AlimentoVirtual alimento) {
		dao.update(alimento);
	}

	public List<AlimentoVirtual> getAlimentosDaRefeicao(Refeicao refeicao) {
		QueryBuilder<AlimentoVirtual, Integer> builderAlimento = dao.queryBuilder();
		PreparedQuery<AlimentoVirtual> prepare = null;
		try {
			builderAlimento.where().eq("refeicao_id", refeicao.getId());
			prepare = builderAlimento.prepare();
		} catch (SQLException e) {
			new TratadorExcecao(helper.context).trataSqlException(e);
		}
		
		List<AlimentoVirtual> alimentosVirtuais = dao.query(prepare);
		AlimentoFisicoDao alimentoFisicoDao = new AlimentoFisicoDao(helper);
		
		for (AlimentoVirtual alimentoVirtual : alimentosVirtuais) {
			alimentoVirtual.setAlimento(alimentoFisicoDao.getAlimentoFisicoDoVirtual(alimentoVirtual));
		}
		
		return alimentosVirtuais;
	}
}