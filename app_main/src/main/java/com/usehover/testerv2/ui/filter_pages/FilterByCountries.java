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
    private final String COLUMN_COUNTRY = "country_alpha2";
    private boolean saveStateChanged = false;
    TextView saveText;
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
           /* StringBuilder args = new StringBuilder();

            for(int i=0; i<selectedCountries.size(); i++) {
                if(i==0) {
                    args = new StringBuilder(COLUMN_COUNTRY + " = '" + selectedCountries.get(i) + "'");
                }
                else args.append(" OR ").append(COLUMN_COUNTRY).append(" = '").append(selectedCountries.get(i)).append("'");
            }

            */
           if(saveStateChanged) {
            ApplicationInstance.setCountriesFilter(selectedCountries);
            finish();
           }

        });

        RecyclerView itemsRecyclerView = findViewById(R.id.filter_recyclerView);
        itemsRecyclerView.setLayoutManager(UIHelper.setMainLinearManagers(this));
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
