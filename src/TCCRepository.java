import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;


public class TCCRepository {

    public void cadastrarTCC(TCC tcc) {
        String sql = "INSERT INTO tccs (aluno_nome, curso, ano, orientador, arquivo_path) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, tcc.getAlunoNome());
            stmt.setString(2, tcc.getCurso());
            stmt.setInt(3, tcc.getAno());
            stmt.setString(4, tcc.getOrientador());
            stmt.setString(5, tcc.getArquivoPath());
            
            stmt.executeUpdate();
            System.out.println("TCC cadastrado com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    
    // Método para buscar TCC por nome do aluno
    public void buscarTCCPorAluno(String alunoNome) {
        String sql = "SELECT * FROM tccs WHERE aluno_nome = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, alunoNome);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                System.out.println("TCC de: " + rs.getString("aluno_nome"));
                System.out.println("Curso: " + rs.getString("curso"));
                System.out.println("Ano: " + rs.getInt("ano"));
                System.out.println("Orientador: " + rs.getString("orientador"));
                System.out.println("Arquivo: " + rs.getString("arquivo_path"));
                System.out.println("------------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
    

