package br.edu.ifpr.biblioteca.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import br.edu.ifpr.biblioteca.model.Livro;

public class LivroDAO {

    public static boolean verificarLivroExistente(int idLivro) throws SQLException {
        String sql = "SELECT EXISTS (SELECT 1 FROM Livro WHERE idLivro = ?)";

        try (Connection con = ConnectionFactory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idLivro);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 1;
                }
            }
        }
        return false;
    }

    public static void inserirLivro(Livro livro) throws SQLException {
        String sqlLivro = "INSERT INTO Livro(nome,ano,autor,total,emprestados) VALUES(?,?,?,?,?)";

        try (Connection con = ConnectionFactory.getConnection();
                PreparedStatement psLivro = con.prepareStatement(sqlLivro)) {

            psLivro.setString(1, livro.getNome());
            psLivro.setInt(2, livro.getAno());
            psLivro.setString(3, livro.getAutor());
            psLivro.setInt(4, livro.getExemplares()); //
            psLivro.setInt(5, livro.getEmprestados()); //

            psLivro.executeUpdate();

        }
    }

    public static ArrayList<Livro> listarLivrosDAO() throws SQLException {
        ArrayList<Livro> lista = new ArrayList<>();
        String sql = "SELECT * FROM Livro";

        try (Connection con = ConnectionFactory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Livro livro = new Livro();
                livro.setCodigo(rs.getInt("idLivro"));
                livro.setNome(rs.getString("nome"));
                livro.setAno(rs.getInt("ano"));
                livro.setAutor(rs.getString("autor"));
                livro.setExemplares(rs.getInt("total"));
                livro.setEmprestados(rs.getInt("emprestados"));
                lista.add(livro);
            }
        }
        return lista; // Retorna a lista (ou vazia, ou cheia)
    }

    public static Livro buscarLivroPorCodigoDAO(int codigo) throws SQLException {
        String sql = "SELECT * FROM Livro WHERE idLivro = ?";

        try (Connection con = ConnectionFactory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Livro livro = new Livro();
                    livro.setCodigo(rs.getInt("idLivro"));
                    livro.setNome(rs.getString("nome"));
                    livro.setAno(rs.getInt("ano"));
                    livro.setAutor(rs.getString("autor"));
                    livro.setExemplares(rs.getInt("total"));
                    livro.setEmprestados(rs.getInt("emprestados"));
                    return livro;
                }
            }
        }
        return null; // Retorno silencioso
    }

    public static ArrayList<Livro> buscarLivrosPorTituloDAO(String palavra) throws SQLException {
        ArrayList<Livro> lista = new ArrayList<>();
        String sql = "SELECT * FROM Livro WHERE nome LIKE ?";

        try (Connection con = ConnectionFactory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + palavra + "%"); // Adiciona os curingas

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Livro livro = new Livro();
                    livro.setCodigo(rs.getInt("idLivro"));
                    livro.setNome(rs.getString("nome"));
                    livro.setAno(rs.getInt("ano"));
                    livro.setAutor(rs.getString("autor"));
                    livro.setExemplares(rs.getInt("total"));
                    livro.setEmprestados(rs.getInt("emprestados"));
                    lista.add(livro);
                }
            }
        }
        return lista;
    }

    public static void atualizarEmprestados(Livro livro) throws SQLException {
        String sql = "UPDATE Livro SET emprestados = ? WHERE idLivro = ?";

        try (Connection con = ConnectionFactory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, livro.getEmprestados());
            ps.setInt(2, livro.getCodigo());
            ps.executeUpdate();
        }
    }

    public static boolean removerLivroDAO(int codigo) throws SQLException {
        String sqlLivro = "DELETE FROM Livro WHERE idLivro = ?";

        try (Connection con = ConnectionFactory.getConnection();
                PreparedStatement psLivro = con.prepareStatement(sqlLivro)) {
            psLivro.setInt(1, codigo);
            int linhasAfetadas = psLivro.executeUpdate();

            return linhasAfetadas > 0;
        }
    }
}