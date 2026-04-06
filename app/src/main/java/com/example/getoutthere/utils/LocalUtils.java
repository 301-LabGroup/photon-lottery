package com.example.getoutthere.utils;

import android.content.Context;
import android.provider.Settings;

/**
 * Misc file for repeated functions, related to the current user
 */
public class LocalUtils {

    /**
     * Retrieves the unique Android ID for the current device.
     * @param context The context of the calling Activity or Application
     * @return String representing the device ID
     */
    public static String getLocalDeviceId(Context context){
        return Settings.Secure.getString(
                context.getContentResolver(),
                Settings.Secure.ANDROID_ID
        );
    }
}