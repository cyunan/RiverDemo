package com.games37.riversdk.demo.scene;

import java.util.Map;

/**
 * @name:dev_demo_ui
 * @package:com.games37.riversdk.demo
 * @author:kechuanghao
 * @created:3:50 PM
 * @description:执行页面切换的接口
 */
public interface Navigation {
    void gotoGameScene(Map<String, String> params);

    void gotoEnterGameScene(Map<String, String> params);

    void gotoSettingScene();

    void gotoLoginScene();

    void removeSettingScene();
}