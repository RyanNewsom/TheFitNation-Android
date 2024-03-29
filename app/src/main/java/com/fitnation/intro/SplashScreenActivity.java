package com.fitnation.intro;

import android.content.Intent;
import android.os.Bundle;

import com.crashlytics.android.Crashlytics;
import com.fitnation.R;
import com.fitnation.base.BaseActivity;
import com.fitnation.login.LoginBaseActivity;

import io.fabric.sdk.android.Fabric;

/**
 * SplashScreen for branding and app loading
 */
public class SplashScreenActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_splash_screen);
    }

    @Override
    protected void onStart() {
        super.onStart();
        launchLoginScreen();
    }

    private void launchLoginScreen() {
        final Intent loginIntent = new Intent(this, LoginBaseActivity.class);
        startActivity(loginIntent);
        finish();
    }
}
