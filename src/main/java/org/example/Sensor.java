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
        if (comando.startsWith("Server")) {
            String cmd = comando.replace("Server ", "");
            Commands.add(cmd);
        } else if (comando.startsWith("DB")) {
            String cmd = comando.replace("DB ", "");
            Commands.add(cmd);
        } else if (comando.equalsIgnoreCase("end")){
            SupervisoraDeConexao.addCommands("end");
        }else {
            System.out.println("Invalid Command");
        }
    }

}
