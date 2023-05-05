package com.hcm.framework.aop;


import com.hcm.common.core.entity.SysResource;
import com.hcm.system.service.ViewCounterService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * UV PV 计数器:监控接口调用量
 *
 * @author pc
 * @date 2023/05/03
 */
@Aspect
@Component
@Slf4j
public class ViewCounter {

    @Autowired
    private ViewCounterService viewCounterService;


    /**
     * execution([可见性]返回类型[声明类型].方法名(参数)[异常])
     */
    @Pointcut("execution(public * com.hcm.controller.*.*.*(..)))")
    public void pointerCut() {
    }

    @Before("pointerCut()")
    public void before(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        SysResource sysResource = new SysResource();
        sysResource.setControllerClass(signature.getDeclaringType().getTypeName());
        sysResource.setMethodName(signature.getName());
        viewCounterService.countResourceUVAndPV(sysResource);
    }

}
