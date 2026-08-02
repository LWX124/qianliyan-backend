package com.cheji.b.config;

import com.github.binarywang.wxpay.service.WxPayService;
import org.junit.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import javax.annotation.Resource;
import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * 验证 WxPayConfiguration 能在 Spring 容器中正确注入 WxPayProperties 并创建 WxPayService。
 *
 * 背景：线上 b.jar 中该类只有无参构造器、且字段无任何注入注解（构建时 Lombok
 * 的 @AllArgsConstructor 未生效），导致 properties 恒为 null，wxService() 抛 NPE，
 * 容器陷入崩溃重启循环，最终耗尽宿主机 netfilter/BPF 资源导致整机网络瘫痪。
 */
public class WxPayConfigurationTest {

    private final ApplicationContextRunner runner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(WxPayConfiguration.class));

    @Test
    public void wxServiceShouldBeCreatedWithInjectedProperties() {
        runner.withPropertyValues(
                        "wx.pay.appId=wx89a612481874931c",
                        "wx.pay.mchId=1487083832",
                        "wx.pay.mchKey=testMchKey123456")
                .run(context -> {
                    // 上下文必须启动成功，不能因 NPE 失败
                    assertNotNull("Spring 上下文启动失败", context.getStartupFailure() == null ? context : null);
                    WxPayService service = context.getBean(WxPayService.class);
                    assertNotNull("WxPayService 未创建", service);
                    // 配置值必须真正绑定进去，而不是 null
                    assertEquals("wx89a612481874931c", service.getConfig().getAppId());
                    assertEquals("1487083832", service.getConfig().getMchId());
                });
    }

    /**
     * 守住线上真实缺陷：properties 字段必须带显式注入注解。
     * 若只依赖 Lombok 生成构造器，注解处理器一旦未生效，编译产物就只有无参
     * 构造器，字段永远为 null —— 这正是本次线上故障的成因，且编译期无警告。
     */
    @Test
    public void propertiesFieldMustHaveExplicitInjectionAnnotation() throws Exception {
        Field field = WxPayConfiguration.class.getDeclaredField("properties");
        boolean hasResource = field.isAnnotationPresent(Resource.class);
        boolean hasAutowired = field.isAnnotationPresent(
                org.springframework.beans.factory.annotation.Autowired.class);
        assertTrue("properties 字段必须带 @Resource 或 @Autowired，"
                + "否则依赖 Lombok 生成构造器，注解处理器未生效时会静默产生 NPE",
                hasResource || hasAutowired);
    }
}
