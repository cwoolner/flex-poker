package com.flexpoker.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Context {

    private static final ApplicationContext instance =
            new ClassPathXmlApplicationContext("classpath*:testApplicationContext.xml");

    private Context() {}

    public static ApplicationContext instance() {
        return instance;
    }

}
