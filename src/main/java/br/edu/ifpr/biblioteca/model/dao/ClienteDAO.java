package br.edu.ifpr.biblioteca.model.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import br.edu.ifpr.biblioteca.model.Cliente;

public class ClienteDAO {

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
            con.close();

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
            con.close();

        } catch(Exception e) {
            e.printStackTrace();
        }

        return lista;
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
            con.close();

        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void removerClienteDAO(int codigo) {
        String sql = "DELETE FROM Cliente WHERE idCliente = ?";
        Connection con = ConnectionFactory.getConnection();

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, codigo);
            ps.executeUpdate();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}

