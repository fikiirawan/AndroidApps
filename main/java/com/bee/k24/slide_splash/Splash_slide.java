package com.bee.k24.slide_splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.view.Window;
import android.view.WindowManager;
import com.bee.k24.R;
import com.bee.k24.services.Home;

public class Splash_slide extends AppCompatActivity implements Loading.LoadingTaskFinishedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressbar_Horizontal);
        new Loading(progressBar, this).execute("");

    }

    public void onTaskFinished() {
        completeSplash();
    }
    private void completeSplash(){
        startApp();
        finish(); // Don't forget to finish this Splash Activity so the user can't return to it!
    }
    private void startApp() {
        Intent intent = new Intent(Splash_slide.this, Home.class);
        startActivity(intent);
        finish();
    }

}