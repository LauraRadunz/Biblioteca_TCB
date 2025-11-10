package br.edu.ifpr.biblioteca.view;

import javax.swing.JOptionPane;

import br.edu.ifpr.biblioteca.controller.LivroController;
import br.edu.ifpr.biblioteca.model.Livro;

// import br.edu.ifpr.biblioteca.controller.ContatoController;
// import br.edu.ifpr.agenda.model.Contato;
// import br.edu.ifpr.agenda.model.Endereco;

public class Main {
    public static void main(String[] args) {
        LivroController controller = new LivroController();
        Livro livro = lerLivro();
        controller.cadastrarLivro(livro);

        // ContatoController controller = new ContatoController();
        // contato.setNome("Fulano de Tal aula LPII");
        // contato.setCelular("(45)99999-9898");
        // contato.setEmail("fulano@gmail.com");
        // contato.setEndereco(endereco);
        // controller.cadastrarContato(contato);

    }

    public static Livro lerLivro() { // ? Função que lê os dados do livro para poder criar um objeto livro e retorna o mesmo
        String nome = lerString("Digite o nome do livro");
        int exemplares = lerInteiro("Digite a quantidade de exemplares desse livro");
        int emprestados = 0; // Como é um cadastro de um livro, não tem como ter sido feito um empréstimo, então começa com 0
        int ano = lerInteiro("Digite o ano de publicação desse livro");
        String autor = lerString("Digite o autor desse livro");
        Livro livro = new Livro(nome, exemplares, emprestados, ano, autor);
        return livro;
    }

    public static int lerInteiro(String pergunta) { // ? Onde saltará uma caixa de pergunta na tela, oq for digitado
        // será convertido e retornado no modelo inteiro
        String resposta = JOptionPane.showInputDialog(pergunta).trim();
        int n = Integer.parseInt(resposta);
        return n;
    }


    public static String lerString(String pergunta) { // ? Onde saltará uma caixa de pergunta na tela, oq for digitado
        // será retornado no modelo string
        String n = JOptionPane.showInputDialog(pergunta);
        return n;
    }
}