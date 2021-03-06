package com.pingplusplus;

import com.pingplusplus.exception.*;
import com.pingplusplus.model.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/*
 * This Java source file was auto generated by running 'gradle init --type java-library'
 * by 'Afon' at '16-12-16 上午10:33' with Gradle 3.1
 *
 * @author Afon, @date 16-12-16 上午10:33
 */
public class PingppTest {

    @BeforeClass public static void initApiKey() {
        Pingpp.apiKey = PingppTestData.getApiKey();
        Pingpp.DEBUG = true;
    }

    @Test public void testSetApiKey() {
        assertEquals("apiKey should be set", "sk_test_ibbTe5jLGCi5rzfH4OqPW9KC", Pingpp.apiKey);
    }

    @Test public void testCreateCharge() {
        String appId = PingppTestData.getAppID();

        // 建议使用 PKCS8 编码的私钥，可以用 openssl 将 PKCS1 转成 PKCS8
        Pingpp.privateKey = PingppTestData.getPKCS8PrivateKey();

        Charge charge = null;
        Map<String, Object> chargeMap = new HashMap<String, Object>();
        chargeMap.put("amount", 100);//订单总金额, 人民币单位：分（如订单总金额为 1 元，此处请填 100）
        chargeMap.put("currency", "cny");
        chargeMap.put("subject", "Your Subject");
        chargeMap.put("body", "Your Body");
        String orderNo = "orderno" + new Date().getTime();
        chargeMap.put("order_no", orderNo);// 推荐使用 8-20 位，要求数字或字母，不允许其他字符
        chargeMap.put("channel", "alipay");// 支付使用的第三方支付渠道取值，请参考：https://www.pingxx.com/api#api-c-new
        chargeMap.put("client_ip", "192.168.1.132"); // 发起支付请求客户端的 IP 地址，格式为 IPV4，如: 127.0.0.1
        Map<String, String> app = new HashMap<String, String>();
        app.put("id", appId);
        chargeMap.put("app", app);

        Map<String, Object> extra = new HashMap<String, Object>();
        chargeMap.put("extra", extra);
        try {
            // 发起 charge 创建请求
            charge = Charge.create(chargeMap);
            // 传到客户端请先转成字符串 .toString(), 调该方法，会自动转成正确的 JSON 字符串
            String chargeString = charge.toString();
            System.out.println(chargeString);
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (ChannelException e) {
            e.printStackTrace();
        } catch (RateLimitException e) {
            e.printStackTrace();
        } catch (AuthenticationException e) {
            e.printStackTrace();
        } catch (APIException e) {
            e.printStackTrace();
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        }

        assertEquals("charge object should be charge", "charge", charge.getObject());
        assertEquals("charge order_no", orderNo, charge.getOrderNo());
    }

    @Test public void testWebhooksParseCharge() {
        String webhookData = PingppTestData.getChargeWebhooksData();

        PingppObject obj = Webhooks.getObject(webhookData);

        assertTrue("object should be an instance of Charge", obj instanceof Charge);
        assertEquals("object should be charge", "charge", ((Charge)obj).getObject());
    }

    @Test public void testWebhooksParseBatchTransfer() {
        String webhookData = PingppTestData.getBatchTransferWebhooksData();

        PingppObject obj = Webhooks.getObject(webhookData);

        assertTrue("object should be an instance of BatchTransfer", obj instanceof BatchTransfer);
        assertEquals("object should be batch_transfer", "batch_transfer", ((BatchTransfer)obj).getObject());
    }

    @Test public void testGetChargeList() {
        try {
            Integer limit = 3;
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("app[id]", PingppTestData.getAppID());
            params.put("limit", limit);
            ChargeCollection chs = Charge.list(params);

            System.out.println(chs);
            assertEquals("object should be list", "list", chs.getObject());
            assertEquals("data count should be same with limit", limit.intValue(), chs.getData().size());
        } catch (AuthenticationException e) {
            e.printStackTrace();
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIException e) {
            e.printStackTrace();
        } catch (ChannelException e) {
            e.printStackTrace();
        } catch (RateLimitException e) {
            e.printStackTrace();
        }
    }

    @Test public void testGetBatchRefundList() {
        try {
            Integer limit = 3;
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("per_page", limit);
            params.put("sign", false);
            BatchRefundCollection objs = BatchRefund.list(params);

            assertEquals("object should be list", "list", objs.getObject());
            assertEquals("data count should be same with per_page", limit.intValue(), objs.getData().size());
        } catch (AuthenticationException e) {
            e.printStackTrace();
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIException e) {
            e.printStackTrace();
        } catch (ChannelException e) {
            e.printStackTrace();
        } catch (RateLimitException e) {
            e.printStackTrace();
        }
    }
}
