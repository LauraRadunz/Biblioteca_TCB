package br.edu.ifpr.biblioteca.controller;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import br.edu.ifpr.biblioteca.model.Cliente;
import br.edu.ifpr.biblioteca.model.dao.ClienteDAO;

public class ClienteController {

    public static void cadastrarCliente(Cliente c) {
        if (c != null) {
        ClienteDAO.cadastrarClienteDAO(c);
        } else {
            JOptionPane.showMessageDialog(null,"Cliente não pode ser nulo");
        }
    }   

    public static ArrayList<Cliente> listarClientes() {
        return ClienteDAO.listarClientesDAO();
    }

    public static Cliente buscarCliente(int codigo) {
        return ClienteDAO.buscarClienteDAO(codigo);
    }

    public static void buscarClienteEEmprestimos(int codigo) {
        String mensagem = ClienteDAO.buscarClienteeEmprestimosDAO(codigo);
        JOptionPane.showMessageDialog(null, mensagem);

    }

    public static void removerCliente(int codigo) {
        if (ClienteDAO.removerClienteDAO(codigo)) {
            JOptionPane.showMessageDialog(null, "Cliente removido com sucesso!");
        }
    }
}

