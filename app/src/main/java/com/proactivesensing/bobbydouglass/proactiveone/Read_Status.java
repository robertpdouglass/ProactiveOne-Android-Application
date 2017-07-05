package com.proactivesensing.bobbydouglass.proactiveone;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;
import android.widget.ToggleButton;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class Read_Status extends AppCompatActivity {

    NfcAdapter nfcAdapter;
    TextView read;
    ToggleButton tgl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_status);

        nfcAdapter = nfcAdapter.getDefaultAdapter(this);
        read = (TextView) findViewById(R.id.textView);
        read.setMovementMethod(new ScrollingMovementMethod());
        tgl = (ToggleButton) findViewById(R.id.toggleButton);
    }

    @Override
    public void onBackPressed() {
        Intent Home = new Intent(this, Home.class);
        Home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(Home);
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
        Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        readTextFromMessage((NdefMessage) parcelables[0]);
    }

    private void readTextFromMessage(NdefMessage ndefMessage) {
        NdefRecord[] ndefRecords = ndefMessage.getRecords();
        String[] outputs = new String[ndefRecords.length];
        for(int i = 0; i < ndefRecords.length; i++) {
            if(!tgl.isChecked())
                outputs[i] = getTextFromNdefRecordStatus(ndefRecords[i]);
            else
                outputs[i] = getTextFromNdefRecordDebug(ndefRecords[i]);
        }
        String temp = "";
        for(int i = 0; i < ndefRecords.length; i++)
            temp = temp + "\n" + "\n" + outputs[i];
        read.setText(temp);
    }

    private void enableForegroundDispatchSystem() {
        Intent intent = new Intent(this, Read_Status.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        IntentFilter[] intentFilters = new IntentFilter[] {};
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null);
    }

    private void disableForegroundDispatchSystem() {
        nfcAdapter.disableForegroundDispatch(this);
    }

    public String getTextFromNdefRecordDebug(NdefRecord ndefRecord) {
        String temp = "";
        try {
            byte[] payload = ndefRecord.getPayload();

            String s = new String(payload, "UTF-8");
            char c1 = s.charAt(0);
            char c2 = s.charAt(1);
            int value = ((payload[3] & 0x00ff) << 8) | ((payload[4] & 0x00ff));
            temp = "Mode: " + c1 + c2 + "\nMB Function: " + payload[2] + "\nMB ID: " + value + "\nReg Count: " + payload[6] + "\nByte Count:" + payload[7] + "\n";
            if(payload[7] == 2) {
                int content = ((payload[8] & 0x00ff) << 8) | ((payload[9] & 0x00ff));
                temp = temp + "Change: " + content;
            }
            else if(payload[7] == 4) {
                byte[] bytes = new byte[]{payload[8], payload[9], payload[10], payload[11], };
                float content = ByteBuffer.wrap(bytes).getFloat();
                temp = temp + "Change: " + content;
            }
            else if(payload[7] == 16) {
                byte[] bytes = new byte[]{payload[8], payload[9], payload[10], payload[11], payload[12], payload[13], payload[14], payload[15],
                        payload[16], payload[17], payload[18], payload[19], payload[20], payload[21], payload[22], payload[23], };
                String content = new String(bytes);
                temp = temp + "Change: " + content;
            }
            else if(payload[7] == 8) {
                byte[] bytes = new byte[]{payload[8], payload[9], payload[10], payload[11], payload[12], payload[13], payload[14], payload[15]};
                String content = new String(bytes);
                temp = temp + "Change: " + content;
            }

        } catch(UnsupportedEncodingException e) {}
        return temp;
    }

    public String getTextFromNdefRecordStatus(NdefRecord ndefRecord) {
        String tagContent = "";
        try {
            byte[] payload = ndefRecord.getPayload();
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
            int languageSize = payload[0] & 0063;
            tagContent = new String(payload, languageSize + 1, payload.length - languageSize - 1, textEncoding);
        } catch (UnsupportedEncodingException e) {}
        return tagContent;
    }
}
