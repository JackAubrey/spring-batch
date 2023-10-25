package com.example.spring.batch.job;

import com.example.spring.batch.job.db.mapper.ClienteRowMapper;
import com.example.spring.batch.job.model.Cliente;
import org.slf4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

@Configuration
public class DbReadJob {
    private static final Logger logger = getLogger(DbReadJob.class);
    private static final int PAGE_SIZE = 5;

    @Bean("DbItemReader")
    @JobScope
    public ItemReader<Cliente> dbItemReader(
            @Qualifier("CustomersDataSource")DataSource customerDataSource,
            @Qualifier("QueryProvider")PagingQueryProvider queryProvider,
            @Value("#{jobParameters['comune']}") String comune
            ) {
        Map<String, Object> params = comune == null ? new HashMap<>() : Map.of("comune", comune);

        return new JdbcPagingItemReaderBuilder<Cliente>()
                .name("customer-cursor-item-reader")
                .dataSource(customerDataSource)
                .queryProvider(queryProvider)
                .pageSize(PAGE_SIZE)
                .rowMapper(new ClienteRowMapper())
                .parameterValues(params)
                //.preparedStatementSetter(comuneSetter(null))
                .build();
    }

    @Bean("QueryProvider")
    @JobScope
    public PagingQueryProvider pagingQueryProvider(@Qualifier("CustomersDataSource")DataSource customerDataSource,
                                                   @Value("#{jobParameters['comune']}") String comune) {
        SqlPagingQueryProviderFactoryBean sqlPagingQueryProviderFactoryBean = new SqlPagingQueryProviderFactoryBean();
        sqlPagingQueryProviderFactoryBean.setSelectClause("SELECT c.id, c.codfid, c.nominativo, c.comune, c.stato, c.bollini");
        sqlPagingQueryProviderFactoryBean.setFromClause("FROM ClientiDataSet.CLIENTI as c");

        if( comune != null) {
            sqlPagingQueryProviderFactoryBean.setWhereClause("WHERE c.comune = :comune");
        }

        sqlPagingQueryProviderFactoryBean.setSortKey("nominativo");
        sqlPagingQueryProviderFactoryBean.setDataSource(customerDataSource);
        try {
            return sqlPagingQueryProviderFactoryBean.getObject();
        } catch (Exception e) {
            throw new DbReadJobRuntimeException("Query Provider Exception: ", e);
        }
    }

    @Bean
    @JobScope
    public ArgumentPreparedStatementSetter comuneSetter(
            @Value("#{jobParameters['comune']}") String comune
    ) {
        logger.info("Comune = {} ", comune);
        return new ArgumentPreparedStatementSetter(new Object[]{comune});
    }

    @Bean("FileItemWriter")
    public ItemWriter<Cliente> sysOutItemWriter () {
        return new FlatFileItemWriterBuilder<Cliente>()
                .name("CustomersCSVWriter")
                .resource(new FileSystemResource("/home/dcividin/git/courses/spring-batch/external_resources/output/customers.csv"))
                .delimited()
                .delimiter(",")
                .names( "codFid", "nominativo", "comune", "bollini", "stato" )
                .headerCallback(c -> {
                    c.write("COD_FID,");
                    c.write("NAME,");
                    c.write("COUNTRY,");
                    c.write("POINTS,");
                    c.write("STATUS");
                }).build();
    }

    @Bean("DbReadJob")
    public Job dbReadsJob(JobRepository jobRepository,
                          @Qualifier("DbReadChunkBasedStep")Step dbReadChunkBasedStep) {
        return new JobBuilder("db-read-job", jobRepository)
                .start(dbReadChunkBasedStep)
                .build();
    }

    @Bean("DbReadChunkBasedStep")
    public Step dbReadChunkBasedStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                                     @Qualifier("DbItemReader") ItemReader<Cliente> itemReader,
                                     @Qualifier("FileItemWriter") ItemWriter<Cliente> itemWriter) {
        return new StepBuilder("db-read-chunk-based-step", jobRepository)
                .<Cliente, Cliente>chunk(PAGE_SIZE, transactionManager)
                .reader(itemReader)
                .writer(itemWriter)
                .build();
    }
}
