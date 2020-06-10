package com.hover.runner.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.hover.runner.ApplicationInstance;
import com.hover.runner.MainActivity;
import com.hover.runner.R;
import com.hover.runner.enums.PassageEnum;
import com.hover.runner.ui.webview.WebViewActivity;
import com.hover.runner.utils.UIHelper;
import com.hover.runner.utils.network.NetworkUtil;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private Fragment frag;
    private static final long MOVE_DEFAULT_TIME = 300;
    private static final long FADE_DEFAULT_TIME = 300;

    private LoginViewModel loginViewModel;
    private ProgressBar loginProgressBar;
    private EditText emailEdit, passwordEdit;
    private TextView errorEmailText, errorPasswordText, emailLabel, passwordLabel;
    private Button signInButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        TextView forgotPassword = findViewById(R.id.forgotPassword_text);

        UIHelper.setTextUnderline(forgotPassword, Objects.requireNonNull(this).getString(R.string.forgot_password));

        forgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(this, WebViewActivity.class);
            intent.putExtra("title", Objects.requireNonNull(this).getString(R.string.forgot_password));
            intent.putExtra("url", Objects.requireNonNull(this).getString(R.string.url_forgot_password));
            startActivity(intent);
        });

        loginProgressBar = findViewById(R.id.login_progress);
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

        loginProgressBar.setIndeterminate(true);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        loginViewModel.getModelResult().observe(this, modelResult -> {
            signInButton.setText(getResources().getString(R.string.signIn));
            loginProgressBar.setVisibility(View.GONE);
            switch (modelResult.getStatus()) {
                case ERROR_EMAIL:
                    signInButton.setClickable(true);
                    //Just to make sure password error gets cleared
                    if (errorPasswordText.getVisibility() == View.VISIBLE)
                        undoErrorView(passwordEdit, errorPasswordText, passwordLabel);
                    //Set email error afterwards
                    setErrorView(emailEdit, errorEmailText, emailLabel);
                    errorEmailText.setText(modelResult.getMessage());

                    break;
                case ERROR_PASSWORD:
                    signInButton.setClickable(true);
                    //Just to make sure email error get's cleared
                    if (errorEmailText.getVisibility() == View.VISIBLE)
                        undoErrorView(emailEdit, errorEmailText, emailLabel);
                    //set password error afterwards
                    setErrorView(passwordEdit, errorPasswordText, passwordLabel);
                    errorPasswordText.setText(modelResult.getMessage());

                    break;
                case ERROR:
                    signInButton.setClickable(true);
                    UIHelper.showHoverToast(this, this.getCurrentFocus(), modelResult.getMessage());
                    break;
                case SUCCESS:
                    MainActivity.LoginYes = 1;
                    startActivity(new Intent(this, MainActivity.class));
                    this.finishAffinity();
                    break;
            }
        });


        signInButton.setOnClickListener(this::signInActionMethod);
        ImageView logo = findViewById(R.id.hover_bg1);

        new Handler().postDelayed(() -> {
                findViewById(R.id.iv1).setVisibility(View.GONE);
            try {
                ScaleAnimation scale = new ScaleAnimation((float) 1.0, (float) 1.0, (float) 1.0, (float) 0.5);
                scale.setFillAfter(true);
                scale.setDuration(500);

                logo.startAnimation(scale);
                fadeIn(findViewById(R.id.iv2));

            } catch (Exception ignored) {
            }

        }, 1500);

        new Handler().postDelayed(() -> fadeIn(findViewById(R.id.divider)), 2000);

    }

    private void fadeIn(View view) {
        if (view != null) {
            view.setVisibility(View.VISIBLE);
            fade(new AlphaAnimation(0.0f, 1.0f), view);
        }
    }

    private void fade(AlphaAnimation fade, View view) {
        fade.setDuration(300);
        fade.setFillAfter(true);
        view.startAnimation(fade);
    }

    private void signInActionMethod(View v) {
        if (new NetworkUtil(this).isNetworkAvailable() == PassageEnum.ACCEPT) {
            loginProgressBar.setVisibility(View.VISIBLE);
            signInButton.setText(getResources().getString(R.string.loggin_in_text));

            if (v != null) v.setClickable(false);

            undoErrorView(emailEdit, errorEmailText, emailLabel);
            undoErrorView(passwordEdit, errorPasswordText, passwordLabel);
            new Handler().postDelayed(() -> loginViewModel.doLogin(emailEdit.getText().toString(), passwordEdit.getText().toString()), 500);
        } else
            UIHelper.showHoverToast(this, getCurrentFocus(), this.getString(R.string.NO_NETWORK));
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



}
