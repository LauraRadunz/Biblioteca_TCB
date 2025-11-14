package br.edu.ifpr.biblioteca.view; 

import br.edu.ifpr.biblioteca.controller.ClienteController;
import br.edu.ifpr.biblioteca.controller.EmprestimoController; // Importa o outro controller!
import br.edu.ifpr.biblioteca.model.Cliente;
import br.edu.ifpr.biblioteca.model.Emprestimo; // Importa o modelo
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class PainelClientes extends JPanel {

    private JButton btnCadastrar, btnListar, btnRemover, btnBuscarDetalhes; // NOVO BOTÃO
    private JTable tabela;
    private DefaultTableModel modeloTabela;

    public PainelClientes() {
        setLayout(new BorderLayout());
        
        JPanel painelBotoes = new JPanel();
        btnCadastrar = new JButton("Cadastrar Novo Cliente");
        btnListar = new JButton("Listar/Atualizar Clientes");
        btnRemover = new JButton("Remover Cliente Selecionado");
        btnBuscarDetalhes = new JButton("Buscar Detalhes (com Empréstimos)"); // NOVO

        painelBotoes.add(btnCadastrar);
        painelBotoes.add(btnListar);
        painelBotoes.add(btnRemover);
        painelBotoes.add(btnBuscarDetalhes); // NOVO

        String[] colunas = {"Registro", "Nome", "Telefone", "CPF"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabela = new JTable(modeloTabela);

        add(painelBotoes, BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        // --- Ações dos Botões ---
        btnListar.addActionListener(e -> atualizarTabela());
        btnCadastrar.addActionListener(e -> cadastrarCliente());
        btnRemover.addActionListener(e -> removerCliente());
        btnBuscarDetalhes.addActionListener(e -> buscarClienteDetalhes()); // NOVO
        
        atualizarTabela(); 
    }

    private void atualizarTabela() {
        // ... (igual ao que já refatoramos) ...
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
        // ... (igual ao que já refatoramos) ...
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
        // ... (igual ao que já refatoramos) ...
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
    
    // --- NOVO MÉTODO (BUSCA COMPLEXA) ---
    private void buscarClienteDetalhes() {
        try {
            int codigo = Integer.parseInt(JOptionPane.showInputDialog(this, "Digite o REGISTRO do cliente:"));
            
            // 1. Busca os dados do Cliente
            Cliente cliente = ClienteController.buscarCliente(codigo);
            if (cliente == null) {
                JOptionPane.showMessageDialog(this, "Nenhum cliente encontrado com o registro " + codigo, "Não Encontrado", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // 2. Busca os Empréstimos ABERTOS desse cliente
            ArrayList<Emprestimo> emprestimos = EmprestimoController.buscarEmprestimosPorCliente(codigo);
            
            // 3. Monta a mensagem de exibição
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
            
            // 4. Exibe o resultado em um pop-up (usando JTextArea para formatar)
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