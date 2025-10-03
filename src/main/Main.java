package main;

import java.io.IOException;
import java.net.Socket;
import servidor.Server;
import sqlite.SQLite;

public class Main {
    public static void main(String[] args) throws IOException {
        Server server = new Server();
        Socket socket = null;

        try {
        	// Inicia o servidor e conecta ao cliente
            System.out.println("Aguardando conexão...");
            server.criarServerSocket(55555);
            socket = server.esperaConexao();
            System.out.println("Cliente conectado.");
            server.iniciarStreams(socket);

        } catch (IOException e) {
            System.out.println(e.getMessage());
            if (socket != null) socket.close();
            return;
        }

        // Inicia o banco de dados (cria uma tabela se já não existir uma)
        SQLite.connect();
        SQLite.createNewTable();

        Integer cursor = 0; 

        do {
            // Envia o menu principal ao cliente
            server.outputString("Bem vindo ao EcoColeta");
            server.outputString("Veja os pontos de coleta disponíveis para cada tipo de resíduo.");
            server.outputString("1. Plástico \n2. Metal \n3. Papel \n4. Vidro \n5. Sair");
            server.outputString("Escolha as opções pela numeração: ");
            // Recebe a ação do cliente
            cursor = server.inputInt();

            // Verifica se a conexão foi perdida
            if (cursor == null) {
                System.out.println("Conexão com o cliente perdida. Encerrando servidor.");
                break; // Sai do loop principal
            }
			switch (cursor) {
			case 1:
				do {
					// Envia uma lista filtrada em String 
					server.outputString(SQLite.getByResiduo("plastico"));
					server.outputString("Digite 0 para voltar.");
					// Recebe a ação do cliente
					cursor = server.inputInt();
				}while (cursor != 0);
				break;
			case 2:
				do {
					// Envia uma lista filtrada em String
					server.outputString(SQLite.getByResiduo("metal"));
					server.outputString("Digite 0 para voltar.");
					// Recebe a ação do cliente
					cursor = server.inputInt();
				}while (cursor != 0);
				break;
			case 3:
				do {
					// Envia uma lista filtrada em String
					server.outputString(SQLite.getByResiduo("papel"));
					server.outputString("Digite 0 para voltar.");
					// Recebe a ação do cliente
					cursor = server.inputInt();
				}while (cursor != 0);
				break;
			case 4:
				do {
					// Envia uma lista filtrada em String
					server.outputString(SQLite.getByResiduo("vidro"));
					server.outputString("Digite 0 para voltar.");
					// Recebe a ação do cliente
					cursor = server.inputInt();
				}while (cursor != 0);
				break;
			case 5:
				// Encerra o loop principal
				server.outputString("Fechando...");
				break;
			case 123456:
				String nome;
				String endereco;
				String residuo;
				do {
					// Envia o menu do ADM
					server.outputString("Entrando como adm...");
					server.outputString("1. Inserir na tabela. \n2. Apagar da tabela (pelo nome) \n3. Editar tabela \n4. Sair");
					server.outputString("Escolha as opções pela numeração: ");
					// Recebe a ação do cliente
					cursor = server.inputInt();
					switch (cursor) {
					case 1:
						// Inserindo no banco de dados
						server.outputString("Digite o nome do local: ");
						nome = server.inputString();
						server.outputString("Digite o endereço do local: ");
						endereco = server.inputString();
						server.outputString("Digite o tipo de resíduo coletado (sem acentos): ");
						residuo = server.inputString();
						SQLite.insert(nome, endereco, residuo);
						server.outputString("Inserido!");
						break;
						
					case 2:
						// Apagando no no banco de dados
						server.outputString("Digite o nome do item que deseja apagar: \n");
						nome = server.inputString();
						SQLite.delete(nome);
						server.outputString("Apagado!");
						break;
						
					case 3:
						String nomeAtual;
						// Editando no banco de dados
						server.outputString("Digite o nome atual do local: ");
						nomeAtual = server.inputString();
						server.outputString("Digite o novo nome do local: ");
						nome = server.inputString();
						server.outputString("Digite o novo endereço do local: ");
						endereco = server.inputString();
						server.outputString("Digite o tipo de resíduo coletado (sem acentos): ");
						residuo = server.inputString();
						SQLite.updateLocal(nomeAtual, nome, endereco, residuo);
						server.outputString("Editado!");
						break;
						
					case 4:
						// Saindo do menu de ADM
						server.outputString("Voltando...");
						break;
					}
				}while (cursor != 4);
				break;
			} 
		}while(cursor != 5);
		socket.close();
	}
}

