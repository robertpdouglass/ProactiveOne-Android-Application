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

    LinearLayout[] layoutToAdd =   {(LinearLayout) findViewById(R.id.data_a1_expansion),    (LinearLayout) findViewById(R.id.data_a2_expansion),
                                    (LinearLayout) findViewById(R.id.data_a3_expansion),    (LinearLayout) findViewById(R.id.data_a4_expansion),
                                    (LinearLayout) findViewById(R.id.data_a5_expansion),    (LinearLayout) findViewById(R.id.data_b6_expansion),
                                    (LinearLayout) findViewById(R.id.data_b7_expansion),    (LinearLayout) findViewById(R.id.data_b8_expansion),
                                    (LinearLayout) findViewById(R.id.data_b9_expansion),    (LinearLayout) findViewById(R.id.data_b10_expansion),
                                    (LinearLayout) findViewById(R.id.data_b11_expansion),   (LinearLayout) findViewById(R.id.data_b12_expansion)};
    View[] viewToInflate = new View[12];
    boolean[] clicked = new boolean[12];
    boolean changes_made = false;

    public static int[] changes = new int[12];
    public static boolean[] changes_bool = new boolean[12];
    public static int[] addresses = {1500, 1501, 1502, 1503, 1504, 1505, 1506, 1507, 1508, 1509, 1510, 1511};

    int[] changes_low = new int[12];
    int[] changes_high = new int[12];

    public static final int Size = 12;

    String[] items = {"Disable", "External Sensor", "Internal Sensor", "Low Limit", "High Limit", "I2C"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advanced_data);

        LayoutTransition transition = new LayoutTransition();

        for(int i = 0; i < 12; i++) {
            layoutToAdd[i].setLayoutTransition(transition);
            clicked[i] = true;
            changes[i] = new Modbus(getApplicationContext(), addresses[i]).getValue();
            changes_bool[i] = false;
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
                    /* *********************************************************** */
                    /* *********************************************************** */
                    /* ***** ADD LENGTHY CODE TO SET OPENED SELECTED OPTIONS ***** */
                    /* *********************************************************** */
                    /* *********************************************************** */
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        }
    };

    public int combineBytes(int low, int high) {
        return ((low & 0x00ff) << 8) | ((high & 0x00ff));
    }

    public int getHighInt(int combined) {
        return (combined & 0x00ff);
    }

    public int getLowInt(int combined) {
        return ((combined >> 8) & 0x00ff);
    }

    public void indexToast() {
        Toast.makeText(this, "Index Value Must Be Between 0-255", Toast.LENGTH_SHORT).show();
    }

    public void dataA1(View view) {
        int index = 0;
        if(clicked[index]) {
            clicked[index] = false;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[index] = inflater.inflate(R.layout.y_data_a1_child, null);
            layoutToAdd[index].addView(viewToInflate[index]);

            Spinner dropdown = (Spinner) findViewById(R.id.a1_type);
            EditText e = (EditText) findViewById(R.id.a1_index);
            Button b1 = (Button) findViewById(R.id.a1_pos);
            Button b2 = (Button) findViewById(R.id.a1_neg);

            display(index, e, dropdown, b1, b2);
        }
        else {
            clicked[index] = true;
            layoutToAdd[index].removeView(viewToInflate[index]);
        }
    }

    public void dataA2(View view) {
        int index = 1;
        if(clicked[index]) {
            clicked[index] = false;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[index] = inflater.inflate(R.layout.y_data_a2_child, null);
            layoutToAdd[index].addView(viewToInflate[index]);

            Spinner dropdown = (Spinner) findViewById(R.id.a2_type);
            EditText e = (EditText) findViewById(R.id.a2_index);
            Button b1 = (Button) findViewById(R.id.a2_pos);
            Button b2 = (Button) findViewById(R.id.a2_neg);

            display(index, e, dropdown, b1, b2);
        }
        else {
            clicked[index] = true;
            layoutToAdd[index].removeView(viewToInflate[index]);
        }
    }

    public void dataA3(View view) {
        int index = 2;
        if(clicked[index]) {
            clicked[index] = false;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[index] = inflater.inflate(R.layout.y_data_a3_child, null);
            layoutToAdd[index].addView(viewToInflate[index]);

            Spinner dropdown = (Spinner) findViewById(R.id.a3_type);
            EditText e = (EditText) findViewById(R.id.a3_index);
            Button b1 = (Button) findViewById(R.id.a3_pos);
            Button b2 = (Button) findViewById(R.id.a3_neg);

            display(index, e, dropdown, b1, b2);
        }
        else {
            clicked[index] = true;
            layoutToAdd[index].removeView(viewToInflate[index]);
        }
    }

    public void dataA4(View view) {
        int index = 3;
        if(clicked[index]) {
            clicked[index] = false;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[index] = inflater.inflate(R.layout.y_data_a4_child, null);
            layoutToAdd[index].addView(viewToInflate[index]);

            Spinner dropdown = (Spinner) findViewById(R.id.a4_type);
            EditText e = (EditText) findViewById(R.id.a4_index);
            Button b1 = (Button) findViewById(R.id.a4_pos);
            Button b2 = (Button) findViewById(R.id.a4_neg);

            display(index, e, dropdown, b1, b2);
        }
        else {
            clicked[index] = true;
            layoutToAdd[index].removeView(viewToInflate[index]);
        }
    }

    public void dataA5(View view) {
        int index = 4;
        if(clicked[index]) {
            clicked[index] = false;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[index] = inflater.inflate(R.layout.y_data_a5_child, null);
            layoutToAdd[index].addView(viewToInflate[index]);

            Spinner dropdown = (Spinner) findViewById(R.id.a5_type);
            EditText e = (EditText) findViewById(R.id.a5_index);
            Button b1 = (Button) findViewById(R.id.a5_pos);
            Button b2 = (Button) findViewById(R.id.a5_neg);

            display(index, e, dropdown, b1, b2);
        }
        else {
            clicked[index] = true;
            layoutToAdd[index].removeView(viewToInflate[index]);
        }
    }

    public void dataB6(View view) {
        int index = 5;
        if(clicked[index]) {
            clicked[index] = false;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[index] = inflater.inflate(R.layout.y_data_b6_child, null);
            layoutToAdd[index].addView(viewToInflate[index]);

            Spinner dropdown = (Spinner) findViewById(R.id.b6_type);
            EditText e = (EditText) findViewById(R.id.b6_index);
            Button b1 = (Button) findViewById(R.id.b6_pos);
            Button b2 = (Button) findViewById(R.id.b6_neg);

            display(index, e, dropdown, b1, b2);
        }
        else {
            clicked[index] = true;
            layoutToAdd[index].removeView(viewToInflate[index]);
        }
    }

    public void dataB7(View view) {
        int index = 6;
        if(clicked[index]) {
            clicked[index] = false;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[index] = inflater.inflate(R.layout.y_data_b7_child, null);
            layoutToAdd[index].addView(viewToInflate[index]);

            Spinner dropdown = (Spinner) findViewById(R.id.b7_type);
            EditText e = (EditText) findViewById(R.id.b7_index);
            Button b1 = (Button) findViewById(R.id.b7_pos);
            Button b2 = (Button) findViewById(R.id.b7_neg);

            display(index, e, dropdown, b1, b2);
        }
        else {
            clicked[index] = true;
            layoutToAdd[index].removeView(viewToInflate[index]);
        }
    }

    public void dataB8(View view) {
        int index = 7;
        if(clicked[index]) {
            clicked[index] = false;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[index] = inflater.inflate(R.layout.y_data_b8_child, null);
            layoutToAdd[index].addView(viewToInflate[index]);

            Spinner dropdown = (Spinner) findViewById(R.id.b8_type);
            EditText e = (EditText) findViewById(R.id.b8_index);
            Button b1 = (Button) findViewById(R.id.b8_pos);
            Button b2 = (Button) findViewById(R.id.b8_neg);

            display(index, e, dropdown, b1, b2);
        }
        else {
            clicked[index] = true;
            layoutToAdd[index].removeView(viewToInflate[index]);
        }
    }

    public void dataB9(View view) {
        int index = 8;
        if(clicked[index]) {
            clicked[index] = false;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[index] = inflater.inflate(R.layout.y_data_b9_child, null);
            layoutToAdd[index].addView(viewToInflate[index]);

            Spinner dropdown = (Spinner) findViewById(R.id.b9_type);
            EditText e = (EditText) findViewById(R.id.b9_index);
            Button b1 = (Button) findViewById(R.id.b9_pos);
            Button b2 = (Button) findViewById(R.id.b9_neg);

            display(index, e, dropdown, b1, b2);
        }
        else {
            clicked[index] = true;
            layoutToAdd[index].removeView(viewToInflate[index]);
        }
    }

    public void dataB10(View view) {
        int index = 9;
        if(clicked[index]) {
            clicked[index] = false;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[index] = inflater.inflate(R.layout.y_data_b10_child, null);
            layoutToAdd[index].addView(viewToInflate[index]);

            Spinner dropdown = (Spinner) findViewById(R.id.b10_type);
            EditText e = (EditText) findViewById(R.id.b10_index);
            Button b1 = (Button) findViewById(R.id.b10_pos);
            Button b2 = (Button) findViewById(R.id.b10_neg);

            display(index, e, dropdown, b1, b2);
        }
        else {
            clicked[index] = true;
            layoutToAdd[index].removeView(viewToInflate[index]);
        }
    }

    public void dataB11(View view) {
        int index = 10;
        if(clicked[index]) {
            clicked[index] = false;

            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[index] = inflater.inflate(R.layout.y_data_b11_child, null);
            layoutToAdd[index].addView(viewToInflate[index]);

            Spinner dropdown = (Spinner) findViewById(R.id.b11_type);
            EditText e = (EditText) findViewById(R.id.b11_index);
            Button b1 = (Button) findViewById(R.id.b11_pos);
            Button b2 = (Button) findViewById(R.id.b11_neg);

            display(index, e, dropdown, b1, b2);
        }
        else {
            clicked[index] = true;
            layoutToAdd[index].removeView(viewToInflate[index]);
        }
    }

    public void dataB12(View view) {
        int index = 11;
        if(clicked[index]) {
            clicked[index] = false;

            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            viewToInflate[index] = inflater.inflate(R.layout.y_data_b12_child, null);
            layoutToAdd[index].addView(viewToInflate[index]);

            Spinner dropdown = (Spinner) findViewById(R.id.b12_type);
            EditText e = (EditText) findViewById(R.id.b12_index);
            Button b1 = (Button) findViewById(R.id.b12_pos);
            Button b2 = (Button) findViewById(R.id.b12_neg);

            display(index, e, dropdown, b1, b2);
        }
        else {
            clicked[index] = true;
            layoutToAdd[index].removeView(viewToInflate[index]);
        }
    }

    public void display(final int index, final EditText e, final Spinner dropdown, final Button b1, final Button b2) {
        AdapterView.OnItemSelectedListener onSpinner = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                changes_made = true;
                changes_bool[index] = true;
                changes_high[index] = position;
                changes[index] = combineBytes(changes_low[index], changes_high[index]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.style_spinner_items, items);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(onSpinner);
        dropdown.setSelection(getHighInt(changes[index]));

        e.setText("" + getLowInt(changes[index]));
        e.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    changes_made = true;
                    changes_bool[0] = true;
                    changes[index] = combineBytes(Integer.parseInt(s.toString()), changes_high[index]);
                }
            }
        });

        b1.setOnTouchListener(new Button.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (Build.VERSION.SDK_INT >= 22)
                            b1.setBackground(getDrawable(R.drawable.material_button_blue));
                        else
                            b1.setBackground(getResources().getDrawable(R.drawable.material_button_blue));
                        if (changes_low[index] < 255) {
                            changes_made = true;
                            changes_bool[index] = true;
                            changes_low[index]++;
                            changes[index] = combineBytes(changes_low[index], changes_high[index]);
                            e.setText("" + getLowInt(changes[index]));
                        } else {
                            if (Build.VERSION.SDK_INT >= 22)
                                b1.setBackground(getDrawable(R.drawable.material_button));
                            else
                                b1.setBackground(getResources().getDrawable(R.drawable.material_button));
                            indexToast();
                        }
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

        b2.setOnTouchListener(new Button.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (Build.VERSION.SDK_INT >= 22)
                            b2.setBackground(getDrawable(R.drawable.material_button_blue));
                        else
                            b2.setBackground(getResources().getDrawable(R.drawable.material_button_blue));
                        if (changes_low[index] > 0) {
                            changes_made = true;
                            changes_bool[index] = true;
                            changes_low[index]--;
                            changes[index] = combineBytes(changes_low[index], changes_high[index]);
                            e.setText("" + getLowInt(changes[index]));
                        } else {
                            if (Build.VERSION.SDK_INT >= 22)
                                b2.setBackground(getDrawable(R.drawable.material_button));
                            else
                                b2.setBackground(getResources().getDrawable(R.drawable.material_button));
                            indexToast();
                        }
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
}
