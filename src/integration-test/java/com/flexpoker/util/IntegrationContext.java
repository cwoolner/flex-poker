package com.flexpoker.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class IntegrationContext {

    private static final ApplicationContext instance =
            new ClassPathXmlApplicationContext("classpath*:integrationTestApplicationContext.xml");

    private IntegrationContext() {}

    public static ApplicationContext instance() {
        return instance;
    }

}
