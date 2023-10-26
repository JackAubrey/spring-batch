package com.example.spring.batch.job.db.mapper;

import com.example.spring.batch.job.model.Cliente;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerRowMapper implements RowMapper<Cliente> {

    @Override
    public Cliente mapRow(ResultSet rs, int rowNum) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setCodFid(rs.getString("codfid"));
        cliente.setBollini(rs.getInt("bollini"));
        cliente.setComune(rs.getString("comune"));
        cliente.setNominativo(rs.getString("nominativo"));
        cliente.setStato(rs.getInt("stato"));
        return cliente;
    }
}
