package com.usehover.testerv2.ui.transactions;

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

public class TransactionFragment extends Fragment {

	private TransactionViewModel transactionViewModel;

	public View onCreateView(@NonNull LayoutInflater inflater,
							 ViewGroup container, Bundle savedInstanceState) {
		transactionViewModel =
				ViewModelProviders.of(this).get(TransactionViewModel.class);
		View root = inflater.inflate(R.layout.fragment_transactions, container, false);
		final TextView textView = root.findViewById(R.id.text_transaction);
		transactionViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
			@Override
			public void onChanged(@Nullable String s) {
				textView.setText(s);
			}
		});

		return root;
	}
}
