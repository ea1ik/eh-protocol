package com.example.ehprotocol;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationsSender extends Application {
    public static final String HIGH_PRIORITY_CHANNEL = "alarmChannel";
    public static final String MEDIUM_PRIORITY_CHANNEL = "casesChannel";
    public static final String LOW_PRIORITY_CHANNEL = "faqChannel";

    private static NotificationManagerCompat notificationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel alarmChannel = new NotificationChannel(
                    HIGH_PRIORITY_CHANNEL,
                    "Alarm Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            alarmChannel.setDescription("This channel is for sending important notifications if you've been in contact with a person that tested positive recently.");

            NotificationChannel casesChannel = new NotificationChannel(
                    MEDIUM_PRIORITY_CHANNEL,
                    "Cases Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            casesChannel.setDescription("This channel is for sending you daily updates about the corona virus statistics in Lebanon.");

            NotificationChannel faqChannel = new NotificationChannel(
                    LOW_PRIORITY_CHANNEL,
                    "Cases Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            faqChannel.setDescription("This channel is for sending you frequently asked questions.");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(alarmChannel);
            manager.createNotificationChannel(casesChannel);
            manager.createNotificationChannel(faqChannel);
        }
    }

    public static void sendOverNotificationChannel(Context context, String channelID, String title, String message) {
        notificationManager = NotificationManagerCompat.from(context);

        Notification notification = new NotificationCompat.Builder(context, channelID)
                .setSmallIcon(R.drawable.notif)
                .setContentTitle(title)
                .setContentText(message)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManager.notify((int) Math.random()* Integer.MAX_VALUE, notification);
    }

}
