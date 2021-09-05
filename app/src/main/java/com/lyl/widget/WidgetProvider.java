package com.lyl.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class WidgetProvider extends AppWidgetProvider {

    private static final String ACTION_UPDATE_ALL = "com.lyl.widget.UPDATE_ALL";
    private static final Set<Integer> idsSet = new HashSet<>();
    private SharedPreferences preferences = null;
    private static final String TAG = "WidgetProvider";
    private static int mIndex = 0;

    /**
     * 接收窗口小部件点击时发送的广播
     */
    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.d(TAG, "WidgetProvider 的 onReceive 执行");
        super.onReceive(context, intent);
        final String action = intent.getAction();

        if (ACTION_UPDATE_ALL.equals(action)) {
            updateAllAppWidgets(context, AppWidgetManager.getInstance(context), idsSet);
        } else if (intent.hasCategory(Intent.CATEGORY_ALTERNATIVE)) {
            mIndex = 0;
            updateAllAppWidgets(context, AppWidgetManager.getInstance(context), idsSet);
        }
    }

    // 更新所有的 widget
    private void updateAllAppWidgets(Context context, AppWidgetManager appWidgetManager, Set<Integer> set) {
        Log.d(TAG, "WidgetProvider 的 updateAllAppWidgets 执行");
        // widget 的id
        int appID = -1;
        // 迭代器，用于遍历所有保存的widget的id
        Iterator it = set.iterator();

        // 要显示的那个数字，每更新一次 + 1
        mIndex++;

        if (!set.isEmpty()) {
            appID = (Integer) it.next();
        } else if (context.getSharedPreferences("widget", Context.MODE_PRIVATE).getInt("first", -1) != -1) {
            appID = context.getSharedPreferences("widget", Context.MODE_PRIVATE).getInt("first", -1);
        }
        if (appID != -1) {
            // 获取 example_appwidget.xml 对应的RemoteViews
            RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.app_widget);

            // 设置显示数字
            remoteView.setTextViewText(R.id.widget_txt, String.valueOf(mIndex));

            // 设置点击按钮对应的PendingIntent：即点击按钮时，发送广播。
            remoteView.setOnClickPendingIntent(R.id.widget_btn_reset, getResetPendingIntent(context));
            remoteView.setOnClickPendingIntent(R.id.widget_btn_open, getOpenPendingIntent(context));

            // 更新 widget
            appWidgetManager.updateAppWidget(appID, remoteView);
            Log.d(TAG, "桌面小部件更新完成");
        }
    }

    /**
     * 获取 重置数字的广播
     */
    private PendingIntent getResetPendingIntent(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, WidgetProvider.class);
        intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        return pendingIntent;
    }

    /**
     * 获取 打开 service 的 PendingIntent
     */
    private PendingIntent getOpenPendingIntent(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, WidgetService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
        return pendingIntent;
    }

    /**
     * 当该窗口小部件第一次添加到桌面时调用该方法，可添加多次但只第一次调用
     */
    @Override
    public void onEnabled(Context context) {
        Log.d(TAG, "WidgetProvider 的 onEnabled 执行");
        // 在第一个 widget 被创建时，开启服务
        Intent intent = new Intent(context, WidgetService.class);
        context.startService(intent);
        Toast.makeText(context, "开始计数", Toast.LENGTH_SHORT).show();
    }

    /**
     * 每次窗口小部件被点击更新都调用一次该方法
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // 每次 widget 被创建时，对应的将widget的id添加到set中
        preferences = context.getSharedPreferences("widget", Context.MODE_PRIVATE);
        Log.d(TAG, "WidgetProvider 的 onUpdate 执行");
        for (int appWidgetId : appWidgetIds) {
            idsSet.add(appWidgetId);
            if (preferences != null) {
                preferences.edit().putInt("first", appWidgetId).apply();
            }
        }
    }

    /**
     * 每删除一次窗口小部件就调用一次
     */
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // 当 widget 被删除时，对应的删除set中保存的widget的id
        Log.d(TAG, "WidgetProvider 的 onDeleted 执行");
        preferences = context.getSharedPreferences("widget", Context.MODE_PRIVATE);
        for (int appWidgetId : appWidgetIds) {
            idsSet.remove(appWidgetId);
            if (preferences != null) {
                preferences.edit().remove("first").apply();
            }
        }
    }

    /**
     * 当最后一个该窗口小部件删除时调用该方法，注意是最后一个
     */
    @Override
    public void onDisabled(Context context) {
        Log.d(TAG, "WidgetProvider 的 onDisabled 执行");
        // 在最后一个 widget 被删除时，终止服务
        Intent intent = new Intent(context, WidgetService.class);
        context.stopService(intent);
        Toast.makeText(context, "服务停止", Toast.LENGTH_SHORT).show();
    }
}
