package com.whiner.devkit.utils;

import android.util.Log;

import java.net.NetworkInterface;
import java.util.Enumeration;

public class MacUtils {

    private static final String TAG = "MacUtils";

    private static final String DEF_MAC = "02:00:00:00:00:00";

    //先读取网口的Mac  再读取Wifi Mac
    public static String getMac() {
        String mac = getEthMac();
        if (DEF_MAC.equals(mac)) {
            mac = getWifiMac();
        }
        return mac;
    }

    public static String getEthMac() {
        return getMacByInterface("eth0");
    }

    public static String getWifiMac() {
        return getMacByInterface("wlan0");
    }

    private static String getMacByInterface(String interfaceName) {
        try {
            Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
            while (enumeration.hasMoreElements()) {
                NetworkInterface networkInterface = enumeration.nextElement();
                if (networkInterface != null) {
                    Log.d(TAG, "getMacAddress: 接口名称 " + networkInterface.getName());
                    //但接口是scan,也直接进去取到一个mac即可
                    if (networkInterface.getName().equalsIgnoreCase(interfaceName)) {
                        byte[] macBytes = networkInterface.getHardwareAddress();
                        if (macBytes != null) {
                            String mac = bytes2HexString(macBytes);
                            Log.d(TAG, "getMacAddress: 原始地址 " + mac);
                            if (mac.length() == 12) {
                                StringBuilder stringBuilder = new StringBuilder(mac);
                                stringBuilder.insert(2, ":");
                                stringBuilder.insert(5, ":");
                                stringBuilder.insert(8, ":");
                                stringBuilder.insert(11, ":");
                                stringBuilder.insert(14, ":");
                                Log.d(TAG, "getMacAddress: 最终地址 " + stringBuilder);
                                return stringBuilder.toString();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return DEF_MAC;
    }

    private static String bytes2HexString(final byte[] bytes) {
        if (bytes == null) return "";
        final char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        int len = bytes.length;
        if (len <= 0) return "";
        char[] ret = new char[len << 1];
        for (int i = 0, j = 0; i < len; i++) {
            ret[j++] = hexDigits[bytes[i] >> 4 & 0x0f];
            ret[j++] = hexDigits[bytes[i] & 0x0f];
        }
        return new String(ret);
    }

}
