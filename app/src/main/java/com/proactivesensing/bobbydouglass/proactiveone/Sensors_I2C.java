package com.proactivesensing.bobbydouglass.proactiveone;

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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Sensors_I2C extends AppCompatActivity {

    public static final int ChangesSizeX =          3;
    public static final int ChangesSizeY =          4;
    public static final int ChangesSizeZ =          12;
    public static final int ViewSize =              8;

    public static int[][][] changes =            {{{-1,     -1,     -1,     -1,     -1,     -1,
                                                    -1,     -1,     -1,     -1,     -1,     -1},
                                                   {-1,     -1,     -1,     -1,     -1,     -1,
                                                    -1,     -1,     -1,     -1,     -1,     -1},
                                                   {-1,     -1,     -1,     -1,     -1,     -1,
                                                    -1,     -1,     -1,     -1,     -1,     -1},
                                                   {-1,     -1,     -1,     -1,     -1,     -1,
                                                    -1,     -1,     -1,     -1,     -1,     -1}},
                                                  {{0,      0,      0,      0,      0,      0,
                                                    0,      0,      0,      0,      0,      0},
                                                   {0,      0,      0,      0,      0,      0,
                                                    0,      0,      0,      0,      0,      0},
                                                   {0,      0,      0,      0,      0,      0,
                                                    0,      0,      0,      0,      0,      0},
                                                   {0,      0,      0,      0,      0,      0,
                                                    0,      0,      0,      0,      0,      0}},
                                                  {{1300,   1301,   1302,   1303,   1304,   1305,
                                                    1306,   1307,   1308,   1309,   1312,   1313},
                                                   {1314,   1315,   1316,   1317,   1318,   1319,
                                                    1320,   1321,   1322,   1323,   1326,   1327},
                                                   {1328,   1329,   1330,   1331,   1332,   1333,
                                                    1334,   1335,   1336,   1337,   1340,   1341},
                                                   {1342,   1343,   1344,   1345,   1346,   1347,
                                                    1348,   1349,   1350,   1351,   1354,   1355}}};

    boolean loadedSpinner[][] =                     new boolean[ChangesSizeY][6];
    LinearLayout[] layoutToAdd =                    new LinearLayout[ViewSize];
    View[] viewToInflate =                          new View[ViewSize];
    boolean[] clicked =                            {false,  false,  false,  false,  false,  false,  false,  false};
    int sensorNum =                                 0;
    boolean changes_made =                          false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensors_i2c);

        for(int i = 0; i < ChangesSizeY; i++)
            for (int j = 0; j < ChangesSizeZ; j++)
                changes[0][i][j] = new Modbus(getApplicationContext(), changes[2][i][j]).getValue();

        for(int i = 0; i < ChangesSizeY; i++) {
            for(int j = 0; j < 6; j++)
                loadedSpinner[i][j] = false;
        }

        layoutToAdd[0] = (LinearLayout) findViewById(R.id.i2c_enable_library_expansion);
        layoutToAdd[1] = (LinearLayout) findViewById(R.id.i2c_power_expansion);
        layoutToAdd[2] = (LinearLayout) findViewById(R.id.i2c_interrupt_expansion);
        layoutToAdd[3] = (LinearLayout) findViewById(R.id.i2c_skip_expansion);
        layoutToAdd[4] = (LinearLayout) findViewById(R.id.i2c_address_expansion);
        layoutToAdd[5] = (LinearLayout) findViewById(R.id.i2c_calibration_expansion);
        layoutToAdd[6] = (LinearLayout) findViewById(R.id.i2c_multiplier_expansion);
        layoutToAdd[7] = (LinearLayout) findViewById(R.id.i2c_limits_expansion);

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

        if(clicked[0]) {
            clicked[0] = false;
            layoutToAdd[0].removeView(viewToInflate[0]);
            enableLibrary(null);
        }
        if(clicked[1]) {
            clicked[1] = false;
            layoutToAdd[1].removeView(viewToInflate[1]);
            power(null);
        }
        if(clicked[2]) {
            clicked[2] = false;
            layoutToAdd[2].removeView(viewToInflate[2]);
            interrupt(null);
        }
        if(clicked[3]) {
            clicked[3] = false;
            layoutToAdd[3].removeView(viewToInflate[3]);
            skip(null);
        }
        if(clicked[4]) {
            clicked[4] = false;
            layoutToAdd[4].removeView(viewToInflate[4]);
            address(null);
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

    public void enableLibrary(View view) {
        final int i = 0, j = 0;
        if(!clicked[i]) {
            clicked[i] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[i] = inflater.inflate(R.layout.z_i2c_01_enable_library_child, null);
            layoutToAdd[i].addView(viewToInflate[i]);

            Spinner dropdown = (Spinner) findViewById(R.id.i2c_library_spinner);
            String[] items =   new String[]{"Library #1",   "Library #2",   "Library #3",   "Library #4",
                                            "Library #5",   "Library #6",   "Library #7",   "Library #8",
                                            "Library #9",   "Library #10",  "Library #11",  "Library #12",
                                            "Library #13",  "Library #14",  "Library #15",  "Library #16"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.style_spinner_items, items);
            dropdown.setAdapter(adapter);
            dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(loadedSpinner[sensorNum][0]) {
                        changes[0][sensorNum][j] = position;
                        changes[1][sensorNum][j] = 1;
                        changes_made = true;
                    }
                    else {
                        loadedSpinner[sensorNum][0] = true;
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
            dropdown.setSelection(changes[0][sensorNum][j]);
        }
        else {
            layoutToAdd[i].removeView(viewToInflate[i]);
            clicked[i] = false;
        }
    }

    public void power(View view) {
        final int i = 1, j = 1;
        if(!clicked[i]) {
            clicked[i] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[i] = inflater.inflate(R.layout.z_i2c_02_power_child, null);
            layoutToAdd[i].addView(viewToInflate[i]);

            Spinner mode = (Spinner) findViewById(R.id.i2c_power_mode_spinner);
            String[] items1 =   new String[]{"Switched", "Always On", "External Power"};
            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, R.layout.style_spinner_items, items1);
            mode.setAdapter(adapter1);
            mode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(loadedSpinner[sensorNum][1]) {
                        changes[0][sensorNum][j] = position;
                        changes[1][sensorNum][j] = 1;
                        changes_made = true;
                    }
                    else {
                        loadedSpinner[sensorNum][1] = true;
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
            mode.setSelection(changes[0][sensorNum][j]);

            Spinner source = (Spinner) findViewById(R.id.i2c_source_spinner);
            String[] items2 =   new String[]{"None", "3V3 #1", "3V3 #2", "15V"};
            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, R.layout.style_spinner_items, items2);
            source.setAdapter(adapter2);
            source.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(loadedSpinner[sensorNum][2]) {
                        changes[0][sensorNum][j + 1] = position;
                        changes[1][sensorNum][j + 1] = 1;
                        changes_made = true;
                    }
                    else {
                        loadedSpinner[sensorNum][2] = true;
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
            source.setSelection(changes[0][sensorNum][j + 1]);

            final EditText wait = (EditText) findViewById(R.id.i2c_power_time_edittext);
            wait.setText("" + changes[0][sensorNum][j + 2]);
            wait.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {}
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() != 0) {
                        changes[0][sensorNum][j + 2] = Integer.parseInt(s.toString());
                        changes[1][sensorNum][j + 2] = 1;
                        changes_made = true;
                    }
                }
            });

            final Button[] button =    {(Button) findViewById(R.id.i2c_power_time_neg),
                                        (Button) findViewById(R.id.i2c_power_time_pos)};
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
                                    changes[0][sensorNum][j + 2]--;
                                else
                                    changes[0][sensorNum][j + 2]++;
                                changes[1][sensorNum][j + 2] = 1;
                                changes_made = true;

                                wait.setText("" + changes[0][sensorNum][j + 2]);
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
            layoutToAdd[i].removeView(viewToInflate[i]);
            clicked[i] = false;
        }
    }

    public void interrupt(View view) {
        final int i = 2, j = 4;
        if(!clicked[i]) {
            clicked[i] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[i] = inflater.inflate(R.layout.z_i2c_03_interrupt_child, null);
            layoutToAdd[i].addView(viewToInflate[i]);

            Spinner mode = (Spinner) findViewById(R.id.i2c_inter_mode_spinner);
            String[] items1 =   new String[]{"No Interrupt", "Exclusive", "Shared"};
            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, R.layout.style_spinner_items, items1);
            mode.setAdapter(adapter1);
            mode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(loadedSpinner[sensorNum][3]) {
                        changes[0][sensorNum][j] = position;
                        changes[1][sensorNum][j] = 1;
                        changes_made = true;
                    }
                    else {
                        loadedSpinner[sensorNum][3] = true;
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
            mode.setSelection(changes[0][sensorNum][j]);

            Spinner channel = (Spinner) findViewById(R.id.i2c_inter_channel_spinner);
            String[] items2 =   new String[]{"Channel #1", "Channel #2", "Channel #3", "Channel #4"};
            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, R.layout.style_spinner_items, items2);
            channel.setAdapter(adapter2);
            channel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(loadedSpinner[sensorNum][4]) {
                        changes[0][sensorNum][j + 1] = position;
                        changes[1][sensorNum][j + 1] = 1;
                        changes_made = true;
                    }
                    else {
                        loadedSpinner[sensorNum][4] = true;
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
            mode.setSelection(changes[0][sensorNum][j + 1]);
        }
        else {
            layoutToAdd[i].removeView(viewToInflate[i]);
            clicked[i] = false;
        }
    }

    public void skip(View view) {
        final int i = 3, j = 6;
        if(!clicked[i]) {
            clicked[i] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[i] = inflater.inflate(R.layout.z_i2c_04_skip_child, null);
            layoutToAdd[i].addView(viewToInflate[i]);

            final EditText skip = (EditText) findViewById(R.id.i2c_skip_edittext);
            skip.setText("" + changes[0][sensorNum][j]);
            skip.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {}
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() != 0) {
                        changes[0][sensorNum][j] = Integer.parseInt(s.toString());
                        changes[1][sensorNum][j] = 1;
                        changes_made = true;
                    }
                }
            });

            final Button[] button =    {(Button) findViewById(R.id.i2c_skip_neg),
                                        (Button) findViewById(R.id.i2c_skip_pos)};
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
                                    changes[0][sensorNum][j]--;
                                else
                                    changes[0][sensorNum][j]++;
                                changes[1][sensorNum][j] = 1;
                                changes_made = true;

                                skip.setText("" + changes[0][sensorNum][j]);
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
            layoutToAdd[i].removeView(viewToInflate[i]);
            clicked[i] = false;
        }
    }

    public void address(View view) {
        final int i = 4, j = 7;
        if(!clicked[i]) {
            clicked[i] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[i] = inflater.inflate(R.layout.z_i2c_05_address_child, null);
            layoutToAdd[i].addView(viewToInflate[i]);

            final EditText add = (EditText) findViewById(R.id.i2c_address_edittext);
            add.setText("" + changes[0][sensorNum][j]);
            add.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {}
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() != 0) {
                        changes[0][sensorNum][j] = Integer.parseInt(s.toString());
                        changes[1][sensorNum][j] = 1;
                        changes_made = true;
                    }
                }
            });
        }
        else {
            layoutToAdd[i].removeView(viewToInflate[i]);
            clicked[i] = false;
        }
    }

    public void calibration(View view) {
        final int i = 5, j = 8;
        if(!clicked[i]) {
            clicked[i] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[i] = inflater.inflate(R.layout.z_i2c_06_calibration_child, null);
            layoutToAdd[i].addView(viewToInflate[i]);

            final EditText cal = (EditText) findViewById(R.id.i2c_cal_edittext);
            cal.setText("" + changes[0][sensorNum][j]);
            cal.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {}
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() != 0) {
                        changes[0][sensorNum][j] = Integer.parseInt(s.toString());
                        changes[1][sensorNum][j] = 1;
                        changes_made = true;
                    }
                }
            });

            final Button[] button =    {(Button) findViewById(R.id.i2c_cal_neg),
                                        (Button) findViewById(R.id.i2c_cal_pos)};
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
            layoutToAdd[i].removeView(viewToInflate[i]);
            clicked[i] = false;
        }
    }

    public void multiplier(View view) {
        final int i = 6, j = 9;
        if(!clicked[i]) {
            clicked[i] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[i] = inflater.inflate(R.layout.z_i2c_07_multiplier_child, null);
            layoutToAdd[i].addView(viewToInflate[i]);

            Spinner mult = (Spinner) findViewById(R.id.i2c_multi_spinner);
            String[] items =   new String[]{"1X", "2X", "10X", "100X"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.style_spinner_items, items);
            mult.setAdapter(adapter);
            mult.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(loadedSpinner[sensorNum][5]) {
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
                        loadedSpinner[sensorNum][5] = true;
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
            mult.setSelection(selection);
        }
        else {
            layoutToAdd[i].removeView(viewToInflate[i]);
            clicked[i] = false;
        }
    }

    public void limits(View view) {
        final int i = 7, j = 10;
        if(!clicked[i]) {
            clicked[i] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[i] = inflater.inflate(R.layout.z_i2c_08_limits_child, null);
            layoutToAdd[i].addView(viewToInflate[i]);

            final EditText[] limits =  {(EditText) findViewById(R.id.i2c_low_edittext),
                                        (EditText) findViewById(R.id.i2c_high_edittext)};
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
                            changes[0][sensorNum][j + k] = Integer.parseInt(s.toString());
                            changes[1][sensorNum][j + k] = 1;
                            changes_made = true;
                        }
                    }
                });
            }

            final Button[] button =    {(Button) findViewById(R.id.i2c_low_neg),   (Button) findViewById(R.id.i2c_low_pos),
                                        (Button) findViewById(R.id.i2c_high_neg),  (Button) findViewById(R.id.i2c_high_pos)};
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
            layoutToAdd[i].removeView(viewToInflate[i]);
            clicked[i] = false;
        }
    }
}
