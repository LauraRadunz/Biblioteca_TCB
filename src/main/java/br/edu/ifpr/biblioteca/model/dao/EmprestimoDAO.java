package br.edu.ifpr.biblioteca.model.dao;

import br.edu.ifpr.biblioteca.model.Cliente;
import br.edu.ifpr.biblioteca.model.Emprestimo;
import br.edu.ifpr.biblioteca.model.Livro;

import java.sql.*;
import java.util.ArrayList;

public class EmprestimoDAO {

    public static Emprestimo inserirEmprestimo(Emprestimo emp) {

        String sql = "INSERT INTO Emprestimo (idCliente, idLivro, idFuncionario, dataEmprestimo, dataDevolucaoPrevista, devolvido, renovacoes) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection con = ConnectionFactory.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, emp.getCliente().getRegistro());
            ps.setInt(2, emp.getLivro().getCodigo());
            ps.setInt(3, emp.getFuncionario().getCodigo());
            ps.setDate(4, java.sql.Date.valueOf(emp.getDataEmprestimo()));
            ps.setDate(5, java.sql.Date.valueOf(emp.getDataDevolucaoPrevista()));
            ps.setBoolean(6, emp.isDevolvido());
            ps.setInt(7, emp.getRenovacoes());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int novoId = rs.getInt(1);
                emp.setCodEmprestimo(novoId);
            }

            return emp; // agora o objeto tem o ID

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<Emprestimo> listarEmprestimos() {
        String sql = "SELECT FROM Emprestimo (codEmprestimo, idCliente, idLivro, dataEmprestimo, dataDevolucaoPrevista, renovacoes)";
        Connection con = ConnectionFactory.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Emprestimo emprestimo = new Emprestimo();

                emprestimo.setCodEmprestimo(rs.getInt("CodEmprestimo"));
                String sqlCliente = "SELECT FROM Cliente (nome, telefone, cpf) where idCliente = " + rs.getInt("idCliente");
                ResultSet rsCliente = ps.executeQuery();
                Cliente cliente = new Cliente(rsCliente.getString("nome"), rsCliente.getString("telefone"), rsCliente.getString("cpf"));
                emprestimo.setCliente(cliente);

                String sqlLivro = "SELECT FROM Livro (nome) where idLivro = " + rs.getInt("idLivro");
                ResultSet rsLivro = ps.executeQuery();
                Livro livro = new Livro();
                livro.setNome(rsLivro.getString("nome"));
                emprestimo.setLivro(livro);

                emprestimo.setDataEmprestimo(rs.getDate("dataEmprestimo"));


                
            }

        } catch(SQLException e) {
            e.printStackTrace();
            return null;
        }

    }
}
