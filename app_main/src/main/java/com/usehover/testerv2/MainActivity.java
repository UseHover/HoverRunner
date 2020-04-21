package com.usehover.testerv2;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hover.sdk.api.Hover;
import com.usehover.testerv2.api.Apis;
import com.usehover.testerv2.enums.PassageEnum;
import com.usehover.testerv2.ui.login.LoginActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

	public static int LoginYes = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		Hover.initialize(this);
			if(LoginYes == 0) {
				if (new Apis().allowIntoMainActivity() == PassageEnum.REJECT) {
					startActivity(new Intent(this, LoginActivity.class));
					finish();
				}
			}

		setContentView(R.layout.activity_main);

		BottomNavigationView navView = findViewById(R.id.nav_view);
		NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
		NavigationUI.setupWithNavController(navView, navController);

		if(getIntent().getExtras() !=null) {
			if(getIntent().getExtras().get("navigate") !=null) navController.navigate(R.id.navigation_transactions);
		}

	}

}
