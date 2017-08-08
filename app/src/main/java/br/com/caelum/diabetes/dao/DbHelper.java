package br.com.caelum.diabetes.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import br.com.caelum.diabetes.model.AlimentoFisico;
import br.com.caelum.diabetes.model.AlimentoVirtual;
import br.com.caelum.diabetes.model.DadosMedicos;
import br.com.caelum.diabetes.model.Glicemia;
import br.com.caelum.diabetes.model.Lembrete;
import br.com.caelum.diabetes.model.Paciente;
import br.com.caelum.diabetes.model.Refeicao;

public class DbHelper extends OrmLiteSqliteOpenHelper{

	private static final String DATABASE = "Diabetes";

	public Context context;

	public DbHelper(Context context) {
		super(context, DATABASE, null, 2);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			Log.i(DbHelper.class.getName(), "onCreate");
			TableUtils.createTable(connectionSource, Paciente.class);
			TableUtils.createTable(connectionSource, DadosMedicos.class);
			TableUtils.createTable(connectionSource, Refeicao.class);
			TableUtils.createTable(connectionSource, AlimentoVirtual.class);
			TableUtils.createTable(connectionSource, AlimentoFisico.class);
			TableUtils.createTable(connectionSource, Lembrete.class);
			TableUtils.createTable(connectionSource, Glicemia.class);

		} catch (SQLException e) {
			Log.e(DbHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		Log.i(DbHelper.class.getName(), "onUpgrade");
		switch (oldVersion) {
			case 1:
				String sql = "ALTER TABLE dadosmedicos ADD column madrugada REAL";
				db.execSQL(sql);
				Log.i(DbHelper.class.getName(), "run migration 1");
		}
	}

	public <T> RuntimeExceptionDao<T, Integer> getSimpleDataDao(Class<T> clazz) {
		return getRuntimeExceptionDao(clazz);
	}
}