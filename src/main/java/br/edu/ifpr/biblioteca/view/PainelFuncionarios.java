package br.edu.ifpr.biblioteca.view;

import br.edu.ifpr.biblioteca.controller.FuncionarioController;
import br.edu.ifpr.biblioteca.model.Funcionario;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class PainelFuncionarios extends JPanel {

    // Componentes dessa aba
    private JButton btnCadastrar, btnListar, btnRemover, btnBuscarPorCodigo; 
    private JTable tabela;
    private DefaultTableModel modeloTabela;

    public PainelFuncionarios() {
        setLayout(new BorderLayout());

        // Painel de botões
        JPanel painelBotoes = new JPanel();

        btnCadastrar = new JButton("Cadastrar Novo Funcionário");
        btnListar = new JButton("Listar/Atualizar Funcionários");
        btnRemover = new JButton("Remover Funcionário Selecionado");
        btnBuscarPorCodigo = new JButton("Buscar por Código"); 

        painelBotoes.add(btnCadastrar);
        painelBotoes.add(btnListar);
        painelBotoes.add(btnRemover);
        painelBotoes.add(btnBuscarPorCodigo); 

        // Tabela
        String[] colunas = { "Código", "Nome", "Salário" };
        modeloTabela = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabela = new JTable(modeloTabela);

        // Vai adicionar os paineis na tela
        add(painelBotoes, BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        // As ações dos botões
        btnListar.addActionListener(e -> atualizarTabela());
        btnCadastrar.addActionListener(e -> cadastrarFuncionario());
        btnRemover.addActionListener(e -> removerFuncionario());
        btnBuscarPorCodigo.addActionListener(e -> buscarFuncionario()); 

        atualizarTabela(); 
    }

    private void atualizarTabela() {
        modeloTabela.setRowCount(0);
        try {
            ArrayList<Funcionario> funcionarios = FuncionarioController.listarFuncionarios();
            for (Funcionario func : funcionarios) {
                modeloTabela.addRow(new Object[] {
                        func.getCodigo(),
                        func.getNome(),
                        func.getSalario()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar funcionários: " + e.getMessage(), "Erro de Banco",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cadastrarFuncionario() {
        try {
            String nome = JOptionPane.showInputDialog(this, "Nome do funcionário:");
            float salario = Float.parseFloat(JOptionPane.showInputDialog(this, "Salário (ex: 3000.50):"));

            Funcionario novoFuncionario = new Funcionario(nome, salario);
            FuncionarioController.cadastrarFuncionario(novoFuncionario);

            JOptionPane.showMessageDialog(this, "Funcionário cadastrado com sucesso!");
            atualizarTabela();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Salário deve ser um número.", "Erro de Formato",
                    JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar no banco: " + e.getMessage(), "Erro de SQL",
                    JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Erro de validação: " + e.getMessage(), "Erro",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro inesperado: " + e.getMessage(), "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removerFuncionario() {
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um funcionário na tabela.", "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int codigo = (int) modeloTabela.getValueAt(linhaSelecionada, 0);
        int resposta = JOptionPane.showConfirmDialog(this, "Tem certeza?", "Confirmar Remoção",
                JOptionPane.YES_NO_OPTION);

        if (resposta == JOptionPane.YES_OPTION) {
            try {
                boolean sucesso = FuncionarioController.removerFuncionario(codigo);
                if (sucesso) {
                    JOptionPane.showMessageDialog(this, "Funcionário removido com sucesso!");
                    atualizarTabela();
                } else {
                    JOptionPane.showMessageDialog(this, "Erro: Funcionário não encontrado (ID: " + codigo + ").",
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erro de banco de dados: " + e.getMessage(), "Erro de SQL",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void buscarFuncionario() {
        try {
            int codigo = Integer.parseInt(JOptionPane.showInputDialog(this, "Digite o código do funcionário:"));

            Funcionario func = FuncionarioController.buscarFuncionario(codigo);

            if (func != null) {
                String mensagem = "Funcionário Encontrado:\n\n" +
                        "Código: " + func.getCodigo() + "\n" +
                        "Nome: " + func.getNome() + "\n" +
                        "Salário: R$ " + func.getSalario();
                JOptionPane.showMessageDialog(this, mensagem, "Resultado da Busca", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Nenhum funcionário encontrado com o código " + codigo,
                        "Não Encontrado", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "O código deve ser um número.", "Erro de Formato",
                    JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro de banco de dados: " + e.getMessage(), "Erro de SQL",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}