package br.com.caelum.diabetes.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import br.com.caelum.diabetes.R;
import br.com.caelum.diabetes.dao.DbHelper;
import br.com.caelum.diabetes.dao.PacienteDao;
import br.com.caelum.diabetes.dao.PopulaAlimento;
import br.com.caelum.diabetes.model.Paciente;

public class BemVindoActivity extends Activity {
	
	private Paciente pacienteBanco;
	private PacienteDao dao;
    private DbHelper helper;
    private CallbackManager facebookCallbackManager;
    private LoginButton facebookLogin;
    private EditText nomeUsuario;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(this.getApplicationContext());

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.bem_vindo);

        facebookCallbackManager = CallbackManager.Factory.create();
        facebookLogin = (LoginButton) findViewById(R.id.facebook_login_button);
        nomeUsuario = (EditText) findViewById(R.id.nome_pessoa);

		helper = new DbHelper(BemVindoActivity.this);
		dao = new PacienteDao(helper);
		pacienteBanco = dao.getPaciente();

		if(pacienteBanco != null) {
			Intent intent = new Intent(BemVindoActivity.this, MainActivity.class);
			intent.putExtra("paciente", pacienteBanco);
			startActivity(intent);
        } else {
            new PopulaAlimento(helper, getResources()).execute();

            nativeLogin();
            facebookLogin();
        }
    }

    @Override
	protected void onPause() {
		super.onPause();
		finish();
	}

    @Override
    protected void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        nextActivity(profile);
    }

    @Override
    protected void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        facebookCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void nativeLogin() {
        Button botao = (Button) findViewById(R.id.botao_proximo);
        botao.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Paciente paciente = new Paciente();
                paciente.setNome(nomeUsuario.getText().toString());
                dao.salva(paciente);
                Intent intent = new Intent(BemVindoActivity.this, MainActivity.class);
                intent.putExtra("paciente", paciente);
                startActivity(intent);
            }
        });
    }

    private void facebookLogin() {
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
            }
        };
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                nextActivity(newProfile);
            }
        };
        accessTokenTracker.startTracking();
        profileTracker.startTracking();

        facebookLogin.setReadPermissions("public_profile");
        facebookLogin.registerCallback(facebookCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                Profile profile = Profile.getCurrentProfile();
                nextActivity(profile);
                Toast.makeText(getApplicationContext(), "Logging in...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Log.d("BemVindoActivity", "cancel Facebook");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("BemVindoActivity", "error Facebook");
            }
        });
    }

    private void nextActivity(Profile profile){
        if(profile != null){
            Paciente paciente = new Paciente();
            paciente.setNome(profile.getFirstName().toString());
            dao.salva(paciente);
            Intent intent = new Intent(BemVindoActivity.this, MainActivity.class);
            intent.putExtra("paciente", paciente);
            startActivity(intent);
        }
    }
}