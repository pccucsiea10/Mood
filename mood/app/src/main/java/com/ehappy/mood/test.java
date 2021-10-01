package com.ehappy.mood;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class test extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        Button original = (Button)findViewById(R.id.original);
        Button normal = (Button)findViewById(R.id.normal);
        Button Happy = (Button)findViewById(R.id.Happy);
        Button Sad = (Button)findViewById(R.id.Sad);
        Button back3 = (Button) findViewById(R.id.back3);

        //實做OnClickListener界面
        original.setOnClickListener(v -> {
            //初始化Intent物件
            Intent intent = new Intent();
            //從test 到time
            intent.setClass(test.this , time.class);
            //開啟Activity
            startActivity(intent);
        });

        normal.setOnClickListener(v -> {
            //初始化Intent物件
            Intent intent = new Intent();
            //從test 到time
            intent.setClass(test.this , time.class);
            //開啟Activity
            startActivity(intent);
        });

        Happy.setOnClickListener(v -> {
            //初始化Intent物件
            Intent intent = new Intent();
            //從test 到time
            intent.setClass(test.this , time.class);
            //開啟Activity
            startActivity(intent);
        });

        Sad.setOnClickListener(v -> {
            //初始化Intent物件
            Intent intent = new Intent();
            //從test 到time
            intent.setClass(test.this , time.class);
            //開啟Activity
            startActivity(intent);
        });

        back3.setOnClickListener(v -> {
            //初始化Intent物件
            Intent intent = new Intent();
            //從test 到time
            intent.setClass(test.this , Setting.class);
            //開啟Activity
            startActivity(intent);
        });


    }
}