package com.stylefeng.guns.huawei.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 呼叫事件与话单通知
 * 客户平台收到RTC业务平台的呼叫事件与话单通知的接口通知
 */
public class HostingVoiceEventDemoImpl {

    public static void main(String args[]) throws Exception {
        // String jsonBody = "{\"eventType\":\"answer\",\"statusInfo\":{\"sessionId\":\"1200_366_0_20161228102743@callenabler.home1.com\",\"timestamp\":\"2018-04-09  10:57:42\",\"userData\":\"cwgtest\"}}";
        String jsonBody = "{\"eventType\":\"fee\",\"feeLst\":[{\"appKey\":\"cJU9QxBt53ZytKo9X8C35Z5c6pvi\",\"bindNum\":\"+8675566660000\",\"callEndTime\":\"2016-12-26  01:19:46\",\"callOutAlertingTime\":\"2016-12-26  01:16:26\",\"callOutAnswerTime\":\"2016-12-26  01:16:31\",\"callOutStartTime\":\"2016-12-26  01:16:20\",\"callOutUnaswRsn\":0,\"calleeNum\":\"+8675588880000\",\"callerNum\":\"+8675566660000\",\"direction\":0,\"fwdAlertingTime\":\"2018-04-26  01:16:38\",\"fwdAnswerTime\":\"2016-12-26  01:16:45\",\"fwdDisplayNum\":\"+8675566660000\",\"fwdDstNum\":\"+8675588880001\",\"fwdStartTime\":\"2016-12-26  01:16:32\",\"fwdUnaswRsn\":0,\"icid\":\"9c2012a5bcb2721681009cb6c245fdc3.3663053204.1117803.14\",\"recordFlag\":0,\"sessionId\":\"1200_366_0_20161228102743@callenabler.home1.com\",\"spId\":\"dial010314322991\",\"ulFailReason\":0,\"endToEndID\":543207693},{\"appKey\":\"cJU9QxBt53ZytKo9X8C35Z5c6pvi\",\"bindNum\":\"+8675566660000\",\"callEndTime\":\"2016-12-26  01:19:46\",\"callOutAlertingTime\":\"2016-12-26  01:16:26\",\"callOutAnswerTime\":\"2016-12-26  01:16:31\",\"callOutStartTime\":\"2016-12-26  01:16:20\",\"callOutUnaswRsn\":0,\"calleeNum\":\"+8675588880000\",\"callerNum\":\"+8675566660000\",\"direction\":0,\"fwdAlertingTime\":\"2018-04-26  01:16:38\",\"fwdAnswerTime\":\"2016-12-26  01:16:45\",\"fwdDisplayNum\":\"+8675566660000\",\"fwdDstNum\":\"+8675588880001\",\"fwdStartTime\":\"2016-12-26  01:16:32\",\"fwdUnaswRsn\":0,\"icid\":\"9c2012a5bcb2721681009cb6c245fdc3.3663053204.1117803.14\",\"recordFlag\":0,\"sessionId\":\"1200_366_0_20161228102743@callenabler.home1.com\",\"spId\":\"dial010314322991\",\"ulFailReason\":0,\"endToEndID\":543207693}]}";
       

        
        // onCallEvent(jsonBody);
        onFeeEvent(jsonBody);
    }
    
    /**
     * 呼叫事件 for 语音回呼/语音通知/语音验证码
     * 
     * @param jsonBody
     * @breif 详细内容以接口文档为准
     */
    public static void onCallEvent(String jsonBody) {
        // 封装JOSN请求
        JSONObject json = JSON.parseObject(jsonBody);
        String eventType = json.getString("eventType"); // 通知事件类型
        
        if ("fee".equalsIgnoreCase(eventType)) {

            return;
        }

        JSONObject statusInfo = json.getJSONObject("statusInfo"); // 呼叫状态事件信息
        
        /**
         * Example: 此处已解析sessionId为例,请按需解析所需参数并自行实现相关处理
         *
         * 'timestamp': 呼叫事件发生时RTC业务平台的UNIX时间戳
         * 'userData': 用户附属信息
         * 'sessionId': 通话链路的标识ID
         * 'caller': 主叫号码
         * 'called': 被叫号码
         * 'partyType': 挂机的用户类型
         * 'stateCode': 通话挂机的原因值
         * 'stateDesc': 通话挂机的原因值的描述
         * 'displayCallerNum': 主显号码
         * 'digitInfo': 放音收号操作结果(即用户输入的数字)
         */
        if (statusInfo.containsKey("sessionId")) {

        }
    }

    /**
     * 话单通知 for 语音回呼/语音通知/语音验证码
     * 
     * @param jsonBody
     * @breif 详细内容以接口文档为准
     */
    public static void onFeeEvent(String jsonBody) {
        // 封装JSON请求
        JSONObject json = JSON.parseObject(jsonBody);
        String eventType = json.getString("eventType"); // 通知事件类型
        
        if (!("fee".equalsIgnoreCase(eventType))) {

            return;
        }

        JSONArray feeLst = json.getJSONArray("feeLst"); // 呼叫话单事件信息
        
        /**
         * Example: 此处已解析sessionId为例,请按需解析所需参数并自行实现相关处理
         *
         * 'direction': 通话的呼叫方向
         * 'spId': 客户的云服务账号
         * 'appKey': 商户应用的AppKey
         * 'icid': 呼叫记录的唯一标识
         * 'bindNum': 发起此次呼叫的CallEnabler业务号码
         * 'sessionId': 通话链路的唯一标识
         * 'callerNum': 主叫号码
         * 'calleeNum': 被叫号码
         * 'origCalleeNum'： 原始被叫号码
         * 'fwdDisplayNum': forward操作时的显示号码
         * 'fwdDstNum': forward操作时的forward号码
         * 'callInTime': 呼入的开始时间
         * 'fwdStartTime': forward操作的开始时间
         * 'fwdAlertingTime': forward操作后的振铃时间
         * 'fwdAnswerTime': forward操作后的应答时间
         * 'callEndTime': 呼叫结束时间
         * 'fwdUnaswRsn': forward操作失败的Q850原因值
         * 'failTime': 呼入,呼出的失败时间
         * 'ulFailReason': 通话失败的拆线点
         * 'sipStatusCode': 呼入,呼出的失败SIP状态码
         * 'callOutStartTime': Initcall的呼出开始时间
         * 'callOutAlertingTime': Initcall的呼出振铃时间
         * 'callOutAnswerTime': Initcall的呼出应答时间
         * 'callOutUnaswRsn': Initcall的呼出失败的Q850原因值
         * 'dynIVRStartTime': 自定义动态IVR开始时间
         * 'dynIVRPath': 自定义动态IVR按键路径
         * 'recordFlag': 录音标识
         * 'recordStartTime': 录音开始时间
         * 'recordObjectName': 录音文件名
         * 'recordBucketName': 录音文件所在的目录名
         * 'recordDomain': 存放录音文件的域名
         * 'recordFileDownloadUrl': 录音文件下载地址
         * 'recordPushURL': RTC业务平台向录音存储服务器推送录音文件的URL
         * 'ttsPlayTimes': 应用TTS功能时,使用TTS的总次数
         * 'ttsTransDuration': 应用TTS功能时,TTS Server进行TTS转换的总时长(单位为秒)
         * 'createMptyTime': 多方通话创建时间
         * 'joinMptyTime': 多方通话加入时间
         * 'mptyId': 多方通话标识
         * 'serviceType': 携带呼叫的业务类型信息
         * 'hostName': 话单生成的服务器设备对应的主机名
         * 'userData': 用户附属信息
         */
        //短时间内有多个通话结束时RTC业务平台会将话单合并推送,每条消息最多携带50个话单
        if (feeLst.size() > 1) {
            for (Object loop : feeLst) {
                if (((JSONObject)loop).containsKey("sessionId")) {

                }
            }
        } else if (feeLst.size() == 1) {
            if (feeLst.getJSONObject(0).containsKey("sessionId")) {

            }
        } else {

        }
    }
}