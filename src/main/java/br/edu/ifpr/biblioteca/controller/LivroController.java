package br.edu.ifpr.biblioteca.controller;

import br.edu.ifpr.model.Livro;
import br.edu.ifpr.model.dao.LivroDAO;

public class LivroController {
    private LivroDAO dao;

    public LivroController() {
        this.dao = new LivroDAO();
    }

    public void cadastrarLivroBD(Livro livro){
        if(livro.getNome() == null  || livro.getNome().isEmpty()){
            System.out.println("Nome não pode ser vazio");
            return;
        }

        dao.salvar(livro);
        
    }
}
