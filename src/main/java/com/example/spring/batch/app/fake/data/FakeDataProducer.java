package com.example.spring.batch.app.fake.data;

import com.example.spring.batch.app.AppRuntimeException;
import com.example.spring.batch.app.model.Sales;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FakeDataProducer {
    public static void main(String [] args) {
        new FakeDataProducer().initFakeData();
    }

    private FakeDataProducer() {
        super();
    }

    private void initFakeData() {
        int size = 100;
        Path res = Paths.get("/home/dcividin/git/courses/spring-batch/external_resources/input/upload.json");
        Faker faker = new Faker();
        Date fromDate = Date.from(LocalDate.of(2022, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date toDate = Date.from(LocalDate.of(2023, 7, 31).atStartOfDay(ZoneId.systemDefault()).toInstant());
        List<Sales> salesList = new ArrayList<>(size);

        for(int i=0; i<size; i++) {
            Sales sales = new Sales();

            sales.setDate( faker.date().between(fromDate, toDate) );
            sales.setName( faker.name().fullName() );
            sales.setCode( ""+faker.number().numberBetween(67000000, 67199999) );
            sales.setPoints( faker.number().numberBetween(0, 30000) );
            sales.setCost( faker.number().randomDouble(2, 5, 500) );
            sales.setMobile( sales.getCost() > 100 );
            salesList.add(sales);
        }

        try (FileWriter writer = new FileWriter(res.toFile()) ) {
            new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(writer, salesList);
        } catch (IOException e) {
            throw new AppRuntimeException(e);
        }
    }
}
