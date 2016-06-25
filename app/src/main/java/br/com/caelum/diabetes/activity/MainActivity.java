package br.com.caelum.diabetes.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import br.com.caelum.diabetes.R;
import br.com.caelum.diabetes.extras.Header;
import br.com.caelum.diabetes.fragment.DashboardFragment;

public class MainActivity extends AppCompatActivity{
    private Header header;

    @Override
    protected void onResume() {
        super.onResume();
        header = new Header(this);
    }

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.main);

		DashboardFragment fragment = new DashboardFragment();
		fragment.setArguments(getIntent().getExtras());

		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.main_view, fragment);
		transaction.commit();
	}

    public void setTitleHeader(String titulo) {
        header.setTitulo(titulo);
    }

    public void setBackArrowIcon() {
        header.setBackArrowIcon();
    }

    public void setHamburguerIcon() {
        header.setHamburguerIcon();
    }

    public void showInfo(String text, String referencia) {
        header.showInfo(text, referencia);
    }

    public void showSettings() {
        header.showSettings();
    }
}