package br.edu.ifpr.biblioteca.controller;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import br.edu.ifpr.biblioteca.model.Funcionario;
import br.edu.ifpr.biblioteca.model.dao.FuncionarioDAO;

public class FuncionarioController {

    public static void cadastrarFuncionario(Funcionario f) {
        if (f.getNome() == null || f.getNome().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nome não pode ser vazio, funcionário não cadastrado.");
            return;
        }
        FuncionarioDAO.cadastrarFuncionarioDAO(f);
    }

    public static ArrayList<Funcionario> listarFuncionarios() {
        return FuncionarioDAO.listarFuncionariosDAO();
    }

    public static Funcionario buscarFuncionario(int codigo) {
        Funcionario funcionario = FuncionarioDAO.buscarFuncionarioDAO(codigo);
        if (funcionario != null) {
            return funcionario;
        } else {
            JOptionPane.showMessageDialog(null,"Funcionário não encontrado");
            return null;
        }
    }

    public static void removerFuncionario(int codigo) {
        FuncionarioDAO.removerFuncionarioDAO(codigo);
    }
}

