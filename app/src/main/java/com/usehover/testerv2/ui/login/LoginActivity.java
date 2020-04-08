package com.usehover.testerv2.ui.login;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.transition.Fade;
import androidx.transition.TransitionInflater;
import androidx.transition.TransitionSet;

import com.usehover.testerv2.R;
public class LoginActivity extends AppCompatActivity {
	private Fragment frag;
	private static final long MOVE_DEFAULT_TIME = 1000;
	private static final long FADE_DEFAULT_TIME = 300;
@Override
protected void onCreate(@Nullable Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.login_activity);

	frag = new LoginFragment();

	TransitionSet enterTransitionSet = new TransitionSet();
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
		enterTransitionSet.addTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.move));
	enterTransitionSet.setDuration(MOVE_DEFAULT_TIME);
	enterTransitionSet.setStartDelay(FADE_DEFAULT_TIME);
	frag.setSharedElementEnterTransition(enterTransitionSet);

	Fade enterFade = new Fade();
	enterFade.setStartDelay(MOVE_DEFAULT_TIME + FADE_DEFAULT_TIME);
	enterFade.setDuration(FADE_DEFAULT_TIME);
	frag.setEnterTransition(enterFade);

	new Handler().postDelayed(() -> {
		View logo = findViewById(R.id.hover_bg1);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) ft.addSharedElement(logo, logo.getTransitionName());
		ft.replace(R.id.login_frame, frag);
		ft.disallowAddToBackStack();
		ft.commit();
	}, 1500);
}
}
