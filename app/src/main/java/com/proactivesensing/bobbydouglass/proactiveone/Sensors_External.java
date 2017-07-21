/* ************************************************************************************** */
/* ***** NEED TO CHECK THAT THE LISTENERS FOLLOW THE CURRENT SELECTED SENSOR NUMBER ***** */
/* ************************************************************************************** */

package com.proactivesensing.bobbydouglass.proactiveone;

import android.animation.LayoutTransition;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Sensors_External extends AppCompatActivity {

    public static final int Size =                  56;
    public static final int SensorChanges =         14;
    public static final int SensorCount =           4;
    public static final int ViewSize =              6;

    public static LinearLayout[] layoutToAdd =      new LinearLayout[ViewSize];
    public static View[] viewToInflate =            new View[ViewSize];
    public static boolean[] clicked =              {false,  false,  false,  false,  false,  false};
    public static int[][] changes =                 new int[SensorCount][SensorChanges];
    public static boolean[][] changes_bool =      {{false,  false,  false,  false,  false,  false,  false,  false,
                                                    false,  false,  false,  false,  false,  false},{false,  false,
                                                    false,  false,  false,  false,  false,  false,  false,  false,
                                                    false,  false,  false,  false},{false,  false,  false,  false,
                                                    false,  false,  false,  false,  false,  false,  false,  false,
                                                    false,  false},{false,  false,  false,  false,  false,  false,
                                                    false,  false,  false,  false,  false,  false,  false,  false}};
    public static int[][] addresses =             {{1100,   1101,   1102,   1103,   1104,   1105,   1106,   1107,
                                                    1108,   1109,   1110,   1111,   1112,   1113}, {1114,   1115,
                                                    1116,   1117,   1118,   1119,   1120,   1121,   1122,   1123,
                                                    1124,   1125,   1126,   1127}, {1128,   1129,   1130,   1131,
                                                    1132,   1133,   1134,   1135,   1136,   1137,   1138,   1139,
                                                    1140,   1141}, {1142,   1143,   1144,   1145,   1146,   1147,
                                                    1148,   1149,   1150,   1151,   1152,   1153,   1154,   1155}};
    public static int config_low[] = new int[SensorCount];
    public static int config_high[] = new int[SensorCount];

    boolean[][] loadedSpinner =                   {{false,  false,  false},{false,  false,  false},{false,  false,
                                                    false},{false,  false,  false}};
    boolean changes_made = false;
    int sensorNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensors_external);

        layoutToAdd[0] = (LinearLayout) findViewById(R.id.config_expansion);
        layoutToAdd[1] = (LinearLayout) findViewById(R.id.alarm_recog_expansion);
        layoutToAdd[2] = (LinearLayout) findViewById(R.id.values_expansion);
        layoutToAdd[3] = (LinearLayout) findViewById(R.id.calibration_expansion);
        layoutToAdd[4] = (LinearLayout) findViewById(R.id.multiplier_expansion);
        layoutToAdd[5] = (LinearLayout) findViewById(R.id.alarm_limits_expansion);

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
        Button sensor0 = (Button) findViewById(R.id.button1);
        Button sensor1 = (Button) findViewById(R.id.button2);
        Button sensor2 = (Button) findViewById(R.id.button3);
        Button sensor3 = (Button) findViewById(R.id.button4);
        Button selectedSensor = null;
        Button[] offSensors = new Button[3];

        switch(sensorNum) {
            case 0:
                offSensors = new Button[]{sensor1, sensor2, sensor3};
                selectedSensor = sensor0;
                break;
            case 1:
                offSensors = new Button[]{sensor0, sensor2, sensor3};
                selectedSensor = sensor1;
                break;
            case 2:
                offSensors = new Button[]{sensor0, sensor1, sensor3};
                selectedSensor = sensor2;
                break;
            case 3:
                offSensors = new Button[]{sensor0, sensor1, sensor2};
                selectedSensor = sensor3;
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

    public void sensor1(View view) {
        sensorNum = 0;
        sensorSelection();
    }

    public void sensor2(View view) {
        sensorNum = 1;
        sensorSelection();
    }

    public void sensor3(View view) {
        sensorNum = 2;
        sensorSelection();
    }

    public void sensor4(View view) {
        sensorNum = 3;
        sensorSelection();
    }

    public void configuration(View view) {
        final int i = 0, j = 0;
        if(!clicked[i]) {
            clicked[i] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[i] = inflater.inflate(R.layout.z_xsen_01_config_child, null);
            layoutToAdd[i].addView(viewToInflate[i]);

            Spinner dropdown_low = (Spinner) findViewById(R.id.xsen_config_spinner);
            String[] items_low = new String[]{"Disabled", "Normally Open", "Normally Closed", "Pulse", "Resistive", "4-20mA", "Voltage"};
            ArrayAdapter<String> adapter_low = new ArrayAdapter<String>(this, R.layout.style_spinner_items, items_low);
            dropdown_low.setAdapter(adapter_low);
            dropdown_low.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(loadedSpinner[sensorNum][0]) {
                        config_low[sensorNum] = position;
                        changes[sensorNum][j] = ((config_low[sensorNum] & 0x00ff) << 8) | ((config_high[sensorNum] & 0x00ff));
                        changes_made = true;
                        changes_bool[sensorNum][j] = true;
                    }
                    else {
                        loadedSpinner[sensorNum][0] = true;
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
            dropdown_low.setSelection(config_low[sensorNum]);

            Spinner dropdown_high = (Spinner) findViewById(R.id.xsen_power_spinner);
            String[] items_high = new String[]{"None", "3V3 #1", "3V3 #2", "15V"};
            ArrayAdapter<String> adapter_high = new ArrayAdapter<String>(this, R.layout.style_spinner_items, items_high);
            dropdown_high.setAdapter(adapter_high);
            dropdown_high.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(loadedSpinner[sensorNum][1]) {
                        config_high[sensorNum] = position;
                        changes[sensorNum][j] = ((config_low[sensorNum] & 0x00ff) << 8) | ((config_high[sensorNum] & 0x00ff));
                        changes_made = true;
                        changes_bool[sensorNum][j] = true;
                    }
                    else {
                        loadedSpinner[sensorNum][1] = true;
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
            dropdown_high.setSelection(config_high[sensorNum]);
        }
        else {
            clicked[i] = false;
            layoutToAdd[i].removeView(viewToInflate[i]);
        }
    }

    public void alarmRecog(View view) {
        final int i = 1, j = 1;
        if(!clicked[i]) {
            clicked[i] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[i] = inflater.inflate(R.layout.z_xsen_02_recog_child, null);
            layoutToAdd[i].addView(viewToInflate[i]);

            final EditText recog = (EditText) findViewById(R.id.xsen_recog_edittext);
            recog.setText("" + changes[sensorNum][j]);
            recog.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {}
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() != 0) {
                        changes[sensorNum][j] = Integer.parseInt(s.toString());
                        changes_made = true;
                        changes_bool[sensorNum][j] = true;
                    }
                }
            });

            final Button[] button =    {(Button) findViewById(R.id.xsen_recog_neg),
                                        (Button) findViewById(R.id.xsen_recog_pos)};
            for(int l = 0; l < 2; l++) {
                final int k = l;
                button[j].setOnTouchListener(new Button.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                if (Build.VERSION.SDK_INT >= 22)
                                    button[k].setBackground(getDrawable(R.drawable.material_button_blue));
                                else
                                    button[k].setBackground(getResources().getDrawable(R.drawable.material_button_blue));

                                changes_made = true;
                                changes_bool[sensorNum][j] = true;
                                if(k == 0)
                                    changes[sensorNum][j]--;
                                else
                                    changes[sensorNum][j]++;
                                recog.setText("" + changes[sensorNum][i]);
                                break;
                            case MotionEvent.ACTION_UP:
                                if (Build.VERSION.SDK_INT >= 22)
                                    button[k].setBackground(getDrawable(R.drawable.material_button));
                                else
                                    button[k].setBackground(getResources().getDrawable(R.drawable.material_button));
                                break;
                        }
                        return false;
                    }
                });
            }
        }
        else {
            clicked[i] = true;
            layoutToAdd[i].removeView(viewToInflate[i]);
        }
    }

    public void values(View view) {
        final int i = 2, j = 2;
        if(!clicked[i]) {
            clicked[i] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[i] = inflater.inflate(R.layout.z_xsen_03_values_child, null);
            layoutToAdd[2].addView(viewToInflate[2]);

            final EditText[] values =  {(EditText) findViewById(R.id.xsen_analog_low_edittext),
                                        (EditText) findViewById(R.id.xsen_analog_high_edittext)};
            for(int l = 0; l < 2; l++) {
                final int k = l;
                values[k].setText("" + changes[sensorNum][j]);
                values[k].addTextChangedListener(new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {}
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.length() != 0) {
                            changes[sensorNum][j + k] = Integer.parseInt(s.toString());
                            changes_made = true;
                            changes_bool[sensorNum][j + k] = true;
                        }
                    }
                });
            }

            final Button[] button =    {(Button) findViewById(R.id.xsen_analog_low_neg),
                                        (Button) findViewById(R.id.xsen_analog_low_pos),
                                        (Button) findViewById(R.id.xsen_analog_high_neg),
                                        (Button) findViewById(R.id.xsen_analog_high_pos)};
            for(int l = 0; l < 4; l++) {
                final int k = l;
                button[k].setOnTouchListener(new Button.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                if (Build.VERSION.SDK_INT >= 22)
                                    button[k].setBackground(getDrawable(R.drawable.material_button_blue));
                                else
                                    button[k].setBackground(getResources().getDrawable(R.drawable.material_button_blue));

                                changes_made = true;
                                changes_bool[sensorNum][j + (((k + 2) / 2) - 1)] = true;
                                if (k == 0 || k == 2)
                                    changes[sensorNum][j + (((k + 2) / 2) - 1)]--;
                                else
                                    changes[sensorNum][j + (((k + 2) / 2) - 1)]++;
                                values[(((k + 2) / 2) - 1)].setText("" + changes[sensorNum][j + (((k + 2) / 2) - 1)]);
                                break;
                            case MotionEvent.ACTION_UP:
                                if (Build.VERSION.SDK_INT >= 22)
                                    button[k].setBackground(getDrawable(R.drawable.material_button));
                                else
                                    button[k].setBackground(getResources().getDrawable(R.drawable.material_button));
                                break;
                        }
                        return false;
                    }
                });
            }
        }
        else {
            clicked[i] = false;
            layoutToAdd[i].removeView(viewToInflate[i]);
        }
    }

    public void calibration(View view) {
        final int i = 3, j = 4;
        if(!clicked[i]) {
            clicked[i] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[i] = inflater.inflate(R.layout.z_xsen_04_cal_child, null);
            layoutToAdd[i].addView(viewToInflate[i]);

            final EditText cal = (EditText) findViewById(R.id.xsen_cal_edittext);
            cal.setText("" + changes[sensorNum][j]);
            cal.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {}
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() != 0) {
                        changes[sensorNum][j] = Integer.parseInt(s.toString());
                        changes_made = true;
                        changes_bool[sensorNum][j] = true;
                    }
                }
            });

            final Button[] button =    {(Button) findViewById(R.id.xsen_cal_neg),
                                        (Button) findViewById(R.id.xsen_cal_pos)};
            for(int l = 0; l < 2; l++) {
                final int k = l;
                button[k].setOnTouchListener(new Button.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                if (Build.VERSION.SDK_INT >= 22)
                                    button[k].setBackground(getDrawable(R.drawable.material_button_blue));
                                else
                                    button[k].setBackground(getResources().getDrawable(R.drawable.material_button_blue));

                                changes_made = true;
                                changes_bool[sensorNum][j] = true;
                                if (k == 0)
                                    changes[sensorNum][j]--;
                                else
                                    changes[sensorNum][j]++;
                                cal.setText("" + changes[sensorNum][j]);
                                break;
                            case MotionEvent.ACTION_UP:
                                if (Build.VERSION.SDK_INT >= 22)
                                    button[k].setBackground(getDrawable(R.drawable.material_button));
                                else
                                    button[k].setBackground(getResources().getDrawable(R.drawable.material_button));
                                break;
                        }
                        return false;
                    }
                });
            }
        }
        else {
            clicked[i] = false;
            layoutToAdd[i].removeView(viewToInflate[i]);
        }
    }

    public void multiplier(View view) {
        final int i = 4, j = 5;
        if(!clicked[i]) {
            clicked[i] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[i] = inflater.inflate(R.layout.z_xsen_05_multi_child, null);
            layoutToAdd[i].addView(viewToInflate[i]);

            Spinner dropdown = (Spinner) findViewById(R.id.xsen_multi_spinner);
            String[] items = new String[]{"1x", "2x", "10x", "100x"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.style_spinner_items, items);
            dropdown.setAdapter(adapter);
            dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(loadedSpinner[sensorNum][2]) {
                        switch (position) {
                            case 0:
                                changes[sensorNum][j] = 1;
                                break;
                            case 1:
                                changes[sensorNum][j] = 2;
                                break;
                            case 2:
                                changes[sensorNum][j] = 10;
                                break;
                            case 3:
                                changes[sensorNum][j] = 100;
                                break;
                        }
                        changes[sensorNum][j] = position;
                        changes_made = true;
                        changes_bool[sensorNum][j] = true;
                    }
                    else {
                        loadedSpinner[sensorNum][2] = true;
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
            int selection = 0;
            switch(changes[sensorNum][j]) {
                case 1:
                    selection = 0;
                    break;
                case 2:
                    selection = 1;
                    break;
                case 10:
                    selection = 2;
                    break;
                case 100:
                    selection = 3;
                    break;
            }
            dropdown.setSelection(selection);
        }
        else {
            clicked[i] = false;
            layoutToAdd[i].removeView(viewToInflate[i]);
        }
    }

    public void alarmLimits(View view) {
        final int i = 5, j = 6;
        if(!clicked[i]) {
            clicked[i] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[i] = inflater.inflate(R.layout.z_xsen_06_limits_child, null);
            layoutToAdd[i].addView(viewToInflate[i]);

            final EditText[] limits =  {(EditText) findViewById(R.id.xsen_low_1_edittext),
                                        (EditText) findViewById(R.id.xsen_high_1_edittext),
                                        (EditText) findViewById(R.id.xsen_low_2_edittext),
                                        (EditText) findViewById(R.id.xsen_high_2_edittext),
                                        (EditText) findViewById(R.id.xsen_low_3_edittext),
                                        (EditText) findViewById(R.id.xsen_high_3_edittext),
                                        (EditText) findViewById(R.id.xsen_low_4_edittext),
                                        (EditText) findViewById(R.id.xsen_high_4_edittext)};
            for(int k = 0, l = j; k < 8; k++, l++) {
                final int h = l;
                limits[k].setText("" + changes[sensorNum][h]);
                limits[k].addTextChangedListener(new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {}
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.length() != 0) {
                            changes[sensorNum][h] = Integer.parseInt(s.toString());
                            changes_made = true;
                            changes_bool[sensorNum][h] = true;
                        }
                    }
                });
            }

            final Button[] button =    {(Button) findViewById(R.id.xsen_low_1_neg),
                                        (Button) findViewById(R.id.xsen_low_1_pos),
                                        (Button) findViewById(R.id.xsen_high_1_neg),
                                        (Button) findViewById(R.id.xsen_high_1_pos),
                                        (Button) findViewById(R.id.xsen_low_2_neg),
                                        (Button) findViewById(R.id.xsen_low_2_pos),
                                        (Button) findViewById(R.id.xsen_high_2_neg),
                                        (Button) findViewById(R.id.xsen_high_2_pos),
                                        (Button) findViewById(R.id.xsen_low_3_neg),
                                        (Button) findViewById(R.id.xsen_low_3_pos),
                                        (Button) findViewById(R.id.xsen_high_3_neg),
                                        (Button) findViewById(R.id.xsen_high_3_pos),
                                        (Button) findViewById(R.id.xsen_low_4_neg),
                                        (Button) findViewById(R.id.xsen_low_4_pos),
                                        (Button) findViewById(R.id.xsen_high_4_neg),
                                        (Button) findViewById(R.id.xsen_high_4_pos)};
            for(int l = 0; l < 16; l++) {
                final int k = l;
                button[k].setOnTouchListener(new Button.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                if (Build.VERSION.SDK_INT >= 22)
                                    button[k].setBackground(getDrawable(R.drawable.material_button_blue));
                                else
                                    button[k].setBackground(getResources().getDrawable(R.drawable.material_button_blue));

                                changes_made = true;
                                changes_bool[sensorNum][j + (((k + 2) / 2) - 1)] = true;
                                if (((k + 2) % 2) == 0)
                                    changes[sensorNum][j + (((k + 2) / 2) - 1)]--;
                                else
                                    changes[sensorNum][j + (((k + 2) / 2) - 1)]++;
                                limits[(((k + 2) / 2) - 1)].setText("" + changes[sensorNum][j + (((k + 2) / 2) - 1)]);
                                break;
                            case MotionEvent.ACTION_UP:
                                if (Build.VERSION.SDK_INT >= 22)
                                    button[k].setBackground(getDrawable(R.drawable.material_button));
                                else
                                    button[k].setBackground(getResources().getDrawable(R.drawable.material_button));
                                break;
                        }
                        return false;
                    }
                });
            }
        }
        else {
            clicked[i] = false;
            layoutToAdd[i].removeView(viewToInflate[i]);
        }
    }
}
