package com.example.spring.batch.job.db.customers;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "CLIENTI")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String codfid;
    private String nominativo;
    private String comune;
    private int stato;
    private int bollini;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodfid() {
        return codfid;
    }

    public void setCodfid(String codfid) {
        this.codfid = codfid;
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
        return "Customer{" +
                "id=" + id +
                ", codfid='" + codfid + '\'' +
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
        Customer customer = (Customer) o;
        return stato == customer.stato && bollini == customer.bollini && Objects.equals(id, customer.id) && Objects.equals(codfid, customer.codfid) && Objects.equals(nominativo, customer.nominativo) && Objects.equals(comune, customer.comune);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, codfid, nominativo, comune, stato, bollini);
    }
}
