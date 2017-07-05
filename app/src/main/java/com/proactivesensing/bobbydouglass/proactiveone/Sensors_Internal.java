package com.proactivesensing.bobbydouglass.proactiveone;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class Sensors_Internal extends AppCompatActivity {

    Button button;
    int optionNum = 0;
    boolean changes_made = false;
    public static final int Size = 10;
    LinearLayout[] LayoutToAdd = new LinearLayout[10];
    View[] viewToInflate = new View[10];
    boolean[] clicked = new boolean[10];
    public static int[] changes = new int[10];
    public static boolean[] changes_bool = new boolean[10];
    public static int[] addresses =    {1200,   1201,   1202,   1203,   1204,
                                        1205,   1206,   1207,   1208,   1209};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensors_internal);
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

    public void option1(View view) {

    }

    public void option2(View view) {

    }

    public void option3(View view) {

    }

    public void option4(View view) {

    }

    public void option5(View view) {

    }
}
