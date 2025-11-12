package br.edu.ifpr.biblioteca.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static final String URL = "jdbc:mysql://localhost:3306/Biblioteca";
    private static final String USER = "root";
    private static final String PASSWORD = "LRPBDlrpbd2828@";

    public static Connection getConnection() {
        try {
            System.out.println("Criando uma nova conexão com o banco...");
            
            return DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar ao banco de dados", e);
        }
    }
}