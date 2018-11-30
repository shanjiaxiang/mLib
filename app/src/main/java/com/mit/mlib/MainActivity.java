package com.mit.mlib;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mit.mlib.base.MLog;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MLog.d("hello MLog");
    }
}
