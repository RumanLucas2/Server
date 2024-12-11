package org.example;

import java.io.Serializable;

public class Sensor implements Serializable {
    private final Parceiro usr;

    public Sensor(Parceiro usr) throws Exception {
        this.usr =usr;
        try{
            Command(usr);
        }catch (Exception ex){
            try {
                ServerCommand.Disconnect.execute(usr);
            }catch (Exception ex1){
                throw new Exception("Fail to execute Disconnect");
            }

        }
    }

    /**
     *
     * @param usr
     * @throws Exception
     */
    private void Command(Parceiro usr) throws Exception {
        String comando;
        try{
            comando = usr.receba();
        }catch (Exception ignore){
            throw new RuntimeException("Connection Fail");
        }
        if (comando.startsWith("Server")) {
            TryServer(comando);
        } else if (comando.startsWith("DB")) {
            TryDB(comando);
        } else {
            String ret = "Invalid Command: "+comando;
            System.out.println(ret);
            usr.envie(ret);
        }
    }

    /**
     * Testa o {@link String command} para ver se ele é um comando de Servidor
     * @param command comando a ser testado
     * @Note: Caso nao encontre o comando, ele apenas printa que nao encontrou o comando na lista de <br\>{@link ServerCommand [Server Commands]}
     */
    private void TryServer(String command) throws Exception {
        String cmd = command.replace("Server ", "");
        try{
            String response = ServerCommand.valueOf(cmd).execute().toString();
            System.out.println(response);
            usr.envie(response+" ");
        }catch(Exception e) {
            try {
                //aqui vai usar apenas o Disconnect
                ServerCommand.valueOf(cmd).execute(usr);
            }catch (Exception ignore){
                String ret = "Server command not valid: " +cmd;
                System.out.println(ret);
                usr.envie(ret);
            }
        }
    }


    /**
     * Testa o {@link String command} para ver se ele é um comando de Servidor
     * @param cmd comando a ser testado
     * @Note: Caso nao encontre o comando, ele apenas printa que nao encontrou o comando na lista de <br\>{@link DB [DataBase Commands]}
     */
    private void TryDB(String cmd) throws Exception {
        cmd = cmd.replace("DB ", "");
        if(DB.exists(cmd.split(" ")[0].trim())){
            try {
                String response = DB.valueOf(cmd.split(" ")[0]).execute(cmd.replace(cmd.split(" ")[0], "")).toString();
                response = response.trim();
                System.out.println(response);
                usr.envie(response+" ");
            }catch (Exception ex){
                System.out.println(ex.getMessage());
            }
        }else{
            String ret = "DataBase command not valid: " +cmd;
            System.err.println(ret);
            usr.envie(ret);
        }
    }
}
