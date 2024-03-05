package com.games37.riversdk.demo;


import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.games37.riversdk.common.utils.DisPlayUtil;
import com.games37.riversdk.common.utils.FixOrientationUtil;
import com.games37.riversdk.common.utils.ResourceUtils;
import com.games37.riversdk.core.RiverSDKApi;
import com.games37.riversdk.core.callback.SDKCallback;
import com.games37.riversdk.core.constant.CallbackKey;
import com.games37.riversdk.core.constant.StatusCode;
import com.games37.riversdk.core.login.model.UserType;
import com.games37.riversdk.demo.config.SDKParams;

import java.util.HashMap;
import java.util.Map;
import com.games37.riversdk.common.log.LogHelper;
import com.games37.riversdk.demo.scene.EnterGameScene;
import com.games37.riversdk.demo.scene.LoginScene;
import com.games37.riversdk.demo.scene.MainScene;
import com.games37.riversdk.demo.scene.Navigation;
import com.games37.riversdk.demo.scene.SettingScene;

/**
 * @name:dev_demo_ui
 * @package:com.games37.riversdk.demo
 * @author:kechuanghao
 * @created:4:59 PM
 * @description:SDK Demo页面
 */
public class RiverSDKDemoActivity extends FragmentActivity implements Navigation {

    public static final String TAG = "RiverSDKDemoActivity";

    private RiverSDKApi riverSDKApi;

    private LoginScene loginScene;

    private EnterGameScene enterGameScene;

    private MainScene mainScene;

    private SettingScene settingScene;

    public static final String ARG_USER_ID = "ARG_USER_ID";
    public static final String ARG_LOGIN_TYPE = "ARG_LOGIN_TYPE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "----------onCreate-------------");
        //修复8.0系统屏幕方向bug
        FixOrientationUtil.FixOrientionBug(this);
        super.onCreate(savedInstanceState);
        DisPlayUtil.hideNavigationBar(getWindow());
        setContentView(ResourceUtils.getLayoutId(this, "river_demo_main_activity"));
        DisPlayUtil.adaptDisplayCutoutMode(this);

        riverSDKApi = RiverSDKApi.getInstance();
        // SDK初始化操作
        riverSDKApi.sqSDKInit(this, new SDKCallback() {

            @Override
            public void onResult(int statusCode, Map<String, String> params) {
                Log.d(TAG, "----------sqSDKInit-------------" +
                        //游戏包名
                        params.get(CallbackKey.PACKAGENAME) + "  :" +
                        //游戏id
                        params.get(CallbackKey.GID) + "  :" +
                        params.get(CallbackKey.PID) + " :" +
                        //平台简码
                        params.get(CallbackKey.PTCODE) + " :" +
                        //设备类型
                        params.get(CallbackKey.DEV));
                LogHelper.d("----------sqSDKInit-------------", params);
            }
        });
        // 调用SDK内部onCreate方法 广告SDK初始化使用
        riverSDKApi.onCreate(this);
        // 一定要设置切换账号回调，否则当用户在用户中心里面切换账号时会收不到回调，导致用户信息不统一！！
        riverSDKApi.sqSDKSetSwitchAccountCallback(new SDKCallback() {
            @Override
            public void onResult(int statusCode, Map<String, String> params) {
                //TODO 收到该回调时，无论成功与否需要重启游戏，执行游戏重登的逻辑
                LogHelper.d("----------sqSDKSetSwitchAccountCallback-------------", params);
                if (StatusCode.SUCCESS == statusCode) {
                    //切换账号成功，重启游戏！回调的参数信息跟登录成功的参数信息一致
                    String isLogout = params.get(CallbackKey.IS_LOGOUT);
                    if ("1".equals(isLogout)) {
                        gotoLoginScene();
                    } else {
                        loginSuccess(params);
                        Map<String, String> loginparams = new HashMap<>();
                        loginparams.put(RiverSDKDemoActivity.ARG_USER_ID, params.get(CallbackKey.USERID));
                        loginparams.put(RiverSDKDemoActivity.ARG_LOGIN_TYPE, params.get(CallbackKey.USERTYPE));
                        gotoEnterGameScene(loginparams);
                    }
                } else {
                    //获取账号信息失败，需要重启游戏
                    String msg = params.get(CallbackKey.MSG);
                    Toast.makeText(RiverSDKDemoActivity.this, msg, Toast.LENGTH_SHORT).show();
                    gotoLoginScene();
                }

            }
        });

        //以下为demo页面切换需要，请忽略
        loginScene = new LoginScene();
        enterGameScene = new EnterGameScene();
        mainScene = new MainScene();
        settingScene = new SettingScene();
        getSupportFragmentManager().beginTransaction()
                .add(ResourceUtils.getResourceId(this, "fragment_container"), loginScene)
                .commit();
        //以下代码为demo自定义参数需要，游戏集成sdk不需要
        SDKParams.updateFromDisk(this);

    }


    @Override
    protected void onRestart() {
        Log.d(TAG, "----------onRestart-------------");
        super.onRestart();
        riverSDKApi.onRestart(this);
    }

    /*********************************生命周期的方法必须接入************************************/


    @Override
    protected void onStart() {
        Log.d(TAG, "----------onStart-------------");
        super.onStart();
        riverSDKApi.onStart(this);
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "----------onResume-------------");
        super.onResume();
        riverSDKApi.onResume(this);
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "----------onPause-------------");
        super.onPause();
        riverSDKApi.onPause(this);
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "----------onStop-------------");
        super.onStop();
        riverSDKApi.onStop(this);
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "----------onDestroy-------------");
        super.onDestroy();
        riverSDKApi.onDestroy(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        riverSDKApi.onNewIntent(this, intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "----------onActivityResult-------------");
        super.onActivityResult(requestCode, resultCode, data);
        riverSDKApi.onActivityResult(this, requestCode, resultCode, data);
    }

    /*********************************生命周期的方法必须接入************************************/

    @Override
    public void onBackPressed() {
        riverSDKApi.sqSDKExitGame(this, new SDKCallback() {
            @Override
            public void onResult(int statusCode, Map<String, String> params) {

            }
        });
    }

    @Override
    public Resources getResources() {
        return ResourceUtils.forbidFontChange(this, super.getResources());
    }

    /**
     * 沉浸导航栏
     *
     * @param hasFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        DisPlayUtil.hideNavigationBar(this, hasFocus);
    }

    /**
     * 获取登录成功的数据
     *
     * @param params
     */
    public static void loginSuccess(Map<String, String> params) {
        //登录成功都返回以下7个值
        //登录方式
        UserType userType = UserType.toUserType(params.get(CallbackKey.USERTYPE));
        //用户id
        String userId = params.get(CallbackKey.USERID);
        String sign = params.get(CallbackKey.SIGN);
        //时间戳
        String timeStamp = params.get(CallbackKey.TIMESTAMP);
        //设备类型这里返回"android"
        String dev = params.get(CallbackKey.DEV);
        //游戏简码
        String gameCode = params.get(CallbackKey.GAMECODE);
        //发布渠道默认为"googlePlay"
        String channelId = params.get(CallbackKey.CHANNELID);

        //是否绑定google play 1代表绑定，0代表未绑定
        String is_gp_bind = params.get(CallbackKey.IS_GP_BIND);
        //是否绑定facebook 1代表绑定，0代表未绑定
        String is_fb_bind = params.get(CallbackKey.IS_FB_BIND);
        //是否绑定twitter 1代表绑定，0代表未绑定
        String is_tw_bind = params.get(CallbackKey.IS_TW_BIND);
        //是否绑定line 1代表绑定，0代表未绑定
        String is_line_bind = params.get(CallbackKey.IS_LINE_BIND);
        //是否绑定naver 1代表绑定，0代表未绑定
        String is_naver_bind = params.get(CallbackKey.IS_NAVER_BIND);
        //绑定的google play用户名
        String gp_name = params.get(CallbackKey.GP_NAME);
        //绑定的twitter用户名
        String tw_name = params.get(CallbackKey.TW_NAME);
        //绑定的facebook用户名
        String fb_name = params.get(CallbackKey.FB_NAME);
        //绑定的line用户名
        String line_name = params.get(CallbackKey.LINE_NAME);
        //绑定的naver用户名
        String naver_name = params.get(CallbackKey.NAVER_NAME);
        //remark拓展字段
        String remark = params.get(CallbackKey.REMARK);

        String str = "userType:" + userType + "    \n" + "userId:"
                + userId + "    \n" + "sign:" + sign + "    \n" + "timeStamp:"
                + timeStamp + "    \n" + "dev:" + dev + "    \n" + "gameCode:"
                + gameCode + "    \n" + "channelId:" + channelId
                + "    \n" + "remark:" + remark;
        Log.i(TAG, str);

    }

    @Override
    public void gotoGameScene(Map<String, String> params) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();   // 开启一个事务
        Bundle bundle = new Bundle();
        bundle.putString(ARG_USER_ID, params.get(ARG_USER_ID));
        bundle.putString(ARG_LOGIN_TYPE, params.get(ARG_LOGIN_TYPE));
        mainScene.setArguments(bundle);
        transaction.replace(ResourceUtils.getResourceId(this, "fragment_container"), mainScene);
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void gotoEnterGameScene(Map<String, String> params) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();   // 开启一个事务
        Bundle bundle = new Bundle();
        bundle.putString(ARG_USER_ID, params.get(ARG_USER_ID));
        bundle.putString(ARG_LOGIN_TYPE, params.get(ARG_LOGIN_TYPE));
        enterGameScene.setArguments(bundle);
        transaction.replace(ResourceUtils.getResourceId(this, "fragment_container"), enterGameScene);
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void gotoSettingScene() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();   // 开启一个事务
        transaction.replace(ResourceUtils.getResourceId(this, "fragment_container"), settingScene, "setting_scene")
                .commitAllowingStateLoss();
    }

    @Override
    public void gotoLoginScene() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();   // 开启一个事务
        transaction.replace(ResourceUtils.getResourceId(this, "fragment_container"), loginScene);
        transaction.commitAllowingStateLoss();
    }


    @Override
    public void removeSettingScene() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();   // 开启一个事务
        transaction.replace(ResourceUtils.getResourceId(this, "fragment_container"), mainScene, "main_scene")
                .commitAllowingStateLoss();

    }
}
