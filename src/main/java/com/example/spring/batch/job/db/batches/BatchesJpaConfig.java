package com.example.spring.batch.job.db.batches;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "org.springframework.batch.core",
        entityManagerFactoryRef = "batchEntityManagerFactory",
        transactionManagerRef = "batchTransactionManager"
)
public class BatchesJpaConfig {
    @Bean("batchEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean batchEntityManagerFactory(
            @Qualifier("BatchDataSource") DataSource dataSource,
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(dataSource)
                .packages("org.springframework.batch.core")
                .build();
    }

    @Bean("batchTransactionManager")
    @Primary
    public PlatformTransactionManager batchTransactionManager(
            @Qualifier("batchEntityManagerFactory") LocalContainerEntityManagerFactoryBean batchEntityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(batchEntityManagerFactory.getObject()));
    }
}
