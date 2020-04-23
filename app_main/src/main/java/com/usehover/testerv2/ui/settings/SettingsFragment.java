package com.usehover.testerv2.ui.settings;

import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.hover.sdk.actions.HoverAction;
import com.hover.sdk.api.Hover;
import com.usehover.testerv2.ApplicationInstance;
import com.usehover.testerv2.R;
import com.usehover.testerv2.api.Apis;
import com.usehover.testerv2.utils.UIHelper;

import java.util.ArrayList;

public class SettingsFragment extends Fragment implements Hover.DownloadListener {

	private SettingsViewModel settingsViewModel;
	private RadioGroup radioGroup;
	private boolean refreshButtonIdle = false;

	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
		View root = inflater.inflate(R.layout.fragment_settings, container, false);
		radioGroup = root.findViewById(R.id.myRadioGroup);
		TextView sim1 = root.findViewById(R.id.sim1_content);
		TextView sim2 = root.findViewById(R.id.sim2_content);

		TextView contactSupport = root.findViewById(R.id.contact_support);
		Spanned sp = HtmlCompat.fromHtml(getString(R.string.contactUs), HtmlCompat.FROM_HTML_OPTION_USE_CSS_COLORS);
		contactSupport.setText(sp);
		contactSupport.setMovementMethod(LinkMovementMethod.getInstance());
		radioGroup.setOnCheckedChangeListener((group, checkedId) -> settingsViewModel.updateMode(checkedId));

		settingsViewModel.loadCurrentModeObs().observe(getViewLifecycleOwner(), mode-> {
			if(mode == Apis.PROD_ENV) radioGroup.check(R.id.mode_normal);
			else if(mode == Apis.DEBUG_ENV) radioGroup.check(R.id.mode_debug);
			else radioGroup.check(R.id.mode_noSim);
		});

		settingsViewModel.loadSimsObs().observe(getViewLifecycleOwner(), simData-> {
			String emptySim = getResources().getString(R.string.no_sim_found);
			sim1.setText(simData.getSim1Name() !=null ? simData.getSim1Name() : emptySim);
			sim2.setText(simData.getSim2Name() !=null ? simData.getSim2Name() : emptySim);
		});

		root.findViewById(R.id.refreshButton).setOnClickListener(v -> {
			refreshButtonIdle = true;
			Hover.updateActionConfigs(this, (getContext() != null) ? getContext() : ApplicationInstance.getContext()) ;
			UIHelper.showHoverToastV2(getContext(), getResources().getString(R.string.app_data_refreshed));
		});
		settingsViewModel.getSims();

		return root;
	}

	@Override
	public void onError(String message) {
		refreshButtonIdle = false;
	UIHelper.showHoverToast(getContext(), getActivity().getCurrentFocus(), message);
	}

	@Override
	public void onSuccess(ArrayList<HoverAction> actions) {
		refreshButtonIdle = false;
	}
}
