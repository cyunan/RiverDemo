package com.games37.riversdk.demo.config;

import android.content.Context;

import com.games37.riversdk.common.log.LogHelper;
import com.games37.riversdk.core.share.SocialType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @name:RiverSDK_Android
 * @package:com.games37.riversdk.demo
 * @class: SDKParams.java
 * @author:kechuanghao
 * @created:2019/5/17 10:07 AM
 * @email:cooke@gm99.com
 * @description:自定义sdk参数配置
 */
public class SDKParams implements Serializable {
    public static final String TAG = SDKParams.class.getSimpleName();
    public static final String SDKPARAMS_FILENAME = "SDKParams.txt";
    private static final long serialVersionUID = 2948645251675060271L;

    private static volatile SDKParams instance;

    public static final int LINK_TYPE = 0;
    public static final int IMAGE_TYPE = 1;
    public static final int AWARD_TYPE = 2;

    private String serverId = "";

    private String roleId = "";

    private String roleName = "";

    private String roleLevel = "";

    private String acheicementId = "";

    private String productId = "";

    private String picPath = "";

    private String shareTitle = "";

    private String shareLink = "";

    private int shareType;

    private boolean applyParams = false;

    private SocialType socialType;

    private String eventType = "";

    private String eventName = "";

    private String eventValue = "1";

    public static SDKParams getInstance() {
        if (instance == null) {
            synchronized (SDKParams.class) {
                if (instance == null) {
                    instance = new SDKParams();
                }
            }
        }
        return instance;
    }


    private SDKParams() {

    }


    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleLevel() {
        return roleLevel;
    }

    public void setRoleLevel(String roleLevel) {
        this.roleLevel = roleLevel;
    }

    public String getAcheicementId() {
        return acheicementId;
    }

    public void setAcheicementId(String acheicementId) {
        this.acheicementId = acheicementId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public boolean isApplyParams() {
        return applyParams;
    }

    public void setApplyParams(boolean applyParams) {
        this.applyParams = applyParams;
    }

    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public String getShareLink() {
        return shareLink;
    }

    public void setShareLink(String shareLink) {
        this.shareLink = shareLink;
    }

    public int getShareType() {
        return shareType;
    }

    public void setShareType(int shareType) {
        this.shareType = shareType;
    }

    public SocialType getSocialType() {
        return socialType;
    }

    public void setSocialType(SocialType socialType) {
        this.socialType = socialType;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventValue() {
        return eventValue;
    }

    public void setEventValue(String eventValue) {
        this.eventValue = eventValue;
    }

    public static void resetParams(Context context) {
        SDKParams sdkParams = new SDKParams();
        save(context, sdkParams);
    }

    @Override
    public String toString() {
        return "SDKParams{" +
                "serverId='" + serverId + '\'' +
                ", roleId='" + roleId + '\'' +
                ", roleName='" + roleName + '\'' +
                ", roleLevel='" + roleLevel + '\'' +
                ", acheicementId='" + acheicementId + '\'' +
                ", productId='" + productId + '\'' +
                ", picPath='" + picPath + '\'' +
                ", shareTitle='" + shareTitle + '\'' +
                ", shareLink='" + shareLink + '\'' +
                ", shareType=" + shareType +
                ", applyParams=" + applyParams +
                ", socialType=" + socialType +
                ", eventType='" + eventType + '\'' +
                ", eventName='" + eventName + '\'' +
                ", eventValue='" + eventValue + '\'' +
                '}';
    }

    public static void save(Context context, SDKParams sdkParams) {
        LogHelper.i(TAG, "saveSDKParams " + sdkParams.toString());
        ObjectOutputStream output = null;
        try {
            File file = new File(getPath(context));
            if (!file.exists()) {
                file.createNewFile();
            }
            output = new ObjectOutputStream(new FileOutputStream(file));
            output.writeObject(sdkParams);
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (Exception e) {

                }
            }
        }
    }

    public static void updateFromDisk(Context context) {
        //反序列化过程
        ObjectInputStream ois = null;
        FileInputStream fis = null;
        try {
            File file = new File(getPath(context));
            if (!file.exists()) {
                file.createNewFile();
            }
            fis = new FileInputStream(file);
            ois = new ObjectInputStream(fis);
            SDKParams sdkParams = (SDKParams) ois.readObject();
            update(sdkParams);

            fis.close();
            ois.close();
            LogHelper.i(TAG, "getSDKParams " + sdkParams.toString());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (Exception e) {

                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception e) {

                }
            }
        }
    }

    private static void update(SDKParams sdkParams) {
        SDKParams params = SDKParams.getInstance();
        params.setApplyParams(sdkParams.applyParams);
        params.setSocialType(sdkParams.socialType);
        params.setShareType(sdkParams.shareType);
        params.setShareTitle(sdkParams.shareTitle);
        params.setShareLink(sdkParams.shareLink);
        params.setPicPath(sdkParams.picPath);
        params.setAcheicementId(sdkParams.acheicementId);

        params.setServerId(sdkParams.serverId);
        params.setRoleId(sdkParams.roleId);
        params.setRoleLevel(sdkParams.roleLevel);
        params.setRoleName(sdkParams.roleName);
        params.setProductId(sdkParams.productId);

        params.setEventType(sdkParams.eventType);
        params.setEventName(sdkParams.eventName);
        params.setEventValue(sdkParams.eventValue);

    }

    private static String getPath(Context context) {
        return context.getCacheDir() + File.separator + SDKPARAMS_FILENAME;
    }

}
