package org.example;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;


public class Cliente {

    public static final File path = new File("Arquivo.json");
    public static final String HOST_PADRAO = "localhost";
    public static final int PORTA_PADRAO = 8080;
    private static Parceiro servidor = null;
    private static Parceiro saida = null;


    /**
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        if (args.length > 2) {
            System.err.println("Uso esperado: java Cliente [HOST [PORTA]]\n");
            return;
        }
        //Socket conn;//8081
        Socket conexao;//8080
        try {
            String host = Cliente.HOST_PADRAO;
            int porta = Cliente.PORTA_PADRAO;

            if (args.length > 0)
                host = args[0];

            if (args.length == 2)
                porta = Integer.parseInt(args[1]);

            conexao = new Socket(host, porta);

        } catch (Exception erro) {
            System.err.println("Indique o servidor e a porta corretos em conexao!\n");
            return;
        }



        try {
            servidor = new Parceiro(conexao, new ObjectInputStream(conexao.getInputStream()),  new ObjectOutputStream(conexao.getOutputStream())); //apenas recebe
        } catch (Exception erro) {
            System.err.println("Indique o servidor e a porta corretos!\n");
            return;
        }
        try {
            while (conexao.isConnected()) {
                if (!Commands.getCommands().isEmpty()) {
                    String cmd = Commands.getTop();
                    Commands.remove(0);
                    if (cmd.equals("end")) return;
                    send(cmd, servidor);
                    CompletableFuture.runAsync(() ->
                    {
                        try {
                            System.out.println("\nServer Response: { "+ servidor.receba()+"}");
                        }catch (Exception ex){
                            System.out.println("\nServer Bad Response: "+ ex);
                        }
                    });
                }
                Commands.add(Teclado.getUmString());
        }
    } catch (Exception erro) {
            System.err.println(erro.getMessage());
        }
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
        }catch (Exception e){
            System.err.println("Dado nao enviado");
        }
    }
}

