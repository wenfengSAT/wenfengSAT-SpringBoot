package me.jollyfly.rocketmq.starter.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * rocketmq基本配置
 *
 * @author jolly
 */


@ConfigurationProperties("rocketmq")
@Data
public final class RocketMqProperties {
    /**
     * @see com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer#namesrvAddr
     */
    private String nameSrvAddr = "localhost:9876";

    /**
     * @see com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer#consumeThreadMin
     */
    private int consumeThreadMin = 20;
    /**
     * @see com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer#consumeThreadMax
     */
    private int consumeThreadMax = 64;

}
