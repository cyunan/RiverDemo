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

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.games37.riversdk.common.utils.ResourceUtils;
import com.games37.riversdk.core.RiverSDKApi;
import com.games37.riversdk.core.callback.SDKCallback;
import com.games37.riversdk.core.callback.ShowViewCallback;
import com.games37.riversdk.core.constant.CallbackKey;
import com.games37.riversdk.core.constant.ReportServerParamsKey;
import com.games37.riversdk.core.constant.StatusCode;
import com.games37.riversdk.core.model.SDKPlatform;
import com.games37.riversdk.demo.RiverSDKDemoActivity;
import com.games37.riversdk.demo.config.SDKParams;

import java.util.HashMap;
import java.util.Map;

/**
 * @name:dev_demo_ui
 * @package:com.games37.riversdk.demo
 * @author:kechuanghao
 * @created:3:15 PM
 * @description:进入游戏的场景
 */
public class EnterGameScene extends Fragment implements View.OnClickListener {


    public static final String TAG = "LoginScene";

    private Button btnEnterGame;
    private Button btnConnectService;
    private Button btnSwitch;
    private TextView tvAccount;

    private Activity activity;
    private RiverSDKApi riverSDKApi;
    private Navigation navigation;
    private String accountInfo;

    private SDKParams mSDKParams;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        riverSDKApi = RiverSDKApi.getInstance(SDKPlatform.GLOBAL);
        String userId = getArguments().getString(RiverSDKDemoActivity.ARG_USER_ID);
        String loginType = getArguments().getString(RiverSDKDemoActivity.ARG_LOGIN_TYPE);
        accountInfo = loginType + " " + userId;

        //以下两行代码为demo自定义参数需要，游戏集成sdk不需要这两行代码
        mSDKParams = SDKParams.getInstance();

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
        btnEnterGame = view.findViewById(ResourceUtils.getResourceId(activity, "btn_login"));
        btnConnectService = view.findViewById(ResourceUtils.getResourceId(activity, "btn_connect_service"));
        btnSwitch = view.findViewById(ResourceUtils.getResourceId(activity, "btn_switch_account"));
        tvAccount = view.findViewById(ResourceUtils.getResourceId(activity, "tv_account"));

        btnEnterGame.setOnClickListener(this);
        btnConnectService.setOnClickListener(this);
        btnSwitch.setOnClickListener(this);

        btnEnterGame.setText("进入游戏");
        tvAccount.setText(accountInfo);
        tvAccount.setVisibility(View.VISIBLE);
        btnSwitch.setVisibility(View.VISIBLE);
        btnConnectService.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(btnEnterGame)) {
            //登录完成，进入游戏
            reportServerCode();
            String userId = getArguments().getString(RiverSDKDemoActivity.ARG_USER_ID);
            String loginType = getArguments().getString(RiverSDKDemoActivity.ARG_LOGIN_TYPE);
            Map<String, String> loginparams = new HashMap<>();
            loginparams.put(RiverSDKDemoActivity.ARG_USER_ID, userId);
            loginparams.put(RiverSDKDemoActivity.ARG_LOGIN_TYPE, loginType);
            navigation.gotoGameScene(loginparams);

        } else if (v.equals(btnConnectService)) {
            //联系客服
            connectService();
        } else if (v.equals(btnSwitch)) {
            switchAccount();
        }
    }

    /**
     * 账密体系切换账号可以参考以下方法
     */
    private void switchAccount() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //首先判断是否登录 状态,已经登录需要登出处理
                if (riverSDKApi.sqSDKhasLogin()) {
                    riverSDKApi.sqSDKLogout(activity,
                            new SDKCallback() {

                                @Override
                                public void onResult(int code, Map<String, String> params) {
                                    //登出回调完成之后,就拉出登录界面
                                    if (code == StatusCode.SUCCESS) {
                                        startSDKLoginView();
                                    }
                                }
                            });
                    //如果没有登录,就拉出登录界面
                } else {
                    startSDKLoginView();
                }
            }
        });
    }

    /**
     * 显示登录界面(该方法只针对港台澳、东南亚账密登录体系，欧美默认游客登录账号体系没有登录界面)
     */
    private void startSDKLoginView() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                riverSDKApi.sqSDKPresentLoginView(
                        activity, new SDKCallback() {
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
                                }
                            }
                        });
            }
        });
    }

    private void reportServerCode() {
        //玩家入服id（必传）
        String serverId = "999";
        //玩家角色id（必传）
        String roleId = "999000026";
        //玩家角色名（必传）
        String roleName = "愛爾·鮑爾";
        //玩家角色等级（必传）
        String roleLevel = "260";
        //玩家角色VIP等级（非必需）
        String vipLevel = "8";
        //玩家拥有钻石、元宝数量（非必需）
        String diamonds = "10000";
        //城堡等级 SLG需要传递
        String castleLevel = "6";


        if (mSDKParams != null && mSDKParams.isApplyParams()) {
            serverId = mSDKParams.getServerId();
            roleId = mSDKParams.getRoleId();
            roleName = mSDKParams.getRoleName();
            roleLevel = mSDKParams.getRoleLevel();
        }

        Bundle reportServerParams = new Bundle();
        //玩家入服id（必传）
        reportServerParams.putString(ReportServerParamsKey.SERVER_ID, serverId);
        //玩家角色id（必传）
        reportServerParams.putString(ReportServerParamsKey.ROLE_ID, roleId);
        //玩家角色名（必传）
        reportServerParams.putString(ReportServerParamsKey.ROLE_NAME, roleName);
        //玩家角色等级（必传）
        reportServerParams.putString(ReportServerParamsKey.ROLE_LEVEL, roleLevel);
        //玩家角色VIP等级（非必需）
        reportServerParams.putString(ReportServerParamsKey.VIP_LEVEL, vipLevel);
        //玩家拥有钻石、元宝数量（非必需）
        reportServerParams.putString(ReportServerParamsKey.DIAMONDS, diamonds);
        //玩家城堡等级（SLG必传）
        reportServerParams.putString(ReportServerParamsKey.CASTLE_LEVEL, castleLevel);

        riverSDKApi.sqSDKReportServerCode(activity, reportServerParams);
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
