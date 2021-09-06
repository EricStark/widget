package com.lyl.widget;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.lyl.widget.service.WidgetService;
import com.lyl.widget.widget.Const;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_auto = null;
    private Button btn_handle = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_auto = (Button) findViewById(R.id.auto);
        btn_handle = (Button) findViewById(R.id.handle);
        btn_handle.setOnClickListener(this);
        btn_auto.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Const.HANDLE.equals(Const.SETTING)) {
            stopService(new Intent(this, WidgetService.class));
            Log.d("main", "app进程退出，停止更新服务");
        } else {
            Log.d("main", "app进程退出");
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.auto) {
            Const.SETTING = Const.AUTO;
            Log.d("mainactivity", "设置自动成功");
        }
        if (v.getId() == R.id.handle) {
            Const.SETTING = Const.HANDLE;
            Log.d("mainactivity", "设置手动成功");
        }
        Toast.makeText(this, "设置成功", Toast.LENGTH_SHORT).show();
    }
}
