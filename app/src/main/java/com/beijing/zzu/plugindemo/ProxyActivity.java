package com.beijing.zzu.plugindemo;

import android.content.Intent;
import android.content.res.Resources;
import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.beijing.zzu.plugin.PluginInterface;

public class ProxyActivity extends FragmentActivity {

    private  String className;
    private PluginInterface pluginInterface;
    private boolean usePluginPkg = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        className = getIntent().getStringExtra("className");

        try {
            Class<?> aClass = PluginManager.getInstance().getPluginClassLoader().loadClass(className);
            Object newInstance = aClass.newInstance();

            if (newInstance instanceof PluginInterface){
                Log.d("tag","====="+newInstance);
                pluginInterface = (PluginInterface) newInstance;
                pluginInterface.attachContext(this);

                Bundle bundle = new Bundle();
                pluginInterface.onCreate(bundle);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startActivity(Intent intent) {
        usePluginPkg = false;

        Intent newIntent = new Intent(this, ProxyActivity.class);
        newIntent.putExtra("className", intent.getComponent().getClassName());
        super.startActivity(newIntent);

        usePluginPkg = true;
    }

    /**
     * 很关键
     * @return
     */
    @Override
    public Resources getResources() {
        return PluginManager.getInstance().getPluginResource();
    }

    @Override
    public String getPackageName() {
        if (usePluginPkg) {
            return PluginManager.getInstance().getPluginPackageArchiveInfo().packageName;
        } else {
            return super.getPackageName();
        }
    }

    @Override
    public void onStart() {
        Log.d("tag","===pluginInterface=="+pluginInterface);
        pluginInterface.onStart();
        super.onStart();
    }

    @Override
    public void onResume() {
        pluginInterface.onResume();
        super.onResume();
    }

    @Override
    public void onRestart() {
        pluginInterface.onRestart();
        super.onRestart();
    }

    @Override
    public void onDestroy() {
        pluginInterface.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onStop() {
        pluginInterface.onStop();
        super.onStop();
    }

    @Override
    public void onPause() {
        pluginInterface.onPause();
        super.onPause();
    }

}
