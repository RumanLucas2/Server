package org.example;

import java.io.*;
import java.net.Socket;


public class Cliente {

    public static final File path = new File("Arquivo.json");
    public static final String HOST_PADRAO = "localhost";
    public static final int PORTA_PADRAO = 8080;
    private static Parceiro servidor = null;
    private static Parceiro saida = null;


    public static void main(String[] args) throws IOException {
        if (args.length > 2) {
            System.err.println("Uso esperado: java Cliente [HOST [PORTA]]\n");
            return;
        }
        Socket conn = null;
        Socket conexao = null;
        try {
            String host = Cliente.HOST_PADRAO;
            int porta = Cliente.PORTA_PADRAO;

            if (args.length > 0)
                host = args[0];

            if (args.length == 2)
                porta = Integer.parseInt(args[1]);

            conexao = new Socket(host, porta);
            conn = new Socket(host, porta+1);
        } catch (Exception erro) {
            System.err.println("Indique o servidor e a porta corretos!\n");
            return;
        }


        try {
            servidor = new Parceiro(conexao, new ObjectInputStream(conexao.getInputStream()), null); //apenas recebe
            saida = new Parceiro(conn, null, new ObjectOutputStream(conn.getOutputStream()));// apenas envia

        } catch (Exception erro) {
            System.err.println("Indique o servidor e a porta corretos!\n");
            return;
        }
        try {
            while (conexao.isConnected()) {
                // recebendo automaticamente dados do (sensor)
                String command = servidor.receba();

                if (command.equals("status")) {
                    saida.envie("Online");
                } else {
                    testCommands(command);
                }
            }
        } catch (Exception erro) {
            System.err.println(erro.getMessage());
        }
    }

    public static void testCommands(String cmd){
        cmd = cmd.replace("DB ", "");
        String[] command = cmd.split(" ");
        if(DB.exists(command[0])){
            try {
                saida.envie(DB.valueOf(command[0]).execute(cmd.replace(command[0]+" ", "")).toString());
            }catch (Exception e){
                System.err.println(e.getMessage());
            }
        }
    }
}

