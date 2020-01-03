package com.zhuchuansheng.cmfz;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.zhuchuansheng.cmfz.util.SendTextMessageUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zhuchuansheng
 * @date: 2019/12/31
 * @time: 14:09
 */
@SpringBootTest(classes = CmfzApplication.class)
@RunWith(SpringRunner.class)
public class TestSendTextMessage {



    @Test
    public void testSendTextMessage() throws ClientException {

        //设置超时时间-可自行调整
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化ascClient需要的几个参数
        final String product = "Dysmsapi";//短信API产品名称（短信产品名固定，无需修改）
        final String domain = "dysmsapi.aliyuncs.com";//短信API产品域名（接口地址固定，无需修改）

        //替换成你的AK 你的accessKeyId,参考本文档步骤2
        final String accessKeyId = "LTAIvZOy1y67DvoJ";
        //你的accessKeySecret，参考本文档步骤2
        final String accessKeySecret = "FYjlH0bMN9bYTKyC2BX2G2NAxC6f7v";

        //初始化ascClient,暂时不支持多region（请勿修改）
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象
        SendSmsRequest request = new SendSmsRequest();
        //使用post提交
        request.setMethod(MethodType.POST);

        //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式；发送国际/港澳台消息时，接收号码格式为国际区号+号码，如“85200000000”
//        request.setPhoneNumbers("17615048621,13797487889,18827506177,18728713921,17006692102,15826921921");
        request.setPhoneNumbers("18728713921");

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
        request.setTemplateParam("{\"code\":\"88888\"}");

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
            System.out.println(code+"---"+message);
        }

    }



    @Test
    public void test(){

        try {
            String s = SendTextMessageUtil.sendMessage("18728713921", "{\"code\":\"666666\"}");
        } catch (ClientException e) {
            e.printStackTrace();
        }

    }



}
