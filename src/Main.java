import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TCCRepository repository = new TCCRepository();

        while (true) {
            System.out.println("1. Cadastrar TCC");
            System.out.println("2. Buscar TCC por nome do aluno");
            System.out.println("3. Sair");
            int opcao = scanner.nextInt();
            scanner.nextLine(); // Limpar buffer

            if (opcao == 1) {
                System.out.print("Nome do aluno: ");
                String alunoNome = scanner.nextLine();

                System.out.print("Curso: ");
                String curso = scanner.nextLine();

                System.out.print("Ano de conclusão: ");
                int ano = scanner.nextInt();
                scanner.nextLine(); // Limpar buffer

                System.out.print("Orientador: ");
                String orientador = scanner.nextLine();

                System.out.print("Caminho do arquivo: ");
                String arquivoPath = scanner.nextLine();

                TCC tcc = new TCC(alunoNome, curso, ano, orientador, arquivoPath);
                repository.cadastrarTCC(tcc);

            } else if (opcao == 2) {
                System.out.print("Nome do aluno: ");
                String alunoNome = scanner.nextLine();
                repository.buscarTCCPorAluno(alunoNome);

            } else if (opcao == 3) {
                break;
            }
        }

        scanner.close();
    }
}
