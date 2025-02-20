package org.entdes.todolist;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

public class Tasca {
    private int id;
    private boolean completada = false;
    private String descripcio;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataInici;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataFiPrevista;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataFiReal;

    private Integer prioritat;

    private static int idCounter = 0;

    public Tasca(String descripcio) {
        this.id = ++idCounter;
        this.descripcio = descripcio;
    }

    public int getId() {
        return id;
    }

    public boolean isCompletada() {
        return completada;
    }

    public void setCompletada(boolean completada) {
        this.completada = completada;
    }

    public String getDescripcio() {
        return descripcio;
    }

    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }

    public LocalDate getDataInici() {
        return dataInici;
    }

    public void setDataInici(LocalDate dataInici) {
        this.dataInici = dataInici;
    }

    public LocalDate getDataFiPrevista() {
        return dataFiPrevista;
    }

    public void setDataFiPrevista(LocalDate dataFi) {
        this.dataFiPrevista = dataFi;
    }

    public Integer getPrioritat() {
        return prioritat;
    }

    public void setPrioritat(Integer prioritat) {
        this.prioritat = prioritat;
    }

    public LocalDate getDataFiReal() {
        return dataFiReal;
    }

    public void setDataFiReal(LocalDate dataFiReal) {
        this.dataFiReal = dataFiReal;
    }

    @Override
    public String toString() {
        return descripcio + ": " + (completada ? "Completada" : "Pendent");            
    }
}