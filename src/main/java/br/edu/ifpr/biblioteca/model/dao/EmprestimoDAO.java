package br.edu.ifpr.biblioteca.model.dao;

// ... (imports)
import java.sql.*;
import java.util.ArrayList;

import br.edu.ifpr.biblioteca.model.Cliente;
import br.edu.ifpr.biblioteca.model.Emprestimo;
import br.edu.ifpr.biblioteca.model.Funcionario;
import br.edu.ifpr.biblioteca.model.Livro;

public class EmprestimoDAO {

    public static boolean verificarEmprestimoExistente(int idEmprestimo) throws SQLException {
        String sql = "SELECT EXISTS (SELECT 1 FROM Emprestimo WHERE idEmprestimo = ?)";
        try (Connection con = ConnectionFactory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idEmprestimo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 1;
                }
            }
        }
        return false;
    }

    public static Emprestimo inserirEmprestimo(Emprestimo emp) throws SQLException {
        String sql = "INSERT INTO Emprestimo (idCliente, idLivro, idFuncionario, dataEmprestimo, dataDevolucaoPrevista, devolvido, renovacoes) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConnectionFactory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, emp.getCliente().getRegistro());
            ps.setInt(2, emp.getLivro().getCodigo());
            ps.setInt(3, emp.getFuncionario().getCodigo());
            ps.setDate(4, Date.valueOf(emp.getDataEmprestimo()));
            ps.setDate(5, Date.valueOf(emp.getDataDevolucaoPrevista()));
            ps.setBoolean(6, emp.isDevolvido());
            ps.setInt(7, emp.getRenovacoes());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    emp.setCodEmprestimo(rs.getInt(1));
                }
            }

            return emp; // Retorna o empréstimo com o ID
        }
    }

    public static ArrayList<Emprestimo> listarEmprestimos() throws SQLException {
        ArrayList<Emprestimo> lista = new ArrayList<>();

        String sql = "SELECT " + "e.idEmprestimo, e.dataEmprestimo, e.dataDevolucaoPrevista, e.renovacoes,"
                + "    l.idLivro, l.nome AS nomeLivro, l.autor, "
                + "    c.idCliente, c.nome AS nomeCliente, c.telefone, c.cpf " + "FROM " + "    Emprestimo AS e "
                + "JOIN " + "    Livro AS l ON e.idLivro = l.idLivro " + "JOIN "
                + "    Cliente AS c ON e.idCliente = c.idCliente WHERE e.devolvido = false";

        try (Connection con = ConnectionFactory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Livro livro = new Livro();
                livro.setCodigo(rs.getInt("idLivro"));
                livro.setNome(rs.getString("nomeLivro"));
                livro.setAutor(rs.getString("autor"));

                Cliente cliente = new Cliente();
                cliente.setRegistro(rs.getInt("idCliente"));
                cliente.setNome(rs.getString("nomeCliente"));
                cliente.setTelefone(rs.getString("telefone"));
                cliente.setCpf(rs.getString("cpf"));

                Emprestimo emprestimo = new Emprestimo();
                emprestimo.setCodEmprestimo(rs.getInt("idEmprestimo"));
                emprestimo.setDataEmprestimo(rs.getDate("dataEmprestimo").toLocalDate());
                emprestimo.setLivro(livro);
                emprestimo.setCliente(cliente);
                emprestimo.setDataDevolucaoPrevista(rs.getDate("dataDevolucaoPrevista").toLocalDate());
                emprestimo.setRenovacoes(rs.getInt("renovacoes"));

                lista.add(emprestimo);
            }
        }
        return lista;
    }

    public static boolean removerEmprestimoDAO(int idEmprestimo) throws SQLException {
        String sql = "DELETE FROM Emprestimo WHERE idEmprestimo = ?";
        try (Connection con = ConnectionFactory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idEmprestimo);
            int linhas = ps.executeUpdate();
            return linhas > 0;
        }
    }

    public static ArrayList<Emprestimo> buscarPorClienteDAO(int idCliente) throws SQLException {
        ArrayList<Emprestimo> lista = new ArrayList<>();
        String sql = "SELECT e.idEmprestimo, e.dataEmprestimo, e.dataDevolucaoPrevista, e.renovacoes, " +
                "       l.idLivro, l.nome AS nomeLivro, l.autor, " +
                "       c.idCliente, c.nome AS nomeCliente " +
                "FROM Emprestimo AS e " +
                "JOIN Livro AS l ON e.idLivro = l.idLivro " +
                "JOIN Cliente AS c ON e.idCliente = c.idCliente " +
                "WHERE e.idCliente = ? AND e.devolvido = false";

        try (Connection con = ConnectionFactory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idCliente);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Emprestimo emprestimo = new Emprestimo();
                    emprestimo.setCodEmprestimo(rs.getInt("idEmprestimo"));
                    emprestimo.setDataEmprestimo(rs.getDate("dataEmprestimo").toLocalDate());
                    emprestimo.setDataDevolucaoPrevista(rs.getDate("dataDevolucaoPrevista").toLocalDate());
                    emprestimo.setRenovacoes(rs.getInt("renovacoes"));

                    Livro livro = new Livro();
                    livro.setCodigo(rs.getInt("idLivro"));
                    livro.setNome(rs.getString("nomeLivro"));
                    livro.setAutor(rs.getString("autor"));

                    Cliente cliente = new Cliente();
                    cliente.setRegistro(rs.getInt("idCliente"));
                    cliente.setNome(rs.getString("nomeCliente"));

                    emprestimo.setLivro(livro);
                    emprestimo.setCliente(cliente);
                    lista.add(emprestimo);
                }
            }
        }
        return lista;
    }

    public static Emprestimo buscarPorIdDAO(int idEmprestimo) throws SQLException {
        String sql = "SELECT e.idEmprestimo, e.dataEmprestimo, e.dataDevolucaoPrevista, e.renovacoes, e.devolvido, " +
                "       l.idLivro, l.nome AS nomeLivro, l.autor, l.ano, l.total, l.emprestados, " +
                "       c.idCliente, c.nome AS nomeCliente, c.telefone, c.cpf, " +
                "       f.idFuncionario, f.nome AS nomeFuncionario, f.salario " +
                "FROM Emprestimo AS e " +
                "JOIN Livro AS l ON e.idLivro = l.idLivro " +
                "JOIN Cliente AS c ON e.idCliente = c.idCliente " +
                "JOIN Funcionario AS f ON e.idFuncionario = f.idFuncionario " +
                "WHERE e.idEmprestimo = ?";

        try (Connection con = ConnectionFactory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idEmprestimo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Livro livro = new Livro();
                    livro.setCodigo(rs.getInt("idLivro"));
                    livro.setNome(rs.getString("nomeLivro"));
                    livro.setAutor(rs.getString("autor"));
                    livro.setAno(rs.getInt("ano"));
                    livro.setExemplares(rs.getInt("total"));
                    livro.setEmprestados(rs.getInt("emprestados"));

                    Cliente cliente = new Cliente();
                    cliente.setRegistro(rs.getInt("idCliente"));
                    cliente.setNome(rs.getString("nomeCliente"));
                    cliente.setTelefone(rs.getString("telefone"));
                    cliente.setCpf(rs.getString("cpf"));

                    Funcionario func = new Funcionario();
                    func.setCodigo(rs.getInt("idFuncionario"));
                    func.setNome(rs.getString("nomeFuncionario"));
                    func.setSalario(rs.getFloat("salario"));

                    Emprestimo emprestimo = new Emprestimo();
                    emprestimo.setCodEmprestimo(rs.getInt("idEmprestimo"));
                    emprestimo.setDataEmprestimo(rs.getDate("dataEmprestimo").toLocalDate());
                    emprestimo.setDataDevolucaoPrevista(rs.getDate("dataDevolucaoPrevista").toLocalDate());
                    emprestimo.setRenovacoes(rs.getInt("renovacoes"));
                    emprestimo.setDevolvido(rs.getBoolean("devolvido"));

                    emprestimo.setLivro(livro);
                    emprestimo.setCliente(cliente);
                    emprestimo.setFuncionario(func);

                    return emprestimo;
                }
            }
        }
        return null; // Não encontrou
    }

    public static boolean atualizarEmprestimoDAO(Emprestimo emp) throws SQLException {
        String sql = "UPDATE Emprestimo SET dataDevolucaoPrevista = ?, renovacoes = ? WHERE idEmprestimo = ?";

        try (Connection con = ConnectionFactory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, java.sql.Date.valueOf(emp.getDataDevolucaoPrevista()));
            ps.setInt(2, emp.getRenovacoes());
            ps.setInt(3, emp.getCodEmprestimo());

            int linhasAfetadas = ps.executeUpdate();
            return linhasAfetadas > 0; // Retorna true se atualizou 1 linha
        }
    }
}