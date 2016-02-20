package com.jalee.bbqbuddy;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Aaron on 20/02/2016.
 */
public class SmartTimer_Service extends Service {

    private final static String TAG = "SmartTimer_Service";
    public static List<SmartTimer_cardUI> TimelineList;
    public static Boolean startTimer = false;
    public static Boolean stopTimer = false;
    public static Boolean timerActive = false;
    public static Boolean timerPaused = false;
    public static Boolean timerCancel = false;
    public static Boolean timerComplete = false;
    public static String timerText = "0";
    public static Integer nextEventindex = 0;
    public static Long minsRemaining = 0L;
    public static Long minRemainingElapsed = 0L;
    public static String secondsString;
    public static String minutesString;
    public static Long smartTimerMax;

    public static final String COUNTDOWN_BR = "com.jalee.bbqbuddy.countdown_br";
    Intent bi = new Intent(COUNTDOWN_BR);

    CountDownTimer cdt = null;

    @Override
    public void onCreate() {
        super.onCreate();
        //initialise timeline data
        TimelineList = new ArrayList<>();
        //Populate Sample data
        TimelineList.add(new SmartTimer_cardUI("Have a drink, preferably a James Squire 150 lashes","Drink Beer",2));
        TimelineList.add(new SmartTimer_cardUI("Cook on BBQ on Medium heat for 7 Minutes","Cook Steak - Side 1",7));
        TimelineList.add(new SmartTimer_cardUI("Cook on BBQ on Medium heat for 7 Minutes","Cook Steak - Side 2",7));
        TimelineList.add(new SmartTimer_cardUI("Have a drink, preferably a James Squire 150 lashes","Drink Beer",2));
        TimelineList.add(new SmartTimer_cardUI("Let your meat rest, its tired","Rest Meat",5));
        TimelineList.add(new SmartTimer_cardUI("Have a drink, preferably a James Squire 150 lashes", "Drink Beer", 10));

        //Build Max timer integer
        Integer newSmartTimerValue = 0;
        for (int i = 0; i < TimelineList.size(); i++) {
            newSmartTimerValue = newSmartTimerValue + (Integer) TimelineList.get(i).id;
        }
        //COnvert to Milliseconds for use in timer
        smartTimerMax = TimeUnit.MINUTES.toMillis(newSmartTimerValue);

        final Handler handler = new Handler();

        Runnable run = new Runnable() {
            @Override
            public void run() {
                if (startTimer) {
                    Log.i("Info", "Start variable found, starting timer");
                    timer();
                    startTimer = false;
                }
                handler.postDelayed(this,500);
            }
        };
        handler.post(run);
    }

    public void timer() {
        Log.i(TAG, "Starting timer...");
        if (!timerActive) {
            timerComplete =false;
            new CountDownTimer(smartTimerMax, 1000) {
                public void onTick(long millisecondsUntilDone) {

                    Long timerEventsElapsTotal = 0L;
                    Integer timerEventsElapsTotalint = 0;
                    for (int i = 0; i < TimelineList.size(); i++) {
                        if (i <= nextEventindex) {
                            timerEventsElapsTotalint = timerEventsElapsTotalint + (Integer) TimelineList.get(i).id;
                        }
                    }

                    int hours = (int) TimeUnit.MILLISECONDS.toHours(millisecondsUntilDone);
                    int minutes = (int) (TimeUnit.MILLISECONDS.toMinutes(millisecondsUntilDone) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisecondsUntilDone)));
                    int seconds = (int) (TimeUnit.MILLISECONDS.toSeconds(millisecondsUntilDone) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisecondsUntilDone)));


                    //obtain remaining minutes
                    minsRemaining = (((TimelineList.get(nextEventindex).getId()) + minRemainingElapsed) - (TimeUnit.MILLISECONDS.toMinutes(smartTimerMax) - TimeUnit.MILLISECONDS.toMinutes(millisecondsUntilDone)));

                    //next near next event 1 min out
                    if (TimeUnit.MILLISECONDS.toMinutes((smartTimerMax - millisecondsUntilDone)) == timerEventsElapsTotalint -2) {
                        Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                        if (vibrator.hasVibrator()) {
                            if (vibrator.hasVibrator()) {
                                int dot = 500;      // Length of a Morse Code "dot" in milliseconds
                                int dash = 1000;     // Length of a Morse Code "dash" in milliseconds
                                int short_gap = 200;    // Length of Gap Between dots/dashes
                                int medium_gap = 500;   // Length of Gap Between Letters
                                int long_gap = 1000;    // Length of Gap Between Words
                                long[] pattern = {
                                        0,  // Start immediately
                                        dash, short_gap, dash
                                };
                                vibrator.vibrate(pattern, -1);
                            }
                        }
                    }

                    //next event occured
                    if (TimeUnit.MILLISECONDS.toMinutes((smartTimerMax - millisecondsUntilDone)) == timerEventsElapsTotalint -1) {
                        Log.i("Info","Mins reached");
                        Log.i("info",String.valueOf(seconds));
                        if (seconds == 0) {
                            minRemainingElapsed = minRemainingElapsed + TimelineList.get(nextEventindex).getId();
                            nextEventindex = nextEventindex + 1;
                            Log.i("Info","Increase Next event index");
                            MediaPlayer mplayer = MediaPlayer.create(getApplicationContext(), R.raw.ding);
                            mplayer.start();


                            Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                            if (vibrator.hasVibrator()) {
                                if (vibrator.hasVibrator()) {
                                    int dot = 500;      // Length of a Morse Code "dot" in milliseconds
                                    int dash = 1000;     // Length of a Morse Code "dash" in milliseconds
                                    int short_gap = 200;    // Length of Gap Between dots/dashes
                                    int medium_gap = 500;   // Length of Gap Between Letters
                                    int long_gap = 1000;    // Length of Gap Between Words
                                    long[] pattern = {
                                            0,  // Start immediately
                                            dash, short_gap, dash
                                    };
                                    vibrator.vibrate(pattern, -1);
                                }
                            }
                        }
                    }
                    if (seconds < 10) {
                        secondsString = "0" + String.valueOf(seconds);
                    } else {
                        secondsString = String.valueOf(seconds);
                    }

                    if (minutes < 10) {
                        minutesString = "0" + String.valueOf(minutes);
                    } else {
                        minutesString = String.valueOf(minutes);
                    }

                    //Remove Minutes if no minutes left
                    if (minutes < 1) {
                        timerText = secondsString;
                    } else {
                        if (hours < 1) {
                            timerText = minutesString + ":" + secondsString;
                        } else {
                            timerText = String.valueOf(hours) + ":" + minutesString + ":" + secondsString;
                        }
                    }

                    //Update Notification
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, intent, 0);
                    PendingIntent pauseIntent = PendingIntent.getActivity(getApplicationContext(), 2, intent, 0);
                    PendingIntent playIntent = PendingIntent.getActivity(getApplicationContext(), 3, intent, 0);
                    PendingIntent cancelIntent = PendingIntent.getActivity(getApplicationContext(), 4, intent, 0);

                    int notiColour = getApplicationContext().getResources().getColor(R.color.colorPrimary);

                    if (nextEventindex + 1 == TimelineList.size()) {
                        if (timerPaused) {
                            Notification timerNotification = new Notification.Builder(getApplicationContext())
                                    .setContentTitle(TimelineList.get(nextEventindex).getName())
                                    .addAction(R.drawable.ic_media_play, "Play", pauseIntent)
                                    .addAction(R.drawable.places_ic_clear, "Cancel", cancelIntent)
                                    .setContentText("Finishes in " + String.valueOf(minsRemaining) + ":" + secondsString)
                                    .setContentIntent(pendingIntent)
                                    .setOngoing(true)
                                    .setSmallIcon(R.drawable.cookingicon512px)
                                    .setColor(notiColour)
                                    .build();
                            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
                            notificationManager.notify(1, timerNotification);
                        } else {
                            Notification timerNotification = new Notification.Builder(getApplicationContext())
                                    .setContentTitle(TimelineList.get(nextEventindex).getName())
                                    .addAction(R.drawable.ic_media_pause, "Pause", playIntent)
                                    .addAction(R.drawable.places_ic_clear, "Cancel", cancelIntent)
                                    .setContentText("Finishes in " + String.valueOf(minsRemaining) + ":" + secondsString)
                                    .setContentIntent(pendingIntent)
                                    .setOngoing(true)
                                    .setSmallIcon(R.drawable.cookingicon512px)
                                    .setColor(notiColour)
                                    .build();
                            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
                            notificationManager.notify(1, timerNotification);
                        }
                    } else {
                        if (timerPaused) {
                            Notification timerNotification = new Notification.Builder(getApplicationContext())
                                    .setContentTitle(TimelineList.get(nextEventindex + 1).getName())
                                    .addAction(R.drawable.ic_media_play, "Play", pauseIntent)
                                    .addAction(R.drawable.places_ic_clear, "Cancel", cancelIntent)
                                    .setContentText("Starts in " + String.valueOf(minsRemaining) + ":" + secondsString)
                                    .setContentIntent(pendingIntent)
                                    .setOngoing(true)
                                    .setSmallIcon(R.drawable.cookingicon512px)
                                    .setColor(notiColour)
                                    .build();
                            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
                            notificationManager.notify(1, timerNotification);
                        } else {
                            Notification timerNotification = new Notification.Builder(getApplicationContext())
                                    .setContentTitle(TimelineList.get(nextEventindex + 1).getName())
                                    .addAction(R.drawable.ic_media_pause, "Pause", playIntent)
                                    .addAction(R.drawable.places_ic_clear, "Cancel", cancelIntent)
                                    .setContentText("Starts in " + String.valueOf(minsRemaining) + ":" + secondsString)
                                    .setContentIntent(pendingIntent)
                                    .setOngoing(true)
                                    .setSmallIcon(R.drawable.cookingicon512px)
                                    .setColor(notiColour)
                                    .build();
                            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
                            notificationManager.notify(1, timerNotification);
                        }
                    }

                    if (timerPaused) {
                        timerActive = false;
                        timerPaused = false;
                        smartTimerMax = smartTimerMax - (smartTimerMax - millisecondsUntilDone);
                        cancel();

                    }
                    if (timerCancel) {
                        timerActive = false;
                        timerPaused = false;
                        timerCancel = false;
                        nextEventindex = 0;
                        minsRemaining = 0L;
                        minRemainingElapsed = 0L;
                        cancel();
                        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
                        notificationManager.cancel(1);
                    }
                }

                public void onFinish() {
                    //On Counter finished
                    timerActive = false;
                    timerPaused = false;
                    timerCancel = false;
                    nextEventindex = 0;
                    minsRemaining = 0L;
                    minRemainingElapsed = 0L;
                    timerComplete = true;


                    Intent intent = new Intent(getApplicationContext(), SmartTimer.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, intent, 0);
                    int notiColour = getApplicationContext().getResources().getColor(R.color.colorPrimary);
                    Notification timerNotification = new Notification.Builder(getApplicationContext())
                            .setContentTitle("BBQ Buddy")
                            .setContentText("Your Smart Timer timeline has completed")
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true)
                            .setOngoing(false)
                            .setSmallIcon(R.drawable.cookingicon512px)
                            .setColor(notiColour)
                            .build();

                    NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
                    notificationManager.notify(1, timerNotification);

                    MediaPlayer mplayer = MediaPlayer.create(getApplicationContext(), R.raw.ding);
                    mplayer.start();
                    Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                    if (vibrator.hasVibrator()) {
                        if (vibrator.hasVibrator()) {
                            int dot = 200;      // Length of a Morse Code "dot" in milliseconds
                            int dash = 2000;     // Length of a Morse Code "dash" in milliseconds
                            int short_gap = 200;    // Length of Gap Between dots/dashes
                            int medium_gap = 500;   // Length of Gap Between Letters
                            int long_gap = 1000;    // Length of Gap Between Words
                            long[] pattern = {
                                    0,  // Start immediately
                                    dash, short_gap, dash
                            };
                            vibrator.vibrate(pattern,-1);
                        }
                    }
                }
            }.start();
            timerActive = true;
        }
    }



    @Override
    public void onDestroy() {
        cdt.cancel();
        Log.i(TAG, "Timer cancelled");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}
