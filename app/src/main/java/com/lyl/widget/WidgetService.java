package com.lyl.widget;

import android.app.ActivityManager;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class WidgetService extends Service {

    // 更新 widget 的广播对应的 action
    private final String ACTION_UPDATE_ALL = "com.lyl.widget.UPDATE_ALL";
    // 周期性更新 widget 的周期
    private static final int UPDATE_TIME = 1000;
    private Timer mTimer;
    private TimerTask mTimerTask;
    private static final String TAG = "WidgetService";

    @Override
    public void onCreate() {
        // 每经过指定时间，发送一次广播
        Log.d(TAG, "WidgetService 的 onCreate 执行");
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                Intent updateIntent = new Intent(ACTION_UPDATE_ALL);
                sendBroadcast(updateIntent);
            }
        };
        mTimer.schedule(mTimerTask, 1000, UPDATE_TIME);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "WidgetService 的 onDestroy 执行");
        mTimerTask.cancel();
        mTimer.cancel();
    }

    /**
     * 服务开始时，即调用startService()时，onStartCommand()被执行。
     * <p>
     * 这个整形可以有四个返回值：start_sticky、start_no_sticky、START_REDELIVER_INTENT、START_STICKY_COMPATIBILITY。
     * 它们的含义分别是：
     * 1):START_STICKY：如果service进程被kill掉，保留service的状态为开始状态，但不保留递送的intent对象。随后系统会尝试重新创建service，
     * 由于服务状态为开始状态，所以创建服务后一定会调用onStartCommand(Intent,int,int)方法。如果在此期间没有任何启动命令被传递到service，那么参数Intent将为null;
     * 2):START_NOT_STICKY：“非粘性的”。使用这个返回值时，如果在执行完onStartCommand后，服务被异常kill掉，系统不会自动重启该服务;
     * 3):START_REDELIVER_INTENT：重传Intent。使用这个返回值时，如果在执行完onStartCommand后，服务被异常kill掉，系统会自动重启该服务，并将Intent的值传入;
     * 4):START_STICKY_COMPATIBILITY：START_STICKY的兼容版本，但不保证服务被kill后一定能重启。
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }
}
