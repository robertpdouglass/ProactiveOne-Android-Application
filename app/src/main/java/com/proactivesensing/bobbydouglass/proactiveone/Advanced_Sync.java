package com.proactivesensing.bobbydouglass.proactiveone;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Advanced_Sync extends AppCompatActivity {

    NfcAdapter nfcAdapter;
    TextView text;
    boolean phase = false;
    Button button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advanced_sync);

        nfcAdapter = nfcAdapter.getDefaultAdapter(this);
        button = (Button) findViewById(R.id.button);
        text = (TextView) findViewById(R.id.textView);
        text.setMovementMethod(new ScrollingMovementMethod());
        text.setTextScaleX(1.0f);
        text.setTypeface(Typeface.MONOSPACE);
        text.setTextSize(30.0f);
        text.setText("Tap ProactiveOne to Initiate Programming Sync");
    }

    @Override
    public void onBackPressed() {
        Intent Adv = new Intent(this, Advanced.class);
        Adv.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(Adv);
    }

    public void cancel(View view) {
        Intent Adv = new Intent(this, Advanced.class);
        Adv.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(Adv);
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

        if(phase) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            NdefMessage ndefMessage = createNdefMessage();
            writeNdefMessage(tag, ndefMessage);
        }
        else {
            Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            readInfoFromSync((NdefMessage) parcelables[0]);
        }
    }

    private void enableForegroundDispatchSystem() {
        Intent intent = new Intent(this, Advanced_Command.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
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

            phase = true;
            text.setText("Tap ProactiveOne To Sync Programming");
        }
        catch (Exception e) {}
    }

    private void writeNdefMessage(Tag tag, NdefMessage ndefMessage) {
        if (tag == null) {
            text.setText("Error(1) communicating with ProactiveOne, please try again...");
            return;
        }

        Ndef ndef = Ndef.get(tag);

        if (ndef == null) {
            text.setText("Error(2) communicating with ProactiveOne, please try again...");
            formatTag(tag, ndefMessage);
        } else {
            try {
                ndef.connect();
                ndef.writeNdefMessage(ndefMessage);
                ndef.close();
            } catch (Exception e) {
                text.setText("Error(3) communicating with ProactiveOne, please try again...");
            }

            phase = true;
            text.setText("Tap ProactiveOne To Sync Programming");
        }
    }

    private NdefRecord createRecord() {
        ByteArrayOutputStream payload = new ByteArrayOutputStream(11);

        payload.write('M');
        payload.write('1');
        payload.write(16);
        payload.write((byte) ((1010 >> 8) & 0x00ff));
        payload.write((byte) (1010 & 0x00ff));
        payload.write(0);
        payload.write(1);
        payload.write(2);
        payload.write((byte) (('!' >> 8) & 0x00ff));
        payload.write((byte) ('!' & 0x00ff));

        return new NdefRecord(NdefRecord.TNF_MIME_MEDIA, NdefRecord.RTD_TEXT, null, payload.toByteArray());
    }

    private NdefMessage createNdefMessage() {
        ArrayList<NdefRecord> ndef = new ArrayList<NdefRecord>();
        ndef.add(createRecord());
        NdefRecord[] ndefr = new NdefRecord[ndef.size()];
        for (int i = 0; i < ndef.size(); i++)
            ndefr[i] = ndef.get(i);
        NdefMessage ndefMessage = new NdefMessage(ndefr);

        return ndefMessage;
    }

    private void readInfoFromSync(NdefMessage ndefMessage) {
        NdefRecord[] ndefRecords = ndefMessage.getRecords();
        for(int i = 0; i < 6; i++)
            getInfoFromNdefRecord(ndefRecords[i], i);

        text.setText("Sync Complete!");
        button.setText("Back");
    }

    public void getInfoFromNdefRecord(NdefRecord ndefRecord, int off) {
        byte[] payload = ndefRecord.getPayload();
        int offset = 0;
        if(off == 0)
            offset = 2;
        int startAdd = ((payload[3 + offset] & 0xff) << 8) | ((payload[4 + offset] & 0xff));
        int quanRegs = ((payload[5 + offset] & 0xff) << 8) | ((payload[6 + offset] & 0xff));
        List<int[]> changes = new ArrayList<int[]>();
        int[] input = new int[2];
        for(int i = 0, j = 7 + offset, add = startAdd; i < quanRegs; i++, startAdd++, j += 2, add++) {
            input[0] = add;
            input[1] = ((payload[j] & 0xff) << 8) | ((payload[j+1] & 0xff));
            changes.add(input);
        }

        Modbus mod = new Modbus(getApplicationContext(), changes);
    }
}
