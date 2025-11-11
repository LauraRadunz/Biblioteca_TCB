package br.edu.ifpr.biblioteca.view;

import br.edu.ifpr.biblioteca.controller.ClienteController;
import br.edu.ifpr.biblioteca.controller.FuncionarioController;
import br.edu.ifpr.biblioteca.controller.LivroController;
import br.edu.ifpr.biblioteca.model.Cliente;
import br.edu.ifpr.biblioteca.model.Funcionario;
import br.edu.ifpr.biblioteca.model.Livro;

import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class View {
    static Scanner teclado = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println(
                "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"); // Limpar
                                                                                                                 // terminal
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
                case 1 -> {
                    menuLivros();
                }

                case 2 -> {
                    menuFuncionarios();
                }

                // case 3 -> {
                // menuClientes();
                // }

                // case 4 -> {
                // menuEmprestimos();
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

                case 2 -> {
                    LivroController.listarLivros("Livros cadastrados");
                    break;
                }

                case 3 -> {
                    int codigo = lerInteiro("Digite o código do livro que deseja buscar");
                    Livro livro = LivroController.buscarLivroPorCodigo(codigo);
                    if (livro != null) {
                        livro.imprimirLivro(livro);
                    } else {
                        imprimirMensagemSaltada("Livro não encontrado");
                    }
                    break;
                }

                case 4 -> {
                    String palavra = lerString("Digite a palavra que deseja buscar no título do livro");
                    ArrayList<Livro> encontrados = new ArrayList<>();
                    encontrados = LivroController.buscarLivrosPorTitulo(palavra);
                    if (encontrados == null) {
                        imprimirMensagemSaltada("Nenhum livro encontrado com essa palavra no título");
                        break;
                    } else {
                        LivroController.imprimirListaDeLivros(encontrados, "Livros encontrados");
                    }
                    break;
                }

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

    public static Livro lerLivro() { // ? Função que lê os dados do livro para poder criar um objeto livro e retorna
                                     // o mesmo
        String nome = lerString("Digite o nome do livro");
        int exemplares = lerInteiro("Digite a quantidade de exemplares desse livro");
        int emprestados = 0; // Como é um cadastro de um livro, não tem como ter sido feito um empréstimo,
                             // então começa com 0
        int ano = lerInteiro("Digite o ano de publicação desse livro");
        String autor = lerString("Digite o autor desse livro");
        Livro livro = new Livro(nome, exemplares, emprestados, ano, autor);
        return livro;
    }

    public static void menuFuncionarios() {
        Menus menu = new Menus();
        while (true) {
            int opcao = lerInteiro(menu.getMenuFuncionarios());
            if (opcao == 0) {
                break;
            }
            switch (opcao) {
                case 1 -> {
                    Funcionario funcionario = lerFuncionario();
                    FuncionarioController.cadastrarFuncionario(funcionario);
                    imprimirMensagemSaltada("Funcionário cadastrado com sucesso!");
                    break;
                }

                case 2 -> {
                    ArrayList<Funcionario> lista = FuncionarioController.listarFuncionarios();
                    StringBuilder mensagem = new StringBuilder("");
                    for (Funcionario f : lista) { mensagem.append("Código: ").append(f.getCodigo()).append(" | Nome: ").append(f.getNome()).append(" | Salário: ").append(f.getSalario()).append("\n");
                    }
                    imprimirMensagemSaltada(mensagem.toString());
                    break;
                }

                case 3 -> {
                    int codigo = lerInteiro("Digite o código do funcionário que deseja buscar");
                    Funcionario funcionario = FuncionarioController.buscarFuncionario(codigo);
                    String info = "Código: " + funcionario.getCodigo() + " | Nome: " + funcionario.getNome() + " | Salário: " + funcionario.getSalario();
                    imprimirMensagemSaltada(info);
                    break;
                }

                case 4 -> {
                    int codigo = lerInteiro("Digite o código do funcionário que deseja remover");
                    FuncionarioController.removerFuncionario(codigo);
                    imprimirMensagemSaltada("Funcionário removido (se existia).");                    
                    break;
                }

                default -> {
                    imprimirMensagemSaltada("Opção inválida!");
                    break;
                }
            }
        }
    }

    public static Funcionario lerFuncionario() { // ? Função que lê os dados do funcionário para poder criar um objeto
                                                 // funcionário e retorna o mesmo
        String nome = lerString("Digite o nome do funcionário");
        float salario = lerFloat("Digite o salário do funcionário");
        Funcionario funcionario = new Funcionario(nome, salario);
        return funcionario;
    }

    public static void menuClientes() {
        Menus menu = new Menus();
        while (true) {
            int opcao = lerInteiro(menu.getMenuClientes());
            if (opcao == 0) {
                break;
            }
            switch (opcao) {
                case 1 -> {
                    Cliente cliente = lerCliente();
                    ClienteController.cadastrarCliente(cliente);                    
                    imprimirMensagemSaltada("Cliente cadastrado com sucesso!");
                    break;
                }

                case 2 -> {
                    ArrayList<Cliente> lista = ClienteController.listarClientes();
                    StringBuilder mensagem = new StringBuilder("Clientes:\n");
                    for (Cliente c : lista) {
                        mensagem.append("Registro: ").append(c.getRegistro()).append(" | Nome: ").append(c.getNome()).append(" | Telefone: ").append(c.getTelefone()).append(" | CPF: ").append(c.getCpf()).append("\n");
                    }
                    imprimirMensagemSaltada(mensagem.toString());
                    break;
                }

                case 3 -> {
                    int codigo = lerInteiro("Digite o código do cliente que deseja buscar");
                    Cliente cliente = ClienteController.buscarCliente(codigo);
                    if (cliente == null) {
                        imprimirMensagemSaltada("Nenhum cliente encontrado com esse código");
                        break;
                    } else {
                        cliente.imprimirCliente(cliente);
                    }
                    break;
                }

                // case 4 -> {
                //     int codigoCliente = lerInteiro("Digite o código do cliente que deseja buscar os empréstimos em aberto");
                //     CadastroEmprestimos encontrados = new CadastroEmprestimos();
                //     encontrados = emprestimos.buscarEmprestimo(codigoCliente);
                //     if (encontrados == null) {
                //         imprimirMensagemSaltada("Nenhum empréstimo em aberto encontrado para esse cliente");
                //         break;
                //     } else {
                //         encontrados.listarEmprestimos("Empréstimos em aberto para o cliente de código " + codigoCliente);
                //     }
                //     break;
                // }

                case 5 -> {
                    int codigo = lerInteiro("Digite o código do cliente que deseja remover");
                    ClienteController.removerCliente(codigo);
                    break;
                }

                default -> {
                    imprimirMensagemSaltada("Opção inválida!");
                    break;
                }
            }
        }
    }

    public static Cliente lerCliente() {
        String nome = lerString("Digite o nome do cliente");
        String telefone = lerString("Digite o telefone do cliente");
        String cpf = lerString("Digite o CPF do cliente");
        Cliente cliente = new Cliente(nome, telefone, cpf);
        return cliente;
    }

    public static void menuEmprestimos() {
        Menus menu = new Menus();
        while (true) {
            int opcao = lerInteiro(menu.getMenuEmprestimos());

            if (opcao == 0) {
                break;
            }

            switch (opcao) {
                case 1 -> {
                    emprestimos.listarEmprestimos("Empréstimos em aberto");
                    break;
                }

                case 2 -> {
                    Emprestimo emprestimo = lerEmprestimo();
                    emprestimos.realizarEmprestimos(emprestimo);
                    break;
                    }
            
                case 3 -> {
                    int codigoEmprestimo = lerInteiro("Digite o código do empréstimo que deseja devolver");
                    emprestimos.devolverLivro(codigoEmprestimo);
                    break;
                }

                case 4 -> {
                    int codigoEmprestimo = lerInteiro("Digite o código do empréstimo que deseja renovar");
                    emprestimos.renovarEmprestimo(codigoEmprestimo);
                    break;
                }
            
                case 5 -> {
                    int codigoEmprestimo = lerInteiro("Digite o código do empréstimo que deseja especificar");
                    Emprestimo emprestimo = emprestimos.buscarEmprestimoPorCodigo(codigoEmprestimo);
                    if (emprestimo == null) {
                        imprimirMensagemSaltada("Nenhum empréstimo encontrado com esse código");
                        break;
                    } else {
                        emprestimo.imprimirEmprestimo(emprestimo);
                    }
                    break;
                }
        
                case 6 -> {
                    int codigoCliente = lerInteiro("Digite o código do cliente que deseja buscar os empréstimos em aberto");
                    CadastroEmprestimos encontrados = new CadastroEmprestimos();
                    encontrados = emprestimos.buscarEmprestimo(codigoCliente);
                    if (encontrados == null) {
                        imprimirMensagemSaltada("Nenhum empréstimo em aberto encontrado para esse cliente");
                        break;
                    } else {
                        encontrados.listarEmprestimos("Empréstimos em aberto para o cliente de código " + codigoCliente);
                    }
                    break;
                }
    
                case 7 -> {
                    int codigoEmprestimo = lerInteiro("Digite o código do empréstimo que deseja remover");
                    emprestimos.removerEmprestimo(codigoEmprestimo);
                    break;
                }

                default -> {
                    imprimirMensagemSaltada("Opção inválida!");
                    break;
                }
            }
        }
    }

    public static Emprestimo lerEmprestimo() {
        int codigoCliente = lerInteiro("Digite o código do cliente que está realizando o empréstimo");
        Cliente cliente = clientes.buscarCliente(codigoCliente);
        int codigoFuncionario = lerInteiro("Digite o código do funcionário que está realizando o empréstimo");
        Funcionario funcionario = descobrirFuncionario(codigoFuncionario);
        int codigoLivro = lerInteiro("Digite o código do livro que deseja emprestar");
        Livro livro = descobrirLivroPorCodigo(codigoLivro);
        Emprestimo emprestimo = new Emprestimo(livro, cliente, funcionario);
        return emprestimo;
    }

    public static float lerFloat(String pergunta) { // ? Onde saltará uma caixa de pergunta na tela, oq for digitado
        // será convertido e retornado no modelo inteiro
        String resposta = JOptionPane.showInputDialog(pergunta).trim();

        float n = Float.parseFloat(resposta);
        return n;
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
