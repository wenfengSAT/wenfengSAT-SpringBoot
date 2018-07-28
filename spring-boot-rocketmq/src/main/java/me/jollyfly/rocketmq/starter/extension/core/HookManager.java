package me.jollyfly.rocketmq.starter.extension.core;


import me.jollyfly.rocketmq.starter.extension.HookAware;
import me.jollyfly.rocketmq.starter.extension.InterceptorHookAware;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Arrays;
import java.util.Map;

/**
 *
 * @author jolly
 */
public class HookManager implements InitializingBean, ApplicationContextAware {

    private ApplicationContext context;

    private void awareHook() {
        InterceptorHookSupport support = new InterceptorHookSupport();
        support.setApplicationContext(context);
        support.afterPropertiesSet();
        Map<String, HookAware> awareMap = context.getBeansOfType(HookAware.class);
        for (Map.Entry<String, HookAware> awareEntry : awareMap.entrySet()) {
            HookAware aware = awareEntry.getValue();
            if (Arrays.asList(aware.getClass().getInterfaces()).contains(InterceptorHookAware.class)) {
                ((InterceptorHookAware) aware).setHook(support);
            }
        }

    }

    @Override
    public void afterPropertiesSet() {

        awareHook();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
