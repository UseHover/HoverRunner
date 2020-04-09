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

import com.google.android.material.snackbar.Snackbar;
import com.usehover.testerv2.MainActivity;
import com.usehover.testerv2.R;
import com.usehover.testerv2.ui.actions.ActionsViewModel;
import com.usehover.testerv2.ui.webview.WebViewActivity;
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

        new UIHelper().setTextUnderline(forgotPassword, Objects.requireNonNull(getContext()).getString(R.string.forgot_password));

        forgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), WebViewActivity.class);
            intent.putExtra("title", Objects.requireNonNull(getContext()).getString(R.string.forgot_password));
            intent.putExtra("url", Objects.requireNonNull(getContext()).getString(R.string.url_forgot_password));
            startActivity(intent);
        });

        loginViewModel.getModelResult().observe(getViewLifecycleOwner(), modelResult -> {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog.cancel();
            }
            switch (modelResult.getStatus()) {
                case 1:
                    UIHelper.showHoverToast(getContext(), getActivity().getCurrentFocus(), modelResult.getMessage());
                    break;
                case 2:
                    startActivity(new Intent(getActivity(), MainActivity.class));
                    if (getActivity() != null)
                        getActivity().finishAffinity();
                    break;
            }
        });

        EditText emailEdit = view.findViewById(R.id.emailEditId);
        EditText passwordEdit = view.findViewById(R.id.passwordEditId);
        view.findViewById(R.id.signinButton).setOnClickListener(v -> {
            if (!progressDialog.isShowing()) progressDialog.show();
            loginViewModel.doLogin(emailEdit.getText().toString(), passwordEdit.getText().toString());
        });

        return view;
    }
}
