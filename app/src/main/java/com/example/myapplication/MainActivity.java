package com.example.myapplication;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.core.content.ContextCompat;


import androidx.activity.EdgeToEdge; // 导入 EdgeToEdge 类
import androidx.appcompat.app.AppCompatActivity; // 导入 AppCompatActivity 类
import androidx.core.app.ActivityCompat; // 导入 ActivityCompat 类
import androidx.core.graphics.Insets; // 导入 Insets 类
import androidx.core.view.ViewCompat; // 导入 ViewCompat 类
import androidx.core.view.WindowInsetsCompat; // 导入 WindowInsetsCompat 类

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private Button toggleButton;
    private CameraManager cameraManager;
    private String cameraId;
    private boolean isFlashOn = false; // 手电筒状态

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // 启用 Edge-to-Edge 模式
        setContentView(R.layout.activity_main); // 设置布局
// 在 onCreate 方法中或其他适当的位置，请求权限
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 1);
        }
        textView = findViewById(R.id.textView);
        toggleButton = findViewById(R.id.button);
        textView.setText("手电筒当前状态：开");
        toggleButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.blue));
        // 初始化 CameraManager 和获取 Camera ID
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            cameraId = cameraManager.getCameraIdList()[0]; // 获取默认的摄像头 ID
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        turnFlashlightOn();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars()); // 获取系统栏的 Insets
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom); // 设置视图的内边距
            return insets; // 返回处理后的 Insets
        });

        toggleButton.setOnClickListener(v -> {
            if (isFlashOn) {
                // 关闭手电筒
                turnFlashlightOff();
                textView.setText("手电筒当前状态：关");
                toggleButton.setText("打开手电筒");
                toggleButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.gray));
            } else {
                // 打开手电筒
                turnFlashlightOn();
                textView.setText("手电筒当前状态：开");
                toggleButton.setText("关闭手电筒");
                toggleButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.blue));
            }
        });
    }

    @Override
    public void onBackPressed() {
        // 关闭手电筒
        super.onBackPressed();
        if (isFlashOn) {
            turnFlashlightOff();
            textView.setText("手电筒当前状态：关");
            toggleButton.setText("打开手电筒");
        }
        // 关闭应用
        finishAffinity(); // 关闭应用并移除所有活动
    }

    private void turnFlashlightOn() {
        if (cameraId == null) return;
        try {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return; // 权限检查
            }
            cameraManager.setTorchMode(cameraId, true);
            isFlashOn = true;
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void turnFlashlightOff() {
        if (cameraId == null) return;
        try {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return; // 权限检查
            }
            cameraManager.setTorchMode(cameraId, false);
            isFlashOn = false;
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
}
