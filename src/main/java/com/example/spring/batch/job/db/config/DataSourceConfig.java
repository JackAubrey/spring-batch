package com.example.spring.batch.job.db.config;

import com.example.spring.batch.job.model.Cliente;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
    @Bean("BatchDataSourceProperties")
    @ConfigurationProperties("spring.datasource.batch")
    public DataSourceProperties batchDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean("BatchDataSource")
    @Primary
    public DataSource batchDataSource(
            @Qualifier("BatchDataSourceProperties")DataSourceProperties batchProperties
    ) {
        System.out.println("BatchDataSource initializing with properties "+batchProperties.getUrl());
        return batchProperties.initializeDataSourceBuilder().build();
    }

    @Bean("CustomersDataSourceProperties")
    @ConfigurationProperties("spring.datasource.customers")
    public DataSourceProperties customersDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean("CustomersDataSource")
    public DataSource customersDataSource(
            @Qualifier("CustomersDataSourceProperties")DataSourceProperties customersProperties
    ) {
        System.out.println("CustomersDataSource initializing with properties "+customersProperties.getUrl());
        return customersProperties.initializeDataSourceBuilder().build();
    }
}
