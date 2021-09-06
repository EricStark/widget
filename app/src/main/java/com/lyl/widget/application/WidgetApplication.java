package com.lyl.widget.application;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.lyl.widget.widget.Const;
import com.lyl.widget.service.WidgetService;

public class WidgetApplication extends Application {

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
