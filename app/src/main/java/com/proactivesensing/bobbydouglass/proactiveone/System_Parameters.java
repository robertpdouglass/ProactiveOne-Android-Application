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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

public class System_Parameters extends AppCompatActivity{

    public static final int Size =                  14;

    public static LinearLayout[] layoutToAdd =      new LinearLayout[Size];
    public static View[] viewToInflate =            new View[Size];
    boolean[] clicked =                            {false,  false,  false,  false,  false,  false,  false,
                                                    false,  false,  false,  false,  false,  false,  false};
    public static int[] changes =                   new int[Size];
    public static boolean[] changes_bool =         {false,  false,  false,  false,  false,  false,  false,
                                                    false,  false,  false,  false,  false,  false,  false};
    boolean changes_made =                          false;
    public static final int[] addresses =          {1001,   1002,   1003,   1004,   1005,   1006,   1007,
                                                    1009,   1011,   1012,   1013,   1014,   1020,   1021};
    public static LayoutInflater inflater;
    public static LayoutTransition transition;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.system_parameters);

        inflater = LayoutInflater.from(getApplicationContext());
        viewToInflate[0] = inflater.inflate(R.layout.z_sys_01_locations_child, null);
        viewToInflate[0] = inflater.inflate(R.layout.z_sys_02_time_child, null);
        viewToInflate[0] = inflater.inflate(R.layout.z_sys_03_auto_child, null);
        viewToInflate[0] = inflater.inflate(R.layout.z_sys_04_a_msg_child, null);
        viewToInflate[0] = inflater.inflate(R.layout.z_sys_05_a_time_child, null);
        viewToInflate[0] = inflater.inflate(R.layout.z_sys_06_b_msg_child, null);
        viewToInflate[0] = inflater.inflate(R.layout.z_sys_07_b_time_child, null);
        viewToInflate[0] = inflater.inflate(R.layout.z_sys_08_battery_child, null);
        viewToInflate[0] = inflater.inflate(R.layout.z_sys_09_airplane_child, null);
        viewToInflate[0] = inflater.inflate(R.layout.z_sys_10_3v3_1_child, null);
        viewToInflate[0] = inflater.inflate(R.layout.z_sys_11_3v3_2_child, null);
        viewToInflate[0] = inflater.inflate(R.layout.z_sys_12_15v_child, null);
        viewToInflate[0] = inflater.inflate(R.layout.z_sys_13_cycle_child, null);
        viewToInflate[0] = inflater.inflate(R.layout.z_sys_14_vcc_child, null);

        layoutToAdd[0] = (LinearLayout) findViewById(R.id.locations);
        layoutToAdd[1] = (LinearLayout) findViewById(R.id.daily);
        layoutToAdd[2] = (LinearLayout) findViewById(R.id.auto);
        layoutToAdd[3] = (LinearLayout) findViewById(R.id.a_msg);
        layoutToAdd[4] = (LinearLayout) findViewById(R.id.a_time);
        layoutToAdd[5] = (LinearLayout) findViewById(R.id.b_msg);
        layoutToAdd[6] = (LinearLayout) findViewById(R.id.b_time);
        layoutToAdd[7] = (LinearLayout) findViewById(R.id.battery);
        layoutToAdd[8] = (LinearLayout) findViewById(R.id.airplane);
        layoutToAdd[9] = (LinearLayout) findViewById(R.id.power_3v3_1);
        layoutToAdd[10] = (LinearLayout) findViewById(R.id.power_3v3_2);
        layoutToAdd[11] = (LinearLayout) findViewById(R.id.power_15v);
        layoutToAdd[12] = (LinearLayout) findViewById(R.id.cycle);
        layoutToAdd[13] = (LinearLayout) findViewById(R.id.vcc);

        transition = new LayoutTransition();

        for (int i = 0; i < Size; i++) {
            layoutToAdd[i].setLayoutTransition(transition);
            changes[i] = new Modbus(getApplicationContext(), addresses[i]).getValue();
        }
    }

    @Override
    public void onBackPressed() {
        if(changes_made) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Changes Made, Save Changes?");
            builder.setPositiveButton("Save", dialogClickListener);
            builder.setNegativeButton("Discard", dialogClickListener);
            builder.show();
        }
        else {
            Intent Home = new Intent(this, Home.class);
            Home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(Home);
        }
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    startNfc();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    startHome();
                    break;
            }
        }
    };

    public void startNfc() {
        Intent NFC = new Intent(this, Nfc.class);
        NFC.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(NFC);
    }

    public void startHome() {
        Intent Home = new Intent(this, Home.class);
        Home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(Home);
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

    public void locations(View view) {
        final int i = 0;
        if(!clicked[i]) {
            clicked[i] = true;
            layoutToAdd[i].addView(viewToInflate[i]);
            final EditText e = (EditText) findViewById(R.id.sys_location_edittext);
            e.setText("" + changes[i]);
            e.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {}

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() != 0) {
                        changes[i] = Integer.parseInt(s.toString());
                        changes_made = true;
                        changes_bool[i] = true;
                    }
                }
            });

            final Button b1 = (Button) findViewById(R.id.sys_location_pos);
            b1.setOnTouchListener(new Button.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch(event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            changes[i]++;
                            changes_made = true;
                            changes_bool[i] = true;
                            e.setText("" + changes[i]);
                            if (Build.VERSION.SDK_INT >= 22)
                                b1.setBackground(getDrawable(R.drawable.material_button_blue));
                            else
                                b1.setBackground(getResources().getDrawable(R.drawable.material_button_blue));
                            break;
                        case MotionEvent.ACTION_UP:
                            if (Build.VERSION.SDK_INT >= 22)
                                b1.setBackground(getDrawable(R.drawable.material_button));
                            else
                                b1.setBackground(getResources().getDrawable(R.drawable.material_button));
                            break;
                    }
                    return false;
                }
            });

            final Button b2 = (Button) findViewById(R.id.sys_location_neg);
            b2.setOnTouchListener(new Button.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch(event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            changes[i]--;
                            changes_made = true;
                            changes_bool[i] = true;
                            e.setText("" + changes[i]);
                            if (Build.VERSION.SDK_INT >= 22)
                                b2.setBackground(getDrawable(R.drawable.material_button_blue));
                            else
                                b2.setBackground(getResources().getDrawable(R.drawable.material_button_blue));
                            break;
                        case MotionEvent.ACTION_UP:
                            if (Build.VERSION.SDK_INT >= 22)
                                b2.setBackground(getDrawable(R.drawable.material_button));
                            else
                                b2.setBackground(getResources().getDrawable(R.drawable.material_button));
                            break;
                    }
                    return false;
                }
            });
        }

        else {
            clicked[i] = false;
            layoutToAdd[i].removeView(viewToInflate[i]);
        }
    }

    public void daily(View view) {
        final int i = 1;
        if(!clicked[i]) {
            clicked[i] = true;
            layoutToAdd[i].addView(viewToInflate[1]);
            TimePicker t = (TimePicker) findViewById(R.id.sys_time);
            t.setIs24HourView(false);
            if (Build.VERSION.SDK_INT >= 23 ) {
                t.setHour(changes[i] / 60);
                t.setMinute(changes[i] % 60);
            }
            else {
                t.setCurrentHour(changes[i] / 60);
                t.setCurrentMinute(changes[i] % 60);
            }

            t.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                @Override
                public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                    changes[i] = minute;
                    changes[i] += (hourOfDay * 60);
                    changes_bool[i] = true;
                    changes_made = true;
                }
            });
        }

        else {
            clicked[i] = false;
            layoutToAdd[i].removeView(viewToInflate[i]);
        }
    }

    public void auto(View view) {
        final int i = 2;
        if(!clicked[i]) {
            clicked[i] = true;
            layoutToAdd[i].addView(viewToInflate[i]);

            final EditText e = (EditText) findViewById(R.id.sys_auto_edittext);
            e.setText("" + changes[i]);
            final ToggleButton b = (ToggleButton) findViewById(R.id.sys_auto_toggle);
            if(changes[i] > 0) {
                b.setText("Enabled");
                e.setEnabled(true);
                if (Build.VERSION.SDK_INT >= 22)
                    b.setBackground(getDrawable(R.drawable.material_button_blue));
                else
                    b.setBackground(getResources().getDrawable(R.drawable.material_button_blue));
            }
            else {
                b.setText("Disabled");
                e.setEnabled(false);
                if (Build.VERSION.SDK_INT >= 22)
                    b.setBackground(getDrawable(R.drawable.material_button));
                else
                    b.setBackground(getResources().getDrawable(R.drawable.material_button));
            }

            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(changes[i] > 0) {
                        changes[i] = 0;
                        e.setText("" + changes[i]);
                        e.setEnabled(false);
                        if (Build.VERSION.SDK_INT >= 22)
                            b.setBackground(getDrawable(R.drawable.material_button));
                        else
                            b.setBackground(getResources().getDrawable(R.drawable.material_button));
                        b.setText("Disabled");
                    }
                    else if(changes[i] == 0) {
                        if(new Modbus(getApplicationContext(), addresses[i]).getValue() > 0)
                            changes[i] = new Modbus(getApplicationContext(), addresses[i]).getValue();
                        else
                            changes[i] = 1;
                        e.setText("" + changes[i]);
                        e.setEnabled(true);
                        if (Build.VERSION.SDK_INT >= 22)
                            b.setBackground(getDrawable(R.drawable.material_button_blue));
                        else
                            b.setBackground(getResources().getDrawable(R.drawable.material_button_blue));
                        b.setText("Enabled");
                    }
                }
            });

            e.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {}

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() != 0) {
                        changes[i] = Integer.parseInt(s.toString());
                        changes_made = true;
                        changes_bool[i] = true;
                    }
                }
            });
        }
        else {
            clicked[i] = false;
            layoutToAdd[i].removeView(viewToInflate[i]);
        }
    }

    public void aMsg(View view) {
        final int i = 3;
        if(!clicked[i]) {
            clicked[i] = true;
            layoutToAdd[i].addView(viewToInflate[i]);

            final EditText e = (EditText) findViewById(R.id.sys_a_msg_edittext);
            e.setText("" + changes[i]);
            e.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {}

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() != 0) {
                        changes[i] = Integer.parseInt(s.toString());
                        changes_made = true;
                        changes_bool[i] = true;
                    }
                }
            });

            final Button b1 = (Button) findViewById(R.id.sys_a_msg_pos);
            b1.setOnTouchListener(new Button.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch(event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            changes[i]++;
                            changes_made = true;
                            changes_bool[i] = true;
                            e.setText("" + changes[i]);
                            if (Build.VERSION.SDK_INT >= 22)
                                b1.setBackground(getDrawable(R.drawable.material_button_blue));
                            else
                                b1.setBackground(getResources().getDrawable(R.drawable.material_button_blue));
                            break;
                        case MotionEvent.ACTION_UP:
                            if (Build.VERSION.SDK_INT >= 22)
                                b1.setBackground(getDrawable(R.drawable.material_button));
                            else
                                b1.setBackground(getResources().getDrawable(R.drawable.material_button));
                            break;
                    }
                    return false;
                }
            });

            final Button b2 = (Button) findViewById(R.id.sys_a_msg_neg);
            b2.setOnTouchListener(new Button.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch(event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            changes[i]--;
                            changes_made = true;
                            changes_bool[i] = true;
                            e.setText("" + changes[i]);
                            if (Build.VERSION.SDK_INT >= 22)
                                b2.setBackground(getDrawable(R.drawable.material_button_blue));
                            else
                                b2.setBackground(getResources().getDrawable(R.drawable.material_button_blue));
                            break;
                        case MotionEvent.ACTION_UP:
                            if (Build.VERSION.SDK_INT >= 22)
                                b2.setBackground(getDrawable(R.drawable.material_button));
                            else
                                b2.setBackground(getResources().getDrawable(R.drawable.material_button));
                            break;
                    }
                    return false;
                }
            });
        }
        else {
            clicked[3] = false;
            layoutToAdd[3].removeView(viewToInflate[3]);
        }
    }

    public void aTime(View view) {
        final int i = 4;
        if(!clicked[i]) {
            clicked[i] = true;
            layoutToAdd[i].addView(viewToInflate[i]);

            TimePicker t = (TimePicker) findViewById(R.id.sys_a_time);
            if (Build.VERSION.SDK_INT >= 23 ) {
                t.setHour(changes[i] / 60);
                t.setMinute(changes[i] % 60);
            }
            else {
                t.setCurrentHour(changes[i] / 60);
                t.setCurrentMinute(changes[i] % 60);
            }

            t.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                @Override
                public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                    changes[i] = minute;
                    changes[i] += (hourOfDay * 60);
                    changes_bool[i] = true;
                    changes_made = true;
                }
            });
        }
        else {
            clicked[i] = false;
            layoutToAdd[i].removeView(viewToInflate[i]);
        }
    }

    public void bMsg(View view) {
        final int i = 5;
        if(!clicked[i]) {
            clicked[i] = true;
            layoutToAdd[i].addView(viewToInflate[i]);

            final EditText e = (EditText) findViewById(R.id.sys_b_msg_edittext);
            e.setText("" + changes[i]);
            e.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {}

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() != 0) {
                        changes[i] = Integer.parseInt(s.toString());
                        changes_made = true;
                        changes_bool[i] = true;
                    }
                }
            });

            final Button b1 = (Button) findViewById(R.id.sys_b_msg_pos);
            b1.setOnTouchListener(new Button.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch(event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            changes[i]++;
                            changes_made = true;
                            changes_bool[i] = true;
                            e.setText("" + changes[i]);
                            if (Build.VERSION.SDK_INT >= 22) {
                                b1.setBackground(getDrawable(R.drawable.material_button_blue));
                            }
                            else {
                                b1.setBackground(getResources().getDrawable(R.drawable.material_button_blue));
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            if (Build.VERSION.SDK_INT >= 22) {
                                b1.setBackground(getDrawable(R.drawable.material_button));
                            }
                            else {
                                b1.setBackground(getResources().getDrawable(R.drawable.material_button));
                            }
                            break;
                    }
                    return false;
                }
            });

            final Button b2 = (Button) findViewById(R.id.sys_b_msg_neg);
            b2.setOnTouchListener(new Button.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch(event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            changes[i]--;
                            changes_made = true;
                            changes_bool[i] = true;
                            e.setText("" + changes[i]);
                            if (Build.VERSION.SDK_INT >= 22) {
                                b2.setBackground(getDrawable(R.drawable.material_button_blue));
                            }
                            else {
                                b2.setBackground(getResources().getDrawable(R.drawable.material_button_blue));
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            if (Build.VERSION.SDK_INT >= 22) {
                                b2.setBackground(getDrawable(R.drawable.material_button));
                            }
                            else {
                                b2.setBackground(getResources().getDrawable(R.drawable.material_button));
                            }
                            break;
                    }
                    return false;
                }
            });
        }
        else {
            clicked[i] = false;
            layoutToAdd[i].removeView(viewToInflate[i]);
        }
    }

    public void bTime(View view) {
        final int i = 6;
        if(!clicked[i]) {
            clicked[i] = true;
            layoutToAdd[i].addView(viewToInflate[i]);

            TimePicker t = (TimePicker) findViewById(R.id.sys_b_time);
            if (Build.VERSION.SDK_INT >= 23 ) {
                t.setHour(changes[i] / 60);
                t.setMinute(changes[i] % 60);
            }
            else {
                t.setCurrentHour(changes[i] / 60);
                t.setCurrentMinute(changes[i] % 60);
            }

            t.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                @Override
                public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                    changes[i] = minute;
                    changes[i] += (hourOfDay * 60);
                    changes_bool[i] = true;
                    changes_made = true;
                }
            });
        }
        else {
            clicked[i] = false;
            layoutToAdd[i].removeView(viewToInflate[i]);
        }
    }

    public void battery(View view) {
        final int i = 7;
        if(!clicked[i]) {
            clicked[i] = true;
            layoutToAdd[i].addView(viewToInflate[i]);

            final EditText e = (EditText) findViewById(R.id.sys_battery_edittext);
            e.setText("" + changes[i]);
            e.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {}

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() != 0) {
                        changes[i] = Integer.parseInt(s.toString());
                        changes_made = true;
                        changes_bool[i] = true;
                    }
                }
            });
            final Button b1 = (Button) findViewById(R.id.sys_battery_pos);
            b1.setOnTouchListener(new Button.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch(event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            changes[i]++;
                            changes_made = true;
                            changes_bool[i] = true;
                            e.setText("" + changes[i]);
                            if (Build.VERSION.SDK_INT >= 22) {
                                b1.setBackground(getDrawable(R.drawable.material_button_blue));
                            }
                            else {
                                b1.setBackground(getResources().getDrawable(R.drawable.material_button_blue));
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            if (Build.VERSION.SDK_INT >= 22) {
                                b1.setBackground(getDrawable(R.drawable.material_button));
                            }
                            else {
                                b1.setBackground(getResources().getDrawable(R.drawable.material_button));
                            }
                            break;
                    }
                    return false;
                }
            });

            final Button b2 = (Button) findViewById(R.id.sys_battery_neg);
            b2.setOnTouchListener(new Button.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch(event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            changes[i]--;
                            changes_made = true;
                            changes_bool[i] = true;
                            e.setText("" + changes[i]);
                            if (Build.VERSION.SDK_INT >= 22) {
                                b2.setBackground(getDrawable(R.drawable.material_button_blue));
                            }
                            else {
                                b2.setBackground(getResources().getDrawable(R.drawable.material_button_blue));
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            if (Build.VERSION.SDK_INT >= 22) {
                                b2.setBackground(getDrawable(R.drawable.material_button));
                            }
                            else {
                                b2.setBackground(getResources().getDrawable(R.drawable.material_button));
                            }
                            break;
                    }
                    return false;
                }
            });
        }
        else {
            clicked[i] = false;
            layoutToAdd[i].removeView(viewToInflate[i]);
        }
    }

    public void airplane(View view) {
        final int i = 8;
        if(!clicked[i]) {
            clicked[i] = true;
            layoutToAdd[i].addView(viewToInflate[i]);

            Spinner dropdown = (Spinner) findViewById(R.id.sys_airplane_spinner);
            String[] items = new String[]{"Disable", "Enable", "Transmit Over 10,000ft"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.style_spinner_items, items);
            dropdown.setAdapter(adapter);
            dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    changes[i] = position;
                    changes_made = true;
                    changes_bool[i] = true;
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
            dropdown.setSelection(changes[i]);
        }
        else {
            clicked[i] = false;
            layoutToAdd[i].removeView(viewToInflate[i]);
        }
    }

    public void power3v3_1(View view) {
        final int i = 9;
        if(!clicked[i]) {
            clicked[i] = true;
            layoutToAdd[i].addView(viewToInflate[i]);

            Spinner dropdown = (Spinner) findViewById(R.id.sys_3v3_1_spinner);
            String[] items = new String[]{"Always Off", "Always On", "Switched For Sensor"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.style_spinner_items, items);
            dropdown.setAdapter(adapter);
            dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    changes[i] = position;
                    changes_made = true;
                    changes_bool[i] = true;
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
            dropdown.setSelection(changes[i]);
        }
        else {
            clicked[i] = false;
            layoutToAdd[i].removeView(viewToInflate[i]);
        }
    }

    public void power3v3_2(View view) {
        final int i = 10;
        if(!clicked[i]) {
            clicked[i] = true;
            layoutToAdd[i].addView(viewToInflate[i]);

            Spinner dropdown = (Spinner) findViewById(R.id.sys_3v3_2_spinner);
            String[] items = new String[]{"Always Off", "Always On", "Switched For Sensor"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.style_spinner_items, items);
            dropdown.setAdapter(adapter);
            dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    changes[i] = position;
                    changes_made = true;
                    changes_bool[i] = true;
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
            dropdown.setSelection(changes[i]);
        }
        else {
            clicked[i] = false;
            layoutToAdd[i].removeView(viewToInflate[i]);
        }
    }

    public void power15v(View view) {
        final int i = 11;
        if(!clicked[i]) {
            clicked[i] = true;
            layoutToAdd[i].addView(viewToInflate[i]);

            Spinner dropdown = (Spinner) findViewById(R.id.sys_15v_spinner);
            String[] items = new String[]{"Always Off", "Always On", "Switched For Sensor"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.style_spinner_items, items);
            dropdown.setAdapter(adapter);
            dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    changes[i] = position;
                    changes_made = true;
                    changes_bool[i] = true;
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
            dropdown.setSelection(changes[i]);
        }
        else {
            clicked[i] = false;
            layoutToAdd[i].removeView(viewToInflate[i]);
        }
    }

    public void cycle(View view) {
        final int i = 12;
        if(!clicked[i]) {
            clicked[i] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            layoutToAdd[i].addView(viewToInflate[i]);

            final EditText e = (EditText) findViewById(R.id.sys_cycle_edittext);
            e.setText("" + changes[i]);
            e.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {}

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() != 0) {
                        changes[i] = Integer.parseInt(s.toString());
                        changes_made = true;
                        changes_bool[i] = true;
                    }
                }
            });

            final Button b1 = (Button) findViewById(R.id.sys_cycle_pos);
            b1.setOnTouchListener(new Button.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch(event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            changes[i]++;
                            changes_made = true;
                            changes_bool[i] = true;
                            e.setText("" + changes[i]);
                            if (Build.VERSION.SDK_INT >= 22) {
                                b1.setBackground(getDrawable(R.drawable.material_button_blue));
                            }
                            else {
                                b1.setBackground(getResources().getDrawable(R.drawable.material_button_blue));
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            if (Build.VERSION.SDK_INT >= 22) {
                                b1.setBackground(getDrawable(R.drawable.material_button));
                            }
                            else {
                                b1.setBackground(getResources().getDrawable(R.drawable.material_button));
                            }
                            break;
                    }
                    return false;
                }
            });

            final Button b2 = (Button) findViewById(R.id.sys_cycle_neg);
            b2.setOnTouchListener(new Button.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch(event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            changes[i]--;
                            changes_made = true;
                            changes_bool[i] = true;
                            e.setText("" + changes[i]);
                            if (Build.VERSION.SDK_INT >= 22) {
                                b2.setBackground(getDrawable(R.drawable.material_button_blue));
                            }
                            else {
                                b2.setBackground(getResources().getDrawable(R.drawable.material_button_blue));
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            if (Build.VERSION.SDK_INT >= 22) {
                                b2.setBackground(getDrawable(R.drawable.material_button));
                            }
                            else {
                                b2.setBackground(getResources().getDrawable(R.drawable.material_button));
                            }
                            break;
                    }
                    return false;
                }
            });
        }
        else {
            clicked[i] = false;
            layoutToAdd[i].removeView(viewToInflate[i]);
        }
    }

    public void vcc(View view) {
        final int i = 13;
        if(!clicked[i]) {
            clicked[i] = true;
            layoutToAdd[i].addView(viewToInflate[i]);

            final EditText e = (EditText) findViewById(R.id.sys_vcc_edittext);
            e.setText("" + changes[i]);
            e.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {}

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() != 0) {
                        changes[i] = Integer.parseInt(s.toString());
                        changes_bool[i] = true;
                        changes_made = true;
                    }
                }
            });

            final Button b1 = (Button) findViewById(R.id.sys_cycle_pos);
            b1.setOnTouchListener(new Button.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch(event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            changes[i]++;
                            changes_made = true;
                            changes_bool[i] = true;
                            e.setText("" + changes[i]);
                            if (Build.VERSION.SDK_INT >= 22) {
                                b1.setBackground(getDrawable(R.drawable.material_button_blue));
                            }
                            else {
                                b1.setBackground(getResources().getDrawable(R.drawable.material_button_blue));
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            if (Build.VERSION.SDK_INT >= 22) {
                                b1.setBackground(getDrawable(R.drawable.material_button));
                            }
                            else {
                                b1.setBackground(getResources().getDrawable(R.drawable.material_button));
                            }
                            break;
                    }
                    return false;
                }
            });

            final Button b2 = (Button) findViewById(R.id.sys_cycle_neg);
            b2.setOnTouchListener(new Button.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch(event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            changes[i]--;
                            changes_made = true;
                            changes_bool[i] = true;
                            e.setText("" + changes[i]);
                            if (Build.VERSION.SDK_INT >= 22) {
                                b2.setBackground(getDrawable(R.drawable.material_button_blue));
                            }
                            else {
                                b2.setBackground(getResources().getDrawable(R.drawable.material_button_blue));
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            if (Build.VERSION.SDK_INT >= 22) {
                                b2.setBackground(getDrawable(R.drawable.material_button));
                            }
                            else {
                                b2.setBackground(getResources().getDrawable(R.drawable.material_button));
                            }
                            break;
                    }
                    return false;
                }
            });
        }
        else {
            clicked[i] = false;
            layoutToAdd[i].removeView(viewToInflate[i]);
        }
    }
}