package br.edu.ifpr.biblioteca.view;

public class Menus {
    String menuPrincipal = "Selecione a área que deseja entrar:\n" +
            "0. Sair\n" +
            "1. Livros\n" +
            "2. Funcionários\n" +
            "3. Clientes\n" +
            "4. Empréstimos\n" ;

    String menuFuncionarios = "Selecione a ação que gostaria de realizar:\n" +
            "0. Voltar ao menu principal\n" +
            "1. Cadastrar funcionário\n" +
            "2. Listar funcionários\n" +
            "3. Buscar funcionário pelo código\n" +
            "4. Remover funcionário pelo código";

    String menuLivros = "Selecione a ação que gostaria de realizar:\n" +
            "0. Voltar ao menu principal\n" +
            "1. Cadastrar livro\n" +
            "2. Listar livros\n" +
            "3. Buscar livro pelo código\n" +
            "4. Buscar livro pelo título\n" +
            "5. Remover livro pelo código";

    String menuClientes = "Selecione a ação que gostaria de realizar:\n" +
            "0. Voltar ao menu principal\n" +
            "1. Cadastrar cliente\n" +
            "2. Listar clientes\n" +
            "3. Buscar cliente pelo código\n" +
            "4. Buscar um empréstimo pelo código do cliente\n" +
            "5. Remover cliente pelo código";

    String menuEmprestimos = "Selecione a ação que gostaria de realizar:\n" + 
            "0. Voltar ao menu principal\n" +
            "1. Realizar empréstimo\n" +
            "2. Listar empréstimos em aberto\n" +
            "3. Realizar devolução\n" +
            "4. Realizar renovação\n" +
            "5. Especificar um empréstimo pelo código do empréstimo\n" +
            "6. Buscar um empréstimo pelo código do cliente\n" +
            "7. Remover um empréstimo pelo código do empréstimo";

    public Menus() {
    }

    public String getMenuPrincipal() {
        return menuPrincipal;
    }

    public String getMenuFuncionarios() {
        return menuFuncionarios;
    }
    
    public String getMenuLivros() {
        return menuLivros;
    }
    
    public String getMenuClientes() {
        return menuClientes;
    }
    
    public String getMenuEmprestimos() {
        return menuEmprestimos;
    }
}