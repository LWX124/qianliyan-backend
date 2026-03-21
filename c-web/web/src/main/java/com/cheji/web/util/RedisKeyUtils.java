package com.cheji.web.util;

public class RedisKeyUtils {


    /**
     * 拼接被点赞的评论id和点赞的人的id作为key。格式 222222::333333
     * @param videoCommontsId 被点赞视频评论的id
     * @param userId 点赞的人的id
     * @return
     */
    public static String getKey(String videoCommontsId, String userId){
        StringBuilder builder = new StringBuilder();
        builder.append(videoCommontsId);
        builder.append("::");
        builder.append(userId);
        return builder.toString();
    }
}