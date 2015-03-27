package br.com.caelum.diabetes.dao;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import br.com.caelum.diabetes.model.AlimentoFisico;
import br.com.caelum.diabetes.model.AlimentoVirtual;
import br.com.caelum.diabetes.model.DadosMedicos;
import br.com.caelum.diabetes.model.Glicemia;
import br.com.caelum.diabetes.model.Lembrete;
import br.com.caelum.diabetes.model.Paciente;
import br.com.caelum.diabetes.model.Refeicao;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.j256.ormlite.table.TableUtils;

public class DbHelper extends OrmLiteSqliteOpenHelper{

	private static final String DATABASE = "Diabetes";
	
	AlimentoFisicoDao alimentoFisicoDao;

	public Context context;
    public ConnectionSource connectionSource;

	public DbHelper(Context context) {
		super(context, DATABASE, null, 1);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        this.connectionSource = connectionSource;
		try {
			TableUtils.createTable(connectionSource, Paciente.class);
			TableUtils.createTable(connectionSource, DadosMedicos.class);
			TableUtils.createTable(connectionSource, Refeicao.class);
			TableUtils.createTable(connectionSource, AlimentoVirtual.class);
			TableUtils.createTable(connectionSource, AlimentoFisico.class);
			TableUtils.createTable(connectionSource, Lembrete.class);
			TableUtils.createTable(connectionSource, Glicemia.class);

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        this.connectionSource = connectionSource;
		try {
			TableUtils.dropTable(connectionSource, Paciente.class, true);
			onCreate(db, connectionSource);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public <T> RuntimeExceptionDao<T, Integer> getSimpleDataDao(Class<T> clazz) {
		return getRuntimeExceptionDao(clazz);
	}
}