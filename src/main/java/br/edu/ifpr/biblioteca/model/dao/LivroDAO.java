package br.edu.ifpr.biblioteca.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import br.edu.ifpr.biblioteca.model.Livro;

public class LivroDAO {
    // Create
    public static void inserirLivro(Livro livro) {
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
            JOptionPane.showMessageDialog(null, "Livro inserido com sucesso");	
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static Livro buscarLivroPorCodigoDAO(int codigo) {
        String sqlLivro = "SELECT * FROM Livro WHERE idLivro = ?";
        Connection con = ConnectionFactory.getConnection();

        try {
            PreparedStatement psLivro = con.prepareStatement(sqlLivro);
            psLivro.setInt(1, codigo);

            ResultSet rs = psLivro.executeQuery();

            if (rs.next()) {

                Livro livro = new Livro();
                livro.setCodigo(rs.getInt("idLivro"));
                livro.setNome(rs.getString("nome"));
                livro.setAutor(rs.getString("autor"));
                livro.setAno(rs.getInt("ano")); // se tiver esse campo, exemplo
                livro.setExemplares(rs.getInt("total"));
                livro.setEmprestados(rs.getInt("emprestados"));

                rs.close();
                psLivro.close();
                con.close();

                return livro;
            }

            rs.close();
            psLivro.close();
            con.close();

            // não achou
            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<Livro> listarLivrosDAO() {
        ArrayList<Livro> lista = new ArrayList<>();
        String sql = "SELECT * FROM Livro";
        Connection con = ConnectionFactory.getConnection();

        try {
            PreparedStatement ps = con.prepareStatement(sql);
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
            con.close();

            return lista;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<Livro> buscarLivrosPorTituloDAO(String palavra) {
        ArrayList<Livro> lista = new ArrayList<>();

        String sql = "SELECT * FROM Livro WHERE nome LIKE ?";
        Connection con = ConnectionFactory.getConnection();

        try {
            PreparedStatement ps = con.prepareStatement(sql);
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
            con.close();

            return lista;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void atualizarEmprestados(Livro livro) {
        String sql = "UPDATE Livro SET emprestados = ? WHERE idLivro = ?";
        Connection con = ConnectionFactory.getConnection();
    
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, livro.getEmprestados());
            ps.setInt(2, livro.getCodigo());
    
            ps.executeUpdate();
            ps.close();
            con.close();
        } catch (SQLException e) {
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
