package org.example;

import java.io.*;
import java.net.Socket;


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

//        try {
//            String host = Cliente.HOST_PADRAO;
//            int porta = Cliente.PORTA_PADRAO+1;
//
//            if (args.length > 0)
//                host = args[0];
//
//            if (args.length == 2)
//                porta = Integer.parseInt(args[1]);
//
//            //conn = new Socket(host, porta+1);
//
//        } catch (Exception erro) {
//            System.err.println("Indique o servidor e a porta corretos em conn!\n");
//            return;
//        }



        try {
            servidor = new Parceiro(conexao, new ObjectInputStream(conexao.getInputStream()),  new ObjectOutputStream(conexao.getOutputStream())); //apenas recebe
            //saida = new Parceiro(conn, new ObjectInputStream(conn.getInputStream()), new ObjectOutputStream(conn.getOutputStream()));// apenas envia

        } catch (Exception erro) {
            System.err.println("Indique o servidor e a porta corretos!\n");
            return;
        }
        try {
            while (conexao.isConnected()) {
                // recebendo automaticamente dados do (sensor)
                String command = servidor.receba();

                if (command.equals("status")) {
                    servidor.envie("Online");
                    //saida.envie("Online");
                } else {
                    TryServer(command);
                    TryDB(command);
                }
            }
        } catch (Exception erro) {
            System.err.println(erro.getMessage());
        }
    }

    private static boolean TryServer(String command){
        String cmd = command.replace("Server ", "");
        try{
            String response = ServerCommand.valueOf(cmd).execute().toString();
            System.out.println(response);
            servidor.envie(response+" ");
            return true;
        }catch(Exception e) {
            System.out.println("Server command not valid: " +cmd);
            return false;
        }
    }

    private static boolean TryDB(String cmd){
        if(DB.exists(cmd.split(" ")[0].trim())){
            try {
                String response = DB.valueOf(cmd.split(" ")[0]).execute(cmd.replace(cmd.split(" ")[0], "")).toString();
                response = response.trim();
                System.out.println(response);
                servidor.envie(response+" ");
                return true;
            }catch (Exception ex){
                System.out.println(ex.getMessage());
                return true;
            }
        }else{
            System.err.println("DataBase command not valid: " +cmd);
            return false;
        }
    }
}

