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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class Sensors_External extends AppCompatActivity {

    LinearLayout[] layoutToAdd = new LinearLayout[6];
    View[] viewToInflate = new View[6];
    boolean[] clicked = new boolean[6];

    public static int[] changes = new int[56];
    public static boolean[] changes_bool = new boolean[56];
    public static int[] addresses =    {1100,   1101,   1102,   1103,   1104,   1105,   1106,   1107,
                                        1108,   1109,   1110,   1111,   1112,   1113,   1114,   1115,
                                        1116,   1117,   1118,   1119,   1120,   1121,   1122,   1123,
                                        1124,   1125,   1126,   1127,   1128,   1129,   1130,   1131,
                                        1132,   1133,   1134,   1135,   1136,   1137,   1138,   1139,
                                        1140,   1141,   1142,   1143,   1144,   1145,   1146,   1147,
                                        1148,   1149,   1150,   1151,   1152,   1153,   1154,   1155};

    public static final int Size = 56;

    boolean changes_made = false;
    int config_low = 0;
    int config_high = 0;
    int sensorNum;

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
        LayoutTransition transition = new LayoutTransition();

        for(int i = 0; i < 6; i++) {
            layoutToAdd[i].setLayoutTransition(transition);
            clicked[i] = true;
        }

        for(int i = 0; i < 52; i++) {
            changes[i] = new Modbus(getApplicationContext(), addresses[i]).getValue();
            changes_bool[i] = false;
        }

        sensorNum = 0;
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
        /*AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are You Sure You Want To Restore Default Values?  This Will Overwrite All Values.");
        builder.setPositiveButton("Restore", restoreDialog);
        builder.setNegativeButton("Cancel", restoreDialog);
        builder.show();*/
    }

    DialogInterface.OnClickListener restoreDialog = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    new Modbus(getApplicationContext(), true);
                    /* ********************************************************** */
                    /* ********************************************************** */
                    /* ***** ADD LENGTHY CODE TO SET TEXT OF OPENED OPTIONS ***** */
                    /* ********************************************************** */
                    /* ********************************************************** */
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        }
    };

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
            if (Build.VERSION.SDK_INT >= 22) {
                offSensors[i].setBackground(getDrawable(R.drawable.material_button));
            }
            else {
                offSensors[i].setBackground(getResources().getDrawable(R.drawable.material_button));
            }
        }

        if (Build.VERSION.SDK_INT >= 22) {
            selectedSensor.setBackground(getDrawable(R.drawable.material_button_blue));
        }
        else {
            selectedSensor.setBackground(getResources().getDrawable(R.drawable.material_button_blue));
        }

        if(!clicked[2]) {
            TextView lowText = (TextView) findViewById(R.id.low_range_text);
            TextView highText = (TextView) findViewById(R.id.high_range_text);
            lowText.setText("Low Range For Sensor " + (sensorNum + 1));
            highText.setText("High Range For Sensor " + (sensorNum + 1));
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

    public int combineBytes(int low, int high) {
        return ((low & 0x00ff) << 8) | ((high & 0x00ff));
    }

    public int getHighInt(int combined) {
        return (combined & 0x00ff);
    }

    public int getLowInt(int combined) {
        return ((combined >> 8) & 0x00ff);
    }

    public void configuration(View view) {
        if(clicked[0]) {
            clicked[0] = false;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[0] = inflater.inflate(R.layout.x_config_child, null);
            layoutToAdd[0].addView(viewToInflate[0]);

            config_low = getLowInt(changes[0 + (sensorNum * 14)]);
            config_high = getHighInt(changes[0 + (sensorNum * 14)]);

            Spinner dropdown_low = (Spinner) findViewById(R.id.config_low_spinner);
            String[] items_low = new String[]{"Disabled", "Normally Open", "Normally Closed", "Pulse", "Resistive", "4-20mA", "Voltage"};
            ArrayAdapter<String> adapter_low = new ArrayAdapter<String>(this, R.layout.style_spinner_items, items_low);
            dropdown_low.setAdapter(adapter_low);
            dropdown_low.setOnItemSelectedListener(onSpinnerLow);
            dropdown_low.setSelection(config_low);

            Spinner dropdown_high = (Spinner) findViewById(R.id.config_high_spinner);
            String[] items_high = new String[]{"None", "3V3 #1", "3V3 #2", "15V"};
            ArrayAdapter<String> adapter_high = new ArrayAdapter<String>(this, R.layout.style_spinner_items, items_high);
            dropdown_high.setAdapter(adapter_high);
            dropdown_high.setOnItemSelectedListener(onSpinnerHigh);
            dropdown_high.setSelection(config_high);
        }
        else {
            clicked[0] = true;
            layoutToAdd[0].removeView(viewToInflate[0]);
        }
    }

    AdapterView.OnItemSelectedListener onSpinnerLow = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            config_low = position;
            changes[0 + (sensorNum * 14)] = combineBytes(config_low, config_high);
            changes_made = true;
            changes_bool[0 + (sensorNum * 14)] = true;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    AdapterView.OnItemSelectedListener onSpinnerHigh = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            config_high = position;
            changes[0 + (sensorNum * 14)] = combineBytes(config_low, config_high);
            changes_made = true;
            changes_bool[0 + (sensorNum * 14)] = true;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    public void alarmRecog(View view) {
        if(clicked[1]) {
            clicked[1] = false;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[1] = inflater.inflate(R.layout.x_alarm_recog_child, null);
            layoutToAdd[1].addView(viewToInflate[1]);

            EditText recog = (EditText) findViewById(R.id.alarm_recog);
            recog.setText("" + changes[1 + (sensorNum * 14)]);
            recog.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {}

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() != 0) {
                        changes[1 + (sensorNum * 14)] = Integer.parseInt(s.toString());
                        changes_made = true;
                        changes_bool[1 + (sensorNum * 14)] = true;
                    }
                }
            });
        }
        else {
            clicked[1] = true;
            layoutToAdd[1].removeView(viewToInflate[1]);
        }
    }

    public void values(View view) {
        if(clicked[2]) {
            clicked[2] = false;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[2] = inflater.inflate(R.layout.x_values_child, null);
            layoutToAdd[2].addView(viewToInflate[2]);

            TextView lowText = (TextView) findViewById(R.id.low_range_text);
            TextView highText = (TextView) findViewById(R.id.high_range_text);
            lowText.setText("Low Range For Sensor " + (sensorNum + 1));
            highText.setText("High Range For Sensor " + (sensorNum + 1));

            EditText low = (EditText) findViewById(R.id.analog_range_low);
            EditText high = (EditText) findViewById(R.id.analog_range_high);
            low.setText("" + changes[2 + (sensorNum * 13)]);
            high.setText("" + changes[3 + (sensorNum * 13)]);
            low.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {}

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() != 0) {
                        changes[2 + (sensorNum * 14)] = Integer.parseInt(s.toString());
                        changes_made = true;
                        changes_bool[2 + (sensorNum * 14)] = true;
                    }
                }
            });
            high.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {}

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() != 0) {
                        changes[3 + (sensorNum * 14)] = Integer.parseInt(s.toString());
                        changes_made = true;
                        changes_bool[3 + (sensorNum * 14)] = true;
                    }
                }
            });
        }
        else {
            clicked[2] = true;
            layoutToAdd[2].removeView(viewToInflate[2]);
        }
    }

    public void calibration(View view) {
        if(clicked[3]) {
            clicked[3] = false;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[3] = inflater.inflate(R.layout.x_calibration_child, null);
            layoutToAdd[3].addView(viewToInflate[3]);

            EditText cal = (EditText) findViewById(R.id.calibration);
            cal.setText("" + changes[4 + (sensorNum * 13)]);
            cal.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {}

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() != 0) {
                        changes[4 + (sensorNum * 14)] = Integer.parseInt(s.toString());
                        changes_made = true;
                        changes_bool[4 + (sensorNum * 14)] = true;
                    }
                }
            });
        }
        else {
            clicked[3] = true;
            layoutToAdd[3].removeView(viewToInflate[3]);
        }
    }

    public void multiplier(View view) {
        if(clicked[4]) {
            clicked[4] = false;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[4] = inflater.inflate(R.layout.x_multiplier_child, null);
            layoutToAdd[4].addView(viewToInflate[4]);

            Spinner dropdown = (Spinner) findViewById(R.id.multiplier_spinner);
            String[] items = new String[]{"1x", "2x", "10x", "100x"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.style_spinner_items, items);
            dropdown.setAdapter(adapter);
            dropdown.setOnItemSelectedListener(onSpinnerMult);
            int selection = 0;
            switch(changes[5 + (sensorNum * 14)]) {
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
            clicked[4] = true;
            layoutToAdd[4].removeView(viewToInflate[4]);
        }
    }

    AdapterView.OnItemSelectedListener onSpinnerMult = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch(position) {
                case 0:
                    changes[5 + (sensorNum * 14)] = 1;
                    break;
                case 1:
                    changes[5 + (sensorNum * 14)] = 2;
                    break;
                case 2:
                    changes[5 + (sensorNum * 14)] = 10;
                    break;
                case 3:
                    changes[5 + (sensorNum * 14)] = 100;
                    break;
            }
            changes[5 + (sensorNum * 14)] = position;
            changes_made = true;
            changes_bool[5 + (sensorNum * 14)] = true;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    public void alarmLimits(View view) {
        if(clicked[5]) {
            clicked[5] = false;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[5] = inflater.inflate(R.layout.x_alarm_limits_child, null);
            layoutToAdd[5].addView(viewToInflate[5]);

            EditText low1 = (EditText) findViewById(R.id.low_limit_1);
            EditText high1 = (EditText) findViewById(R.id.high_limit_1);
            EditText low2 = (EditText) findViewById(R.id.low_limit_2);
            EditText high2 = (EditText) findViewById(R.id.high_limit_2);
            EditText low3 = (EditText) findViewById(R.id.low_limit_3);
            EditText high3 = (EditText) findViewById(R.id.high_limit_3);
            EditText low4 = (EditText) findViewById(R.id.low_limit_4);
            EditText high4 = (EditText) findViewById(R.id.high_limit_4);
            low1.setText("" + changes[6 + (sensorNum * 14)]);
            high1.setText("" + changes[7 + (sensorNum * 14)]);
            low2.setText("" + changes[8 + (sensorNum * 14)]);
            high2.setText("" + changes[9 + (sensorNum * 14)]);
            low3.setText("" + changes[10 + (sensorNum * 14)]);
            high3.setText("" + changes[11 + (sensorNum * 14)]);
            low4.setText("" + changes[12 + (sensorNum * 14)]);
            high4.setText("" + changes[13 + (sensorNum * 14)]);
            low1.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {}

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() != 0) {
                        changes[6 + (sensorNum * 14)] = Integer.parseInt(s.toString());
                        changes_made = true;
                        changes_bool[6 + (sensorNum * 14)] = true;
                    }
                }
            });
            high1.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {}

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() != 0) {
                        changes[7 + (sensorNum * 14)] = Integer.parseInt(s.toString());
                        changes_made = true;
                        changes_bool[7 + (sensorNum * 14)] = true;
                    }
                }
            });
            low2.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {}

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() != 0) {
                        changes[8 + (sensorNum * 14)] = Integer.parseInt(s.toString());
                        changes_made = true;
                        changes_bool[8 + (sensorNum * 14)] = true;
                    }
                }
            });
            high2.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {}

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() != 0) {
                        changes[9 + (sensorNum * 14)] = Integer.parseInt(s.toString());
                        changes_made = true;
                        changes_bool[9 + (sensorNum * 14)] = true;
                    }
                }
            });
            low3.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {}

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() != 0) {
                        changes[10 + (sensorNum * 14)] = Integer.parseInt(s.toString());
                        changes_made = true;
                        changes_bool[10 + (sensorNum * 14)] = true;
                    }
                }
            });
            high3.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {}

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() != 0) {
                        changes[11 + (sensorNum * 14)] = Integer.parseInt(s.toString());
                        changes_made = true;
                        changes_bool[11 + (sensorNum * 14)] = true;
                    }
                }
            });
            low4.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {}

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() != 0) {
                        changes[12 + (sensorNum * 14)] = Integer.parseInt(s.toString());
                        changes_made = true;
                        changes_bool[12 + (sensorNum * 14)] = true;
                    }
                }
            });
            high4.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {}

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() != 0) {
                        changes[13 + (sensorNum * 14)] = Integer.parseInt(s.toString());
                        changes_made = true;
                        changes_bool[13 + (sensorNum * 14)] = true;
                    }
                }
            });
        }
        else {
            clicked[5] = true;
            layoutToAdd[5].removeView(viewToInflate[5]);
        }
    }
}
