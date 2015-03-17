package br.com.caelum.diabetes.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import br.com.caelum.diabetes.R;
import br.com.caelum.diabetes.fragment.DashboardFragment;
import br.com.caelum.diabetes.fragment.calculadora.NovaRefeicaoFragment;
import br.com.caelum.diabetes.fragment.calculadora.NovoAlimentoDiferenteFragment;
import br.com.caelum.diabetes.fragment.glicemia.NovaGlicemiaFragment;
import br.com.caelum.diabetes.fragment.perfil.ConfigurarDadosPessoaisFragment;
import br.com.caelum.diabetes.fragment.perfil.ConfigurarGlicemiaAlvoFragment;
import br.com.caelum.diabetes.fragment.perfil.ConfigurarInsulinaContinuaFragment;
import br.com.caelum.diabetes.fragment.perfil.ConfigurarInsulinaCorrecaoFragment;

public class MainActivity extends FragmentActivity{
	  private DrawerLayout menuLateral;
	  private ListView listaMenuLateral;
	  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
	                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.main);
		
		String[] titulos = {"Home", "Novo Alimento", "Nova Refeição", "Nova Glicemia", 
				"Configurações Pessoais", "Configurar Basal", "Configurar Bolus", "Configurar Glicemia Alvo"};
		
        menuLateral = (DrawerLayout) findViewById(R.id.menu_lateral);
        listaMenuLateral = (ListView) findViewById(R.id.lista_menu_lateral);

        listaMenuLateral.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titulos));
        listaMenuLateral.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
				FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
				switch(pos) {
					case 0:
						transaction.replace(R.id.main_view, new DashboardFragment());
						break;
					case 1:
						transaction.replace(R.id.main_view, new NovoAlimentoDiferenteFragment());
						break;
					case 2:
						transaction.replace(R.id.main_view, new NovaRefeicaoFragment());
						break;
					case 3:
						transaction.replace(R.id.main_view, new NovaGlicemiaFragment());
						break;
					case 4:
						transaction.replace(R.id.main_view, new ConfigurarDadosPessoaisFragment());
						break;
					case 5:
						transaction.replace(R.id.main_view, new ConfigurarInsulinaContinuaFragment());
						break;
					case 6:
						transaction.replace(R.id.main_view, new ConfigurarInsulinaCorrecaoFragment());
						break;
					case 7:
						transaction.replace(R.id.main_view, new ConfigurarGlicemiaAlvoFragment());
						break;
				}
				transaction.addToBackStack(null);
				transaction.commit();
				menuLateral.closeDrawer(listaMenuLateral);
			}
		});
        
        Button logo = (Button) findViewById(R.id.logo);
        logo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				menuLateral.openDrawer(listaMenuLateral);
			}
		});		
		
		DashboardFragment fragment = new DashboardFragment();
		fragment.setArguments(getIntent().getExtras());
		
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.main_view, fragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}
}