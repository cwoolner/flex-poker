package com.flexpoker.config;

import javax.inject.Inject;

import org.quartz.Trigger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

import com.flexpoker.controller.ActionOnTimerController;

@Configuration
@ComponentScan(basePackages = "com.flexpoker")
public class TimerConfig {

    @Inject
    private ActionOnTimerController actionOnTimerController;
    
    @Inject
    private MethodInvokingJobDetailFactoryBean actionOnTimerJob;
    
    @Bean
    public MethodInvokingJobDetailFactoryBean getActionOnTimerJob() {
        MethodInvokingJobDetailFactoryBean jobDetailFactoryBean = new MethodInvokingJobDetailFactoryBean();
        jobDetailFactoryBean.setTargetObject(actionOnTimerController);
        jobDetailFactoryBean.setTargetMethod("decrementTime");
        return jobDetailFactoryBean;
    }
    
    @Bean
    public SimpleTriggerFactoryBean getActionOnTimerTrigger() {
        SimpleTriggerFactoryBean triggerFactoryBean = new SimpleTriggerFactoryBean();
        triggerFactoryBean.setJobDetail(actionOnTimerJob.getObject());
        triggerFactoryBean.setRepeatInterval(1000);
        return triggerFactoryBean;
    }
    
    @Bean
    public SchedulerFactoryBean schedulerFactory() {
        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
        schedulerFactory.setTriggers(new Trigger[]{ getActionOnTimerTrigger().getObject() });
        return schedulerFactory;
    }

}
