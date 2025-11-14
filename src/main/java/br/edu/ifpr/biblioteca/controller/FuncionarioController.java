package br.edu.ifpr.biblioteca.controller;
import java.sql.SQLException; 
import java.util.ArrayList;
import br.edu.ifpr.biblioteca.model.Funcionario;
import br.edu.ifpr.biblioteca.model.dao.FuncionarioDAO;

public class FuncionarioController {

    public static void cadastrarFuncionario(Funcionario f) throws SQLException, IllegalArgumentException {
        if (f.getNome() == null || f.getNome().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio");
        }
        FuncionarioDAO.cadastrarFuncionarioDAO(f); 
    }

    public static ArrayList<Funcionario> listarFuncionarios() throws SQLException {
        return FuncionarioDAO.listarFuncionariosDAO();
    }

    public static Funcionario buscarFuncionario(int codigo) throws SQLException {
        return FuncionarioDAO.buscarFuncionarioDAO(codigo); 
    }

    public static boolean removerFuncionario(int codigo) throws SQLException {
        return FuncionarioDAO.removerFuncionarioDAO(codigo);
    }
}