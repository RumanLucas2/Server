package org.example;

import java.io.Serializable;

public class Sensor implements Serializable {
    private String nome;
    private Parceiro usr;

    public Sensor(Parceiro usr) {
        this.usr =usr;
        try{
            Command(usr);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    /**
     *
     * @param usr
     * @throws Exception
     */
    private void Command(Parceiro usr) throws Exception {
        System.out.println("Insert new command");
        String comando = usr.receba();
        if (comando.startsWith("Server")) {
            String cmd = comando.replace("Server ", "");
            TryServer(comando);
        } else if (comando.startsWith("DB")) {
            TryDB(comando);
        } else {
            System.out.println("Invalid Command");
            return;
        }
    }

    private void TryServer(String command){
        String cmd = command.replace("Server ", "");
        try{
            String response = ServerCommand.valueOf(cmd).execute().toString();
            System.out.println(response);
            usr.envie(response+" ");
        }catch(Exception e) {
            System.out.println("Server command not valid: " +cmd);
        }
    }

    private void TryDB(String cmd){
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
            System.err.println("DataBase command not valid: " +cmd);
        }
    }
}
