package com.hover.runner.ui.create;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Response;
import com.hover.runner.ApplicationInstance;
import com.hover.runner.R;
import com.hover.runner.enums.PassageEnum;
import com.hover.runner.models.HoverAction;
import com.hover.runner.utils.UIHelper;
import com.hover.runner.utils.network.NetworkUtil;
import com.hover.runner.utils.network.VolleyRunner;
import com.hover.sdk.api.ActionHelper;
import com.hover.sdk.api.Hover;
import com.hover.sdk.api.HoverParameters;
import com.hover.sdk.requests.TransactionSequenceService;
import com.hover.sdk.sims.SimInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ManualRunFragment extends Fragment {
    private final String TAG = "ManualRunFragment";

    List<SimInfo> sims;
    int selectedSlot = 0;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpView(view);
    }

    private void setUpView(View view) {
        createRadios(view);

        view.findViewById(R.id.startSession).setOnClickListener(v -> {
            if (((EditText) view.findViewById(R.id.root_code)).getText().toString().isEmpty())
                UIHelper.flashMessage(getContext(), "Please enter a valid ussd shortcode. eg *123#");
            else if (new NetworkUtil(getContext()).isNetworkAvailable() == PassageEnum.REJECT)
                UIHelper.flashMessage(getContext(), "Please go online to enable this.");
            else run();
        });
    }

    private void createRadios(View v) {
        ((RadioGroup) v.findViewById(R.id.chooseSim)).setOnCheckedChangeListener((group, checkedId) -> onSelect(checkedId));
        sims = Hover.getPresentSims(getContext());
        for (SimInfo sim: sims) {
            RadioButton radioButton = (RadioButton) LayoutInflater.from(getContext()).inflate(R.layout.radio_btn, null);
            radioButton.setId(sim.slotIdx);
            radioButton.setText(sim.getOperatorName());
            radioButton.setTag(sim.subscriptionId);
            radioButton.setChecked(sim.slotIdx == 0 || sims.size() == 1);

            ((RadioGroup) v.findViewById(R.id.chooseSim)).addView(radioButton);
        }
    }

    private void onSelect(int slotIdx) { selectedSlot = slotIdx; }

    private void run() {
        if (new NetworkUtil(getContext()).isNetworkAvailable() == PassageEnum.REJECT) {
            Log.e(TAG, "This only works when online.");
            UIHelper.flashMessage(getContext(), "This functionality only works when online.");
        } else {
            Log.e(TAG, "One moment, connecting...");
            UIHelper.flashMessage(getContext(), "One moment, connecting...");
            saveActionToServer();
        }
    }

    private String saveActionToServer() {
        HoverAction action = new HoverAction(((TextView) getView().findViewById(R.id.name)).getText().toString(), ((TextView) getView().findViewById(R.id.root_code)).getText().toString(), sims.get(selectedSlot).getOSReportedHni());
        try {
            Log.e(TAG, "Saving, please wait...");
            UIHelper.flashMessage(getContext(), "Saving, please wait...");
            VolleyRunner.uploadJsonNow(ApplicationInstance.getContext(), "actions", action.toJson(), listener);
        } catch (Exception e) {
            Log.e(TAG, "Failed to save, please try again.");
            UIHelper.flashMessage(getContext(), "Failed to save, please try again.");
        }
        return null;
    }

    private Response.Listener listener = (Response.Listener<JSONObject>) response -> {
        ActionHelper.updateActionConfigs(getContext());
        Log.d(TAG, response.toString());
        try {
            String actionId = response.getJSONObject("data").getJSONObject("attributes").getString("public_id");
            if (actionId != null) {
                new Handler().postDelayed(() -> runSession(actionId), 5000);
            }
        } catch (JSONException e) { Log.e(TAG, "Json fail", e); }
    };

    private void runSession(String actionId) {
        Log.e(TAG, "Saved, running session.");
        UIHelper.flashMessage(getContext(), "Saved, running session.");
        HoverParameters.Builder builder = new HoverParameters.Builder(getActivity());
        builder.request(actionId);
        builder.setSim(sims.get(selectedSlot).getOSReportedHni());
        builder.setEnvironment(HoverParameters.MANUAL_ENV);

        showStopNotification();
        startActivityForResult(builder.buildIntent(), 0);
    }

    private void showStopNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), "DEFAULT")
                .setSmallIcon(R.drawable.hoverlogo_svg)
                .setContentTitle("Recording USSD Session")
                .setContentText("Tap this notification to end session")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Tap this notification to end session"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_TRANSPORT)
                .setContentIntent(createStopIntent())
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());
        notificationManager.notify(0, builder.build());
    }

    private PendingIntent createStopIntent() {
        Intent i = new Intent(getContext(), TransactionSequenceService.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setAction(TransactionSequenceService.MENU_FINISH);
        return PendingIntent.getActivity(getContext(), 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(0);
    }
}
