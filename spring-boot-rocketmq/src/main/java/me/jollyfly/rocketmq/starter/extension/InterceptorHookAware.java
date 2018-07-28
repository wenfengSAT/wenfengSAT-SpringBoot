package me.jollyfly.rocketmq.starter.extension;

/**
 * 实现这个接口用来注入一个方法拦截钩子的实例，默认为InterceptorHookSupport
 *
 * @author jolly
 */
public interface InterceptorHookAware extends HookAware<InterceptorHook> {

    /**
     * set注入方法拦截钩子
     *
     * @param hook 方法拦截钩子
     */
    @Override
    void setHook(InterceptorHook hook);
}
