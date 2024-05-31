package org.example;

import java.io.Serializable;

public class Sensor implements Serializable {
    private String nome;

    public Sensor(String nome) {
        this.nome = nome;
    }

    public double lerDado() {
        //dados aleatórios, range 0 - 100
        return Math.random() * 100;
    }

    public String getNome() {
        return nome;
    }
}
