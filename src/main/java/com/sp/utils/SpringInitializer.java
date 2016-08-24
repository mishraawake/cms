package com.sp.utils;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Created by pankajmishra on 07/08/16.
 */
@Configuration
@ComponentScan(basePackages = {"com.sp.service.impl", "com.sp.dao.jcr", "com.sp.dao.spring"})
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class SpringInitializer {

}
