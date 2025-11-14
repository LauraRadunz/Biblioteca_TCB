package br.edu.ifpr.biblioteca.view;

import br.edu.ifpr.biblioteca.controller.LivroController;
import br.edu.ifpr.biblioteca.model.Livro;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class PainelLivros extends JPanel {

    // --- Componentes ---
    private JButton btnCadastrar, btnListarTudo, btnRemover, btnBuscarPorCodigo, btnBuscarPorTitulo;
    private JTable tabela;
    private DefaultTableModel modeloTabela;
    
    // --- Controller ---
    // (Precisamos da instância por causa do seu 'cadastrarLivro' não-estático)
    private LivroController livroController; 

    public PainelLivros() {
        // Instancia o controller
        livroController = new LivroController();
        
        // Define o layout
        setLayout(new BorderLayout());

        // --- 1. Painel de Botões (Norte) ---
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

        // --- 2. Tabela de Livros (Centro) ---
        // (Adicionada a coluna "Disponíveis")
        String[] colunas = {"Código", "Nome", "Autor", "Ano", "Exemplares", "Emprestados", "Disponíveis"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Impede edição na tabela
            }
        };
        tabela = new JTable(modeloTabela);

        // --- 3. Adiciona os painéis à tela ---
        add(painelBotoes, BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        // --- 4. Ações dos Botões (COM TRY-CATCH) ---
        btnListarTudo.addActionListener(e -> atualizarTabelaComListaCompleta());
        btnCadastrar.addActionListener(e -> cadastrarLivro());
        btnRemover.addActionListener(e -> removerLivro());
        btnBuscarPorCodigo.addActionListener(e -> buscarLivroPorCodigo());
        btnBuscarPorTitulo.addActionListener(e -> buscarLivroPorTitulo());
        
        // --- 5. Carrega os dados iniciais ---
        atualizarTabelaComListaCompleta(); 
    }

    /**
     * MÉTODO AUXILIAR: Recebe uma lista e a exibe na tabela
     */
    private void popularTabela(ArrayList<Livro> livros) {
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

    /**
     * Busca a lista COMPLETA de livros e chama o popularTabela
     */
    private void atualizarTabelaComListaCompleta() {
        try {
            // Chama o controller 'silencioso'
            ArrayList<Livro> livros = LivroController.listarLivros();
            popularTabela(livros); // Usa o método auxiliar
        } catch (SQLException e) {
            // A View trata o erro!
            JOptionPane.showMessageDialog(this, "Erro ao buscar livros: " + e.getMessage(), "Erro de Banco", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Pede os dados e chama o Controller para cadastrar
     */
    private void cadastrarLivro() {
        try {
            // A View pega os dados
            String nome = JOptionPane.showInputDialog(this, "Nome do livro:");
            String autor = JOptionPane.showInputDialog(this, "Autor:");
            int ano = Integer.parseInt(JOptionPane.showInputDialog(this, "Ano:"));
            int exemplares = Integer.parseInt(JOptionPane.showInputDialog(this, "Qtd. Exemplares:"));

            // (Validação simples)
            if (nome == null || nome.trim().isEmpty()) {
                throw new IllegalArgumentException("Nome não pode ser vazio");
            }

            Livro novoLivro = new Livro(nome, exemplares, 0, ano, autor);
            
            // Chama o controller NÃO-estático
            livroController.cadastrarLivro(novoLivro); 
            
            // A View dá o feedback de SUCESSO!
            JOptionPane.showMessageDialog(this, "Livro cadastrado com sucesso!");
            atualizarTabelaComListaCompleta(); // Atualiza a tabela

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

    /**
     * Pega o livro selecionado e chama o Controller para remover
     */
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
                // Chama o controller 'silencioso' que retorna boolean
                boolean sucesso = LivroController.removerLivro(codigo); 
                
                // A View decide o que mostrar
                if (sucesso) {
                    JOptionPane.showMessageDialog(this, "Livro removido com sucesso!");
                    atualizarTabelaComListaCompleta();
                } else {
                    JOptionPane.showMessageDialog(this, "Erro: Livro não encontrado (ID: " + codigo + ").", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                // A View trata o erro de SQL
                JOptionPane.showMessageDialog(this, "Erro de banco de dados: " + e.getMessage(), "Erro de SQL", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * (NOVA FUNÇÃO) Pede um código e mostra os dados do livro
     */
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
    
    /**
     * (NOVA FUNÇÃO) Pede uma palavra e filtra a tabela com os resultados
     */
    private void buscarLivroPorTitulo() {
        try {
            String palavra = JOptionPane.showInputDialog(this, "Digite uma palavra do título:");
            if (palavra == null || palavra.trim().isEmpty()) {
                return; // Usuário cancelou ou não digitou nada
            }
            
            ArrayList<Livro> livros = LivroController.buscarLivrosPorTitulo(palavra);
            
            if (livros.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nenhum livro encontrado com o termo '" + palavra + "'", "Não Encontrado", JOptionPane.WARNING_MESSAGE);
            } else {
                // ATUALIZA A TABELA com os resultados do filtro!
                popularTabela(livros);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro de banco de dados: " + e.getMessage(), "Erro de SQL", JOptionPane.ERROR_MESSAGE);
        }
    }
}