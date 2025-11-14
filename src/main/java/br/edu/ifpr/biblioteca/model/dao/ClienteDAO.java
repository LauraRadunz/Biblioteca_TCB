package br.edu.ifpr.biblioteca.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException; 
import java.util.ArrayList;
import br.edu.ifpr.biblioteca.model.Cliente;

public class ClienteDAO {

    public static boolean verificarClienteExistente(int idCliente) throws SQLException {
        String sql = "SELECT EXISTS (SELECT 1 FROM Cliente WHERE idCliente = ?)";

        try (Connection con = ConnectionFactory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 1;
                }
            }
        }
        return false;
    }

    public static void cadastrarClienteDAO(Cliente c) throws SQLException {
        String sql = "INSERT INTO Cliente (nome, telefone, cpf) VALUES (?,?,?)";

        try (Connection con = ConnectionFactory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getNome());
            ps.setString(2, c.getTelefone());
            ps.setString(3, c.getCpf());
            ps.executeUpdate();
        }
    }

    public static ArrayList<Cliente> listarClientesDAO() throws SQLException {
        ArrayList<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM Cliente";

        try (Connection con = ConnectionFactory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Cliente c = new Cliente();
                c.setRegistro(rs.getInt("idCliente"));
                c.setNome(rs.getString("nome"));
                c.setTelefone(rs.getString("telefone"));
                c.setCpf(rs.getString("cpf"));
                lista.add(c);
            }
        }
        return lista;
    }

    public static Cliente buscarClienteDAO(int codigo) throws SQLException {
        String sql = "SELECT * FROM Cliente WHERE idCliente = ?";

        try (Connection con = ConnectionFactory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Cliente c = new Cliente();
                    c.setRegistro(rs.getInt("idCliente"));
                    c.setNome(rs.getString("nome"));
                    c.setTelefone(rs.getString("telefone"));
                    c.setCpf(rs.getString("cpf"));
                    return c;
                }
            }
        }
        return null; 
    }

    public static boolean removerClienteDAO(int codigo) throws SQLException {
        String sql = "DELETE FROM Cliente WHERE idCliente = ?";

        try (Connection con = ConnectionFactory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, codigo);
            int linhasAfetadas = ps.executeUpdate();
            return linhasAfetadas > 0; // Retorna true se removeu (linhas > 0)
        }
    }

}