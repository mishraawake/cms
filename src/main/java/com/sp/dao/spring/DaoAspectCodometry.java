package com.sp.dao.spring;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Service;

/**
 * Created by pankajmishra on 24/08/16.
 *
 * This will contain all the point cuts of dao objects.
 */
@Service
@Aspect
public class DaoAspectCodometry {

    @Pointcut("execution(* com.sp.dao.jcr.JCRItemDao.create*(..))")
    public void itemWrite(){}

}
