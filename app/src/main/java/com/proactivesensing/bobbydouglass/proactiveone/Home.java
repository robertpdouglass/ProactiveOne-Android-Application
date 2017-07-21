package com.proactivesensing.bobbydouglass.proactiveone;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
        if(!loaded) {
            setContentView(R.layout.splash_screen);

            final String PREFS_NAME = "Preferences";
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            boolean firstTime = false;
            if (settings.getBoolean("firstTime", true)) {
                firstTime = true;
                settings.edit().putBoolean("firstTime", false).commit();
            }
            new Modbus(getApplicationContext(), firstTime);

            loadAdvancedCommand();
            loadAdvancedData();
            loadSensorsExternal();
            loadSensorsI2C();
            loadSensorsInternal();
            loadSystemParameters();
            loaded = true;
        }
        else {
            loadAdvancedCommand();
            loadAdvancedData();
            loadSensorsExternal();
            loadSensorsI2C();
            loadSensorsInternal();
            loadSystemParameters();
        }
        setContentView(R.layout.home);
    }

    public void loadAdvancedCommand() {

    }

    public void loadAdvancedData() {
        for (int i = 0; i < Advanced_Data.Size; i++) {
            Advanced_Data.changes[i] = new Modbus(getApplicationContext(), Advanced_Data.addresses[i]).getValue();
            Advanced_Data.changes_low[i] = ((Advanced_Data.changes[i] >> 8) & 0x00ff);
            Advanced_Data.changes_high[i] = (Advanced_Data.changes[i] & 0x00ff);
        }
    }

    public void loadSensorsExternal() {
        for (int i = 0; i < Sensors_External.SensorCount; i++) {
            for (int j = 0; j < Sensors_External.SensorChanges; j++)
                Sensors_External.changes[i][j] = new Modbus(getApplicationContext(), Sensors_External.addresses[i][j]).getValue();
        }

        for(int i = 0; i < Sensors_External.SensorCount; i++) {
            Sensors_External.config_low[i] = ((Sensors_External.changes[i][0] >> 8) & 0x00ff);
            Sensors_External.config_high[i] = (Sensors_External.changes[i][0] & 0x00ff);
        }
    }

    public void loadSensorsI2C() {

    }

    public void loadSensorsInternal() {
        for (int i = 0; i < Sensors_Internal.MainCount; i++) {
            for(int j = 0; j < Sensors_Internal.ChangesSize; j++)
                Sensors_Internal.changes[i][j] = new Modbus(getApplicationContext(), Sensors_Internal.addresses[i][j]).getValue();
        }
    }

    public void loadSystemParameters() {
        for(int i = 0; i < System_Parameters.Size; i++)
            System_Parameters.changes[i] = new Modbus(getApplicationContext(), System_Parameters.addresses[i]).getValue();
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
