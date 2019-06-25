package com.realmo.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkAddress;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.RouteInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.annotation.RequiresPermission;
import android.text.format.Formatter;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.Manifest.permission.ACCESS_WIFI_STATE;
import static android.Manifest.permission.INTERNET;

/**
 * @author Realmo
 * @version 1.0.0
 * @name HHTSettingLib
 * @email momo.weiye@gmail.com
 * @time 2019/6/18 17:14
 * @describe
 */
public class NetworkUtils {


    /**
     * Return the ip address.
     *
     * @param useIPv4 True to use ipv4, false otherwise.
     * @return the ip address
     */
    @RequiresPermission(INTERNET)
    public static String getIPAddress(final boolean useIPv4) {
        try {
            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
            LinkedList<InetAddress> adds = new LinkedList<>();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = nis.nextElement();
                if (!ni.isUp() || ni.isLoopback()){
                    continue;
                }
                Enumeration<InetAddress> addresses = ni.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    //adds.addFirst(addresses.nextElement());
                    adds.add(addresses.nextElement());
                }
            }
            for (InetAddress add : adds) {
                if (!add.isLoopbackAddress()) {
                    String hostAddress = add.getHostAddress();
                    boolean isIPv4 = hostAddress.indexOf(':') < 0;
                    if (useIPv4) {
                        if (isIPv4) {
                            return hostAddress;
                        }
                    } else {
                        if (!isIPv4) {
                            int index = hostAddress.indexOf('%');
                            return index < 0
                                    ? hostAddress.toUpperCase()
                                    : hostAddress.substring(0, index).toUpperCase();
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "";
    }

//    /**
//     * Return host name.
//     * <p>Must hold {@code <uses-permission android:name="android.permission.INTERNET" />}</p>
//     *
//     * @return Return host name.
//     */
//    @RequiresPermission(INTERNET)
//    public static String getHostName() {
//        try {
//            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
//            LinkedList<InetAddress> adds = new LinkedList<>();
//            while (nis.hasMoreElements()) {
//                NetworkInterface ni = nis.nextElement();
//                if (!ni.isUp() || ni.isLoopback()){
//                    continue;
//                }
//                Enumeration<InetAddress> addresses = ni.getInetAddresses();
//                while (addresses.hasMoreElements()) {
//                    //adds.addFirst(addresses.nextElement());
//                    adds.add(addresses.nextElement());
//                }
//            }
//            for (InetAddress add : adds) {
//                if (!add.isLoopbackAddress()) {
//
//                   return add.getHostName();
//
//                }
//            }
//        } catch (SocketException e) {
//            e.printStackTrace();
//        }
//        return "";
//    }


    /**
     * Return the gate way by wifi.
     *
     * @return the gate way by wifi
     */
    @RequiresPermission(ACCESS_WIFI_STATE)
    public static String getGatewayByWifi(Context context) {
        @SuppressLint("WifiManagerLeak")
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wm == null) {
            return "";
        }
        return Formatter.formatIpAddress(wm.getDhcpInfo().gateway);
    }

    /**
     * Return the net mask by wifi.
     *
     * @return the net mask by wifi
     */
    @RequiresPermission(ACCESS_WIFI_STATE)
    public static String getSubnetMaskByWifi(Context context) {
        WifiManager wm = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wm == null) {
            return "";
        }
        return Formatter.formatIpAddress(wm.getDhcpInfo().netmask);
    }


    /**
     * Get wifi mac address
     *
     * @param context
     * @return wifi mac address
     */
    @RequiresPermission(allOf = {INTERNET,ACCESS_WIFI_STATE})
    public static String getMacAddressByWifi(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return getMacAddressByWifiN();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
            return getMacAddressByWifiM();
        } else {
            return getMacAddressByWifiBase(context);
        }
    }



    @RequiresPermission(ACCESS_WIFI_STATE)
    private static String getMacAddressByWifiBase(Context context) {

        WifiManager wifi = (WifiManager) context.getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
        try {
            WifiInfo info = wifi.getConnectionInfo();
            if (info == null) {
                return null;
            }
            return info.getMacAddress();
        } catch (Exception e) {
            return null;
        }

    }



    @RequiresApi(Build.VERSION_CODES.M)
    private static String getMacAddressByWifiM() {
        String wifiMacAddress = "02:00:00:00:00:00";
        try {
            wifiMacAddress = new BufferedReader(new FileReader(new File("/sys/class/net/wlan0/address"))).readLine();
        } catch (IOException e) {
            return null;
        }
        return wifiMacAddress;
    }



    @RequiresPermission(INTERNET)
    @RequiresApi(Build.VERSION_CODES.N)
    private static String getMacAddressByWifiN() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) {
                    continue;
                }

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception e) {
            return null;
        }
        return "02:00:00:00:00:00";
    }


    /**
     * Get ethernet mac address
     * @return local mac address
     */
    @RequiresPermission(INTERNET)
    public static String getMacAddressByEthernet() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("eth0")) {
                    continue;
                }

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception e) {
            return null;
        }
        return "02:00:00:00:00:00";
    }

    /**
     * Return the net mask by network status.
     * @param context
     * @return Return the net mask by network status
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    public static String getSubnetMask(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        LinkProperties mLinkProperties = null;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            Network activeNetwork = cm.getActiveNetwork();
            mLinkProperties = cm
                    .getLinkProperties(activeNetwork);
        }else{
            NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
            int type = activeNetworkInfo.getType();
            mLinkProperties = (LinkProperties) ReflectionUtils.invokeMethod(cm,"getLinkProperties",new Class[]{int.class},new Object[]{type});
        }

        int prefix = 0;

        if (mLinkProperties != null) {
            for (LinkAddress addr : mLinkProperties.getLinkAddresses()) {
                if (addr.getAddress() instanceof Inet4Address) {
                    prefix = addr.getPrefixLength();
                    return ipv4CalcMaskByPrefixLength(prefix);
                }else if(addr.getAddress() instanceof Inet6Address){
                    continue;
                }
            }
        }
        return null;
    }

    @RequiresPermission(ACCESS_NETWORK_STATE)
    public static String getActiveGateway(Context context){

        ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        LinkProperties linkProperties = (LinkProperties)ReflectionUtils.invokeMethod(cm,"getActiveLinkProperties",null,null);
        if (linkProperties != null) {
            for (RouteInfo route : linkProperties.getRoutes()) {
                if (route.isDefaultRoute()) {
                    return route.getGateway().getHostAddress();

                }
            }

        }
        return null;
    }

    /**
     * Return the network connected active type.
     * @param context
     * @return Return the network connected active type.
     * one of {@link ConnectivityManager#TYPE_MOBILE}, {@link
     * ConnectivityManager#TYPE_WIFI}, {@link ConnectivityManager#TYPE_WIMAX}, {@link
     * ConnectivityManager#TYPE_ETHERNET},  {@link ConnectivityManager#TYPE_BLUETOOTH}, or other
     * types defined by {@link ConnectivityManager}.
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    public static int getNetworkConnectActiveType(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        return activeNetworkInfo.getType();

    }


    /**
     * Return the ip address by wifi.
     *
     * @return the ip address by wifi
     */
    @RequiresPermission(ACCESS_WIFI_STATE)
    public static String getIpAddressByWifi(Context context) {
        @SuppressLint("WifiManagerLeak")
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wm == null) {
            return "";
        }
        return Formatter.formatIpAddress(wm.getDhcpInfo().ipAddress);
    }

    /**
     *
     * @return dns1 address
     */
    public static String getDNS1(){
        return (String) SystemPropertiesUtils.getProperty("net.dns1","null");
    }

    /**
     *
     * @return dns2 address
     */
    public static String getDNS2(){
        return (String) SystemPropertiesUtils.getProperty("net.dns2","null");
    }

    private static String ipv4CalcMaskByPrefixLength(int prefixLength) {

        int mask = -1 << (32 - prefixLength);
        int partsNum = 4;
        int bitsOfPart = 8;
        int maskParts[] = new int[partsNum];
        int selector = 0x000000ff;

        for (int i = 0; i < maskParts.length; i++) {
            int pos = maskParts.length - 1 - i;
            maskParts[pos] = (mask >> (i * bitsOfPart)) & selector;
        }

        StringBuilder result = new StringBuilder();
        result = result.append(maskParts[0]);
        for (int i = 1; i < maskParts.length; i++) {
            result.append(".").append(maskParts[i]);
        }
        return result.toString();
    }



}
