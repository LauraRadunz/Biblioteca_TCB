package br.edu.ifpr.biblioteca.view;

import br.edu.ifpr.biblioteca.controller.LivroController;
import br.edu.ifpr.biblioteca.model.Livro;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class PainelLivros extends JPanel {

    // Componentes dessa aba
    private JButton btnCadastrar, btnListarTudo, btnRemover, btnBuscarPorCodigo, btnBuscarPorTitulo;
    private JTable tabela;
    private DefaultTableModel modeloTabela;
    

    private LivroController livroController; // Tem q instanciar porque o meu cadastrar livro não é estśatico

    public PainelLivros() {
        // Instancia o controller
        livroController = new LivroController();
        
        // Define o layout
        setLayout(new BorderLayout());

        // Botões
        JPanel painelBotoes = new JPanel();
        
        btnCadastrar = new JButton("Cadastrar Novo Livro");
        btnListarTudo = new JButton("Listar Tudo");
        btnRemover = new JButton("Remover Livro Selecionado");
        btnBuscarPorCodigo = new JButton("Buscar por Código");
        btnBuscarPorTitulo = new JButton("Buscar por Título");

        painelBotoes.add(btnCadastrar);
        painelBotoes.add(btnListarTudo);
        painelBotoes.add(btnRemover);
        painelBotoes.add(btnBuscarPorCodigo);
        painelBotoes.add(btnBuscarPorTitulo);

        // Tabela 
        String[] colunas = {"Código", "Nome", "Autor", "Ano", "Exemplares", "Emprestados", "Disponíveis"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Fala que não pode editar a tabela
            }
        };
        tabela = new JTable(modeloTabela);

        // para adicionar os paineis na tela
        add(painelBotoes, BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        // As ações dos botões
        btnListarTudo.addActionListener(e -> atualizarTabelaComListaCompleta());
        btnCadastrar.addActionListener(e -> cadastrarLivro());
        btnRemover.addActionListener(e -> removerLivro());
        btnBuscarPorCodigo.addActionListener(e -> buscarLivroPorCodigo());
        btnBuscarPorTitulo.addActionListener(e -> buscarLivroPorTitulo());
        
        atualizarTabelaComListaCompleta(); 
    }

    private void popularTabela(ArrayList<Livro> livros) { // Vai adicionar os livros pegos de uma função na tabela
        modeloTabela.setRowCount(0); // Limpa a tabela
        
        for (Livro livro : livros) {
            modeloTabela.addRow(new Object[]{
                livro.getCodigo(),        //
                livro.getNome(),          //
                livro.getAutor(),         //
                livro.getAno(),           //
                livro.getExemplares(),    //
                livro.getEmprestados(),   //
                livro.getDisponiveis()    //
            });
        }
    }


    private void atualizarTabelaComListaCompleta() {
        try {
            ArrayList<Livro> livros = LivroController.listarLivros();
            popularTabela(livros); 
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar livros: " + e.getMessage(), "Erro de Banco", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cadastrarLivro() {
        try {
            String nome = JOptionPane.showInputDialog(this, "Nome do livro:");
            String autor = JOptionPane.showInputDialog(this, "Autor:");
            int ano = Integer.parseInt(JOptionPane.showInputDialog(this, "Ano:"));
            int exemplares = Integer.parseInt(JOptionPane.showInputDialog(this, "Qtd. Exemplares:"));

            if (nome == null || nome.trim().isEmpty()) {
                throw new IllegalArgumentException("Nome não pode ser vazio");
            }

            Livro novoLivro = new Livro(nome, exemplares, 0, ano, autor);
            
            livroController.cadastrarLivro(novoLivro); 
            
            JOptionPane.showMessageDialog(this, "Livro cadastrado com sucesso!");
            atualizarTabelaComListaCompleta();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ano e Exemplares devem ser números.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar no banco: " + e.getMessage(), "Erro de SQL", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Erro de validação: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro inesperado: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removerLivro() {
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um livro na tabela.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int codigo = (int) modeloTabela.getValueAt(linhaSelecionada, 0);
        int resposta = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja remover o livro " + codigo + "?", "Confirmar Remoção", JOptionPane.YES_NO_OPTION);

        if (resposta == JOptionPane.YES_OPTION) {
            try {
                boolean sucesso = LivroController.removerLivro(codigo); 
                
                if (sucesso) {
                    JOptionPane.showMessageDialog(this, "Livro removido com sucesso!");
                    atualizarTabelaComListaCompleta();
                } else {
                    JOptionPane.showMessageDialog(this, "Erro: Livro não encontrado (ID: " + codigo + ").", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erro de banco de dados: " + e.getMessage(), "Erro de SQL", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void buscarLivroPorCodigo() {
        try {
            int codigo = Integer.parseInt(JOptionPane.showInputDialog(this, "Digite o código do livro:"));
            
            Livro livro = LivroController.buscarLivroPorCodigo(codigo);
            
            if (livro != null) {
                String mensagem = "Livro Encontrado:\n\n" +
                                  "Código: " + livro.getCodigo() + "\n" +
                                  "Nome: " + livro.getNome() + "\n" +
                                  "Autor: " + livro.getAutor() + "\n" +
                                  "Ano: " + livro.getAno() + "\n" +
                                  "Exemplares: " + livro.getExemplares() + "\n" +
                                  "Emprestados: " + livro.getEmprestados() + "\n" +
                                  "Disponíveis: " + livro.getDisponiveis();
                JOptionPane.showMessageDialog(this, mensagem, "Resultado da Busca", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Nenhum livro encontrado com o código " + codigo, "Não Encontrado", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "O código deve ser um número.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro de banco de dados: " + e.getMessage(), "Erro de SQL", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void buscarLivroPorTitulo() {
        try {
            String palavra = JOptionPane.showInputDialog(this, "Digite uma palavra do título:");
            if (palavra == null || palavra.trim().isEmpty()) {
                return;
            }
            
            ArrayList<Livro> livros = LivroController.buscarLivrosPorTitulo(palavra);
            
            if (livros.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nenhum livro encontrado com o termo '" + palavra + "'", "Não Encontrado", JOptionPane.WARNING_MESSAGE);
            } else {
                popularTabela(livros);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro de banco de dados: " + e.getMessage(), "Erro de SQL", JOptionPane.ERROR_MESSAGE);
        }
    }
}