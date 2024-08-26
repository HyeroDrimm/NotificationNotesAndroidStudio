package com.hyerodrimm.notificationnotes;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.room.Room;

import com.hyerodrimm.notificationnotes.database.AppDatabase;

public class MyApp extends Application {
    public static final String NORMAL_NOTIFICATION_CHANNEL_ID = "normal_notification_channel";
    public static final String REPEAT_NOTIFICATION_CHANNEL_ID = "repeat_notification_channel";
    public static AppDatabase appDatabase;
    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();
        appDatabase = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database").allowMainThreadQueries().build();
    }

    private void createNotificationChannels() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
            NotificationChannel normal_channel = new NotificationChannel(NORMAL_NOTIFICATION_CHANNEL_ID, "normal", NotificationManager.IMPORTANCE_HIGH);
            normal_channel.setDescription("Channel for normal non repeating notifications");

            NotificationChannel repeat_channel = new NotificationChannel(REPEAT_NOTIFICATION_CHANNEL_ID, "repeat", NotificationManager.IMPORTANCE_HIGH);
            repeat_channel.setDescription("Channel for repeating notifications");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(normal_channel);
            notificationManager.createNotificationChannel(repeat_channel);


        }
    }
}
