package com.hover.runner.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Fade;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.hover.runner.ApplicationInstance;
import com.hover.runner.R;
import com.hover.runner.enums.PassageEnum;
import com.hover.runner.ui.webview.WebViewActivity;
import com.hover.runner.settings.SettingsHelper;
import com.hover.runner.utils.network.NetworkUtil;
import com.hover.runner.utils.UIHelper;

import java.util.Objects;


public class LoginActivity2 extends AppCompatActivity {
    private EditText emailEdit,passwordEdit;
    private  TextView errorEmailText, errorPasswordText,emailLabel, passwordLabel;
    private  Button signInButton;

   

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity2);
        TextView forgotPassword = findViewById(R.id.forgotPassword_text);


        UIHelper.setTextUnderline(forgotPassword, Objects.requireNonNull(this).getString(R.string.forgot_password));

        forgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(this, WebViewActivity.class);
            intent.putExtra("title", Objects.requireNonNull(this).getString(R.string.forgot_password));
            intent.putExtra("url", Objects.requireNonNull(this).getString(R.string.url_forgot_password));
            startActivity(intent);
        });

        signInButton = findViewById(R.id.signinButton);
         emailEdit = findViewById(R.id.emailEditId);
        passwordEdit = findViewById(R.id.passwordEditId);
        errorEmailText = findViewById(R.id.errorText_email);
        errorPasswordText = findViewById(R.id.errorText_password);
        emailLabel = findViewById(R.id.email_label_id);
        passwordLabel = findViewById(R.id.password_label_id);

        emailEdit.setOnClickListener(v -> undoErrorView(emailEdit, errorEmailText, emailLabel));
        passwordEdit.setOnClickListener(v -> undoErrorView(passwordEdit, errorPasswordText, passwordLabel));
        passwordEdit.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                signInActionMethod(null);
            }
            return false;
        });

        LoginViewModel loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        loginViewModel.getModelResult().observe(this, modelResult -> {
            signInButton.setText(getResources().getString(R.string.signIn));
            switch (modelResult.getStatus()) {
                case ERROR_EMAIL:
                    signInButton.setClickable(true);
                    //Just to make sure password error gets cleared
                    if(errorPasswordText.getVisibility() == View.VISIBLE)undoErrorView(passwordEdit, errorPasswordText, passwordLabel);
                    //Set email error afterwards
                    setErrorView(emailEdit, errorEmailText, emailLabel);
                    errorEmailText.setText(modelResult.getMessage());

                    break;
                case ERROR_PASSWORD:
                    signInButton.setClickable(true);
                    //Just to make sure email error get's cleared
                    if(errorEmailText.getVisibility() == View.VISIBLE)undoErrorView(emailEdit, errorEmailText, emailLabel);
                    //set password error afterwards
                    setErrorView(passwordEdit, errorPasswordText, passwordLabel);
                    errorPasswordText.setText(modelResult.getMessage());

                    break;
            }
        });


        signInButton.setOnClickListener(this::signInActionMethod);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Fade fade = new Fade();
            fade.excludeTarget(android.R.id.statusBarBackground, true);
            fade.excludeTarget(android.R.id.navigationBarBackground, true);

            getWindow().setEnterTransition(fade);
            getWindow().setExitTransition(fade);
        }

    }

    private void signInActionMethod(View v) {
        undoErrorView(emailEdit, errorEmailText, emailLabel);
        undoErrorView(passwordEdit, errorPasswordText, passwordLabel);

        if(new NetworkUtil(this).isNetworkAvailable() == PassageEnum.ACCEPT) {
            new Handler().postDelayed(() -> {
                SettingsHelper.saveEmail(emailEdit.getText().toString(), this);
                Intent returnIntent = new Intent();
                String[] result = new String[] {emailEdit.getText().toString(), passwordEdit.getText().toString()};
                returnIntent.putExtra("login_data",result);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }, 500);

        }
        else UIHelper.flashMessage(this, getCurrentFocus(), ApplicationInstance.getContext().getString(R.string.NO_NETWORK));
    }
    private void setErrorView(EditText editText, TextView errorText, TextView label) {
        editText.setActivated(true);
        editText.setTextColor(ApplicationInstance.getColorRed());
        errorText.setVisibility(View.VISIBLE);
        label.setTextColor(ApplicationInstance.getColorRed());
    }
    private void undoErrorView(EditText editText, TextView errorText, TextView label) {
        editText.setActivated(false);
        editText.setTextColor(getResources().getColor(R.color.colorHoverWhite));
        errorText.setVisibility(View.GONE);
        label.setTextColor(getResources().getColor(R.color.colorHoverWhite));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED,returnIntent);
        finish();

    }
}
