package com.hyerodrimm.notificationnotes;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.hyerodrimm.notificationnotes.database.NoteSave;
import com.hyerodrimm.notificationnotes.database.NoteSaveDao;
import com.hyerodrimm.notificationnotes.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQ_CODE = 100;
    private static final String PERMISSION_POST_NOTIFICATIONS = Manifest.permission.POST_NOTIFICATIONS;

    private NotificationManager notificationManager;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        notificationManager = getSystemService(NotificationManager.class);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);


        findViewById(R.id.test_permission_button).setOnClickListener( v -> requestRuntimePermission());
        findViewById(R.id.send_notification_button).setOnClickListener( v ->{
            NoteSave noteSave = createNotification("message", "title");
            saveNotification(noteSave);
            sendNotification(noteSave);
            List<NoteSave> test = MyApp.appDatabase.noteSaveDao().getAll();
        });
    }

    private NoteSave createNotification(String message, String title){
        NoteSave noteSave = new NoteSave();
        noteSave.message = message;
        noteSave.title = title != null && !title.trim().isEmpty() ? title : message;
        return noteSave;
    }

    private void saveNotification(NoteSave noteSave){
        NoteSaveDao noteSaveDao = MyApp.appDatabase.noteSaveDao();
        noteSaveDao.insertAll(noteSave);
    }

    private void sendNotification(NoteSave noteSave){ sendNotification(noteSave.id, noteSave.title, noteSave.message, false);}

    private void sendNotification(int id, String title, String message, boolean isRepeating){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, isRepeating ? MyApp.REPEAT_NOTIFICATION_CHANNEL_ID : MyApp.NORMAL_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_article_24)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        if (!isNotificationPermissionGranted()) {
            requestRuntimePermission();
        }

        if (notificationManager != null){
            notificationManager.notify(id, builder.build());
        }
    }

    private boolean isNotificationPermissionGranted(){
        return ActivityCompat.checkSelfPermission(MainActivity.this, PERMISSION_POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestRuntimePermission(){
        if (ActivityCompat.checkSelfPermission(MainActivity.this, PERMISSION_POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(MainActivity.this, "Permission Already Granted", Toast.LENGTH_SHORT).show();
        }
        else if (ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSION_POST_NOTIFICATIONS)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("This app requires POST_NOTIFICATION permission to post the notes.")
                    .setTitle("Permission Required")
                    .setCancelable(false)
                    .setPositiveButton("Ok", (dialog, which) -> ActivityCompat.requestPermissions(MainActivity.this, new String[]{PERMISSION_POST_NOTIFICATIONS},PERMISSION_REQ_CODE))
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

            builder.show();
        }
        else{
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{PERMISSION_POST_NOTIFICATIONS},PERMISSION_REQ_CODE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQ_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else if (!ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSION_POST_NOTIFICATIONS)){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("This app REQUIRES permission to post notification. It's called NOTIFICATION Notes, like come on. If you won't grand this permission this app is useless.")
                        .setTitle("Permission Required")
                        .setCancelable(false)
                        .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                        .setPositiveButton("Settings", (dialog, which) -> {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);

                            dialog.dismiss();
                        });

                builder.show();
            }
            else{
                requestRuntimePermission();
            }
        }
    }

}