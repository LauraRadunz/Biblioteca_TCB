package br.edu.ifpr.biblioteca.model;

import javax.swing.JOptionPane;
import java.io.Serializable;

public class Livro implements Serializable{
    private int codigo;
    private String nome;
    private int exemplares;
    private int emprestados;
    private String autor;
    private int ano;

    private static int contador = 2001;

    public static void setContador(int valor) {
        contador = valor;
    }

    public Livro(String nome, int exemplares, int emprestados, int ano, String autor) {
        this.nome = nome;
        this.codigo = codigo + contador;
        this.exemplares = exemplares;
        this.emprestados = emprestados;
        this.ano = ano;
        this.autor = autor;

        contador++;
    }

    public Livro() {
        
    }

    public boolean emprestar() {
        if (emprestados < exemplares) {
            emprestados++;
            return true;
        } else {
            return false;
        }
    }

    public boolean devolver() {
        if (emprestados > 0) {
            emprestados--;
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Devolução não realizada. Nenhum exemplar emprestado.");	
            return false;
        }
    }

    public void imprimirLivro(Livro livro) {
        String mensagem = "Código: " + livro.getCodigo() + " | Nome: " + livro.getNome() + " | Autor: " + livro.getAutor()+"\n" +
                            "Ano: " + livro.getAno() + " | Exemplares: " + livro.getExemplares() + " | Emprestados: " + livro.getEmprestados() + " | Disponíveis: " + livro.getDisponiveis()+"\n";    
        JOptionPane.showMessageDialog(null, mensagem);
    }

    public int getDisponiveis() {
        return exemplares - emprestados;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getCodigo() {
        return codigo;
    }

    public int getExemplares() {
        return exemplares;
    }

    public void setExemplares(int exemplares) {
        this.exemplares = exemplares;
    }

    public int getEmprestados() {
        return emprestados;
    }

    public void setEmprestados(int emprestados) {
        this.emprestados = emprestados;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }
}