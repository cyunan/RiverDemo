package com.games37.riversdk.demo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.games37.riversdk.common.utils.DisPlayUtil
import com.games37.riversdk.common.utils.ResourceUtils

/**
 * @author: chenyunan
 * @date 2024/3/5
 * email: chenyunan@37.com
 * description:
 */
class SplashActivity : Activity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DisPlayUtil.hideNavigationBar(window)
        setContentView(ResourceUtils.getLayoutId(this, "river_activity_splash"))
        DisPlayUtil.adaptDisplayCutoutMode(this)
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@SplashActivity, RiverSDKDemoActivity::class.java)
            startActivity(intent)
            finish()
        }, 1200)
    }
}