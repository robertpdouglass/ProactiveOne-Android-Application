package com.proactivesensing.bobbydouglass.proactiveone;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class Home extends AppCompatActivity {

    public static int Screen =      0;
    public static boolean loaded =  false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Screen = 0;
        setContentView(R.layout.splash_screen);

        final String PREFS_NAME = "Preferences";
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        boolean firstTime = false;
        if (settings.getBoolean("firstTime", true)) {
            firstTime = true;
            settings.edit().putBoolean("firstTime", false).commit();
        }
        new Modbus(getApplicationContext(), ((firstTime) ? 0 : 2));

        setContentView(R.layout.home);
    }

    public void readStatus(View view) {
        startActivity(new Intent(this, Read_Status.class));
    }

    public void configSensors(View view) {
        startActivity(new Intent(this, Configure_Sensors.class));
    }

    public void sysParameters(View view) {
        Screen = 3;
        startActivity(new Intent(this, System_Parameters.class));
    }

    public void advanced(View view) {
        startActivity(new Intent(this, Advanced.class));
    }

    @Override
    public void onBackPressed() {
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }
}
