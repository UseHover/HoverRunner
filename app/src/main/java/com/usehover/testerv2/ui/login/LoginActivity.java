package com.usehover.testerv2.ui.login;

import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.usehover.testerv2.R;
public class LoginActivity extends AppCompatActivity {
	private Fragment frag;
@Override
protected void onCreate(@Nullable Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.login_activity);

	new Handler().postDelayed(new Runnable() {
		@Override
		public void run() {
			frag = new LoginFragment();
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.replace(R.id.login_frame, frag);
			ft.disallowAddToBackStack();
			ft.commit();
		}
	}, 1500);
}
}
