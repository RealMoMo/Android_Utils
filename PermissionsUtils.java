package com.realmo.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/*
 *
 * <!-- 读写 -->
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <!-- 相机 -->
    <uses-permission android:name="android.permission.CAMERA" />
    <!-- 麦克风 -->
    <uses-permission android:name="android.permission.RECORD_AUDIO" />

    <!-- 位置 -->
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
     <!--联系人-->
     <uses-permission android:name="android.permission.READ_CONTACTS" />
     <uses-permission android:name="android.permission.WRITE_CONTACTS" />
     <uses-permission android:name="android.permission.GET_ACCOUNTS" />
     <!--手机-->
     <uses-permission android:name="android.permission.READ_PHONE_STATE" />
     <uses-permission android:name="android.permission.CALL_PHONE" />
     <uses-permission android:name="android.permission.READ_CALL_LOG" />
     <uses-permission android:name="android.permission.WRITE_CALL_LOG" />
     <uses-permission android:name="android.permission.ADD_VOICEMAIL" />
     <uses-permission android:name="android.permission.USE_SIP" />
     <uses-permission android:name="android.permission.PROCESS_OUTGOING_CALLS" />
     <!--短信-->
     <uses-permission android:name="android.permission.SEND_SMS" />
     <uses-permission android:name="android.permission.RECEIVE_SMS" />
     <uses-permission android:name="android.permission.READ_SMS" />
     <uses-permission android:name="android.permission.RECEIVE_WAP_PUSH" />
     <uses-permission android:name="android.permission.RECEIVE_MMS" />
     <!--日历-->
     <uses-permission android:name="android.permission.READ_CALENDAR" />
     <uses-permission android:name="android.permission.READ_CALENDAR" />
     <!--传感器-->
     <uses-permission android:name="android.permission.BODY_SENSORS" />
 */


public class PermissionsUtils {

    static final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    private static List<String> permissionsNeeded;
    private static CheckVersion checkVersion;


    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void checkPermissions(final Activity activity,CheckVersion checkVersion,int... type) {
        PermissionsUtils.checkVersion = checkVersion;
        permissionsNeeded = new ArrayList<String>();
        final List<String> permissionsList = new ArrayList<String>();

        for (int i = 0; i < type.length; i++) {
            switch (type[i]) {
                case 0: {//存储
                    if (!addPermission(activity, permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))//存储
                    {
                        permissionsNeeded.add("存储");
                    }
                }
                break;
                case 1: {//位置
                    if (!addPermission(activity, permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))//位置
                    {
                        permissionsNeeded.add("位置");
                    }
                }
                break;
                case 2: {//相机
                    if (!addPermission(activity, permissionsList, Manifest.permission.CAMERA))//相机
                    {
                        permissionsNeeded.add("相机");
                    }
                }
                break;

                case 3: {//联系人
                    if (!addPermission(activity, permissionsList, Manifest.permission.READ_CONTACTS))//联系人
                    {
                        permissionsNeeded.add("联系人");
                    }
                }
                break;
                case 4: {//麦克风
                    if (!addPermission(activity, permissionsList, Manifest.permission.RECORD_AUDIO))//麦克风
                    {
                        permissionsNeeded.add("麦克风");
                    }
                }
                break;


                case 5: {//手机

                    if (!addPermission(activity, permissionsList, Manifest.permission.CALL_PHONE))//手机
                    {
                        permissionsNeeded.add("手机");
                    }
                }
                break;
                case 6: {//短信
                    if (!addPermission(activity, permissionsList, Manifest.permission.SEND_SMS))//短信
                    {
                        permissionsNeeded.add("短信");
                    }
                }
                break;
                case 7: {//日历
                    if (!addPermission(activity, permissionsList, Manifest.permission.READ_CALENDAR))//日历
                    {
                        permissionsNeeded.add("日历");
                    }
                }
                break;
                case 8: {//传感器
                    if (!addPermission(activity, permissionsList, Manifest.permission.BODY_SENSORS))//传感器
                    {
                        permissionsNeeded.add("传感器");
                    }
                }
                break;


            }
        }

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // 需要去申请权限
                String message = "你需要申请 " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++) {
                    message = message + ", " + permissionsNeeded.get(i);

                }
                showMessageOKCancel(activity, message + "等权限",
                        new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.M)
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                activity.requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                            }
                        });
                return;
            }
            activity.requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return;
        } else {
            if (checkVersion!=null){
                checkVersion.checkVersionSuccess();
            }
        }

        //可以操作
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private static boolean addPermission(Activity activity, List<String> permissionsList, String permission) {
        if (activity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!activity.shouldShowRequestPermissionRationale(permission)) {
                //permissionsList.add(permission);

            } else {


            }
            return false;


        }
        return true;
    }

    //如果所有权限被授权，依然回调onRequestPermissionsResult，我用hashmap让代码整洁便于阅读。
    private static void showMessageOKCancel(Activity activity, String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(activity)
                .setMessage(message)
                .setPositiveButton("确定", okListener)
                .setNegativeButton("取消", null)
                .create()
                .show();
    }

    //  权限申请回调
    public static void onRequestPermissionsResult(final Activity activity, int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {

                List<String> failpermissions = new ArrayList<>();

                for (int i = 0; i < permissions.length; i++) {
                    switch (permissions[i]) {
                        case Manifest.permission.WRITE_EXTERNAL_STORAGE: {//存储
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {//允许
                                failpermissions.add("存储");
                            }

                        }
                        break;
                        case Manifest.permission.ACCESS_FINE_LOCATION: {//位置
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {//允许
                                failpermissions.add("位置");
                            }
                        }
                        break;
                        case Manifest.permission.CAMERA: {//相机
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {//允许
                                failpermissions.add("相机");
                            }
                        }
                        break;
                        case Manifest.permission.READ_CONTACTS: {//联系人
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {//允许
                                failpermissions.add("联系人");
                            }
                        }
                        break;
                        case Manifest.permission.RECORD_AUDIO: {//麦克风
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {//允许
                                failpermissions.add("麦克风");
                            }
                        }
                        break;
                        case Manifest.permission.CALL_PHONE: {//手机
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {//允许
                                failpermissions.add("手机");
                            }
                        }
                        break;
                        case Manifest.permission.SEND_SMS: {//短信
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {//允许
                                failpermissions.add("短信");
                            }
                        }
                        break;
                        case Manifest.permission.READ_CALENDAR: {//日历
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {//允许
                                failpermissions.add("日历");
                            }
                        }
                        break;
                        case Manifest.permission.BODY_SENSORS: {//传感器
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {//允许
                                failpermissions.add("传感器");
                            }
                        }
                        break;
                    }
                }


                if (failpermissions.size() == 0) {//授权成功
                    if (checkVersion!=null){
                        checkVersion.checkVersionSuccess();
                    }

                } else {
                    // 授权失败

                    Toast.makeText(activity, "部分权限被拒绝，使用过程中可能会出现未知错误", Toast.LENGTH_SHORT)
                            .show();
                    String failStrings = "";
                    for (int i = 0; i < failpermissions.size(); i++) {
                        if (i == failpermissions.size() - 1) {
                            failStrings = failStrings + failpermissions.get(i);
                        } else {
                            failStrings = failStrings + failpermissions.get(i) + ",";
                        }
                    }
                    showMessageOKCancel(activity, "权限被禁 ，需手动打开" + failStrings+"，再次进入改页面生效", new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            activity.startActivity(getAppDetailSettingIntent(activity));
                        }
                    });
                    // checkVersion();
                }
            }
            break;
            default://

                //activity.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    /**
     * 获取应用详情页面intent
     *
     * @return
     */
    private static Intent getAppDetailSettingIntent(Activity activity) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", activity.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", activity.getPackageName());
        }
        return localIntent;
    }



    public interface CheckVersion {

        void checkVersionSuccess();

        void checkVersionFail();
    }
}


