package br.com.caelum.diabetes.dao;

import java.sql.SQLException;
import java.util.List;

import br.com.caelum.diabetes.exception.TratadorExcecao;
import br.com.caelum.diabetes.model.Glicemia;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

public class GlicemiaDao {
	private RuntimeExceptionDao<Glicemia, Integer> dao;

	private DbHelper helper;

	public GlicemiaDao(DbHelper helper) {
		this.helper = helper;
		this.dao = helper.getSimpleDataDao(Glicemia.class);
	}
	
	public void salva(Glicemia glicemia) {
		dao.create(glicemia);
	}

	public void deletar(Glicemia glicemia) {
		dao.delete(glicemia);
	}

	public void atualiza(Glicemia glicemia) {
		dao.update(glicemia);
	}
	
	public List<Glicemia> getGlicemias() {
		QueryBuilder<Glicemia,Integer> builder = dao.queryBuilder();
		
		PreparedQuery<Glicemia> prepare = null;
		try {
			prepare = builder.prepare();
		} catch (SQLException e) {
			new TratadorExcecao(helper.context).trataSqlException(e);
		}
		
		List<Glicemia> glicemias = dao.query(prepare);
		return glicemias;
	}
}
