package com.example.dheerajkaushik.lab1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv = new TextView(this);
        tv.setText(HelloJNI());
        setContentView(tv);
    }
    public native String HelloJNI();
    static {
        System.loadLibrary("HelloJNI");

    }
}
