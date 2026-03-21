package com.stylefeng.guns.modular.system.constant;

import com.stylefeng.guns.core.common.annotion.CnName;

/**
 * <ul>
 * <li>文件包名 : com.stylefeng.guns.modular.system.constant</li>
 * <li>创建时间 : 2019-03-25 15:11</li>
 * <li>修改记录 : 无</li>
 * </ul>
 * 类说明：
 *
 * @author duanhong
 * @version 2.0.0
 */
public interface RedisKey {

    @CnName("华为文字识别ak")
    String HUA_WEI_AK = "HUA_WEI_AK";

    @CnName("小程序首页视频地址")
    String XCX_HOME_VIDEO_URL = "XCX_HOME_VIDEO_URL";

    @CnName("环信好友列表")
    String HUANXIN_FRIEND_LIST = "HUANXIN_FRIEND_LIST";

    @CnName("环信好友列表(理赔员列表)")
    String HUANXIN_FRIEND_LIST_LP = "HUANXIN_FRIEND_LIST_LP";

    @CnName("环信好友列表（4s店员列表）")
    String HUANXIN_FRIEND_LIST_4S = "HUANXIN_FRIEND_LIST_4S";

    @CnName("环信token")
    String HUANXIN_TOKEN = "HUANXIN_TOKEN";

    @CnName("版本号信息")
    String CJ_LI_APP = "CJ_LI_APP";
}
