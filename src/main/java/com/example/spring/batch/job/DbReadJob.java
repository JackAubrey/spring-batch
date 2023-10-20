package com.example.spring.batch.job;

import com.example.spring.batch.job.db.mapper.ClienteRowMapper;
import com.example.spring.batch.job.model.Cliente;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.*;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.List;

@Configuration
public class DbReadJob {
    private static final String [] tokens = {"codfid", "nominativo", "comune", "stato", "bollini"};
    private static final String SQL_SELECT_CLIENTI = """
            SELECT 
                c.id, 
                c.codfid, 
                c.nominativo, 
                c.comune, 
                c.stato, 
                c.bollini 
            FROM 
                ClientiDataSet.CLIENTI as c
            where 
                c.comune = ?
            """;

    private static final String SQL_CALL_SP_GET_CLIENTE = "call ClientiDataSet.GetCustomerByComune(?);";

    @Bean("DbItemReader")
    public ItemReader<Cliente> dbItemReader(
            @Qualifier("CustomersDataSource")DataSource customerDataSource
    ) {
        return new JdbcCursorItemReaderBuilder<Cliente>()
                .name("customer-cursor-item-reader")
                .dataSource(customerDataSource)
                .sql(SQL_CALL_SP_GET_CLIENTE)
                .rowMapper(new ClienteRowMapper())
                .preparedStatementSetter(comuneSetter(null))
                .build();
    }

    @Bean
    @StepScope
    public ArgumentPreparedStatementSetter comuneSetter(
            @Value("#{jobParameters['comune']}") String comune
    ) {
        System.out.println("Comune = "+comune);
        return new ArgumentPreparedStatementSetter(new Object[]{comune});
    }

    @Bean("SysOutItemWriter")
    public ItemWriter<Cliente> sysOutItemWriter () {
        return new ItemWriter<Cliente>() {
            @Override
            public void write(Chunk<? extends Cliente> chunk) throws Exception {
                List<? extends Cliente> customers = chunk.getItems();
                customers.forEach(System.out::println);
            }
        };
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
                                     @Qualifier("SysOutItemWriter") ItemWriter<Cliente> itemWriter) {
        return new StepBuilder("db-read-chunk-based-step", jobRepository)
                .<Cliente, Cliente>chunk(3, transactionManager)
                .reader(itemReader)
                .writer(itemWriter)
                .build();
    }
}
