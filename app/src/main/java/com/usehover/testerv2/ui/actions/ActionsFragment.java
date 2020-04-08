package com.usehover.testerv2.ui.actions;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.usehover.testerv2.R;
import com.usehover.testerv2.ui.login.LoginActivity;

public class ActionsFragment extends Fragment {

private ActionsViewModel actionsViewModel;

public View onCreateView(@NonNull LayoutInflater inflater,
						 ViewGroup container, Bundle savedInstanceState) {
	actionsViewModel =
			ViewModelProviders.of(this).get(ActionsViewModel.class);
	View root = inflater.inflate(R.layout.fragment_actions, container, false);
	final TextView textView = root.findViewById(R.id.text_actions);
	actionsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

	textView.setOnClickListener(v -> {
	startActivity(new Intent(getActivity(), LoginActivity.class));
	});
	return root;
}
}
