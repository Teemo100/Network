package com.soul.netutil;

import com.soul.netutil.base.DefaultConfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * description:注解里添加config 实例,在 ApiService中加入注解
 * @author soul
 * Create date: 2019/7/1/001 1:39
*/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ApiConfig {
  Class<? extends Api.Config> value() default DefaultConfig.class;
}
