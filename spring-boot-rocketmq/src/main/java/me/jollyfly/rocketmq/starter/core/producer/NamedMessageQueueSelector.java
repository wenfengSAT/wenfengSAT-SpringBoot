package me.jollyfly.rocketmq.starter.core.producer;

import com.alibaba.rocketmq.client.producer.MessageQueueSelector;

import org.springframework.beans.factory.BeanNameAware;

/**
 *
 * @author jolly
 */
public interface NamedMessageQueueSelector extends MessageQueueSelector, BeanNameAware {

    String getBeanName();

}
