package sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLite {
	
	// Conecta o banco de dados
	public static void connect() {
		String url = "jdbc:sqlite:ecocoleta.db";
		
		try (Connection conn = DriverManager.getConnection(url)){
			if (conn != null) {
				System.out.println("Conectado ao DB.");
			}
			conn.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	// Cria uma tabela se não existir
	public static void createNewTable() {
		String url = "jdbc:sqlite:ecocoleta.db";
		String sql = "CREATE TABLE IF NOT EXISTS locais (\n"
				+ "id INTERGER PRIMARY KEY, \n"
				+ "nome TEXT NOT NULL, \n"
				+ "endereco TEXT NOT NULL, \n"
				+ "residuo TEXT NOT NULL \n"
				+ ");";
		
		try (Connection conn = DriverManager.getConnection(url)){
			java.sql.Statement stmt = conn.createStatement();
			stmt.execute(sql);
			System.out.println("Tabela criada.");
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	// Insere informações
	public static void insert(String nome, String endereco, String residuo) {
		String url = "jdbc:sqlite:ecocoleta.db";
		String sql = "INSERT INTO locais (nome, endereco, residuo) VALUES (?, ?, ?)";
		
		try (Connection conn = DriverManager.getConnection(url)){
			java.sql.PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, nome);
			pstmt.setString(2, endereco);
			pstmt.setString(3, residuo);
			pstmt.executeUpdate();
			System.out.println("Dados inseridos.");
			pstmt.close();
			conn.close();
		}catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	// Exibe informações
	public static void printOut() {
		String url = "jdbc:sqlite:ecocoleta.db";
		String sql = "SELECT nome, endereco, residuo FROM locais";
		
		try (Connection conn = DriverManager.getConnection(url)){
			java.sql.Statement stmt = conn.createStatement();
			java.sql.ResultSet rs = stmt.executeQuery(sql);
				
			while (rs.next()) {
				System.out.println(rs.getString("nome") + "\t" +
				rs.getString("endereco") + "\t" +
				rs.getString("residuo"));
			}
			stmt.close();
			rs.close();
			conn.close();
		}catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	// Deleta pelo nome
	public static void delete(String nome) {
	    String url = "jdbc:sqlite:ecocoleta.db";
	    String sql = "DELETE FROM locais WHERE nome = ?";
	    
	    try (Connection conn = DriverManager.getConnection(url);
	         java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        
	        pstmt.setString(1, nome);
	        int rowsAffected = pstmt.executeUpdate();
	        
	        if (rowsAffected > 0) {
	            System.out.println("Local deletado com sucesso!");
	        } else {
	            System.out.println("Nenhum local encontrado com esse nome.");
	        }
	        
	    } catch (SQLException e) {
	        System.out.println("Erro ao deletar: " + e.getMessage());
	    }
	}
	
	// Exibe informações filtradas pelo residuo
	public static String getByResiduo(String residuo) {
	    String url = "jdbc:sqlite:ecocoleta.db";
	    String sql = "SELECT * FROM locais WHERE locais.residuo = ?";
	    StringBuilder resultado = new StringBuilder();
	    
	    try (Connection conn = DriverManager.getConnection(url);
	        java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        
	        pstmt.setString(1, residuo);
	        ResultSet rs = pstmt.executeQuery();
	        
	        while (rs.next()) {
	            resultado.append("Nome: ").append(rs.getString("nome")).append("\n")
	                    .append("Endereço: ").append(rs.getString("endereco")).append("\n")
	                    .append("Resíduo: ").append(rs.getString("residuo")).append("\n")
	                    .append("---\n");
	        }
	        
	        if (resultado.length() == 0) {
	            return "Nenhum local encontrado para o resíduo: " + residuo;
	        }
	        
	    } catch (SQLException e) {
	        return "Erro ao consultar o banco de dados: " + e.getMessage();
	    }
	    
	    return resultado.toString();
	}
	
	// Edita informações
	public static void updateLocal(String nomeAtual, String novoNome, String novoEndereco, String novoResiduo) {
	    String url = "jdbc:sqlite:ecocoleta.db";
	    String sql = "UPDATE locais SET nome = ?, endereco = ?, residuo = ? WHERE nome = ?";
	    
	    try (Connection conn = DriverManager.getConnection(url);
	         java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        
	        pstmt.setString(1, novoNome);
	        pstmt.setString(2, novoEndereco);
	        pstmt.setString(3, novoResiduo);
	        pstmt.setString(4, nomeAtual);
	        
	        int rowsAffected = pstmt.executeUpdate();
	        
	        if (rowsAffected > 0) {
	            System.out.println("Local atualizado com sucesso!");
	        } else {
	            System.out.println("Nenhum local encontrado com o nome: " + nomeAtual);
	        }
	        
	    } catch (SQLException e) {
	        System.out.println("Erro ao atualizar: " + e.getMessage());
	    }
	}
}
