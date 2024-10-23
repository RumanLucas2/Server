package org.example;

import jdk.jshell.Snippet;

import java.awt.desktop.AppEvent;
import java.util.ArrayList;
import java.util.Arrays;



public class Servidor {
    private static boolean status = false;
    private static final ArrayList<Parceiro> usuarios = new ArrayList<>();
    private static String[] Args;
    private static String Porta;

    public static String getPorta() {
        return Porta;
    }

    public static String[] GetArgs()
    {
        return Args;
    }
    public static void TestCmds(){
        Post(Post.Start);
        System.out.println(Get(Get.Status));
        Post(Post.Terminate);
    }

    public static void main(String[] args) {
        // Inicializa algo de forma assíncrona
        Porta = "8080";
        Args = args;
        Command();
    }

    public enum Get {
        Status {
            @Override
            public String execute() {
                System.out.println("Server is "+ (Servidor.status?"Online" :"Offline"));
                return "" + Servidor.status;
            }
        };
        /**
         * @return status of completion
         */
        public abstract String execute();
    }

    public enum Post
    {
        Terminate{
            @Override
            public void execute(){
                synchronized (usuarios) {
                    ComunicadoDeDesligamento comunicadoDeDesligamento =
                            new ComunicadoDeDesligamento();

                    for (Parceiro usuario : usuarios) {
                        try {
                            usuario.receba(comunicadoDeDesligamento);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        try {
                            usuario.adeus();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                    System.out.println("Server Off");
                    Servidor.status = false;
                }
            }
        },
        Start{
            @Override
            public void execute() {
                String[] args = GetArgs();
                String porta = getPorta();
                if (args.length > 1) {
                    System.err.println("Uso esperado: java Servidor [PORTA]\n");
                }

                if (args.length == 1) porta = args[0];

                AceitadoraDeConexao aceitadoraDeConexao = null;
                try {
                    aceitadoraDeConexao = new AceitadoraDeConexao(porta, usuarios);
                } catch (Exception error) {
                    System.err.println("Escolha uma porta apropriada e liberada para uso!\n");
                }
                assert aceitadoraDeConexao != null;
                aceitadoraDeConexao.start();
                status = true;
                System.out.println("Server Started");
            }
        };
        public abstract void execute();
    }

    public static String Get(Get a){
        return a.execute();
    }
    public static void Post(Post a){
        a.execute();
    }
    private static void Command(){
        while(Boolean.TRUE) {
            System.out.println("Ready to Load");
            String comando = Teclado.getUmString();
            if (comando.toLowerCase().contains("post")) {
                TryPost(comando);
            } else if (comando.toLowerCase().contains("get")) {
                TryGet(comando);
            } else if (comando.equalsIgnoreCase("end")){
                System.exit(0);
            }else {
                System.out.println("Invalid Command");
            }
        }
    }

    private static void TryPost(String command){
        command = command.replace("post ", "").replace("Post ", "");
        try{
            Post.valueOf(command).execute();
        }catch(Exception e) {
            System.out.println("Command not valid: " +command);
        }
    }

    private static void TryGet(String command){
        command = command.replace("get ", "").replace("Get ", "");
        try{
            Get.valueOf(command).execute();
        }catch(Exception e) {
            System.out.println("Command not valid: " +command);
        }
    }
}
