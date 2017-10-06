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
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class Advanced_Command extends AppCompatActivity {

    NfcAdapter nfcAdapter;
    TextView sendText, receiveText;
    char command;
    String tempCommand;
    int phase = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advanced_command);

        nfcAdapter = nfcAdapter.getDefaultAdapter(this);

        EditText e = (EditText) findViewById(R.id.advanced_command_edittext);
        e.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0)
                    tempCommand = s.toString();
            }
        });
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

    public void command(View view) {
        if(tempCommand.length() == 1) {
            command = tempCommand.charAt(0);

            sendScreen();
        }
        else if(tempCommand.length() == 0)
            Toast.makeText(this, "Input Cannot Be Empty", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "Input Must Be A Single Character", Toast.LENGTH_SHORT).show();
    }

    public void sendScreen() {
        phase = 1;
        setContentView(R.layout.advanced_command_send);
        sendText = (TextView) findViewById(R.id.advanced_command_send_textview);
        sendText.setText("Tap ProactiveOne To Send Command");
    }

    public void receiveScreen() {
        setContentView(R.layout.advanced_command_receive);
        receiveText = (TextView) findViewById(R.id.advanced_command_receive_textview);
        receiveText.setText("Tap ProactiveOne To Read Command Status");
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

        if(phase == 1) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            NdefMessage ndefMessage = createNdefMessage();
            writeNdefMessage(tag, ndefMessage);
        }
        else if(phase == 2) {
            receiveScreen();
            Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            readTextFromMessage((NdefMessage) parcelables[0]);
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

            phase = 2;
            sendText.setText("Tap ProactiveOne To Read Command Status");
        } catch (Exception e) {
        }
    }

    private void writeNdefMessage(Tag tag, NdefMessage ndefMessage) {
        if (tag == null) {
            sendText.setText("Error(1) communicating with ProactiveOne, please try again...");
            return;
        }

        Ndef ndef = Ndef.get(tag);

        if (ndef == null) {
            sendText.setText("Error(2) communicating with ProactiveOne, please try again...");
            formatTag(tag, ndefMessage);
        } else {
            try {
                ndef.connect();
                ndef.writeNdefMessage(ndefMessage);
                ndef.close();
            } catch (Exception e) {
                sendText.setText("Error(3) communicating with ProactiveOne, please try again...");
            }

            phase = 2;
            sendText.setText("Tap ProactiveOne To Read Command Status");
        }
    }

    private NdefRecord createRecord(char value) {
        ByteArrayOutputStream payload = new ByteArrayOutputStream(11);

        payload.write('M');
        payload.write('1');
        payload.write(16);
        payload.write((byte) ((1010 >> 8) & 0x00ff));
        payload.write((byte) (1010 & 0x00ff));
        payload.write(0);
        payload.write(1);
        payload.write(2);
        payload.write((byte) ((value >> 8) & 0x00ff));
        payload.write((byte) (value & 0x00ff));

        return new NdefRecord(NdefRecord.TNF_MIME_MEDIA, NdefRecord.RTD_TEXT, null, payload.toByteArray());
    }

    private NdefMessage createNdefMessage() {
        ArrayList<NdefRecord> ndef = new ArrayList<NdefRecord>();
        ndef.add(createRecord(command));
        NdefRecord[] ndefr = new NdefRecord[ndef.size()];
        for (int i = 0; i < ndef.size(); i++)
            ndefr[i] = ndef.get(i);
        NdefMessage ndefMessage = new NdefMessage(ndefr);

        return ndefMessage;
    }

    private void readTextFromMessage(NdefMessage ndefMessage) {
        NdefRecord[] ndefRecords = ndefMessage.getRecords();
        String[] outputs = new String[ndefRecords.length];
        for(int i = 0; i < ndefRecords.length; i++) {
            outputs[i] = getTextFromNdefRecordStatus(ndefRecords[i]);
            //outputs[i] = getTextFromNdefRecordDebug(ndefRecords[i]);
        }
        String temp = "";
        for(int i = 0; i < ndefRecords.length; i++)
            temp = temp + "\n" + "\n" + outputs[i];
        receiveText.setText(temp);
        receiveText.setMovementMethod(new ScrollingMovementMethod());
        receiveText.setTextScaleX(1.0f);
        receiveText.setTypeface(Typeface.MONOSPACE);
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
