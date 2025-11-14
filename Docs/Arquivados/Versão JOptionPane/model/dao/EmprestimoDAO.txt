package br.edu.ifpr.biblioteca.model.dao;

import br.edu.ifpr.biblioteca.model.Cliente;
import br.edu.ifpr.biblioteca.model.Emprestimo;
import br.edu.ifpr.biblioteca.model.Funcionario;
import br.edu.ifpr.biblioteca.model.Livro;
import java.time.LocalDate;
import java.sql.*;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class EmprestimoDAO {

    public static boolean verificarEmprestimoExistente(int idEmprestimo) {
        String sql = "SELECT EXISTS (SELECT 1 FROM Emprestimo WHERE idEmprestimo = ?)";

        // Todos os recursos abertos aqui (con, ps, rs) serão fechados automaticamente
        try (Connection con = ConnectionFactory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idEmprestimo);

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

    public static Emprestimo inserirEmprestimo(Emprestimo emp) {

        String sql = "INSERT INTO Emprestimo (idCliente, idLivro, idFuncionario, dataEmprestimo, dataDevolucaoPrevista, devolvido, renovacoes) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConnectionFactory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

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
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public static LocalDate devolverLivroDAO(int idEmprestimo, LocalDate dataDigitada) {
        String sqlBusca = "SELECT idLivro, dataDevolucaoPrevista, devolvido FROM Emprestimo WHERE idEmprestimo = ?";

        try (Connection con = ConnectionFactory.getConnection();
                PreparedStatement psBusca = con.prepareStatement(sqlBusca)) {
            psBusca.setInt(1, idEmprestimo);
            ResultSet rs = psBusca.executeQuery();

            if (!rs.next()) {
                JOptionPane.showMessageDialog(null, "Empréstimo não encontrado.");
                return null;
            }

            boolean devolvido = rs.getBoolean("devolvido");
            if (devolvido) {
                JOptionPane.showMessageDialog(null, "Este empréstimo já foi devolvido.");
                return null;
            }

            int idLivro = rs.getInt("idLivro");
            LocalDate dataPrevista = rs.getDate("dataDevolucaoPrevista").toLocalDate();

            String sqlUpdate = "UPDATE Emprestimo SET dataDevolucaoEfetiva = ?, devolvido = ? WHERE idEmprestimo = ?";
            PreparedStatement psUpdate = con.prepareStatement(sqlUpdate);
            psUpdate.setDate(1, java.sql.Date.valueOf(dataDigitada));
            psUpdate.setBoolean(2, true);
            psUpdate.setInt(3, idEmprestimo);
            psUpdate.executeUpdate();

            String sqlLivro = "UPDATE Livro SET emprestados = emprestados - 1 WHERE idLivro = ?";
            PreparedStatement psLivro = con.prepareStatement(sqlLivro);
            psLivro.setInt(1, idLivro);
            psLivro.executeUpdate();

            return dataPrevista;

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao devolver o livro!");
            return null;
        }
    }

    public static Emprestimo buscarEmprestimoPorId(int idEmprestimo) {
        String sql = "SELECT e.idEmprestimo, e.dataEmprestimo, e.dataDevolucaoPrevista, e.devolvido, e.renovacoes, l.nome, e.idLivro, l.autor, c.nome, e.idCliente, e.idFuncionario, f.nome FROM Emprestimo e JOIN Livro l ON l.idLivro = e.idLivro JOIN Cliente c ON c.idCliente = e.idCliente JOIN Funcionario f ON f.idFuncionario = e.idFuncionario WHERE idEmprestimo = ?";

        try (Connection con = ConnectionFactory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, idEmprestimo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Emprestimo e = new Emprestimo();
                e.setCodEmprestimo(rs.getInt("e.idEmprestimo"));
                e.setDataEmprestimo(rs.getDate("e.dataEmprestimo").toLocalDate());
                e.setDataDevolucaoPrevista(rs.getDate("e.dataDevolucaoPrevista").toLocalDate());
                e.setDevolvido(rs.getBoolean("e.devolvido"));
                e.setRenovacoes(rs.getInt("e.renovacoes"));

                Livro livro = new Livro();
                livro.setCodigo(rs.getInt("e.idLivro"));
                livro.setNome(rs.getString("nome"));
                livro.setAutor(rs.getString("autor"));

                Funcionario funcionario = new Funcionario();
                funcionario.setCodigo(rs.getInt("e.idFuncionario"));
                funcionario.setNome(rs.getString("f.nome"));

                Cliente cliente = new Cliente();
                cliente.setRegistro(rs.getInt("e.idCliente"));
                cliente.setNome(rs.getString("c.nome"));

                e.setLivro(livro);
                e.setFuncionario(funcionario);
                e.setCliente(cliente);

                con.close();
                return e;
            }

            con.close();
            return null;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static boolean renovarLivroDAO(int idEmprestimo, LocalDate novaData, int novasRenovacoes) {
        String sql = "UPDATE Emprestimo SET dataDevolucaoPrevista = ?, renovacoes = ? WHERE idEmprestimo = ?";

        try (Connection con = ConnectionFactory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(novaData));
            ps.setInt(2, novasRenovacoes);
            ps.setInt(3, idEmprestimo);

            int linhas = ps.executeUpdate();
            con.close();

            return linhas > 0; // se atualizou pelo menos 1 linha, deu certo
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static ArrayList<Emprestimo> buscarEmprestimosAbertosPorClienteDAO(int idCliente) {
        ArrayList<Emprestimo> lista = new ArrayList<>();
        String sql = "SELECT e.idEmprestimo, e.idLivro, e.dataEmprestimo, e.dataDevolucaoPrevista, e.renovacoes, c.nome, c.idCliente, l.idLivro, l.nome, l.autor FROM Emprestimo e JOIN Livro l ON l.idLivro = e.idLivro JOIN Cliente c ON c.idCliente = e.idCliente WHERE e.idCliente = ? AND e.devolvido = false";

        try (Connection con = ConnectionFactory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);) {
            ps.setInt(1, idCliente);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Emprestimo emprestimo = new Emprestimo();

                emprestimo.setCodEmprestimo(rs.getInt("e.idEmprestimo"));
                emprestimo.setDataEmprestimo(rs.getDate("e.dataEmprestimo").toLocalDate());
                emprestimo.setDataDevolucaoPrevista(rs.getDate("e.dataDevolucaoPrevista").toLocalDate());
                emprestimo.setRenovacoes(rs.getInt("e.renovacoes"));

                Livro livro = new Livro();
                livro.setCodigo(rs.getInt("l.idLivro"));
                livro.setNome(rs.getString("l.nome"));
                livro.setAutor(rs.getString("l.autor"));

                Cliente cliente = new Cliente();
                cliente.setRegistro(rs.getInt("c.idCliente"));
                cliente.setNome(rs.getString("c.nome"));

                emprestimo.setLivro(livro);
                emprestimo.setCliente(cliente);

                lista.add(emprestimo);
            }

            return lista;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean removerEmprestimoDAO(int idEmprestimo) {
        String sql = "DELETE FROM Emprestimo WHERE idEmprestimo = ?";

        try (Connection con = ConnectionFactory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idEmprestimo);

            int linhas = ps.executeUpdate();

            return linhas > 0; // se removeu pelo menos 1 linha, deu certo
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
