package com.ehappy.mood;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;


import com.golife.contract.AppContract;
import com.golife.customizeclass.CareAlarm;
import com.golife.customizeclass.CareDoNotDisturb;
import com.golife.customizeclass.CareHRWarning;
import com.golife.customizeclass.CareIdleAlert;
import com.golife.customizeclass.CareMeasureHR;
import com.golife.customizeclass.SetCareSetting;
import com.goyourlife.gofitsdk.GoFITSdk;

import java.util.ArrayList;

public class Setting extends Status {
    Button test,battery,find;


    public enum SettingItem {
        STEP_TARGET,
        UNIT,
        TIME_FORMAT,
        AUTO_SHOW_SCREEN,
        SIT_REMINDER,
        BLE_DISCONNECT_NOTIFICATION,
        HANDEDNESS,
        ALARM_CLOCK,
        HR_TIMING_MEASURE,
        LANGUAGE,
        DND,
        SCREEN_LOCK,
        HR_WARNING
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


        test = findViewById(R.id.test);
        battery = findViewById(R.id.battery);
        find = findViewById(R.id.find);


        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setting();
                //初始化Intent物件
                Intent intent = new Intent();
                //從Status 到test
                intent.setClass(Setting.this, test.class);
                //開啟Activity
                startActivity(intent);



            }

        });
        battery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                battery();
            }
        });
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                find();
            }
        });

    }


    public void setting() {
        if (_goFITSdk != null) {

            if (mCareSettings == null) {
                mCareSettings = _goFITSdk.getNewCareSettings();
            }
            //displaySettingMainMenu();
            demoSettingHRTimingMeasure();
        }
        else {
            //showToast("SDK Instance invalid, needs `SDK init`");
        }
    }

    void displaySettingMainMenu() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Setting Option");
        builder.setMessage("1 : 步數目標\n" +
                "2 : 設定單位\n" +
                "3 : 設定時間顯示\n" +
                "4 : 抬手點亮\n" +
                "5 : 久坐提醒\n" +
                "6 : 藍芽斷開連結通知\n" +
                "7 : 配戴的手腕\n" +
                "8 : 設定鬧鐘\n" +
                "9 : 心率偵測\n" +
                "10 : 設定語言\n" +
                "11 : 勿擾模式 \n" +
                "12 : 螢幕鎖 \n" +
                "13 : 心率警示\n" +
                "\n\n999 : Restore default setting");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String userInput = input.getText().toString();
                String instructions = "";
                switch (userInput) {
                    case "1" :
                        instructions = "e.g : 8000";
                        displaySettingDetail(instructions, SettingItem.STEP_TARGET);
                        break;
                    case "2" :
                        instructions = "格式 : [\"imperial(英制)\" / \"metric(公制)\"]\ne.g : imperial";
                        displaySettingDetail(instructions, SettingItem.UNIT);
                        break;
                    case "3" :
                        instructions = "格式 : [\"12\" / \"24\"]\ne.g : 12";
                        displaySettingDetail(instructions, SettingItem.TIME_FORMAT);
                        break;
                    case "4" :
                        instructions = "格式 : [0:off / 1:on]\ne.g : 1";
                        displaySettingDetail(instructions, SettingItem.AUTO_SHOW_SCREEN);
                        break;
                    case "5" :
                        instructions = "格式 : [on/off], [repeatDays(0~127 bit operator)], [HH:mm(startTime)], [HH:mm(endTime)], [IntervalMin]\ne.g : on,127,09:30,18:30,15";
                        displaySettingDetail(instructions, SettingItem.SIT_REMINDER);
                        break;
                    case "6" :
                        instructions = "格式 : [0:off / 1:on]\ne.g : 1";
                        displaySettingDetail(instructions, SettingItem.BLE_DISCONNECT_NOTIFICATION);
                        break;
                    case "7" :
                        instructions = "格式 : [\"left\" / \"right\"]\ne.g : left";
                        displaySettingDetail(instructions, SettingItem.HANDEDNESS);
                        break;
                    case "8" :
                        instructions = "格式 : [on/off], [index(0~29)], [repeatDays(0~127 bit operator)], [HH:mm], [category]\ne.g : on,2,0,07:30,0";
                        displaySettingDetail(instructions, SettingItem.ALARM_CLOCK);
                        break;
                    case "9" :
                        instructions = "格式 : [on/off], [HH:mm(startTime)], [HH:mm(endTime)], [IntervalMin]\ne.g : on,00:00,23:59,15";
                        displaySettingDetail(instructions, SettingItem.HR_TIMING_MEASURE);
                        break;
                    case "10" :
                        instructions = "格式 : [0:TW / 1:CN / 2:EN / 3:JP]\ne.g : 2";
                        displaySettingDetail(instructions, SettingItem.LANGUAGE);
                        break;
                    case "11" :
                        instructions = "格式 : [0:off / 1:on]\ne.g : 1";
                        displaySettingDetail(instructions, SettingItem.DND);
                        break;
                    case "12" :
                        instructions = "格式 : [0:off / 1:on]\ne.g : 1";
                        displaySettingDetail(instructions, SettingItem.SCREEN_LOCK);
                        break;
                    case "13" :
                        instructions = "格式 : [on/off], [max warning], [min warning]\ne.g : on,170,50 (max value must larger than min value)";
                        displaySettingDetail(instructions, SettingItem.HR_WARNING);
                        break;
                    case "999" :
                        mCareSettings = _goFITSdk.getDefaultCareSettings();
                        demoSetSettingToDevice();
                        break;
                    default :
                        showToast("Invalid input");
                        break;
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    SetCareSetting demoGenSettingObject() {
        // Demo - new Setting object
        SetCareSetting mCareSettings = _goFITSdk.getDefaultCareSettings();

        if (mCareSettings != null) {
            // Demo - generate system unit setting
            String systemUnit = mCareSettings.getSystemUnit();
            systemUnit = "imperial";
            mCareSettings.setSystemUnit(systemUnit);

            // Demo - generate time format setting
            String timeFormat = mCareSettings.getTimeFormat();
            timeFormat = "12";
            mCareSettings.setTimeFormat(timeFormat);

            // Demo - generate disconnect alert setting
            SetCareSetting.Switch disconnectAlert = mCareSettings.getEnableDisconnectAlert();
            disconnectAlert = SetCareSetting.Switch.True;
            mCareSettings.setEnableDisconnectAlert(disconnectAlert);
        }

        return mCareSettings;
    }

    void displaySettingDetail(String instructions, final SettingItem setting) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("");
        builder.setMessage(instructions);

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String userInput = input.getText().toString();
                switch (setting) {
                    case STEP_TARGET:
                        demoSettingStepTarget(userInput);
                        break;
                    case UNIT:
                        demoSettingUnit(userInput);
                        break;
                    case TIME_FORMAT:
                        demoSettingTimeFormat(userInput);
                        break;
                    case AUTO_SHOW_SCREEN:
                        demoSettingAutoShowScreen(userInput);
                        break;
                    case SIT_REMINDER:
                        demoSettingSitReminder(userInput);
                        break;
                    case BLE_DISCONNECT_NOTIFICATION:
                        demoSettingBLEDisconnectNotification(userInput);
                        break;
                    case HANDEDNESS:
                        demoSettingHandedness(userInput);
                        break;
                    case ALARM_CLOCK:
                        demoSettingAlarmClock(userInput);
                        break;
                    case HR_TIMING_MEASURE:
                        //demoSettingHRTimingMeasure(userInput);
                        break;
                    case LANGUAGE:
                        demoSettingLanguage(userInput);
                        break;
                    case DND:
                        demoSettingDND(userInput);
                        break;
                    case SCREEN_LOCK:
                        demoSettingScreenLock(userInput);
                        break;
                    case HR_WARNING:
                        demoSettingHRWarning(userInput);
                        break;
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    void demoSettingStepTarget(String userInput) {
        try {
            // Demo - step target setting
            int target = Integer.valueOf(userInput);
            mCareSettings.setStepGoal(target);
            demoSetSettingToDevice();
        }
        catch (NumberFormatException e) {
            showToast("Error Format (not number format)");
        }
    }

    void demoSettingUnit(String userInput) {
        if (userInput.equals("imperial") || userInput.equals("metric")) {
            // Demo - system unit setting
            String systemUnit = userInput;
            mCareSettings.setSystemUnit(systemUnit);
            demoSetSettingToDevice();
        }
        else {
            showToast("Error Format (invalid string : must be `imperial` or `metric`)");
        }
    }

    void demoSettingTimeFormat(String userInput) {
        if (userInput.equals("12") || userInput.equals("24")) {
            // Demo - time format setting
            String timeFormat = userInput;
            mCareSettings.setTimeFormat(timeFormat);
            demoSetSettingToDevice();
        }
        else {
            showToast("Error Format (invalid string : must be `12` or `24`)");
        }
    }

    void demoSettingAutoShowScreen(String userInput) {
        if (userInput.equals("0") || userInput.equals("1")) {
            // Demo - auto show screen setting
            SetCareSetting.Switch setting = SetCareSetting.Switch.None;
            if (userInput.equals("1")) {
                setting = SetCareSetting.Switch.True;
            }
            else {
                setting = SetCareSetting.Switch.False;
            }
            mCareSettings.setEnableAutoLightUp(setting);
            demoSetSettingToDevice();
        }
        else {
            showToast("Error Format (invalid input : must be `0` or `1`)");
        }
    }

    void demoSettingSitReminder(String userInput) {
        String[] separated = userInput.split(",");
        // Demo - sit reminder setting
        CareIdleAlert setting = mCareSettings.getDefaultIdleAlert();
        if (separated.length == 5) {
            if (separated[0].equals("on") || separated[0].equals("off")) {
                boolean enable = separated[0].equals("on") ? true : false;
                setting.setEnableIdleAlert(enable);
            }
            else {
                showToast("Error Format (invalid input : must be `on` or `off`)");
                return;
            }

            try {
                int repeatDays = Integer.valueOf(separated[1]);
                if (repeatDays >= 0 && repeatDays <= 127) {
                    byte[] bytesRepeatDay = convertRepeatDay(repeatDays);
                    setting.setRepeatDays(bytesRepeatDay);
                }
                else {
                    showToast("Error Format (invalid range : 0~127)");
                    return;
                }

            }
            catch (NumberFormatException e) {
                showToast("Error Format (not number format)");
                return;
            }

            int startMin = convertHHmmToMin(separated[2]);
            if (startMin >= 0 && startMin <= 1439) {
                setting.setStartMin((short) startMin);
            }
            else {
                showToast("Error Format (invalid time format)");
            }

            int endMin = convertHHmmToMin(separated[3]);
            if (endMin >= 0 && endMin <= 1439) {
                setting.setEndMin((short) endMin);
            }
            else {
                showToast("Error Format (invalid time format)");
            }

            try {
                int intervalMin = Integer.valueOf(separated[4]);
                setting.setInterval((short)intervalMin);
            }
            catch (NumberFormatException e) {
                showToast("Error Format (not number format)");
                return;
            }

            mCareSettings.setIdleAlert(setting);
            demoSetSettingToDevice();
        }
        else {
            showToast("Error Format (invalid parameter counts)");
            return;
        }
    }

    void demoSettingBLEDisconnectNotification(String userInput) {
        if (userInput.equals("0") || userInput.equals("1")) {
            // Demo - BLE disconnect alert setting
            SetCareSetting.Switch setting = SetCareSetting.Switch.None;
            if (userInput.equals("1")) {
                setting = SetCareSetting.Switch.True;
            }
            else {
                setting = SetCareSetting.Switch.False;
            }
            mCareSettings.setEnableDisconnectAlert(setting);
            demoSetSettingToDevice();
        }
        else {
            showToast("Error Format (invalid input : must be `0` or `1`)");
        }
    }

    void demoSettingHandedness(String userInput) {
        if (userInput.equals("left") || userInput.equals("right")) {
            // Demo - handedness setting
            mCareSettings.setHandedness(userInput);
            demoSetSettingToDevice();
        }
        else {
            showToast("Error Format (invalid input : must be `left` or `right`)");
        }
    }

    void demoSettingAlarmClock(String userInput) {
        String[] separated = userInput.split(",");
        if (separated.length == 5) {
            // Demo - alarm clock setting
            CareAlarm careAlarms = mCareSettings.getAlarms();
            if (careAlarms == null) {
                careAlarms = mCareSettings.getDefaultAlarms();
            }
            ArrayList<CareAlarm.Alarm> alarms = careAlarms.getAlarms();
            CareAlarm.Alarm setting = null;
            int index = 0;
            try {
                index = Integer.valueOf(separated[1]).intValue();
                if (index >= 0 && index <= 29) {
                    setting = alarms.get(index);
                } else {
                    showToast("Error Format [index] (invalid range : 0~29)");
                    return;
                }
            } catch (NumberFormatException e) {
                showToast("Error Format (not number format)");
                return;
            }

            if (separated[0].equals("on") || separated[0].equals("off")) {
                boolean enable = separated[0].equals("on") ? true : false;
                setting.setEnableAlarm(enable);
                setting.setIsActive(enable);
            } else {
                showToast("Error Format (invalid input : must be `on` or `off`)");
                return;
            }

            try {
                int repeatDays = Integer.valueOf(separated[2]).intValue();
                if (repeatDays >= 0 && repeatDays <= 127) {
                    byte[] bytesRepeatDay = convertRepeatDay(repeatDays);
                    setting.setRepeatDays(bytesRepeatDay);
                } else {
                    showToast("Error Format [repeatDays] (invalid range : 0~127)");
                    return;
                }

            } catch (NumberFormatException e) {
                showToast("Error Format (not number format)");
                return;
            }

            int reminderTime = convertHHmmToMin(separated[3]) * 60;  // input must be `seconds`
            if (reminderTime >= 0 && reminderTime <= 86399) {
                setting.setReminderTime(reminderTime);
            } else {
                showToast("Error Format [reminderTime] (invalid time format)");
                return;
            }

            try {
                int category = Integer.valueOf(separated[4]).intValue();
                if (category >= 0 && category <= 7) {
                    setting.setCategory((short) category);
                } else {
                    showToast("Error Format [category] (invalid range : 0~7)");
                    return;
                }
            } catch (NumberFormatException e) {
                showToast("Error Format (not number format)");
                return;
            }

            alarms.set(index, setting);
            careAlarms.setAlarms(alarms);
            mCareSettings.setAlarms(careAlarms);
            demoSetSettingToDevice();
        } else {
            showToast("Error Format (invalid parameter counts)");
        }
    }

    void demoSettingHRTimingMeasure() {
//        String[] separated = userInput.split(",");//on,00:00,23:59,1
//        if (separated.length == 4) {
//            // Demo - HR timing measure setting
            CareMeasureHR careMeasureHR = mCareSettings.getDefaultMeasureHR();
//            careMeasureHR.setRepeatDays(convertRepeatDay(127));
//            if (separated[0].equals("on") || separated[0].equals("off")) {
//                boolean enable = separated[0].equals("on") ? true : false;
//                careMeasureHR.setEnableMeasureHR(enable);
//            }
//            else {
//                showToast("Error Format (invalid input : must be `on` or `off`)");
//                return;
//            }
//
//            int startMin = convertHHmmToMin(separated[1]);
//            if (startMin >= 0 && startMin <= 1439) {
//                careMeasureHR.setStartMin((short) startMin);
//            }
//            else {
//                showToast("Error Format (invalid time format)");
//            }
//
//            int endMin = convertHHmmToMin(separated[2]);
//            if (endMin >= 0 && endMin <= 1439) {
//                careMeasureHR.setEndMin((short) endMin);
//            }
//            else {
//                showToast("Error Format (invalid time format)");
//            }

            try {
//                int intervalMin = Integer.valueOf(01);
                careMeasureHR.setInterval((short)1);
            }
            catch (NumberFormatException e) {
                //showToast("Error Format (not number format)");
                return;
            }

            mCareSettings.setMeasureHR(careMeasureHR);
            demoSetSettingToDevice();
        }
//        else {
//            showToast("Error Format (invalid parameter counts)");
//        }
//    }



    void demoSettingLanguage(String userInput) {
        // Demo - language setting
        AppContract.SystemLanguage language;
        if (userInput.equals("0")) {
            language = AppContract.SystemLanguage.zhrTW;
            mCareSettings.setSystemLanguage(language);
            demoSetSettingToDevice();
        }
        else if (userInput.equals("1")) {
            language = AppContract.SystemLanguage.zhrCN;
            mCareSettings.setSystemLanguage(language);
            demoSetSettingToDevice();
        }
        else if (userInput.equals("2")) {
            language = AppContract.SystemLanguage.en;
            mCareSettings.setSystemLanguage(language);
            demoSetSettingToDevice();
        }
        else if (userInput.equals("3")) {
            language = AppContract.SystemLanguage.ja;
            mCareSettings.setSystemLanguage(language);
            demoSetSettingToDevice();
        }
        else {
            showToast("Error Format (invalid input)");
        }
    }

    void demoSettingDND(String userInput) {
        if (userInput.equals("0") || userInput.equals("1")) {
            // Demo - DND setting
            CareDoNotDisturb setting = mCareSettings.getDefaultDoNotDisturb();
            boolean enable = userInput.equals("1") ? true : false;
            setting.setEnableDoNotDisturb(enable);
            setting.setRepeatDays(convertRepeatDay(127));
            setting.setStartMin((short)convertHHmmToMin("22:00"));
            setting.setEndMin((short)convertHHmmToMin("07:30"));
            mCareSettings.setDoNotDisturb(setting);
            demoSetSettingToDevice();
        }
        else {
            showToast("Error Format (invalid input : must be `0` or `1`)");
        }
    }

    void demoSettingScreenLock(String userInput) {
        if (userInput.equals("0") || userInput.equals("1")) {
            // Demo - screen lock setting
            SetCareSetting.Switch setting = SetCareSetting.Switch.None;
            if (userInput.equals("1")) {
                setting = SetCareSetting.Switch.True;
            }
            else {
                setting = SetCareSetting.Switch.False;
            }
            mCareSettings.setEnableHorizontalUnlock(setting);
            demoSetSettingToDevice();
        }
        else {
            showToast("Error Format (invalid input : must be `0` or `1`)");
        }
    }

    void demoSettingHRWarning(String userInput) {
        String[] separated = userInput.split(",");
        if (separated.length == 3) {
            // Demo - HR warning setting
            CareHRWarning careHRWarning = mCareSettings.getDefaultHRWarning();
            if (separated[0].equals("on") || separated[0].equals("off")) {
                boolean enable = separated[0].equals("on") ? true : false;
                careHRWarning.setEnableHRWarning(enable);
            }
            else {
                showToast("Error Format (invalid input : must be `on` or `off`)");
                return;
            }

            try {
                int maxWarning = Integer.valueOf(separated[1]).intValue();
                careHRWarning.setMaxHRLimit(maxWarning);
            }
            catch (NumberFormatException e) {
                showToast("Error Format (not number format)");
                return;
            }

            try {
                int minWarning = Integer.valueOf(separated[2]).intValue();
                careHRWarning.setMinHRLimit(minWarning);
            }
            catch (NumberFormatException e) {
                showToast("Error Format (not number format)");
                return;
            }

            mCareSettings.setHRWarning(careHRWarning);
            demoSetSettingToDevice();
        }
        else {
            showToast("Error Format (invalid parameter counts)");
        }
    }

    byte[] convertRepeatDay(int days) {
        byte[] repeatDays = {0, 0, 0, 0, 0, 0, 0};
        try {
            repeatDays[0] = (byte) (((days & 0x01) == 1) ? 1 : 0);
            repeatDays[1] = (byte) ((((days >> 1) & 0x01) == 1) ? 1 : 0);
            repeatDays[2] = (byte) ((((days >> 2) & 0x01) == 1) ? 1 : 0);
            repeatDays[3] = (byte) ((((days >> 3) & 0x01) == 1) ? 1 : 0);
            repeatDays[4] = (byte) ((((days >> 4) & 0x01) == 1) ? 1 : 0);
            repeatDays[5] = (byte) ((((days >> 5) & 0x01) == 1) ? 1 : 0);
            repeatDays[6] = (byte) ((((days >> 6) & 0x01) == 1) ? 1 : 0);
        } catch (Exception e) {
            for (int i = 0; i < repeatDays.length; i++) {
                repeatDays[i] = 0;
            }
        }

        return repeatDays;
    }

    int convertHHmmToMin(String HHmm) {
        try {
            String[] timestamp = HHmm.split(":");
            int hour = Integer.parseInt(timestamp[0]);
            int minute = Integer.parseInt(timestamp[1]);
            return (hour * 60 + minute);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    void demoSetSettingToDevice() {
        // Demo - doSetSetting API
        _goFITSdk.doSetSettings(mCareSettings, new GoFITSdk.SettingsCallback() {
            @Override
            public void onCompletion() {
                Log.i(_tag, "doSetSettings() : onCompletion()");
                //showToast("Setting OK");
                //demo_function_setting

                String summary = "Setting OK";
                //showToast(summary);
            }

            @Override
            public void onProgress(String message) {
                Log.i(_tag, "doSetSettings() : onProgress() : message = " + message);
            }

            @Override
            public void onFailure(int errorCode, String errorMsg) {
                Log.e(_tag, "doSetSettings() : onFailure() : errorCode = " + errorCode + ", " + "errorMsg = " + errorMsg);
                //showToast("doSetSettings() : onFailure() : errorCode = " + errorCode + ", " + "errorMsg = " + errorMsg);
            }
        });

    }
}