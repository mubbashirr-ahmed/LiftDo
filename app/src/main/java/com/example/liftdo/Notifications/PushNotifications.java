package com.example.liftdo.Notifications;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.example.liftdo.AllActivities.MapActivities.PassengerRideTrack;
import com.example.liftdo.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

@SuppressLint("NewApi")
public class PushNotifications extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        String title = message.getNotification().getTitle();
        String body = message.getNotification().getBody();
        String channelID = "MESSAGE";
        NotificationChannel channel = new NotificationChannel(
                channelID,
                "notification", NotificationManager.IMPORTANCE_HIGH);
        getSystemService(NotificationManager.class).createNotificationChannel(channel);
        Notification.Builder notification = new Notification.Builder(this, channelID)
                .setContentTitle(title)
                        .setContentText(body)
                                .setSmallIcon(R.drawable.logo)
                                        .setAutoCancel(true);


        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1002, notification.build());

    }
}
