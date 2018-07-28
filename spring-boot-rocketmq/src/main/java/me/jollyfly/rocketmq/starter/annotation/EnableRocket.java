package me.jollyfly.rocketmq.starter.annotation;

import me.jollyfly.rocketmq.starter.configuration.RocketMqAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 使用此注解，允许在程序中使用rocketMQ
 *
 * @author jolly
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(RocketMqAutoConfiguration.class)
public @interface EnableRocket {
}
