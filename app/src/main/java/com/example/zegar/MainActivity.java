package com.example.zegar;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    Button timeSelectionButton;
    int hour, minute;
    AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        CreateAlarmNotificationChannel(); //własna funkcja tworzaca kanał do powiadomień

        timeSelectionButton = findViewById(R.id.alarmSetButton);
    }

    public void popTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {


                hour = selectedHour;
                minute = selectedMinute;

                Calendar calendarNow = Calendar.getInstance();
                Calendar calendarSet = (Calendar) calendarNow.clone();
                calendarSet.set(Calendar.HOUR_OF_DAY, selectedHour);
                calendarSet.set(Calendar.MINUTE, selectedMinute);
                calendarSet.set(Calendar.SECOND, 0);
                calendarSet.set(Calendar.MILLISECOND, 0);

                //Jeśli minęła już godzina, na którą ustawiono alarm
                //data zostaje przesunięta na następny dzień
                if(calendarSet.compareTo(calendarNow) <= 0){
                    calendarSet.add(Calendar.DATE, 1);
                }

                Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast
                        (
                                MainActivity.this,
                                0,
                                intent,
                                PendingIntent.FLAG_IMMUTABLE
                        );

                alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.set(
                        AlarmManager.RTC_WAKEUP, //obudzi urządzenie, żeby wystartować pendingIntent
                        calendarSet.getTimeInMillis(),
                        pendingIntent
                );

                Toast alarmSetInformerToast = Toast.makeText
                        (
                                MainActivity.this,
                                "Alarm o godzinie "+calendarSet.get(Calendar.HOUR_OF_DAY)+":"+calendarSet.get(Calendar.MINUTE),
                                Toast.LENGTH_LONG
                        );
                alarmSetInformerToast.show();
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, hour, minute, true);
        timePickerDialog.setTitle("Ustaw alarm");
        timePickerDialog.show();
    }

    private void CreateAlarmNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel nChannel = new NotificationChannel
                    (
                            "notifyAlarm",
                            "AlarmFinishedChannel",
                            NotificationManager.IMPORTANCE_HIGH
                    );
            nChannel.setDescription("Kanał do powiadomień o ustawionym alarmie");

            NotificationManager nManager = getSystemService(NotificationManager.class);
            nManager.createNotificationChannel(nChannel);
        }
    }
}