package me.jollyfly.rocketmq.starter.core.consumer;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.common.message.MessageExt;

import me.jollyfly.rocketmq.starter.RocketMqConsumerListener;

import java.util.List;

/**
 * 并发消费监听默认实现
 *
 * @author jolly
 */
public final class MessageListenerConcurrentlyImpl implements MessageListenerConcurrently {

    private final RocketMqConsumerListener listener;

    MessageListenerConcurrentlyImpl(RocketMqConsumerListener listener) {
        this.listener = listener;
    }

    /**
     * 消费消息
     *
     * @param msgs    msgs.size() >= 1
     *                DefaultMQPushConsumer.consumeMessageBatchMaxSize=1，you can modify here
     *                这里只设置为1，当设置为多个时，msgs中只要有一条消息消费失败，就会整体重试
     * @param context 上下文信息
     * @return 消费状态  成功（CONSUME_SUCCESS）或者 重试 (RECONSUME_LATER)
     */
    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        ConsumeStatus status = MessageHandler.handleMessage(listener, msgs, context.getMessageQueue());
        if (status.equals(ConsumeStatus.SUCCESS)) {
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        } else {
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
    }

}
