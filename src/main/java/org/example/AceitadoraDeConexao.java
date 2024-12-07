package org.example;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class AceitadoraDeConexao extends Thread {
    private ServerSocket pedido = new ServerSocket();
    private final ArrayList<Parceiro> usuarios;


    /**
     *
     * @param porta
     * @param usuarios
     * @throws Exception
     */
    public AceitadoraDeConexao(String porta, ArrayList<Parceiro> usuarios) throws Exception {
        if (porta == null)
            throw new Exception("Porta ausente");
        try {
            assert pedido == null;
            this.pedido = new ServerSocket(Integer.parseInt(porta));
        } catch (Exception erro) {
            throw new Exception("Porta invalida");
        }

        if (usuarios == null)
            throw new Exception("Usuarios ausentes");
        this.usuarios = usuarios;
    }

    /**
     * Pode ser chamado pelo Start
     */
    @Override
    public void run() {
        System.out.println("Tentando conexao");

        Socket conexao = null;
        try {
            conexao = this.pedido.accept();
            pedido.close();
            System.out.println("Pedido Aceito");
        } catch (Exception erro) {
            System.err.println("Err no aceitamento");
        }

        SupervisoraDeConexao supervisoraDeConexao = null;
        try {
            supervisoraDeConexao = new SupervisoraDeConexao(conexao, usuarios);
            System.out.println("Supervisora Iniciada");
        } catch (Exception ignored) {
            System.err.println("Err na Inicializacao da Supervisora");
        }
        System.out.println("Iniciando Supervisora.Start()");
        assert supervisoraDeConexao != null;
        supervisoraDeConexao.start();

    }
}
