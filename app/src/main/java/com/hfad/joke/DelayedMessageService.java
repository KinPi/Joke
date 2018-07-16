package com.hfad.joke;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

// Declare your service in the manifest!
public class DelayedMessageService extends IntentService {

    public static final String TAG = DelayedMessageService.class.getSimpleName();
    public static final String EXTRA_MESSAGE = "message";
    public static final int NOTIFICATION_ID = 5453;

    private int count = 0; // Reuses same instance if it exists, else create new instance of DelayedMessageService.

    public DelayedMessageService () {
        super(TAG);
    }

    // Runs on a separate thread
    @Override
    protected void onHandleIntent(Intent intent) {
        synchronized (this) {
            try {
                wait(5000);
                count++;
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        String text = intent.getStringExtra(EXTRA_MESSAGE);
        showText(text);
    }

    private void showText(String text) {
        Log.v(TAG, "The message is: " + text + " and count is: " + count);

        String channelId = "my_channel_01";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.sym_def_app_icon)
                .setContentTitle(getString(R.string.question))
                .setContentTitle(text)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(new long[]{0, 1000})
                .setAutoCancel(true)
                .setChannelId(channelId);

        // Create an action
        Intent actionIntent = new Intent(this, MainActivity.class);
        PendingIntent actionPendingIntent = PendingIntent.getActivity(this, 0, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(actionPendingIntent);

        // Issue the notification
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Testing", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

}
