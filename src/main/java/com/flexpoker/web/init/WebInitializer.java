package com.flexpoker.web.init;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.flexpoker.config.BsoConfig;
import com.flexpoker.config.DataSourceConfig;
import com.flexpoker.config.RepositoryConfig;
import com.flexpoker.config.SecurityConfig;
import com.flexpoker.config.TimerConfig;
import com.flexpoker.config.WebConfig;

public class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[] { DataSourceConfig.class, RepositoryConfig.class,
                BsoConfig.class, SecurityConfig.class, TimerConfig.class };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[] { WebConfig.class };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }

}
