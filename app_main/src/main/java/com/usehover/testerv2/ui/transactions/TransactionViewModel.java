package com.usehover.testerv2.ui.transactions;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.usehover.testerv2.api.Apis;
import com.usehover.testerv2.enums.HomeEnums;
import com.usehover.testerv2.enums.StatusEnums;
import com.usehover.testerv2.models.FullTransactionResult;


public class TransactionViewModel extends ViewModel {

	private MutableLiveData<HomeEnums> filterStatus;
	private MutableLiveData<FullTransactionResult> homeTransactions;


	public TransactionViewModel() {
		filterStatus = new MutableLiveData<>();
		homeTransactions = new MutableLiveData<>();
		filterStatus.setValue(HomeEnums.FILTER_OFF);
		homeTransactions.setValue(new FullTransactionResult(StatusEnums.LOADING, null));
	}

	LiveData<HomeEnums> getText() {
		return filterStatus;
	}
	LiveData<FullTransactionResult> loadTransactionsObs() {return homeTransactions;}

	void setFilterOn() {
		filterStatus.postValue(HomeEnums.FILTER_ON);
	}
	void getAllTransactions() { homeTransactions.postValue(new Apis().doGetAllTransactionsWorkManager()); }
	void getTransactionByActionId(String actionId) {homeTransactions.postValue(new Apis().doGetTransactionsByActionIdWorkManager(actionId)); }
}