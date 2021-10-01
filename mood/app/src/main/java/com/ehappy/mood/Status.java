package com.ehappy.mood;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ehappy.remotecamera.CameraClass;
import com.ehappy.remotecamera.RemoteCamera;
import com.golife.contract.AppContract;
import com.golife.customizeclass.ScanBluetoothDevice;
import com.golife.customizeclass.SetCareSetting;
import com.goyourlife.gofitsdk.GoFITSdk;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class Status extends AppCompatActivity {

    public static GoFITSdk _goFITSdk = null;
    public static String sdk_license = null;
    public static String sdk_certificate = null;
    private String mMacAddress = null;
    private String mPairingCode = null;
    private String mPairingTime = null;
    private String mProductID = null;
    SetCareSetting mCareSettings;
    public static String _tag = "activity_status";
    private ScanBluetoothDevice mSelectDevice = null;
    Button connect,next3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);


        connect = (Button) findViewById(R.id.connect);
        next3 = (Button) findViewById(R.id.next3);
        sdk_certificate = null;

        try {
            InputStream inputstream = this.getAssets().open("client_cert.crt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputstream));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            reader.close();
            sdk_certificate = sb.toString();
        } catch (Exception e) {
            Log.e(_tag, e.toString());
            //showToast("Exception : " + e.toString());
        }

//        String enabledListeners = android.provider.Settings.Secure.getString(getContentResolver(), "enabled_notification_listeners");
//        if (enabledListeners == null || !enabledListeners.contains(NotificationListenerService.class.getName())) {
//            new AlertDialog.Builder(this)
//                    .setTitle("提示")
//                    .setMessage("如果您想使用智能手環通知功能，請在您的智能手機中打開設置的APP通知。 （將APP設置為白名單，防止被清理。）")
//                    .setPositiveButton("確定", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            try {
//                                Status.this.startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }).setNegativeButton("取消", null).show();
//        }

        CameraClass.checkPermission(AppContract.PermissionType.storage, this);

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SDKinit();
                scan();

            }
        });
        next3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //初始化Intent物件
                Intent intent = new Intent();
                //從MainActivity 到user
                intent.setClass(Status.this, Setting.class);
                //開啟Activity
                startActivity(intent);

            }
        });

    }

    void showToast(String text) {
        Toast.makeText(Status.this, text, Toast.LENGTH_SHORT).show();
    }

    public void SDKinit() {
        if (_goFITSdk == null) {
            // Read license if exist in local storage
            SharedPreferences sp;
            sp = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor pe = sp.edit();
            sdk_license = sp.getString("sdk_license", null);
            pe.apply();

            _goFITSdk = GoFITSdk.getInstance(this, sdk_certificate, sdk_license, new GoFITSdk.ReceivedLicenseCallback() {
                @Override
                public void onSuccess(String receivedLicense) {
                    Log.i(_tag, receivedLicense);
                    sdk_license = receivedLicense;

                    // Store license in local storage
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Status.this);
                    SharedPreferences.Editor pe = sp.edit();
                    pe.putString("sdk_license", sdk_license);
                    pe.commit();


//                    showToast("SDK init OK : \n" + sdk_license);
                }

                @Override
                public void onFailure(int errorCode, String errorMsg) {
//                    Log.e(_tag, "GoFITSdk.getInstance() : (callback) onFailure() : errorCode = " + errorCode + ", " + "errorMsg = " + errorMsg);
//                    showToast("SDK init Error : \n" + errorMsg);
                }
            });
            _goFITSdk.reInitInstance();
            showToast("請再點擊一次連接裝置!按完之後須等待...");
//            showToast("SDK init!");
        } else {
            _goFITSdk.reInitInstance();
//            showToast("SDK init!");
        }
    }

    public void scan() {

        if (_goFITSdk != null) {

            // Demo - doScanDevice API
            _goFITSdk.doScanDevice(new GoFITSdk.DeviceScanCallback() {
                @Override
                public void onSuccess(ScanBluetoothDevice device) {
                    // TODO : TBD
//                    Log.i(_tag, "doScanDevice() : onSuccess() : device = " + device.getDevice().getName() + ", " + device.getDevice().getAddress() + ", " + device.getRSSI() + ", " + device.getProductID());
                    pairing();
                }

                @Override
                public void onCompletion(ArrayList<ScanBluetoothDevice> devices) {


                    for (ScanBluetoothDevice device : devices) {
                        Log.i(_tag, "doScanDevice() : onCompletion() : device = " + device.getDevice().getName() + ", " + device.getDevice().getAddress() + ", " + device.getRSSI() + ", " + device.getProductID());
                    }

                    if (devices.size() > 0) {
                        mSelectDevice = devices.get(0);
                        String summary = "Recommended Device : \n" + mSelectDevice.getDevice().getAddress() + ", " + mSelectDevice.getRSSI();

//                        showToast(summary);
//                        Log.i(_tag, "doScanDevice() : onCompletion() : mSelectDevice = " + mSelectDevice.getDevice().getName() + ", " + mSelectDevice.getDevice().getAddress() + ", " + mSelectDevice.getRSSI() + ", " + mSelectDevice.getProductID());
                    } else {
                        showToast("未找到裝置,請重新連接!");
                    }

                }

                @Override
                public void onFailure(int errorCode, String errorMsg) {
//                    Log.e(_tag, "doScanDevice() : onFailure() : errorCode = " + errorCode + ", " + "errorMsg = " + errorMsg);
//                    showToast("doScanDevice() : onFailure() : errorCode = " + errorCode + ", " + "errorMsg = " + errorMsg);
                }
            });
        } else {
//            showToast("SDK Instance invalid, needs `SDK init`");
        }
    }

    public boolean pairing() {
        if (_goFITSdk != null) {

            if (mSelectDevice != null) {
                mMacAddress = mSelectDevice.getDevice().getAddress();
            } else {
//                Toast.makeText(Status.this, "No Device Selected, `Scan Device` First!", Toast.LENGTH_SHORT).show();
                return true;
            }

            // Demo - doNewPairing API
            _goFITSdk.doNewPairing(mSelectDevice, new GoFITSdk.NewPairingCallback() {
                @Override
                public void onSuccess(String pairingCode, String pairingTime) {
                    Log.i(_tag, "doNewPairing() : onSuccess() : Got pairingCode = " + pairingCode);
                    Log.i(_tag, "doNewPairing() : onSuccess() : Confirming...");
                    mPairingCode = pairingCode;
                    mPairingTime = pairingTime;
                    mConfirmPairingCodeHandler.postDelayed(mConfirmPairingCodeRunnable, 5000);
                    connect();
                }

                @Override
                public void onFailure(int errorCode, String errorMsg) {
//                    Log.e(_tag, "doNewPairing() : onFailure() : errorCode = " + errorCode + ", " + "errorMsg = " + errorMsg);
//                    showToast("doNewPairing() : onFailure() : errorCode = " + errorCode + ", " + "errorMsg = " + errorMsg);
                }
            });
        } else {
//            showToast("SDK Instance invalid, needs `SDK init`");
        }
        return false;
    }

    public void connect() {
        if (_goFITSdk != null) {


            // Demo - get connect information from local storage
            if (mMacAddress == null || mPairingCode == null || mPairingTime == null) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor pe = sp.edit();
                mMacAddress = sp.getString("macAddress", "");
                mPairingCode = sp.getString("pairCode", "");
                mPairingTime = sp.getString("pairTime", "");
                mProductID = sp.getString("productID", "");
                pe.apply();
            }

            // Demo - doConnectDevice API
            _goFITSdk.doConnectDevice(mMacAddress, mPairingCode, mPairingTime, mProductID, new GoFITSdk.GenericCallback() {
                @Override
                public void onSuccess() {
                    Log.i(_tag, "doConnectDevice() : onSuccess()");
//                    showToast("Connect complete");

                    //demo_connect_status

                    // Demo - isBLEConnect API
                    boolean isConnect = _goFITSdk.isBLEConnect();
                    String summary = isConnect ? "Connected" : "Disconnected";
//                    showToast(summary);

                    //demo_function_connect

                    //showToast("Connected : " + mMacAddress);

                    // Demo - setRemoteCameraHandler API
                    demoSetRemoteCameraHandler();
                }

                @Override
                public void onFailure(int errorCode, String errorMsg) {
                    Log.e(_tag, "doConnectDevice() : onFailure() : errorCode = " + errorCode + ", " + "errorMsg = " + errorMsg);
//                    showToast("doConnectDevice() : onFailure() : errorCode = " + errorCode + ", " + "errorMsg = " + errorMsg);
                }
            });
        }
        else {
//            showToast("SDK Instance invalid, needs `SDK init`");
        }
    }

    public void init() {
        if (_goFITSdk != null) {

            // Demo - doInitialDevice API
            _goFITSdk.doInitialDevice(new GoFITSdk.GenericCallback() {
                @Override
                public void onSuccess() {
                    Log.i(_tag, "doInitialDevice() : onSuccess()");
                    showToast("Initialize OK");
                }

                @Override
                public void onFailure(int errorCode, String errorMsg) {
                    Log.e(_tag, "doInitialDevice() : onFailure() : errorCode = " + errorCode + ", " + "errorMsg = " + errorMsg);
                    showToast("doInitialDevice() : onFailure() : errorCode = " + errorCode + ", " + "errorMsg = " + errorMsg);
                }
            });
        }
        else {
            showToast("SDK Instance invalid, needs `SDK init`");
        }
    }

    public void find() {
        if (_goFITSdk != null) {

            // Demo - doFindMyCare API
            _goFITSdk.doFindMyCare(3);
        }
        else {
//            showToast("SDK Instance invalid, needs `SDK init`");
        }
    }


    public  void timeout() {
        if (_goFITSdk != null) {


            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
            final SharedPreferences.Editor pe = sp.edit();
            mProductID = sp.getString("productID", "");
            pe.apply();

            if (mProductID.length() > 0 && mProductID != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Set timeout (default is 60s)");

                final EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            String userInput = input.getText().toString();
                            int timeout = Integer.valueOf(userInput);

                            // Demo - setConnectTimeout API
                            _goFITSdk.setConnectTimeout(mProductID, timeout);

                            //demo_function_set_connect_timeout
                            String summary = String.format("%d s", timeout);
                            showToast(summary);
                        }
                        catch (NumberFormatException e) {
                            showToast("Error Format (not number format)");
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
            else {
                showToast("`New Pairing` first!");
            }
        }
        else {
            showToast("SDK Instance invalid, needs `SDK init`");
        }
    }

    public void battery() {
        if (_goFITSdk != null) {
            Log.i(_tag, "demo_battery");

            // Demo - getDeviceBatteryValue API
            _goFITSdk.getDeviceBatteryValue(new GoFITSdk.GetDeviceInfoCallback() {
                @Override
                public void onSuccess(String info) {
                    Log.i(_tag, "getDeviceBatteryValue() : onSuccess() : info = " + info  );
//                    showToast("Get Battery OK");

                    //demo_battery
                    showToast(info +"%");
                }

                @Override
                public void onFailure(int errorCode, String errorMsg) {
                    Log.e(_tag, "getDeviceBatteryValue() : onFailure() : errorCode = " + errorCode + ", " + "errorMsg = " + errorMsg);
//                    showToast("getDeviceBatteryValue() : onFailure() : errorCode = " + errorCode + ", " + "errorMsg = " + errorMsg);
                }
            });
        }
        else {
//            showToast("SDK Instance invalid, needs `SDK init`");
        }
    }

    public void disconnect() {
        if (Status._goFITSdk != null) {
//            Log.i(Status._tag, "demo_function_disconnect");
//            showToast("Device Disconnect");

            // Demo - doDisconnectDevice API
            Status._goFITSdk.doDisconnectDevice();


            // Demo - isBLEConnect API
            boolean isConnect = Status._goFITSdk.isBLEConnect();
            String summary = isConnect ?"Connected" : "Disconnected";


        }
        else {
            //showToast("SDK Instance invalid, needs `SDK init`");
        }
    }

    private Handler mConfirmPairingCodeHandler = new Handler();
    private final Runnable mConfirmPairingCodeRunnable = new Runnable() {
        public void run() {
            mConfirmPairingCodeHandler.removeCallbacks(mConfirmPairingCodeRunnable);

            // Demo - confirmPairingCode API
            if (_goFITSdk != null) {
                mProductID = mSelectDevice.getProductID();
                _goFITSdk.doConfirmPairingCode(mPairingCode, mPairingTime, mProductID, new GoFITSdk.GenericCallback() {
                    @Override
                    public void onSuccess() {
                        Log.i(_tag, "doConfirmPairingCode() : onSuccess() : Pairing Complete!");
                        showToast("配對完成!"+mPairingCode);

                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Status.this);
                        SharedPreferences.Editor pe = sp.edit();
                        pe.putString("productID", mProductID);
                        pe.commit();


                        String summary = "Confirm Paring Code : " + mPairingCode + "(" + mPairingTime + ")";
//                        showToast(summary);

                        //demo_connect_status

                        // Demo - isBLEConnect API
                        boolean isConnect = _goFITSdk.isBLEConnect();
                        summary = isConnect ? "連接成功!請按下一步~~" : "未連接成功!!";
                        showToast(summary);

                        // Demo - setRemoteCameraHandler API
                        demoSetRemoteCameraHandler();
                    }

                    @Override
                    public void onFailure(int errorCode, String errorMsg) {
                        Log.e(_tag, "doConfirmPairingCode() : onFailure() : errorCode = " + errorCode + ", " + "errorMsg = " + errorMsg);
//                        showToast("doConfirmPairingCode() : onFailure() : errorCode = " + errorCode + ", " + "errorMsg = " + errorMsg);
                    }
                });
            }
            else {
//                showToast("SDK Instance invalid, needs `SDK init`");
            }
        }
    };

    void demoSetRemoteCameraHandler() {
        _goFITSdk.setRemoteCameraHandler(new AppContract.RemoteCameraHandler() {
            @Override
            public void triggerCamera() {

                Log.e("[RemoteCamera]", "Trigger Remote Camera!");

                if (CameraClass.cameraGetCurrent() != null) {
                    CameraClass.cameraTakePicture(mCameraShutterCallback, CameraClass.mCameraPictureCallback);
                } else {
                    startActivity(new Intent(getApplicationContext(), RemoteCamera.class));
                }

            }
        });
    }

    @SuppressWarnings("deprecation")
    private Camera.ShutterCallback mCameraShutterCallback = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {
            AudioManager mgr = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
            mgr.playSoundEffect(AudioManager.FLAG_PLAY_SOUND, 100);

            Vibrator myVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
            myVibrator.vibrate(300);
        }
    };






}