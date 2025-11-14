package br.edu.ifpr.biblioteca.model;

import java.time.LocalDate;

import br.edu.ifpr.biblioteca.view.View;

import java.io.Serializable;

public class Emprestimo implements Serializable {
    private Livro livro;
    private Cliente cliente;
    private Funcionario funcionario;
    private int codEmprestimo; // gerado pelo banco (auto increment)
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucaoPrevista;
    private LocalDate dataDevolucaoEfetiva;
    private boolean devolvido;
    private int renovacoes;

    public Emprestimo(Livro livro, Cliente cliente, Funcionario funcionario) {
        this.livro = livro;
        this.cliente = cliente;
        this.funcionario = funcionario;
        this.dataEmprestimo = LocalDate.now();
        this.dataDevolucaoPrevista = dataEmprestimo.plusDays(15);
        this.devolvido = false;
        this.renovacoes = 0;
    }

    public Emprestimo() {
        
    }

    public void setDataEmprestimo(LocalDate dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }
    
    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    public int getCodEmprestimo() {
        return codEmprestimo;
    }

    public void setCodEmprestimo(int codEmprestimo) {
        this.codEmprestimo = codEmprestimo;
    }

    public Livro getLivro() {
        return livro;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public LocalDate getDataDevolucaoPrevista() {
        return dataDevolucaoPrevista;
    }

    public LocalDate setDataDevolucaoPrevista(LocalDate dataDevolucaoPrevista) {
        return this.dataDevolucaoPrevista = dataDevolucaoPrevista;
    }

    public LocalDate getDataDevolucaoEfetiva() {
        return dataDevolucaoEfetiva;
    }

    public void setDataDevolucaoEfetiva(LocalDate dataDevolucaoEfetiva) {
        this.dataDevolucaoEfetiva = dataDevolucaoEfetiva;
    }

    public boolean isDevolvido() {
        return devolvido;
    }

    public void setDevolvido(boolean devolvido) {
        this.devolvido = devolvido;
    }

    public int getRenovacoes() {
        return renovacoes;
    }

    public void setRenovacoes(int renovacoes) {
        this.renovacoes = renovacoes;
    }

    public void imprimirEmprestimo(Emprestimo emp) {
        String mensagem = "Código: " + emp.codEmprestimo + "\nLivro: " + emp.livro.getNome() + " (" + emp.livro.getAutor() + ")\nCliente: " + emp.cliente.getNome()+" | Funcionario: " + emp.funcionario.getNome() + "\nData Empréstimo: " + emp.dataEmprestimo + " | Data Devolução Prevista: " + emp.dataDevolucaoPrevista + "\nRenovações: " + emp.renovacoes + " | Devolvido: " + emp.isDevolvido();
        View.imprimirMensagemSaltada(mensagem);
    }

}
