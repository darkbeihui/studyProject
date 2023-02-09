package com.beihui.beihui;

import com.beihui.beihui.service.UserInterface;
import com.beihui.spring.BeihuiApplicationContext;

/**
 * @ClassName: Test
 * @description:
 * @author: 北慧
 * @date: 2023/2/9 23:07
 * @Version: 1.0
 **/
public class Test {

    public static void main(String[] args) {

        //扫描-->创建单例Bean
        BeihuiApplicationContext applicationContext=new BeihuiApplicationContext(AppConfig.class);

        UserInterface userService = (UserInterface) applicationContext.getBean("userService");

        userService.test();
    }
}
