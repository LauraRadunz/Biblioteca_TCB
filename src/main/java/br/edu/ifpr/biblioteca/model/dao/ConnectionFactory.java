package br.edu.ifpr.biblioteca.model.dao; // O seu pacote

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    // --- C do TiDB Cloud ---
    
    // 1. Host (Endereço)
    private static final String HOST = "gateway01.us-east-1.prod.aws.tidb.cloud";
    
    // 2. Port (Porta)
    private static final String PORT = "4000";
    
    // 3. Database (Banco).
    private static final String DATABASE = "test";
    
    // 4. User (Usuário)
    private static final String USER = "2QjTfCmUjXizSPv4.root";
    
    // 5. PASSWORD (Senha)
    private static final String PASSWORD = "uf783Usq9z1PGvoa"; 
    
    // --- Fim da Configuração ---

    // URL de conexão
    private static final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE + "?useSSL=true&requireSSL=true";

    public static Connection getConnection() {
        try {
            // A cada chamada, ele cria uma nova conexão segura com a nuvem
            System.out.println("Conectando ao banco de dados na nuvem...");
            return DriverManager.getConnection(URL, USER, PASSWORD);
            
        } catch (SQLException e) {
            // Lança uma exceção para quem chamou saber que deu erro
            throw new RuntimeException("Erro ao conectar ao banco de dados na nuvem", e);
        }
    }
}