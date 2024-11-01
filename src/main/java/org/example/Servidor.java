package org.example;

import jdk.jshell.Snippet;

import java.awt.desktop.AppEvent;
import java.util.ArrayList;
import java.util.Arrays;



public class Servidor {
    private static boolean status = false;

    public static boolean getStatus(){
        return status;
    }
    private static final ArrayList<Parceiro> usuarios = new ArrayList<>();
    private static String[] Args;
    private static String Porta;

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
        synchronized (usuarios) {
            for (Parceiro usuario : usuarios) {
                try {
                    usuario.adeus();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("Server Off");

        }
    }
    public static void Start(){
        String[] args = Servidor.getArgs();
        String porta = Servidor.getPorta();
        if (args.length > 1) {
            System.err.println("Uso esperado: java Servidor [PORTA]\n");
        }

        if (args.length == 1) porta = args[0];

        AceitadoraDeConexao aceitadoraDeConexao = null;
        try {
            aceitadoraDeConexao = new AceitadoraDeConexao(porta, Servidor.usuarios);
        } catch (Exception error) {
            System.err.println("Escolha uma porta apropriada e liberada para uso!\n");
        }
        assert aceitadoraDeConexao != null;
        aceitadoraDeConexao.start();
        Servidor.status = true;
        System.out.println("Server Started");
    }
}
