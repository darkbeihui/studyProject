package com.beihui.beihui.service;

import com.beihui.spring.Component;
import com.beihui.spring.Scope;

/**
 * @ClassName: UserService
 * @description:
 * @author: 北慧
 * @date: 2023/2/9 23:06
 * @Version: 1.0
 **/
@Component
@Scope("prototype")
public class OrderService {

    public void test() {
        System.out.println("beihui");
    }

}
