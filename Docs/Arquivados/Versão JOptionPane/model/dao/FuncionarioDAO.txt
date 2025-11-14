package br.edu.ifpr.biblioteca.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import br.edu.ifpr.biblioteca.model.Funcionario;

public class FuncionarioDAO {

    public static boolean verificarFuncionarioExistente(int idFuncionario) {
        String sql = "SELECT EXISTS (SELECT 1 FROM Funcionario WHERE idFuncionario = ?)";

        // Todos os recursos abertos aqui (con, ps, rs) serão fechados automaticamente
        try (Connection con = ConnectionFactory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idFuncionario);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt(1) == 1;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void cadastrarFuncionarioDAO(Funcionario f) {
        String sql = "INSERT INTO Funcionario (nome, salario) VALUES (?,?)";

        try (Connection con = ConnectionFactory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, f.getNome());
            ps.setFloat(2, f.getSalario());

            ps.executeUpdate();
            ps.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Funcionario> listarFuncionariosDAO() {
        ArrayList<Funcionario> lista = new ArrayList<>();
        String sql = "SELECT * FROM Funcionario";

        try (Connection con = ConnectionFactory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Funcionario f = new Funcionario();
                f.setCodigo(rs.getInt("idFuncionario"));
                f.setNome(rs.getString("nome"));
                f.setSalario(rs.getFloat("salario"));
                lista.add(f);
            }

            rs.close();
            ps.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public static Funcionario buscarFuncionarioDAO(int codigo) {
        String sql = "SELECT * FROM Funcionario WHERE idFuncionario = ?";

        try (Connection con = ConnectionFactory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, codigo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Funcionario f = new Funcionario();
                f.setCodigo(rs.getInt("idFuncionario"));
                f.setNome(rs.getString("nome"));
                f.setSalario(rs.getFloat("salario"));
                return f;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void removerFuncionarioDAO(int codigo) {
        String sql = "DELETE FROM Funcionario WHERE idFuncionario = ?";

        try (Connection con = ConnectionFactory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, codigo);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
