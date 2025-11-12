package br.edu.ifpr.biblioteca.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import br.edu.ifpr.biblioteca.model.Cliente;
import br.edu.ifpr.biblioteca.model.Emprestimo;

public class ClienteDAO {

    public static boolean verificarClienteExistente(int idCliente) {
        String sql = "SELECT EXISTS (SELECT 1 FROM Cliente WHERE idCliente = ?)";

        // Todos os recursos abertos aqui (con, ps, rs) serão fechados automaticamente
        try (Connection con = ConnectionFactory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idCliente);

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

    public static void cadastrarClienteDAO(Cliente c) {
        String sql = "INSERT INTO Cliente (nome, telefone, cpf) VALUES (?,?,?)";

        try (Connection con = ConnectionFactory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getNome());
            ps.setString(2, c.getTelefone());
            ps.setString(3, c.getCpf());

            ps.executeUpdate();

            ps.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Cliente> listarClientesDAO() {
        ArrayList<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM Cliente";

        try (Connection con = ConnectionFactory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Cliente c = new Cliente();
                c.setRegistro(rs.getInt("idCliente"));
                c.setNome(rs.getString("nome"));
                c.setTelefone(rs.getString("telefone"));
                c.setCpf(rs.getString("cpf"));
                lista.add(c);
            }

            rs.close();
            ps.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public static String buscarClienteeEmprestimosDAO(int codigo) {
        String sql = "SELECT * FROM Cliente WHERE idCliente = ?";

        try (Connection con = ConnectionFactory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, codigo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Cliente c = new Cliente();
                c.setRegistro(rs.getInt("idCliente"));
                c.setNome(rs.getString("nome"));
                c.setTelefone(rs.getString("telefone"));
                c.setCpf(rs.getString("cpf"));
                
                String mensagem = "Registro: " + c.getRegistro() + " | Nome: " + c.getNome() + " | Telefone: " + c.getTelefone() + " | CPF: " + c.getCpf() + "\nEmpréstimos em aberto desse cliente:\n";
                ArrayList<Emprestimo> emprestimosAtivos = new ArrayList<>();
                emprestimosAtivos = EmprestimoDAO.buscarEmprestimosAbertosPorClienteDAO(codigo);
                
                if (!emprestimosAtivos.isEmpty()) {
                    for (Emprestimo emp : emprestimosAtivos) {
                        mensagem += "Código: " + emp.getCodEmprestimo() + "\nLivro: " + emp.getLivro().getNome() + " ("
                                + emp.getLivro().getAutor() + "\nData Empréstimo: " + emp.getDataEmprestimo()
                                + " | Data Devolução Prevista: " + emp.getDataDevolucaoPrevista() + "\nRenovações: "
                                + emp.getRenovacoes() + "\n\n";
                    }
                } else {
                    mensagem += "Nenhum empréstimo em aberto para esse cliente.\n";
                }
                return mensagem;
            }

            ps.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Cliente buscarClienteDAO(int codigo) {
        String sql = "SELECT * FROM Cliente WHERE idCliente = ?";

        try (Connection con = ConnectionFactory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, codigo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Cliente c = new Cliente();
                c.setRegistro(rs.getInt("idCliente"));
                c.setNome(rs.getString("nome"));
                c.setTelefone(rs.getString("telefone"));
                c.setCpf(rs.getString("cpf"));

                return c;
            }

            ps.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean removerClienteDAO(int codigo) {
        String sql = "DELETE FROM Cliente WHERE idCliente = ?";

        try (Connection con = ConnectionFactory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, codigo);
            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
