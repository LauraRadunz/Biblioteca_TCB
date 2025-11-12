package br.edu.ifpr.biblioteca.model.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import br.edu.ifpr.biblioteca.model.Cliente;
import br.edu.ifpr.biblioteca.model.Emprestimo;

public class ClienteDAO {

    public static boolean verificarClienteExistente(int idCliente) {
        String sql = "SELECT EXISTS (SELECT 1 FROM Cliente WHERE idCliente = 3)";
        Connection con = ConnectionFactory.getConnection();

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idCliente);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                if (rs.equals("1")) {
                    return true;
                }
            }

            ps.close();

        } catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void cadastrarClienteDAO(Cliente c) {
        String sql = "INSERT INTO Cliente (nome, telefone, cpf) VALUES (?,?,?)";
        Connection con = ConnectionFactory.getConnection();

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, c.getNome());
            ps.setString(2, c.getTelefone());
            ps.setString(3, c.getCpf());

            ps.executeUpdate();

            ps.close();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Cliente> listarClientesDAO() {
        ArrayList<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM Cliente";
        Connection con = ConnectionFactory.getConnection();

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                Cliente c = new Cliente();
                c.setRegistro(rs.getInt("idCliente"));
                c.setNome(rs.getString("nome"));
                c.setTelefone(rs.getString("telefone"));
                c.setCpf(rs.getString("cpf"));
                lista.add(c);
            }

            rs.close();
            ps.close();

        } catch(Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public static String buscarClienteeEmprestimosDAO(int codigo) {
        String sql = "SELECT * FROM Cliente WHERE idCliente = ?";
        Connection con = ConnectionFactory.getConnection();

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, codigo);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                Cliente c = new Cliente();
                c.setRegistro(rs.getInt("idCliente"));
                c.setNome(rs.getString("nome"));
                c.setTelefone(rs.getString("telefone"));
                c.setCpf(rs.getString("cpf"));

                ArrayList<Emprestimo> emprestimosAtivos = new ArrayList<>();
                emprestimosAtivos = EmprestimoDAO.buscarEmprestimosAbertosPorClienteDAO(codigo);
                String mensagem = "Registro: " + c.getRegistro() + " | Nome: " + c.getNome() + " | Telefone: " + c.getTelefone() + " | CPF: " + c.getCpf()+"\nEmpréstimos em aberto desse cliente:\n";    
                for(Emprestimo emp : emprestimosAtivos) {
                    mensagem += "Código: " + emp.getCodEmprestimo() + "\nLivro: " + emp.getLivro().getNome() + " (" + emp.getLivro().getAutor() + "\nData Empréstimo: " + emp.getDataEmprestimo() + " | Data Devolução Prevista: " + emp.getDataDevolucaoPrevista() + "\nRenovações: " + emp.getRenovacoes() + "\n\n";
                }
                return mensagem;
            }

            ps.close();

        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Cliente buscarClienteDAO(int codigo) {
        String sql = "SELECT * FROM Cliente WHERE idCliente = ?";
        Connection con = ConnectionFactory.getConnection();

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, codigo);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                Cliente c = new Cliente();
                c.setRegistro(rs.getInt("idCliente"));
                c.setNome(rs.getString("nome"));
                c.setTelefone(rs.getString("telefone"));
                c.setCpf(rs.getString("cpf"));

                return c;
            }

            ps.close();

        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static boolean removerClienteDAO(int codigo) {
        String sql = "DELETE FROM Cliente WHERE idCliente = ?";
        Connection con = ConnectionFactory.getConnection();

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, codigo);
            ps.executeUpdate();
            return true;

        } catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

