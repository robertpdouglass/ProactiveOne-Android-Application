package com.proactivesensing.bobbydouglass.proactiveone;

import android.animation.LayoutTransition;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Sensors_I2C extends AppCompatActivity {

    public static final int Size =                  44;
    public static final int SensorSize =            11;
    public static final int SensorCount =           4;
    public static final int ViewSize =              8;

    public static int[][] changes =                 new int[SensorCount][SensorSize];
    public static boolean[][] changes_bool =        new boolean[SensorCount][SensorSize];
    public static int[][] addresses =             {{1300,   1301,   1302,   1303,   1304,   1305,   1306,   1307,
                                                    1308,   1309,   1310}, {1311,   1312,   1313,   1314,   1315,
                                                    1316,   1317,   1318,   1319,   1320,   1321}, {1322,   1323,
                                                    1324,   1325,   1326,   1327,   1328,   1329,   1330,   1331,
                                                    1332}, {1333,   1334,   1335,   1336,   1337,   1338,   1339,
                                                    1340,   1341,   1342,   1343}};

    LinearLayout[] layoutToAdd =                    new LinearLayout[ViewSize];
    View[] viewToInflate =                          new View[ViewSize];
    boolean[] clicked =                            {false,  false,  false,  false,  false,  false,  false,  false};
    int[][] lowBytes =                              new int[4][3];
    int[][] highBytes =                             new int[4][3];
    int sensorNum =                                 0;
    boolean changes_made =                          false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensors_i2c);

        for(int i = 0; i < SensorCount; i++) {
            for(int j = 0; j < SensorSize; j++) {
                changes[i][j] = new Modbus(getApplicationContext(), addresses[i][j]).getValue();
                changes_bool[i][j] = false;
            }
        }

        for(int i = 0; i < SensorCount; i++) {
            for(int j = 0; j < 3; j++) {
                lowBytes[i][j] = ((changes[i][j] >> 8) & 0x00ff);
                highBytes[i][j] = (changes[i][j] & 0x00ff);
            }
        }

        for(int i = 0; i < SensorCount; i++) {
            //for(int j = 0; j < 5; j++)
                //loadedSpinner[i][j] = false;
        }

        layoutToAdd[0] = (LinearLayout) findViewById(R.id.i2c_option1_expansion);
        layoutToAdd[1] = (LinearLayout) findViewById(R.id.i2c_option2_expansion);
        layoutToAdd[2] = (LinearLayout) findViewById(R.id.i2c_option3_expansion);
        layoutToAdd[3] = (LinearLayout) findViewById(R.id.i2c_option4_expansion);
        layoutToAdd[4] = (LinearLayout) findViewById(R.id.i2c_option5_expansion);
        layoutToAdd[5] = (LinearLayout) findViewById(R.id.i2c_option6_expansion);
        layoutToAdd[6] = (LinearLayout) findViewById(R.id.i2c_option7_expansion);
        layoutToAdd[7] = (LinearLayout) findViewById(R.id.i2c_option8_expansion);

        sensorSelection();
    }

    public void onBackPressed() {
        if(changes_made) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Changes Made, Save Changes?");
            builder.setPositiveButton("Save", changesDialog);
            builder.setNegativeButton("Discard", changesDialog);
            builder.show();
        }
        else {
            Intent Conf = new Intent(this, Configure_Sensors.class);
            Conf.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(Conf);
        }
    }

    DialogInterface.OnClickListener changesDialog = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    startNfc();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    startConfigure();
                    break;
            }
        }
    };

    public void startNfc() {
        Intent NFC = new Intent(this, Nfc.class);
        NFC.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(NFC);
    }

    public void startConfigure() {
        Intent Conf = new Intent(this, Configure_Sensors.class);
        Conf.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(Conf);
    }

    public void restore(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are You Sure You Want To Restore Default Values?  This Will Overwrite All Values.");
        builder.setPositiveButton("Restore", restoreDialog);
        builder.setNegativeButton("Cancel", restoreDialog);
        builder.show();
    }

    DialogInterface.OnClickListener restoreDialog = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    toast();
                    Home.Screen = -1;
                    new Modbus(getApplicationContext(), true);
                    startNfc();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        }
    };

    public void toast() {
        Toast.makeText(this, "Please Wait...", Toast.LENGTH_SHORT).show();
    }

    public void sensorSelection() {
        Button i2c1 = (Button) findViewById(R.id.i2c1);
        Button i2c2 = (Button) findViewById(R.id.i2c2);
        Button i2c3 = (Button) findViewById(R.id.i2c3);
        Button i2c4 = (Button) findViewById(R.id.i2c4);
        Button selectedSensor = null;
        Button[] offSensors = new Button[3];

        switch(sensorNum) {
            case 0:
                offSensors = new Button[]{i2c2, i2c3, i2c4};
                selectedSensor = i2c1;
                break;
            case 1:
                offSensors = new Button[]{i2c1, i2c3, i2c4};
                selectedSensor = i2c2;
                break;
            case 2:
                offSensors = new Button[]{i2c1, i2c2, i2c4};
                selectedSensor = i2c3;
                break;
            case 3:
                offSensors = new Button[]{i2c1, i2c2, i2c3};
                selectedSensor = i2c4;
                break;
            default:
                Log.e("ERROR", "INVALID SENSORNUM");
                break;
        }

        for(int i = 0; i < 3; i++) {
            if (Build.VERSION.SDK_INT >= 22)
                offSensors[i].setBackground(getDrawable(R.drawable.material_button));
            else
                offSensors[i].setBackground(getResources().getDrawable(R.drawable.material_button));
        }

        if (Build.VERSION.SDK_INT >= 22)
            selectedSensor.setBackground(getDrawable(R.drawable.material_button_blue));
        else
            selectedSensor.setBackground(getResources().getDrawable(R.drawable.material_button_blue));
    }

    public void I2C1(View view) {
        sensorNum = 0;
        sensorSelection();
    }

    public void I2C2(View view) {
        sensorNum = 1;
        sensorSelection();
    }

    public void I2C3(View view) {
        sensorNum = 2;
        sensorSelection();
    }

    public void I2C4(View view) {
        sensorNum = 3;
        sensorSelection();
    }

    public void I2COption1(View view) {
        final int i = 0, j = 0;
        if(!clicked[i]) {
            clicked[i] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[i] = inflater.inflate(R.layout.z_i2c_01_enable_child, null);
            layoutToAdd[i].addView(viewToInflate[i]);

            ToggleButton enable = (ToggleButton) findViewById(R.id.i2c_enable_toggle);
            enable.setChecked(lowBytes[sensorNum][j] == 1);
            enable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    lowBytes[sensorNum][j] = (isChecked) ? 1 : 0;
                    changes[sensorNum][j] = changes[sensorNum][j] = ((lowBytes[sensorNum][j] & 0x00ff) << 8) | ((highBytes[sensorNum][j] & 0x00ff));
                    changes_bool[sensorNum][j] = true;
                    changes_made = true;
                }
            });

            Spinner dropdown = (Spinner) findViewById(R.id.i2c_library_spinner);
            String[] items = new String[]{"Sensor 1", "Sensor 2", "Sensor 3", "Temp Sensor", "Accel Sensor"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.style_spinner_items, items);
            dropdown.setAdapter(adapter);
            dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //if(loadedSpinner[sensorNum][0]) {
                        //config_low[sensorNum] = position;
                        //changes[sensorNum][j] = ((config_low[sensorNum] & 0x00ff) << 8) | ((config_high[sensorNum] & 0x00ff));
                        //changes_made = true;
                        //changes_bool[sensorNum][j] = true;
                    //}
                    //else {
                        //loadedSpinner[sensorNum][0] = true;
                    //}/
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
            //dropdown_low.setSelection(config_low[sensorNum]);
        }
        else {
            layoutToAdd[i].removeView(viewToInflate[i]);
            clicked[i] = false;
        }
    }

    public void I2COption2(View view) {
        final int i = 1, j = 1;
        if(!clicked[i]) {
            clicked[i] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[i] = inflater.inflate(R.layout.z_i2c_02_power_child, null);
            layoutToAdd[i].addView(viewToInflate[i]);

        }
        else {
            layoutToAdd[i].removeView(viewToInflate[i]);
            clicked[i] = false;
        }
    }

    public void I2COption3(View view) {
        final int i = 2, j = 2;
        if(!clicked[i]) {
            clicked[i] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[i] = inflater.inflate(R.layout.z_i2c_03_inter_child, null);
            layoutToAdd[i].addView(viewToInflate[i]);

        }
        else {
            layoutToAdd[i].removeView(viewToInflate[i]);
            clicked[i] = false;
        }
    }

    public void I2COption4(View view) {
        final int i = 3;
        if(!clicked[i]) {
            clicked[i] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[i] = inflater.inflate(R.layout.z_i2c_04_polling_child, null);
            layoutToAdd[i].addView(viewToInflate[i]);

        }
        else {
            layoutToAdd[i].removeView(viewToInflate[i]);
            clicked[i] = false;
        }
    }

    public void I2COption5(View view) {
        final int i = 4;
        if(!clicked[i]) {
            clicked[i] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[i] = inflater.inflate(R.layout.z_i2c_05_address_child, null);
            layoutToAdd[i].addView(viewToInflate[i]);

        }
        else {
            layoutToAdd[i].removeView(viewToInflate[i]);
            clicked[i] = false;
        }
    }

    public void I2COption6(View view) {
        final int i = 5;
        if(!clicked[i]) {
            clicked[i] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[i] = inflater.inflate(R.layout.z_i2c_06_cal_child, null);
            layoutToAdd[i].addView(viewToInflate[i]);

        }
        else {
            layoutToAdd[i].removeView(viewToInflate[i]);
            clicked[i] = false;
        }
    }

    public void I2COption7(View view) {
        final int i = 6;
        if(!clicked[i]) {
            clicked[i] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[i] = inflater.inflate(R.layout.z_i2c_07_multi_child, null);
            layoutToAdd[i].addView(viewToInflate[i]);

        }
        else {
            layoutToAdd[i].removeView(viewToInflate[i]);
            clicked[i] = false;
        }
    }

    public void I2COption8(View view) {
        final int i = 7;
        if(!clicked[i]) {
            clicked[i] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[i] = inflater.inflate(R.layout.z_i2c_08_limits_child, null);
            layoutToAdd[i].addView(viewToInflate[i]);

        }
        else {
            layoutToAdd[i].removeView(viewToInflate[i]);
            clicked[i] = false;
        }
    }
}
