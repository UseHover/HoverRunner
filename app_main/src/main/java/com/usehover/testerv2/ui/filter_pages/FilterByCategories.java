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

public class FilterByCategories extends AppCompatActivity implements CustomOnClickListener {
    private ArrayList<SingleFilterInfoModel> categoryList = new ArrayList<>();
    private ArrayList<String> selectedCategories = new ArrayList<>();
    boolean saveStateChanged = false;
    private final String COLUMN_COUNTRY = "category_alpha2";

    TextView saveText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_by_categories);
        if(getIntent().getExtras() !=null) categoryList = getIntent().getExtras().getParcelableArrayList("data");

        assert categoryList != null;
        if(categoryList.size() > 0 ) {
            for(int i=0; i<categoryList.size(); i++) {
                if(categoryList.get(i).isCheck()) selectedCategories.add(categoryList.get(i).getTitle());
            }
        }

        findViewById(R.id.category_title).setOnClickListener(v-> finish());
        saveText = findViewById(R.id.filter_save_id);
        saveText.setOnClickListener(v->{
           /* StringBuilder args = new StringBuilder();

            for(int i=0; i<selectedCategories.size(); i++) {
                if(i==0) {
                    args = new StringBuilder(COLUMN_COUNTRY + " = '" + selectedCategories.get(i) + "'");
                }
                else args.append(" OR ").append(COLUMN_COUNTRY).append(" = '").append(selectedCategories.get(i)).append("'");
            }

            */
           if(saveStateChanged) {
               ApplicationInstance.setCategoryFilter(selectedCategories);
               finish();
           }
        });

        RecyclerView itemsRecyclerView = findViewById(R.id.filter_recyclerView);
        itemsRecyclerView.setLayoutManager(UIHelper.setMainLinearManagers(this));
        itemsRecyclerView.setAdapter(new FilterSingleItemRecyclerAdapter(categoryList, this));



    }

    @Override
    public void customClickListener(Object... data) {
        if(!saveStateChanged) {
            saveText.setTextColor(getResources().getColor(R.color.colorHoverWhite));
            UIHelper.setTextUnderline(saveText, "Save");
            saveStateChanged = true;
        }
        String categoryClicked = (String) data[0];
        boolean checked = (boolean) data[1];
        if(checked) selectedCategories.add(categoryClicked);
        else if(selectedCategories.indexOf(categoryClicked) != -1) selectedCategories.remove(categoryClicked);
    }
}
