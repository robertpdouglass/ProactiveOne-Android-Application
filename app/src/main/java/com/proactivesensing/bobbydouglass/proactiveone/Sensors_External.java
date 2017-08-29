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

    public static final int ChangesSizeX =          3;
    public static final int ChangesSizeY =          4;
    public static final int ChangesSizeZ =          11;
    public static final int ViewSize =              8;

                                                    /* ***** VALUES ***** */
    public static short[][][] changes =          {{{-1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
                                                    -1,     -1,     -1},
                                                   {-1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
                                                    -1,     -1,     -1},
                                                   {-1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
                                                    -1,     -1,     -1},
                                                   {-1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
                                                    -1,     -1,     -1}},

                                                    /* ***** CHANGES MADE ***** */
                                                  {{0,      0,      0,      0,      0,      0,      0,      0,
                                                    0,      0,      0},
                                                   {0,      0,      0,      0,      0,      0,      0,      0,
                                                    0,      0,      0},
                                                   {0,      0,      0,      0,      0,      0,      0,      0,
                                                    0,      0,      0},
                                                   {0,      0,      0,      0,      0,      0,      0,      0,
                                                    0,      0,      0}},

                                                    /* ***** ADDRESS ***** */
                                                  {{1100,   1101,   1102,   1103,   1104,   1105,   1106,   1107,
                                                    1108,   1112,   1113},
                                                   {1114,   1115,   1116,   1117,   1118,   1119,   1120,   1121,
                                                    1122,   1126,   1127},
                                                   {1128,   1129,   1130,   1131,   1132,   1133,   1134,   1135,
                                                    1136,   1140,   1141},
                                                   {1142,   1143,   1144,   1145,   1146,   1147,   1148,   1149,
                                                    1150,   1154,   1155}}};

    LinearLayout[] layoutToAdd =                    new LinearLayout[ViewSize];
    View[] viewToInflate =                          new View[ViewSize];
    boolean[] clicked =                            {false,  false,  false,  false,  false,  false,  false,  false};
    boolean changes_made =                          false;
    int sensorNum =                                 0;
    boolean[][] loadedSpinner =                   {{false,  false,  false},{false,  false,  false},{false,  false,
                                                    false},{false,  false,  false}};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensors_external);

        layoutToAdd[0] = (LinearLayout) findViewById(R.id.config_expansion);
        layoutToAdd[1] = (LinearLayout) findViewById(R.id.power_expansion);
        layoutToAdd[2] = (LinearLayout) findViewById(R.id.skip_expansion);
        layoutToAdd[3] = (LinearLayout) findViewById(R.id.recog_expansion);
        layoutToAdd[4] = (LinearLayout) findViewById(R.id.analog_expansion);
        layoutToAdd[5] = (LinearLayout) findViewById(R.id.calibration_expansion);
        layoutToAdd[6] = (LinearLayout) findViewById(R.id.multiplier_expansion);
        layoutToAdd[7] = (LinearLayout) findViewById(R.id.limits_expansion);

        for (int i = 0; i < ChangesSizeY; i++)
            for (int j = 0; j < ChangesSizeZ; j++)
                changes[0][i][j] = new Modbus(getApplicationContext(), changes[2][i][j]).getValue();

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
                    new Modbus(getApplicationContext(), 1);
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

        switch (sensorNum) {
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

        for (int i = 0; i < 3; i++) {
            if (Build.VERSION.SDK_INT >= 22)
                offSensors[i].setBackground(getDrawable(R.drawable.material_button));
            else
                offSensors[i].setBackground(getResources().getDrawable(R.drawable.material_button));
        }

        if (Build.VERSION.SDK_INT >= 22)
            selectedSensor.setBackground(getDrawable(R.drawable.material_button_blue));
        else
            selectedSensor.setBackground(getResources().getDrawable(R.drawable.material_button_blue));

        if(clicked[0]) {
            clicked[0] = false;
            layoutToAdd[0].removeView(viewToInflate[0]);
            configuration(null);
        }
        if(clicked[1]) {
            clicked[1] = false;
            layoutToAdd[1].removeView(viewToInflate[1]);
            power(null);
        }
        if(clicked[2]) {
            clicked[2] = false;
            layoutToAdd[2].removeView(viewToInflate[2]);
            skip(null);
        }
        if(clicked[3]) {
            clicked[3] = false;
            layoutToAdd[3].removeView(viewToInflate[3]);
            recog(null);
        }
        if(clicked[4]) {
            clicked[4] = false;
            layoutToAdd[4].removeView(viewToInflate[4]);
            analog(null);
        }
        if(clicked[5]) {
            clicked[5] = false;
            layoutToAdd[5].removeView(viewToInflate[5]);
            calibration(null);
        }
        if(clicked[6]) {
            clicked[6] = false;
            layoutToAdd[6].removeView(viewToInflate[6]);
            multiplier(null);
        }
        if(clicked[7]) {
            clicked[7] = false;
            layoutToAdd[7].removeView(viewToInflate[7]);
            limits(null);
        }
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

    public void negativeToast() {
        Toast.makeText(this, "Input Cannot Be Negative", Toast.LENGTH_SHORT).show();
    }

    public void configuration(View view) {
        final int i = 0, j = 0;
        if(!clicked[i]) {
            clicked[i] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[i] = inflater.inflate(R.layout.z_xsen_01_config_child, null);
            layoutToAdd[i].addView(viewToInflate[i]);

            Spinner dropdown = (Spinner) findViewById(R.id.xsen_config_spinner);
            String[] items = new String[]{"Disabled", "Normally Open", "Normally Closed", "Any Contact", "Resistive", "4-20mA", "Voltage"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.style_spinner_items, items);
            dropdown.setAdapter(adapter);
            dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(loadedSpinner[sensorNum][0]) {
                        changes[0][sensorNum][j] = (short) position;
                        changes[1][sensorNum][j] = 1;
                        changes_made = true;
                    }
                    else
                        loadedSpinner[sensorNum][0] = true;
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
            dropdown.setSelection(changes[0][sensorNum][j]);
        }
        else {
            clicked[i] = false;
            layoutToAdd[i].removeView(viewToInflate[i]);
        }
    }

    public void power(View view) {
        final int i = 1, j = 1;
        if(!clicked[i]) {
            clicked[i] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[i] = inflater.inflate(R.layout.z_xsen_02_power_child, null);
            layoutToAdd[i].addView(viewToInflate[i]);

            Spinner dropdown = (Spinner) findViewById(R.id.xsen_power_source_spinner);
            String[] items = new String[]{"None", "3V3 #1", "3V3 #2", "15V"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.style_spinner_items, items);
            dropdown.setAdapter(adapter);
            dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(loadedSpinner[sensorNum][0]) {
                        changes[0][sensorNum][j] = (short) position;
                        changes[1][sensorNum][j] = 1;
                        changes_made = true;
                    }
                    else
                        loadedSpinner[sensorNum][0] = true;
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
            dropdown.setSelection(changes[0][sensorNum][j]);

            final EditText power_time = (EditText) findViewById(R.id.xsen_power_time_edittext);
            power_time.setText("" + changes[0][sensorNum][j + 1]);
            power_time.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {}
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() != 0) {
                        changes[0][sensorNum][j + 1] = Short.parseShort(s.toString());
                        changes[1][sensorNum][j + 1] = 1;
                        changes_made = true;
                    }
                }
            });

            final Button[] button =    {(Button) findViewById(R.id.xsen_power_time_neg),
                                        (Button) findViewById(R.id.xsen_power_time_pos)};
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

                                if(k == 0)
                                    changes[0][sensorNum][j + 1]--;
                                else
                                    changes[0][sensorNum][j + 1]++;
                                changes[1][sensorNum][j + 1] = 1;
                                changes_made = true;

                                power_time.setText("" + changes[0][sensorNum][j + 1]);
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

    public void skip(View view) {
        final int i = 2, j = 3;
        if(!clicked[i]) {
            clicked[i] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[i] = inflater.inflate(R.layout.z_xsen_03_skip_child, null);
            layoutToAdd[i].addView(viewToInflate[i]);

            final EditText skip = (EditText) findViewById(R.id.xsen_skip_edittext);
            skip.setText("" + changes[0][sensorNum][j]);
            skip.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {}
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() != 0) {
                        short value = Short.parseShort(s.toString());
                        if(value > -1) {
                            changes[0][sensorNum][j] = value;
                            changes[1][sensorNum][j] = 1;
                            changes_made = true;
                        }
                        else
                            negativeToast();
                    }
                }
            });

            final Button[] button =    {(Button) findViewById(R.id.xsen_skip_neg),
                                        (Button) findViewById(R.id.xsen_skip_pos)};
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

                                if(k == 0) {
                                    if(changes[0][sensorNum][j] > 0) {
                                        changes[0][sensorNum][j]--;
                                        changes[1][sensorNum][j] = 1;
                                        changes_made = true;
                                        skip.setText("" + changes[0][sensorNum][j]);
                                    }
                                    else
                                        negativeToast();
                                }
                                else {
                                    changes[0][sensorNum][j]++;
                                    changes[1][sensorNum][j] = 1;
                                    changes_made = true;
                                    skip.setText("" + changes[0][sensorNum][j]);
                                }
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

    public void recog(View view) {
        final int i = 3, j = 4;
        if(!clicked[i]) {
            clicked[i] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[i] = inflater.inflate(R.layout.z_xsen_04_recog_child, null);
            layoutToAdd[i].addView(viewToInflate[i]);

            final EditText recog = (EditText) findViewById(R.id.xsen_recog_edittext);
            recog.setText("" + changes[0][sensorNum][j]);
            recog.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {}
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() != 0) {
                        short value = Short.parseShort(s.toString());
                        if(value > -1) {
                            changes[0][sensorNum][j] = value;
                            changes[1][sensorNum][j] = 1;
                            changes_made = true;
                        }
                        else
                            negativeToast();
                    }
                }
            });

            final Button[] button =    {(Button) findViewById(R.id.xsen_recog_neg),
                                        (Button) findViewById(R.id.xsen_recog_pos)};
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

                                if(k == 0) {
                                    if(changes[0][sensorNum][j] > 0) {
                                        changes[0][sensorNum][j]--;
                                        changes[1][sensorNum][j] = 1;
                                        changes_made = true;
                                        recog.setText("" + changes[0][sensorNum][j]);
                                    }
                                    else
                                        negativeToast();
                                }
                                else {
                                    changes[0][sensorNum][j]++;
                                    changes[1][sensorNum][j] = 1;
                                    changes_made = true;
                                    recog.setText("" + changes[0][sensorNum][j]);
                                }
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

    public void analog(View view) {
        final int i = 4, j = 5;
        if(!clicked[i]) {
            clicked[i] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[i] = inflater.inflate(R.layout.z_xsen_05_analog_child, null);
            layoutToAdd[i].addView(viewToInflate[i]);

            final EditText[] analog =  {(EditText) findViewById(R.id.xsen_analog_low_edittext),
                                        (EditText) findViewById(R.id.xsen_analog_high_edittext)};
            for(int l = 0; l < 2; l++) {
                final int k = l;
                analog[k].setText("" + changes[0][sensorNum][j + k]);
                analog[k].addTextChangedListener(new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {}
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.length() != 0) {
                            changes[0][sensorNum][j + k] = Short.parseShort(s.toString());
                            changes[1][sensorNum][j + k] = 1;
                            changes_made = true;
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

                                if (k == 0 || k == 2)
                                    changes[0][sensorNum][j + (((k + 2) / 2) - 1)]--;
                                else
                                    changes[0][sensorNum][j + (((k + 2) / 2) - 1)]++;
                                changes[1][sensorNum][j + (((k + 2) / 2) - 1)] = 1;
                                changes_made = true;

                                analog[(((k + 2) / 2) - 1)].setText("" + changes[0][sensorNum][j + (((k + 2) / 2) - 1)]);
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
        final int i = 5, j = 7;
        if(!clicked[i]) {
            clicked[i] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[i] = inflater.inflate(R.layout.z_xsen_06_calibration_child, null);
            layoutToAdd[i].addView(viewToInflate[i]);

            final EditText cal = (EditText) findViewById(R.id.xsen_cal_edittext);
            cal.setText("" + changes[0][sensorNum][j]);
            cal.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {}
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() != 0) {
                        changes[0][sensorNum][j] = Short.parseShort(s.toString());
                        changes[1][sensorNum][j] = 1;
                        changes_made = true;
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

                                if (k == 0)
                                    changes[0][sensorNum][j]--;
                                else
                                    changes[0][sensorNum][j]++;
                                changes[1][sensorNum][j] = 1;
                                changes_made = true;

                                cal.setText("" + changes[0][sensorNum][j]);
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
        final int i = 6, j = 8;
        if(!clicked[i]) {
            clicked[i] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[i] = inflater.inflate(R.layout.z_xsen_07_multiplier_child, null);
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
                                changes[0][sensorNum][j] = 1;
                                break;
                            case 1:
                                changes[0][sensorNum][j] = 2;
                                break;
                            case 2:
                                changes[0][sensorNum][j] = 10;
                                break;
                            case 3:
                                changes[0][sensorNum][j] = 100;
                                break;
                        }
                        changes[1][sensorNum][j] = 1;
                        changes_made = true;
                    }
                    else {
                        loadedSpinner[sensorNum][2] = true;
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });

            int selection = 0;
            switch(changes[0][sensorNum][j]) {
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

    public void limits(View view) {
        final int i = 7, j = 9;
        if(!clicked[i]) {
            clicked[i] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[i] = inflater.inflate(R.layout.z_xsen_08_limits_child, null);
            layoutToAdd[i].addView(viewToInflate[i]);

            final EditText[] limits =  {(EditText) findViewById(R.id.xsen_low_edittext),
                                        (EditText) findViewById(R.id.xsen_high_edittext)};
            for(int l = 0; l < 2; l++) {
                final int k = l;
                limits[k].setText("" + changes[0][sensorNum][j + k]);
                limits[k].addTextChangedListener(new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {}
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.length() != 0) {
                            changes[0][sensorNum][j + k] = Short.parseShort(s.toString());
                            changes[1][sensorNum][j + k] = 1;
                            changes_made = true;
                        }
                    }
                });
            }

            final Button[] button =    {(Button) findViewById(R.id.xsen_low_neg),   (Button) findViewById(R.id.xsen_low_pos),
                                        (Button) findViewById(R.id.xsen_high_neg),  (Button) findViewById(R.id.xsen_high_pos)};
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

                                if (k == 0 || k == 2)
                                    changes[0][sensorNum][j + (((k + 2) / 2) - 1)]--;
                                else
                                    changes[0][sensorNum][j + (((k + 2) / 2) - 1)]++;
                                changes[1][sensorNum][j + (((k + 2) / 2) - 1)] = 1;
                                changes_made = true;

                                limits[(((k + 2) / 2) - 1)].setText("" + changes[0][sensorNum][j + (((k + 2) / 2) - 1)]);
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
