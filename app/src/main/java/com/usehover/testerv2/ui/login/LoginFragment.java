package com.usehover.testerv2.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.usehover.testerv2.R;

public class LoginFragment extends Fragment {
@Nullable
@Override
public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
	View view = inflater.inflate(R.layout.login_fragment, container, false);
	return view;
}
}
