package br.edu.ifpr.biblioteca.controller;
import java.util.ArrayList;
import br.edu.ifpr.biblioteca.model.Cliente;
import br.edu.ifpr.biblioteca.model.dao.ClienteDAO;

public class ClienteController {

    public static void cadastrarCliente(Cliente c) {
        ClienteDAO.cadastrarClienteDAO(c);
    }

    public static ArrayList<Cliente> listarClientes() {
        return ClienteDAO.listarClientesDAO();
    }

    public static Cliente buscarCliente(int codigo) {
        return ClienteDAO.buscarClienteDAO(codigo);
    }

    public static void removerCliente(int codigo) {
        ClienteDAO.removerClienteDAO(codigo);
    }
}

