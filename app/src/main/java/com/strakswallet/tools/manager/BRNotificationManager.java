package com.strakswallet.tools.manager;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.strakswallet.presenter.activities.WalletActivity;


/**
 * BreadWallet
 * <p>
 * Created by Mihail Gutan <mihail@breadwallet.com> on 6/28/16.
 * Copyright (c) 2016 breadwallet LLC
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

public class BRNotificationManager {
    public static final String TAG = BRNotificationManager.class.getName();

    public static void sendNotification(Activity ctx, int icon, String title, String message, int mId) {
        if (ctx == null) return;
        Uri soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + ctx.getPackageName() + "/raw/coinflip");
        String channelId = "straks_notification_channel";

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(ctx, channelId)
                        .setSmallIcon(icon)
                        .setContentTitle(title)
                        .setSound(soundUri)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true)    // for remove notification after click
                        .setContentText(message);
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(ctx, WalletActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(ctx);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String channelDescription = "Straks Notification Channel";

            NotificationChannel notificationChannel = mNotificationManager.getNotificationChannel(channelId);
            // DON'T FORGET : If you want to change something on Notification Channel settings,
            // you must delete previous channel or uninstall application or use new channelId name.
            if (notificationChannel == null) {
                AudioAttributes audioAttributes = new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .build();

                notificationChannel = new NotificationChannel(channelId, channelDescription, NotificationManager.IMPORTANCE_DEFAULT);
                notificationChannel.setSound(soundUri, audioAttributes);
                mNotificationManager.createNotificationChannel(notificationChannel);
            }
        }

        // mId allows you to update the notification later on.
        mNotificationManager.notify(mId, mBuilder.build());
    }
}
