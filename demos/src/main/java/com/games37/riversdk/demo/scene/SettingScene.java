package com.games37.riversdk.demo.scene;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.games37.riversdk.common.utils.ResourceUtils;
import com.games37.riversdk.core.share.SocialType;
import com.games37.riversdk.demo.config.SDKParams;

import java.util.ArrayList;
import java.util.List;

/**
 * @name:dev_demo_ui
 * @package:com.games37.riversdk.demo
 * @author:kechuanghao
 * @created:9:28 AM
 * @description: 设置API参数的场景
 */
public class SettingScene extends Fragment implements View.OnClickListener {

    private Activity activity;
    private Spinner spinnerSocialtype;
    private RadioButton rbShareLink;
    private RadioButton rbShareImage;
    private RadioButton rbShareImageWithAward;
    private RadioGroup rgShareType;
    private EditText edtServerId;
    private EditText edtRoleId;
    private EditText edtRoleName;
    private EditText edtRoleLevel;
    private EditText edtProductId;
    private Button btnCancel;
    private Button btnConfirm;

    /**
     * SDK配置参数
     */
    SDKParams mSDKParams;

    private Navigation navigation;
    private EditText edtShareTitle;
    private EditText edtShareLink;
    private EditText edtShareImg;
    private EditText edtAwardId;
    private LinearLayout llShareTitle;
    private LinearLayout llShareLink;
    private LinearLayout llShareImage;
    private LinearLayout llAwardId;
    private TextView tvPackageName;
    private EditText edtEventType;
    private EditText edtEventName;
    private EditText edtEventValue;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
        navigation = (Navigation) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(ResourceUtils.getLayoutId(activity, "river_demo_setting_layout"), container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        spinnerSocialtype = (Spinner) view.findViewById(ResourceUtils.getResourceId(activity, "spinner_socialtype"));
        rbShareLink = (RadioButton) view.findViewById(ResourceUtils.getResourceId(activity, "rb_share_link"));
        rbShareImage = (RadioButton) view.findViewById(ResourceUtils.getResourceId(activity, "rb_share_image"));
        rbShareImageWithAward = (RadioButton) view.findViewById(ResourceUtils.getResourceId(activity, "rb_share_image_with_award"));
        rgShareType = (RadioGroup) view.findViewById(ResourceUtils.getResourceId(activity, "rg_share_type"));

        edtShareTitle = (EditText) view.findViewById(ResourceUtils.getResourceId(activity, "edt_share_title"));
        edtShareLink = (EditText) view.findViewById(ResourceUtils.getResourceId(activity, "edt_share_link"));
        edtShareImg = (EditText) view.findViewById(ResourceUtils.getResourceId(activity, "edt_share_img"));
        edtAwardId = (EditText) view.findViewById(ResourceUtils.getResourceId(activity, "edt_award_id"));

        edtServerId = (EditText) view.findViewById(ResourceUtils.getResourceId(activity, "edt_server_id"));
        edtRoleId = (EditText) view.findViewById(ResourceUtils.getResourceId(activity, "edt_role_id"));
        edtRoleName = (EditText) view.findViewById(ResourceUtils.getResourceId(activity, "edt_role_name"));
        edtRoleLevel = (EditText) view.findViewById(ResourceUtils.getResourceId(activity, "edt_role_level"));
        edtProductId = (EditText) view.findViewById(ResourceUtils.getResourceId(activity, "edt_product_id"));
        btnCancel = view.findViewById(ResourceUtils.getResourceId(activity, "btn_cancel"));
        btnConfirm = view.findViewById(ResourceUtils.getResourceId(activity, "btn_confirm"));

        llShareTitle = (LinearLayout) view.findViewById(ResourceUtils.getResourceId(activity, "ll_share_title"));
        llShareLink = (LinearLayout) view.findViewById(ResourceUtils.getResourceId(activity, "ll_share_link"));
        llShareImage = (LinearLayout) view.findViewById(ResourceUtils.getResourceId(activity, "ll_share_image"));
        llAwardId = (LinearLayout) view.findViewById(ResourceUtils.getResourceId(activity, "ll_award_id"));
        tvPackageName = view.findViewById(ResourceUtils.getResourceId(activity, "tv_package_name"));
        edtEventType = (EditText) view.findViewById(ResourceUtils.getResourceId(activity, "edt_event_type"));
        edtEventName = (EditText) view.findViewById(ResourceUtils.getResourceId(activity, "edt_event_name"));
        edtEventValue = (EditText) view.findViewById(ResourceUtils.getResourceId(activity, "edt_event_value"));

        btnCancel.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        tvPackageName.setText(activity.getPackageName());

        mSDKParams = SDKParams.getInstance();
        setShareParamsVisibility();

        rgShareType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == rbShareLink.getId()) {
                    mSDKParams.setShareType(SDKParams.LINK_TYPE);
                } else if (checkedId == rbShareImage.getId()) {
                    mSDKParams.setShareType(SDKParams.IMAGE_TYPE);
                } else if (checkedId == rbShareImageWithAward.getId()) {
                    mSDKParams.setShareType(SDKParams.AWARD_TYPE);
                }
                setShareParamsVisibility();
            }
        });
        List<String> list = new ArrayList<>();
        for (SocialType socialType : SocialType.values()) {
            list.add(socialType.name());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSocialtype.setAdapter(adapter);
        spinnerSocialtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view;
                if (tv != null) {
                    tv.setTextColor(ResourceUtils.getColor(activity, "text_color"));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void setShareParamsVisibility() {
        int shareType = mSDKParams.getShareType();
        if (shareType == SDKParams.LINK_TYPE) {
            llShareImage.setVisibility(View.GONE);
            llAwardId.setVisibility(View.GONE);
            llShareTitle.setVisibility(View.VISIBLE);
            llShareLink.setVisibility(View.VISIBLE);
        } else if (shareType == SDKParams.IMAGE_TYPE) {
            llShareTitle.setVisibility(View.GONE);
            llShareLink.setVisibility(View.GONE);
            llAwardId.setVisibility(View.GONE);
            llShareImage.setVisibility(View.VISIBLE);
        } else {
            llShareTitle.setVisibility(View.GONE);
            llShareLink.setVisibility(View.GONE);
            llShareImage.setVisibility(View.VISIBLE);
            llAwardId.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        edtShareTitle.setText(mSDKParams.getShareTitle());
        edtShareLink.setText(mSDKParams.getShareLink());
        edtShareImg.setText(mSDKParams.getPicPath());
        edtAwardId.setText(mSDKParams.getAcheicementId());

        edtServerId.setText(mSDKParams.getServerId());
        edtRoleId.setText(mSDKParams.getRoleId());
        edtRoleName.setText(mSDKParams.getRoleName());
        edtRoleLevel.setText(mSDKParams.getRoleLevel());
        edtProductId.setText(mSDKParams.getProductId());

        edtEventType.setText(mSDKParams.getEventType());
        edtEventName.setText(mSDKParams.getEventName());
        edtEventValue.setText(mSDKParams.getEventValue());

        SocialType socialType = mSDKParams.getSocialType();
        if (socialType != null) {
            spinnerSocialtype.setSelection(socialType.ordinal());
        } else {
            spinnerSocialtype.setSelected(false);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.equals(btnConfirm)) {
            mSDKParams.setApplyParams(true);
            Object selectedItem = spinnerSocialtype.getSelectedItem();
            if (selectedItem != null) {
                mSDKParams.setSocialType(SocialType.valueOf(selectedItem.toString()));
            }
            String shareTitle = edtShareTitle.getText().toString().trim();
            if (!TextUtils.isEmpty(shareTitle)) {
                mSDKParams.setShareTitle(shareTitle);
            }
            String shareLink = edtShareLink.getText().toString().trim();
            if (!TextUtils.isEmpty(shareLink)) {
                mSDKParams.setShareLink(shareLink);
            }
            String shareImg = edtShareImg.getText().toString().trim();
            if (!TextUtils.isEmpty(shareImg)) {
                mSDKParams.setPicPath(shareImg);
            }
            String awardId = edtAwardId.getText().toString().trim();
            if (!TextUtils.isEmpty(awardId)) {
                mSDKParams.setAcheicementId(awardId);
            }
            String serverId = edtServerId.getText().toString().trim();
            if (!TextUtils.isEmpty(serverId)) {
                mSDKParams.setServerId(serverId);
            }
            String roleId = edtRoleId.getText().toString().trim();
            if (!TextUtils.isEmpty(roleId)) {
                mSDKParams.setRoleId(roleId);
            }
            String roleName = edtRoleName.getText().toString().trim();
            if (!TextUtils.isEmpty(roleName)) {
                mSDKParams.setRoleName(roleName);
            }
            String roleLevel = edtRoleLevel.getText().toString().trim();
            if (!TextUtils.isEmpty(roleLevel)) {
                mSDKParams.setRoleLevel(roleLevel);
            }
            String productId = edtProductId.getText().toString().trim();
            if (!TextUtils.isEmpty(productId)) {
                mSDKParams.setProductId(productId);
            }
            String eventName = edtEventType.getText().toString().trim();
            if (!TextUtils.isEmpty(eventName)) {
                mSDKParams.setEventType(eventName);
            }
            String eventKey = edtEventName.getText().toString().trim();
            if (!TextUtils.isEmpty(eventKey)) {
                mSDKParams.setEventName(eventKey);
            }
            String eventValue = edtEventValue.getText().toString().trim();
            if (!TextUtils.isEmpty(eventValue)) {
                mSDKParams.setEventValue(eventValue);
            }
            SDKParams.save(activity, mSDKParams);

            navigation.removeSettingScene();
        } else if (v.equals(btnCancel)) {
            navigation.removeSettingScene();
        }
    }

}
