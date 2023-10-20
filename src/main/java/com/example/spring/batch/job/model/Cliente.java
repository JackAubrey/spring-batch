package com.example.spring.batch.job.model;

import java.util.Objects;

public class Cliente {
    private String codFid;
    private String name;

    private String points;

    public String getCodFid() {
        return codFid;
    }

    public void setCodFid(String codFid) {
        this.codFid = codFid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "codFid='" + codFid + '\'' +
                ", name='" + name + '\'' +
                ", points='" + points + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(codFid, cliente.codFid) && Objects.equals(name, cliente.name) && Objects.equals(points, cliente.points);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codFid, name, points);
    }
}
