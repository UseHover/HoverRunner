package com.usehover.testerv2.ui.transactions;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.usehover.testerv2.R;
import com.usehover.testerv2.adapters.HoverAdapters;
import com.usehover.testerv2.adapters.ViewsRelated;
import com.usehover.testerv2.interfaces.CustomOnClickListener;
import com.usehover.testerv2.ui.actions.filter.ActionFilterActivity;
import com.usehover.testerv2.utils.UIHelper;


public class TransactionFragment extends Fragment implements CustomOnClickListener {

	private static final int FILTER_RESULT_TRANSACTION = 301;
	private TransactionViewModel transactionViewModel;
	private TextView filterText, emptyStateText;
	private ProgressBar progressBar;
	private RecyclerView homeTransactionsRecyclerView;

	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
		View root = inflater.inflate(R.layout.fragment_transactions, container, false);

		filterText  = root.findViewById(R.id.transactionFilter_id);
		progressBar = root.findViewById(R.id.progress_state_1);
		emptyStateText = root.findViewById(R.id.empty_text_1);

		homeTransactionsRecyclerView= root.findViewById(R.id.recyclerViewId);
		homeTransactionsRecyclerView.setLayoutManager(ViewsRelated.setMainLinearManagers(getContext()));

		UIHelper.setTextUnderline(filterText, getResources().getString(R.string.filter_text));

		//CALL THE FILTER FUNCTION
		filterText.setOnClickListener(v -> {
			Intent i = new Intent(getActivity(), ActionFilterActivity.class);
			startActivityForResult(i, FILTER_RESULT_TRANSACTION);
		});

		transactionViewModel.getAllTransactions();

		return root;
	}

	@Override
	public void onResume() {
		super.onResume();
		transactionViewModel.getText().observe(getViewLifecycleOwner(), filterStatus-> {
			switch (filterStatus) {
				case FILTER_OFF: filterText.setTextColor(getResources().getColor(R.color.colorHoverWhite));
					break;
				case FILTER_ON: filterText.setTextColor(getResources().getColor(R.color.colorPrimary));
				filterText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_dot_purple_24dp, 0,0,0);
				filterText.setCompoundDrawablePadding(8);
					break;
			}
		});


		transactionViewModel.loadTransactionsObs().observe(getViewLifecycleOwner(), fullActionResult -> {
			switch (fullActionResult.getEnums()){
				case LOADING:
					if(homeTransactionsRecyclerView.getVisibility() == View.VISIBLE)homeTransactionsRecyclerView.setVisibility(View.GONE);
					if(emptyStateText.getVisibility() == View.VISIBLE) emptyStateText.setVisibility(View.GONE);
					progressBar.setVisibility(View.VISIBLE);
					break;
				case EMPTY:
					if(homeTransactionsRecyclerView.getVisibility() == View.VISIBLE)homeTransactionsRecyclerView.setVisibility(View.GONE);
					if(progressBar.getVisibility() == View.VISIBLE) progressBar.setVisibility(View.GONE);
					emptyStateText.setVisibility(View.VISIBLE);
					break;
				case HAS_DATA:
					if(emptyStateText.getVisibility() == View.VISIBLE) emptyStateText.setVisibility(View.GONE);
					if(progressBar.getVisibility() == View.VISIBLE) progressBar.setVisibility(View.GONE);
					if(homeTransactionsRecyclerView.getVisibility() != View.VISIBLE) homeTransactionsRecyclerView.setVisibility(View.VISIBLE);
					homeTransactionsRecyclerView.setAdapter(new HoverAdapters.TransactionRecyclerAdapter(fullActionResult.getTransactionModelsList(),
							this,
							getResources().getColor(R.color.colorYellow),
							getResources().getColor(R.color.colorRed),
							getResources().getColor(R.color.colorGreen)));
					break;
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == FILTER_RESULT_TRANSACTION) {
			if (resultCode == Activity.RESULT_OK) {
				String newText = data.getStringExtra("idn");
				assert newText != null;
				if (newText.equals("enteredTextValue")) {
					transactionViewModel.setFilterOn();
				}
			}
		}
	}

	@Override
	public void customClickListener(Object... data) {

	}
}
