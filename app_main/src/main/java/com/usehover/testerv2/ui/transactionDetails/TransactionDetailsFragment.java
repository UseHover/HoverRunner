package com.usehover.testerv2.ui.transactionDetails;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.usehover.testerv2.R;
import com.usehover.testerv2.adapters.TransactionDetailsRecyclerAdapter;
import com.usehover.testerv2.api.Apis;
import com.usehover.testerv2.enums.ClickTypeEnum;
import com.usehover.testerv2.enums.StatusEnums;
import com.usehover.testerv2.enums.TransactionDetailsDataType;
import com.usehover.testerv2.interfaces.CustomOnClickListener;
import com.usehover.testerv2.ui.action_details.ActionDetailsActivity;
import com.usehover.testerv2.ui.parsers.ParsersActivity;
import com.usehover.testerv2.utils.UIHelper;

public class TransactionDetailsFragment extends Fragment implements CustomOnClickListener {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.transaction_details_fragment, container, false);

        RecyclerView aboutInfoRecyclerView = view.findViewById(R.id.transac_aboutInfo_recyclerView);
        RecyclerView deviceRecyclerView = view.findViewById(R.id.transac_devices_recyclerView);
        RecyclerView debugInfoRecyclerView = view.findViewById(R.id.transac_debugInfo_recyclerView);

        aboutInfoRecyclerView.setLayoutManager(UIHelper.setMainLinearManagers(getContext()));
        deviceRecyclerView.setLayoutManager(UIHelper.setMainLinearManagers(getContext()));
        debugInfoRecyclerView.setLayoutManager(UIHelper.setMainLinearManagers(getContext()));

        TransactionDetailsViewModel transactionDetailsViewModel = new ViewModelProvider(this).get(TransactionDetailsViewModel.class);
        transactionDetailsViewModel.loadAboutInfoModelsObs().observe(getViewLifecycleOwner(), model->{
            if(model !=null) {
                aboutInfoRecyclerView.setAdapter(new TransactionDetailsRecyclerAdapter(model,
                        this,
                        R.color.colorRed,
                        R.color.colorYellow,
                        R.color.colorGreen));
            }

        });

        transactionDetailsViewModel.loadDeviceModelsObs().observe(getViewLifecycleOwner(), model-> {
            if(model !=null)
                aboutInfoRecyclerView.setAdapter(new TransactionDetailsRecyclerAdapter(model, this));
        });

        transactionDetailsViewModel.loadDebugInfoModelsObs().observe(getViewLifecycleOwner(), model-> {
            if(model !=null)
                aboutInfoRecyclerView.setAdapter(new TransactionDetailsRecyclerAdapter(model, this));
        });

        transactionDetailsViewModel.getAboutInfoModels();
        transactionDetailsViewModel.getDebugInfoModels();
        transactionDetailsViewModel.getDeviceModels();

        return view;
    }

    @Override
    public void customClickListener(Object... data) {
        if (data[0] == ClickTypeEnum.CLICK_ACTION) {
            Intent i = new Intent(getActivity(), ActionDetailsActivity.class);
            i.putExtra(Apis.ACTION_ID, (String) data[1]);
            i.putExtra(Apis.ACTION_TITLE, (String) data[2]);
            i.putExtra(Apis.ACTION_STATUS, (StatusEnums) data[3]);
            startActivity(i);
        }
        else {
            Intent i2 = new Intent(getActivity(), ParsersActivity.class);
            i2.putExtra(ParsersActivity.PARSER_EXTRA, (String )data[1]);
            startActivity(i2);
        }
    }
}
