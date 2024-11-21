package org.example;

import java.io.*;
import java.net.Socket;
import java.util.*;


public class Cliente {

    public static final String HOST_PADRAO = "localhost";
    public static final int PORTA_PADRAO = 8080;
    private static Parceiro servidor = null;
    private static final Map<String, String> Arquivos = new HashMap<>();


    public static void main(String[] args) throws IOException {
        if (args.length > 2) {
            System.err.println("Uso esperado: java Cliente [HOST [PORTA]]\n");
            return;
        }

        Socket conexao = null;
        try {
            String host = Cliente.HOST_PADRAO;
            int porta = Cliente.PORTA_PADRAO;

            if (args.length > 0)
                host = args[0];

            if (args.length == 2)
                porta = Integer.parseInt(args[1]);

            conexao = new Socket(host, porta);
        } catch (Exception erro) {
            System.err.println("Indique o servidor e a porta corretos!\n");
            return;
        }

        ObjectOutputStream transmissor = null;
        try {
            transmissor = new ObjectOutputStream(conexao.getOutputStream());
        } catch (Exception erro) {
            System.err.println("Indique o servidor e a porta corretos!\n");
            return;
        }

        ObjectInputStream receptor = null;
        try {
            receptor = new ObjectInputStream(conexao.getInputStream());
        } catch (Exception erro) {
            System.err.println("Indique o servidor e a porta corretos!\n");
            return;
        }


        try {
            servidor = new Parceiro(conexao, receptor, transmissor);
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
                } else if (command.equals("list")) {
                    servidor.envie(Cliente.listarArquivos());
                } else {
                    testCommands(command);
                }
            }
        } catch (Exception erro) {
            System.err.println(erro.getMessage());
        }
    }

    private static String listarArquivos() {
        File diretorio = new File(Cliente.Diretorio);
        StringBuilder lista = new StringBuilder();

        if (diretorio.exists() && diretorio.isDirectory()) {
            File[] arquivos = diretorio.listFiles((dir, nome) -> nome.toLowerCase().endsWith(".json"));
            if (arquivos != null && arquivos.length > 0) {
                for (File arquivo : arquivos) {
                    lista.append(arquivo.getAbsolutePath()).append("\n");
                }
            } else {
                lista.append("Nenhum arquivo JSON encontrado no diretório.");
            }
        } else {
            lista.append("Diretório não encontrado.");
        }
        return lista.toString();
    }

    private static String criarArquivoJson(String nomeArquivo, String conteudo) {
        // Define o diretório onde os arquivos JSON serão salvos
        final String DIRETORIO_ARQUIVOS = "arquivos";

        // Verifica se o nome do arquivo termina com ".json"
        if (!nomeArquivo.endsWith(".json")) {
            nomeArquivo += ".json";
        }

        // Define o caminho completo do arquivo no diretório especificado
        File diretorio = new File(DIRETORIO_ARQUIVOS);
        if (!diretorio.exists()) {
            diretorio.mkdirs(); // Cria o diretório se ele não existir
        }

        File arquivo = new File(diretorio, nomeArquivo);

        // Escreve o conteúdo JSON no arquivo
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo))) {
            writer.write(conteudo);
            return "Arquivo JSON '" + arquivo.getAbsolutePath() + "' criado com sucesso.";
        } catch (IOException e) {
            return "Erro ao criar o arquivo JSON: " + e.getMessage();
        }
    }

    private static String criarArquivoJson(String nomeArquivo) {
        return criarArquivoJson(nomeArquivo, "");
    }

    public static void testCommands(String cmd){
        cmd = cmd.replace("DB ", "");
        String[] command = cmd.split(" ");
        if(DB.exists(command[0])){
            try {
                servidor.envie(DB.valueOf(command[0]).execute(cmd.replace(command[0]+" ", "")));
            }catch (Exception e){
                System.err.println(e);
            }
        }
    }
}

