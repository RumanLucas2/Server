package org.example;

import javax.swing.filechooser.FileView;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Semaphore;

public class Parceiro {
    private final Socket conexao;
    private final ObjectInputStream receptor;
    private final ObjectOutputStream transmissor;
    private String proximoComunicado = null;

    private final boolean FileView;

    public boolean isFileView(){
        return FileView;
    }
    public Parceiro(Socket conexao, ObjectInputStream receptor, ObjectOutputStream transmissor) throws Exception {
        if (conexao == null)
            throw new Exception("Conexao ausente");
        this.conexao = conexao;

        if (transmissor == null)
            throw new Exception("Transmissor ausente");
        this.transmissor = transmissor;

        if (! (receptor == null)){
            this.receptor = receptor;
            FileView = false;
        }else {
            System.err.println("Receptor Ausente, inicializando como Transmissor");
            this.receptor = null;
            FileView = true;
        }
    }


    public void envie(String x) throws Exception {
        try {
            this.transmissor.writeObject(x);
            this.transmissor.flush();
        } catch (IOException erro) {
            throw new Exception("Erro de transmissao");
        }
    }
    public String receba() throws Exception {
        if (FileView) throw new Exception("Observer only");
        try {
            if (this.proximoComunicado == null) this.proximoComunicado = this.receptor.readObject().toString();
            String ret = this.proximoComunicado;
            this.proximoComunicado = null;
            return ret;
        } catch (Exception erro) {
            throw new Exception("Erro de recepcao");
        }
    }
    public void Terminate() throws Exception {
        try {
            this.transmissor.close();
            if (!(this.receptor ==null))this.receptor.close();
            this.conexao.close();
        } catch (Exception erro) {
            throw new Exception("Erro de desconexao");
        }
    }
}
