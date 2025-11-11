package br.edu.ifpr.biblioteca.controller;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import br.edu.ifpr.biblioteca.model.Livro;
import br.edu.ifpr.biblioteca.model.dao.LivroDAO;

public class LivroController {

    public void cadastrarLivro(Livro livro){
        if(livro.getNome() == null  || livro.getNome().isEmpty()){
            System.out.println("Nome não pode ser vazio");
            return;
        }
        LivroDAO.inserirLivro(livro);        
    }

    public static void removerLivro(int codigo) {
        LivroDAO.removerLivroDAO(codigo);
    }

    public static Livro buscarLivroPorCodigo(int codigo) {
        Livro livro = LivroDAO.buscarLivroPorCodigoDAO(codigo);
        if (livro != null) {
            return livro;
        } else {
            System.out.println("Livro não encontrado");
            return null;
        }
    }

    public static void listarLivros(String tipo) {
    ArrayList<Livro> livros = LivroDAO.listarLivrosDAO();
    imprimirListaDeLivros(livros, tipo);
    }

    public static ArrayList<Livro> buscarLivrosPorTitulo(String palavra) {
        return LivroDAO.buscarLivrosPorTituloDAO(palavra);
    }

    public static void imprimirListaDeLivros(ArrayList<Livro> lista, String tituloMsg) {
        StringBuilder mensagem = new StringBuilder(tituloMsg + ":\n");
        for (Livro livro : lista) {
            mensagem.append("Código: ").append(livro.getCodigo())
                    .append(" | Título: ").append(livro.getNome())
                    .append(" | Disponíveis: ").append(livro.getDisponiveis())
                    .append("\n");
        }
        JOptionPane.showMessageDialog(null, mensagem);
    }

}
