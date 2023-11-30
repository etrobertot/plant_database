package com.etrobertot.plant;

public class Plant {
    String key, nombre, tipo, especie, cuidado, estado;

    public Plant(){

    }

    public String getKey() {
        return key;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public String getEspecie() {
        return especie;
    }

    public String getCuidado() {
        return cuidado;
    }

    public String getEstado() {
        return estado;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public void setCuidado(String cuidado) {
        this.cuidado = cuidado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
