package com.beihui.spring;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: BeihuiApplicationContext
 * @description:
 * @author: 北慧
 * @date: 2023/2/9 23:13
 * @Version: 1.0
 **/
public class BeihuiApplicationContext {

    private Class configClass;
    private Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();
    private Map<String, Object> singletonObjects = new HashMap<>();
    private List<BeanPostProcessor> beanPostProcessorList=new ArrayList<>();

    public BeihuiApplicationContext(Class configClass) {
        this.configClass = configClass;

        //扫描
        scan(configClass);

        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
            String beanName = entry.getKey();
            BeanDefinition beanDefinition = entry.getValue();
            if ("singleton".equals(beanDefinition.getScope())) {
                //创建bean
                Object bean = createBean(beanName, beanDefinition);
                singletonObjects.put(beanName, bean);
            }
        }
    }


    public Object getBean(String beanName) {
        //如果在beanDefinitionMap中不存在当前bean，抛出异常
        if (!beanDefinitionMap.containsKey(beanName)) {
            throw new NullPointerException(beanName + "不存在");
        }
        //存在  获取BeanDefinition对象
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        //判断当前bean是单例还是原型bean
        if (beanDefinition.getScope().equals("singleton")) {
            Object singletonBean = singletonObjects.get(beanName);
            if (null == singletonBean) {
                singletonBean = createBean(beanName, beanDefinition);
                singletonObjects.put(beanName, singletonBean);
            }
            return singletonBean;
        } else {
            //原型
            return createBean(beanName, beanDefinition);
        }
    }

    private Object createBean(String beanName, BeanDefinition beanDefinition) {
        Class clazz = beanDefinition.getType();
        Object instance = null;
        try {
            instance = clazz.getConstructor().newInstance();

            //遍历类中的属性
            for (Field field : clazz.getDeclaredFields()) {
                //判断属性是否加了Autowired注解
                if (field.isAnnotationPresent(Autowired.class)) {
                    field.setAccessible(true);
                    field.set(instance, getBean(field.getName()));
                }
            }
            //初始化前  逻辑和初始化后一样
            //instance=beanPostProcessor.postProcessBeforeInitialization(instance,beanName);

            //初始化
            if (instance instanceof InitializingBean) {
                ((InitializingBean) instance).afterPropertiesSet();
            }

            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                // 初始化后
                instance=beanPostProcessor.postProcessAfterInitialization(instance,beanName);
            }

        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return instance;
    }

    private void scan(Class configClass) {
        //判断configClass是否有ComponentScan注解
        if (configClass.isAnnotationPresent(ComponentScan.class)) {
            //获取ComponentScan注解信息
            ComponentScan configClassAnnotation = (ComponentScan) configClass.getAnnotation(ComponentScan.class);
            //获取扫描路径
            String path = configClassAnnotation.value();
            //把获取到扫描路径中的.替换为/
            path = path.replace(".", "/");
            //获取BeihuiApplicationContext的ClassLoader类加载器
            ClassLoader classLoader = BeihuiApplicationContext.class.getClassLoader();
            //获取到url地址
            URL resource = classLoader.getResource(path);
            //获取到想要到目录
            File file = new File(resource.getFile());
            //判断是不是一个目录
            if (file.isDirectory()) {
                //获取文件夹下面的所有文件并且遍历
                for (File f : file.listFiles()) {
                    //获取文件路径
                    String absolutePath = f.getAbsolutePath();
                    //把absolutePath文件路径替换为类加载文件路径的格式
                    absolutePath = absolutePath.substring(absolutePath.indexOf("classes/") + 8, absolutePath.indexOf(".class"));
                    absolutePath = absolutePath.replace("/", ".");
                    try {
                        //把文件f加载为一个类文件
                        Class<?> clazz = classLoader.loadClass(absolutePath);
                        //判断当前的clazz类上是否包含Component注解
                        if (clazz.isAnnotationPresent(Component.class)) {

                            //判断当前类是否实现了BeanPostProcessor接口
                            if (BeanPostProcessor.class.isAssignableFrom(clazz)) {
                                BeanPostProcessor instance = (BeanPostProcessor) clazz.getConstructor().newInstance();
                                beanPostProcessorList.add(instance);
                            }
                            //获取bean名称
                            String beanName = clazz.getAnnotation(Component.class).value();
                            //如果beanName为空
                            if (null == beanName || "".equals(beanName)) {
                                //获取当前类的类名
                                beanName = absolutePath.substring(absolutePath.lastIndexOf(".") + 1);
                                //把当前类名首字母小写
                                beanName = beanName.substring(0, 1).toLowerCase() + beanName.substring(1);
                            }
                            //创建bean定义
                            BeanDefinition beanDefinition = new BeanDefinition();
                            //设置type属性
                            beanDefinition.setType(clazz);
                            //判断当前Bean是不是单例的
                            if (clazz.isAnnotationPresent(Scope.class)) {
                                //获取当前类上Scope注解
                                Scope scope = clazz.getAnnotation(Scope.class);
                                //获取Scope注解上的值
                                String value = scope.value();
                                //设置bean的作用域
                                beanDefinition.setScope(value);
                            } else {
                                //单例,设置bean的作用域
                                beanDefinition.setScope("singleton");
                            }
                            beanDefinitionMap.put(beanName, beanDefinition);
                        }
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException(e);
                    } catch (InstantiationException e) {
                        throw new RuntimeException(e);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    } catch (NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
}
