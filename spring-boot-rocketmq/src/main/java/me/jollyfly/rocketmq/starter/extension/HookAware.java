package me.jollyfly.rocketmq.starter.extension;

import org.springframework.beans.factory.Aware;

/**
 * 在类进行实例化后对泛型所对应的钩子进行set注入
 *
 * @author jolly
 */
public interface HookAware<H extends Hook> extends Aware {

    /**
     * 设置bean的钩子
     *
     * @param hook
     */
    void setHook(H hook);


}
