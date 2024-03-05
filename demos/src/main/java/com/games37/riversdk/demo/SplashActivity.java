package com.games37.riversdk.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.games37.riversdk.common.utils.DisPlayUtil;
import com.games37.riversdk.common.utils.ResourceUtils;

/**
 * @author: chenyunan
 * @date 2024/3/5
 * email: chenyunan@37.com
 * description:
 */
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisPlayUtil.hideNavigationBar(getWindow());
        setContentView(ResourceUtils.getLayoutId(this, "river_activity_splash"));
        DisPlayUtil.adaptDisplayCutoutMode(this);
        Log.i("SplashActivity", "onCreate: ");
        new Handler(Looper.myLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, RiverSDKDemoActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1200);
    }
}

