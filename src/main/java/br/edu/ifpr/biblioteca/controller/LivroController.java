package br.edu.ifpr.biblioteca.controller;

import br.edu.ifpr.biblioteca.model.Livro;
import br.edu.ifpr.biblioteca.model.dao.LivroDAO;

public class LivroController {
    private LivroDAO dao;

    public LivroController() {
        this.dao = new LivroDAO();
    }

    public void cadastrarLivro(Livro livro){
        if(livro.getNome() == null  || livro.getNome().isEmpty()){
            System.out.println("Nome não pode ser vazio");
            return;
        }
        LivroDAO.salvarLivro(livro);        
    }

    public static void removerLivro(int codigo) {
        LivroDAO.removerLivroDAO(codigo);
    }
}
