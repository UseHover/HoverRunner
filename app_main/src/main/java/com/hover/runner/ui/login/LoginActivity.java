package com.hover.runner.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import android.transition.Fade;
import android.widget.ProgressBar;
import com.hover.runner.MainActivity;
import com.hover.runner.R;
import com.hover.runner.api.Apis;
import com.hover.runner.models.LoginModel;
import com.hover.runner.utils.UIHelper;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private ProgressBar loginProgress;
    private ImageView imageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        imageView = findViewById(R.id.hover_bg1);
        loginProgress = findViewById(R.id.login_progress);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        Fade fade = new Fade();
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        fade.excludeTarget(android.R.id.navigationBarBackground, true);

            getWindow().setEnterTransition(fade);
            getWindow().setExitTransition(fade);
        }

        new Handler().postDelayed(this::moveToLoginActivity2, 1500);

    }
    void moveToLoginActivity2() {
        Intent intent = new Intent(this, LoginActivity2.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            loginProgress.setVisibility(View.GONE);
            ImageView imageView2 = findViewById(R.id.iv1);
            Pair<View, String> sharedElement1= new Pair<>(imageView, Objects.requireNonNull(ViewCompat.getTransitionName(imageView)));
            Pair<View, String> sharedElement2= new Pair<>(imageView2, Objects.requireNonNull(ViewCompat.getTransitionName(imageView2)));

            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, sharedElement1, sharedElement2);
            startActivityForResult(intent, 200, activityOptionsCompat.toBundle());
        }
        else startActivityForResult(intent, 200);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
            if(resultCode == Activity.RESULT_OK){
                loginProgress.setIndeterminate(true);
                loginProgress.setVisibility(View.VISIBLE);
                new Handler().postDelayed(() -> {
                    String[] result = data.getStringArrayExtra("login_data");
                    if(result !=null && result.length ==2) {
                        LoginModel loginModel = new Apis().doLoginWorkManager(result[0], result[1]);

                        switch (loginModel.getStatus()) {
                            case ERROR:
                                UIHelper.showHoverToast(LoginActivity.this, getCurrentFocus(), loginModel.getMessage());
                                moveToLoginActivity2();
                                break;
                            case SUCCESS:
                                MainActivity.LoginYes = 1;
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finishAffinity();
                        }
                    }
                    else {
                        UIHelper.showHoverToast(LoginActivity.this, getCurrentFocus(), getResources().getString(R.string.somethingWentWrong));
                        moveToLoginActivity2();
                    }
                }, 500);


            }
            else {
                new Handler().postDelayed(this::moveToLoginActivity2, 1500);
            }
        }
    }
}
