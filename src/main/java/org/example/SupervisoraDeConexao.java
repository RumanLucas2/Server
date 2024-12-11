package org.example;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;


public class SupervisoraDeConexao extends Thread {

    private Parceiro usuario;
    private final Socket conexao;
    private final ArrayList<Parceiro> usuarios;

    public SupervisoraDeConexao(Socket conexao, ArrayList<Parceiro> usuarios) throws Exception {
        if (conexao == null) throw new Exception("Conexao ausente");

        if (usuarios == null) throw new Exception("Usuarios ausentes");

        this.conexao = conexao;
        this.usuarios = usuarios;
    }

    /**
     * Pode ser chamada pelo Start()
     */
    @Override
    public void run() {

        ObjectOutputStream transmissor;
        try {
            transmissor = new ObjectOutputStream(this.conexao.getOutputStream());// manda pela porta
        } catch (Exception erro) {
            return;
        }

        ObjectInputStream receptor;
        try {
            receptor = new ObjectInputStream(this.conexao.getInputStream()); // recebe pela porta
        } catch (Exception err0) {
            try {
                transmissor.close();
            } catch (Exception falha) {
                System.err.println("Falha total Transmissor");
                throw new RuntimeException(falha);
            }
            throw new RuntimeException(err0);
        }
        try {
            this.usuario = new Parceiro(this.conexao, receptor, transmissor);
        } catch (Exception erro) {
            System.err.println(erro.getMessage());
        }
        try {
            synchronized (this.usuarios) {
                this.usuarios.add(this.usuario);
            }
            System.out.println("System started");

            for (; ; ) {
                new Sensor(this.usuario);
            }
        } catch (Exception erro) {
            try {
                transmissor.close();
                receptor.close();
            } catch (Exception falha) {
                throw new RuntimeException(falha);
            }
        }
    }
}
