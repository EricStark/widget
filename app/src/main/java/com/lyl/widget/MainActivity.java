package com.lyl.widget;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.lyl.widget.service.WidgetService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_auto = null;
    private Button btn_handle = null;
    //默认是手动启动服务
    private String setting = AUTO;
    private static String AUTO = "auto";
    private static String HANDLE = "handle";

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
        //如果设置为handle 则在activity销毁时杀死服务
        if (HANDLE.equals(setting)) {
            stopService(new Intent(this, WidgetService.class));
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.auto) {
            setting = AUTO;
        }
        if (v.getId() == R.id.handle) {
            setting = HANDLE;
        }
        Toast.makeText(this, "设置成功", Toast.LENGTH_SHORT).show();
    }
}
