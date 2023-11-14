package com.example.spring.batch.job.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.util.Objects;

public class Cliente2 {
    private String codFid;
    @Size(min = 6, max = 50)
    private String nominativo;
    private String comune;
    private String stato;
    private String tipo;
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

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getBollini() {
        return bollini;
    }

    public void setBollini(int bollini) {
        this.bollini = bollini;
    }


    @Override
    public String toString() {
        return "Cliente2{" +
                "codFid='" + codFid + '\'' +
                ", nominativo='" + nominativo + '\'' +
                ", comune='" + comune + '\'' +
                ", stato='" + stato + '\'' +
                ", tipo='" + tipo + '\'' +
                ", bollini=" + bollini +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente2 cliente2 = (Cliente2) o;
        return bollini == cliente2.bollini && Objects.equals(codFid, cliente2.codFid) && Objects.equals(nominativo, cliente2.nominativo) && Objects.equals(comune, cliente2.comune) && Objects.equals(stato, cliente2.stato) && Objects.equals(tipo, cliente2.tipo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codFid, nominativo, comune, stato, tipo, bollini);
    }
}
