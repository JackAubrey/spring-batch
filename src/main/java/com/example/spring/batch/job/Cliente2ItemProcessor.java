package com.example.spring.batch.job;

import com.example.spring.batch.job.model.Cliente;
import com.example.spring.batch.job.model.Cliente2;
import org.springframework.batch.item.ItemProcessor;

public class Cliente2ItemProcessor implements ItemProcessor<Cliente, Cliente2> {
    @Override
    public Cliente2 process(Cliente item) throws Exception {
        Cliente2 cliente2 = new Cliente2();

        if( item.getCodFid().trim().length() != 8) {
            throw new CodeNotValidException("Ricevuto codice cliente non valido: "+item.getCodFid());
        }

        cliente2.setBollini(item.getBollini() < 0 ? 0 : item.getBollini());
        cliente2.setNominativo(item.getNominativo().toLowerCase().trim());
        cliente2.setCodFid(item.getCodFid().toLowerCase().trim());
        cliente2.setComune(item.getComune().toLowerCase().trim());
        cliente2.setStato(item.getStato() == 0 ? "Sospeso" : "Attivo");
        cliente2.setTipo(item.getCodFid().substring(0, 3).equals("671") ? "Standard" : "Mobile");
        return cliente2;
    }
}
