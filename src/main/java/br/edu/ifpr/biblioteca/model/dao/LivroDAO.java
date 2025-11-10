package br.edu.ifpr.biblioteca.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import br.edu.ifpr.biblioteca.model.Livro;

public class LivroDAO {
    // Create
    public static void salvarLivro(Livro livro) {
        String sqlLivro = "INSERT INTO Livro(nome,ano,autor,total,emprestados) VALUES(?,?,?,?,?)";
        Connection con = ConnectionFactory.getConnection();
        try {
            PreparedStatement psLivro = con.prepareStatement(sqlLivro);
            psLivro.setString(1, livro.getNome());
            psLivro.setInt(2, livro.getAno());
            psLivro.setString(3, livro.getAutor());
            psLivro.setInt(4, livro.getExemplares());
            psLivro.setInt(5, livro.getEmprestados());

            psLivro.executeUpdate();
            System.out.println("Livro inserido com sucesso");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void removerLivroDAO(int codigo) {
        String sqlLivro = "DELETE FROM Livro WHERE idLivro = ?";
        Connection con = ConnectionFactory.getConnection();

        try {
            PreparedStatement psLivro = con.prepareStatement(sqlLivro);

            // Define o valor do parâmetro (substitui o "?")
            psLivro.setInt(1, codigo);

            // Executa o comando
            int linhasAfetadas = psLivro.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("Livro removido com sucesso!");
            } else {
                System.out.println("Nenhum livro encontrado com esse ID.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
