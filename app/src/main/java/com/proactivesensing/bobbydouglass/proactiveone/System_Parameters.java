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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

public class System_Parameters extends AppCompatActivity{

    public static final int Size =                  15;
    public static final int ChangesSizeY =          3;
    public static final int ChangesSizeZ =          15;

    public static LinearLayout[] layoutToAdd =      new LinearLayout[Size];
    public static View[] viewToInflate =            new View[Size];
    boolean[] clicked =                            {false,  false,  false,  false,  false,
                                                    false,  false,  false,  false,  false,
                                                    false,  false,  false,  false,  false};
    public static int[][] changes =               {{-1,     -1,     -1,     -1,     -1,
                                                    -1,     -1,     -1,     -1,     -1,
                                                    -1,     -1,     -1,     -1,     -1},

                                                   {0,      0,      0,      0,      0,
                                                    0,      0,      0,      0,      0,
                                                    0,      0,      0,      0,      0},

                                                   {1022,   1023,   1024,   1025,   1027,
                                                    1028,   1029,   1030,   1031,   1032,
                                                    1009,   1012,   1013,   1014,   1020}};
    boolean changes_made =                          false;
    int[] timeBetweenType =                        {0,      0,      0};
    boolean[] loadedSpinner =                      {false,  false,  false,  false};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.system_parameters);

        layoutToAdd[0] =    (LinearLayout) findViewById(R.id.time_zone);
        layoutToAdd[1] =    (LinearLayout) findViewById(R.id.first_gps_msg);
        layoutToAdd[2] =    (LinearLayout) findViewById(R.id.time_between_gps);
        layoutToAdd[3] =    (LinearLayout) findViewById(R.id.total_gps_msg);
        layoutToAdd[4] =    (LinearLayout) findViewById(R.id.time_a_msg);
        layoutToAdd[5] =    (LinearLayout) findViewById(R.id.time_a_between);
        layoutToAdd[6] =    (LinearLayout) findViewById(R.id.total_a_msg);
        layoutToAdd[7] =    (LinearLayout) findViewById(R.id.time_b_msg);
        layoutToAdd[8] =    (LinearLayout) findViewById(R.id.time_b_between);
        layoutToAdd[9] =    (LinearLayout) findViewById(R.id.total_b_msg);
        layoutToAdd[10] =   (LinearLayout) findViewById(R.id.battery);
        layoutToAdd[11] =   (LinearLayout) findViewById(R.id.power_3v3_1);
        layoutToAdd[12] =   (LinearLayout) findViewById(R.id.power_3v3_2);
        layoutToAdd[13] =   (LinearLayout) findViewById(R.id.power_15v);
        layoutToAdd[14] =   (LinearLayout) findViewById(R.id.cycle);

        for (int i = 0; i < Size; i++) {
            changes[0][i] = new Modbus(getApplicationContext(), changes[2][i]).getValue();
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

    public void negativeToast() {
        Toast.makeText(this, "Input Cannot Be Negative", Toast.LENGTH_SHORT).show();
    }

    public void inputTooLow() {
        Toast.makeText(this, "Input Too Low", Toast.LENGTH_SHORT).show();
    }

    public void inputTooHigh() {
        Toast.makeText(this, "Input Too High", Toast.LENGTH_SHORT).show();
    }

    /* ***** 01 ***** */
    public void timeZone(View view) {
        final int i = 0;
        if(!clicked[i]) {
            clicked[i] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[i] = inflater.inflate(R.layout.z_sys_01_time_zone_child, null);
            layoutToAdd[i].addView(viewToInflate[i]);

            final ToggleButton tgl = (ToggleButton) findViewById(R.id.sys_daylight_toggle);
            tgl.setChecked(((changes[0][i] >> 8) & 0xff) == 1);
            if(tgl.isChecked()){
                if(Build.VERSION.SDK_INT >= 22)
                    tgl.setBackground(getDrawable(R.drawable.material_button_blue));
                else
                    tgl.setBackground(getResources().getDrawable(R.drawable.material_button_blue));
            }
            else {
                if(Build.VERSION.SDK_INT >= 22)
                    tgl.setBackground(getDrawable(R.drawable.material_button));
                else
                    tgl.setBackground(getResources().getDrawable(R.drawable.material_button));
            }
            tgl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (Build.VERSION.SDK_INT >= 22)
                            tgl.setBackground(getDrawable(R.drawable.material_button_blue));
                        else
                            tgl.setBackground(getResources().getDrawable(R.drawable.material_button_blue));
                    }
                    else {
                        if (Build.VERSION.SDK_INT >= 22)
                            tgl.setBackground(getDrawable(R.drawable.material_button));
                        else
                            tgl.setBackground(getResources().getDrawable(R.drawable.material_button));
                    }

                    byte lowByte = (byte) (changes[0][i] * 0xff);
                    byte highByte = (byte) ((isChecked) ? 1 : 0);
                    changes[0][i] = ((lowByte << 8) | (highByte & 0xff));
                    changes[1][i] = 1;
                    changes_made = true;
                }
            });

            Spinner dropdown = (Spinner) findViewById(R.id.sys_time_zone_spinner);
            String[] items =    new String[]   {"GMT -11:00",   "GMT -10:00",   "GMT -9:00",    "GMT -8:00",    "GMT -7:00",
                                                "GMT -6:00",    "GMT -5:00",    "GMT -4:00",    "GMT -3:00",    "GMT -2:00",
                                                "GMT -1:00",    "GMT",          "GMT +1:00",    "GMT +2:00",    "GMT +3:00",
                                                "GMT +4:00",    "GMT +5:00",    "GMT +6:00",    "GMT +7:00",    "GMT +8:00",
                                                "GMT +9:00",    "GMT +10:00",   "GMT +11:00",   "GMT +12:00"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.style_spinner_items, items);
            loadedSpinner[0] = false;
            dropdown.setAdapter(adapter);
            dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(loadedSpinner[0]) {
                        byte lowByte = (byte) ((position - 11) & 0xff);
                        byte highByte = (byte) ((changes[0][i] >> 8) & 0xff);
                        changes[0][i] = (((highByte & 0xff) << 8) | (lowByte & 0xff));
                        changes_made = true;
                        changes[1][i] = 1;
                    }
                    else
                        loadedSpinner[0] = true;
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
            dropdown.setSelection((changes[0][i] + 11) & 0xff);
        }

        else {
            clicked[i] = false;
            layoutToAdd[i].removeView(viewToInflate[i]);
        }
    }

    /* ***** 02 ***** */
    public void firstGpsMsg(View view) {
        final int i = 1;
        if(!clicked[i]) {
            clicked[i] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[i] = inflater.inflate(R.layout.z_sys_02_first_gps_msg_child, null);
            layoutToAdd[i].addView(viewToInflate[i]);

            TimePicker t = (TimePicker) findViewById(R.id.sys_first_gps_msg);
            t.setIs24HourView(false);
            if (Build.VERSION.SDK_INT >= 23 ) {
                t.setHour((changes[0][i] >> 8) & 0xff);
                t.setMinute(changes[0][i] & 0xff);
            }
            else {
                t.setCurrentHour((changes[0][i] >> 8) & 0xff);
                t.setCurrentMinute(changes[0][i] & 0xff);
            }

            t.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                @Override
                public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                    changes[0][i] = (((((byte) hourOfDay) & 0xff) << 8) | (((byte) minute) & 0xff));
                    changes[1][i] = 1;
                    changes_made = true;
                }
            });
        }

        else {
            clicked[i] = false;
            layoutToAdd[i].removeView(viewToInflate[i]);
        }
    }

    /* ***** 03 ***** */
    public void timeBetweenGps(View view) {
        final int i = 2;
        if(!clicked[i]) {
            clicked[i] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[i] = inflater.inflate(R.layout.z_sys_03_time_between_gps_child, null);
            layoutToAdd[i].addView(viewToInflate[i]);

            final TextView t = (TextView) findViewById(R.id.textView4);

            final int[] mult = {1440, 60, 1};
            final int[] curr = new int[3];
            final EditText[] e = {(EditText) findViewById(R.id.sys_time_between_gps_days_edittext),
                    (EditText) findViewById(R.id.sys_time_between_gps_hours_edittext),
                    (EditText) findViewById(R.id.sys_time_between_gps_mins_edittext)};

            curr[0] = (changes[0][i] / mult[0]);
            curr[1] = ((changes[0][i] % mult[0]) / mult[1]);
            curr[2] = ((changes[0][i] % mult[0]) % mult[1]);

            for (int j = 0; j < 3; j++) {
                final int h = j;
                e[h].setText("" + curr[h]);
                e[h].addTextChangedListener(new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {}
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.length() != 0) {
                            int total;
                            switch (h) {
                                case 0:
                                    total = ((Integer.parseInt(s.toString()) * mult[0])
                                            + (Integer.parseInt(e[1].getText().toString()) * mult[1])
                                            + (Integer.parseInt(e[2].getText().toString()) * mult[2]));
                                    break;
                                case 1:
                                    total = ((Integer.parseInt(s.toString()) * mult[1])
                                            + (Integer.parseInt(e[0].getText().toString()) * mult[0])
                                            + (Integer.parseInt(e[2].getText().toString()) * mult[2]));
                                    break;
                                default:
                                    total = ((Integer.parseInt(s.toString()) * mult[2])
                                            + (Integer.parseInt(e[0].getText().toString()) * mult[0])
                                            + (Integer.parseInt(e[1].getText().toString()) * mult[1]));
                                    break;
                            }
                            if (total > 17 && total < 65536) {
                                changes[0][i] = total;
                                changes[1][i] = 1;
                                changes_made = true;
                            }
                            else if (total < 18) {
                                inputTooLow();
                            }
                            else {
                                inputTooHigh();
                            }
                        }
                    }
                });
            }

            final Button[][] button = {{(Button) findViewById(R.id.sys_time_between_gps_days_neg),
                                        (Button) findViewById(R.id.sys_time_between_gps_days_pos)},
                                       {(Button) findViewById(R.id.sys_time_between_gps_hours_neg),
                                        (Button) findViewById(R.id.sys_time_between_gps_hours_pos)},
                                       {(Button) findViewById(R.id.sys_time_between_gps_mins_neg),
                                        (Button) findViewById(R.id.sys_time_between_gps_mins_pos)}};

            for(int j = 0; j < 3; j++) {
                for (int k = 0; k < 2; k++) {
                    final int l = j, m = k;
                    button[l][m].setOnTouchListener(new Button.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            switch (event.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    if (Build.VERSION.SDK_INT >= 22)
                                        button[l][m].setBackground(getDrawable(R.drawable.material_button_blue));
                                    else
                                        button[l][m].setBackground(getResources().getDrawable(R.drawable.material_button_blue));

                                    if(m == 0) {
                                        int temp = (changes[0][i] - mult[l]);
                                        if (temp > 17) {
                                            changes[0][i] -= mult[l];
                                            changes[1][i] = 1;
                                            changes_made = true;

                                            curr[0] = (changes[0][i] / mult[0]);
                                            curr[1] = ((changes[0][i] % mult[0]) / mult[1]);
                                            curr[2] = ((changes[0][i] % mult[0]) % mult[1]);

                                            e[0].setText("" + curr[0]);
                                            e[1].setText("" + curr[1]);
                                            e[2].setText("" + curr[2]);
                                            t.setText("" + changes[0][i]);
                                        } else {
                                            inputTooLow();
                                        }
                                    }
                                    else {
                                        int temp = (changes[0][i] + mult[l]);
                                        if (temp < 65536) {
                                            changes[0][i] += mult[l];
                                            changes[1][i] = 1;
                                            changes_made = true;

                                            curr[0] = (changes[0][i] / mult[0]);
                                            curr[1] = ((changes[0][i] % mult[0]) / mult[1]);
                                            curr[2] = ((changes[0][i] % mult[0]) % mult[1]);

                                            e[0].setText("" + curr[0]);
                                            e[1].setText("" + curr[1]);
                                            e[2].setText("" + curr[2]);
                                            t.setText("" + changes[0][i]);
                                        } else {
                                            inputTooHigh();
                                        }
                                    }
                                    break;
                                case MotionEvent.ACTION_UP:
                                    if (Build.VERSION.SDK_INT >= 22)
                                        button[l][m].setBackground(getDrawable(R.drawable.material_button));
                                    else
                                        button[l][m].setBackground(getResources().getDrawable(R.drawable.material_button));
                                    break;
                            }
                            return false;
                        }
                    });
                }
            }
        }
        else {
            clicked[i] = false;
            layoutToAdd[i].removeView(viewToInflate[i]);
        }
    }

    /* ***** 04 ***** */
    public void totalGpsMsg(View view) {
        final int i = 3;
        if(!clicked[i]) {
            clicked[i] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[i] = inflater.inflate(R.layout.z_sys_04_total_gps_msg_child, null);
            layoutToAdd[i].addView(viewToInflate[i]);

            final EditText e = (EditText) findViewById(R.id.sys_total_gps_msg_edittext);
            e.setText("" + changes[0][i]);
            e.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {}
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() != 0) {
                        int value = Integer.parseInt(s.toString());
                        if(value > 71) {
                            changes[0][i] = value;
                            changes[1][i] = 1;
                            changes_made = true;
                        }
                        else
                            inputTooLow();
                    }
                }
            });

            final Button[] button =    {(Button) findViewById(R.id.sys_total_gps_msg_neg),
                                        (Button) findViewById(R.id.sys_total_gps_msg_pos)};
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
                                    if(changes[0][i] > 72) {
                                        changes[0][i]--;
                                        changes[1][i] = 1;
                                        changes_made = true;
                                        e.setText("" + changes[0][i]);
                                    }
                                    else
                                        inputTooLow();
                                }
                                else {
                                    changes[0][i]++;
                                    changes[1][i] = 1;
                                    changes_made = true;
                                    e.setText("" + changes[0][i]);
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

    /* ***** 05 ***** */
    public void timeAMsg(View view) {
        final int i = 4;
        if(!clicked[i]) {
            clicked[i] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[i] = inflater.inflate(R.layout.z_sys_05_time_a_msg_child, null);
            layoutToAdd[i].addView(viewToInflate[i]);

            TimePicker t = (TimePicker) findViewById(R.id.sys_a_time);
            t.setIs24HourView(false);
            if (Build.VERSION.SDK_INT >= 23 ) {
                t.setHour((changes[0][i] >> 8) & 0xff);
                t.setMinute(changes[0][i] & 0xff);
            }
            else {
                t.setCurrentHour((changes[0][i] >> 8) & 0xff);
                t.setCurrentMinute(changes[0][i] & 0xff);
            }

            t.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                @Override
                public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                    changes[0][i] = (((((byte) hourOfDay) & 0xff) << 8) | (((byte) minute) & 0xff));
                    changes[1][i] = 1;
                    changes_made = true;
                }
            });
        }
        else {
            clicked[i] = false;
            layoutToAdd[i].removeView(viewToInflate[i]);
        }
    }

    /* ***** 06 ***** */
    public void timeABetween(View view) {
        final int i = 5;
        if(!clicked[i]) {
            clicked[i] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[i] = inflater.inflate(R.layout.z_sys_06_time_a_between_child, null);
            layoutToAdd[i].addView(viewToInflate[i]);

            final int[] mult =   {1440, 60, 1};
            final int[] curr =    new int[3];
            final EditText[] e =   {(EditText) findViewById(R.id.sys_time_a_between_days_edittext),
                                    (EditText) findViewById(R.id.sys_time_a_between_hours_edittext),
                                    (EditText) findViewById(R.id.sys_time_a_between_mins_edittext)};

            curr[0] = (changes[0][i] / mult[0]);
            curr[1] = ((changes[0][i] - (curr[0] * mult[0])) / mult[1]);
            curr[2] = (changes[0][i] - (curr[0] * mult[0]) - (curr[1] * mult[1]));

            for(int j = 0; j < 3; j++) {
                final int h = j;
                e[h].setText("" + curr[h]);
                e[h].addTextChangedListener(new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {}
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.length() != 0) {
                            int total;
                            switch(h) {
                                case 0:
                                    total = (Integer.parseInt(s.toString()) + curr[1] + curr[2]);
                                    break;
                                case 1:
                                    total = (Integer.parseInt(s.toString()) + curr[0] + curr[2]);
                                    break;
                                default:
                                    total = (Integer.parseInt(s.toString()) + curr[0] + curr[1]);
                                    break;
                            }
                            if(total > 17 && total < 65536) {
                                changes[0][i] = total;
                                changes[1][i] = 1;
                                changes_made = true;
                            }
                            else if(total < 18)
                                inputTooLow();
                            else
                                inputTooHigh();
                        }
                    }
                });
            }

            final Button[][] button = {{(Button) findViewById(R.id.sys_time_a_between_days_neg),
                                        (Button) findViewById(R.id.sys_time_a_between_days_pos)},
                                       {(Button) findViewById(R.id.sys_time_a_between_hours_neg),
                                        (Button) findViewById(R.id.sys_time_a_between_hours_pos)},
                                       {(Button) findViewById(R.id.sys_time_a_between_mins_neg),
                                        (Button) findViewById(R.id.sys_time_a_between_mins_pos)}};
            for(int j = 0; j < 3; j++) {
                for(int k = 0; k < 2; k++) {
                    final int l = k, h = j;
                    button[h][l].setOnTouchListener(new Button.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            switch(event.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    if (Build.VERSION.SDK_INT >= 22)
                                        button[h][l].setBackground(getDrawable(R.drawable.material_button_blue));
                                    else
                                        button[h][l].setBackground(getResources().getDrawable(R.drawable.material_button_blue));

                                    if (l == 0) {
                                        if(changes[0][i] > 18) {
                                            changes[0][i]--;
                                            changes[1][i] = 1;
                                            changes_made = true;

                                            curr[0] = (changes[0][i] / mult[0]);
                                            curr[1] = ((changes[0][i] - (curr[0] * mult[0])) / mult[1]);
                                            curr[2] = (changes[0][i] - (curr[0] * mult[0]) - (curr[1] * mult[1]));

                                            e[0].setText("" + curr[0]);
                                            e[1].setText("" + curr[1]);
                                            e[2].setText("" + curr[2]);
                                        }
                                        else
                                            inputTooLow();
                                    }
                                    else {
                                        if(changes[0][i] < 65535) {
                                            changes[0][i]++;
                                            changes[1][i] = 1;
                                            changes_made = true;

                                            curr[0] = (changes[0][i] / mult[0]);
                                            curr[1] = ((changes[0][i] - (curr[0] * mult[0])) / mult[1]);
                                            curr[2] = (changes[0][i] - (curr[0] * mult[0]) - (curr[1] * mult[1]));

                                            e[0].setText("" + curr[0]);
                                            e[1].setText("" + curr[1]);
                                            e[2].setText("" + curr[2]);
                                        }
                                        else
                                            inputTooHigh();
                                    }
                                    break;
                                case MotionEvent.ACTION_UP:
                                    if (Build.VERSION.SDK_INT >= 22)
                                        button[h][l].setBackground(getDrawable(R.drawable.material_button));
                                    else
                                        button[h][l].setBackground(getResources().getDrawable(R.drawable.material_button));
                                    break;
                            }
                            return false;
                        }
                    });
                }
            }
        }
        else {
            clicked[i] = false;
            layoutToAdd[i].removeView(viewToInflate[i]);
        }
    }

    /* ***** 07 ***** */
    public void totalAMsg(View view) {
        final int i = 6;
        if(!clicked[i]) {
            clicked[i] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[i] = inflater.inflate(R.layout.z_sys_07_total_a_msg_child, null);
            layoutToAdd[i].addView(viewToInflate[i]);

            final EditText e = (EditText) findViewById(R.id.sys_total_a_msg_edittext);
            e.setText("" + changes[0][i]);
            e.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {}
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() != 0) {
                        int value = Integer.parseInt(s.toString());
                        if(value > 71) {
                            changes[0][i] = value;
                            changes[1][i] = 1;
                            changes_made = true;
                        }
                        else
                            inputTooLow();
                    }
                }
            });

            final Button[] button =    {(Button) findViewById(R.id.sys_total_a_msg_neg),
                                        (Button) findViewById(R.id.sys_total_a_msg_pos)};
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
                                    if(changes[0][i] > 72) {
                                        changes[0][i]--;
                                        changes[1][i] = 1;
                                        changes_made = true;
                                        e.setText("" + changes[0][i]);
                                    }
                                    else
                                        inputTooLow();
                                }
                                else {
                                    changes[0][i]++;
                                    changes[1][i] = 1;
                                    changes_made = true;
                                    e.setText("" + changes[0][i]);
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

    /* ***** 08 ***** */
    public void timeBMsg(View view) {
        final int i = 7;
        if(!clicked[i]) {
            clicked[i] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[i] = inflater.inflate(R.layout.z_sys_08_time_b_msg_child, null);
            layoutToAdd[i].addView(viewToInflate[i]);

            TimePicker t = (TimePicker) findViewById(R.id.sys_b_time);
            t.setIs24HourView(false);
            if (Build.VERSION.SDK_INT >= 23 ) {
                t.setHour((changes[0][i] >> 8) & 0xff);
                t.setMinute(changes[0][i] & 0xff);
            }
            else {
                t.setCurrentHour((changes[0][i] >> 8) & 0xff);
                t.setCurrentMinute(changes[0][i] & 0xff);
            }

            t.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                @Override
                public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                    changes[0][i] = (((((byte) hourOfDay) & 0xff) << 8) | (((byte) minute) & 0xff));
                    changes[1][i] = 1;
                    changes_made = true;
                }
            });
        }
        else {
            clicked[i] = false;
            layoutToAdd[i].removeView(viewToInflate[i]);
        }
    }

    /* ***** 09 ***** */
    public void timeBBetween(View view) {
        final int i = 8;
        if(!clicked[i]) {
            clicked[i] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[i] = inflater.inflate(R.layout.z_sys_09_time_b_between_child, null);
            layoutToAdd[i].addView(viewToInflate[i]);

            final int[] mult =   {1440, 60, 1};
            final int[] curr =    new int[3];
            final EditText[] e =   {(EditText) findViewById(R.id.sys_time_b_between_days_edittext),
                                    (EditText) findViewById(R.id.sys_time_b_between_hours_edittext),
                                    (EditText) findViewById(R.id.sys_time_b_between_mins_edittext)};

            curr[0] = (changes[0][i] / mult[0]);
            curr[1] = ((changes[0][i] - (curr[0] * mult[0])) / mult[1]);
            curr[2] = (changes[0][i] - (curr[0] * mult[0]) - (curr[1] * mult[1]));

            for(int j = 0; j < 3; j++) {
                final int h = j;
                e[h].setText("" + curr[h]);
                e[h].addTextChangedListener(new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {}
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.length() != 0) {
                            int total;
                            switch(h) {
                                case 0:
                                    total = (Integer.parseInt(s.toString()) + curr[1] + curr[2]);
                                    break;
                                case 1:
                                    total = (Integer.parseInt(s.toString()) + curr[0] + curr[2]);
                                    break;
                                default:
                                    total = (Integer.parseInt(s.toString()) + curr[0] + curr[1]);
                                    break;
                            }
                            if(total > 17 && total < 65536) {
                                changes[0][i] = total;
                                changes[1][i] = 1;
                                changes_made = true;
                            }
                            else if(total < 18)
                                inputTooLow();
                            else
                                inputTooHigh();
                        }
                    }
                });
            }

            final Button[][] button = {{(Button) findViewById(R.id.sys_time_b_between_days_neg),
                                        (Button) findViewById(R.id.sys_time_b_between_days_pos)},
                                       {(Button) findViewById(R.id.sys_time_b_between_hours_neg),
                                        (Button) findViewById(R.id.sys_time_b_between_hours_pos)},
                                       {(Button) findViewById(R.id.sys_time_b_between_mins_neg),
                                        (Button) findViewById(R.id.sys_time_b_between_mins_pos)}};
            for(int j = 0; j < 3; j++) {
                for(int k = 0; k < 2; k++) {
                    final int l = k, h = j;
                    button[h][l].setOnTouchListener(new Button.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            switch(event.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    if (Build.VERSION.SDK_INT >= 22)
                                        button[h][l].setBackground(getDrawable(R.drawable.material_button_blue));
                                    else
                                        button[h][l].setBackground(getResources().getDrawable(R.drawable.material_button_blue));

                                    if (l == 0) {
                                        if(changes[0][i] > 18) {
                                            changes[0][i]--;
                                            changes[1][i] = 1;
                                            changes_made = true;

                                            curr[0] = (changes[0][i] / mult[0]);
                                            curr[1] = ((changes[0][i] - (curr[0] * mult[0])) / mult[1]);
                                            curr[2] = (changes[0][i] - (curr[0] * mult[0]) - (curr[1] * mult[1]));

                                            e[0].setText("" + curr[0]);
                                            e[1].setText("" + curr[1]);
                                            e[2].setText("" + curr[2]);
                                        }
                                        else
                                            inputTooLow();
                                    }
                                    else {
                                        if(changes[0][i] < 65535) {
                                            changes[0][i]++;
                                            changes[1][i] = 1;
                                            changes_made = true;

                                            curr[0] = (changes[0][i] / mult[0]);
                                            curr[1] = ((changes[0][i] - (curr[0] * mult[0])) / mult[1]);
                                            curr[2] = (changes[0][i] - (curr[0] * mult[0]) - (curr[1] * mult[1]));

                                            e[0].setText("" + curr[0]);
                                            e[1].setText("" + curr[1]);
                                            e[2].setText("" + curr[2]);
                                        }
                                        else
                                            inputTooHigh();
                                    }
                                    break;
                                case MotionEvent.ACTION_UP:
                                    if (Build.VERSION.SDK_INT >= 22)
                                        button[h][l].setBackground(getDrawable(R.drawable.material_button));
                                    else
                                        button[h][l].setBackground(getResources().getDrawable(R.drawable.material_button));
                                    break;
                            }
                            return false;
                        }
                    });
                }
            }
        }
        else {
            clicked[i] = false;
            layoutToAdd[i].removeView(viewToInflate[i]);
        }
    }

    /* ***** 10 ***** */
    public void totalBMsg(View view) {
        final int i = 9;
        if(!clicked[i]) {
            clicked[i] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[i] = inflater.inflate(R.layout.z_sys_10_total_b_msg_child, null);
            layoutToAdd[i].addView(viewToInflate[i]);

            final EditText e = (EditText) findViewById(R.id.sys_total_b_msg_edittext);
            e.setText("" + changes[0][i]);
            e.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {}
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() != 0) {
                        int value = Integer.parseInt(s.toString());
                        if(value > 71) {
                            changes[0][i] = value;
                            changes[1][i] = 1;
                            changes_made = true;
                        }
                        else
                            inputTooLow();
                    }
                }
            });

            final Button[] button =    {(Button) findViewById(R.id.sys_total_b_msg_neg),
                                        (Button) findViewById(R.id.sys_total_b_msg_pos)};
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
                                    if(changes[0][i] > 72) {
                                        changes[0][i]--;
                                        changes[1][i] = 1;
                                        changes_made = true;
                                        e.setText("" + changes[0][i]);
                                    }
                                    else
                                        inputTooLow();
                                }
                                else {
                                    changes[0][i]++;
                                    changes[1][i] = 1;
                                    changes_made = true;
                                    e.setText("" + changes[0][i]);
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

    /* ***** 11 ***** */
    public void battery(View view) {
        final int i = 10;
        if(!clicked[i]) {
            clicked[i] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[i] = inflater.inflate(R.layout.z_sys_11_battery_child, null);
            layoutToAdd[i].addView(viewToInflate[i]);

            final EditText e = (EditText) findViewById(R.id.sys_battery_edittext);
            e.setText("" + changes[0][i]);
            e.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {}
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() != 0) {
                        int value = Integer.parseInt(s.toString());
                        if(value > -1) {
                            changes[0][i] = value;
                            changes[1][i] = 1;
                            changes_made = true;
                        }
                        else
                            negativeToast();
                    }
                }
            });

            final Button[] button =    {(Button) findViewById(R.id.sys_battery_neg),
                                        (Button) findViewById(R.id.sys_battery_pos)};
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
                                    if(changes[0][i] > 0) {
                                        changes[0][i]--;
                                        changes[1][i] = 1;
                                        changes_made = true;
                                        e.setText("" + changes[0][i]);
                                    }
                                    else
                                        negativeToast();
                                }
                                else {
                                    changes[0][i]++;
                                    changes[1][i] = 1;
                                    changes_made = true;
                                    e.setText("" + changes[0][i]);
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

    /* ***** 12 ***** */
    public void power3v3_1(View view) {
        final int i = 11;
        if(!clicked[i]) {
            clicked[i] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[i] = inflater.inflate(R.layout.z_sys_12_3v3_1_child, null);
            layoutToAdd[i].addView(viewToInflate[i]);

            Spinner dropdown = (Spinner) findViewById(R.id.sys_3v3_1_spinner);
            String[] items = new String[]{"Always Off", "Always On", "Switched For Sensor"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.style_spinner_items, items);
            dropdown.setAdapter(adapter);
            loadedSpinner[1] = false;
            dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(loadedSpinner[1]) {
                        changes[0][i] = position;
                        changes_made = true;
                        changes[1][i] = 1;
                    }
                    else
                        loadedSpinner[1] = true;
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
            dropdown.setSelection(changes[0][i]);
        }
        else {
            clicked[i] = false;
            layoutToAdd[i].removeView(viewToInflate[i]);
        }
    }

    /* ***** 13 ***** */
    public void power3v3_2(View view) {
        final int i = 12;
        if(!clicked[i]) {
            clicked[i] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[i] = inflater.inflate(R.layout.z_sys_13_3v3_2_child, null);
            layoutToAdd[i].addView(viewToInflate[i]);

            Spinner dropdown = (Spinner) findViewById(R.id.sys_3v3_2_spinner);
            String[] items = new String[]{"Always Off", "Always On", "Switched For Sensor"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.style_spinner_items, items);
            dropdown.setAdapter(adapter);
            loadedSpinner[2] = false;
            dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(loadedSpinner[2]) {
                        changes[0][i] = position;
                        changes_made = true;
                        changes[1][i] = 1;
                    }
                    else
                        loadedSpinner[2] = true;
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
            dropdown.setSelection(changes[0][i]);
        }
        else {
            clicked[i] = false;
            layoutToAdd[i].removeView(viewToInflate[i]);
        }
    }

    /* ***** 14 ***** */
    public void power15v(View view) {
        final int i = 13;
        if(!clicked[i]) {
            clicked[i] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[i] = inflater.inflate(R.layout.z_sys_14_15v_child, null);
            layoutToAdd[i].addView(viewToInflate[i]);

            Spinner dropdown = (Spinner) findViewById(R.id.sys_15v_spinner);
            String[] items = new String[]{"Always Off", "Always On", "Switched For Sensor"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.style_spinner_items, items);
            dropdown.setAdapter(adapter);
            loadedSpinner[3] = false;
            dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(loadedSpinner[3]) {
                        changes[0][i] = position;
                        changes_made = true;
                        changes[1][i] = 1;
                    }
                    else
                        loadedSpinner[3] = true;
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
            dropdown.setSelection(changes[0][i]);
        }
        else {
            clicked[i] = false;
            layoutToAdd[i].removeView(viewToInflate[i]);
        }
    }

    /* ***** 15 ***** */
    public void cycle(View view) {
        final int i = 14;
        if(!clicked[i]) {
            clicked[i] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[i] = inflater.inflate(R.layout.z_sys_15_sensor_cycle_child, null);
            layoutToAdd[i].addView(viewToInflate[i]);

            final EditText e = (EditText) findViewById(R.id.sys_cycle_edittext);
            e.setText("" + changes[0][i]);
            e.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {}
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() != 0) {
                        int value = Integer.parseInt(s.toString());
                        if(value > -1) {
                            changes[0][i] = value;
                            changes[1][i] = 1;
                            changes_made = true;
                        }
                        else
                            negativeToast();
                    }
                }
            });

            final Button[] button =    {(Button) findViewById(R.id.sys_cycle_neg),
                                        (Button) findViewById(R.id.sys_cycle_pos)};
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
                                    if(changes[0][i] > 0) {
                                        changes[0][i]--;
                                        changes[1][i] = 1;
                                        changes_made = true;
                                        e.setText("" + changes[0][i]);
                                    }
                                    else
                                        negativeToast();
                                }
                                else {
                                    changes[0][i]++;
                                    changes[1][i] = 1;
                                    changes_made = true;
                                    e.setText("" + changes[0][i]);
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
}