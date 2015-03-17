package br.com.caelum.diabetes.dao;

import java.sql.SQLException;
import java.util.List;

import br.com.caelum.diabetes.exception.TratadorExcecao;
import br.com.caelum.diabetes.model.Lembrete;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

public class LembreteDao {
	private RuntimeExceptionDao<Lembrete, Integer> dao;
	private final DbHelper helper;

	public LembreteDao(DbHelper helper) {
		this.helper = helper;
		this.dao = helper.getSimpleDataDao(Lembrete.class);
	}

	public void salva(Lembrete lembrete) {
		dao.create(lembrete);
	}

	public void deletar(Lembrete lembrete) {
		dao.delete(lembrete);
	}

	public void atualiza(Lembrete lembrete) {
		dao.update(lembrete);
	}

	public List<Lembrete> getLembretes() {
		QueryBuilder<Lembrete,Integer> builder = dao.queryBuilder();
		
		PreparedQuery<Lembrete> prepare = null;
		try {
			prepare = builder.prepare();
		} catch (SQLException e) {
			new TratadorExcecao(helper.context).trataSqlException(e);
		}
		
		List<Lembrete> lembretes = dao.query(prepare);
		return lembretes;
	}

	public List<Lembrete> getLembretes(int limit) {
		List<Lembrete> lembretes = getLembretes();
		if (lembretes.size() < limit) return lembretes;
		else return lembretes.subList(0, limit);
	}
}
