package com.example.spring.batch.job;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component("SimpleItemReader")
public class SimpleItemReader implements ItemReader<String> {
    private List<String> dataSet = new ArrayList<>();

    private Iterator<String> iterator;

    private SimpleItemReader() {
        dataSet.add("Nicola");
        dataSet.add("Luigi");
        dataSet.add("Marco");
        dataSet.add("Alberto");
        dataSet.add("Luca");
        dataSet.add("Andrea");
        dataSet.add("Federico");

        iterator = dataSet.iterator();
    }

    @Override
    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        return iterator.hasNext() ? iterator.next() : null;
    }
}
