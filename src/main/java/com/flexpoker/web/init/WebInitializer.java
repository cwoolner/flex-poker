package com.flexpoker.web.init;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;

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

    @Override
    protected void customizeRegistration(Dynamic registration) {
        registration.setInitParameter("dispatchOptionsRequest", "true");
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        servletContext.setInitParameter("spring.profiles.active", "simple-broker");
        //servletContext.setInitParameter("spring.profiles.active", "stomp-broker-relay");
        super.onStartup(servletContext);
    }

}
