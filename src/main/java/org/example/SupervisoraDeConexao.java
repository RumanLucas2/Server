package org.example;

import com.sun.source.util.TaskEvent;
import com.sun.source.util.TaskListener;

import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.StandardSocketOptions;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;


public class SupervisoraDeConexao extends Thread {

    private Parceiro usuario;
    private final Socket conexao;
    private final ArrayList<Parceiro> usuarios;



    public static void addCommands(String value){
        Commands.add(value);
    }

    public SupervisoraDeConexao(Socket conexao, ArrayList<Parceiro> usuarios) throws Exception {
        if (conexao == null) throw new Exception("Conexao ausente");

        if (usuarios == null) throw new Exception("Usuarios ausentes");

        this.conexao = conexao;
        this.usuarios = usuarios;
    }

    @Override
    public void run() {

        ObjectOutputStream transmissor;
        try {
            transmissor = new ObjectOutputStream(this.conexao.getOutputStream());
        } catch (Exception erro) {
            return;
        }

        ObjectInputStream receptor = null;
        try {
            receptor = new ObjectInputStream(this.conexao.getInputStream());
        } catch (Exception err0) {
            try {
                transmissor.close();
            } catch (Exception falha) {
                System.err.println("Falha total Transmissor");
            }
        }
        assert receptor != null;
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
                if (!Commands.getCommands().isEmpty()) {
                    String cmd = Commands.getTop();
                    Commands.remove(0);
                    if (cmd.equals("end")) return;
                    Servidor.send(cmd, this.usuario);
                }
                new Sensor();
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
