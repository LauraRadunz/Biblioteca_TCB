package br.edu.ifpr.biblioteca.model;

import java.io.Serializable;

import javax.swing.JOptionPane;

public class Cliente implements Serializable {
    private String nome;
    private int registro;
    private String telefone;
    private String cpf;

    public Cliente(String nome, String telefone, String cpf) {
        this.nome = nome;
        this.telefone = telefone;
        this.cpf = cpf;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Cliente() {
        
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getRegistro() {
        return registro;
    }

    public void setRegistro(int registro) {
        this.registro = registro;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void imprimirCliente(Cliente cliente) {
        String mensagem = "Registro: " + cliente.getRegistro() + " | Nome: " + cliente.getNome() + " | Telefone: " + cliente.getTelefone() + " | CPF: " + cliente.getCpf()+"\n";    
        JOptionPane.showMessageDialog(null, mensagem);
    }
}