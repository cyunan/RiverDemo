package com.games37.riversdk.demo.scene;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.games37.riversdk.common.utils.ResourceUtils;
import com.games37.riversdk.core.RiverSDKApi;
import com.games37.riversdk.core.callback.SDKCallback;
import com.games37.riversdk.core.callback.ShowViewCallback;
import com.games37.riversdk.core.constant.CallbackKey;
import com.games37.riversdk.core.constant.StatusCode;
import com.games37.riversdk.core.model.SDKPlatform;
import com.games37.riversdk.demo.RiverSDKDemoActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * @name:dev_demo_ui
 * @package:com.games37.riversdk.demo
 * @author:kechuanghao
 * @created:7:44 PM
 * @description: 登录游戏的场景
 */
public class LoginScene extends Fragment implements View.OnClickListener {

    public static final String TAG = "LoginScene";

    private Button btnLogin;
    private Button btnConnectService;
    private TextView tvPackageName;
    private Activity activity;

    private RiverSDKApi riverSDKApi;
    private Navigation navigation;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        activity = getActivity();
        riverSDKApi = RiverSDKApi.getInstance(SDKPlatform.GLOBAL);
        View view = inflater.inflate(ResourceUtils.getLayoutId(getActivity(), "river_demo_login_layout"), container, false);
        initView(view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
        navigation = (Navigation) context;
    }

    private void initView(View view) {
        btnLogin = view.findViewById(ResourceUtils.getResourceId(activity, "btn_login"));
        btnConnectService = view.findViewById(ResourceUtils.getResourceId(activity, "btn_connect_service"));
        tvPackageName = view.findViewById(ResourceUtils.getResourceId(activity, "tv_package_name"));

        btnLogin.setOnClickListener(this);
        btnConnectService.setOnClickListener(this);
        tvPackageName.setText(activity.getPackageName());

    }

    @Override
    public void onClick(View v) {
        if (v.equals(btnLogin)) {
            autoLogin();
        }else if (v.equals(btnConnectService)) {
            //联系客服
            connectService();
        }
    }

    /**
     * 自动登录调用
     */
    private void autoLogin() {
        riverSDKApi.sqSDKAutoLogin(activity, new SDKCallback() {
            @Override
            public void onResult(int statusCode, Map<String, String> params) {
                if (StatusCode.SUCCESS == statusCode) {
                    RiverSDKDemoActivity.loginSuccess(params);
                    //登录成功，加载进入游戏场景
                    Map<String, String> loginparams = new HashMap<>();
                    loginparams.put(RiverSDKDemoActivity.ARG_USER_ID, params.get(CallbackKey.USERID));
                    loginparams.put(RiverSDKDemoActivity.ARG_LOGIN_TYPE, params.get(CallbackKey.USERTYPE));
                    navigation.gotoEnterGameScene(loginparams);
                } else {
                    //登录失败，msg获取登录失败信息
                    String msg = params.get(CallbackKey.MSG);
                    Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void connectService() {
        riverSDKApi.sqSDKPresentFAQView(
                activity, new ShowViewCallback() {

                    @Override
                    public void onViewShow() {
                        Log.i(TAG, "faq: " + "onViewShow");
                    }

                    @Override
                    public void onViewDismiss() {
                        Log.i(TAG, "faq: " + "onViewDismiss");
                    }
                });
    }


}
