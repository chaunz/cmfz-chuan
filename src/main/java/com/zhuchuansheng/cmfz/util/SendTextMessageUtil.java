package com.zhuchuansheng.cmfz.util;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

import javax.naming.ldap.PagedResultsControl;

/**
 * @author zhuchuansheng
 * @date: 2019/12/31
 * @time: 14:23
 */
public class SendTextMessageUtil {


    /**
     * 超时时间的key
     */
    private static final String DEFAULT_CONNECTION_TIME_OUT = "sun.net.client.defaultConnectTimeout";
    private static final String DEFAULT_READ_TIME_OUT = "sun.net.client.defaultReadTimeout";
    /**
     * 超时时间的value
     */
    private static final String DEFAULT_CONNECTION_TIME_OUT_VALUE = "10000";
    private static final String DEFAULT_READ_TIME_OUT_VALUE = "10000";

    //短信API产品名称（短信产品名固定，无需修改）
    private static final String PRODUCT = "Dysmsapi";
    //短信API产品域名（接口地址固定，无需修改）
    private static final String DOMAIN = "dysmsapi.aliyuncs.com";


    //替换成你的AK 你的accessKeyId,参考本文档步骤2
    private static final String ACCESS_KEY_ID = "LTAIvZOy1y67DvoJ";
    //你的accessKeySecret，参考本文档步骤2
    private static final String ACCESS_KEY_SECRET = "FYjlH0bMN9bYTKyC2BX2G2NAxC6f7v";


    /**
     *          短信发送的工具类
     *
     * @param phoneNumber           发送的电话号码
     * @param content               发生内容,需要json字符串形式的字符串   "{\"code\":\"88888\"}"
     * @return
     * @throws ClientException
     */
    public static String sendMessage(String phoneNumber, String content) throws ClientException {

        //设置超时时间-可自行调整
        System.setProperty(DEFAULT_CONNECTION_TIME_OUT, DEFAULT_CONNECTION_TIME_OUT_VALUE);
        System.setProperty(DEFAULT_READ_TIME_OUT, DEFAULT_READ_TIME_OUT_VALUE);

        //初始化ascClient,暂时不支持多region（请勿修改）
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", PRODUCT, DOMAIN);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象
        SendSmsRequest request = new SendSmsRequest();
        //使用post提交
        request.setMethod(MethodType.POST);

        //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式；发送国际/港澳台消息时，接收号码格式为国际区号+号码，如“85200000000”
//        request.setPhoneNumbers("17615048621,13797487889,18827506177,18728713921,17006692102,15826921921");
        request.setPhoneNumbers(phoneNumber);

        //必填:短信签名-可在短信控制台中找到
        request.setSignName("小黑人");
        //必填:短信模板-可在短信控制台中找到，发送国际/港澳台消息时，请使用国际/港澳台短信模版
        request.setTemplateCode("SMS_164270184");

        /**
         * 可选:模板中的变量替换JSON串,
         *      如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
         *
         * 友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,
         *          比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,
         *          否则会导致JSON在服务端解析失败
         */
//        request.setTemplateParam("{\"code\":\"88888\"}");
        request.setTemplateParam(content);

        /**
         * 可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段)
         *      request.setSmsUpExtendCode("90997");
         *
         * 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
         */
        request.setOutId("yourOutId");

        //请求失败这里会抛ClientException异常
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);

        //请求成功
        if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
            String code = sendSmsResponse.getCode();
            String message = sendSmsResponse.getMessage();

            return code;
        }else {
            return "error";
        }
    }


}
