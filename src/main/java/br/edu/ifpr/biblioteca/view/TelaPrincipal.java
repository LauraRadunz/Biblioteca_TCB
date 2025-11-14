package br.edu.ifpr.biblioteca.view; 

import javax.swing.*;

public class TelaPrincipal extends JFrame {

    private JTabbedPane abas; 

    public TelaPrincipal() {
        setTitle("Sistema de Gerenciamento da Biblioteca");
        setSize(1024, 768); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 

        abas = new JTabbedPane();

        // 1. Aba de Livros 
        PainelLivros painelLivros = new PainelLivros();
        abas.addTab("Livros", painelLivros);

        // 2. Aba de Clientes
        PainelClientes painelClientes = new PainelClientes();
        abas.addTab("Clientes", painelClientes);

        // 3. Aba de Funcionários
        PainelFuncionarios painelFunc = new PainelFuncionarios();
        abas.addTab("Funcionários", painelFunc);

        // 4. Aba de Empréstimos
        PainelEmprestimos painelEmp = new PainelEmprestimos();
        abas.addTab("Empréstimos", painelEmp);

        // Adiciona o painel de abas na janela principal
        add(abas);
    }
}