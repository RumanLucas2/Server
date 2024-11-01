package org.example;

import java.io.Serializable;

public class DadoDoSensor extends Comunicado implements Serializable {
    private String dado;

    public DadoDoSensor(String dado) {
        this.dado = dado;
    }


    public String getDado() {
        return dado;
    }
}
