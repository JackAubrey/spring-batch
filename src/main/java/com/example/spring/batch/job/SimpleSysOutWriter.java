package com.example.spring.batch.job;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("SimpleSysOutWriter")
public class SimpleSysOutWriter implements ItemWriter<String> {
    @Override
    public void write(Chunk<? extends String> chunk) throws Exception {
        List<? extends String> list = chunk.getItems();
        int listSize = list == null ? 0 : list.size();
        System.out.println( String.format("Ricevuta una lista di %s elementi", listSize) );

        if(list != null) {
            list.forEach(System.out::println);
        } else  {
            System.err.println("The list is NULL");
        }
    }
}
