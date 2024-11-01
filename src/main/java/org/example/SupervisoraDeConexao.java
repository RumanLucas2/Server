package org.example;

import com.sun.source.util.TaskEvent;
import com.sun.source.util.TaskListener;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.StandardSocketOptions;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;


public class SupervisoraDeConexao extends Thread {
    private Parceiro usuario;
    private final Socket conexao;
    private final ArrayList<Parceiro> usuarios;
    private static ArrayList<String> Commands;


    public static void addCommands(String value){
        Commands.add(value);
    }

    public SupervisoraDeConexao(Socket conexao, ArrayList<Parceiro> usuarios) throws Exception {
        if (conexao == null)
            throw new Exception("Conexao ausente");

        if (usuarios == null)
            throw new Exception("Usuarios ausentes");

        this.conexao = conexao;
        this.usuarios = usuarios;
        Commands = new ArrayList<String>();
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
                System.err.println("Falha total");
            }

            return;
        }

        try {
            this.usuario = new Parceiro(this.conexao, receptor, transmissor);
        } catch (Exception erro) {
            System.err.println("erro no parceiro");
        }
        try {
            synchronized (this.usuarios) {
                this.usuarios.add(this.usuario);
            }
            for (; ; ) {
                CompletableFuture<Void> assyncVar = CompletableFuture.runAsync(() -> {
                    if (!Commands.get(0).isEmpty()){
                        String cmd = Commands.get(0);
                        Commands.remove(0);
                        assyncMethod(cmd);
                    }
                });
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


    private void assyncMethod(String cmd){
            try {
                command(cmd);
            } catch (Exception e) {
                System.err.println("Erro aqui");
            }
            // Espera a conclusão do processo assíncrono
    }

    private void command(String cmd){
        try{
            usuario.envie(cmd);
            Thread.sleep(1000);
            System.out.println(usuario.receba());
        }catch (Exception e){
            System.err.println("Dado nao enviado");
        }
    }
}
