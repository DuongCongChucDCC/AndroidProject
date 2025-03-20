package com.example.myapplication;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.myapplication.Activities.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d("ZZZZZZZZZZZZZZZZZZZZ", "onNewToken: " + token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        String channelId = "FCM_Channel";
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (message.getNotification() != null) {
            NotificationChannel channel = new NotificationChannel(channelId, "FCM Channel", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
            Log.d("ZZZZZZZZZZZZZZZZZZZZ", "Tiêu đề: " + message.getNotification().getTitle());
            Log.d("ZZZZZZZZZZZZZZZZZZZZ", "Nội dung: " + message.getNotification().getBody());
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId).setSmallIcon(android.R.drawable.ic_dialog_info).setContentTitle(message.getNotification().getTitle()).setContentText(message.getNotification().getBody()).setAutoCancel(true).setContentIntent(pendingIntent).setPriority(NotificationCompat.PRIORITY_HIGH);
            notificationManager.notify(0, builder.build());
        }
    }
}
