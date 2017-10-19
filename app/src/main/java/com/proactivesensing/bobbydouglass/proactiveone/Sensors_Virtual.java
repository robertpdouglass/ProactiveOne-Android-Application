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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class Sensors_Virtual extends AppCompatActivity {

    public static int ChangesSizeX =    3;
    public static int ChangesSizeY =    8;
    public static int ChangesSizeZ =    12;
    public static int ViewSize =        7;

    public static int[][][] changes =    {{{-1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
                                            -1,     -1,     -1,     -1,},
                                           {-1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
                                            -1,     -1,     -1,     -1,},
                                           {-1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
                                            -1,     -1,     -1,     -1,},
                                           {-1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
                                            -1,     -1,     -1,     -1,},
                                           {-1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
                                            -1,     -1,     -1,     -1,},
                                           {-1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
                                            -1,     -1,     -1,     -1,},
                                           {-1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
                                            -1,     -1,     -1,     -1,},
                                           {-1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
                                            -1,     -1,     -1,     -1,}},
                                          {{0,      0,      0,      0,      0,      0,      0,      0,
                                            0,      0,      0,      0},
                                           {0,      0,      0,      0,      0,      0,      0,      0,
                                            0,      0,      0,      0},
                                           {0,      0,      0,      0,      0,      0,      0,      0,
                                            0,      0,      0,      0},
                                           {0,      0,      0,      0,      0,      0,      0,      0,
                                            0,      0,      0,      0},
                                           {0,      0,      0,      0,      0,      0,      0,      0,
                                            0,      0,      0,      0},
                                           {0,      0,      0,      0,      0,      0,      0,      0,
                                            0,      0,      0,      0},
                                           {0,      0,      0,      0,      0,      0,      0,      0,
                                            0,      0,      0,      0},
                                           {0,      0,      0,      0,      0,      0,      0,      0,
                                            0,      0,      0,      0}},
                                          {{1700,   1701,   1702,   1703,   1704,   1705,   1706,   1707,
                                            1708,   1709,   1710,   1711},
                                           {1715,   1716,   1717,   1718,   1719,   1720,   1721,   1722,
                                            1723,   1724,   1725,   1726},
                                           {1730,   1731,   1732,   1733,   1734,   1735,   1736,   1737,
                                            1738,   1739,   1740,   1741},
                                           {1745,   1746,   1747,   1748,   1749,   1750,   1751,   1752,
                                            1753,   1754,   1755,   1756},
                                           {1760,   1761,   1762,   1763,   1764,   1765,   1766,   1767,
                                            1768,   1769,   1770,   1771},
                                           {1775,   1776,   1777,   1778,   1779,   1780,   1781,   1782,
                                            1783,   1784,   1785,   1786},
                                           {1790,   1791,   1792,   1793,   1794,   1795,   1796,   1797,
                                            1798,   1799,   1800,   1801},
                                           {1805,   1806,   1807,   1808,   1809,   1810,   1811,   1812,
                                            1813,   1814,   1815,   1816}}};

    LinearLayout[] layoutToAdd =            new LinearLayout[ViewSize];
    View[] viewToInflate =                  new View[ViewSize];
    boolean[] clicked =                    {false,  false,  false,  false,  false,  false,  false};
    boolean changes_made =                  false;
    int sensorNum =                         0;
    boolean[][] loadedSpinner =           {{false,  false,  false,  false},{false,  false,  false,  false},
                                           {false,  false,  false,  false},{false,  false,  false,  false},
                                           {false,  false,  false,  false},{false,  false,  false,  false},
                                           {false,  false,  false,  false},{false,  false,  false,  false}};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensors_virtual);

        layoutToAdd[0] = (LinearLayout) findViewById(R.id.config_expansion);
        layoutToAdd[1] = (LinearLayout) findViewById(R.id.skip_expansion);
        layoutToAdd[2] = (LinearLayout) findViewById(R.id.data_a_expansion);
        layoutToAdd[3] = (LinearLayout) findViewById(R.id.data_b_expansion);
        layoutToAdd[4] = (LinearLayout) findViewById(R.id.data_c_expansion);
        layoutToAdd[5] = (LinearLayout) findViewById(R.id.data_d_expansion);
        layoutToAdd[6] = (LinearLayout) findViewById(R.id.manual_expansion);

        for(int i = 0; i < ChangesSizeY; i++)
            for(int j = 0; j < ChangesSizeZ; j++)
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
        Button sensor4 = (Button) findViewById(R.id.button5);
        Button sensor5 = (Button) findViewById(R.id.button6);
        Button sensor6 = (Button) findViewById(R.id.button7);
        Button sensor7 = (Button) findViewById(R.id.button8);
        Button selectedSensor = null;
        Button[] offSensors = new Button[3];

        switch (sensorNum) {
            case 0:
                offSensors = new Button[]{sensor1, sensor2, sensor3, sensor4, sensor5, sensor6, sensor7};
                selectedSensor = sensor0;
                break;
            case 1:
                offSensors = new Button[]{sensor0, sensor2, sensor3, sensor4, sensor5, sensor6, sensor7};
                selectedSensor = sensor1;
                break;
            case 2:
                offSensors = new Button[]{sensor0, sensor1, sensor3, sensor4, sensor5, sensor6, sensor7};
                selectedSensor = sensor2;
                break;
            case 3:
                offSensors = new Button[]{sensor0, sensor1, sensor2, sensor4, sensor5, sensor6, sensor7};
                selectedSensor = sensor3;
                break;
            case 4:
                offSensors = new Button[]{sensor0, sensor1, sensor2, sensor3, sensor5, sensor6, sensor7};
                selectedSensor = sensor4;
                break;
            case 5:
                offSensors = new Button[]{sensor0, sensor1, sensor2, sensor3, sensor4, sensor6, sensor7};
                selectedSensor = sensor5;
                break;
            case 6:
                offSensors = new Button[]{sensor0, sensor1, sensor2, sensor3, sensor4, sensor5, sensor7};
                selectedSensor = sensor6;
                break;
            case 7:
                offSensors = new Button[]{sensor0, sensor1, sensor2, sensor3, sensor4, sensor5, sensor6};
                selectedSensor = sensor7;
                break;
            default:
                Log.e("ERROR", "INVALID SENSORNUM");
                break;
        }

        for (int i = 0; i < 7; i++) {
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
            skip(null);
        }
        if(clicked[2]) {
            clicked[2] = false;
            layoutToAdd[2].removeView(viewToInflate[2]);
            dataA(null);
        }
        if(clicked[3]) {
            clicked[3] = false;
            layoutToAdd[3].removeView(viewToInflate[3]);
            dataB(null);
        }
        if(clicked[4]) {
            clicked[4] = false;
            layoutToAdd[4].removeView(viewToInflate[4]);
            dataC(null);
        }
        if(clicked[5]) {
            clicked[5] = false;
            layoutToAdd[5].removeView(viewToInflate[5]);
            dataD(null);
        }
        if(clicked[6]) {
            clicked[6] = false;
            layoutToAdd[6].removeView(viewToInflate[6]);
            manual(null);
        }
    }

    public void sensor0(View view) {
        sensorNum = 0;
        sensorSelection();
    }

    public void sensor1(View view) {
        sensorNum = 1;
        sensorSelection();
    }

    public void sensor2(View view) {
        sensorNum = 2;
        sensorSelection();
    }

    public void sensor3(View view) {
        sensorNum = 3;
        sensorSelection();
    }

    public void sensor4(View view) {
        sensorNum = 4;
        sensorSelection();
    }

    public void sensor5(View view) {
        sensorNum = 5;
        sensorSelection();
    }

    public void sensor6(View view) {
        sensorNum = 6;
        sensorSelection();
    }

    public void sensor7(View view) {
        sensorNum = 7;
        sensorSelection();
    }

    public void negativeToast() {
        Toast.makeText(this, "Input Cannot Be Negative", Toast.LENGTH_SHORT).show();
    }

    public void disabledToast() {
        Toast.makeText(this, "Disabled", Toast.LENGTH_SHORT).show();
    }

    public void enabledToast() {
        Toast.makeText(this, "Enabled", Toast.LENGTH_SHORT).show();
    }

    public void configuration(View view) {
        final int i = 0, j = 0;
        if(!clicked[i]) {
            clicked[i] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[i] = inflater.inflate(R.layout.z_vir_01_config_child, null);
            layoutToAdd[i].addView(viewToInflate[i]);

            final EditText config = (EditText) findViewById(R.id.vir_config_edittext);
            config.setText("" + changes[0][sensorNum][j]);
            config.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {}
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() != 0) {
                        int value = Integer.parseInt(s.toString());
                        if(value > -1) {
                            if(value == 0)
                                disabledToast();
                            if(value > 0 && changes[0][sensorNum][i] == 0)
                                enabledToast();
                            changes[0][sensorNum][i] = value;
                            changes[1][sensorNum][i] = 1;
                            changes_made = true;
                        }
                        else
                            negativeToast();
                    }
                }
            });

            final Button[] button =    {(Button) findViewById(R.id.vir_config_neg),
                                        (Button) findViewById(R.id.vir_config_pos)};
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
                                    if(changes[0][sensorNum][i] > 0) {
                                        if(changes[0][sensorNum][i] == 1)
                                            disabledToast();
                                        changes[0][sensorNum][i]--;
                                        changes[1][sensorNum][i] = 1;
                                        changes_made = true;
                                        config.setText("" + changes[0][sensorNum][i]);
                                    }
                                    else
                                        negativeToast();
                                }
                                else {
                                    if(changes[0][sensorNum][i] == 0)
                                        enabledToast();
                                    changes[0][sensorNum][i]++;
                                    changes[1][sensorNum][i] = 1;
                                    changes_made = true;
                                    config.setText("" + changes[0][sensorNum][i]);
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

    public void skip(View view) {
        final int i = 1, j = 1;
        if(!clicked[i]) {
            clicked[i] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[i] = inflater.inflate(R.layout.z_vir_02_skip_child, null);
            layoutToAdd[i].addView(viewToInflate[i]);

            final EditText skip = (EditText) findViewById(R.id.vir_skip_edittext);
            skip.setText("" + changes[0][sensorNum][j]);
            skip.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {}
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() != 0) {
                        int value = Integer.parseInt(s.toString());
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

            final Button[] button =    {(Button) findViewById(R.id.vir_skip_neg),
                                        (Button) findViewById(R.id.vir_skip_pos)};
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

    public void dataA(View view) {
        final int i = 2, j = 2, h = 0;
        if(!clicked[i]) {
            clicked[i] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[i] = inflater.inflate(R.layout.z_vir_03_data_a_child, null);
            layoutToAdd[i].addView(viewToInflate[i]);

            Spinner dropdown = (Spinner) findViewById(R.id.vir_data_a_spinner);
            String[] items = new String[]{"Disabled", "External Sensor", "Internal Sensor", "I2C Sensor", "Virtual Sensor"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.style_spinner_items, items);
            dropdown.setAdapter(adapter);
            dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(loadedSpinner[sensorNum][h]) {
                        changes[0][sensorNum][j] = position;
                        changes[1][sensorNum][j] = 1;
                        changes_made = true;
                    }
                    else
                        loadedSpinner[sensorNum][h] = true;
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
            dropdown.setSelection(changes[0][sensorNum][j]);

            final EditText index = (EditText) findViewById(R.id.vir_data_a_edittext);
            index.setText("" + changes[0][sensorNum][j + 1]);
            index.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {}
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() != 0) {
                        int value = Integer.parseInt(s.toString());
                        if(value > -1) {
                            changes[0][sensorNum][j + 1] = value;
                            changes[1][sensorNum][j + 1] = 1;
                            changes_made = true;
                        }
                        else
                            negativeToast();
                    }
                }
            });

            final Button[] button =    {(Button) findViewById(R.id.vir_data_a_neg),
                                        (Button) findViewById(R.id.vir_data_a_pos)};
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
                                    if(changes[0][sensorNum][j + 1] > 0) {
                                        changes[0][sensorNum][j + 1]--;
                                        changes[1][sensorNum][j + 1] = 1;
                                        changes_made = true;
                                        index.setText("" + changes[0][sensorNum][j + 1]);
                                    }
                                    else
                                        negativeToast();
                                }
                                else {
                                    changes[0][sensorNum][j + 1]++;
                                    changes[1][sensorNum][j + 1] = 1;
                                    changes_made = true;
                                    index.setText("" + changes[0][sensorNum][j + 1]);
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

    public void dataB(View view) {
        final int i = 3, j = 4, h = 1;
        if(!clicked[i]) {
            clicked[i] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[i] = inflater.inflate(R.layout.z_vir_04_data_b_child, null);
            layoutToAdd[i].addView(viewToInflate[i]);

            Spinner dropdown = (Spinner) findViewById(R.id.vir_data_b_spinner);
            String[] items = new String[]{"Disabled", "External Sensor", "Internal Sensor", "I2C Sensor", "Virtual Sensor"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.style_spinner_items, items);
            dropdown.setAdapter(adapter);
            dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(loadedSpinner[sensorNum][h]) {
                        changes[0][sensorNum][j] = position;
                        changes[1][sensorNum][j] = 1;
                        changes_made = true;
                    }
                    else
                        loadedSpinner[sensorNum][h] = true;
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
            dropdown.setSelection(changes[0][sensorNum][j]);

            final EditText index = (EditText) findViewById(R.id.vir_data_b_edittext);
            index.setText("" + changes[0][sensorNum][j + 1]);
            index.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {}
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() != 0) {
                        int value = Integer.parseInt(s.toString());
                        if(value > -1) {
                            changes[0][sensorNum][j + 1] = value;
                            changes[1][sensorNum][j + 1] = 1;
                            changes_made = true;
                        }
                        else
                            negativeToast();
                    }
                }
            });

            final Button[] button =    {(Button) findViewById(R.id.vir_data_b_neg),
                                        (Button) findViewById(R.id.vir_data_b_pos)};
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
                                    if(changes[0][sensorNum][j + 1] > 0) {
                                        changes[0][sensorNum][j + 1]--;
                                        changes[1][sensorNum][j + 1] = 1;
                                        changes_made = true;
                                        index.setText("" + changes[0][sensorNum][j + 1]);
                                    }
                                    else
                                        negativeToast();
                                }
                                else {
                                    changes[0][sensorNum][j + 1]++;
                                    changes[1][sensorNum][j + 1] = 1;
                                    changes_made = true;
                                    index.setText("" + changes[0][sensorNum][j + 1]);
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

    public void dataC(View view) {
        final int i = 4, j = 6, h = 2;
        if(!clicked[i]) {
            clicked[i] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[i] = inflater.inflate(R.layout.z_vir_05_data_c_child, null);
            layoutToAdd[i].addView(viewToInflate[i]);

            Spinner dropdown = (Spinner) findViewById(R.id.vir_data_c_spinner);
            String[] items = new String[]{"Disabled", "External Sensor", "Internal Sensor", "I2C Sensor", "Virtual Sensor"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.style_spinner_items, items);
            dropdown.setAdapter(adapter);
            dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(loadedSpinner[sensorNum][h]) {
                        changes[0][sensorNum][j] = position;
                        changes[1][sensorNum][j] = 1;
                        changes_made = true;
                    }
                    else
                        loadedSpinner[sensorNum][h] = true;
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
            dropdown.setSelection(changes[0][sensorNum][j]);

            final EditText index = (EditText) findViewById(R.id.vir_data_c_edittext);
            index.setText("" + changes[0][sensorNum][j + 1]);
            index.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {}
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() != 0) {
                        int value = Integer.parseInt(s.toString());
                        if(value > -1) {
                            changes[0][sensorNum][j + 1] = value;
                            changes[1][sensorNum][j + 1] = 1;
                            changes_made = true;
                        }
                        else
                            negativeToast();
                    }
                }
            });

            final Button[] button =    {(Button) findViewById(R.id.vir_data_c_neg),
                                        (Button) findViewById(R.id.vir_data_c_pos)};
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
                                    if(changes[0][sensorNum][j + 1] > 0) {
                                        changes[0][sensorNum][j + 1]--;
                                        changes[1][sensorNum][j + 1] = 1;
                                        changes_made = true;
                                        index.setText("" + changes[0][sensorNum][j + 1]);
                                    }
                                    else
                                        negativeToast();
                                }
                                else {
                                    changes[0][sensorNum][j + 1]++;
                                    changes[1][sensorNum][j + 1] = 1;
                                    changes_made = true;
                                    index.setText("" + changes[0][sensorNum][j + 1]);
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

    public void dataD(View view) {
        final int i = 5, j = 8, h = 3;
        if(!clicked[i]) {
            clicked[i] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[i] = inflater.inflate(R.layout.z_vir_06_data_d_child, null);
            layoutToAdd[i].addView(viewToInflate[i]);

            Spinner dropdown = (Spinner) findViewById(R.id.vir_data_d_spinner);
            String[] items = new String[]{"Disabled", "External Sensor", "Internal Sensor", "I2C Sensor", "Virtual Sensor"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.style_spinner_items, items);
            dropdown.setAdapter(adapter);
            dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(loadedSpinner[sensorNum][h]) {
                        changes[0][sensorNum][j] = position;
                        changes[1][sensorNum][j] = 1;
                        changes_made = true;
                    }
                    else
                        loadedSpinner[sensorNum][h] = true;
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
            dropdown.setSelection(changes[0][sensorNum][j]);

            final EditText index = (EditText) findViewById(R.id.vir_data_d_edittext);
            index.setText("" + changes[0][sensorNum][j + 1]);
            index.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {}
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() != 0) {
                        int value = Integer.parseInt(s.toString());
                        if(value > -1) {
                            changes[0][sensorNum][j + 1] = value;
                            changes[1][sensorNum][j + 1] = 1;
                            changes_made = true;
                        }
                        else
                            negativeToast();
                    }
                }
            });

            final Button[] button =    {(Button) findViewById(R.id.vir_data_d_neg),
                                        (Button) findViewById(R.id.vir_data_d_pos)};
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
                                    if(changes[0][sensorNum][j + 1] > 0) {
                                        changes[0][sensorNum][j + 1]--;
                                        changes[1][sensorNum][j + 1] = 1;
                                        changes_made = true;
                                        index.setText("" + changes[0][sensorNum][j + 1]);
                                    }
                                    else
                                        negativeToast();
                                }
                                else {
                                    changes[0][sensorNum][j + 1]++;
                                    changes[1][sensorNum][j + 1] = 1;
                                    changes_made = true;
                                    index.setText("" + changes[0][sensorNum][j + 1]);
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

    public void manual(View view) {
        final int i = 6, j = 10;
        if(!clicked[i]) {
            clicked[i] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[i] = inflater.inflate(R.layout.z_vir_07_manual_child, null);
            layoutToAdd[i].addView(viewToInflate[i]);

            final EditText e1 = (EditText) findViewById(R.id.vir_manual_1_edittext);
            e1.setText("" + changes[0][sensorNum][j]);
            e1.addTextChangedListener(new TextWatcher() {
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

            final EditText e2 = (EditText) findViewById(R.id.vir_manual_2_edittext);
            e2.setText("" + changes[0][sensorNum][j + 1]);
            e2.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {}
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() != 0) {
                        changes[0][sensorNum][j + 1] = Integer.parseInt(s.toString());
                        changes[1][sensorNum][j + 1] = 1;
                        changes_made = true;
                    }
                }
            });
        }
        else {
            clicked[i] = false;
            layoutToAdd[i].removeView(viewToInflate[i]);
        }
    }
}
