package br.edu.ifpr.biblioteca.model;

import java.io.Serializable;

public class Funcionario implements Serializable {

    private String nome;
    private float salario;
    private int codigo;

    public Funcionario() {
    }

    public Funcionario(String nome, float salario) {
        this.nome = nome;
        this.salario = salario;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public float getSalario() {
        return salario;
    }

    public void setSalario(float salario) {
        this.salario = salario;
    }

    public int getCodigo() {
        return codigo;
    }
}