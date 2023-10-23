package com.example.spring.batch.job.db.customers;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class CustomerDataSourceConfig {
    @Bean("CustomersDataSourceProperties")
    @ConfigurationProperties("spring.datasource.customers")
    public DataSourceProperties customersDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean("CustomersDataSource")
    @ConfigurationProperties("spring.datasource.customers")
    public DataSource customersDataSource(
            @Qualifier("CustomersDataSourceProperties")DataSourceProperties customersProperties
    ) {
        return customersProperties.initializeDataSourceBuilder().build();
    }

    @Bean("customerJdbcTemplate")
    public JdbcTemplate customerJdbcTemplate(@Qualifier("CustomersDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
