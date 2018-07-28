package me.jollyfly.rocketmq.starter.core.consumer;


import me.jollyfly.rocketmq.starter.RocketMqConsumerListener;
import me.jollyfly.rocketmq.starter.annotation.RocketListeners;
import me.jollyfly.rocketmq.starter.annotation.RocketMQListener;
import me.jollyfly.rocketmq.starter.exception.ConsumeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jolly
 */
public final class RocketMqListenerMethodAdapter<E> implements RocketMqConsumerListener<E> {

    private static final Logger logger = LoggerFactory.getLogger(RocketMqListenerMethodAdapter.class);

    private final SubscriptionGroup subscriptionGroup;

    private ConsumerConfig consumerConfig;

    private MethodInvoker invoker;

    RocketMqListenerMethodAdapter(SubscriptionGroup subscriptionGroup) {
        this.subscriptionGroup = subscriptionGroup;
        initConfig(subscriptionGroup);
    }

    private void initConfig(SubscriptionGroup subscriptionGroup) {
        RocketListeners rocketListeners = subscriptionGroup.getTarget().getClass().getAnnotation(RocketListeners.class);
        consumerConfig = ConsumerConfig.builder()
                .consumerGroup(rocketListeners.consumerGroup())
                .messageModel(rocketListeners.messageModel())
                .orderlyMessage(rocketListeners.orderly())
                .topic(rocketListeners.topic())
                .consumeThreadMax(rocketListeners.consumeThreadMax())
                .consumeThreadMin(rocketListeners.consumeThreadMin())
                .build();
        Map<String, Class<?>> tags = new HashMap<>();
        subscriptionGroup.getTagList().forEach(tag -> {
            RocketMQListener rocketMQListener = subscriptionGroup.getMethod(tag).getAnnotation(RocketMQListener.class);
            tags.put(tag, rocketMQListener.messageClass());
        });
        consumerConfig.setTags(tags);

    }


    @Override
    public void onMessage(E message, MessageContext context) throws ConsumeException {
        if (logger.isDebugEnabled()) {
            logger.debug("recived message:{}", message);
        }
        String tag = context.getMessageExt().getTags();
        Method method = this.subscriptionGroup.getMethod(tag);
        Object delegate = this.subscriptionGroup.getTarget();
        if (method != null) {
            try {
                invoker.invoke(delegate, method, message, context);
            } catch (Exception e) {
                throw new ConsumeException(e);
            }
        } else {
            if (("*").equals(tag.trim())) {
                invoker.invoke(delegate, this.subscriptionGroup.getAllMethods(), message, context);
            } else {
                throw new ConsumeException("未找到相应tag的方法");
            }
        }


    }

    @Override
    public ConsumerConfig getConsumerConfig() {
        return this.consumerConfig;
    }


    public void setInvoker(MethodInvoker invoker) {
        this.invoker = invoker;
    }
}
