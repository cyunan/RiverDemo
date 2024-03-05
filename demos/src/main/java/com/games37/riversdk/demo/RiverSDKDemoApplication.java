package com.games37.riversdk.demo;

import android.content.Context;

import androidx.multidex.MultiDex;

import com.games37.riversdk.core.RiverSDKApplication;

/**
 * @name:RiverSDK_Android
 * @package:com.games37.riversdk.demo
 * @class: RiverSDKDemoApplication.java
 * @author:kechuanghao
 * @created:2019/5/22 6:33 PM
 * @email:cooke@gm99.com
 * @description:
 */
public class RiverSDKDemoApplication extends RiverSDKApplication {

    @Override
    protected void attachBaseContext(Context base) {
        MultiDex.install(base);
        super.attachBaseContext(base);
    }
}
