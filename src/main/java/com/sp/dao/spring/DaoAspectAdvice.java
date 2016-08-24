package com.sp.dao.spring;

import com.sp.dao.api.hooks.AfterWrite;
import com.sp.dao.api.hooks.BeforeWrite;
import com.sp.dao.jcr.hooks.ItemValidationHook;
import com.sp.dao.jcr.model.JCRItem;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by pankajmishra on 24/08/16.
 */

@Service
@Aspect
public class DaoAspectAdvice implements ApplicationContextAware {

    ApplicationContext applicationContext;

    @Autowired
    ItemValidationHook itemValidationHook;

    @Before("com.sp.dao.spring.DaoAspectCodometry.itemWrite()")
    public void beforeSave(JoinPoint joinPoint){

        Map<String, BeforeWrite> beforeWriteMap = applicationContext.getBeansOfType(BeforeWrite.class);

        List<BeforeWrite> beanList = new ArrayList<>(beforeWriteMap.values());

        Collections.sort( beanList, new Comparator<BeforeWrite>() {
            @Override
            public int compare(BeforeWrite o1, BeforeWrite o2) {
                return o1.rank().compareTo(o2.rank());
            }
        });

        beanList.forEach(
                (bean) -> {
                    bean.proceed( (JCRItem)joinPoint.getArgs()[0]);
                }
        );
    }



    @After("com.sp.dao.spring.DaoAspectCodometry.itemWrite()")
    public void afterSave(JoinPoint joinPoint){

        Map<String, AfterWrite> beforeWriteMap = applicationContext.getBeansOfType(AfterWrite.class);

        List<AfterWrite> beanList = new ArrayList<>(beforeWriteMap.values());

        Collections.sort( beanList, new Comparator<AfterWrite>() {
            @Override
            public int compare(AfterWrite o1, AfterWrite o2) {
                return o1.rank().compareTo(o2.rank());
            }
        });

        beanList.forEach(
                (bean) -> {
                    bean.doThis((JCRItem)joinPoint.getArgs()[0]);
                }
        );
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
