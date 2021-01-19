package com.hover.runner.settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.hover.runner.MainActivity;
import com.hover.runner.ui.login.LoginActivity;
import com.hover.sdk.actions.HoverAction;
import com.hover.sdk.api.Hover;
import com.hover.runner.ApplicationInstance;
import com.hover.runner.R;
import com.hover.runner.api.Apis;
import com.hover.runner.utils.UIHelper;

import java.util.ArrayList;

public class SettingsFragment extends Fragment implements Hover.DownloadListener {

	private SettingsViewModel settingsViewModel;
	private RadioGroup radioGroup;
	private boolean refreshButtonIdle = false;

	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.settings_fragment, container, false);

		initEnvRadio(root);
		loadSims(root);
		colorHelpText(root.findViewById(R.id.contact_support));
		root.findViewById(R.id.refreshButton).setOnClickListener(v -> confirmRefresh());

		((TextView) root.findViewById(R.id.email)).setText(SettingsHelper.getEmail(getActivity()));
		((TextView) root.findViewById(R.id.packageName)).setText(SettingsHelper.getPackage(getActivity()));
		((TextView) root.findViewById(R.id.apiKey)).setText(SettingsHelper.getApiKey(getActivity()));
		((TextView) root.findViewById(R.id.delayInput)).setText(String.valueOf(SettingsHelper.getDelay(getActivity())));

		((TextView) root.findViewById(R.id.delayInput)).addTextChangedListener(delayWatcher);
		root.findViewById(R.id.signOutButton).setOnClickListener(v -> confirmSignOut());

		return root;
	}

	private TextWatcher delayWatcher = new TextWatcher() {
		@Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
		@Override public void afterTextChanged(Editable editable) { }
		@Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
			if (charSequence != null && charSequence.length() > 0)
				SettingsHelper.setDelay(Integer.parseInt(charSequence.toString()), getContext());
		}
	};

	private void loadSims(View root) {
		settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
		settingsViewModel.loadSimsObs().observe(getViewLifecycleOwner(), simData-> {
			String emptySim = getResources().getString(R.string.no_sim_found);
			((TextView) root.findViewById(R.id.sim1_content)).setText(simData.getSim1Name() != null ? simData.getSim1Name() : emptySim);
			((TextView) root.findViewById(R.id.sim2_content)).setText(simData.getSim2Name() != null ? simData.getSim2Name() : emptySim);
		});
		settingsViewModel.getSims();
	}

	private void initEnvRadio(View root) {
		radioGroup = root.findViewById(R.id.envRadioGroup);
		if (SettingsHelper.getCurrentEnv() == Apis.PROD_ENV) radioGroup.check(R.id.mode_normal);
		else if(SettingsHelper.getCurrentEnv() == Apis.DEBUG_ENV) radioGroup.check(R.id.mode_debug);
		else radioGroup.check(R.id.mode_noSim);
		radioGroup.setOnCheckedChangeListener((group, checkedId) -> SettingsHelper.chooseEnv(checkedId));
	}

	private void colorHelpText(TextView contactSupport) {
		Spanned sp = HtmlCompat.fromHtml(getString(R.string.contactUs), HtmlCompat.FROM_HTML_OPTION_USE_CSS_COLORS);
		contactSupport.setText(sp);
		contactSupport.setMovementMethod(LinkMovementMethod.getInstance());
	}

	private void confirmRefresh() {
		try {
			AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
			builder.setTitle("App refresh");
			builder.setMessage("Refreshing your app data will delete all cached entries. Are you sure you want continue?");
			builder.setPositiveButton("Refresh", (dialog, which) -> refresh(dialog));
			builder.setNegativeButton("Cancel", (dialog, which) -> {
				dialog.dismiss();
				dialog.cancel();
			});
			builder.show();
		} catch (Exception ignored){}
	}

	private void refresh(DialogInterface dialog) {
		if (!refreshButtonIdle) {
			dialog.dismiss();
			dialog.cancel();

			refreshButtonIdle = true;
			Hover.updateActionConfigs(this, (getContext() != null) ? getContext() : ApplicationInstance.getContext()) ;
			UIHelper.flashMessage(getContext(), getResources().getString(R.string.app_data_refreshed));
		}
	}

	private void confirmSignOut() {
		try {
			AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
			builder.setTitle("Sign out");
			builder.setMessage("Are you sure you want to sign out of Runner app?");
			builder.setPositiveButton("Yes", (dialog, which) -> {
				signOut();
			});
			builder.setNegativeButton("No", (dialog, which)-> {
				dialog.dismiss();
				dialog.cancel();
			});
			builder.show();
		} catch (Exception ignored){}
	}

	private void signOut() {
		MainActivity.LoginYes = 0;
		SettingsHelper.clearData(getContext());
		startActivity(new Intent(getContext(), LoginActivity.class));
		if (getActivity() !=null) getActivity().finishAffinity();
	}

	@Override
	public void onError(String message) {
		refreshButtonIdle = false;
		UIHelper.flashMessage(getContext(), message);
	}

	@Override
	public void onSuccess(ArrayList<HoverAction> actions) {
		refreshButtonIdle = false;
	}
}
