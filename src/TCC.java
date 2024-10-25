import java.sql.Timestamp;
public class TCC {
    private int id;
    private String alunoNome;
    private String curso;
    private int ano;
    private String orientador;
    private String arquivoPath;
    private Timestamp dataRegistro;
    


    // Construtores, Getters e Setters
    public TCC(String alunoNome, String curso, int ano, String orientador, String arquivoPath) {
        this.alunoNome = alunoNome;
        this.curso = curso;
        this.ano = ano;
        this.orientador = orientador;
        this.arquivoPath = arquivoPath;
    }

    // Getters e Setters omitidos por brevidade
    public String getAlunoNome() {
        return alunoNome;
    }

    public String getCurso() {
        return curso;
    }

    public int getAno() {
        return ano;
    }

    public String getOrientador() {
        return orientador;
    }

    public String getArquivoPath() {
        return arquivoPath;
    }

    public Timestamp getDataRegistro() {
        return dataRegistro;
    }

    public int getId() {
        return id;
    }


}
