package me.jollyfly.rocketmq.starter.core;


import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.remoting.exception.RemotingException;

import me.jollyfly.rocketmq.starter.core.producer.MessageProxy;


/**
 * @author jolly
 */
public interface MqProducer<M> {

    void send(MessageProxy<M> messageProxy) throws MQClientException, InterruptedException, RemotingException;


    void start() throws MQClientException;

    void shutdown();

}
