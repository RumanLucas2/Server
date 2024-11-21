package org.example;

import jdk.jshell.Snippet;

import java.awt.desktop.AppEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;


public class Servidor {

    private static ArrayList<Parceiro> usuarios = new ArrayList<>();
    private static String[] Args;
    private static String Porta;

    public static ArrayList<Parceiro> getUsers() {
        return usuarios;
    }

    public static String getPorta() {
        return Porta;
    }

    public static String[] getArgs()
    {
        return Args;
    }

    public static void main(String[] args) {
        // Inicializa algo de forma assíncrona
        Porta = "8080";
        Args = args;
        Start();
    }

    public static void Terminate(){
        for (Parceiro usuario : usuarios) {
            try {
                usuario.Terminate();
                System.out.println("Server Off");
            } catch (Exception e) {
                System.err.println(e);
                throw new RuntimeException(e);
            }
        }
    }
    public static void Start(){
        if(!Servidor.usuarios.isEmpty()){
            Servidor.usuarios = new ArrayList<>();
        }
        String[] args = Servidor.getArgs();
        String porta = Servidor.getPorta();
        if (args.length > 1) {
            System.err.println("Uso esperado: java Servidor [PORTA]\n");
        }

        if (args.length == 1) porta = args[0];
        System.out.println(porta);
        AceitadoraDeConexao aceitadoraDeConexao = null;
        try {
            aceitadoraDeConexao = new AceitadoraDeConexao(porta, Servidor.usuarios);
        } catch (Exception error) {
            System.err.println(error.toString());
        }
        assert aceitadoraDeConexao != null;
        aceitadoraDeConexao.start();
        System.out.println("Server Started");
    }

    public static void send(String cmd, Parceiro usr){
        try {
            command(cmd, usr);
        } catch (Exception e) {
            System.err.println("Erro aqui");
        }
    }

    private static void command(String cmd, Parceiro usr){
        try{
            usr.envie(cmd);
            System.out.println(usr.receba());
        }catch (Exception e){
            System.err.println("Dado nao enviado");
        }
    }
}
