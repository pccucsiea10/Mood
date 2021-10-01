package com.ehappy.mood;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //取得此Button的實體
        Button start = (Button)findViewById(R.id.start);
        Button end = (Button)findViewById(R.id.end);

        //實做OnClickListener界面
        start.setOnClickListener(v -> {
            //初始化Intent物件
            Intent intent = new Intent();
            //從MainActivity 到user
            intent.setClass(MainActivity.this , user.class);
            //開啟Activity
            startActivity(intent);
        });

        end.setOnClickListener(v ->{
            switch (v.getId()){
                case R.id.end:
                    new AlertDialog.Builder(this)
                        .setTitle("提醒")
                        .setMessage("是否關閉程式?斷開連接?")
                        .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                android.os.Process.killProcess(android.os.Process.myPid());
//                              finish(); // 離開程式
                                System.exit(0);
                                }
                            })
                        .setNegativeButton("取消", null).show();
            break;
            }

        });
    }
}