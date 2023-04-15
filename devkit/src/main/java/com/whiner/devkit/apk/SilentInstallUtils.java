package com.whiner.devkit.apk;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInstaller;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class SilentInstallUtils {

    public static final String PACKAGE_INSTALLED_ACTION = "PACKAGE_INSTALLED_ACTION";

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public static void actionNewIntent(@NonNull Intent intent) {
        String action = intent.getAction();
        Bundle extras = intent.getExtras();
        if (PACKAGE_INSTALLED_ACTION.equals(action)) {
            int status = extras.getInt(PackageInstaller.EXTRA_STATUS);
            String packageName = extras.getString(PackageInstaller.EXTRA_PACKAGE_NAME, "");
            boolean success = status == PackageInstaller.STATUS_SUCCESS;
            //发送Event
            EventBus.getDefault().post(new ApkInstallEvent(packageName, success));
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public static void silentInstall(@NonNull final Activity activity, @NonNull final File file) {
        PackageInstaller.Session session;
        try {
            PackageInstaller packageInstaller = activity.getPackageManager().getPackageInstaller();
            PackageInstaller.SessionParams params = new PackageInstaller.SessionParams(PackageInstaller.SessionParams.MODE_FULL_INSTALL);
            int sessionId = packageInstaller.createSession(params);
            session = packageInstaller.openSession(sessionId);
            addApkToInstallSession(file, session);
            // Create an install status receiver.
            Intent intent = new Intent(activity, activity.getClass());
            intent.setAction(PACKAGE_INSTALLED_ACTION);
            @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, intent, 0);
            IntentSender statusReceiver = pendingIntent.getIntentSender();
            // Commit the session (this will start the installation workflow).
            session.commit(statusReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private static void addApkToInstallSession(File file, PackageInstaller.Session session) throws IOException {
        // It's recommended to pass the file size to openWrite(). Otherwise installation may fail
        // if the disk is almost full.
        try (OutputStream packageInSession = session.openWrite("package", 0, -1); FileInputStream is = new FileInputStream(file)) {
            byte[] buffer = new byte[65536];
            int n;
            while ((n = is.read(buffer)) >= 0) {
                packageInSession.write(buffer, 0, n);
            }
        }
    }

}
