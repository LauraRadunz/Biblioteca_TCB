package br.edu.ifpr.biblioteca.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import br.edu.ifpr.biblioteca.model.Livro;

public class LivroDAO {

    public static boolean verificarLivroExistente(int idLivro) {
        String sql = "SELECT EXISTS (SELECT 1 FROM Livro WHERE idLivro = ?)";

        // Todos os recursos abertos aqui (con, ps, rs) serão fechados automaticamente
        try (Connection con = ConnectionFactory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idLivro);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt(1) == 1;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void inserirLivro(Livro livro) {
        String sqlLivro = "INSERT INTO Livro(nome,ano,autor,total,emprestados) VALUES(?,?,?,?,?)";

        try (Connection con = ConnectionFactory.getConnection();
                PreparedStatement psLivro = con.prepareStatement(sqlLivro)) {
            psLivro.setString(1, livro.getNome());
            psLivro.setInt(2, livro.getAno());
            psLivro.setString(3, livro.getAutor());
            psLivro.setInt(4, livro.getExemplares());
            psLivro.setInt(5, livro.getEmprestados());

            psLivro.executeUpdate();
            JOptionPane.showMessageDialog(null, "Livro inserido com sucesso");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static Livro buscarLivroPorCodigoDAO(int idLivro) {
        String sql = "SELECT * FROM Livro WHERE idLivro = ?";
        Livro livroEncontrado = null;
        try (Connection con = ConnectionFactory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idLivro);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    livroEncontrado = new Livro();
                    livroEncontrado.setCodigo(idLivro);
                    livroEncontrado.setNome(rs.getString("nome"));
                    livroEncontrado.setAutor(rs.getString("autor"));
                    livroEncontrado.setAno(rs.getInt("ano"));
                    livroEncontrado.setExemplares(rs.getInt("total"));
                    livroEncontrado.setEmprestados(rs.getInt("emprestados"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return livroEncontrado;
    }

    public static ArrayList<Livro> listarLivrosDAO() {
        ArrayList<Livro> lista = new ArrayList<>();
        String sql = "SELECT * FROM Livro";

        try (Connection con = ConnectionFactory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Livro livro = new Livro();
                livro.setCodigo(rs.getInt("idLivro"));
                livro.setNome(rs.getString("nome"));
                livro.setAutor(rs.getString("autor"));
                livro.setAno(rs.getInt("ano"));
                livro.setExemplares(rs.getInt("total"));
                livro.setEmprestados(rs.getInt("emprestados"));

                lista.add(livro);
            }

            rs.close();
            ps.close();

            return lista;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<Livro> buscarLivrosPorTituloDAO(String palavra) {
        ArrayList<Livro> lista = new ArrayList<>();

        String sql = "SELECT * FROM Livro WHERE nome LIKE ?";

        try (Connection con = ConnectionFactory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + palavra + "%"); // com % para pegar partes

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Livro livro = new Livro();
                livro.setCodigo(rs.getInt("idLivro"));
                livro.setNome(rs.getString("nome"));
                livro.setAutor(rs.getString("autor"));
                livro.setAno(rs.getInt("ano"));
                livro.setExemplares(rs.getInt("total"));
                livro.setEmprestados(rs.getInt("emprestados"));
                lista.add(livro);
            }

            rs.close();
            ps.close();

            return lista;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void atualizarEmprestados(Livro livro) {
        String sql = "UPDATE Livro SET emprestados = ? WHERE idLivro = ?";

        try (Connection con = ConnectionFactory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, livro.getEmprestados());
            ps.setInt(2, livro.getCodigo());

            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removerLivroDAO(int codigo) {
        String sqlLivro = "DELETE FROM Livro WHERE idLivro = ?";

        try (Connection con = ConnectionFactory.getConnection();
                PreparedStatement psLivro = con.prepareStatement(sqlLivro)) {
            psLivro.setInt(1, codigo);

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
