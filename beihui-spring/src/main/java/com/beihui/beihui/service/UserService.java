package com.beihui.beihui.service;

import com.beihui.spring.Autowired;
import com.beihui.spring.Component;

/**
 * @ClassName: UserService
 * @description:
 * @author: 北慧
 * @date: 2023/2/9 23:06
 * @Version: 1.0
 **/
@Component
public class UserService implements UserInterface {
    @Autowired
    private OrderService orderService;

    public void test() {
        System.out.println(orderService);
    }

}
