package br.edu.ifpr.biblioteca.controller;

import java.sql.SQLException; // Importe!
import java.util.ArrayList;
import br.edu.ifpr.biblioteca.model.Livro;
import br.edu.ifpr.biblioteca.model.dao.LivroDAO;

public class LivroController {

    public void cadastrarLivro(Livro livro) throws SQLException, IllegalArgumentException {
        if(livro.getNome() == null  || livro.getNome().isEmpty()){
            throw new IllegalArgumentException("Nome não pode ser vazio");
        }
        LivroDAO.inserirLivro(livro);
    }

    public static boolean removerLivro(int codigo) throws SQLException {
        return LivroDAO.removerLivroDAO(codigo);
    }

    public static Livro buscarLivroPorCodigo(int codigo) throws SQLException {
        return LivroDAO.buscarLivroPorCodigoDAO(codigo);
    }

    public static ArrayList<Livro> listarLivros() throws SQLException {
        return LivroDAO.listarLivrosDAO();
    }
    

    public static ArrayList<Livro> buscarLivrosPorTitulo(String palavra) throws SQLException {
        return LivroDAO.buscarLivrosPorTituloDAO(palavra);
    }
}