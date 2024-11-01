package org.example;

import java.io.Serializable;

public class Sensor implements Serializable {
    private String nome;

    public Sensor() {
        Command();
    }

    public String getNome() {
        return nome;
    }

    public enum Server
    {
        Terminate{
            @Override
            public void execute(){
                Servidor.Terminate();
            }
        },
        Start{
            @Override
            public void execute() {
                Servidor.Start();
            }
        },

        Status{
            @Override
            public void execute(){
                SupervisoraDeConexao.addCommands("status");
            }
        };
        public abstract void execute();
    }

    public enum DB
    {;
        public enum Get{
            GET();

            public static String execute(String obj){
                return obj;
            }

        };

        public enum Post {
            POST();
            public static boolean execute (Object obj){
                return true;
            }
        };
        public void end(){}
    }

    private static void Command(){
        System.out.println("Ready to Load");
        String comando = Teclado.getUmString();
        if (comando.contains("Server")) {
            TryServer(comando);
        } else if (comando.contains("DB")) {
            TryDB(comando);
        } else if (comando.equalsIgnoreCase("end")){
            System.exit(0);
        }else {
            System.out.println("Invalid Command");
        }
    }

    private static void TryServer(String command){
        command = command.replace("Server ", "");
        try{
            Sensor.Server.valueOf(command).execute();
        }catch(Exception e) {
            System.out.println("Command not valid: " +command);
        }
    }

    private static void TryDB(String command){
        command = command.replace("DB ", "");
        if (command.contains("Get")){
            command = command.replace("Get ", "");
            String dado = Sensor.DB.Get.execute(command);
            System.out.println(dado);
        }
        if (command.contains("Post")){
            command = command.replace("Post ", "");
            String dado = "" +Sensor.DB.Post.execute(command);
            System.out.println(dado);
        }

    }
}
