package com.example.sonakshi.socialcops.notifs;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.arch.lifecycle.Observer;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.example.sonakshi.socialcops.R;
import com.example.sonakshi.socialcops.models.Article;
import com.example.sonakshi.socialcops.models.Specification;
import com.example.sonakshi.socialcops.network.NewsApi;
import com.example.sonakshi.socialcops.network.NewsApiClient;
import com.example.sonakshi.socialcops.ui.MainActivity;

import java.util.List;

import static android.app.PendingIntent.FLAG_ONE_SHOT;


public class AlarmNotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        NewsApi.Category all = NewsApi.Category.valueOf("all");
        final Specification specs = new Specification();
        final NewsApiClient newsApiService = NewsApiClient.getInstance(context);
        final Context con = context;
        specs.setCategory(all);
        Observer<List<Article>> o = new Observer<List<Article>>() {

            @Override
            public void onChanged(@Nullable List<Article> articles) {
                if(articles!=null){

                    Article b = articles.get(0);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(con);
                    Intent myIntent = new Intent(con, MainActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(
                            con,
                            0,
                            myIntent,
                            FLAG_ONE_SHOT );
                    builder.setAutoCancel(true)
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setWhen(System.currentTimeMillis())
                            .setSmallIcon(R.mipmap.ic_news_logo_round)
                            .setContentTitle(b.getTitle())
                            .setContentIntent(pendingIntent)
                            .setContentText(b.getSource().getName())
                            .setStyle(new NotificationCompat.BigTextStyle().bigText(b.getDescription()))
                            .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                            .setSubText(b.getContent());
                    NotificationManager notificationManager = (NotificationManager)con.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        String channelId = "YOUR_CHANNEL_ID";
                        NotificationChannel channel = new NotificationChannel(channelId,
                                "Channel human readable title",
                                NotificationManager.IMPORTANCE_DEFAULT);
                        notificationManager.createNotificationChannel(channel);
                        builder.setChannelId(channelId);
                    }

                    notificationManager.notify(1,builder.build());
                    newsApiService.getHeadlines(specs).removeObserver(this);
                }
            }
        };

        newsApiService.getHeadlines(specs).observeForever(o);
        NewsApi.Category ent = NewsApi.Category.valueOf("entertainment");
        NewsApi.Category gen = NewsApi.Category.valueOf("general");

        /*

        List<Article> networkData = newsApiService.getHeadlines(specs).getValue();
        Article b = networkData.get(0);
        specs.setCategory(ent);
        networkData = newsApiService.getHeadlines(specs).getValue();
        Article e = networkData.get(0);
        specs.setCategory(gen);
        networkData = newsApiService.getHeadlines(specs).getValue();
        Article g = networkData.get(0);*/

        /*NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        NotificationCompat.Builder builder2 = new NotificationCompat.Builder(context);
        NotificationCompat.Builder builder3 = new NotificationCompat.Builder(context);

        Intent myIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                myIntent,
                FLAG_ONE_SHOT );
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_news_logo)
                .setContentTitle(b.getTitle())
                .setContentIntent(pendingIntent)
                .setContentText(b.getDescription())
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setContentInfo(b.getContent());
        builder2.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_news_logo)
                .setContentTitle(e.getTitle())
                .setContentIntent(pendingIntent)
                .setContentText(e.getDescription())
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setContentInfo(e.getContent());
        builder3.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_news_logo)
                .setContentTitle(g.getTitle())
                .setContentIntent(pendingIntent)
                .setContentText(g.getDescription())
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setContentInfo(g.getContent());

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,builder.build());
        notificationManager.notify(2,builder2.build());
        notificationManager.notify(3,builder2.build());*/
    }
}