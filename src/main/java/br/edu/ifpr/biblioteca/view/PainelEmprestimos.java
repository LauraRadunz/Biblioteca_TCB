package br.edu.ifpr.biblioteca.view; 

import br.edu.ifpr.biblioteca.controller.*;
import br.edu.ifpr.biblioteca.model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class PainelEmprestimos extends JPanel {

    // Componentes dessa aba
    private JButton btnRealizarEmprestimo, btnListar, btnDevolucao, btnBuscarPorCodigo, btnRenovar;
    private JTable tabela;
    private DefaultTableModel modeloTabela;

    public PainelEmprestimos() {
        setLayout(new BorderLayout());

        // Painel de botões, vão ficar na parte de cima
        JPanel painelBotoes = new JPanel();
        btnRealizarEmprestimo = new JButton("Realizar Novo Empréstimo");
        btnListar = new JButton("Listar/Atualizar Empréstimos");
        btnDevolucao = new JButton("Realizar Devolução (Remover)");
        btnBuscarPorCodigo = new JButton("Buscar por Cód. Empréstimo");
        btnRenovar = new JButton("Renovar");

        painelBotoes.add(btnRealizarEmprestimo);
        painelBotoes.add(btnListar);
        painelBotoes.add(btnDevolucao);
        painelBotoes.add(btnBuscarPorCodigo);
        painelBotoes.add(btnRenovar); 

        // Coisas da tabela, que vai ficar no centro
        String[] colunas = {"Cód. Empréstimo", "Livro", "Cliente", "Data Empréstimo", "Data Prevista", "Renovações"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabela = new JTable(modeloTabela);

        // Vai adicionar os paineis na tela
        add(painelBotoes, BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        // As ações de cada botão
        btnListar.addActionListener(e -> atualizarTabela());
        btnRealizarEmprestimo.addActionListener(e -> realizarEmprestimo());
        btnDevolucao.addActionListener(e -> realizarDevolucao());
        btnBuscarPorCodigo.addActionListener(e -> buscarEmprestimo());
        btnRenovar.addActionListener(e -> renovarEmprestimo());
        
        atualizarTabela();
    }

    private void atualizarTabela() {
        modeloTabela.setRowCount(0);
        try {
            ArrayList<Emprestimo> emprestimos = EmprestimoController.getListaDeEmprestimos();
            for (Emprestimo emp : emprestimos) {
                modeloTabela.addRow(new Object[]{
                    emp.getCodEmprestimo(),
                    emp.getLivro().getNome(),
                    emp.getCliente().getNome(),
                    emp.getDataEmprestimo(),
                    emp.getDataDevolucaoPrevista(),
                    emp.getRenovacoes()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar empréstimos: " + e.getMessage(), "Erro de Banco", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void realizarEmprestimo() {
        try {
            int idCliente = Integer.parseInt(JOptionPane.showInputDialog(this, "Registro do Cliente:"));
            int idLivro = Integer.parseInt(JOptionPane.showInputDialog(this, "Código do Livro:"));
            int idFuncionario = Integer.parseInt(JOptionPane.showInputDialog(this, "Código do Funcionário:"));

            Cliente cliente = ClienteController.buscarCliente(idCliente);
            if (cliente == null) throw new IllegalArgumentException("Cliente " + idCliente + " não encontrado.");

            Livro livro = LivroController.buscarLivroPorCodigo(idLivro);
            if (livro == null) throw new IllegalArgumentException("Livro " + idLivro + " não encontrado.");
            
            if (livro.getDisponiveis() <= 0) {
                throw new IllegalArgumentException("Livro '" + livro.getNome() + "' indisponível (0 exemplares).");
            }
            
            Funcionario func = FuncionarioController.buscarFuncionario(idFuncionario);
            if (func == null) throw new IllegalArgumentException("Funcionário " + idFuncionario + " não encontrado.");
            
            Emprestimo novoEmprestimo = new Emprestimo(livro, cliente, func);
            EmprestimoController.realizarEmprestimo(novoEmprestimo);
            
            JOptionPane.showMessageDialog(this, "Empréstimo realizado com sucesso!");
            atualizarTabela();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Código/Registro deve ser um número.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro de banco de dados: " + e.getMessage(), "Erro de SQL", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Erro de validação: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro inesperado: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void realizarDevolucao() {
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um empréstimo na tabela.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int codEmprestimo = (int) modeloTabela.getValueAt(linhaSelecionada, 0);
        int resposta = JOptionPane.showConfirmDialog(this, "Confirmar devolução (remoção) do empréstimo " + codEmprestimo + "?", "Confirmar", JOptionPane.YES_NO_OPTION);

        if (resposta == JOptionPane.YES_OPTION) {
            try {
                boolean sucesso = EmprestimoController.removerEmprestimo(codEmprestimo);
                if (sucesso) {
                    JOptionPane.showMessageDialog(this, "Devolução (remoção) realizada com sucesso!");
                    atualizarTabela();
                } else {
                    JOptionPane.showMessageDialog(this, "Erro: Empréstimo não encontrado (ID: " + codEmprestimo + ").", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erro de banco de dados: " + e.getMessage(), "Erro de SQL", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void buscarEmprestimo() {
        try {
            int codigo = Integer.parseInt(JOptionPane.showInputDialog(this, "Digite o código do empréstimo:"));
            Emprestimo emp = EmprestimoController.buscarEmprestimoPorId(codigo);
            
            if (emp != null) {
                StringBuilder mensagem = new StringBuilder();
                mensagem.append("--- Empréstimo Encontrado ---\n");
                mensagem.append("Cód. Empréstimo: ").append(emp.getCodEmprestimo()).append("\n");
                JTextArea textArea = new JTextArea(mensagem.toString());
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(400, 300));
                JOptionPane.showMessageDialog(this, scrollPane, "Resultado da Busca", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Nenhum empréstimo encontrado com o código " + codigo, "Não Encontrado", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "O código deve ser um número.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro de banco de dados: " + e.getMessage(), "Erro de SQL", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void renovarEmprestimo() {
        try {
            String strCodigo = JOptionPane.showInputDialog(this, "Digite o CÓDIGO do empréstimo que deseja renovar:");
            if (strCodigo == null || strCodigo.trim().isEmpty()) {
                return;
            }
            int codEmprestimo = Integer.parseInt(strCodigo);

            int resposta = JOptionPane.showConfirmDialog(this, 
                "Deseja realmente renovar o empréstimo " + codEmprestimo + "?\n(A data de devolução será 15 dias a partir de hoje)", 
                "Confirmar Renovação", 
                JOptionPane.YES_NO_OPTION);

            if (resposta == JOptionPane.YES_OPTION) {
                boolean sucesso = EmprestimoController.renovarEmprestimo(codEmprestimo);

                if (sucesso) {
                    JOptionPane.showMessageDialog(this, "Empréstimo " + codEmprestimo + " renovado com sucesso!");
                    atualizarTabela(); 
                } else {
                    JOptionPane.showMessageDialog(this, "Não foi possível renovar o empréstimo.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException e) {
            // Erro se o usuário digitar algo que não seja um número
            JOptionPane.showMessageDialog(this, "O código deve ser um número.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            // Erro de banco de dados
            JOptionPane.showMessageDialog(this, "Erro de banco de dados: " + e.getMessage(), "Erro de SQL", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            // Erro de lógica de negócio, tipo se não encontrar o empŕestimo, se o limite de renovações ja tiver sido atingido...
            JOptionPane.showMessageDialog(this, "Não foi possível renovar: " + e.getMessage(), "Regra de Negócio", JOptionPane.WARNING_MESSAGE);
        }
    }
}