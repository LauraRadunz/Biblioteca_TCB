package br.edu.ifpr.biblioteca.model.dao;

import br.edu.ifpr.biblioteca.model.Cliente;
import br.edu.ifpr.biblioteca.model.Emprestimo;
import br.edu.ifpr.biblioteca.model.Livro;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class EmprestimoDAO {

    public static Emprestimo inserirEmprestimo(Emprestimo emp) {

        String sql = "INSERT INTO Emprestimo (idCliente, idLivro, idFuncionario, dataEmprestimo, dataDevolucaoPrevista, devolvido, renovacoes) " + "VALUES (?, ?, ?, ?, ?, ?, ?)";
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
        ArrayList<Emprestimo> lista = new ArrayList<>();
        String sql = "SELECT codEmprestimo, idCliente, idLivro, dataEmprestimo, dataDevolucaoPrevista, renovacoes FROM Emprestimo";
    
        Connection con = ConnectionFactory.getConnection();
    
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
    
            while (rs.next()) {
                Emprestimo emprestimo = new Emprestimo();
    
                emprestimo.setCodEmprestimo(rs.getInt("codEmprestimo"));
    
                String sqlCliente = "SELECT nome, telefone, cpf FROM Cliente WHERE idCliente = ?";
                PreparedStatement psCliente = con.prepareStatement(sqlCliente);
                psCliente.setInt(1, rs.getInt("idCliente"));
                ResultSet rsCliente = psCliente.executeQuery();
                Cliente cliente = null;
                if (rsCliente.next()) {
                    cliente = new Cliente(rsCliente.getString("nome"), rsCliente.getString("telefone"), rsCliente.getString("cpf"));
                }
                emprestimo.setCliente(cliente);
    
                String sqlLivro = "SELECT nome FROM Livro WHERE idLivro = ?";
                PreparedStatement psLivro = con.prepareStatement(sqlLivro);
                psLivro.setInt(1, rs.getInt("idLivro"));
                ResultSet rsLivro = psLivro.executeQuery();
                Livro livro = null;
                if (rsLivro.next()) {
                    livro = new Livro();
                    livro.setNome(rsLivro.getString("nome"));
                }
                emprestimo.setLivro(livro);
    
                emprestimo.setDataEmprestimo(rs.getDate("dataEmprestimo").toLocalDate());
                emprestimo.setDataDevolucaoPrevista(rs.getDate("dataDevolucaoPrevista").toLocalDate());
                emprestimo.setRenovacoes(rs.getInt("renovacoes"));
    
                lista.add(emprestimo);
            }
    
            return lista;
    
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static LocalDate devolverLivroDAO(int idEmprestimo, LocalDate dataDigitada) {
    Connection con = ConnectionFactory.getConnection();
    
    try {
        String sqlBusca = "SELECT idLivro, dataDevolucaoPrevista, devolvido FROM Emprestimo WHERE idEmprestimo = ?";
        PreparedStatement psBusca = con.prepareStatement(sqlBusca);
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
        Connection con = ConnectionFactory.getConnection();
        String sql = "SELECT idEmprestimo, dataDevolucaoPrevista, devolvido, renovacoes FROM Emprestimo WHERE idEmprestimo = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idEmprestimo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Emprestimo e = new Emprestimo();
                e.setCodEmprestimo(rs.getInt("idEmprestimo"));
                e.setDataDevolucaoPrevista(rs.getDate("dataDevolucaoPrevista").toLocalDate());
                e.setDevolvido(rs.getBoolean("devolvido"));
                e.setRenovacoes(rs.getInt("renovacoes"));
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
        Connection con = ConnectionFactory.getConnection();
        String sql = "UPDATE Emprestimo SET dataDevolucaoPrevista = ?, renovacoes = ? WHERE idEmprestimo = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
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
        String sql = "SELECT codEmprestimo, idLivro, dataEmprestimo, dataDevolucaoPrevista, renovacoes FROM Emprestimo WHERE idCliente = ? AND devolvido = false";

        Connection con = ConnectionFactory.getConnection();

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idCliente);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Emprestimo emprestimo = new Emprestimo();

                emprestimo.setCodEmprestimo(rs.getInt("codEmprestimo"));

                String sqlLivro = "SELECT nome FROM Livro WHERE idLivro = ?";
                PreparedStatement psLivro = con.prepareStatement(sqlLivro);
                psLivro.setInt(1, rs.getInt("idLivro"));
                ResultSet rsLivro = psLivro.executeQuery();
                Livro livro = null;
                if (rsLivro.next()) {
                    livro = new Livro();
                    livro.setNome(rsLivro.getString("nome"));
                }
                emprestimo.setLivro(livro);

                emprestimo.setDataEmprestimo(rs.getDate("dataEmprestimo").toLocalDate());
                emprestimo.setDataDevolucaoPrevista(rs.getDate("dataDevolucaoPrevista").toLocalDate());
                emprestimo.setRenovacoes(rs.getInt("renovacoes"));

                lista.add(emprestimo);
            }

            return lista;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean removerEmprestimoDAO(int idEmprestimo) {
        Connection con = ConnectionFactory.getConnection();
        String sql = "DELETE FROM Emprestimo WHERE idEmprestimo = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idEmprestimo);

            int linhas = ps.executeUpdate();

            return linhas > 0; // se removeu pelo menos 1 linha, deu certo
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
