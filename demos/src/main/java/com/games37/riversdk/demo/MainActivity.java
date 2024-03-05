package com.games37.riversdk.demo;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.games37.riversdk.common.utils.ResourceUtils;

/**
 * @author: chenyunan
 * @date 2024/3/5
 * email: chenyunan@37.com
 * description:
 */
public class MainActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ResourceUtils.getLayoutId(this, "river_activity_splash"));
    }
}
