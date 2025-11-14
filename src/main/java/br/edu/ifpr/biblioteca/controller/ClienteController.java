package br.edu.ifpr.biblioteca.controller;
import java.sql.SQLException; // Importe este!
import java.util.ArrayList;
import br.edu.ifpr.biblioteca.model.Cliente;
import br.edu.ifpr.biblioteca.model.dao.ClienteDAO;

public class ClienteController {

    public static void cadastrarCliente(Cliente c) throws SQLException, IllegalArgumentException {
        if (c == null || c.getNome() == null || c.getNome().isEmpty()) {
            throw new IllegalArgumentException("Cliente ou nome do cliente não pode ser nulo/vazio");
        }
        ClienteDAO.cadastrarClienteDAO(c); // Repassa a ordem e exceção, se tiver
    }   

    public static ArrayList<Cliente> listarClientes() throws SQLException {
        return ClienteDAO.listarClientesDAO();
    }

    public static Cliente buscarCliente(int codigo) throws SQLException {
        Cliente cliente = ClienteDAO.buscarClienteDAO(codigo);
        return cliente; // Retorna cliente ou null
    }

    public static boolean removerCliente(int codigo) throws SQLException {
        return ClienteDAO.removerClienteDAO(codigo);
    }
    
}