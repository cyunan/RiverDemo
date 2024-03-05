package com.games37.riversdk.demo.scene;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import com.games37.riversdk.common.log.LogHelper;
import com.games37.riversdk.common.utils.ResourceUtils;
import com.games37.riversdk.core.RiverSDKApi;
import com.games37.riversdk.core.callback.SDKCallback;
import com.games37.riversdk.core.callback.ShowViewCallback;
import com.games37.riversdk.core.constant.CallbackKey;
import com.games37.riversdk.core.constant.PurchaseParamsKey;
import com.games37.riversdk.core.constant.ReportServerParamsKey;
import com.games37.riversdk.core.constant.StatusCode;
import com.games37.riversdk.core.purchase.model.PurchaseType;
import com.games37.riversdk.core.share.SocialType;
import com.games37.riversdk.demo.RiverSDKDemoActivity;
import com.games37.riversdk.demo.config.SDKParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @name:dev_demo_ui
 * @package:com.games37.riversdk.demo
 * @author:kechuanghao
 * @created:3:36 PM
 * @description:游戏主界面
 */
public class MainScene extends Fragment implements View.OnClickListener {
    public static final String TAG = "MainScene";
    public static final int REQUEST_SHARE_IMG = 37001;
    public static final int REQUEST_SHARE_IMG_WITH_AWARD = 37002;

    private TextView tvAccount;
    private Button btnSetting;
    private Button btnAutoLogin;
    private Button btnAddServer;
    private Button btnBilling;
    private Button btnUserCenter;
    private Button btnTrackEvent;
    private Button btnSwitchAccount;
    private Button btnGetSkuDetail;
    private Button btnShare;
    private Button btnFaq;
    private Button btnOpenLink;


    private Activity activity;
    private RiverSDKApi riverSDKApi;
    private Navigation navigation;
    private String accountInfo;
    private SDKParams mSDKParams;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = getActivity();
        riverSDKApi = RiverSDKApi.getInstance();
        String userId = getArguments().getString(RiverSDKDemoActivity.ARG_USER_ID);
        String loginType = getArguments().getString(RiverSDKDemoActivity.ARG_LOGIN_TYPE);
        accountInfo = loginType + " " + userId;
        //以下两行代码为demo自定义参数需要，游戏集成sdk不需要这两行代码
        mSDKParams = SDKParams.getInstance();
        View view = inflater.inflate(ResourceUtils.getLayoutId(getActivity(), "river_demo_game_layout"), container, false);

        initView(view);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
        navigation = (Navigation) context;
    }

    @Override
    public void onResume() {
        super.onResume();
        mSDKParams = SDKParams.getInstance();
    }

    private void initView(View view) {
        btnSetting = view.findViewById(ResourceUtils.getResourceId(activity, "btn_setting"));
        tvAccount = view.findViewById(ResourceUtils.getResourceId(activity, "tv_account"));
        btnAutoLogin = view.findViewById(ResourceUtils.getResourceId(activity, "btn_auto_login"));
        btnAddServer = view.findViewById(ResourceUtils.getResourceId(activity, "btn_add_server"));
        btnBilling = view.findViewById(ResourceUtils.getResourceId(activity, "btn_billing"));
        btnUserCenter = view.findViewById(ResourceUtils.getResourceId(activity, "btn_user_center"));
        btnTrackEvent = view.findViewById(ResourceUtils.getResourceId(activity, "btn_track_event"));

        btnSwitchAccount = view.findViewById(ResourceUtils.getResourceId(activity, "btn_switch_aacount"));
        btnGetSkuDetail = view.findViewById(ResourceUtils.getResourceId(activity, "btn_get_sku_detail"));
        btnShare = view.findViewById(ResourceUtils.getResourceId(activity, "btn_share"));
        btnFaq = view.findViewById(ResourceUtils.getResourceId(activity, "btn_faq"));
        btnOpenLink = view.findViewById(ResourceUtils.getResourceId(activity, "btn_open_link"));

        btnSetting.setOnClickListener(this);
        btnAutoLogin.setOnClickListener(this);
        btnAddServer.setOnClickListener(this);
        btnBilling.setOnClickListener(this);
        btnUserCenter.setOnClickListener(this);
        btnTrackEvent.setOnClickListener(this);
        btnSwitchAccount.setOnClickListener(this);
        btnGetSkuDetail.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        btnFaq.setOnClickListener(this);
        btnOpenLink.setOnClickListener(this);

        tvAccount.setText(accountInfo);

        setBtnDrawable(btnAutoLogin);
        setBtnDrawable(btnAddServer);
        setBtnDrawable(btnBilling);
        setBtnDrawable(btnUserCenter);
        setBtnDrawable(btnTrackEvent);
        setBtnDrawable(btnSwitchAccount);
        setBtnDrawable(btnGetSkuDetail);
        setBtnDrawable(btnShare);
        setBtnDrawable(btnFaq);
        setBtnDrawable(btnOpenLink);
    }

    /**
     * 设置按钮的drawable选中效果
     *
     * @param btn
     */
    private void setBtnDrawable(Button btn) {
        ColorStateList colorStateList = getResources().getColorStateList(ResourceUtils.getColorId(activity, "btn_icon_tint_color"));
        if (btn.getCompoundDrawables() != null) {
            if (btn.getCompoundDrawables()[0] != null) {
                Drawable drawableLeft = tintDrawable(btn.getCompoundDrawables()[0], colorStateList);
                btn.setCompoundDrawables(drawableLeft,
                        null, null, null);
            } else if (btn.getCompoundDrawables()[1] != null) {
                Drawable drawableTop = tintDrawable(btn.getCompoundDrawables()[1], colorStateList);
                btn.setCompoundDrawables(null,
                        drawableTop, null, null);
            }
        }

    }

    public static Drawable tintDrawable(Drawable drawable, ColorStateList colors) {
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(wrappedDrawable, colors);
        return wrappedDrawable;
    }

    @Override
    public void onClick(View v) {
        if (v.equals(btnSetting)) {
            navigation.gotoSettingScene();
        } else if (v.equals(btnAutoLogin)) {
            autoLogin();
        } else if (v.equals(btnAddServer)) {
            reportServerCode();
        } else if (v.equals(btnBilling)) {
            billing();
        } else if (v.equals(btnUserCenter)) {
            openUserCenter();
        } else if (v.equals(btnTrackEvent)) {
            trackGameEvent();
        } else if (v.equals(btnSwitchAccount)) {
            switchAcoount();
        } else if (v.equals(btnGetSkuDetail)) {
            getSkuDetail();
        } else if (v.equals(btnShare)) {
            share();
        } else if (v.equals(btnFaq)) {
            faq();
        } else if (v.equals(btnOpenLink)) {
            requestOpenLink();
        }
    }

    private void share() {
        if (mSDKParams != null && mSDKParams.isApplyParams()) {
            //自定义参数，测试需要
            SocialType socialType = mSDKParams.getSocialType();
            int shareType = mSDKParams.getShareType();
            switch (shareType) {
                case SDKParams.LINK_TYPE:
                    String title = mSDKParams.getShareTitle();
                    String link = mSDKParams.getShareLink();
                    shareLink(socialType, title, link);
                    break;
                case SDKParams.IMAGE_TYPE:
                    Intent intent =new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, REQUEST_SHARE_IMG);
                    break;
                case SDKParams.AWARD_TYPE:
                    Intent intent2 =new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent2.setType("image/*");
                    startActivityForResult(intent2, REQUEST_SHARE_IMG_WITH_AWARD);
                    break;
                default:
                    break;
            }
        } else {
            shareLink(SocialType.FACEBOOK_TYPE, "测试", "https://www.baidu.com");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if(requestCode == REQUEST_SHARE_IMG) {
                Uri uri = data.getData();
                shareImage(mSDKParams.getSocialType(), uri);
            } else if (requestCode == REQUEST_SHARE_IMG_WITH_AWARD) {
                String awardId = mSDKParams.getAcheicementId();
                String serverId = mSDKParams.getServerId();
                String roleId = mSDKParams.getRoleId();
                String roleName = mSDKParams.getRoleName();
                Uri uri = data.getData();
                shareImageWithAward(mSDKParams.getSocialType(), awardId, uri, serverId, roleId, roleName);
            }
        }
    }

    /**
     * 分享链接
     *
     * @param socialType 社交类型
     * @param title      标题
     * @param url        链接
     */
    private void shareLink(SocialType socialType, String title, String url) {
        riverSDKApi.sqSDKShareToSocialAPP(activity,
                socialType,
                title,
                url,
                null,
                new SDKCallback() {
                    @Override
                    public void onResult(int statusCode, Map<String, String> params) {
                        String msg = params.get(CallbackKey.MSG);
                        Log.e(TAG, "sqSDKShareToSocialAPP onResult statusCode=" + statusCode + " msg=" + msg);
                        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 分享图片
     *
     * @param socialType 社交类型
     * @param imageUri  图片路径
     */
    private void shareImage(SocialType socialType, Uri imageUri) {
        riverSDKApi.sqSDKShareToSocialAPP(activity,
                socialType,
                "",
                "",
                imageUri,
                new SDKCallback() {
                    @Override
                    public void onResult(int statusCode, Map<String, String> params) {
                        String msg = params.get(CallbackKey.MSG);
                        Log.e(TAG, "sqSDKShareToSocialAPP onResult statusCode=" + statusCode + " msg=" + msg);
                        Log.e(TAG, "isMainThread=" + Thread.currentThread().getName());
                        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 分享图片并发奖
     *
     * @param socialType 社交类型
     * @param awardId    发奖ID
     * @param imageUri  图片路径
     * @param serverId   服务器ID
     * @param roleId     角色ID
     * @param roleName   角色名称
     */
    private void shareImageWithAward(SocialType socialType,
                                     String awardId,
                                     Uri imageUri,
                                     String serverId,
                                     String roleId,
                                     String roleName) {
        riverSDKApi.sqSDKShareToSocialAPP(activity,
                socialType,
                awardId,
                imageUri,
                serverId,
                roleId,
                roleName,
                new SDKCallback() {
                    @Override
                    public void onResult(int statusCode, Map<String, String> params) {
                        String msg = params.get(CallbackKey.MSG);
                        Log.e(TAG, "sqSDKShareToSocialAPP onResult statusCode=" + statusCode + " msg=" + msg);
                        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    /**
     * 打开链接
     */
    private void requestOpenLink() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String url = "https://www.37games.com";
                riverSDKApi.sqSDKOpenLink(
                        activity, url, new ShowViewCallback() {

                            @Override
                            public void onViewShow() {
                                Log.i(TAG, "webview: " + "onViewShow");
                            }

                            @Override
                            public void onViewDismiss() {
                                Log.i(TAG, "webview: " + "onViewDismiss");
                            }
                        });
            }
        });
    }

    /**
     * 游戏内事件上报
     */
    private void trackGameEvent() {

        //以上报等级统计为例，游戏在角色升级的时候需要进行等级上报（每一级都上报）
        String eventType = "custom_active";
        String eventName = "player_levelup";
        String eventValue = "1";
        if (mSDKParams != null && mSDKParams.isApplyParams()) {
            //自定义参数，测试需要
            eventType = mSDKParams.getEventType();
            eventName = mSDKParams.getEventName();
            eventValue = mSDKParams.getEventValue();
        }
        riverSDKApi.sqSDKTrackGameEvent(eventType, eventName, eventValue);
    }

    /**
     * 账密体系切换账号可以参考以下方法
     */
    private void switchAcoount() {
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

    /**
     * 游戏内需要设置一个用户中心的按钮，然后调用SDK用户中心接口
     */
    private void openUserCenter() {
        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                riverSDKApi.sqSDKPresentUserCenterView(
                        activity, new ShowViewCallback() {
                            @Override
                            public void onViewShow() {
                                Log.i(TAG, "UserCenter onViewShow");
                            }

                            @Override
                            public void onViewDismiss() {
                                Log.i(TAG, "UserCenter onViewDismiss");
                            }
                        });
            }
        });
    }


    private void autoLogin() {
        riverSDKApi.sqSDKAutoLogin(activity, new SDKCallback() {
            @Override
            public void onResult(int statusCode, Map<String, String> params) {
                if (StatusCode.SUCCESS == statusCode) {
                    LogHelper.d("----------autoLogin-------------", params);
                    RiverSDKDemoActivity.loginSuccess(params);
                } else {
                    //登录失败，msg获取登录失败信息
                    String msg = params.get(CallbackKey.MSG);
                    Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 储值
     */
    private void billing() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //玩家角色储值所在的服务器编号
                String serverId = "10001";
                //角色id
                String roleId = "36039535510945817";
                //角色名称
                String roleName = "권재미";
                //角色等级
                String roleLevel = "260";
                //商品项ID（商品项ID需要录入到GooglePlay后台，需要传入特定的ID否则充值的时候会提示查询商品项失败）
                String productId = "sl.app.gold90";
                //开发商订单号
                String cpOrderId = "cd6ztek2nh2m7zukdy258hbax963xr24";
                //开发商商品项id(非必须，有则传没有可以不传)
                String cpProductId = "app-gold90";
                //游戏内显示的货币币种(非必须，有则传没有可以不传)
                String tagCurrency = "USD";
                //游戏内显示的价格(非必须，有则传没有可以不传)
                String tagMoney = "4.99";
                //游戏内显示商品描述(非必须，有则传没有可以不传)
                String productDesc = "90元宝(首储翻倍)";
                //订单附加信息（非必须，该值最后会由SDK服务器透传到游戏发货服务器）
                String remark = "12345-789";


                if (mSDKParams != null &&
                        mSDKParams.isApplyParams()) {
                    //自定义参数，测试需要
                    serverId = mSDKParams.getServerId();
                    roleId = mSDKParams.getRoleId();
                    roleName = mSDKParams.getRoleName();
                    productId = mSDKParams.getProductId();
                    roleLevel = mSDKParams.getRoleLevel();
                }

                Bundle purchaseParams = new Bundle();
                //（必传）选择应用内购买(PurchaseType.ITEM_TYPE_APP)或是订阅类购买(PurchaseType.ITEM_TYPE_SUBS)
                purchaseParams.putInt(PurchaseParamsKey.PURCHASE_TYPE, PurchaseType.ITEM_TYPE_APP);
                //角色id（必传）
                purchaseParams.putString(PurchaseParamsKey.ROLE_ID, roleId);
                //角色名称（必传）
                purchaseParams.putString(PurchaseParamsKey.ROLE_NAME, roleName);
                //角色等级（必传）
                purchaseParams.putString(PurchaseParamsKey.ROLE_LEVEL, roleLevel);
                //玩家角色储值所在的服务器编号（必传）
                purchaseParams.putString(PurchaseParamsKey.SERVER_ID, serverId);
                //（必传）商品项ID（商品项ID需要录入到GooglePlay后台，需要传入特定的ID否则充值的时候会提示查询商品项失败）
                purchaseParams.putString(PurchaseParamsKey.PRODUCT_ID, productId);
                //开发商订单号（必传）
                purchaseParams.putString(PurchaseParamsKey.CP_ORDER_ID, cpOrderId);
                //开发商商品项id(非必须，有则传没有可以不传)
                purchaseParams.putString(PurchaseParamsKey.CP_PRODUCT_ID, cpProductId);
                //游戏内显示的货币(非必须，有则传没有可以不传)
                purchaseParams.putString(PurchaseParamsKey.TAG_CURRENCY, tagCurrency);
                //游戏内显示的价格(非必须，有则传没有可以不传)
                purchaseParams.putString(PurchaseParamsKey.TAG_MONEY, tagMoney);
                //游戏内显示商品描述(非必须，有则传没有可以不传)
                purchaseParams.putString(PurchaseParamsKey.PRODUCT_DESC, productDesc);
                //订单附加信息（非必须，该值最后会由SDK服务器透传到游戏发货服务器）
                purchaseParams.putString(PurchaseParamsKey.REMARK, remark);


                riverSDKApi.sqSDKInAppPurchase(
                        activity,
                        purchaseParams,
                        new SDKCallback() {

                            @Override
                            public void onResult(int stateCode, Map<String, String> params) {
                                if (StatusCode.SUCCESS == stateCode) {
                                    // 成功只返回商品项（sku）
                                    String productId = params.get(CallbackKey.PRODUCTID);
                                    String str = "productId: " + productId;
                                    Log.i(TAG, str);
                                } else { // 失败
                                    // 失败通过message查看失败原因
                                    String message = params.get(CallbackKey.MSG);
                                    String str = "message: " + message;
                                    Log.i(TAG, str);
                                }
                            }
                        });
            }
        });
    }

    /**
     * 获取商品项信息
     */
    private void getSkuDetail() {
        Log.e(TAG, "getSkuDetail");
        List<String> list = new ArrayList<String>();
        String productId = "qyjgp.app.gold1180";
        if (mSDKParams != null
                && mSDKParams.isApplyParams()) {
            //自定义参数，测试需要
            productId = mSDKParams.getProductId();
        }
        //商品项id
        list.add(productId);
        riverSDKApi.sqSDKRequestGoogleSkuDetail(activity, list
                , new SDKCallback() {
                    @Override
                    public void onResult(int statusCode, Map<String, String> params) {
                        if (StatusCode.SUCCESS == statusCode) {
                            //成功
                            Set<Map.Entry<String, String>> set = params.entrySet();
                            Iterator<Map.Entry<String, String>> it = set.iterator();
                            while (it.hasNext()) {
                                Map.Entry<String, String> entry = it.next();
                                String key = entry.getKey();
                                String value = entry.getValue();
                                Log.e(TAG, "getSkuDetail success key=" + key + "---" + "value=" + value);
                            }
                        } else {
                            // 失败
                            String msg = params.get(CallbackKey.MSG);
                            Log.e(TAG, "getSkuDetail fail msg =" + msg);
                        }
                    }
                });
    }

    /**
     * 客服工单
     */
    private void faq() {
        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
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
        });
    }

    /**
     * 上报服务器编码，在用户选服完成后，需要调用SDK的入服上报接口
     */
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

        riverSDKApi.sqSDKReportServerCode(activity, reportServerParams);
    }


}
