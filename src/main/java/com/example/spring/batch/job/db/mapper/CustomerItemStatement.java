package com.example.spring.batch.job.db.mapper;

import com.example.spring.batch.job.model.Cliente;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CustomerItemStatement implements ItemPreparedStatementSetter<Cliente> {
    @Override
    public void setValues(Cliente item, PreparedStatement ps) throws SQLException {
        // CodFid, Nominativo, Comune, Stato, Bollini
        ps.setString(1, item.getCodFid());
        ps.setString(2, item.getNominativo());
        ps.setString(3, item.getComune());
        ps.setInt(4, item.getStato());
        ps.setInt(5, item.getBollini());
    }
}
