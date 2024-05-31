package org.example;

import java.io.Serializable;

public class DadoDoSensor extends Comunicado implements Serializable {
    private Sensor sensor;
    private double dado;

    public DadoDoSensor(Sensor sensor, double dado) {
        this.sensor = sensor;
        this.dado = dado;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public double getDado() {
        return dado;
    }
}
