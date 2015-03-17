package br.com.caelum.diabetes.exception;

import java.sql.SQLException;

import android.content.Context;
import android.widget.Toast;

public class TratadorExcecao {
	private Context context;
	
	public TratadorExcecao(Context context) {
		this.context = context;
	}
	
	public void trataSqlException (SQLException e) {
		Toast.makeText(context, "Problema ao manipular informações", Toast.LENGTH_LONG);
		e.printStackTrace();
	}
}
