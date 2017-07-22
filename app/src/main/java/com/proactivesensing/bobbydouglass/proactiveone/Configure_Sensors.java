package com.proactivesensing.bobbydouglass.proactiveone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class Configure_Sensors extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configure_sensors);
    }

    public void external(View view) {
        Home.Screen = 0;
        startActivity(new Intent(this, Sensors_External.class));
    }

    public void i2c(View view) {
        Home.Screen = 1;
        startActivity(new Intent(this, Sensors_I2C.class));
    }

    public void internal(View view) {
        Home.Screen = 2;
        startActivity(new Intent(this, Sensors_Internal.class));
    }

    @Override
    public void onBackPressed() {
        Intent Home = new Intent(this, Home.class);
        Home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(Home);
    }
}