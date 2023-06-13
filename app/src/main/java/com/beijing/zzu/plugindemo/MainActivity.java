package com.beijing.zzu.plugindemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void loadPlugin(View view){
          ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},100);
    }

    public void startPlugin(View view){
        if (!PluginManager.getInstance().isApkLoaded()) return;

        Intent intent = new Intent(this, ProxyActivity.class);
        String otherapkName = PluginManager.getInstance().getPluginPackageArchiveInfo().activities[0].name;
        intent.putExtra("className", otherapkName);
        startActivity(intent);
    }

    // 编译pluginapk工程生成pluginapk-debug.apk之后，执行adb push命令将.apk推到手机上
    //  adb push D:\Dev\GitHub\AndroidPluginDemo\pluginapk\build\outputs\apk\debug\pluginapk-debug.apk /sdcard
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                PluginManager.getInstance().setContext(this);
                PluginManager.getInstance().loadApk(Environment.getExternalStorageDirectory().getAbsolutePath() + "/pluginapk-debug.apk");

                startPlugin(null);
            }
        }
    }
}
