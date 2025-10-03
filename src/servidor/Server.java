package servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public ServerSocket serverSocket;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    
    public void criarServerSocket(int porta) throws IOException {
        serverSocket = new ServerSocket(porta);
    }

    public Socket esperaConexao() throws IOException {
        return serverSocket.accept();
    }

    public void iniciarStreams(Socket socket) throws IOException {
        output = new ObjectOutputStream(socket.getOutputStream());
        input = new ObjectInputStream(socket.getInputStream());
    }

    public void fechaSocket(Socket s) throws IOException {
        // Fecha os streams também
        if (output != null) output.close();
        if (input != null) input.close();
        if (s != null) s.close();
    }

    // Envia String ao cliente
    public void outputString(String saida) {
        try {
            output.writeUTF(saida);
            output.flush();
            System.out.println("String enviada.");
        } catch (IOException e) {
            System.out.println("Erro ao enviar String: " + e.getMessage());
        }
    }

    // Recebe String do cliente
    public String inputString() {
        try {
            return input.readUTF();
        } catch (IOException e) {
            System.out.println("Erro ao ler String: " + e.getMessage());
        }
        return null;
    }
    
    // Recebe Integer do cliente
    public Integer inputInt() { 
        try {
            return input.readInt();
        } catch (IOException e) {
            System.out.println("Erro ao ler Int: " + e.getMessage());
        }
        return null; 
    }
}