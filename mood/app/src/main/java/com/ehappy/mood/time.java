package com.ehappy.mood;

import android.content.Intent;
import android.os.SystemClock;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;


public class time extends AppCompatActivity {
    Chronometer chronometer_down;
    Button start1;
    Button Next;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);


        chronometer_down = findViewById(R.id.count_down);
        start1 = findViewById(R.id.start1);
        Next = findViewById(R.id.Next1);

        chronometer_down.setBase(SystemClock.elapsedRealtime() + 300000);


        start1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                chronometer_down.start();

            }


        });

        chronometer_down.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {

            @Override
            public void onChronometerTick(Chronometer chronometer) {


                chronometer_down.setText(chronometer_down.getText().toString().substring(1));
                if (SystemClock.elapsedRealtime() - chronometer_down.getBase() >= 0) {
                    chronometer_down.stop();
                    chronometer_down.setText("測試結束!!!");
                }
            }
        });

        Next.setOnClickListener(v -> {

            //初始化Intent物件
            Intent intent = new Intent();
            //從time 到test
            intent.setClass(time.this, BreakTime.class);
            //開啟Activity
            startActivity(intent);


        });

    }


}

