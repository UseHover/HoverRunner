package com.usehover.runner.ui.filter_pages;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.usehover.runner.ApplicationInstance;
import com.usehover.runner.R;
import com.usehover.runner.adapters.WithSubtitleRecyclerAdapter;
import com.usehover.runner.interfaces.CustomOnClickListener;
import com.usehover.runner.models.WithSubtitleFilterInfoModel;
import com.usehover.runner.utils.UIHelper;

import java.util.ArrayList;

public class FilterByActions extends AppCompatActivity implements CustomOnClickListener {
    private ArrayList<WithSubtitleFilterInfoModel> actionList = new ArrayList<>();
    private ArrayList<String> selectedActions = new ArrayList<>();
    private boolean saveStateChanged = false;
    private TextView saveText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_by_actions);
        if(getIntent().getExtras() !=null) actionList = getIntent().getExtras().getParcelableArrayList("data");

        assert actionList != null;
        if(actionList.size() > 0 ) {
            for(int i=0; i<actionList.size(); i++) {
                if(actionList.get(i).isCheck()) selectedActions.add(actionList.get(i).getTitle());
            }
        }
        findViewById(R.id.filter_by_action_title).setOnClickListener(v-> finish());

        saveText = findViewById(R.id.filter_save_id);
        saveText.setOnClickListener(v->{
            if(saveStateChanged) {
                ApplicationInstance.setTransactionActionsSelectedFilter(selectedActions);
                finish();
            }

        });

        RecyclerView itemsRecyclerView = findViewById(R.id.filter_recyclerView);
        itemsRecyclerView.setLayoutManager(UIHelper.setMainLinearManagers(this));
        itemsRecyclerView.setHasFixedSize(true);
        itemsRecyclerView.setAdapter(new WithSubtitleRecyclerAdapter(actionList, this));
    }

    @Override
    public void customClickListener(Object... data) {
        if(!saveStateChanged) {
            saveText.setTextColor(getResources().getColor(R.color.colorHoverWhite));
            UIHelper.setTextUnderline(saveText, "Save");
            saveStateChanged = true;
        }

        String actionClicked = (String) data[0];
        boolean checked = (boolean) data[1];
        if(checked) selectedActions.add(actionClicked);
        else if(selectedActions.indexOf(actionClicked) != -1) selectedActions.remove(actionClicked);
    }
}
