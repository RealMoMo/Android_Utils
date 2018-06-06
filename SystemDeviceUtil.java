package com.hht.uc.Quicksetting.utils;

import android.content.Context;
import java.lang.reflect.Method;
import android.media.AudioManager;

public class SystemDeviceUtil {


    public static void setMute(boolean mute,AudioManager audiomanger){
            Class<?> c = null;

       try {
           c = Class.forName("android.media.AudioManager");

            Method m1 = c.getMethod("setMasterMute", boolean.class);

            m1.invoke(audiomanger,mute);
       }  catch (Exception e){
             e.printStackTrace();
       }

    }


//        public static void setMute(boolean mute,Context context){
//            Class<?> c = null;
//            AudioManager mAudioManager = null;
//       try {
//             c = Class.forName("android.media.AudioManager");
//
//             java.lang.reflect.Constructor<?> con = c.getConstructor(Context.class);
//
//             mAudioManager = (AudioManager) con.newInstance(context);
//
//            Method m1 = c.getMethod("setMasterMute", boolean.class);
//
//            m1.invoke(mAudioManager,mute);
//       }  catch (Exception e){
//             e.printStackTrace();
//       }
//
//    }


    public static void setWifiP2pDeviceName(String devName,Context context) {
        WifiP2pManager manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        WifiP2pManager.Channel channel = manager.initialize(context, context.getMainLooper(), null);
        try {
            Class[] paramTypes = new Class[3];
            paramTypes[0] = WifiP2pManager.Channel.class;
            paramTypes[1] = String.class;
            paramTypes[2] = WifiP2pManager.ActionListener.class;
            Method setDeviceName = manager.getClass().getMethod(
                    "setDeviceName", paramTypes);
            setDeviceName.setAccessible(true);

            Object arglist[] = new Object[3];
            arglist[0] = channel;
            arglist[1] = devName;
            arglist[2] = new WifiP2pManager.ActionListener() {

                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailure(int reason) {

                }
            };

            setDeviceName.invoke(manager, arglist);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

}
