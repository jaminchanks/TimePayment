package com.rhythm7.timepayment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.com.rhythm7.com.rhythm.timepayment.R;
import com.rhythm7.timepayment.core.annotations.TimePayment;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foo1();
            }
        });
    }


    @TimePayment
    public void foo1() {
        int i = 0;
        while (i < 1000) {
            i++;
        }
    }
}
