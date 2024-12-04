package org.example;

import com.mongodb.lang.Nullable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Parceiro {
    private final Socket conexao;
    private final ObjectInputStream receptor;
    private final ObjectOutputStream transmissor;
    private String proximoComunicado = null;

    private boolean SenderOnly = false;
    private boolean ReceiverOnly = false;

    public boolean IsSenderOnly(){
        return SenderOnly;
    }

    public boolean IsReceiverOnly(){
        return ReceiverOnly;
    }
    public Parceiro(Socket conexao, @Nullable ObjectInputStream receptor, @Nullable ObjectOutputStream transmissor) throws Exception {
        if (conexao == null)
            throw new Exception("Conexao ausente");
        this.conexao = conexao;

        if (! (transmissor == null)){
            this.transmissor = transmissor;
        }else {
            System.err.println("Receptor Ausente, inicializando como Transmissor");
            this.transmissor = null;
            SenderOnly = true;
        }

        if (! (receptor == null)){
            this.receptor = receptor;
        }else {
            System.err.println("Receptor Ausente, inicializando como Transmissor");
            this.receptor = null;
            ReceiverOnly = true;
        }
    }


    public void envie(String x) throws Exception {
        if (ReceiverOnly) throw new Exception("Este apenas recebe dados");
        try {
            this.transmissor.writeObject(x);
            this.transmissor.flush();
        } catch (IOException erro) {
            throw new Exception("Erro de transmissao");
        }
    }
    public String receba() throws Exception {
        if (SenderOnly) throw new Exception("Este apenas envia dados");
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
