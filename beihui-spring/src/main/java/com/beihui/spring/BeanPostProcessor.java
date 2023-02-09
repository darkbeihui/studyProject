package com.beihui.spring;

/**
 * @ClassName: BeanPostProcessor
 * @description:
 * @author: 北慧
 * @date: 2023/2/10 02:16
 * @Version: 1.0
 **/
public interface BeanPostProcessor {

    default Object postProcessBeforeInitialization(Object bean, String beanName){
        return bean;
    }

    default Object postProcessAfterInitialization(Object bean, String beanName){
        return bean;
    }

}
