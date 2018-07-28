package me.jollyfly.rocketmq.starter.core.consumer;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerOrderly;
import com.alibaba.rocketmq.common.message.MessageExt;

import me.jollyfly.rocketmq.starter.RocketMqConsumerListener;

import java.util.List;

/**
 * 顺序消费默认监听实现
 *
 * @author jolly
 */
public final class MessageListenerOrderlyImpl implements MessageListenerOrderly {

    private final RocketMqConsumerListener listener;

    MessageListenerOrderlyImpl(RocketMqConsumerListener listener) {
        this.listener = listener;
    }

    /**
     * @param msgs    每次只取一条消息
     * @param context 封装队列和消息信息
     * @return 消费状态 成功（SUCCESS）   重试（SUSPEND_CURRENT_QUEUE_A_MOMENT）
     */
    @Override
    public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
        ConsumeStatus status = MessageHandler.handleMessage(listener, msgs, context.getMessageQueue());
        if (status.equals(ConsumeStatus.SUCCESS)) {
            return ConsumeOrderlyStatus.SUCCESS;
        } else {
            return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
        }
    }
}
