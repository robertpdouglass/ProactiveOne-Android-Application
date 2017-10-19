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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class Advanced_Data extends AppCompatActivity {

    public static final int LayoutSize =        12;
    public static final int ChangesSizeY =      3;
    public static final int ChangesSizeZ =      24;

    static LinearLayout[] layoutToAdd =         new LinearLayout[LayoutSize];
    View[] viewToInflate =                      new View[LayoutSize];
    boolean changes_made =                      false;
    boolean[] loadedSpinner =                  {false,  false,  false,  false,  false,  false,
                                                false,  false,  false,  false,  false,  false};
    public static boolean[] clicked =          {false,  false,  false,  false,  false,  false,
                                                false,  false,  false,  false,  false,  false};
    public static int[][] changes =           {{-1,     -1,     -1,     -1,     -1,     -1,
                                                -1,     -1,     -1,     -1,     -1,     -1,
                                                -1,     -1,     -1,     -1,     -1,     -1,
                                                -1,     -1,     -1,     -1,     -1,     -1},

                                               {0,      0,      0,      0,      0,      0,
                                                0,      0,      0,      0,      0,      0,
                                                0,      0,      0,      0,      0,      0,
                                                0,      0,      0,      0,      0,      0},

                                               {1500,   1501,   1502,   1503,   1504,   1505,
                                                1506,   1507,   1508,   1509,   1510,   1511,
                                                1512,   1513,   1514,   1515,   1516,   1517,
                                                1518,   1519,   1520,   1521,   1522,   1523}};
    String[] items =                           {"Disabled",         "External Sensor",  "Internal Sensor",
                                                "I2C Sensor",       "Virtual Sensor"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advanced_data);

        layoutToAdd[0] =      (LinearLayout) findViewById(R.id.data_a1_expansion);
        layoutToAdd[1] =      (LinearLayout) findViewById(R.id.data_a2_expansion);
        layoutToAdd[2] =      (LinearLayout) findViewById(R.id.data_a3_expansion);
        layoutToAdd[3] =      (LinearLayout) findViewById(R.id.data_a4_expansion);
        layoutToAdd[4] =      (LinearLayout) findViewById(R.id.data_a5_expansion);
        layoutToAdd[5] =      (LinearLayout) findViewById(R.id.data_b6_expansion);
        layoutToAdd[6] =      (LinearLayout) findViewById(R.id.data_b7_expansion);
        layoutToAdd[7] =      (LinearLayout) findViewById(R.id.data_b8_expansion);
        layoutToAdd[8] =      (LinearLayout) findViewById(R.id.data_b9_expansion);
        layoutToAdd[9] =      (LinearLayout) findViewById(R.id.data_b10_expansion);
        layoutToAdd[10] =     (LinearLayout) findViewById(R.id.data_c11_expansion);
        layoutToAdd[11] =     (LinearLayout) findViewById(R.id.data_c12_expansion);

        for (int i = 0; i < ChangesSizeZ; i++) {
            changes[0][i] = new Modbus(getApplicationContext(), changes[2][i]).getValue();
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
            Intent Adv = new Intent(this, Advanced.class);
            Adv.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(Adv);
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
                    startAdvanced();
                    break;
            }
        }
    };

    public void startNfc() {
        Intent NFC = new Intent(this, Nfc.class);
        NFC.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(NFC);
    }

    public void startAdvanced() {
        Intent Adv = new Intent(this, Advanced.class);
        Adv.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(Adv);
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

    public void indexToast() {
        Toast.makeText(this, "Index Value Must Be Between 0-255", Toast.LENGTH_SHORT).show();
    }

    public void dataA1(View view) {
        final int index = 0;
        if(!clicked[index]) {
            clicked[index] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[index] = inflater.inflate(R.layout.z_data_a01_child, null);
            layoutToAdd[index].addView(viewToInflate[index]);

            Spinner spinner =       (Spinner) findViewById(R.id.data_a1_spinner);
            EditText e =            (EditText) findViewById(R.id.data_a1_edittext);
            Button[] button =      {(Button) findViewById(R.id.data_a1_neg),
                                    (Button) findViewById(R.id.data_a1_pos)};
            display(index, spinner, e, button);
        }
        else {
            clicked[index] = false;
            layoutToAdd[index].removeView(viewToInflate[index]);
        }
    }

    public void dataA2(View view) {
        final int index = 1;
        if(!clicked[index]) {
            clicked[index] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[index] = inflater.inflate(R.layout.z_data_a02_child, null);
            layoutToAdd[index].addView(viewToInflate[index]);

            Spinner spinner =       (Spinner) findViewById(R.id.data_a2_spinner);
            EditText e =            (EditText) findViewById(R.id.data_a2_edittext);
            Button[] button =      {(Button) findViewById(R.id.data_a2_neg),
                                    (Button) findViewById(R.id.data_a2_pos)};
            display(index, spinner, e, button);
        }
        else {
            clicked[index] = false;
            layoutToAdd[index].removeView(viewToInflate[index]);
        }
    }

    public void dataA3(View view) {
        final int index = 2;
        if(!clicked[index]) {
            clicked[index] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[index] = inflater.inflate(R.layout.z_data_a03_child, null);
            layoutToAdd[index].addView(viewToInflate[index]);

            Spinner spinner =       (Spinner) findViewById(R.id.data_a3_spinner);
            EditText e =            (EditText) findViewById(R.id.data_a3_edittext);
            Button[] button =      {(Button) findViewById(R.id.data_a3_neg),
                                    (Button) findViewById(R.id.data_a3_pos)};
            display(index, spinner, e, button);
        }
        else {
            clicked[index] = false;
            layoutToAdd[index].removeView(viewToInflate[index]);
        }
    }

    public void dataA4(View view) {
        final int index = 3;
        if(!clicked[index]) {
            clicked[index] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[index] = inflater.inflate(R.layout.z_data_a04_child, null);
            layoutToAdd[index].addView(viewToInflate[index]);

            Spinner spinner =       (Spinner) findViewById(R.id.data_a4_spinner);
            EditText e =            (EditText) findViewById(R.id.data_a4_edittext);
            Button[] button =      {(Button) findViewById(R.id.data_a4_neg),
                                    (Button) findViewById(R.id.data_a4_pos)};
            display(index, spinner, e, button);
        }
        else {
            clicked[index] = false;
            layoutToAdd[index].removeView(viewToInflate[index]);
        }
    }

    public void dataA5(View view) {
        final int index = 4;
        if(!clicked[index]) {
            clicked[index] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[index] = inflater.inflate(R.layout.z_data_a05_child, null);
            layoutToAdd[index].addView(viewToInflate[index]);

            Spinner spinner =       (Spinner) findViewById(R.id.data_a5_spinner);
            EditText e =            (EditText) findViewById(R.id.data_a5_edittext);
            Button[] button =      {(Button) findViewById(R.id.data_a5_neg),
                                    (Button) findViewById(R.id.data_a5_pos)};
            display(index, spinner, e, button);
        }
        else {
            clicked[index] = false;
            layoutToAdd[index].removeView(viewToInflate[index]);
        }
    }

    public void dataB6(View view) {
        final int index = 5;
        if(!clicked[index]) {
            clicked[index] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[index] = inflater.inflate(R.layout.z_data_b06_child, null);
            layoutToAdd[index].addView(viewToInflate[index]);

            Spinner spinner =       (Spinner) findViewById(R.id.data_b6_spinner);
            EditText e =            (EditText) findViewById(R.id.data_b6_edittext);
            Button[] button =      {(Button) findViewById(R.id.data_b6_neg),
                                    (Button) findViewById(R.id.data_b6_pos)};
            display(index, spinner, e, button);
        }
        else {
            clicked[index] = false;
            layoutToAdd[index].removeView(viewToInflate[index]);
        }
    }

    public void dataB7(View view) {
        final int index = 6;
        if(!clicked[index]) {
            clicked[index] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[index] = inflater.inflate(R.layout.z_data_b07_child, null);
            layoutToAdd[index].addView(viewToInflate[index]);

            Spinner spinner =       (Spinner) findViewById(R.id.data_b7_spinner);
            EditText e =            (EditText) findViewById(R.id.data_b7_edittext);
            Button[] button =      {(Button) findViewById(R.id.data_b7_neg),
                                    (Button) findViewById(R.id.data_b7_pos)};
            display(index, spinner, e, button);
        }
        else {
            clicked[index] = false;
            layoutToAdd[index].removeView(viewToInflate[index]);
        }
    }

    public void dataB8(View view) {
        final int index = 7;
        if(!clicked[index]) {
            clicked[index] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[index] = inflater.inflate(R.layout.z_data_b08_child, null);
            layoutToAdd[index].addView(viewToInflate[index]);

            Spinner spinner =       (Spinner) findViewById(R.id.data_b8_spinner);
            EditText e =            (EditText) findViewById(R.id.data_b8_edittext);
            Button[] button =      {(Button) findViewById(R.id.data_b8_neg),
                                    (Button) findViewById(R.id.data_b8_pos)};
            display(index, spinner, e, button);
        }
        else {
            clicked[index] = false;
            layoutToAdd[index].removeView(viewToInflate[index]);
        }
    }

    public void dataB9(View view) {
        final int index = 8;
        if(!clicked[index]) {
            clicked[index] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[index] = inflater.inflate(R.layout.z_data_b09_child, null);
            layoutToAdd[index].addView(viewToInflate[index]);

            Spinner spinner =       (Spinner) findViewById(R.id.data_b9_spinner);
            EditText e =            (EditText) findViewById(R.id.data_b9_edittext);
            Button[] button =      {(Button) findViewById(R.id.data_b9_neg),
                                    (Button) findViewById(R.id.data_b9_pos)};
            display(index, spinner, e, button);
        }
        else {
            clicked[index] = false;
            layoutToAdd[index].removeView(viewToInflate[index]);
        }
    }

    public void dataB10(View view) {
        final int index = 9;
        if(!clicked[index]) {
            clicked[index] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[index] = inflater.inflate(R.layout.z_data_b10_child, null);
            layoutToAdd[index].addView(viewToInflate[index]);

            Spinner spinner =       (Spinner) findViewById(R.id.data_b10_spinner);
            EditText e =            (EditText) findViewById(R.id.data_b10_edittext);
            Button[] button =      {(Button) findViewById(R.id.data_b10_neg),
                                    (Button) findViewById(R.id.data_b10_pos)};
            display(index, spinner, e, button);
        }
        else {
            clicked[index] = false;
            layoutToAdd[index].removeView(viewToInflate[index]);
        }
    }

    public void dataC11(View view) {
        final int index = 10;
        if(!clicked[index]) {
            clicked[index] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[index] = inflater.inflate(R.layout.z_data_c11_child, null);
            layoutToAdd[index].addView(viewToInflate[index]);

            Spinner spinner =       (Spinner) findViewById(R.id.data_c11_spinner);
            EditText e =            (EditText) findViewById(R.id.data_c11_edittext);
            Button[] button =      {(Button) findViewById(R.id.data_c11_neg),
                                    (Button) findViewById(R.id.data_c11_pos)};
            display(index, spinner, e, button);
        }
        else {
            clicked[index] = false;
            layoutToAdd[index].removeView(viewToInflate[index]);
        }
    }

    public void dataC12(View view) {
        final int index = 11;
        if(!clicked[index]) {
            clicked[index] = true;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[index] = inflater.inflate(R.layout.z_data_c12_child, null);
            layoutToAdd[index].addView(viewToInflate[index]);

            Spinner spinner =       (Spinner) findViewById(R.id.data_c12_spinner);
            EditText e =            (EditText) findViewById(R.id.data_c12_edittext);
            Button[] button =      {(Button) findViewById(R.id.data_c12_neg),
                                    (Button) findViewById(R.id.data_c12_pos)};
            display(index, spinner, e, button);
        }
        else {
            clicked[index] = false;
            layoutToAdd[index].removeView(viewToInflate[index]);
        }
    }

    public void display(final int i, final Spinner dropdown, final EditText editText, final Button[] button) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.style_spinner_items, items);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(loadedSpinner[i]) {
                    changes[0][i * 2] = position;
                    changes[1][i * 2] = 1;
                    changes_made = true;
                }
                else {
                    loadedSpinner[i] = true;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        dropdown.setSelection(changes[0][i * 2]);

        editText.setText("" + changes[0][(i * 2) + 1]);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    changes[0][(i * 2) + 1] = Integer.parseInt(s.toString());
                    changes[1][(i * 2) + 1] = 1;
                    changes_made = true;
                }
            }
        });

        for(int j = 0; j < 2; j++) {
            final int k = j;
            button[k].setOnTouchListener(new Button.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            if (Build.VERSION.SDK_INT >= 22)
                                button[k].setBackground(getDrawable(R.drawable.material_button_blue));
                            else
                                button[k].setBackground(getResources().getDrawable(R.drawable.material_button_blue));

                            if (changes[0][(i * 2) + 1] < 255) {
                                changes_made = true;
                                changes[1][(i * 2) + 1] = 1;
                                if(k == 0)
                                    changes[0][(i * 2) + 1]--;
                                else
                                    changes[0][(i * 2) + 1]++;
                                editText.setText("" + changes[0][(i * 2) + 1]);
                            }
                            else
                                indexToast();
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
}