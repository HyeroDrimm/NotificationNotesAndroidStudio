package com.hyerodrimm.notificationnotes;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.hyerodrimm.notificationnotes.database.NoteSave;
import com.hyerodrimm.notificationnotes.database.NoteSaveDao;

public class NotificationHelper {
    public static final String PERMISSION_POST_NOTIFICATIONS = Manifest.permission.POST_NOTIFICATIONS;
    public static final int PERMISSION_REQ_CODE = 100;

    public static void requestRuntimePermission(Context context, Activity activity){
        if (ActivityCompat.checkSelfPermission(context, PERMISSION_POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(context, "Permission Already Granted", Toast.LENGTH_SHORT).show();
        }
        else if (ActivityCompat.shouldShowRequestPermissionRationale(activity, PERMISSION_POST_NOTIFICATIONS)){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("This app requires POST_NOTIFICATION permission to post the notes.")
                    .setTitle("Permission Required")
                    .setCancelable(false)
                    .setPositiveButton("Ok", (dialog, which) -> ActivityCompat.requestPermissions(activity, new String[]{PERMISSION_POST_NOTIFICATIONS},PERMISSION_REQ_CODE))
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

            builder.show();
        }
        else{
            ActivityCompat.requestPermissions(activity, new String[]{PERMISSION_POST_NOTIFICATIONS},PERMISSION_REQ_CODE);
        }
    }

    public static NoteSave createNotification(String message, String title){
        NoteSave noteSave = new NoteSave();
        noteSave.message = message;
        noteSave.title = title != null && !title.trim().isEmpty() ? title : message;
        return noteSave;
    }

    public static NoteSave saveNotification(NoteSave noteSave){
        NoteSaveDao noteSaveDao = MyApp.historyDatabase.noteSaveDao();
        noteSave.id = (int) noteSaveDao.insert(noteSave);
        return noteSave;
    }

    public static void sendNotification(Context context, Activity activity, NotificationManager notificationManager,NoteSave noteSave){ sendNotification(context, activity,notificationManager, noteSave.id, noteSave.title, noteSave.message, false);}

    public static void sendNotification(Context context, Activity activity, NotificationManager notificationManager, int id, String title, String message, boolean isRepeating){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, isRepeating ? MyApp.REPEAT_NOTIFICATION_CHANNEL_ID : MyApp.NORMAL_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        if (!isNotificationPermissionGranted(context)) {
            requestRuntimePermission(context, activity);
        }

        if (notificationManager != null){
            notificationManager.notify(id, builder.build());
        }
    }

    public static boolean isNotificationPermissionGranted(Context context){
        return ActivityCompat.checkSelfPermission(context, PERMISSION_POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;
    }


}
