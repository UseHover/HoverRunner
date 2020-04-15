package com.usehover.testerv2.ui.transactionDetails;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.usehover.testerv2.R;
import com.usehover.testerv2.api.Apis;
import com.usehover.testerv2.enums.StatusEnums;

public class TransactionDetailsActivity extends AppCompatActivity {
    static public String transactionId;
    static public String transactionDate;
    static public StatusEnums transactionStatusEnums;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getIntent().getExtras() !=null) {
            transactionId = getIntent().getExtras().getString(Apis.ACTION_ID);
            transactionDate = getIntent().getExtras().getString(Apis.ACTION_TITLE);
            transactionStatusEnums = (StatusEnums) getIntent().getExtras().get(Apis.ACTION_STATUS);
        }
        setContentView(R.layout.transaction_details_activity);
    }
}
