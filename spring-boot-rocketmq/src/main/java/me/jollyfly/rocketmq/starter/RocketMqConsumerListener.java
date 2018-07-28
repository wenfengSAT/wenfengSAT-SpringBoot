package me.jollyfly.rocketmq.starter;


import me.jollyfly.rocketmq.starter.core.consumer.ConsumerConfig;
import me.jollyfly.rocketmq.starter.core.consumer.MessageContext;
import me.jollyfly.rocketmq.starter.exception.ConsumeException;

/**
 * @author jolly
 */
public interface RocketMqConsumerListener<E> {

    void onMessage(E message, MessageContext messageContext) throws ConsumeException;

    ConsumerConfig getConsumerConfig();


}
