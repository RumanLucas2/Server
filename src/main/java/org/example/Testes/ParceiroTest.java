package org.example.Testes;

import org.example.Parceiro;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static org.junit.Assert.*;

public class ParceiroTest {

    @Test
    public void testEnvie_ValidString() throws Exception {
        Socket mockSocket = new Socket();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream transmissor = new ObjectOutputStream(byteArrayOutputStream);

        Parceiro parceiro = new Parceiro(mockSocket, null, transmissor);

        parceiro.envie("Mensagem válida");
        transmissor.flush();

        String resultado = byteArrayOutputStream.toString();
        assertTrue(resultado.contains("Mensagem válida"));
    }

    @Test
    public void testEnvie_NullString() {
        try {
            Socket mockSocket = new Socket();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream transmissor = new ObjectOutputStream(byteArrayOutputStream);

            Parceiro parceiro = new Parceiro(mockSocket, null, transmissor);

            parceiro.envie(null);
            fail("Deveria ter lançado uma exceção");
        } catch (Exception e) {
            assertEquals("Erro de transmissao", e.getMessage());
        }
    }

    @Test
    public void testEnvie_LargeString() throws Exception {
        Socket mockSocket = new Socket();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream transmissor = new ObjectOutputStream(byteArrayOutputStream);

        Parceiro parceiro = new Parceiro(mockSocket, null, transmissor);

        String largeString = "A".repeat(10_000); // String grande
        parceiro.envie(largeString);
        transmissor.flush();

        String resultado = byteArrayOutputStream.toString();
        assertTrue(resultado.contains(largeString));
    }

    @Test
    public void testEnvie_EmptyString() throws Exception {
        Socket mockSocket = new Socket();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream transmissor = new ObjectOutputStream(byteArrayOutputStream);

        Parceiro parceiro = new Parceiro(mockSocket, null, transmissor);

        parceiro.envie("");
        transmissor.flush();

        String resultado = byteArrayOutputStream.toString();
        assertEquals("", resultado);
    }

    @Test
    public void testEnvie_LargeStringBoundary() throws Exception {
        Socket mockSocket = new Socket();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream transmissor = new ObjectOutputStream(byteArrayOutputStream);

        Parceiro parceiro = new Parceiro(mockSocket, null, transmissor);

        String largeString = "A".repeat(10_000); // Limite superior
        parceiro.envie(largeString);
        transmissor.flush();

        String resultado = byteArrayOutputStream.toString();
        assertTrue(resultado.contains(largeString));
    }
    @Test
    public void testEnvie_SpecialCharacters() throws Exception {
        Socket mockSocket = new Socket();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream transmissor = new ObjectOutputStream(byteArrayOutputStream);

        Parceiro parceiro = new Parceiro(mockSocket, null, transmissor);

        String specialString = "\uFFFF"; // Caracter especial
        parceiro.envie(specialString);
        transmissor.flush();

        String resultado = byteArrayOutputStream.toString();
        assertTrue(resultado.contains(specialString));
    }

    @Test
    public void testEnvie_TooLargeString() {
        try {
            Socket mockSocket = new Socket();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream transmissor = new ObjectOutputStream(byteArrayOutputStream);

            Parceiro parceiro = new Parceiro(mockSocket, null, transmissor);

            String tooLargeString = "A".repeat(1_000_000); // Muito além do esperado
            parceiro.envie(tooLargeString);
            fail("Deveria lançar exceção ao enviar String grande demais");
        } catch (Exception e) {
            assertEquals("Erro de transmissao", e.getMessage());
        }
    }
    @Test
    public void testEnvie_ValidSimpleString() throws Exception {
        Socket mockSocket = new Socket();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream transmissor = new ObjectOutputStream(byteArrayOutputStream);

        Parceiro parceiro = new Parceiro(mockSocket, null, transmissor);

        parceiro.envie("Olá, cliente!");
        transmissor.flush();

        String resultado = byteArrayOutputStream.toString();
        assertTrue(resultado.contains("Olá, cliente!"));
    }

    @Test
    public void testEnvie_SpacesOnly() throws Exception {
        Socket mockSocket = new Socket();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream transmissor = new ObjectOutputStream(byteArrayOutputStream);

        Parceiro parceiro = new Parceiro(mockSocket, null, transmissor);

        parceiro.envie(" ");
        transmissor.flush();

        String resultado = byteArrayOutputStream.toString();
        assertEquals(" ", resultado);
    }

    @Test
    public void testEnvie_NullValue() {
        try {
            Socket mockSocket = new Socket();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream transmissor = new ObjectOutputStream(byteArrayOutputStream);

            Parceiro parceiro = new Parceiro(mockSocket, null, transmissor);

            parceiro.envie(null);
            fail("Deveria lançar exceção ao enviar null");
        } catch (Exception e) {
            assertEquals("Erro de transmissao", e.getMessage());
        }
    }

    @Test
    public void testPairwise_SimpleAndEmpty() throws Exception {
        Socket mockSocket = new Socket();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream transmissor = new ObjectOutputStream(byteArrayOutputStream);

        Parceiro parceiro = new Parceiro(mockSocket, null, transmissor);

        parceiro.envie("Olá, cliente!");
        transmissor.flush();
        assertTrue(byteArrayOutputStream.toString().contains("Olá, cliente!"));

        parceiro.envie("");
        transmissor.flush();
        assertEquals("", byteArrayOutputStream.toString().strip());
    }

    @Test
    public void testPairwise_LargeAndExtreme() throws Exception {
        Socket mockSocket = new Socket();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream transmissor = new ObjectOutputStream(byteArrayOutputStream);

        Parceiro parceiro = new Parceiro(mockSocket, null, transmissor);

        parceiro.envie("A".repeat(10_000));
        transmissor.flush();
        assertTrue(byteArrayOutputStream.toString().contains("A".repeat(10_000)));

        try {
            parceiro.envie("A".repeat(1_000_000));
            fail("Deveria lançar exceção ao enviar String muito grande");
        } catch (Exception e) {
            assertEquals("Erro de transmissao", e.getMessage());
        }
    }

    @Test
    public void testPairwise_NullAndUnicodeExtreme() {
        try {
            Socket mockSocket = new Socket();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream transmissor = new ObjectOutputStream(byteArrayOutputStream);

            Parceiro parceiro = new Parceiro(mockSocket, null, transmissor);

            parceiro.envie(null);
            fail("Deveria lançar exceção ao enviar null");
        } catch (Exception e) {
            assertEquals("Erro de transmissao", e.getMessage());
        }

        try {
            Socket mockSocket = new Socket();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream transmissor = new ObjectOutputStream(byteArrayOutputStream);

            Parceiro parceiro = new Parceiro(mockSocket, null, transmissor);

            parceiro.envie("\uFFFF");
            transmissor.flush();

            String resultado = byteArrayOutputStream.toString();
            assertTrue(resultado.contains("\uFFFF"));
        } catch (Exception e) {
            fail("Não deveria lançar exceção para caracteres Unicode extremos válidos");
        }
    }

}
