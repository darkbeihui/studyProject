package com.beihui.beihui.service;

import com.beihui.spring.BeanPostProcessor;
import com.beihui.spring.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @ClassName: BeihuiBeanPostProcessor
 * @description:
 * @author: 北慧
 * @date: 2023/2/10 02:20
 * @Version: 1.0
 **/
@Component
public class BeihuiBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        //如果只针对某一个bean使用这个方法 那么可以
        if (beanName.equals("userService")){
            Object proxyInstance = Proxy.newProxyInstance(BeihuiBeanPostProcessor.class.getClassLoader(), bean.getClass().getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    //切面 代理逻辑
                    System.out.println("切面 代理逻辑");
                    return method.invoke(bean,args);
                }
            });
            return proxyInstance;
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        //如果只针对某一个bean使用这个方法 那么可以
        //if (beanName.equals("xxx")){}
        System.out.println("postProcessAfterInitialization");
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
