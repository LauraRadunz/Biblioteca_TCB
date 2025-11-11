package br.edu.ifpr.biblioteca.dao;

import br.edu.ifpr.biblioteca.model.Emprestimo;
import java.sql.*;
import br.edu.ifpr.biblioteca.conexao.Conexao;

public class EmprestimoDAO {

    public Emprestimo inserir(Emprestimo emp) {

        String sql = "INSERT INTO Emprestimo (idCliente, idLivro, idFuncionario, dataEmprestimo, dataDevolucaoPrevista, devolvido, renovacoes) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, emp.getCliente().getRegistro());
            stmt.setInt(2, emp.getLivro().getCodigo());
            stmt.setInt(3, emp.getFuncionario().getCodigo());
            stmt.setDate(4, java.sql.Date.valueOf(emp.getDataEmprestimo()));
            stmt.setDate(5, java.sql.Date.valueOf(emp.getDataDevolucaoPrevista()));
            stmt.setBoolean(6, emp.isDevolvido());
            stmt.setInt(7, emp.getRenovacoes());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
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
}
