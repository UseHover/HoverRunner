package com.usehover.testerv2.ui.filter_pages;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.usehover.testerv2.ApplicationInstance;
import com.usehover.testerv2.R;
import com.usehover.testerv2.adapters.FilterSingleItemRecyclerAdapter;
import com.usehover.testerv2.interfaces.CustomOnClickListener;
import com.usehover.testerv2.models.SingleFilterInfoModel;
import com.usehover.testerv2.utils.UIHelper;

import java.util.ArrayList;

public class FilterByCountries extends AppCompatActivity implements CustomOnClickListener {
    private ArrayList<SingleFilterInfoModel> countryList = new ArrayList<>();
    private ArrayList<String> selectedCountries = new ArrayList<>();
    private boolean saveStateChanged = false;
    private TextView saveText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_by_country);
        if(getIntent().getExtras() !=null) countryList = getIntent().getExtras().getParcelableArrayList("data");

        assert countryList != null;
        if(countryList.size() > 0 ) {
            for(int i=0; i<countryList.size(); i++) {
                if(countryList.get(i).isCheck()) selectedCountries.add(countryList.get(i).getTitle());
            }
        }

        findViewById(R.id.country_title).setOnClickListener(v-> finish());
        saveText = findViewById(R.id.filter_save_id);
        saveText.setOnClickListener(v->{
            if(saveStateChanged) {
                int filterType = getIntent().getExtras().getInt("filter_type", 0);
                if(filterType == 0) ApplicationInstance.setCountriesFilter(selectedCountries);
                else ApplicationInstance.setTransactionCountriesFilter(selectedCountries);
                finish();
            }

        });

        RecyclerView itemsRecyclerView = findViewById(R.id.filter_recyclerView);
        itemsRecyclerView.setLayoutManager(UIHelper.setMainLinearManagers(this));
        itemsRecyclerView.setHasFixedSize(true);
        itemsRecyclerView.setAdapter(new FilterSingleItemRecyclerAdapter(countryList, this));



    }

    @Override
    public void customClickListener(Object... data) {
        if(!saveStateChanged) {
            saveText.setTextColor(getResources().getColor(R.color.colorHoverWhite));
            UIHelper.setTextUnderline(saveText, "Save");
            saveStateChanged = true;
        }
        String countryClicked = (String) data[0];
        boolean checked = (boolean) data[1];

        if(checked) selectedCountries.add(countryClicked);
        else if(selectedCountries.indexOf(countryClicked) != -1) selectedCountries.remove(countryClicked);
    }
}
