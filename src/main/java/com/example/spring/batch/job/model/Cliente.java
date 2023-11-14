package com.example.spring.batch.job.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.util.Objects;

public class Cliente {
    private String codFid;
    @Size(min = 6, max = 50)
    private String nominativo;
    private String comune;
    private int stato;
    @Min(0)
    @Max(5000)
    private int bollini;

    public String getCodFid() {
        return codFid;
    }

    public void setCodFid(String codFid) {
        this.codFid = codFid;
    }

    public String getNominativo() {
        return nominativo;
    }

    public void setNominativo(String nominativo) {
        this.nominativo = nominativo;
    }

    public String getComune() {
        return comune;
    }

    public void setComune(String comune) {
        this.comune = comune;
    }

    public int getStato() {
        return stato;
    }

    public void setStato(int stato) {
        this.stato = stato;
    }

    public int getBollini() {
        return bollini;
    }

    public void setBollini(int bollini) {
        this.bollini = bollini;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "codFid='" + codFid + '\'' +
                ", nominativo='" + nominativo + '\'' +
                ", comune='" + comune + '\'' +
                ", stato=" + stato +
                ", bollini=" + bollini +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return stato == cliente.stato && bollini == cliente.bollini && Objects.equals(codFid, cliente.codFid) && Objects.equals(nominativo, cliente.nominativo) && Objects.equals(comune, cliente.comune);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codFid, nominativo, comune, stato, bollini);
    }
}
