package com.beihui.spring;

/**
 * @ClassName: BeanDefinition
 * @description:
 * @author: 北慧
 * @date: 2023/2/10 00:52
 * @Version: 1.0
 **/
public class BeanDefinition {
    //Bean的类型
    private Class type;
    //Bean的作用域
    private String scope;
    //Bean是不是懒加载
    private boolean isLazy;

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public boolean isLazy() {
        return isLazy;
    }

    public void setLazy(boolean lazy) {
        isLazy = lazy;
    }
}
