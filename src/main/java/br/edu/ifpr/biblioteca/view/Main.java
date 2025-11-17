package br.edu.ifpr.biblioteca.view;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {

    public static void main(String[] args) {
        
        try { // Setar o modelo de exibição, tipo, o LookAndFeel é uma aparência
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                TelaPrincipal tela = new TelaPrincipal();
                tela.setVisible(true);
            }
        });
    }
}