package com.example.spring.batch.job.db.mapper;

import com.example.spring.batch.job.db.customers.Customer;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerRowMapper implements RowMapper<Customer> {

    @Override
    public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
        Customer customer = new Customer();
        customer.setCodfid(rs.getString("codfid"));
        customer.setBollini(rs.getInt("bollini"));
        customer.setComune(rs.getString("comune"));
        customer.setNominativo(rs.getString("nominativo"));
        customer.setStato(rs.getInt("stato"));
        return customer;
    }
}
