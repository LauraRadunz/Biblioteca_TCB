package br.edu.ifpr.biblioteca.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import br.edu.ifpr.biblioteca.model.Emprestimo;
import br.edu.ifpr.biblioteca.model.dao.ClienteDAO;
import br.edu.ifpr.biblioteca.model.dao.EmprestimoDAO;

public class EmprestimoController {
    
    public static void realizarEmprestimo(Emprestimo emprestimo) {
        if (emprestimo != null) {
        EmprestimoDAO.inserirEmprestimo(emprestimo);
        JOptionPane.showMessageDialog(null,"Empréstimo realizado com sucesso!");
        } else {
            JOptionPane.showMessageDialog(null,"Empréstimo ão pode ser nulo! Empréstimo não realizado.");
        }
    }

    public static String listarEmprestimos() {
        ArrayList<Emprestimo> listaEmprestimos = EmprestimoDAO.listarEmprestimos();
        if (listaEmprestimos.isEmpty()) {
            return "Nenhum empréstimo encontrado.";            
        }

        String mensagem = "Empréstimos em aberto:\n";
        for (Emprestimo emp : listaEmprestimos) {
            mensagem += "Código: " + emp.getCodEmprestimo() +
                        ", Cliente: " + emp.getCliente().getNome() +
                        ", Livro: " + emp.getLivro().getNome() +
                        ", Data Empréstimo: " + emp.getDataEmprestimo() +
                        ", Data Devolução Prevista: " + emp.getDataDevolucaoPrevista() +
                        ", Renovacoes: " + emp.getRenovacoes() + "\n";
        }
        return mensagem;
    }

    public static void devolverLivro(int codEmprestimo) {
        boolean verificacao = EmprestimoDAO.verificarEmprestimoExistente(codEmprestimo);
        if (verificacao == false) {
            JOptionPane.showMessageDialog(null, "Código de empréstimo inválido.");
            return;
        }
        
        try {
            String dataString = JOptionPane.showInputDialog("Digite a data de devolução (dd/MM/yyyy):");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dataDigitada = LocalDate.parse(dataString, formatter);
        
            LocalDate dataPrevista = EmprestimoDAO.devolverLivroDAO(codEmprestimo, dataDigitada);
            if (dataDigitada.isAfter(dataPrevista)) {
                long diasAtraso = ChronoUnit.DAYS.between(dataPrevista, dataDigitada);
                double multa = diasAtraso * 0.50;
                JOptionPane.showMessageDialog(null, "Devolução feita com multa de R$" + multa + " por " + diasAtraso + " dias de atraso.");
            } else {
                JOptionPane.showMessageDialog(null, "Devolução realizada no prazo. Sem multa.");
            }
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(null, "Data inválida. Devolução não realizada.");
        }
    }

    public static void renovarLivro(int idEmprestimo) {
        boolean verificacao = EmprestimoDAO.verificarEmprestimoExistente(idEmprestimo);
        if (verificacao == false) {
            JOptionPane.showMessageDialog(null, "Código de empréstimo inválido.");
            return;
        }

        // Buscar informações do empréstimo no banco
        Emprestimo emprestimo = EmprestimoDAO.buscarEmprestimoPorId(idEmprestimo);

        if (emprestimo == null) {
            JOptionPane.showMessageDialog(null, "Empréstimo não encontrado.");
            return;
        }

        if (emprestimo.isDevolvido()) {
            JOptionPane.showMessageDialog(null, "Este empréstimo já foi devolvido. Não é possível renovar.");
            return;
        }

        // Calcular nova data (15 dias a mais)
        LocalDate novaData = emprestimo.getDataDevolucaoPrevista().plusDays(15);
        int novasRenovacoes = emprestimo.getRenovacoes() + 1;

        // Atualizar no banco de dados
        boolean sucesso = EmprestimoDAO.renovarLivroDAO(idEmprestimo, novaData, novasRenovacoes);

        if (sucesso) {
            JOptionPane.showMessageDialog(null, 
                "Empréstimo renovado com sucesso!\nNova data de devolução: " 
                + novaData.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        } else {
            JOptionPane.showMessageDialog(null, "Erro ao renovar o empréstimo.");
        }
    }

    public static Emprestimo buscarEmprestimo(int idEmprestimo) {
        boolean verificacao = EmprestimoDAO.verificarEmprestimoExistente(idEmprestimo);
        if (verificacao == false) {
            JOptionPane.showMessageDialog(null, "Código de empréstimo inválido.");
            return null;            
        }

        Emprestimo emprestimo = EmprestimoDAO.buscarEmprestimoPorId(idEmprestimo);
        if (emprestimo != null) {
            return emprestimo;
        } else {
            return null;
        }
    }

    public static ArrayList<Emprestimo> buscarEmprestimosAbertosPorCliente(int idCliente) {
        boolean verificacao = ClienteDAO.verificarClienteExistente(idCliente);
        if (verificacao == false) {
            JOptionPane.showMessageDialog(null, "Código de cliente inválido.");
            return new ArrayList<>();            
        }
        ArrayList<Emprestimo> emprestimos = EmprestimoDAO.buscarEmprestimosAbertosPorClienteDAO(idCliente);
        if (emprestimos != null && !emprestimos.isEmpty()) {
            return emprestimos;
        } else {
            JOptionPane.showMessageDialog(null, "Nenhum empréstimo encontrado para este cliente.");
            return new ArrayList<>();
        }
    }

    public static void listarEmprestimosDeUmCliente (ArrayList<Emprestimo> listaEmprestimos, int idCliente) {
        boolean verificacao = ClienteDAO.verificarClienteExistente(idCliente);
        if (verificacao) {
            if (listaEmprestimos.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Nenhum empréstimo encontrado para o cliente do id: " + idCliente);
                return;            
            }
    
            String mensagem = "Empréstimos em aberto do cliente do id: " + idCliente + "\n";
            for (Emprestimo emp : listaEmprestimos) {
                mensagem += "\nCódigo: " + emp.getCodEmprestimo() +
                            " | Cliente: " + emp.getCliente().getNome() +
                            " | Livro: " + emp.getLivro().getNome() +
                            " | Data Empréstimo: " + emp.getDataEmprestimo() +
                            " | Data Devolução Prevista: " + emp.getDataDevolucaoPrevista() +
                            " | Renovacoes: " + emp.getRenovacoes() + "\n";
            }
            JOptionPane.showMessageDialog(null, mensagem);
        }
    }

    public static void removerEmprestimo(int idEmprestimo) {
        boolean verificacao = EmprestimoDAO.verificarEmprestimoExistente(idEmprestimo);
        if (verificacao) {
            
            boolean sucesso = EmprestimoDAO.removerEmprestimoDAO(idEmprestimo);
            if (sucesso) {
                JOptionPane.showMessageDialog(null, "Empréstimo removido com sucesso.");
            } else {
                JOptionPane.showMessageDialog(null, "Erro ao remover o empréstimo.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Código de empréstimo inválido.");
        }
    }
}

