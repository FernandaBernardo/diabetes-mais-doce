package br.com.diabetesmaisdoce.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import br.com.diabetesmaisdoce.model.AlimentoFisico;
import br.com.diabetesmaisdoce.model.AlimentoVirtual;
import br.com.diabetesmaisdoce.model.DadosMedicos;
import br.com.diabetesmaisdoce.model.Glicemia;
import br.com.diabetesmaisdoce.model.Lembrete;
import br.com.diabetesmaisdoce.model.Paciente;
import br.com.diabetesmaisdoce.model.Refeicao;

public class DbHelper extends OrmLiteSqliteOpenHelper{

	private static final String DATABASE = "Diabetes";

	public Context context;

	public DbHelper(Context context) {
		super(context, DATABASE, null, 3);
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
				db.execSQL("ALTER TABLE dadosmedicos ADD column madrugada REAL");
				Log.i(DbHelper.class.getName(), "run migration 1");
			case 2:
				db.execSQL("ALTER TABLE glicemia ADD column observacao TEXT");
				Log.i(DbHelper.class.getName(), "run migration 2");
		}
	}

	public <T> RuntimeExceptionDao<T, Integer> getSimpleDataDao(Class<T> clazz) {
		return getRuntimeExceptionDao(clazz);
	}
}