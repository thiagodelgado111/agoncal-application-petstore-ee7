package org.agoncal.application.petstore.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 *         This interceptor has been converted from CDI Interceptor to Spring AOP Aspect
 */

@Aspect
@Component
public class LoggingInterceptor 
{

    // ======================================
    // =             Attributes             =
    // ======================================

    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    // ======================================
    // =          Business methods          =
    // ======================================

    @Around("@within(org.agoncal.application.petstore.util.Loggable) || @annotation(org.agoncal.application.petstore.util.Loggable)")
    public Object intercept(ProceedingJoinPoint pjp) throws Throwable
    {
        String className = pjp.getTarget().getClass().getName();
        String methodName = pjp.getSignature().getName();
        
        logger.info(">>> {}-{}", className, methodName);
        try 
        {
            return pjp.proceed();
        } 
        finally 
        {
            logger.info("<<< {}-{}", className, methodName);
        }
    }
}
