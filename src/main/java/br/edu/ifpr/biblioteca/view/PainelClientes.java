package br.edu.ifpr.biblioteca.view; 

import br.edu.ifpr.biblioteca.controller.ClienteController;
import br.edu.ifpr.biblioteca.controller.EmprestimoController; 
import br.edu.ifpr.biblioteca.model.Cliente;
import br.edu.ifpr.biblioteca.model.Emprestimo;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class PainelClientes extends JPanel {

    // Componentes dessa aba
    private JButton btnCadastrar, btnListar, btnRemover, btnBuscarDetalhes; //Pra criar os botões dessa aba
    private JTable tabela; // Tabela de Clientes
    private DefaultTableModel modeloTabela;

    public PainelClientes() {
        setLayout(new BorderLayout());
        
        // Criação dos botões dessa aba
        JPanel painelBotoes = new JPanel(); 
        btnCadastrar = new JButton("Cadastrar Novo Cliente");
        btnListar = new JButton("Listar/Atualizar Clientes");
        btnRemover = new JButton("Remover Cliente Selecionado");
        btnBuscarDetalhes = new JButton("Buscar Detalhes (com Empréstimos)");

        // Adiciona os botões criados no painel
        painelBotoes.add(btnCadastrar);
        painelBotoes.add(btnListar);
        painelBotoes.add(btnRemover);
        painelBotoes.add(btnBuscarDetalhes); 

        String[] colunas = {"Registro", "Nome", "Telefone", "CPF"}; // Colunas da tabela
        modeloTabela = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabela = new JTable(modeloTabela); // Coloca o modelo da tabela na tabela

        add(painelBotoes, BorderLayout.NORTH); // O painel de botões vai ficar na parte de cima
        add(new JScrollPane(tabela), BorderLayout.CENTER); // A tabela vai ficar no centro

        // Ações que cada botão faz
        btnListar.addActionListener(e -> atualizarTabela());
        btnCadastrar.addActionListener(e -> cadastrarCliente());
        btnRemover.addActionListener(e -> removerCliente());
        btnBuscarDetalhes.addActionListener(e -> buscarClienteDetalhes()); 
        
        atualizarTabela(); 
    }

    private void atualizarTabela() {
        modeloTabela.setRowCount(0);
        try {
            ArrayList<Cliente> clientes = ClienteController.listarClientes();
            for (Cliente cliente : clientes) {
                modeloTabela.addRow(new Object[]{
                    cliente.getRegistro(),
                    cliente.getNome(),    
                    cliente.getTelefone(),
                    cliente.getCpf()      
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar clientes: " + e.getMessage(), "Erro de Banco", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cadastrarCliente() {
        try {
            String nome = JOptionPane.showInputDialog(this, "Nome do cliente:");
            String telefone = JOptionPane.showInputDialog(this, "Telefone:");
            String cpf = JOptionPane.showInputDialog(this, "CPF:");
            Cliente novoCliente = new Cliente(nome, telefone, cpf);
            ClienteController.cadastrarCliente(novoCliente);
            JOptionPane.showMessageDialog(this, "Cliente cadastrado com sucesso!");
            atualizarTabela(); 

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar no banco: " + e.getMessage(), "Erro de SQL", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Erro de validação: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro inesperado: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removerCliente() {
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente na tabela.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int registro = (int) modeloTabela.getValueAt(linhaSelecionada, 0);
        int resposta = JOptionPane.showConfirmDialog(this, "Tem certeza?", "Confirmar Remoção", JOptionPane.YES_NO_OPTION);

        if (resposta == JOptionPane.YES_OPTION) {
            try {
                boolean sucesso = ClienteController.removerCliente(registro);
                if (sucesso) {
                    JOptionPane.showMessageDialog(this, "Cliente removido com sucesso!");
                    atualizarTabela();
                } else {
                    JOptionPane.showMessageDialog(this, "Erro: Cliente não encontrado (ID: " + registro + ").", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erro de banco de dados: " + e.getMessage(), "Erro de SQL", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void buscarClienteDetalhes() {
        try {
            int codigo = Integer.parseInt(JOptionPane.showInputDialog(this, "Digite o REGISTRO do cliente:"));
            
            Cliente cliente = ClienteController.buscarCliente(codigo);
            if (cliente == null) {
                JOptionPane.showMessageDialog(this, "Nenhum cliente encontrado com o registro " + codigo, "Não Encontrado", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            ArrayList<Emprestimo> emprestimos = EmprestimoController.buscarEmprestimosPorCliente(codigo);
            
            StringBuilder mensagem = new StringBuilder();
            mensagem.append("--- Dados do Cliente ---\n");
            mensagem.append("Registro: ").append(cliente.getRegistro()).append("\n");
            mensagem.append("Nome: ").append(cliente.getNome()).append("\n");
            mensagem.append("Telefone: ").append(cliente.getTelefone()).append("\n");
            mensagem.append("CPF: ").append(cliente.getCpf()).append("\n");
            
            mensagem.append("\n--- Empréstimos em Aberto (").append(emprestimos.size()).append(") ---\n");
            
            if (emprestimos.isEmpty()) {
                mensagem.append("Nenhum empréstimo em aberto encontrado.");
            } else {
                for (Emprestimo emp : emprestimos) {
                    mensagem.append("  Cód. Empréstimo: ").append(emp.getCodEmprestimo()).append("\n");
                    mensagem.append("  Livro: ").append(emp.getLivro().getNome()).append("\n");
                    mensagem.append("  Data Devolução: ").append(emp.getDataDevolucaoPrevista()).append("\n\n");
                }
            }
            
            // Vai exibir o resultado em um pop-up, usando o JTextArea para formatar
            JTextArea textArea = new JTextArea(mensagem.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 300)); // Tamanho do pop-up
            
            JOptionPane.showMessageDialog(this, scrollPane, "Detalhes do Cliente", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "O registro deve ser um número.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro de banco de dados: " + e.getMessage(), "Erro de SQL", JOptionPane.ERROR_MESSAGE);
        }
    }
}