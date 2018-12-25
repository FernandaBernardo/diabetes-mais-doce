package br.com.diabetesmaisdoce.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import br.com.diabetesmaisdoce.R;
import br.com.diabetesmaisdoce.dao.DbHelper;
import br.com.diabetesmaisdoce.dao.PacienteDao;
import br.com.diabetesmaisdoce.dao.PopulaAlimento;
import br.com.diabetesmaisdoce.dialog.InfoDialog;
import br.com.diabetesmaisdoce.dialog.TermsDialog;
import br.com.diabetesmaisdoce.model.Paciente;

public class BemVindoActivity extends AppCompatActivity {
	
	private Paciente pacienteBanco;
	private PacienteDao dao;
    private DbHelper helper;
    private CallbackManager facebookCallbackManager;
    private LoginButton facebookLogin;
    private EditText nomeUsuario;
    private ProgressDialog progressDialog;
    private Button botaoEntrar;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(this.getApplicationContext());
        facebookCallbackManager = CallbackManager.Factory.create();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.bem_vindo);

		helper = new DbHelper(BemVindoActivity.this);
		dao = new PacienteDao(helper);
		pacienteBanco = dao.getPaciente();
        botaoEntrar = (Button) findViewById(R.id.botao_entrar);

        final CheckBox termsCheckbox = (CheckBox) findViewById(R.id.terms);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View widget) {
                widget.cancelPendingInputEvents();
                TermsDialog terms = new TermsDialog();
                FragmentManager fm = BemVindoActivity.this.getSupportFragmentManager();
                terms.show(fm, "header");
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
            }
        };

        SpannableString linkText = new SpannableString("Termos de Uso");
        linkText.setSpan(clickableSpan, 0, linkText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        CharSequence cs = TextUtils.expandTemplate("Eu li e aceito os ^1", linkText);

        termsCheckbox.setText(cs);
        termsCheckbox.setMovementMethod(LinkMovementMethod.getInstance());

        termsCheckbox.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                botaoEntrar.setEnabled(termsCheckbox.isChecked());
                if(!botaoEntrar.isEnabled()) {
                    botaoEntrar.setAlpha(.5f);
                } else {
                    botaoEntrar.setAlpha(1f);
                }
            }
        });

        if(pacienteBanco != null) {
			Intent intent = new Intent(BemVindoActivity.this, MainActivity.class);
			intent.putExtra("paciente", pacienteBanco);
			startActivity(intent);
            finish();
        } else {
            facebookLogin = (LoginButton) findViewById(R.id.facebook_login_button);
            nomeUsuario = (EditText) findViewById(R.id.nome_pessoa);

            nativeLogin();
            facebookLogin();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            finish();
        }
        facebookCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void nativeLogin() {
        nomeUsuario.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    submitNativeLogin();
                    return true;
                }
                return false;
            }
        });
        botaoEntrar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
            submitNativeLogin();
            }
        });
    }

    private void submitNativeLogin() {
        progressDialog = new ProgressDialog(BemVindoActivity.this);
        progressDialog.setMessage("Carregando...");
        progressDialog.show();
        newUser(nomeUsuario.getText().toString());
    }

    private void facebookLogin() {
        facebookLogin.setReadPermissions(Arrays.asList("public_profile"));
        facebookLogin.registerCallback(facebookCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                progressDialog = new ProgressDialog(BemVindoActivity.this);
                progressDialog.setMessage("Carregando...");
                progressDialog.show();
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            newUser(object.getString("name"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "name, picture");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                finish();
                Intent intent = new Intent(BemVindoActivity.this, BemVindoActivity.class);
                startActivity(intent);
            }

            @Override
            public void onError(FacebookException error) {
                finish();
                Intent intent = new Intent(BemVindoActivity.this, BemVindoActivity.class);
                startActivity(intent);
            }
        });
    }

    private void newUser(String nome) {
        new PopulaAlimento(helper, getResources()).execute();
        Paciente paciente = new Paciente();
        paciente.setNome(nome);
        dao.salva(paciente);
        Intent intent = new Intent(BemVindoActivity.this, MainActivity.class);
        intent.putExtra("paciente", paciente);
        startActivity(intent);
        finish();
        progressDialog.dismiss();
    }
}