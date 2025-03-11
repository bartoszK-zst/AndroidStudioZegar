package com.example.zegar;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {
    @SuppressLint("MissingPermission")
    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
            context, "notifyAlarm")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Alarm!!!")
                .setContentText("Alaurm!!!!")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(200, notificationBuilder.build());
    }
}
