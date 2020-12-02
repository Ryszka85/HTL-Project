package com.ryszka.imageRestApi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;



@EnableTransactionManagement
public class TransactionManagerConfig {
    @Bean
    public PlatformTransactionManager txManager() { return new DataSourceTransactionManager(); }
}
