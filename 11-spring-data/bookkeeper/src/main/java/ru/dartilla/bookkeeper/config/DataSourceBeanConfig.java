package ru.dartilla.bookkeeper.config;

import lombok.extern.slf4j.Slf4j;
import net.ttddyy.dsproxy.listener.logging.SystemOutQueryLoggingListener;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Slf4j
@Configuration
public class DataSourceBeanConfig implements BeanPostProcessor {

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof DataSource) {
            log.info("Inside Proxy Creation");
            return ProxyDataSourceBuilder.create((DataSource) bean).name("MyDS")
                    .logQueryBySlf4j().build();
        }
        return bean;
    }
}