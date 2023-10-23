package com.example.spring.batch.job.db.batches;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class BatchesDataSourceConfig {
    @Bean("BatchDataSourceProperties")
    @ConfigurationProperties("spring.datasource.batch")
    public DataSourceProperties batchDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean("BatchDataSource")
    @Primary
    @ConfigurationProperties("spring.datasource.batch")
    public DataSource batchDataSource(
            @Qualifier("BatchDataSourceProperties")DataSourceProperties batchProperties
    ) {
        return batchProperties.initializeDataSourceBuilder().build();
    }

    @Bean("batchesJdbcTemplate")
    public JdbcTemplate batchesJdbcTemplate(@Qualifier("BatchDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
