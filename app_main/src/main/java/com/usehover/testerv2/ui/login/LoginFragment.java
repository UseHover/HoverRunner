package com.usehover.testerv2.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.usehover.testerv2.MainActivity;
import com.usehover.testerv2.R;
import com.usehover.testerv2.api.Apis;
import com.usehover.testerv2.enums.PassageEnum;
import com.usehover.testerv2.ui.webview.WebViewActivity;
import com.usehover.testerv2.utils.CustomNetworkUtil;
import com.usehover.testerv2.utils.UIHelper;

import java.util.Objects;


public class LoginFragment extends Fragment {
    private LoginViewModel loginViewModel;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        TextView forgotPassword = view.findViewById(R.id.forgotPassword_text);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getContext().getString(R.string.loggin_in_text));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        UIHelper.setTextUnderline(forgotPassword, Objects.requireNonNull(getContext()).getString(R.string.forgot_password));

        forgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), WebViewActivity.class);
            intent.putExtra("title", Objects.requireNonNull(getContext()).getString(R.string.forgot_password));
            intent.putExtra("url", Objects.requireNonNull(getContext()).getString(R.string.url_forgot_password));
            startActivity(intent);
        });

        EditText emailEdit = view.findViewById(R.id.emailEditId);
        EditText passwordEdit = view.findViewById(R.id.passwordEditId);
        TextView errorEmailText = view.findViewById(R.id.errorText_email);
        TextView errorPasswordText = view.findViewById(R.id.errorText_password);
        TextView emailLabel = view.findViewById(R.id.email_label_id);
        TextView passwordLabel = view.findViewById(R.id.password_label_id);

        emailEdit.setOnClickListener(v -> undoErrorView(emailEdit, errorEmailText, emailLabel));
        passwordEdit.setOnClickListener(v -> undoErrorView(passwordEdit, errorPasswordText, passwordLabel));

        loginViewModel.getModelResult().observe(getViewLifecycleOwner(), modelResult -> {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog.cancel();
            }
            switch (modelResult.getStatus()) {
                case ERROR_EMAIL:
                    //Just to make sure password error gets cleared
                    if(errorPasswordText.getVisibility() == View.VISIBLE)undoErrorView(passwordEdit, errorPasswordText, passwordLabel);
                    //Set email error afterwards
                    setErrorView(emailEdit, errorEmailText, emailLabel);
                    errorEmailText.setText(modelResult.getMessage());

                    break;
                case ERROR_PASSWORD:
                    //Just to make sure email error get's cleared
                    if(errorEmailText.getVisibility() == View.VISIBLE)undoErrorView(emailEdit, errorEmailText, emailLabel);
                    //set password error afterwards
                    setErrorView(passwordEdit, errorPasswordText, passwordLabel);
                    errorPasswordText.setText(modelResult.getMessage());

                    break;
                case ERROR:
                    UIHelper.showHoverToast(getContext(), getActivity().getCurrentFocus(), modelResult.getMessage());
                    break;
                case SUCCESS:
                    MainActivity.LoginYes = 1;
                    startActivity(new Intent(getActivity(), MainActivity.class));
                    if (getActivity() != null)
                        getActivity().finishAffinity();
                    break;
            }
        });


        view.findViewById(R.id.signinButton).setOnClickListener(v -> {
            if(new CustomNetworkUtil(getContext()).isNetworkAvailable() == PassageEnum.ACCEPT) {
                if (!progressDialog.isShowing()) progressDialog.show();
                undoErrorView(emailEdit, errorEmailText, emailLabel);
                undoErrorView(passwordEdit, errorPasswordText, passwordLabel);
                loginViewModel.doLogin(emailEdit.getText().toString(), passwordEdit.getText().toString());
            }
            else UIHelper.showHoverToast(getContext(), getActivity().getCurrentFocus(), Apis.NO_NETWORK);
        });

        return view;
    }
    private void setErrorView(EditText editText, TextView errorText, TextView label) {
        editText.setActivated(true);
        editText.setTextColor(getResources().getColor(R.color.colorRed));
        errorText.setVisibility(View.VISIBLE);
        label.setTextColor(getResources().getColor(R.color.colorRed));
    }
    private void undoErrorView(EditText editText, TextView errorText, TextView label) {
        editText.setActivated(false);
        editText.setTextColor(getResources().getColor(R.color.colorHoverWhite));
        errorText.setVisibility(View.GONE);
        label.setTextColor(getResources().getColor(R.color.colorHoverWhite));
    }
}
