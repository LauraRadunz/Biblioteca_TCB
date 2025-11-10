package br.edu.ifpr.biblioteca.view;
import br.edu.ifpr.biblioteca.controller.LivroController;
import br.edu.ifpr.biblioteca.model.Livro;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class View {
    static Scanner teclado = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"); // Limpar terminal
        menu();
        teclado.close();
    }

    public static void menu() {
        Menus menu = new Menus();
        while (true) {
            int opcao = lerInteiro(menu.getMenuPrincipal());
            if (opcao == 0) {
                break;
            }
            switch (opcao) {
                // case 1 -> {
                //     menuFuncionarios();
                // }

                case 2 -> {
                    menuLivros();
                }

                // case 3 -> {
                //     menuClientes();
                // }

                // case 4 -> {
                //     menuEmprestimos();
                // }              

                default -> {
                    imprimirMensagemSaltada("Opção inválida!");
                    break;
                }
            }
        }
    }


    public static void menuLivros() {
        Menus menu = new Menus();
        while (true) {
            int opcao = lerInteiro(menu.getMenuLivros());
            if (opcao == 0) {
                break;
            }
            switch (opcao) {
                case 1 -> {
                    LivroController controller = new LivroController();
                    Livro livro = lerLivro();
                    controller.cadastrarLivro(livro);
                    break;
                }

                // case 2 -> {
                //     acervo.listarLivros("Livros cadastrados");
                //     break;
                // }

                // case 3 -> {
                //     int codigo = lerInteiro("Digite o código do livro que deseja buscar");
                //     Livro livro = descobrirLivroPorCodigo(codigo);
                //     livro.imprimirLivro(livro);
                //     break;
                // }

                // case 4 -> {
                //     CadastroLivros encontrados = new CadastroLivros();
                //     encontrados = descobrirLivroPorTitulo();
                //     if (encontrados == null) {
                //         imprimirMensagemSaltada("Nenhum livro encontrado com essa palavra no título");
                //         break;
                //     } else {
                //     encontrados.listarLivros("Livros encontrados: ");
                //     }
                //     break;
                // }

                case 5 -> {
                    int codigo = lerInteiro("Digigte o código do livro que deseja remover");
                    LivroController.removerLivro(codigo);
                    break;
                }

                default -> {
                    imprimirMensagemSaltada("Opção inválida!");
                    break;
                }
            }
            
        }
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

    public static void imprimirMensagemSaltada(String mensagem) { // ? Função que mostra cada mensagem saltada na tela,
        // fora do terminal
        JOptionPane.showMessageDialog(null, mensagem);
    }
}
