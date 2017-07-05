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

    LinearLayout[] layoutToAdd = new LinearLayout[14];
    View[] viewToInflate = new View[14];
    boolean[] clicked = new boolean[14];

    public static int[] changes = new int[14];
    public static boolean[] changes_bool = new boolean[14];
    boolean changes_made = false;
    public static final int[] addresses =  {1001,   1002,   1003,   1004,   1005,   1006,   1007,
                                            1009,   1011,   1012,   1013,   1014,   1020,   1021};

    public static final int Size = 14;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.system_parameters);

        layoutToAdd[0] =    (LinearLayout) findViewById(R.id.locations);
        layoutToAdd[1] =    (LinearLayout) findViewById(R.id.daily);
        layoutToAdd[2] =    (LinearLayout) findViewById(R.id.auto);
        layoutToAdd[3] =    (LinearLayout) findViewById(R.id.a_msg);
        layoutToAdd[4] =    (LinearLayout) findViewById(R.id.a_time);
        layoutToAdd[5] =    (LinearLayout) findViewById(R.id.b_msg);
        layoutToAdd[6] =    (LinearLayout) findViewById(R.id.b_time);
        layoutToAdd[7] =    (LinearLayout) findViewById(R.id.battery);
        layoutToAdd[8] =    (LinearLayout) findViewById(R.id.airplane);
        layoutToAdd[9] =    (LinearLayout) findViewById(R.id.power_3v3_1);
        layoutToAdd[10] =   (LinearLayout) findViewById(R.id.power_3v3_2);
        layoutToAdd[11] =   (LinearLayout) findViewById(R.id.power_15v);
        layoutToAdd[12] =   (LinearLayout) findViewById(R.id.cycle);
        layoutToAdd[13] =   (LinearLayout) findViewById(R.id.vcc);
        LayoutTransition transition = new LayoutTransition();

        for(int i = 0; i < 14; i++) {
            layoutToAdd[i].setLayoutTransition(transition);
            changes[i] = new Modbus(getApplicationContext(), addresses[i]).getValue();
            changes_bool[i] = false;
            clicked[i] = true;
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
        /*AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are You Sure You Want To Restore Values?  This Will Overwrite All Values.");
        builder.setPositiveButton("Restore", restoreDialog);
        builder.setNegativeButton("Cancel", restoreDialog);
        builder.show();*/
    }

    /*
    DialogInterface.OnClickListener restoreDialog = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    new Modbus(getApplicationContext(), true);
                    // **********************************************************
                    // **********************************************************
                    // ***** ADD LENGTHY CODE TO SET TEXT OF OPENED OPTIONS *****
                    // **********************************************************
                    // **********************************************************
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        }
    };
    */

    public void locations(View view) {
        if(clicked[0]) {
            clicked[0] = false;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[0] = inflater.inflate(R.layout.z_locations_per_day_child, null);
            layoutToAdd[0].addView(viewToInflate[0]);
            final EditText e = (EditText) findViewById(R.id.locations_per_day);
            e.setText("" + changes[0]);
            e.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {}

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() != 0) {
                        changes[0] = Integer.parseInt(s.toString());
                        changes_made = true;
                        changes_bool[0] = true;
                    }
                }
            });

            final Button b1 = (Button) findViewById(R.id.buttonPos);
            b1.setOnTouchListener(new Button.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch(event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            changes[0]++;
                            changes_made = true;
                            changes_bool[0] = true;
                            e.setText("" + changes[0]);
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

            final Button b2 = (Button) findViewById(R.id.buttonNeg);
            b2.setOnTouchListener(new Button.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch(event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            changes[0]--;
                            changes_made = true;
                            changes_bool[0] = true;
                            e.setText("" + changes[0]);
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
            clicked[0] = true;
            layoutToAdd[0].removeView(viewToInflate[0]);
        }
    }

    public void daily(View view) {
        if(clicked[1]) {
            clicked[1] = false;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[1] = inflater.inflate(R.layout.z_time_daily_gps_msg_child, null);
            layoutToAdd[1].addView(viewToInflate[1]);
            TimePicker t = (TimePicker) findViewById(R.id.dailyGpsMsg);
            t.setIs24HourView(false);
            if (Build.VERSION.SDK_INT >= 23 ) {
                t.setHour(changes[1] / 60);
                t.setMinute(changes[1] % 60);
            }
            else {
                t.setCurrentHour(changes[1] / 60);
                t.setCurrentMinute(changes[1] % 60);
            }

            t.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                @Override
                public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                    changes[1] = minute;
                    changes[1] += (hourOfDay * 60);
                    changes_bool[1] = true;
                    changes_made = true;
                }
            });
        }

        else {
            clicked[1] = true;
            layoutToAdd[1].removeView(viewToInflate[1]);
        }
    }

    public void auto(View view) {
        if(clicked[2]) {
            clicked[2] = false;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[2] = inflater.inflate(R.layout.z_auto_location_child, null);
            layoutToAdd[2].addView(viewToInflate[2]);

            final EditText e = (EditText) findViewById(R.id.auto_editText);
            e.setText("" + changes[2]);

            final Button b = (Button) findViewById(R.id.auto_button);
            if(changes[2] > 0) {
                b.setText("Enabled");
                e.setEnabled(true);
                if (Build.VERSION.SDK_INT >= 22) {
                    b.setBackground(getDrawable(R.drawable.material_button_blue));
                }
                else {
                    b.setBackground(getResources().getDrawable(R.drawable.material_button_blue));
                }
            }
            else {
                b.setText("Disabled");
                e.setEnabled(false);
                if (Build.VERSION.SDK_INT >= 22) {
                    b.setBackground(getDrawable(R.drawable.material_button));
                }
                else {
                    b.setBackground(getResources().getDrawable(R.drawable.material_button));
                }
            }

            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(changes[2] > 0) {
                        changes[2] = 0;
                        e.setText("" + changes[2]);
                        e.setEnabled(false);
                        if (Build.VERSION.SDK_INT >= 22) {
                            b.setBackground(getDrawable(R.drawable.material_button));
                        }
                        else {
                            b.setBackground(getResources().getDrawable(R.drawable.material_button));
                        }
                        b.setText("Disabled");
                    }
                    else if(changes[2] == 0) {
                        if(new Modbus(getApplicationContext(), addresses[2]).getValue() > 0) {
                            changes[2] = new Modbus(getApplicationContext(), addresses[2]).getValue();
                        }
                        else {
                            changes[2] = 1;
                        }
                        e.setText("" + changes[2]);
                        e.setEnabled(true);
                        if (Build.VERSION.SDK_INT >= 22) {
                            b.setBackground(getDrawable(R.drawable.material_button_blue));
                        }
                        else {
                            b.setBackground(getResources().getDrawable(R.drawable.material_button_blue));
                        }
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
                        changes[2] = Integer.parseInt(s.toString());
                        changes_made = true;
                        changes_bool[2] = true;
                    }
                }
            });
        }
        else {
            clicked[2] = true;
            layoutToAdd[2].removeView(viewToInflate[2]);
        }
    }

    public void aMsg(View view) {
        if(clicked[3]) {
            clicked[3] = false;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[3] = inflater.inflate(R.layout.z_a_msg_per_day_child, null);
            layoutToAdd[3].addView(viewToInflate[3]);

            final EditText e = (EditText) findViewById(R.id.a_msg_per_day);
            e.setText("" + changes[3]);
            e.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {}

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() != 0) {
                        changes[3] = Integer.parseInt(s.toString());
                        changes_made = true;
                        changes_bool[3] = true;
                    }
                }
            });

            final Button b1 = (Button) findViewById(R.id.buttonAMsgPos);
            b1.setOnTouchListener(new Button.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch(event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            changes[3]++;
                            changes_made = true;
                            changes_bool[3] = true;
                            e.setText("" + changes[3]);
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

            final Button b2 = (Button) findViewById(R.id.buttonAMsgNeg);
            b2.setOnTouchListener(new Button.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch(event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            changes[3]--;
                            changes_made = true;
                            changes_bool[3] = true;
                            e.setText("" + changes[3]);
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
            clicked[3] = true;
            layoutToAdd[3].removeView(viewToInflate[3]);
        }
    }

    public void aTime(View view) {
        if(clicked[4]) {
            clicked[4] = false;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[4] = inflater.inflate(R.layout.z_a_time_daily_msg_child, null);
            layoutToAdd[4].addView(viewToInflate[4]);

            TimePicker t = (TimePicker) findViewById(R.id.a_time_daily_msg);
            if (Build.VERSION.SDK_INT >= 23 ) {
                t.setHour(changes[4] / 60);
                t.setMinute(changes[4] % 60);
            }
            else {
                t.setCurrentHour(changes[4] / 60);
                t.setCurrentMinute(changes[4] % 60);
            }

            t.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                @Override
                public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                    changes[4] = minute;
                    changes[4] += (hourOfDay * 60);
                    changes_bool[4] = true;
                    changes_made = true;
                }
            });
        }
        else {
            clicked[4] = true;
            layoutToAdd[4].removeView(viewToInflate[4]);
        }
    }

    public void bMsg(View view) {
        if(clicked[5]) {
            clicked[5] = false;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[5] = inflater.inflate(R.layout.z_b_msg_per_day_child, null);
            layoutToAdd[5].addView(viewToInflate[5]);

            final EditText e = (EditText) findViewById(R.id.b_msg_per_day);
            e.setText("" + changes[5]);
            e.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {}

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() != 0) {
                        changes[5] = Integer.parseInt(s.toString());
                        changes_made = true;
                        changes_bool[5] = true;
                    }
                }
            });

            final Button b1 = (Button) findViewById(R.id.buttonBMsgPos);
            b1.setOnTouchListener(new Button.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch(event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            changes[5]++;
                            changes_made = true;
                            changes_bool[5] = true;
                            e.setText("" + changes[5]);
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

            final Button b2 = (Button) findViewById(R.id.buttonBMsgNeg);
            b2.setOnTouchListener(new Button.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch(event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            changes[5]--;
                            changes_made = true;
                            changes_bool[5] = true;
                            e.setText("" + changes[5]);
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
            clicked[5] = true;
            layoutToAdd[5].removeView(viewToInflate[5]);
        }
    }

    public void bTime(View view) {
        if(clicked[6]) {
            clicked[6] = false;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[6] = inflater.inflate(R.layout.z_b_time_daily_msg_child, null);
            layoutToAdd[6].addView(viewToInflate[6]);

            TimePicker t = (TimePicker) findViewById(R.id.b_time_daily_msg);
            if (Build.VERSION.SDK_INT >= 23 ) {
                t.setHour(changes[6] / 60);
                t.setMinute(changes[6] % 60);
            }
            else {
                t.setCurrentHour(changes[6] / 60);
                t.setCurrentMinute(changes[6] % 60);
            }

            t.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                @Override
                public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                    changes[6] = minute;
                    changes[6] += (hourOfDay * 60);
                    changes_bool[6] = true;
                    changes_made = true;
                }
            });
        }
        else {
            clicked[6] = true;
            layoutToAdd[6].removeView(viewToInflate[6]);
        }
    }

    public void battery(View view) {
        if(clicked[7]) {
            clicked[7] = false;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[7] = inflater.inflate(R.layout.z_battery_child, null);
            layoutToAdd[7].addView(viewToInflate[7]);

            EditText e = (EditText) findViewById(R.id.battery_editText);
            e.setText("" + changes[7]);
            e.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {}

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() != 0) {
                        changes[7] = Integer.parseInt(s.toString());
                        changes_made = true;
                        changes_bool[7] = true;
                    }
                }
            });
        }
        else {
            clicked[7] = true;
            layoutToAdd[7].removeView(viewToInflate[7]);
        }
    }

    public void airplane(View view) {
        final int index = 8;
        if(clicked[index]) {
            clicked[index] = false;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[index] = inflater.inflate(R.layout.z_airplane_mode_child, null);
            layoutToAdd[index].addView(viewToInflate[index]);

            AdapterView.OnItemSelectedListener onSpinner = new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    changes[index] = position;
                    changes_made = true;
                    changes_bool[index] = true;
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            };

            Spinner dropdown = (Spinner) findViewById(R.id.airplane_spinner);
            String[] items = new String[]{"Disable", "Enable", "Transmit Over 10,000ft"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.style_spinner_items, items);
            dropdown.setAdapter(adapter);
            dropdown.setOnItemSelectedListener(onSpinner);
            dropdown.setSelection(changes[index]);
        }
        else {
            clicked[index] = true;
            layoutToAdd[index].removeView(viewToInflate[index]);
        }
    }

    public void power3v3_1(View view) {
        final int index = 9;
        if(clicked[index]) {
            clicked[index] = false;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[index] = inflater.inflate(R.layout.z_power_3v3_1_child, null);
            layoutToAdd[index].addView(viewToInflate[index]);

            AdapterView.OnItemSelectedListener onSpinner = new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    changes[index] = position;
                    changes_made = true;
                    changes_bool[index] = true;
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            };

            Spinner dropdown = (Spinner) findViewById(R.id.power_3v3_1_spinner);
            String[] items = new String[]{"Always Off", "Always On", "Switched For Sensor"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.style_spinner_items, items);
            dropdown.setAdapter(adapter);
            dropdown.setOnItemSelectedListener(onSpinner);
            dropdown.setSelection(changes[index]);
        }
        else {
            clicked[index] = true;
            layoutToAdd[index].removeView(viewToInflate[index]);
        }
    }

    public void power3v3_2(View view) {
        final int index = 10;
        if(clicked[index]) {
            clicked[index] = false;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[index] = inflater.inflate(R.layout.z_power_3v3_2_child, null);
            layoutToAdd[index].addView(viewToInflate[index]);

            AdapterView.OnItemSelectedListener onSpinner = new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    changes[index] = position;
                    changes_made = true;
                    changes_bool[index] = true;
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            };

            Spinner dropdown = (Spinner) findViewById(R.id.power_3v3_2_spinner);
            String[] items = new String[]{"Always Off", "Always On", "Switched For Sensor"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.style_spinner_items, items);
            dropdown.setAdapter(adapter);
            dropdown.setOnItemSelectedListener(onSpinner);
            dropdown.setSelection(changes[index]);
        }
        else {
            clicked[index] = true;
            layoutToAdd[index].removeView(viewToInflate[index]);
        }
    }

    public void power15v(View view) {
        final int index = 11;
        if(clicked[index]) {
            clicked[index] = false;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[index] = inflater.inflate(R.layout.z_power_15v_child, null);
            layoutToAdd[index].addView(viewToInflate[index]);

            AdapterView.OnItemSelectedListener onSpinner = new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    changes[index] = position;
                    changes_made = true;
                    changes_bool[index] = true;
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            };

            Spinner dropdown = (Spinner) findViewById(R.id.power_15v_spinner);
            String[] items = new String[]{"Always Off", "Always On", "Switched For Sensor"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.style_spinner_items, items);
            dropdown.setAdapter(adapter);
            dropdown.setOnItemSelectedListener(onSpinner);
            dropdown.setSelection(changes[index]);
        }
        else {
            clicked[index] = true;
            layoutToAdd[index].removeView(viewToInflate[index]);
        }
    }

    public void cycle(View view) {
        final int index = 12;
        if(clicked[index]) {
            clicked[index] = false;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[index] = inflater.inflate(R.layout.z_sensor_cycle_child, null);
            layoutToAdd[index].addView(viewToInflate[8]);

            EditText e = (EditText) findViewById(R.id.sensor_cycle);
            e.setText("" + changes[index]);
            e.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {}

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() != 0) {
                        changes[index] = Integer.parseInt(s.toString());
                        changes_made = true;
                        changes_bool[index] = true;
                    }
                }
            });
        }
        else {
            clicked[index] = true;
            layoutToAdd[index].removeView(viewToInflate[index]);
        }
    }

    public void vcc(View view) {
        final int index = 13;
        if(clicked[index]) {
            clicked[index] = false;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[index] = inflater.inflate(R.layout.z_vcc_wait_time_child, null);
            layoutToAdd[index].addView(viewToInflate[index]);

            EditText e = (EditText) findViewById(R.id.vcc_wait);
            e.setText("" + changes[index]);
            e.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {}

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() != 0) {
                        changes[index] = Integer.parseInt(s.toString());
                        changes_bool[index] = true;
                        changes_made = true;
                    }
                }
            });
        }
        else {
            clicked[index] = true;
            layoutToAdd[index].removeView(viewToInflate[index]);
        }
    }
}