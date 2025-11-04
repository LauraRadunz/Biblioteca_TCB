package br.edu.ifpr.biblioteca.model;

package Java;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.Normalizer;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import br.edu.ifpr.biblioteca.controller.LivroController;

public class CadastroLivros {
    private static final String ARQUIVO = "livros.dat";

    private ArrayList<Livro> livros = new ArrayList<>();    

    public CadastroLivros() {
        carregarLivros();
    }

    public void cadastrarLivros(Livro x) {
        livros.add(x);
        cadastrarLivrosBD(x);
    }

    public CadastroLivros(boolean carregar) {
        if (carregar) {
            carregarLivros();
        } else {
            livros = new ArrayList<>();
        }
    }

    public Livro buscarLivros(int codigo) {
        for (Livro livro : livros) {
            if (livro.getCodigo() == codigo) {
                return livro; // Retorna o livro com o código correspondente
            }
        }
        return null; // Se não existe um livro cadastrado com esse código retorna nulo
    }

    public void adicionarSemSalvar(Livro x) {
        livros.add(x);
    }

    public CadastroLivros buscarLivrosPorTitulo(String palavra) {
        // cria um CadastroLivros public class CadastroLivros {
    
}
vazio (não carrega do arquivo)
        CadastroLivros encontrados = new CadastroLivros(false); // <<-- aqui está a diferença

        if (palavra == null) {
            return encontrados;
        }

        String consulta = normalize(palavra).trim().toLowerCase();

        for (Livro livro : livros) {
            if (livro == null) continue;
            String titulo = livro.getNome();
            if (titulo == null) continue;

            String tituloNormalizado = normalize(titulo).toLowerCase();

            if (tituloNormalizado.contains(consulta)) {
                encontrados.adicionarSemSalvar(livro); // adiciona só os que batem
            }
        }
        return encontrados;
    }

    // Normaliza e remove acentos 
    private static String normalize(String s) {
        if (s == null) return "";
        String n = Normalizer.normalize(s, Normalizer.Form.NFD);
        return n.replaceAll("\\p{M}", ""); // remove marcas de acento
    }

    public void listarLivros(String tipo) {
        StringBuilder mensagem = new StringBuilder(tipo+":\n");
        for (Livro livro : livros) {
            mensagem.append("Código: ").append(livro.getCodigo())
            .append(" | Título: ").append(livro.getNome()).append(" | Disponíveis: ").append(livro.getDisponiveis())
            .append("\n");        }
        JOptionPane.showMessageDialog(null, mensagem);
    }

    public void removerLivro(int codigoLivro) {
        Livro livro = buscarLivros(codigoLivro);
        if (livro != null) {
            livros.remove(livro);
            salvarLivros();
            JOptionPane.showMessageDialog(null, "Livro removido com sucesso.");
            return; // remoção bem-sucedida
        }
        JOptionPane.showMessageDialog(null, "Livro com código " + codigoLivro + " não encontrado.");
        return; // não encontrado
    }

     // ---- Persistência ----
    private void salvarLivros() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO))) {
            oos.writeObject(livros);

            // Ajusta o contador para continuar do último código
            if (!livros.isEmpty()) {
                int ultimoCodigo = livros.get(livros.size() - 1).getCodigo();
                Livro.setContador(ultimoCodigo + 1);
            }
        } catch (IOException l) {
            l.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void carregarLivros() {
        File arquivo = new File(ARQUIVO);
        if (arquivo.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
                livros = (ArrayList<Livro>) ois.readObject();
            } catch (IOException | ClassNotFoundException l) {
                l.printStackTrace();
            }
        }
    }
}