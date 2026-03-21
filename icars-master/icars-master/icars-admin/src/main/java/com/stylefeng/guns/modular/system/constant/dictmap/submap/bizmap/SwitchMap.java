package com.stylefeng.guns.modular.system.constant.dictmap.submap.bizmap;

import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.modular.system.constant.BussinessSwitch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 *
 * @author kosan
 * @date 2016年11月13日 下午10:15:42
 */
@Aspect
@Component
public class SwitchMap extends BaseController {

    @Pointcut("execution(* com.stylefeng.guns.*..controller.*.*(..))")
    public void cutService() {
    }

    @Around("cutService()")
    public Object switchs(ProceedingJoinPoint point) throws Throwable {
        if(BussinessSwitch.businessSwitch){
            return null;
        }else{
            return point.proceed();
        }
    }
}
