package com.proactivesensing.bobbydouglass.proactiveone;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class Nfc extends AppCompatActivity {

    NfcAdapter nfcAdapter;
    TextView info;
    Button cancelButton;
    boolean sent = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nfc);

        nfcAdapter = nfcAdapter.getDefaultAdapter(this);
        info = (TextView) findViewById(R.id.nfc_info);
        info.setText("Tap ProactiveOne To Make Changes");
        cancelButton = (Button) findViewById(R.id.cancel);
    }

    @Override
    public void onBackPressed() {
        if(!sent) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("If You Exit Now, Your Changes Won't Be Saved");
            builder.setPositiveButton("Stay", changesDialog);
            builder.setNegativeButton("Exit", changesDialog);
            builder.show();
        }
        else {
            startHome();
        }
    }

    DialogInterface.OnClickListener changesDialog = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    startHome();
                    break;
            }
        }
    };

    public void back(View view)  {
        Intent Home = new Intent(this, Home.class);
        Home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(Home);
    }

    public void startHome() {
        Intent Home = new Intent(this, Home.class);
        Home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(Home);
    }

    public void save() {
        switch(Home.Screen) {
            case -1:
                new Modbus(getApplicationContext(), 1);
                break;
            case 0:
                new Modbus(getApplicationContext(), Sensors_External.changes, Sensors_External.ChangesSizeY, Sensors_External.ChangesSizeZ);
                for(int i = 0; i < Sensors_External.ChangesSizeY; i++)
                    for(int j = 0; j < Sensors_External.ChangesSizeZ; j++)
                        Sensors_External.changes[1][i][j] = 0;
                break;
            case 1:
                new Modbus(getApplicationContext(), Sensors_Internal.changes, Sensors_Internal.ChangesSizeY, Sensors_Internal.ChangesSizeZ);
                for(int i = 0; i < Sensors_Internal.ChangesSizeY; i++)
                    for(int j = 0; j < Sensors_Internal.ChangesSizeZ; j++)
                        Sensors_Internal.changes[1][i][j] = 0;
                break;
            case 2:
                new Modbus(getApplicationContext(), Sensors_I2C.changes, Sensors_I2C.ChangesSizeY, Sensors_I2C.ChangesSizeZ);
                for(int i = 0; i < Sensors_I2C.ChangesSizeY; i++)
                    for(int j = 0; j < Sensors_I2C.ChangesSizeZ; j++)
                        Sensors_I2C.changes[1][i][j] = 0;
                break;
            case 3:
                new Modbus(getApplicationContext(), Sensors_Virtual.changes, Sensors_Virtual.ChangesSizeY, Sensors_Virtual.ChangesSizeZ);
                for(int i = 0; i < Sensors_Virtual.ChangesSizeY; i++)
                    for(int j = 0; j < Sensors_Virtual.ChangesSizeZ; j++)
                        Sensors_Virtual.changes[1][i][j] = 0;
                break;
            case 4:
                new Modbus(getApplicationContext(), System_Parameters.changes, System_Parameters.ChangesSizeZ);
                for(int i = 0; i < System_Parameters.ChangesSizeZ; i++)
                    System_Parameters.changes[1][i] = 0;
                break;
            case 5:
                new Modbus(getApplicationContext(), Advanced_Data.changes, Advanced_Data.ChangesSizeZ);
                for(int i = 0; i < Advanced_Data.ChangesSizeZ; i++)
                    Advanced_Data.changes[1][i] = 0;
                break;
            case 6:
                break;
            default:
                Log.e("ERROR", "INVALID SELECTED SCREEN INDEX");
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        enableForegroundDispatchSystem();
    }

    @Override
    protected void onPause() {
        super.onPause();
        disableForegroundDispatchSystem();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if(!sent) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            NdefMessage ndefMessage = createNdefMessage();
            writeNdefMessage(tag, ndefMessage);
        }
    }

    private void enableForegroundDispatchSystem() {
        Intent intent = new Intent(this, Nfc.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        IntentFilter[] intentFilters = new IntentFilter[]{};
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null);
    }

    private void disableForegroundDispatchSystem() {
        nfcAdapter.disableForegroundDispatch(this);
    }

    private void formatTag(Tag tag, NdefMessage ndefMessage) {
        try {
            NdefFormatable ndefFormatable = NdefFormatable.get(tag);

            ndefFormatable.connect();
            ndefFormatable.format(ndefMessage);
            ndefFormatable.close();
            info.setText("Changes Sent To ProactiveOne");
            cancelButton.setText("Back");
            save();
            sent = true;
        } catch (Exception e) {
        }
    }

    private void writeNdefMessage(Tag tag, NdefMessage ndefMessage) {
        if (tag == null) {
            info.setText("Error(1) communicating with ProactiveOne, please try again...");
            return;
        }

        Ndef ndef = Ndef.get(tag);

        if (ndef == null) {
            info.setText("Error(2) communicating with ProactiveOne, please try again...");
            formatTag(tag, ndefMessage);
        } else {
            try {
                ndef.connect();
                ndef.writeNdefMessage(ndefMessage);
                ndef.close();
            } catch (Exception e) {
                info.setText("Error(3) communicating with ProactiveOne, please try again...");
            }
            info.setText("Changes Sent To ProactiveOne!");
            cancelButton.setText("Back");
            save();
            sent = true;
        }
    }

    private NdefRecord createRecord(int address, int value) {
        ByteArrayOutputStream payload = new ByteArrayOutputStream(11);

        payload.write('M');
        payload.write('1');
        payload.write(16);
        payload.write((byte) ((address >> 8) & 0xff));
        payload.write((byte) (address & 0xff));
        payload.write(0);
        payload.write(1);
        payload.write(2);
        payload.write((byte) ((value >> 8) & 0xff));
        payload.write((byte) (value & 0xff));

        return new NdefRecord(NdefRecord.TNF_MIME_MEDIA, NdefRecord.RTD_TEXT, null, payload.toByteArray());
    }

    private NdefMessage createNdefMessage() {
        ArrayList<NdefRecord> ndef = new ArrayList<NdefRecord>();

        if(Home.Screen == -1)
            for(int i = 0; i < Modbus.Size; i++)
                ndef.add(createRecord(Modbus.address[i], Modbus.defaults[i]));
        else if(Home.Screen == 0) {
            for(int i = 0; i < Sensors_External.ChangesSizeY; i++)
                for(int j = 0; j < Sensors_External.ChangesSizeZ; j++)
                    if(Sensors_External.changes[1][i][j] == 1)
                        ndef.add(createRecord(Sensors_External.changes[2][i][j], Sensors_External.changes[0][i][j]));
        }
        else if(Home.Screen == 1) {
            for(int i = 0; i < Sensors_Internal.ChangesSizeY; i++)
                for(int j = 0; j < Sensors_Internal.ChangesSizeZ; j++)
                    if(Sensors_Internal.changes[1][i][j] == 1)
                        ndef.add(createRecord(Sensors_Internal.changes[2][i][j], Sensors_Internal.changes[0][i][j]));
        }
        else if(Home.Screen == 2) {
            for(int i = 0; i < Sensors_I2C.ChangesSizeY; i++)
                for(int j = 0; j < Sensors_I2C.ChangesSizeZ; j++)
                    if(Sensors_I2C.changes[1][i][j] == 1)
                        ndef.add(createRecord(Sensors_I2C.changes[2][i][j], Sensors_I2C.changes[0][i][j]));
        }
        else if(Home.Screen == 3) {
            for(int i = 0; i < Sensors_Virtual.ChangesSizeY; i++)
                for(int j = 0; j < Sensors_Virtual.ChangesSizeZ; j++)
                    if(Sensors_Virtual.changes[1][i][j] == 1)
                        ndef.add(createRecord(Sensors_Virtual.changes[2][i][j], Sensors_Virtual.changes[0][i][j]));
        }
        else if(Home.Screen == 4) {
            for(int i = 0; i < System_Parameters.ChangesSizeZ; i++)
                if(System_Parameters.changes[1][i] == 1)
                    ndef.add(createRecord(System_Parameters.changes[2][i], System_Parameters.changes[0][i]));
        }
        else if(Home.Screen == 5) {
            for(int i = 0; i < Advanced_Data.ChangesSizeZ; i++)
                if(Advanced_Data.changes[1][i] == 1)
                    ndef.add(createRecord(Advanced_Data.changes[2][i], Advanced_Data.changes[0][i]));
        }
        else if(Home.Screen == 6) {

        }
        else {
            Log.e("ERROR", "INVALID SELECTED SCREEN INDEX");
        }

        NdefRecord[] ndefr = new NdefRecord[ndef.size()];
        for (int i = 0; i < ndef.size(); i++)
            ndefr[i] = ndef.get(i);
        NdefMessage ndefMessage = new NdefMessage(ndefr);

        return ndefMessage;
    }
}
