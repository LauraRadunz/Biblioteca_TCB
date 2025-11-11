package br.edu.ifpr.biblioteca.controller;

import javax.swing.JOptionPane;

import br.edu.ifpr.biblioteca.model.Emprestimo;
import br.edu.ifpr.biblioteca.model.dao.EmprestimoDAO;

public class EmprestimoController {
    
    public static void realizarEmprestimo(Emprestimo emprestimo) {
        if (emprestimo != null) {
        EmprestimoDAO.inserirEmprestimo(emprestimo);
        JOptionPane.showMessageDialog(null,"Empréstimo realizado com sucesso!");
        } else {
            JOptionPane.showMessageDialog(null,"Empréstimo ão pode ser nulo! Empréstimo não realizado.");
        }
    }

    public static void listarEmprestimos() {
        String mensagem = "Empréstimos em aberto:\n";
        

    }


}
