package com.hover.runner;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hover.runner.R;
import com.hover.sdk.api.Hover;
import com.hover.sdk.permissions.PermissionActivity;
import com.hover.runner.api.Apis;
import com.hover.runner.enums.PassageEnum;
import com.hover.runner.ui.login.LoginActivity;
import com.hover.runner.utils.PermissionHelper;
import com.hover.runner.utils.UIHelper;
import com.hover.runner.utils.Utils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

	public static int LoginYes = 0;
	public static String initialActionFilter;
	private final int PERMISSION_REQ_CODE = 201;
	final String permission_acceptance_incomplete = "You did not allow all permissions";
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		if(LoginYes == 0) {
			if (new Apis().allowIntoMainActivity() == PassageEnum.REJECT) {
				startActivity(new Intent(this, LoginActivity.class));
				finish();
				return;
			}
		}
		//Test keys
		Hover.initialize(this, Utils.getAppApiKey(this));
		Hover.setBranding("Runner by Hover", R.drawable.ic_runner_logo, this);
		if(!new PermissionHelper(this, new String[]{ Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE}).hasPermissions()) {
			startActivityForResult(new Intent(this, PermissionActivity.class), PERMISSION_REQ_CODE);
		}

		setContentView(R.layout.activity_main);

		BottomNavigationView navView = findViewById(R.id.nav_view);
		NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
		NavigationUI.setupWithNavController(navView, navController);

		if(getIntent().getExtras() !=null) {
			if(getIntent().getExtras().getString("navigate") !=null) {
				initialActionFilter = getIntent().getExtras().getString("navigate");
				navController.navigate(R.id.navigation_transactions);
			}
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == PERMISSION_REQ_CODE) {
			if(resultCode != RESULT_OK) {
				UIHelper.showHoverToast(this, getCurrentFocus(), permission_acceptance_incomplete);
			}
		}
	}

}
