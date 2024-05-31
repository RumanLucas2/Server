package org.example;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class Cliente {
    public static final String HOST_PADRAO = "localhost";
    public static final int PORTA_PADRAO = 8000;

    public static void main(String[] args) {
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

        Parceiro servidor = null;
        try {
            servidor = new Parceiro(conexao, receptor, transmissor);
        } catch (Exception erro) {
            System.err.println("Indique o servidor e a porta corretos!\n");
            return;
        }

        do {
            try {
                // recebendo automaticamente dados do (sensor)
                Comunicado comunicado = (Comunicado) servidor.espie();

                if (comunicado instanceof DadoDoSensor) {
                    DadoDoSensor dadoDoSensor = (DadoDoSensor) comunicado;

                    try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
                        MongoDatabase database = mongoClient.getDatabase("projetoRP");
                        MongoCollection<Document> collection = database.getCollection("sensor");
                        Document document = new Document("dado", dadoDoSensor.getDado())
                                .append("timestamp", System.currentTimeMillis());
                        collection.insertOne(document);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                   // System.out.println("Dado do Sensor " + dadoDoSensor.getSensor().getNome() + ": " + dadoDoSensor.getDado());

                }

            } catch (Exception erro) {
                System.err.println("Erro de comunicacao com o servidor;");
                System.err.println("Tente novamente!");
                System.err.println("Caso o erro persista, termine o programa");
                System.err.println("e volte a tentar mais tarde!\n");
            }
        } while (true);

    }
}
