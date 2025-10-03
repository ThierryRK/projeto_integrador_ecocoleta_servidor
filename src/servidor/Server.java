package servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public ServerSocket serverSocket;
    // Adicione os streams como atributos
    private ObjectOutputStream output;
    private ObjectInputStream input;

    public void criarServerSocket(int porta) throws IOException {
        serverSocket = new ServerSocket(porta);
    }

    public Socket esperaConexao() throws IOException {
        return serverSocket.accept();
    }

    // Método para inicializar os streams UMA VEZ
    public void iniciarStreams(Socket socket) throws IOException {
        output = new ObjectOutputStream(socket.getOutputStream());
        input = new ObjectInputStream(socket.getInputStream());
    }

    public void fechaSocket(Socket s) throws IOException {
        // Feche os streams também
        if (output != null) output.close();
        if (input != null) input.close();
        if (s != null) s.close();
    }

    // Modifique os métodos para USAR os streams existentes
    public void outputString(String saida) {
        try {
            output.writeUTF(saida);
            output.flush();
            System.out.println("String enviada.");
        } catch (IOException e) {
            System.out.println("Erro ao enviar String: " + e.getMessage());
        }
    }

    public String inputString() {
        try {
            return input.readUTF();
        } catch (IOException e) {
            System.out.println("Erro ao ler String: " + e.getMessage());
        }
        return null;
    }

    public Integer inputInt() { // Retorne Integer para poder retornar null
        try {
            return input.readInt();
        } catch (IOException e) {
            System.out.println("Erro ao ler Int: " + e.getMessage());
        }
        return null; // Retorne um Integer nulo, não um int
    }
}