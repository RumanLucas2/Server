package org.example;

import java.io.Serializable;

public class Sensor implements Serializable {
    private String nome;

    public Sensor() {
        Command();
    }

    private static void Command(){
        System.out.println("Insert new command");
        String comando = Teclado.getUmString();
        if (comando.contains("Server")) {
            TryServer(comando);
        } else if (comando.contains("DB")) {
            TryDB(comando);
        } else if (comando.equalsIgnoreCase("end")){
            SupervisoraDeConexao.addCommands("end");
        }else {
            System.out.println("Invalid Command");
        }
    }

    private static void TryServer(String command){
        command = command.replace("Server ", "");
        try{
            ServerCommand.valueOf(command).execute();
        }catch(Exception e) {
            System.out.println("Command not valid: " +command);
        }
    }

    private static void TryDB(String command){
        var cmd = command.replace("DB ", "");
        if(DB.exists(cmd)){
            try {
                DB.valueOf(cmd.split(" ")[0]).execute(command.replace("DB ", ""));
            }catch (Exception ex){
                System.out.println(ex.getMessage());
            }
        }else{
            System.err.println("Command not found:" + command);
        }
    }
}
