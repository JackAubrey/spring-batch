package com.example.spring.batch.job;

import com.example.spring.batch.job.model.Cliente;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("SimpleCustomerSysOutWriter")
public class SimpleClienteSysOutWriter implements ItemWriter<Cliente> {
    @Override
    public void write(Chunk<? extends Cliente> chunk) throws Exception {
        List<? extends Cliente> list = chunk.getItems();
        Integer listSize = (Integer) (list == null ? 0 : list.size());
        System.out.println( String.format("Ricevuta una lista di %s clienti", listSize) );

        if(list != null) {
            list.forEach(System.out::println);
        } else  {
            System.err.println("The list is NULL");
        }
    }
}
