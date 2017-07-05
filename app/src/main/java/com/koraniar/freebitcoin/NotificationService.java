package com.koraniar.freebitcoin;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.SystemClock;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

/**
 * Created by Esteban on 6/16/2017.
 */

public class NotificationService {

    private static final String LOG_TAG = "NotifAdm";

    public static class MyNotificationPublisher extends BroadcastReceiver {

        public MyNotificationPublisher(){
            super();
        }

        @Override
        public void onReceive(final Context context, Intent intent) {
            NotificationService self = new NotificationService();

            Bundle extras = intent.getExtras();
            if(extras != null) {
                try {
                    int notificationId = extras.getInt("NOTIFICATION_ID");
                    String className = extras.getString("ACTIVITY");
                    String title = extras.getString("TITLE");
                    String message = extras.getString("MESSAGE");
                    Class<?> activity = Class.forName(className);
                    self.showNotification(context, notificationId, title, message, activity);
                }catch (ClassNotFoundException e){
                    Log.e(LOG_TAG, "Class not founded");
                }
            } else {
                Log.e(LOG_TAG, "extras null");
            }
        }
    }

    public void showClaimBtcNotification(Context context, long delay, int notificationId, String title, String contentMessage, String activity) {
        Intent notificationIntent = new Intent(context, MyNotificationPublisher.class);
        notificationIntent.putExtra("NOTIFICATION_ID", notificationId);
        notificationIntent.putExtra("ACTIVITY", activity);
        notificationIntent.putExtra("TITLE", title);
        notificationIntent.putExtra("MESSAGE", contentMessage);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    public void showNotification(Context context, int notificationId, String Title, String Cotent, Class activity) {
        int color = context.getResources().getColor(R.color.colorMainNotification);
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_default_notification_icon)
                        .setColor(color)
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                                R.mipmap.ic_launcher_round))
                        .setContentTitle(Title)
                        .setContentText(Cotent)
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setVibrate(new long[] { 0, 300, 300, 400 })
                        .setLights(Color.YELLOW, 3000, 3000);

        Intent resultIntent = new Intent(context, activity);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT );
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(notificationId, mBuilder.build());
    }

}
