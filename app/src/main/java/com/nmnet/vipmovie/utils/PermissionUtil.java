package com.nmnet.vipmovie.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class PermissionUtil {
    @TargetApi(Build.VERSION_CODES.M)
    public static void check(Activity activity, String permission, int request_code, ICall call) {
        check(activity, new String[]{permission}, request_code, call);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static void check(final Activity activity, final String[] permission, final int request_code, final ICall call) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (!checkPermission(activity, permission)) {
                        ActivityCompat.requestPermissions(activity, permission, request_code);
                        return;
                    }
                } catch (Throwable e) {
                }
                if (call != null) {
                    call.call();
                }
            }
        });
    }


    @TargetApi(Build.VERSION_CODES.M)
    public static boolean checkPermission(Activity activity, String[] permissions) {
        boolean b = true;
        for (String permission : permissions) {
            b = b && (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED);
        }
        return b;
    }

    public static boolean checkPermission(Activity activity, String permissions) {
        try {
            return checkPermission(activity, new String[]{permissions});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /***
     * 用户选择拒绝给APP授权
     */
    public static boolean delayAllPermissions(Activity activity, String... permissions) {
        int count = 0;
        for (String permission : permissions) {
            boolean shouldShowRequestPermissionRationale = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
            boolean denied = ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_DENIED;
            if (!shouldShowRequestPermissionRationale && denied) {
                count++;
            }
        }
        if (count == permissions.length) {
            return true;
        }
        return false;
    }

    public interface ICall {
        void call();
    }
}
