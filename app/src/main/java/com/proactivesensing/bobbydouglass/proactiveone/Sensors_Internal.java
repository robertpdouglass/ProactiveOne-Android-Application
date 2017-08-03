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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Sensors_Internal extends AppCompatActivity {

    public static final int Size =                      10;
    public static final int ChangesSizeX =              3;
    public static final int ChangesSizeY =              2;
    public static final int ChangesSizeZ =              5;

    public static LinearLayout[] layoutToAdd =          new LinearLayout[4];
    public static View[][] viewToInflate =              new View[2][4];
    public static int[][][] changes =                {{{-1,     -1,     -1,     -1,     -1},
                                                       {-1,     -1,     -1,     -1,     -1}},

                                                      {{0,      0,      0,      0,      0},
                                                       {0,      0,      0,      0,      0}},

                                                      {{1200,   1201,   1202,   1203,   1204},
                                                       {1205,   1206,   1207,   1208,   1209}}};

    public static boolean[][] clicked =               {{false,  false,  false,  false},{false,
                                                        false,  false,  false}};

    boolean[] loadedSpinner =                          {false, false};
    Button temp;
    Button accel;
    boolean changes_made =                              false;
    int screen =                                        0;
    TextView[] optionTextViews =                        new TextView[4];
    String[][] optionTexts =                          {{"Temperature Configuration",    "Temperature Sensor Calibration",
                                                        "Temperature Multiplier",       "Temperature Sensor Alarm Limits"},
                                                       {"Accelerometer Configuration",  "Accelerometer Axis Limits",
                                                        "Accelerometer Combined Limit"}};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensors_internal);

        temp =                  (Button) findViewById(R.id.temp_button);
        accel =                 (Button) findViewById(R.id.accel_button);
        optionTextViews[0] =    (TextView) findViewById(R.id.int_textView1);
        optionTextViews[1] =    (TextView) findViewById(R.id.int_textView2);
        optionTextViews[2] =    (TextView) findViewById(R.id.int_textView3);
        optionTextViews[3] =    (TextView) findViewById(R.id.int_textView4);

        for(int i = 0; i < (4 - screen); i++)
            optionTextViews[i].setText(optionTexts[screen][i]);
        if (Build.VERSION.SDK_INT >= 22)
            temp.setBackground(getDrawable(R.drawable.material_button_blue));
        else
            temp.setBackground(getResources().getDrawable(R.drawable.material_button_blue));

        layoutToAdd[0] =     (LinearLayout) findViewById(R.id.int_option1_expansion);
        layoutToAdd[1] =     (LinearLayout) findViewById(R.id.int_option2_expansion);
        layoutToAdd[2] =     (LinearLayout) findViewById(R.id.int_option3_expansion);
        layoutToAdd[3] =     (LinearLayout) findViewById(R.id.int_option4_expansion);

        for (int i = 0; i < ChangesSizeY; i++) {
            for(int j = 0; j < ChangesSizeZ; j++)
                changes[0][i][j] = new Modbus(getApplicationContext(), changes[2][i][j]).getValue();
        }
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

    public void startConfigure() {
        Intent Conf = new Intent(this, Configure_Sensors.class);
        Conf.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(Conf);
    }

    public void temperature(View view) {
        if(screen == 1) {
            refreshScreen();
            screen = 0;
            for(int i = 0; i < (4 - screen); i++)
                optionTextViews[i].setText(optionTexts[screen][i]);
            RelativeLayout lastLayout = (RelativeLayout) findViewById(R.id.int_option4_layout);
            lastLayout.setVisibility(View.VISIBLE);
            lastLayout.setClickable(true);
            lastLayout.setEnabled(true);
            if (Build.VERSION.SDK_INT >= 22) {
                temp.setBackground(getDrawable(R.drawable.material_button_blue));
                accel.setBackground(getDrawable(R.drawable.material_button));
            } else {
                temp.setBackground(getResources().getDrawable(R.drawable.material_button_blue));
                accel.setBackground(getResources().getDrawable(R.drawable.material_button));
            }
        }
    }

    public void accelerometer(View view) {
        if(screen == 0) {
            refreshScreen();
            screen = 1;
            for(int i = 0; i < (4 - screen); i++)
                optionTextViews[i].setText(optionTexts[screen][i]);
            RelativeLayout lastLayout = (RelativeLayout) findViewById(R.id.int_option4_layout);
            lastLayout.setVisibility(View.INVISIBLE);
            lastLayout.setClickable(false);
            lastLayout.setEnabled(false);
            if (Build.VERSION.SDK_INT >= 22) {
                accel.setBackground(getDrawable(R.drawable.material_button_blue));
                temp.setBackground(getDrawable(R.drawable.material_button));
            } else {
                accel.setBackground(getResources().getDrawable(R.drawable.material_button_blue));
                temp.setBackground(getResources().getDrawable(R.drawable.material_button));
            }
        }
    }

    public void refreshScreen() {
        for (int i = 0; i < (4 - screen); i++) {
            if (clicked[screen][i]) {
                clicked[screen][i] = false;
                layoutToAdd[i].removeView(viewToInflate[screen][i]);
            }
        }
    }

    public void option1(View view) {
        if(screen == 0)
            tempConfig();
        else
            accelConfig();
    }

    public void option2(View view) {
        if(screen == 0)
            tempCal();
        else
            accelLimits();
    }

    public void option3(View view) {
        if(screen == 0)
            tempMulti();
        else
            accelCombo();
    }

    public void option4(View view) {
        tempLimits();
    }

    public void tempConfig() {
        final int i = 0, j = 0;
        if(!clicked[0][i]) {
            clicked[0][i] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[0][i] = inflater.inflate(R.layout.z_int_t_01_config_child, null);
            layoutToAdd[i].addView(viewToInflate[0][i]);

            Spinner dropdown = (Spinner) findViewById(R.id.int_t_config_spinner);
            String[] items = new String[]{"Disabled", "Enabled F", "Enabled C"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.style_spinner_items, items);
            dropdown.setAdapter(adapter);
            dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(loadedSpinner[0]) {
                        changes[0][0][j] = position;
                        changes[1][0][j] = 1;
                        changes_made = true;
                    }
                    else
                        loadedSpinner[0] = true;
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
            dropdown.setSelection(changes[0][0][j]);
        }
        else {
            clicked[0][i] = false;
            layoutToAdd[i].removeView(viewToInflate[0][i]);
        }
    }

    public void tempCal() {
        final int i = 1, j = 1;
        if(!clicked[0][i]) {
            clicked[0][i] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[0][i] = inflater.inflate(R.layout.z_int_t_02_cal_child, null);
            layoutToAdd[i].addView(viewToInflate[0][i]);
            final EditText e = (EditText) findViewById(R.id.int_t_cal_edittext);
            e.setText("" + changes[0][0][j]);
            e.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {}
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() != 0) {
                        changes[0][0][j] = Integer.parseInt(s.toString());
                        changes[1][0][j] = 1;
                        changes_made = true;
                    }
                }
            });

            final Button[] button =    {(Button) findViewById(R.id.int_t_cal_neg),
                                        (Button) findViewById(R.id.int_t_cal_pos)};
            for(int l = 0, h = -1; l < 2; l++, h += 2) {
                final int k = l, m = h;
                button[k].setOnTouchListener(new Button.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                changes[0][0][j] += m;
                                e.setText("" + changes[0][0][i]);
                                if (Build.VERSION.SDK_INT >= 22)
                                    button[k].setBackground(getDrawable(R.drawable.material_button_blue));
                                else
                                    button[k].setBackground(getResources().getDrawable(R.drawable.material_button_blue));
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
            clicked[0][i] = false;
            layoutToAdd[i].removeView(viewToInflate[0][i]);
        }
    }

    public void tempMulti() {
        final int i = 2, j = 2;
        if(!clicked[0][i]) {
            clicked[0][i] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[0][i] = inflater.inflate(R.layout.z_int_t_03_multi_child, null);
            layoutToAdd[i].addView(viewToInflate[0][i]);
            Spinner dropdown = (Spinner) findViewById(R.id.int_t_multi_spinner);
            String[] items = new String[]{"1X", "2X", "10X", "100X"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.style_spinner_items, items);
            dropdown.setAdapter(adapter);
            dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(loadedSpinner[1]) {
                        switch (position) {
                            case 0:
                                changes[0][0][j] = 1;
                                break;
                            case 1:
                                changes[0][0][j] = 2;
                                break;
                            case 2:
                                changes[0][0][j] = 10;
                                break;
                            case 3:
                                changes[0][0][j] = 100;
                                break;
                        }
                        changes[1][0][j] = 1;
                        changes_made = true;
                    }
                    else
                        loadedSpinner[1] = true;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
            int selected;
            switch(changes[0][0][j]) {
                case 1:
                    selected = 0;
                    break;
                case 2:
                    selected = 1;
                    break;
                case 10:
                    selected = 2;
                    break;
                case 100:
                    selected = 3;
                    break;
                default:
                    selected = 0;
                    break;
            }
            dropdown.setSelection(selected);
        }
        else {
            clicked[0][i] = false;
            layoutToAdd[i].removeView(viewToInflate[0][j]);
        }
    }

    public void tempLimits() {
        final int i = 3, j = 3;
        if(!clicked[0][i]) {
            clicked[0][i] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[0][i] = inflater.inflate(R.layout.z_int_t_04_limits_child, null);
            layoutToAdd[i].addView(viewToInflate[0][i]);

            final EditText[] limits =    {(EditText) findViewById(R.id.int_t_low_edittext),
                                    (EditText) findViewById(R.id.int_t_high_edittext)};
            for(int l = 0; l < 2; l++) {
                final int k = l;
                limits[k].setText("" + changes[0][0][j + k]);
                limits[k].addTextChangedListener(new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {}
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.length() != 0) {
                            changes[0][0][j + k] = Integer.parseInt(s.toString());
                            changes[1][0][j + k] = 1;
                            changes_made = true;
                        }
                    }
                });
            }

            final Button[] button =    {(Button) findViewById(R.id.int_t_low_neg),
                                        (Button) findViewById(R.id.int_t_low_pos),
                                        (Button) findViewById(R.id.int_t_high_neg),
                                        (Button) findViewById(R.id.int_t_high_pos)};
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
                                    changes[0][0][j + (((k + 2) / 2) - 1)]--;
                                else
                                    changes[0][0][j + (((k + 2) / 2) - 1)]++;
                                limits[(((k + 2) / 2) - 1)].setText("" + changes[0][0][j + (((k + 2) / 2) - 1)]);
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
            clicked[0][i] = false;
            layoutToAdd[i].removeView(viewToInflate[0][i]);
        }
    }

    public void accelConfig() {
        final int i = 0, j = 0;
        if(!clicked[1][i]) {
            clicked[1][i] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[1][i] = inflater.inflate(R.layout.z_int_a_01_config_child, null);
            layoutToAdd[i].addView(viewToInflate[1][i]);
            final ToggleButton tgl = (ToggleButton) findViewById(R.id.int_a_config_toggle);
            if(tgl.isChecked()){
                if(Build.VERSION.SDK_INT >= 22)
                    tgl.setBackground(getDrawable(R.drawable.material_button_blue));
                else
                    tgl.setBackground(getResources().getDrawable(R.drawable.material_button));
            }
            tgl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(tgl.isChecked()){
                        if(Build.VERSION.SDK_INT >= 22)
                            tgl.setBackground(getDrawable(R.drawable.material_button_blue));
                        else
                            tgl.setBackground(getResources().getDrawable(R.drawable.material_button));
                        changes[0][1][j] = (isChecked) ? 1 : 0;
                        changes[1][1][j] = 1;
                        changes_made = true;
                    }
                }
            });
            tgl.setChecked(changes[0][1][j] == 1);
        }
        else {
            clicked[1][i] = false;
            layoutToAdd[i].removeView(viewToInflate[1][i]);
        }
    }

    public void accelLimits() {
        final int i = 1, j = 1;
        if(!clicked[1][i]) {
            clicked[1][i] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[1][i] = inflater.inflate(R.layout.z_int_a_02_axis_child, null);
            layoutToAdd[i].addView(viewToInflate[1][i]);

            EditText[] xyz =   {(EditText) findViewById(R.id.int_a_x_edittext),
                                (EditText) findViewById(R.id.int_a_y_edittext),
                                (EditText) findViewById(R.id.int_a_z_edittext)};
            for(int l = 0; l < 3; l++) {
                final int k = l;
                xyz[k].setText("" + (((float) (changes[0][1][j + k])) / 100));
                xyz[k].addTextChangedListener(new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {}
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.length() != 0) {
                            changes[0][1][j + k] = ((int) ((Float.parseFloat(s.toString())) * 100));
                            changes[1][1][j + k] = 1;
                            changes_made = true;
                        }
                    }
                });
            }
        }
        else {
            clicked[1][i] = false;
            layoutToAdd[i].removeView(viewToInflate[1][i]);
        }
    }

    public void accelCombo() {
        final int i = 2, j = 4;
        if(!clicked[1][i]) {
            clicked[1][i] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[1][i] = inflater.inflate(R.layout.z_int_a_03_combo_child, null);
            layoutToAdd[i].addView(viewToInflate[1][i]);

            EditText combo = (EditText) findViewById(R.id.int_a_combo_edittext);
            combo.setText("" + (((float) (changes[0][1][j])) / 100));
            combo.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {}
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() != 0) {
                        changes[0][1][j] = ((int) ((Float.parseFloat(s.toString())) * 100));
                        changes[1][1][j] = 1;
                        changes_made = true;
                    }
                }
            });
        }
        else {
            clicked[1][i] = false;
            layoutToAdd[i].removeView(viewToInflate[1][i]);
        }
    }
}
