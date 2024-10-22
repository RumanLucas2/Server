package org.example;

import java.util.ArrayList;

public class Servidor {
    private static boolean status = false;
    public static class Inicializador{
        private static String porta;
        private static String[] args;

        private static final ArrayList<Parceiro> usuarios = new ArrayList<>();
        private static void Starter(String port, String[] arguments) throws InterruptedException {
            porta = port;
            args = arguments;
            try {
                Start();

            }catch (Exception ignore){}
        }
        private static void Start() throws InterruptedException {
            if (args.length > 1) {
                System.err.println("Uso esperado: java Servidor [PORTA]\n");
                return;
            }
            System.out.println("Servidor-> Porta Valido\n");

            if (args.length == 1)
                porta = args[0];

            AceitadoraDeConexao aceitadoraDeConexao = null;
            try {
                aceitadoraDeConexao = new AceitadoraDeConexao(porta, usuarios);
                aceitadoraDeConexao.start();
                status = true;
            } catch (Exception erro) {
                System.err.println("Escolha uma porta apropriada e liberada para uso!\n");
                return;
            }
            System.out.println("Porta Valida\n");
            try {
                Servidor.TestCmds();

            }catch (Exception ignore){

            }
        }

        public static void Terminate(){
            synchronized (usuarios) {
                ComunicadoDeDesligamento comunicadoDeDesligamento =
                        new ComunicadoDeDesligamento();

                for (Parceiro usuario : usuarios) {
                    try {
                        usuario.receba(comunicadoDeDesligamento);
                        usuario.adeus();
                    } catch (Exception ignored) {
                    }
                }
                System.exit(0);
            }
        }
    }
    public static void main(String[] args) throws InterruptedException {
        // Inicializa algo de forma assíncrona
        String PORTA_PADRAO = "8080";
        Inicializador.Starter(PORTA_PADRAO, args);
    }

    private static final class cmd{
        public enum GetCmd
        {
            Status {
                @Override
                public String execute(){
                    return ""+Servidor.status;
                }
            },
            Get(){
                @Override
                public String execute(){
                    return "This is "+Boolean.TRUE;
                }
            }
            ;

            public abstract String execute();
        }

        public enum PostCmd
        {
            Terminate{
                @Override
                public void execute(){
                    Inicializador.Terminate();
                }
            };
            public abstract void execute();
        }
        public static String Get(cmd.GetCmd a){
            return a.execute();
        }
        public static void Post(cmd.PostCmd a){
            a.execute();
        }
    }
    public static void TestCmds(){
        System.out.println(cmd.Get(cmd.GetCmd.Get));
        cmd.Post((cmd.PostCmd.Terminate));
    }
}
