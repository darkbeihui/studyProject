package com.beihui.spring;

/**
 * @ClassName: InitializingBean
 * @description:
 * @author: 北慧
 * @date: 2023/2/10 02:12
 * @Version: 1.0
 **/
public interface InitializingBean {

    void afterPropertiesSet() throws Exception;
}
