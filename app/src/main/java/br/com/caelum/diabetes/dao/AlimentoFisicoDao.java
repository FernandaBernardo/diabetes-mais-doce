package br.com.caelum.diabetes.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import br.com.caelum.diabetes.exception.TratadorExcecao;
import br.com.caelum.diabetes.model.AlimentoFisico;
import br.com.caelum.diabetes.model.AlimentoVirtual;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.TableUtils;

public class AlimentoFisicoDao{

	private DbHelper helper;
	private RuntimeExceptionDao<AlimentoFisico, Integer> dao;
    private String arquivo = "insert.sql";

	public AlimentoFisicoDao(DbHelper helper) {
		this.helper = helper;
		dao = helper.getSimpleDataDao(AlimentoFisico.class);
	}
	
	public void salva(AlimentoFisico alimento) {
		dao.create(alimento);
	}
	
	public void deletar(AlimentoFisico alimento) {
		dao.delete(alimento);
	}

	public void atualiza(AlimentoFisico alimento) {
		dao.update(alimento);
	}
	
	public List<AlimentoFisico> getAlimentos() {
		QueryBuilder<AlimentoFisico, Integer> builder = dao.queryBuilder();
		
		PreparedQuery<AlimentoFisico> prepare = null;
		try {
			prepare = builder.prepare();
		} catch (SQLException e) {
			new TratadorExcecao(helper.context).trataSqlException(e);
		}
		
		return dao.query(prepare);
	}

	public AlimentoFisico getAlimentoFisicoDoVirtual(AlimentoVirtual alimentoVirtual) {
		QueryBuilder<AlimentoFisico, Integer> builderAlimento = dao.queryBuilder();
		PreparedQuery<AlimentoFisico> prepare = null;
		try {
			builderAlimento.where().eq("id", alimentoVirtual.getAlimento().getId());
			prepare = builderAlimento.prepare();
		} catch (SQLException e) {
			new TratadorExcecao(helper.context).trataSqlException(e);
		}
		
		return dao.queryForFirst(prepare);
	}

    public void executaInsert(String insert) {
        dao.executeRaw(insert);
    }

    public void importarAlimentos(final ArrayList<String> inserts) {
        try {
            TransactionManager.callInTransaction(helper.connectionSource, new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    for(String insert: inserts) {
                        executaInsert(insert);
                    }
                    return null;
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}