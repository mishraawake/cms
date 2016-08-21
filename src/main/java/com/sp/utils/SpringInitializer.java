package com.sp.utils;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by pankajmishra on 07/08/16.
 */
@Configuration
@ComponentScan(basePackages = {"com.sp.service.impl", "com.sp.dao.jcr"})
public class SpringInitializer {

}
