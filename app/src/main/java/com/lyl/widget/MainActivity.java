package com.lyl.widget;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_auto = null;
    private Button btn_handle = null;
    private String setting = null;

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
        if ("handle".equals(setting)) {
            stopService(new Intent(this, WidgetService.class));
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.auto) {
            setting = "auto";
        }
        if (v.getId() == R.id.handle) {
            setting = "handle";
        }
        Toast.makeText(this, "设置成功", Toast.LENGTH_SHORT).show();
    }
}
