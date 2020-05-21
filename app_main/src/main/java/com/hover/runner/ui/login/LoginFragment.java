package com.hover.runner.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.hover.runner.ApplicationInstance;
import com.hover.runner.MainActivity;
import com.hover.runner.R;
import com.hover.runner.api.Apis;
import com.hover.runner.enums.PassageEnum;
import com.hover.runner.ui.webview.WebViewActivity;
import com.hover.runner.utils.network.NetworkUtil;
import com.hover.runner.utils.UIHelper;

import java.util.Objects;


public class LoginFragment extends Fragment {
    private LoginViewModel loginViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        TextView forgotPassword = view.findViewById(R.id.forgotPassword_text);


        UIHelper.setTextUnderline(forgotPassword, Objects.requireNonNull(getContext()).getString(R.string.forgot_password));

        forgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), WebViewActivity.class);
            intent.putExtra("title", Objects.requireNonNull(getContext()).getString(R.string.forgot_password));
            intent.putExtra("url", Objects.requireNonNull(getContext()).getString(R.string.url_forgot_password));
            startActivity(intent);
        });

        ProgressBar loginProgressBar = view.findViewById(R.id.login_progress);
        Button signInButton = view.findViewById(R.id.signinButton);
        EditText emailEdit = view.findViewById(R.id.emailEditId);
        EditText passwordEdit = view.findViewById(R.id.passwordEditId);
        TextView errorEmailText = view.findViewById(R.id.errorText_email);
        TextView errorPasswordText = view.findViewById(R.id.errorText_password);
        TextView emailLabel = view.findViewById(R.id.email_label_id);
        TextView passwordLabel = view.findViewById(R.id.password_label_id);

        emailEdit.setOnClickListener(v -> undoErrorView(emailEdit, errorEmailText, emailLabel));
        passwordEdit.setOnClickListener(v -> undoErrorView(passwordEdit, errorPasswordText, passwordLabel));

        loginProgressBar.setIndeterminate(true);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        loginViewModel.getModelResult().observe(getViewLifecycleOwner(), modelResult -> {
            signInButton.setText(getResources().getString(R.string.signIn));
            loginProgressBar.setVisibility(View.GONE);
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
                case ERROR:
                    signInButton.setClickable(true);
                    UIHelper.showHoverToast(getContext(), getActivity()!=null ? getActivity().getCurrentFocus() : null , modelResult.getMessage());
                    break;
                case SUCCESS:
                    MainActivity.LoginYes = 1;
                    startActivity(new Intent(getActivity(), MainActivity.class));
                    if (getActivity() != null)
                        getActivity().finishAffinity();
                    break;
            }
        });


        signInButton.setOnClickListener(v -> {
            if(new NetworkUtil(getContext()).isNetworkAvailable() == PassageEnum.ACCEPT) {
                loginProgressBar.setVisibility(View.VISIBLE);
                signInButton.setText(getResources().getString(R.string.loggin_in_text));
                v.setClickable(false);
                undoErrorView(emailEdit, errorEmailText, emailLabel);
                undoErrorView(passwordEdit, errorPasswordText, passwordLabel);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loginViewModel.doLogin(emailEdit.getText().toString(), passwordEdit.getText().toString());
                    }
                }, 500);
            }
            else UIHelper.showHoverToast(getContext(), getActivity()!=null ? getActivity().getCurrentFocus() : null, Apis.NO_NETWORK);
        });

        return view;
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
