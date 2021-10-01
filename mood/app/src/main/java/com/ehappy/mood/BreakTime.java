package com.ehappy.mood;

import android.content.Intent;
import android.os.SystemClock;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

import com.golife.database.table.TablePulseRecord;
import com.golife.database.table.TableSleepRecord;
import com.golife.database.table.TableSpO2Record;
import com.golife.database.table.TableStepRecord;
import com.goyourlife.gofitsdk.GoFITSdk;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.protocol.HTTP;

public class BreakTime extends Status {
    Chronometer chronometer_down1;
    Button start2;
    Button Back2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_break_time);


        chronometer_down1 = findViewById(R.id.count_down1);
        start2 = findViewById(R.id.start2);
        Back2 = findViewById(R.id.back2);
        chronometer_down1.setBase(SystemClock.elapsedRealtime() + 300000);
        start2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                sync();
                chronometer_down1.start();


            }
        });
        chronometer_down1.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {

            @Override
            public void onChronometerTick(Chronometer chronometer) {

                chronometer_down1.setText(chronometer_down1.getText().toString().substring(1));
                if (SystemClock.elapsedRealtime()-chronometer_down1.getBase()>=0){chronometer_down1.stop();
                    chronometer_down1.setText("休息時間結束!!");}
            }
        });

        Back2.setOnClickListener(v -> {
            clear();
            //初始化Intent物件
            Intent intent = new Intent();
            //從time 到test
            intent.setClass(BreakTime.this , test.class);
            //開啟Activity
            startActivity(intent);
        });

    }
    public void sync() {
        if (_goFITSdk != null) {


            // Demo - doSyncFitnessData API
            _goFITSdk.doSyncFitnessData(new GoFITSdk.SyncCallback() {
                @Override
                public void onCompletion() {
                    Log.i(_tag, "doSyncFitnessData() : onCompletion()");
                    //showToast("Sync complete!\nDetail fitness data show in `Logcat`");
                }

                @Override
                public void onProgress(String message, int progress) {
                    Log.i(_tag, "doSyncFitnessData() : onProgress() : message = " + message + ", progress = " + progress);

                    String summary = String.format("%d", progress);
                    //showToast(summary);
                }

                @Override
                public void onFailure(int errorCode, String errorMsg) {
                    Log.e(_tag, "doSyncFitnessData() : onFailure() : errorCode = " + errorCode + ", " + "errorMsg = " + errorMsg);
                    //showToast("doSyncFitnessData() : onFailure() : errorCode = " + errorCode + ", " + "errorMsg = " + errorMsg);
                }

                @Override
                public void onGetFitnessData(ArrayList<TableStepRecord> stepRecords, ArrayList<TableSleepRecord> sleepRecords, ArrayList<TablePulseRecord> hrRecords, ArrayList<TableSpO2Record> spo2Records) {

                    StringEntity entity ;
                    for (TableStepRecord step : stepRecords) {
                        Log.i(_tag, "doSyncFitnessData() : onGetFitnessData() : step = " + step.toJSONString());
//                        entity = new StringEntity(step.toJSONString(), HTTP.UTF_8);
//                        ElasticRestClient.post(null,"hh/_doc", entity, "application/json",new JsonHttpResponseHandler() );

                    }

                    for (TableSleepRecord sleep : sleepRecords) {
                        Log.i(_tag, "doSyncFitnessData() : onGetFitnessData() : sleep = " + sleep.toJSONString());
//                        entity = new StringEntity(sleep.toString(), HTTP.UTF_8);
//                        ElasticRestClient.post(null,"hh/_doc", entity, "application/json",new JsonHttpResponseHandler() );

                    }
                    try {
                        for (TablePulseRecord hr : hrRecords) {
                            Log.i(_tag, "doSyncFitnessData() : onGetFitnessData() : hr = " + hr.toJSONString());
                            JSONObject j = new JSONObject();
                            j.put("name", user.UserName.getText().toString());
                            j.put("age", user.Age.getSelectedItem().toString());
                            j.put("sex",user.Sex.getSelectedItem().toString());
                            j.put("pulse",hr.getPulse());
                            j.put("time",hr.getTimestamp());
                            j.put("timeForCompare",hr.getTimestampForCompare());
                            entity = new StringEntity(j.toString(), HTTP.UTF_8);
                            ElasticRestClient.post(null,"hh1/_doc", entity, "application/json",new JsonHttpResponseHandler() );
                        }

                    } catch (JSONException e) {
                    }

                    for (TableSpO2Record spo2 : spo2Records) {
                        Log.i(_tag, "doSyncFitnessData() : onGetFitnessData() : spo2 = " + spo2.toJSONString());
                    }
                }
            });
        }
        else {
            //showToast("SDK Instance invalid, needs `SDK init`");
        }
    }



    public void clear() {
        if (_goFITSdk != null) {

            // Demo - doClearDeviceData API
            _goFITSdk.doClearDeviceData(new GoFITSdk.GenericCallback() {
                @Override
                public void onSuccess() {
                    Log.i(_tag, "doClearDeviceData() : onSuccess()");
                    //showToast("Clear Data OK");
                }

                @Override
                public void onFailure(int errorCode, String errorMsg) {
                    Log.e(_tag, "doClearDeviceData() : onFailure() : errorCode = " + errorCode + ", " + "errorMsg = " + errorMsg);
                    //showToast("doClearDeviceData() : onFailure() : errorCode = " + errorCode + ", " + "errorMsg = " + errorMsg);
                }
            });
        }
        else {
            //showToast("SDK Instance invalid, needs `SDK init`");
        }
    }
}