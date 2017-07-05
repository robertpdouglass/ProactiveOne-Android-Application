package com.proactivesensing.bobbydouglass.proactiveone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Home extends AppCompatActivity {

    public static int Screen = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        Screen = -1;

        final String PREFS_NAME = "Preferences";
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        boolean firstTime = false;

        if (settings.getBoolean("firstTime", true)) {
            firstTime = true;
            settings.edit().putBoolean("firstTime", false).commit();
        }
        new Modbus(getApplicationContext(), firstTime);
    }

    public void readStatus(View view) {
        startActivity(new Intent(this, Read_Status.class));
    }

    public void configSensors(View view) {
        startActivity(new Intent(this, Configure_Sensors.class));
    }

    public void sysParameters(View view) {
        Screen = 4;
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
