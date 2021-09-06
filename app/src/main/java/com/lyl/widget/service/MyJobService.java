package com.lyl.widget.service;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.lyl.widget.widget.WidgetProvider;

import java.util.Timer;
import java.util.TimerTask;

/**
 * JobScheduler拉活进程会在手机通知栏有显示
 */
public class MyJobService extends JobService {

    private static final String TAG = "MyJobService";
    private final String ACTION_UPDATE_ALL = "com.lyl.widget.UPDATE_ALL";
    // 周期性更新 widget 的周期
    private static final int UPDATE_TIME = 1000;
    private Timer mTimer;
    private TimerTask mTimerTask;
    private static JobScheduler jobScheduler;

    public static void startJob(Context context) {
        jobScheduler = (JobScheduler) context.getSystemService(Context
                .JOB_SCHEDULER_SERVICE);
        // setPersisted 在设备重启依然执行
        JobInfo.Builder builder = new JobInfo.Builder(1, new ComponentName(context
                .getPackageName(), MyJobService.class
                .getName())).setPersisted(true);
        //小于7.0
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            // 每隔1s 执行一次 job
            builder.setPeriodic(1000);
        } else {
            //延迟执行任务
            builder.setMinimumLatency(1000);
        }

        if (jobScheduler != null) {
            jobScheduler.schedule(builder.build());
        }

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "JobService==onCreate");
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                Intent updateIntent = new Intent(ACTION_UPDATE_ALL);
                sendBroadcast(updateIntent);
            }
        };
        mTimer.schedule(mTimerTask, 1000, UPDATE_TIME);
        WidgetProvider.witchService = 1;
        startJob(getApplicationContext());
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "开启job");
        //如果7.0以上 轮训
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            startJob(this);
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "关闭job");
        return false;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "JobService==onDestroy");
        mTimerTask.cancel();
        mTimer.cancel();
        jobScheduler.cancel(1);
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "JobService==onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }
}
