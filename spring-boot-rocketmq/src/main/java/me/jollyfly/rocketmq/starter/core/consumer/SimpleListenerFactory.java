package me.jollyfly.rocketmq.starter.core.consumer;


import me.jollyfly.rocketmq.starter.RocketMqConsumerListener;
import me.jollyfly.rocketmq.starter.annotation.RocketListeners;
import me.jollyfly.rocketmq.starter.annotation.RocketMQListener;
import me.jollyfly.rocketmq.starter.exception.MethodNotSupportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jolly
 */
public class SimpleListenerFactory implements InitializingBean, ApplicationContextAware {


    private static final Logger logger = LoggerFactory.getLogger(SimpleListenerFactory.class);

    private Map<String, RocketMqConsumerListener> allListeners;

    private MethodResolver resolver;

    private ApplicationContext context;

    SimpleListenerFactory() {
        this.resolver = new MethodResolver();
    }

    private RocketMqConsumerListener createRocketMqConsumerListener(SubscriptionGroup subscriptionGroup) {
        RocketMqListenerMethodAdapter adapter = new RocketMqListenerMethodAdapter(subscriptionGroup);
        adapter.setInvoker(context.getBean(MethodInvoker.class));
        return adapter;
    }

    public Map<String, RocketMqConsumerListener> getAllListeners() {
        return allListeners;
    }

    @Override
    public void afterPropertiesSet() {
        allListeners = new HashMap<>();
        Map<String, SubscriptionGroup> subscriptionGroups = this.resolver.getSubscriptionGroups();
        subscriptionGroups.forEach((topic, subscriptionGroup) ->
                allListeners.put(topic, createRocketMqConsumerListener(subscriptionGroup)));

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
        this.resolver.setApplicationContext(applicationContext);
    }


    private class MethodResolver implements ApplicationContextAware {

        private ApplicationContext context;

        private Map<String, SubscriptionGroup> subscriptionGroups = new HashMap<>();

        private boolean initSubscription = false;


        Map<String, SubscriptionGroup> getSubscriptionGroups() {
            if (!initSubscription) {
                resolveListenerMethod();
            }
            return subscriptionGroups;
        }

        void resolveListenerMethod() {
            context.getBeansWithAnnotation(RocketListeners.class).forEach((beanName, obj) -> {
                Map<Method, RocketMQListener> annotatedMethods = MethodIntrospector.selectMethods(obj.getClass(),
                        (MethodIntrospector.MetadataLookup<RocketMQListener>) method -> AnnotatedElementUtils
                                .findMergedAnnotation(method, RocketMQListener.class));
                initSubscriptionGroup(annotatedMethods, obj);
            });
            this.initSubscription = true;
        }

        private void initSubscriptionGroup(Map<Method, RocketMQListener> annotatedMethod, Object target) {
            if (!CollectionUtils.isEmpty(annotatedMethod)) {
                annotatedMethod.forEach((method, listener) -> {
                    validateMethod(method);
                    RocketListeners rocketListeners = method.getDeclaringClass().getAnnotation(RocketListeners.class);
                    String topic = rocketListeners.topic();
                    String tag = listener.tag();
                    if (subscriptionGroups.containsKey(topic)) {
                        subscriptionGroups.get(topic).putTagToGroup(tag, method);
                    } else {
                        SubscriptionGroup subscriptionGroup = new SubscriptionGroup(topic);
                        subscriptionGroup.putTagToGroup(tag, method);
                        subscriptionGroup.setTarget(target);
                        subscriptionGroups.put(topic, subscriptionGroup);
                    }

                });
            }

        }

        private void validateMethod(Method method) {
            if (method.getParameterCount() > 2) {
                throw new MethodNotSupportException("method: " + method + " 参数列表不被支持");
            }
            boolean typeSupport = Arrays.stream(method.getParameterTypes()).allMatch(parmType -> parmType.equals(method
                    .getAnnotation
                            (RocketMQListener.class).messageClass()) || parmType.equals(MessageContext.class));
            if (!typeSupport) {
                throw new MethodNotSupportException("方法参数中含有不被支持的类型");
            }
        }

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            this.context = applicationContext;
        }
    }

}
