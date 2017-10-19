package com.proactivesensing.bobbydouglass.proactiveone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class Advanced extends AppCompatActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advanced);
    }

    public void dataPackets(View view) {
        Home.Screen = 5;
        startActivity(new Intent(this, Advanced_Data.class));
    }

    public void command(View view) {
        Home.Screen = 6;
        startActivity(new Intent(this, Advanced_Command.class));
    }

    public void sync(View view) {
        startActivity(new Intent(this, Advanced_Sync.class));
    }

    public void restore(View view) {

    }

    @Override
    public void onBackPressed() {
        Intent Home = new Intent(this, Home.class);
        Home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(Home);
    }
}
