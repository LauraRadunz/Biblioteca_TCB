package br.edu.ifpr.biblioteca.controller;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import br.edu.ifpr.biblioteca.model.Emprestimo;
import br.edu.ifpr.biblioteca.model.Livro; 
import br.edu.ifpr.biblioteca.model.dao.EmprestimoDAO;
import br.edu.ifpr.biblioteca.model.dao.LivroDAO; 

public class EmprestimoController {
    
    public static void realizarEmprestimo(Emprestimo emprestimo) throws SQLException, IllegalArgumentException {
        if (emprestimo == null) {
            throw new IllegalArgumentException("Empréstimo não pode ser nulo!");
        }
        
        Livro livro = emprestimo.getLivro();
        
        if (livro.getDisponiveis() <= 0) {
            throw new IllegalArgumentException("Livro '" + livro.getNome() + "' indisponível (0 exemplares).");
        }
        
        EmprestimoDAO.inserirEmprestimo(emprestimo);
        
        livro.emprestar(); // Atualiza o objeto (emprestados++)
        LivroDAO.atualizarEmprestados(livro); // Salva no banco
    }

    public static ArrayList<Emprestimo> getListaDeEmprestimos() throws SQLException {
        return EmprestimoDAO.listarEmprestimos();
    }

    public static boolean removerEmprestimo(int idEmprestimo) throws SQLException {
        Emprestimo emp = EmprestimoDAO.buscarPorIdDAO(idEmprestimo);
        if (emp == null) {
            return false; // Empréstimo não existe
        }

        boolean sucesso = EmprestimoDAO.removerEmprestimoDAO(idEmprestimo);
        
        if (sucesso) {
            Livro livro = emp.getLivro();
            livro.devolver(); // Atualiza o objeto (emprestados--)
            LivroDAO.atualizarEmprestados(livro); // Salva no banco
        }
        
        return sucesso;
    }
    
    public static Emprestimo buscarEmprestimoPorId(int idEmprestimo) throws SQLException {
        return EmprestimoDAO.buscarPorIdDAO(idEmprestimo);
    }
    
    public static ArrayList<Emprestimo> buscarEmprestimosPorCliente(int idCliente) throws SQLException {
        return EmprestimoDAO.buscarPorClienteDAO(idCliente);
    }

    public static boolean renovarEmprestimo(int idEmprestimo) throws SQLException, Exception {
        final int LIMITE_RENOVACOES = 3;

        // 1. Busca o empréstimo atual no banco
        Emprestimo emp = EmprestimoDAO.buscarPorIdDAO(idEmprestimo);

        if (emp == null) {
            throw new Exception("Empréstimo não encontrado.");
        }

        // 2. Verifica as regras de negócio
        if (emp.isDevolvido()) {
            throw new Exception("Não é possível renovar um empréstimo que já foi devolvido.");
        }

        if (emp.getRenovacoes() >= LIMITE_RENOVACOES) {
            throw new Exception("Limite de " + LIMITE_RENOVACOES + " renovações já foi atingido.");
        }

        emp.setRenovacoes(emp.getRenovacoes() + 1);
        emp.setDataDevolucaoPrevista(LocalDate.now().plusDays(15));

        return EmprestimoDAO.atualizarEmprestimoDAO(emp);
    }
}