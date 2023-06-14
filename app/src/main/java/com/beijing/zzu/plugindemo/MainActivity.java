package com.beijing.zzu.plugindemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.os.Environment;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void loadPlugin(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        } else {
            _loadPlugin();
            startPlugin(null);
        }
    }

    /***
     编译pluginapk工程生成pluginapk-debug.apk之后，执行adb push命令将.apk推到手机上
     adb push D:\Dev\GitHub\AndroidPluginDemo\pluginapk\build\outputs\apk\debug\pluginapk-debug.apk /sdcard/Download

     注意：在Android高版本上不允许直接访问共享/外部存储设备，如/sdcard，或/sdcard/Download
     https://blog.csdn.net/xiaowang_lj/article/details/128081642
     */
    private void _loadPlugin() {
        PluginManager.getInstance().setContext(this);
        PluginManager.getInstance().loadApk(getExternalFilesDir(null).getAbsolutePath() + "/pluginapk-debug.apk");
    }

    public void startPlugin(View view) {
        if (!PluginManager.getInstance().isApkLoaded()) return;

        Intent intent = new Intent(this, ProxyActivity.class);
        String otherapkName = PluginManager.getInstance().getPluginPackageArchiveInfo().activities[0].name;
        intent.putExtra("className", otherapkName);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                _loadPlugin();
                startPlugin(null);
            }
        }
    }
}
