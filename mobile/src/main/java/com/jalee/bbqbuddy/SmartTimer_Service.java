package com.jalee.bbqbuddy;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.v7.app.NotificationCompat;
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
    public static Boolean timerActive = false;
    public static Boolean timerPaused = false;
    public static Boolean timerCancel = false;
    public static Boolean timerComplete = false;
    public static Boolean timerSkip = false;
    public static String timerText = "0";
    public static Integer nextEventindex = 0;
    public static Long minsRemaining = 0L;
    public static Long minRemainingElapsed = 0L;
    public static String secondsString;
    public static String minutesString;
    public static Long smartTimerMax;
    public static Long smartTimerCurrentMax;
    public static Integer timerEventsRem = 0;


    public static final String COUNTDOWN_BR = "com.jalee.bbqbuddy.countdown_br";
    Intent bi = new Intent(COUNTDOWN_BR);

    CountDownTimer cdt = null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null && intent.getAction() != null) {
            if (intent.getAction().equals("Start Foreground")) {
                Log.i(TAG, "Received Start Foreground Intent ");
                updateNotification();
            } else if (intent.getAction().equals("pause")) {
                Log.i(TAG, "Clicked Previous");
                timerPaused = true;
            } else if (intent.getAction().equals("play")) {
                Log.i(TAG, "Clicked Play");
                startTimer = true;
            } else if (intent.getAction().equals("next")) {
                Log.i(TAG, "Clicked Skip");
                timerSkip = true;
            } else if (intent.getAction().equals(
                    "stop")) {
                Log.i(TAG, "Received Stop Foreground Intent");
                stopForeground(true);
                stopSelf();
            }
        } else {
            //if no in intent, service must have crashed, relaunch with last known data
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.jalee.bbqbuddy", MODE_PRIVATE);
            smartTimerCurrentMax = sharedPreferences.getLong("curMilli", 0);
            nextEventindex = sharedPreferences.getInt("curIndex", 0);
            loadTimeLine(this);
            intTimer();
        }

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        final Handler handler = new Handler();

        Runnable run = new Runnable() {
            @Override
            public void run() {
                if (startTimer) {
                    Log.i(TAG, "Start variable found, starting timer");
                    if (!timerPaused) {
                        //rebuild start values
                        Integer newSmartTimerValue = 0;
                        for (int i = 0; i < TimelineList.size(); i++) {
                            newSmartTimerValue = newSmartTimerValue + (Integer) TimelineList.get(i).id;
                        }
                        smartTimerMax = TimeUnit.MINUTES.toMillis(newSmartTimerValue);
                        smartTimerCurrentMax = TimeUnit.MINUTES.toMillis((TimelineList.get(0).getId()));
                    }else {
                        timerPaused = false;
                    }
                    intTimer();
                    startTimer = false;
                }
                handler.postDelayed(this,500);
            }
        };
        handler.post(run);
    }

    public void updateNotification() {
        int notiColour = getApplicationContext().getResources().getColor(R.color.colorPrimary);
        Intent notificationIntent = new Intent(getApplicationContext(), SmartTimer.class);
        notificationIntent.setAction("Start Foreground");
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                notificationIntent, 0);

        Intent pauseIntent = new Intent(this, SmartTimer_Service.class);
        pauseIntent.setAction("pause");
        PendingIntent ppauseIntent = PendingIntent.getService(this, 0,
                pauseIntent, 0);

        Intent playIntent = new Intent(this, SmartTimer_Service.class);
        playIntent.setAction("play");
        PendingIntent pplayIntent = PendingIntent.getService(this, 0,
                playIntent, 0);

        Intent nextIntent = new Intent(this, SmartTimer_Service.class);
        nextIntent.setAction("next");
        PendingIntent pnextIntent = PendingIntent.getService(this, 0,
                nextIntent, 0);

        String notTitle = "Current Event";
        String littleText = "";
        String bigText = "";
        if (nextEventindex + 1 != TimelineList.size()) {
            if (TimelineList.get(nextEventindex).getName() != "") {
                notTitle = TimelineList.get(nextEventindex).getName();
            }
            littleText = "Finishes in " + timerText;

            bigText = "Finishes in " + timerText + "\n\n" + TimelineList.get(nextEventindex + 1).getName() + " is up next!";
            if (TimelineList.get(nextEventindex + 1).getName() == "") {
                bigText = "Finishes in " + timerText;
            }
        } else {
            if (TimelineList.get(nextEventindex).getName() != "") {
                notTitle = TimelineList.get(nextEventindex).getName();
            }
            littleText = "Finishes in " + timerText;
            bigText = "Finishes in " + timerText;
        }

        if (timerActive) {
            if (nextEventindex + 1 == TimelineList.size()) {
                if (timerPaused) {
                    Notification notification = new NotificationCompat.Builder(this)
                            .setContentTitle(notTitle)
                                    //.setTicker("Truiton Music Player")
                            .setContentText(littleText)
                            .setSmallIcon(R.drawable.cookingicon512px)
                            .setContentIntent(pendingIntent)
                            .setOngoing(true)
                            .setColor(notiColour)
                            .addAction(android.R.drawable.ic_media_play, "Resume",
                                    pplayIntent)
                            .addAction(android.R.drawable.ic_media_next, "Skip",
                                    pnextIntent)
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(bigText))
                            .build();
                    startForeground(1,
                            notification);
                } else {
                    Notification notification = new NotificationCompat.Builder(this)
                            .setContentTitle(notTitle)
                                    //.setTicker("Truiton Music Player")
                            .setContentText(littleText)
                            .setSmallIcon(R.drawable.cookingicon512px)
                            .setContentIntent(pendingIntent)
                            .setOngoing(true)
                            .setColor(notiColour)
                            .addAction(android.R.drawable.ic_media_pause,
                                    "Pause", ppauseIntent)
                            .addAction(android.R.drawable.ic_media_next, "Skip",
                                    pnextIntent)
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(bigText))
                            .build();
                    startForeground(1,
                            notification);
                }
            } else {
                if (timerPaused) {
                    Notification notification = new NotificationCompat.Builder(this)
                            .setContentTitle(notTitle)
                                    //.setTicker("Truiton Music Player")
                            .setContentText(littleText)
                            .setSmallIcon(R.drawable.cookingicon512px)
                            .setContentIntent(pendingIntent)
                            .setOngoing(true)
                            .setColor(notiColour)
                            .addAction(android.R.drawable.ic_media_play, "Resume",
                                    pplayIntent)
                            .addAction(android.R.drawable.ic_media_next, "Skip",
                                    pnextIntent)
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(bigText))
                            .build();
                    startForeground(1,
                            notification);
                } else {
                    Notification notification = new NotificationCompat.Builder(this)
                            .setContentTitle(notTitle)
                                    //.setTicker("Truiton Music Player")
                            .setContentText(littleText)
                            .setSmallIcon(R.drawable.cookingicon512px)
                            .setContentIntent(pendingIntent)
                            .setOngoing(true)
                            .setColor(notiColour)
                            .addAction(android.R.drawable.ic_media_pause,
                                    "Pause", ppauseIntent)
                            .addAction(android.R.drawable.ic_media_next, "Skip",
                                    pnextIntent)
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(bigText))
                            .build();
                    startForeground(1,
                            notification);
                }

            }
        } else {
            Notification notification = new NotificationCompat.Builder(this)
                    .setContentTitle("BBQ Buddy")
                            //.setTicker("Truiton Music Player")
                    .setContentText("Smart Timer is Idle...")
                    .setSmallIcon(R.drawable.cookingicon512px)
                    .setContentIntent(pendingIntent)
                    .setOngoing(true)
                    .setColor(notiColour)
                    .build();
            startForeground(1,
                    notification);
        }
    }

    public void intTimer() {
        Log.i(TAG, "Starting timer...");
        timerComplete =false;
        new CountDownTimer(smartTimerCurrentMax, 1000) {
            public void onTick(long millisecondsUntilDone) {

                //saving current state
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.jalee.bbqbuddy", MODE_PRIVATE);
                sharedPreferences.edit().putLong("curMilli", millisecondsUntilDone);
                sharedPreferences.edit().putInt("curIndex", nextEventindex);

                int hours = (int) TimeUnit.MILLISECONDS.toHours(millisecondsUntilDone);
                int minutes = (int) (TimeUnit.MILLISECONDS.toMinutes(millisecondsUntilDone) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisecondsUntilDone)));
                int seconds = (int) (TimeUnit.MILLISECONDS.toSeconds(millisecondsUntilDone) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisecondsUntilDone)));

                timerEventsRem = minutes;
                for (int i = 0; i < TimelineList.size(); i++) {
                    if (i > nextEventindex) {
                        timerEventsRem = timerEventsRem + (Integer) TimelineList.get(i).id;
                    }
                }

                //obtain remaining minutes
                minsRemaining = (((TimelineList.get(nextEventindex).getId()) + minRemainingElapsed) - (TimeUnit.MILLISECONDS.toMinutes(smartTimerMax) - TimeUnit.MILLISECONDS.toMinutes(millisecondsUntilDone)));


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
                updateNotification();

                if (timerSkip) {
                    timerSkip = false;
                    cancel();
                    curTimerEnd();
                }

                if (timerPaused) {
                    updateNotification();
                    timerActive = false;
                    smartTimerCurrentMax = smartTimerCurrentMax - (smartTimerCurrentMax - millisecondsUntilDone);
                    cancel();

                }
                if (timerCancel) {
                    timerActive = false;
                    timerPaused = false;
                    timerCancel = false;
                    timerEventsRem = 0;
                    nextEventindex = 0;
                    minsRemaining = 0L;
                    minRemainingElapsed = 0L;
                    updateNotification();
                    secondsString = "0";
                    minutesString = "0";
                    timerText = "0";

                    cancel();
                    updateNotification();
                }
            }


            public void onFinish() {

                curTimerEnd();
            }
        }.start();
        timerActive = true;
    }

    public void curTimerEnd() {
        if (nextEventindex + 1 < TimelineList.size()) {
            minRemainingElapsed = minRemainingElapsed + TimelineList.get(nextEventindex).getId();
            nextEventindex = nextEventindex + 1;
            Log.i(TAG, "Next Interval occured");
            Log.i(TAG, "Increase Next event index");
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
            smartTimerCurrentMax = TimeUnit.MINUTES.toMillis((TimelineList.get(nextEventindex).getId()));
            Log.i(TAG, "Launch timerStarter");

            Log.i(TAG, "timeStarter Launched");

            intTimer();
        } else {
            Log.i(TAG, "Timer completed all intervals, resetting");
            //On Counter finished
            timerActive = false;
            timerPaused = false;
            timerCancel = false;
            nextEventindex = 0;
            minsRemaining = 0L;
            minRemainingElapsed = 0L;
            timerComplete = true;
            timerEventsRem = 0;

            updateNotification();
            Intent intent = new Intent(getApplicationContext(), SmartTimer.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 2, intent, 0);
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
            notificationManager.notify(2, timerNotification);

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
                    vibrator.vibrate(pattern, -1);
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "Timer cancelled");
        super.onDestroy();
    }


    public void saveTimeline(Context cntxt) {
        SharedPreferences sharedPreferences = cntxt.getSharedPreferences("com.jalee.bbqbuddy", MODE_PRIVATE);

        sharedPreferences.edit().putInt("timeLineListSize",SmartTimer_Service.TimelineList.size()).apply();
        Integer newSmartTimerValue = 0;
        for (int i = 0; i < SmartTimer_Service.TimelineList.size(); i++) {
            sharedPreferences.edit().putInt("ID"+i,SmartTimer_Service.TimelineList.get(i).getId()).apply();
            sharedPreferences.edit().putString("title" + i, SmartTimer_Service.TimelineList.get(i).getName()).apply();
            sharedPreferences.edit().putString("desc" + i, SmartTimer_Service.TimelineList.get(i).getsubTitle()).apply();
        }
    }

    public void loadTimeLine(Context cntxt) {
        SharedPreferences sharedPreferences = cntxt.getSharedPreferences("com.jalee.bbqbuddy", MODE_PRIVATE);

        int timeLineSize = sharedPreferences.getInt("timeLineListSize", 0);

        //initialise timeline data
        TimelineList = new ArrayList<>();
        if (timeLineSize == 0) {
            //Populate Sample data
            TimelineList.add(new SmartTimer_cardUI("Have a drink, preferably a James Squire 150 lashes","Drink Beer",2));
            TimelineList.add(new SmartTimer_cardUI("Cook on BBQ on Medium heat for 7 Minutes","Cook Steak - Side 1",7));
            TimelineList.add(new SmartTimer_cardUI("Cook on BBQ on Medium heat for 7 Minutes","Cook Steak - Side 2",7));
            TimelineList.add(new SmartTimer_cardUI("Have a drink, preferably a James Squire 150 lashes","Drink Beer",2));
            TimelineList.add(new SmartTimer_cardUI("Let your meat rest, its tired","Rest Meat",5));
            TimelineList.add(new SmartTimer_cardUI("Have a drink, preferably a James Squire 150 lashes", "Drink Beer", 10));
        } else {
            for (int i = 0; i < timeLineSize; i++) {
                TimelineList.add(new SmartTimer_cardUI(sharedPreferences.getString("desc" + i,""), sharedPreferences.getString("title" + i,""), sharedPreferences.getInt("ID" + i,0)));
            }
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}
