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
            System.out.println("Aguardando conexão...");
            server.criarServerSocket(55555);
            socket = server.esperaConexao();
            System.out.println("Cliente conectado.");
            
            // Inicialize os streams aqui, uma única vez!
            server.iniciarStreams(socket);

        } catch (IOException e) {
            System.out.println(e.getMessage());
            // Se a conexão falhar, não adianta continuar
            if (socket != null) socket.close();
            return;
        }

        SQLite.connect();
        SQLite.createNewTable();

        Integer cursor = 0; // Use Integer para aceitar null

        do {
            // Ajuste as chamadas para não passar o socket
            server.outputString("Bem vindo ao EcoColeta");
            server.outputString("Veja os pontos de coleta disponíveis para cada tipo de resíduo.");
            server.outputString("1. Plástico \n2. Metal \n3. Papel \n4. Vidro \n5. Sair");
            server.outputString("Escolha as opções pela numeração: ");
            
            cursor = server.inputInt();

            // Verifique se a conexão foi perdida
            if (cursor == null) {
                System.out.println("Conexão com o cliente perdida. Encerrando servidor.");
                break; // Sai do loop principal
            }
			switch (cursor) {
			case 1:
				do {
					server.outputString(SQLite.getByResiduo("plastico"));
					//saida
					server.outputString("Digite 0 para voltar.");
					//entrada
					cursor = server.inputInt();
				}while (cursor != 0);
				break;
			case 2:
				do {
					server.outputString(SQLite.getByResiduo("metal"));
					//saida
					server.outputString("Digite 0 para voltar.");
					//entrada
					cursor = server.inputInt();
				}while (cursor != 0);
				break;
			case 3:
				do {
					server.outputString(SQLite.getByResiduo("papel"));
					//saida
					server.outputString("Digite 0 para voltar.");
					//entrada
					cursor = server.inputInt();
				}while (cursor != 0);
				break;
			case 4:
				do {
					server.outputString(SQLite.getByResiduo("vidro"));
					//saida
					server.outputString("Digite 0 para voltar.");
					//entrada
					cursor = server.inputInt();
				}while (cursor != 0);
				break;
			case 5:
				//saida
				server.outputString("Fechando...");
				break;
			case 123456:
				String nome;
				String endereco;
				String residuo;
				do {
					//saida
					server.outputString("Entrando como adm...");
					server.outputString("1. Inserir na tabela. \n2. Apagar da tabela (pelo nome) \n3. Editar tabela \n4. Sair");
					server.outputString("Escolha as opções pela numeração: ");
					//entrada
					cursor = server.inputInt();
					switch (cursor) {
					case 1:
						//saida
						server.outputString("Digite o nome do local: ");
						//entrada
						nome = server.inputString();
						//saida
						server.outputString("Digite o endereço do local: ");
						//entrada
						endereco = server.inputString();
						//saida
						server.outputString("Digite o tipo de resíduo coletado (sem acentos): ");
						//entrada
						residuo = server.inputString();
						SQLite.insert(nome, endereco, residuo);
						//saida
						server.outputString("Inserido!");
						break;
						
					case 2:
						//saida
						server.outputString("Digite o nome do item que deseja apagar: \n");
						//entrada
						nome = server.inputString();
						SQLite.delete(nome);
						//saida
						server.outputString("Apagado!");
						break;
						
					case 3:
						String nomeAtual;
						//saida
						server.outputString("Digite o nome atual do local: ");
						//entrada
						nomeAtual = server.inputString();
						//saida
						server.outputString("Digite o novo nome do local: ");
						//entrada
						nome = server.inputString();
						//saida
						server.outputString("Digite o novo endereço do local: ");
						//entrada
						endereco = server.inputString();
						//saida
						server.outputString("Digite o tipo de resíduo coletado (sem acentos): ");
						//entrada
						residuo = server.inputString();
						SQLite.updateLocal(nomeAtual, nome, endereco, residuo);
						//saida
						server.outputString("Editado!");
						break;
						
					case 4:
						//saida
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

