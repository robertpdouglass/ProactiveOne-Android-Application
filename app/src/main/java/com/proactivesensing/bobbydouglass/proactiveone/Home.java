package com.proactivesensing.bobbydouglass.proactiveone;

import android.animation.LayoutTransition;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
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
    public static SharedPreferences settings;
    NfcAdapter nfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Screen = 0;

        final String PREFS_NAME = "Preferences";
        settings = getSharedPreferences(PREFS_NAME, 0);
        boolean firstTime = false;
        if (settings.getBoolean("firstTime", true)) {
            firstTime = true;
            settings.edit().putBoolean("firstTime", false).commit();
            settings.edit().putString("table_name", Modbus.TABLE_NAME);
        }
        else if(settings.getString("table_name","").length() < 3) {
            settings.edit().putString("table_name", Modbus.TABLE_NAME);
        }
        new Modbus(getApplicationContext(), ((firstTime) ? 0 : 2));

        nfcAdapter = nfcAdapter.getDefaultAdapter(this);
        if(!nfcAdapter.isEnabled()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("NFC Disabled, Please Enable NFC In Settings");
            builder.setPositiveButton("Settings", dialogClickListener);
            builder.setNegativeButton("Cancel", dialogClickListener);
            builder.show();
        }

        setContentView(R.layout.home);
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    openSettings();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    onBackPressed();
                    break;
            }
        }
    };

    public void openSettings() {
        Intent setnfc = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
        startActivity(setnfc);
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

    public String getOldTableName() {
        return settings.getString("table_name","");
    }

    public void setOldTableName(String str) {
        settings.edit().putString("table_name", str);
    }
}
