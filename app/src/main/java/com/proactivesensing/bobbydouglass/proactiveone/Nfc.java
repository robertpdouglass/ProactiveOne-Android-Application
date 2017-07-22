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

        if(!nfcAdapter.isEnabled()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("NFC Disabled, Please Enable NFC In Settings");
            builder.setPositiveButton("Settings", dialogClickListener);
            builder.setNegativeButton("Cancel", dialogClickListener);
            builder.show();
        }
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    openSettings();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    startHome();
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        Intent Home = new Intent(this, Home.class);
        Home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(Home);
    }

    public void back(View view)  {
        Intent Home = new Intent(this, Home.class);
        Home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(Home);
    }

    public void openSettings() {
        Intent setnfc = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
        startActivity(setnfc);
    }

    public void startHome() {
        Intent Home = new Intent(this, Home.class);
        Home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(Home);
    }

    public void save() {
        if(Home.Screen == -1) {
            for(int i = 0; i < Modbus.Size; i++)
                new Modbus(getApplicationContext(), Modbus.address[i], Modbus.defaults[i]);
        }
        if(Home.Screen == 0) {
            for (int i = 0; i < Sensors_External.SensorCount; i++) {
                for(int j = 0; j < Sensors_External.SensorChanges; j++) {
                    if (Sensors_External.changes_bool[i][j])
                        new Modbus(getApplicationContext(), Sensors_External.addresses[i][j], Sensors_External.changes[i][j]);
                }
            }
        }
        else {
            Log.e("ERROR", "INVALID SELECTED SCREEN INDEX");
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
        try {
            if (tag == null) {
                info.setText("Error(1) communicating with ProactiveOne, please try again...");
                return;
            }

            Ndef ndef = Ndef.get(tag);

            if (ndef == null) {
                info.setText("Error(2) communicating with ProactiveOne, please try again...");
                formatTag(tag, ndefMessage);
            } else {
                ndef.connect();
                ndef.writeNdefMessage(ndefMessage);
                ndef.close();
                info.setText("Changes Sent To ProactiveOne!");
                cancelButton.setText("Back");
                save();
                sent = true;
            }
        } catch (Exception e) {
            info.setText("Error(3) communicating with ProactiveOne, please try again...");
        }
    }

    private NdefRecord createRecord(int address, int value) {
        ByteArrayOutputStream payload = new ByteArrayOutputStream(11);

        payload.write('M');
        payload.write('1');
        payload.write(16);
        payload.write((byte) ((address >> 8) & 0x00ff));
        payload.write((byte) (address & 0x00ff));
        payload.write(0);
        payload.write(1);
        payload.write(2);
        payload.write((byte) ((value >> 8) & 0x00ff));
        payload.write((byte) (value & 0x00ff));

        return new NdefRecord(NdefRecord.TNF_MIME_MEDIA, NdefRecord.RTD_TEXT, null, payload.toByteArray());
    }

    private NdefMessage createNdefMessage() {
        ArrayList<NdefRecord> ndef = new ArrayList<NdefRecord>();

        if(Home.Screen == -1) {
            for(int i = 0; i < Modbus.Size; i++)
                ndef.add(createRecord(Modbus.address[i], Modbus.defaults[i]));
        }
        else if(Home.Screen == 0) {
            for(int i = 0; i < Sensors_External.SensorCount; i++) {
                for(int j = 0; j < Sensors_External.SensorChanges; j++) {
                    if (Sensors_External.changes_bool[i][j])
                        ndef.add(createRecord(Sensors_External.addresses[i][j], Sensors_External.changes[i][j]));
                }
            }
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
