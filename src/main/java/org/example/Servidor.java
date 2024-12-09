package org.example;

import java.util.ArrayList;


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


    /**
     *Inicializa a função {@link Servidor main } do servidor
     * @param args
     */
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

    /**
     *
     *
     */
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
}
